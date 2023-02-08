package com.gdesign.fisheyemoviesys.controller;

import cn.hutool.core.map.MapUtil;
import com.gdesign.fisheyemoviesys.entity.dto.MenuDTO;
import com.gdesign.fisheyemoviesys.entity.dto.NavMenuDTO;
import com.gdesign.fisheyemoviesys.entity.dto.Result;
import com.gdesign.fisheyemoviesys.entity.dto.UserDTO;
import com.gdesign.fisheyemoviesys.service.MenuService;
import com.gdesign.fisheyemoviesys.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController<MenuDTO> {
    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;

    @GetMapping("/nav")
    public Result nav(Principal principal) {
        UserDTO userDTO = (UserDTO) redisUtil.get("User:" + principal.getName());
        //获取权限信息
        String authority = userService.getUserAuthorityByUserName(userDTO.getUsername());
        String[] authorityArray = StringUtils.tokenizeToStringArray(authority, ",");
        //获取导航栏信息
        List<NavMenuDTO> nav = menuService.getCurrentUserNav();
        return Result.succ(MapUtil.builder().put("authoritys", authorityArray).put("nav", nav).map());
    }
}
