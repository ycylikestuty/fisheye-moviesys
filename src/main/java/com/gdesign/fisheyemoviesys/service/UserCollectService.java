package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.UserCollectDO;

/**
 * @author ycy
 */
public interface UserCollectService extends IService<UserCollectDO> {

    /**
     * 根据用户id查询是否已经收藏
     * @param userId 用户id
     * @param collectId 收藏id
     * @param collectKind 收藏类型
     * @return 是否已经收藏
     */
    Boolean getCollectByUserId(Long userId,Long collectId,Integer collectKind);
}
