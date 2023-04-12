package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.entity.UserCollectDO;
import com.gdesign.fisheyemoviesys.mapper.UserCollectMapper;
import com.gdesign.fisheyemoviesys.service.UserCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author ycy
 */
@Service
@Slf4j
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollectDO> implements UserCollectService {
    @Override
    public Boolean getCollectByUserId(Long userId, Long collectId, Integer collectKind) {
        LambdaQueryWrapper<UserCollectDO> queryWrapper = new LambdaQueryWrapper<UserCollectDO>()
                .eq(UserCollectDO::getUserId, userId)
                .eq(UserCollectDO::getKind,collectKind);
        UserCollectDO userCollectDO = this.getOne(queryWrapper);
        //用户没有收藏的情况
        if(Objects.isNull(userCollectDO)){
            return false;
        }
        String collectIds = userCollectDO.getCollectIds();
        if (collectIds.contains(String.valueOf(collectId)) == true) {
            return true;
        }
        return false;
    }
}
