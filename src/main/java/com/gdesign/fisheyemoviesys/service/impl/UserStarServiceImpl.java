package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.entity.UserStarDO;
import com.gdesign.fisheyemoviesys.mapper.UserStarMapper;
import com.gdesign.fisheyemoviesys.service.UserStarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ycy
 */
@Service
@Slf4j
public class UserStarServiceImpl extends ServiceImpl<UserStarMapper, UserStarDO> implements UserStarService {
}
