package com.gdesign.fisheyemoviesys.service.impl;

import com.gdesign.fisheyemoviesys.entity.AccountUser;
import com.gdesign.fisheyemoviesys.entity.dto.UserDTO;
import com.gdesign.fisheyemoviesys.service.UserService;
import com.gdesign.fisheyemoviesys.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author ycy
 */
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userService.getUserByUserName(username).getResult();
        if (Objects.isNull(userDTO)) {
            throw new UsernameNotFoundException("用户不存在，请重新输入账号密码");
        }
        //将用户相关信息存入redis，到获取权限时再取出
        redisUtil.set("User:" + username, userDTO, 86400);
        return new AccountUser(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(), null);
    }

    //获取用户的角色和操作权限
    public List<GrantedAuthority> getUserAuthority(String username) {
        String authority = userService.getUserAuthorityByUserName(username);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
