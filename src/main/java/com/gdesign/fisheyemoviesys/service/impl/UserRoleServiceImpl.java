package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.entity.UserRoleDO;
import com.gdesign.fisheyemoviesys.mapper.UserRoleMapper;
import com.gdesign.fisheyemoviesys.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author ycy
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements UserRoleService {
}
