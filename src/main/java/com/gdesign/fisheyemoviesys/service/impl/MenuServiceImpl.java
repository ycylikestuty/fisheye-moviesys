package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.entity.MenuDO;
import com.gdesign.fisheyemoviesys.entity.RoleDO;
import com.gdesign.fisheyemoviesys.entity.dto.NavMenuDTO;
import com.gdesign.fisheyemoviesys.entity.dto.UserDTO;
import com.gdesign.fisheyemoviesys.entity.enums.DeleteEnum;
import com.gdesign.fisheyemoviesys.mapper.MenuMapper;
import com.gdesign.fisheyemoviesys.service.MenuService;
import com.gdesign.fisheyemoviesys.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ycy
 */
@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDO> implements MenuService {
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RoleServiceImpl roleService;

    @Override
    public List<NavMenuDTO> getCurrentUserNav() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserDTO userDTO = (UserDTO) redisUtil.get("User:" + username);

        LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<RoleDO>()
                .eq(RoleDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .inSql(RoleDO::getId, "select role_id from user_role where user_id = " + userDTO.getId());
        RoleDO roleDO = roleService.getOne(queryWrapper);
        List<Long> menuIds = this.list(new LambdaQueryWrapper<MenuDO>()
                .inSql(MenuDO::getId, "select menu_id from role_menu where role_id = " + roleDO.getId()))
                .stream().map(MenuDO::getId).collect(Collectors.toList());

        List<MenuDO> menus = listByIds(menuIds);
        //转换为树状结构
        List<MenuDO> treeMenu = buildTreeMenu(menus);
        //pojo转为dto
        return convert(treeMenu);
    }

    private List<MenuDO> buildTreeMenu(List<MenuDO> menus) {
        List<MenuDO> finalMenus = new ArrayList<>();
        for (MenuDO menu : menus) {
            for (MenuDO sysMenu : menus) {
                if (menu.getId().equals(sysMenu.getParentId())) {
                    menu.getChildren().add(sysMenu);
                }
            }
            if (menu.getParentId() == 0L) {
                finalMenus.add(menu);
            }
        }
        return finalMenus;
    }

    private List<NavMenuDTO> convert(List<MenuDO> menuTree) {
        List<NavMenuDTO> navMenus = new ArrayList<>();
        menuTree.forEach(
                menu -> {
                    if (menu.getDeleted() != 1) {//未被删除
                        NavMenuDTO navMenu = new NavMenuDTO();
                        navMenu.setId(menu.getId());
                        navMenu.setTitle(menu.getName());
                        navMenu.setName(menu.getPerms());
                        navMenu.setComponent(menu.getComponent());
                        navMenu.setPath(menu.getPath());
                        navMenu.setIcon(menu.getIcon());
                        navMenu.setOrderNum(menu.getOrderNum());
                        if (menu.getChildren().size() > 0) {
                            navMenu.setChildren(convert(menu.getChildren()));
                        }
                        navMenus.add(navMenu);
                    }
                }
        );
        navMenus.sort(Comparator.comparing(NavMenuDTO::getOrderNum));
        navMenus.forEach(nav -> {
            if (nav.getChildren().size() > 0) {
                nav.getChildren().sort(Comparator.comparing(NavMenuDTO::getOrderNum));
            }
        });
        return navMenus;
    }

}
