package com.gdesign.fisheyemoviesys.controller;

import com.gdesign.fisheyemoviesys.annotation.Log;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.SensitiveWordDTO;
import com.gdesign.fisheyemoviesys.entity.param.SensitiveWordQuery;
import com.gdesign.fisheyemoviesys.service.SensitiveWordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/sensitiveWord")
public class SensitiveWordController {

    @Resource
    private SensitiveWordService sensitiveWordService;

    /**
     * 分页查询违禁词
     *
     * @param query 查询条件
     * @return 违禁词列表
     */
    @PostMapping(path = "/list")
    public ResponseMessageDTO<PageResultDTO<SensitiveWordDTO>> list(@RequestBody SensitiveWordQuery query) {
        return sensitiveWordService.pageQuerySensitiveWordByCondition(query);
    }

    @DeleteMapping(path = "/delete")
    @Log(title = "违禁词列表", businessType = "删除违禁词")
    @PreAuthorize("hasAnyAuthority('prohibited:delete')")
    public ResponseMessageDTO<String> delete(Long[] ids) {
        return sensitiveWordService.deleteSensitiveWord(ids);
    }

    @PostMapping(path = "/update")
    @Log(title = "违禁词列表", businessType = "修改违禁词")
    @PreAuthorize("hasAnyAuthority('prohibited:update')")
    public ResponseMessageDTO<String> update(@RequestBody SensitiveWordDTO sensitiveWordDTO) {
        return sensitiveWordService.updateSensitiveWord(sensitiveWordDTO);
    }

    @PostMapping(path = "/add")
    @Log(title = "违禁词列表", businessType = "新增违禁词")
    @PreAuthorize("hasAnyAuthority('prohibited:add')")
    public ResponseMessageDTO<String> add(@RequestBody SensitiveWordDTO sensitiveWordDTO) {
        return sensitiveWordService.addSensitiveWord(sensitiveWordDTO);
    }
}
