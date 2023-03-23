package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.entity.UserLabelDO;
import com.gdesign.fisheyemoviesys.mapper.UserLabelMapper;
import com.gdesign.fisheyemoviesys.service.UserLabelService;
import org.springframework.stereotype.Service;

/**
 * @author ycy
 */
@Service
public class UserLabelServiceImpl extends ServiceImpl<UserLabelMapper, UserLabelDO> implements UserLabelService {
}
