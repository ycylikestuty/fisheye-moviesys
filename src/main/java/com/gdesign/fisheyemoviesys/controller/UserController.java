package com.gdesign.fisheyemoviesys.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gdesign.fisheyemoviesys.annotation.Log;
import com.gdesign.fisheyemoviesys.config.SecurityConfig;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.Result;
import com.gdesign.fisheyemoviesys.entity.dto.UserDTO;
import com.gdesign.fisheyemoviesys.entity.param.UserQuery;
import com.gdesign.fisheyemoviesys.service.LabelService;
import com.gdesign.fisheyemoviesys.service.UserService;
import com.gdesign.fisheyemoviesys.utils.JwtUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private LabelService labelService;

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

    @PostMapping("/avatar")
    @Log(title = "用户管理", businessType = "用户修改头像")
    public Result avatar(@RequestParam("avatarFile") MultipartFile file, @RequestParam("user") String userJson) throws JsonProcessingException {
        return userService.updateImage(file, userJson);
    }

    @GetMapping("/updatePassword")
    @Log(title = "用户管理", businessType = "用户修改密码")
    public Result updatePassword(String oldPassword, String newPassword, String confirmPassword, Principal principal) {
        return userService.updatePassword(oldPassword, newPassword, confirmPassword, principal);
    }

    @PostMapping("/register")
    public Result registerUser(@RequestBody UserDTO userDTO){
        return labelService.registerUser(userDTO);
    }

    @PostMapping("/updateInfo")
    @Log(title = "用户管理", businessType = "用户更新信息")
    public ResponseMessageDTO<String> updateInfo(@RequestBody UserDTO userDTO){
        return userService.updateUserInfo(userDTO);
    }
}
