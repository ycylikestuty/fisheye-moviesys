package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.entity.RoleDO;
import com.gdesign.fisheyemoviesys.mapper.RoleMapper;
import com.gdesign.fisheyemoviesys.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ycy
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements RoleService {
}
