package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.constants.Constants;
import com.gdesign.fisheyemoviesys.entity.CommentDO;
import com.gdesign.fisheyemoviesys.entity.RoleDO;
import com.gdesign.fisheyemoviesys.entity.UserDO;
import com.gdesign.fisheyemoviesys.entity.dto.CommentDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.enums.CodeEnum;
import com.gdesign.fisheyemoviesys.entity.enums.CommentStatusEnum;
import com.gdesign.fisheyemoviesys.entity.enums.DeleteEnum;
import com.gdesign.fisheyemoviesys.entity.enums.RoleEnum;
import com.gdesign.fisheyemoviesys.entity.param.CommentQuery;
import com.gdesign.fisheyemoviesys.mapper.CommentMapper;
import com.gdesign.fisheyemoviesys.service.CommentService;
import com.gdesign.fisheyemoviesys.service.MovieService;
import com.gdesign.fisheyemoviesys.service.RoleService;
import com.gdesign.fisheyemoviesys.service.UserService;
import com.gdesign.fisheyemoviesys.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ycy
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentDO> implements CommentService {
    @Resource
    private UserService userService;

    @Resource
    private MovieService movieService;

    @Resource
    private RoleService roleService;

    /**
     * 分页条件查询评论
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @Override
    public ResponseMessageDTO<PageResultDTO<CommentDTO>> pageQueryCommentByCondition(CommentQuery query) {
        PageResultDTO<CommentDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<CommentDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<CommentDO> queryWrapper = new LambdaQueryWrapper<>();

            //获取用户名类似的user_id集 和 电影名类似的movie_id集
            List<Long> userId = userService.getUserIdByLikeUserName(query.getUserName()).getResult();
            List<Long> movieId = movieService.getMovieIdByMovieName(query.getMovieName()).getResult();
            if (CollectionUtils.isEmpty(userId) || CollectionUtils.isEmpty(movieId)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            } else {
                //查询条件为空值或nll时，查询全部未被删除的评论记录
                queryWrapper.eq(CommentDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                        .in(CommentDO::getUserId, userId)
                        .in(CommentDO::getMovieId, movieId)
                        .eq(null != query.getStatus(), CommentDO::getStatus, query.getStatus())
                        .like(StringUtils.isNotBlank(query.getDetail()), CommentDO::getDetail, query.getDetail())
                        .orderByDesc(CommentDO::getCreateTime);
            }
            IPage<CommentDO> commentDOPage = this.page(page, queryWrapper);
            List<CommentDO> commentDORecords = commentDOPage.getRecords();
            if (CollectionUtils.isEmpty(commentDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            List<CommentDTO> commentDTOList = ConversionUtils.transformList(commentDORecords, CommentDTO.class);
            //设置返回的用户名、电影名
            commentDTOList.forEach(
                    item -> {
                        item.setMovieName(movieService.getById(item.getMovieId()).getName());
                        item.setUserName(userService.getById(item.getUserId()).getUsername());
                    }
            );
            pageResultDTO.setRows(commentDTOList);
            pageResultDTO.setTotal(commentDOPage.getTotal());
            pageResultDTO.setPageSize(commentDOPage.getSize());
            pageResultDTO.setPage(commentDOPage.getCurrent());
            pageResultDTO.setTotalPage(commentDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<CommentDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("评论分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<String> updateCommentStatus(CommentDTO commentDTO) {
        //获取前端传过来的状态，进行判断并修改
        Integer oldStatus = commentDTO.getStatus();
        Integer newStatus = oldStatus;
        //获取登录用户的权限
        //获得当前登录用户的用户名,注意不是昵称和id =》根据用户名获取角色id
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("当前登录的用户名为" + username);
        LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, username);
        UserDO userDO = userService.getOne(userQueryWrapper);
        LambdaQueryWrapper<RoleDO> roleQueryWrapper = new LambdaQueryWrapper<RoleDO>()
                .inSql(RoleDO::getId, "select role_id from user_role where user_id = " + userDO.getId());
        RoleDO roleDO = roleService.getOne(roleQueryWrapper);
        if (roleDO.getCode().equals(RoleEnum.ADMIN.getMsg())) {
            //身份为管理员，则对评论的状态只有三种：正常=》加精，加精=》正常，举报=》正常
            if (oldStatus.equals(CommentStatusEnum.NORMAL.getCode())) {//正常=》加精
                newStatus = CommentStatusEnum.REFINEMENT.getCode();
            } else {//举报=》正常，加精=》正常
                newStatus = CommentStatusEnum.NORMAL.getCode();
            }

        } else if (roleDO.getCode().equals(RoleEnum.ORDINARY.getMsg())) {
            //身份为普通用户，则对评论的状态只有一种：正常=》举报
            if (oldStatus.equals(CommentStatusEnum.NORMAL.getCode())) {
                newStatus = CommentStatusEnum.REPORT.getCode();
            }
        }
        //更新
        LambdaUpdateWrapper<CommentDO> updateWrapper = new LambdaUpdateWrapper<CommentDO>()
                .eq(CommentDO::getId, commentDTO.getId())
                .set(CommentDO::getStatus, newStatus);
        CommentDO commentDO = new CommentDO();
        if (this.update(commentDO, updateWrapper)) {
            return ResponseMessageDTO.success("修改成功");
        }
        return ResponseMessageDTO.success("修改失败");
    }

    @Override
    public ResponseMessageDTO<String> deleteCommentsByIds(Long[] ids) {
        List<Long> commentIds = Arrays.asList(ids);
        LambdaUpdateWrapper<CommentDO> updateWrapper = new LambdaUpdateWrapper<CommentDO>()
                .eq(CommentDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .set(CommentDO::getDeleted, DeleteEnum.DELETE.getCode())
                .in(CommentDO::getId, commentIds);
        CommentDO commentDO = new CommentDO();
        if (this.update(commentDO, updateWrapper)) {
            return ResponseMessageDTO.success("删除成功");
        }
        return ResponseMessageDTO.success("删除失败");
    }
}
