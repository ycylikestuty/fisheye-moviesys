package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.UserLabelDO;
import com.gdesign.fisheyemoviesys.entity.dto.LabelDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.UserLabelDTO;

import java.util.List;

/**
 * @author ycy
 */
public interface UserLabelService extends IService<UserLabelDO> {
    /**
     * 根据用户信息获取用户的所有标签列表
     * @param userLabelDTO
     * @return
     */
//    ResponseMessageDTO<List<LabelDTO>> getUserLabelList(UserLabelDTO userLabelDTO);
}
