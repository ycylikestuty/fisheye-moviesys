package com.gdesign.fisheyemoviesys.controller;


import com.gdesign.fisheyemoviesys.annotation.Log;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.UserDTO;
import com.gdesign.fisheyemoviesys.entity.param.UserQuery;
import com.gdesign.fisheyemoviesys.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 分页查询用户
     *
     * @param query 查询条件
     * @return 用户列表
     */
    @PostMapping(path = "/list")
    @PreAuthorize("hasAnyAuthority('user:list')")
    public ResponseMessageDTO<PageResultDTO<UserDTO>> list(@RequestBody UserQuery query) {
        return userService.pageQueryUserByCondition(query);
    }

    @PostMapping(path = "/update")
    @Log(title = "用户列表", businessType = "修改用户")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseMessageDTO<String> update(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @DeleteMapping(path = "/delete")
    @Log(title = "用户列表", businessType = "删除用户")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseMessageDTO<String> delete(Long[] ids) {
        return userService.deleteUser(ids);
    }

}
