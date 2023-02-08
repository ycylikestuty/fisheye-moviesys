package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.constants.Constants;
import com.gdesign.fisheyemoviesys.entity.MenuDO;
import com.gdesign.fisheyemoviesys.entity.RoleDO;
import com.gdesign.fisheyemoviesys.entity.UserDO;
import com.gdesign.fisheyemoviesys.entity.UserRoleDO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.UserDTO;
import com.gdesign.fisheyemoviesys.entity.enums.CodeEnum;
import com.gdesign.fisheyemoviesys.entity.enums.DeleteEnum;
import com.gdesign.fisheyemoviesys.entity.param.UserQuery;
import com.gdesign.fisheyemoviesys.mapper.UserMapper;
import com.gdesign.fisheyemoviesys.service.MenuService;
import com.gdesign.fisheyemoviesys.service.RoleService;
import com.gdesign.fisheyemoviesys.service.UserRoleService;
import com.gdesign.fisheyemoviesys.service.UserService;
import com.gdesign.fisheyemoviesys.utils.ConversionUtils;
import com.gdesign.fisheyemoviesys.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ycy
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @Resource
    private UserRoleService userRoleService;

    @Override
    public ResponseMessageDTO<UserDTO> getUserByUserName(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, username)
                .eq(UserDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
        UserDO userDO = this.getOne(queryWrapper);
        //将对用户为空的情况交给spring security处理即在UserDetailServiceImpl处理
//        if(Objects.isNull(userDO)){
//            return ResponseMessageDTO.<UserDTO>builder()
//                    .success(Boolean.FALSE)
//                    .message("用户不存在")
//                    .code(ErrorCodeEnum.QUERY_CODE.getCode())
//                    .build();
//        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDO, userDTO);
        return ResponseMessageDTO.success(userDTO);
    }

    @Override
    public String getUserAuthorityByUserName(String username) {
        //根据账号获取用户角色和操作权限
        UserDTO userDTO = (UserDTO) redisUtil.get("User:" + username);
        String authority = "";
        if (redisUtil.hasKey("GrantedAuthority:" + userDTO.getUsername())) {
            authority = redisUtil.get("GrantedAuthority:" + userDTO.getUsername()).toString();
        } else {
            //从 user-role 表中根据 登录用户id 获取 角色id
            LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<RoleDO>()
                    .eq(RoleDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .inSql(RoleDO::getId, "select role_id from user_role where user_id = " + userDTO.getId());
            RoleDO roleDO = roleService.getOne(queryWrapper);
            //角色id必定存在，所以不存在为空的情况
            String roleCodes = "ROLE_" + roleDO.getCode();
            authority = roleCodes.concat(",");

            //从role_menu 表中 根据角色id 获取 可操作菜单id(list)的perms
            String menuPerms = menuService.list(new LambdaQueryWrapper<MenuDO>()
                    .inSql(MenuDO::getId, "select menu_id from role_menu where role_id = " + roleDO.getId()))
                    .stream().map(MenuDO::getPerms).collect(Collectors.joining(","));
            authority = authority.concat(menuPerms);
            System.out.println("authority:" + authority);
            redisUtil.set("GrantedAuthority:" + userDTO.getUsername(), authority, 60 * 60);
        }
        return authority;
    }


    @Override
    public ResponseMessageDTO<PageResultDTO<UserDTO>> pageQueryUserByCondition(UserQuery query) {
        PageResultDTO<UserDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<UserDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
            //查询条件为空值或nll时，查询全部未被删除的 身份为普通用户的 记录
            queryWrapper.eq(UserDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .like(StringUtils.isNotBlank(query.getUsername()), UserDO::getUsername, query.getUsername())
                    .like(StringUtils.isNotBlank(query.getNickname()), UserDO::getNickname, query.getNickname())
                    //Integer类型和String类型的判空不同
                    .eq(null != query.getStatus(), UserDO::getStatus, query.getStatus())
                    //从user_role表中 查询出普通用户（即role_id=2）列表
                    .inSql(UserDO::getId, "select user_id from user_role where role_id = " + Constants.ORDINARY_NUM)
                    .orderByDesc(UserDO::getCreateTime);
            IPage<UserDO> userDOPage = this.page(page, queryWrapper);
            List<UserDO> userDORecords = userDOPage.getRecords();
            if (CollectionUtils.isEmpty(userDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<UserDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            pageResultDTO.setRows(ConversionUtils.transformList(userDORecords, UserDTO.class));
            pageResultDTO.setTotal(userDOPage.getTotal());
            pageResultDTO.setPageSize(userDOPage.getSize());
            pageResultDTO.setPage(userDOPage.getCurrent());
            pageResultDTO.setTotalPage(userDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<UserDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("用户分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<String> updateUser(UserDTO userDTO) {
        log.info("time:" + userDTO.getModifyTime());
        //调用mybatisplus的IService接口中的update(T entity, Wrapper<T> updateWrapper)方法SQL语句会自动填充修改时间字段
        //而使用update(Wrapper<T> updateWrapper)则修改时间字段不会自动填充
        LambdaUpdateWrapper<UserDO> updateWrapper = new LambdaUpdateWrapper<UserDO>()
                .eq(UserDO::getId, userDTO.getId())
                .eq(UserDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .set(StringUtils.isNotBlank(userDTO.getUsername()), UserDO::getUsername, userDTO.getUsername())
                .set(StringUtils.isNotBlank(userDTO.getNickname()), UserDO::getNickname, userDTO.getNickname())
                .set(UserDO::getModifyTime, userDTO.getModifyTime())
                .set(null != userDTO.getStatus(), UserDO::getStatus, userDTO.getStatus());
        if (this.update(updateWrapper)) {
            return ResponseMessageDTO.success("修改成功");
        }
        return ResponseMessageDTO.success("用户修改异常");
    }

    @Override
    public ResponseMessageDTO<String> deleteUser(Long[] ids) {
        //逻辑删除用户的同时逻辑删除 user_role上的数据
        //只有List有forEach，数组没有
//        Long[] longIds=new Long[ids.length];
//        for(int x=0;x<ids.length;x++){
//            longIds[x]= Long.valueOf(ids[x]);
//        }

        List<Long> newIds = Arrays.asList(ids);
        newIds.forEach(id -> log.info("id:" + id));
        LambdaUpdateWrapper<UserDO> userUpdateWrapper = new LambdaUpdateWrapper<UserDO>()
                .eq(UserDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .in(UserDO::getId, newIds)
                .set(UserDO::getDeleted, DeleteEnum.DELETE.getCode());
        LambdaUpdateWrapper<UserRoleDO> userRoleUpdateWrapper = new LambdaUpdateWrapper<UserRoleDO>()
                .eq(UserRoleDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .in(UserRoleDO::getUserId, newIds)
                .set(UserRoleDO::getDeleted, DeleteEnum.DELETE.getCode());
        if (this.update(userUpdateWrapper) == false || userRoleService.update(userRoleUpdateWrapper) == false) {
            return ResponseMessageDTO.success("删除失败");
        }
        return ResponseMessageDTO.success("删除成功");
    }
}
