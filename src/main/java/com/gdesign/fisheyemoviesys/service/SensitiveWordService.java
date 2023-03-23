package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.SensitiveWordDO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.SensitiveWordDTO;
import com.gdesign.fisheyemoviesys.entity.param.SensitiveWordQuery;

/**
 * @author ycy
 */
public interface SensitiveWordService extends IService<SensitiveWordDO> {
    /**
     * 分页条件查询违禁词
     *
     * @param query 查询条件
     * @return 违禁词集合
     */
    ResponseMessageDTO<PageResultDTO<SensitiveWordDTO>> pageQuerySensitiveWordByCondition(SensitiveWordQuery query);

    /**
     * 修改违禁词
     *
     * @param sensitiveWordDTO 违禁词信息
     * @return 修改成功与否
     */
    ResponseMessageDTO<String> updateSensitiveWord(SensitiveWordDTO sensitiveWordDTO);

    /**
     * 逻辑删除违禁词
     *
     * @param ids 违禁词id
     * @return删除成功与否
     */
    ResponseMessageDTO<String> deleteSensitiveWord(Long[] ids);

    /**
     * 新增违禁词
     *
     * @param sensitiveWordDTO 违禁词信息
     * @return 新增成功与否
     */
    ResponseMessageDTO<String> addSensitiveWord(SensitiveWordDTO sensitiveWordDTO);
}
