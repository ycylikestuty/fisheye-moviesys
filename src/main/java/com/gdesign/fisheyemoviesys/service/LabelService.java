package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.LabelDO;
import com.gdesign.fisheyemoviesys.entity.dto.LabelDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.param.LabelQuery;

import java.util.List;

/**
 * @author ycy
 */
public interface LabelService extends IService<LabelDO> {
    /**
     * 分页条件查询标签
     *
     * @param query 查询条件
     * @return 标签集合
     */
    ResponseMessageDTO<PageResultDTO<LabelDTO>> pageQueryLabelByCondition(LabelQuery query);

    /**
     * 逻辑删除标签
     *
     * @param ids 标签id
     * @return删除成功与否
     */
    ResponseMessageDTO<String> deleteLabel(Long[] ids);

    /**
     * 统计标签的分布
     *
     * @param labelDTO 标签
     * @return
     */
    ResponseMessageDTO<List<LabelDTO>> countLabelByCondition(LabelDTO labelDTO);
}
