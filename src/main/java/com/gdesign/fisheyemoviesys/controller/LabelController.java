package com.gdesign.fisheyemoviesys.controller;

import com.gdesign.fisheyemoviesys.annotation.Log;
import com.gdesign.fisheyemoviesys.entity.dto.LabelDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.UserLabelDTO;
import com.gdesign.fisheyemoviesys.entity.param.LabelQuery;
import com.gdesign.fisheyemoviesys.service.LabelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/label")
public class LabelController {

    @Resource
    private LabelService labelService;

    /**
     * 分页查询标签
     *
     * @param query 查询条件
     * @return 标签列表
     */
    @PostMapping(path = "/list")
    @PreAuthorize("hasAnyAuthority('label:list')")
    public ResponseMessageDTO<PageResultDTO<LabelDTO>> list(@RequestBody LabelQuery query) {
        return labelService.pageQueryLabelByCondition(query);
    }

    @DeleteMapping(path = "/delete")
    @Log(title = "标签列表", businessType = "删除标签")
    @PreAuthorize("hasAnyAuthority('label:delete')")
    public ResponseMessageDTO<String> delete(Long[] ids) {
        return labelService.deleteLabel(ids);
    }

    @PostMapping(path = "/count")
    public ResponseMessageDTO<List<LabelDTO>> count(@RequestBody LabelDTO labelDTO) {
        return labelService.countLabelByCondition(labelDTO);
    }

    @PostMapping(path = "/getOldUserLabelList")
    public ResponseMessageDTO<List<LabelDTO>> getUserLabelList(@RequestBody UserLabelDTO userLabelDTO){
        return labelService.getOldUserLabelList(userLabelDTO);
    }

    @PostMapping(path = "/getNewUserLabelList")
    public ResponseMessageDTO<List<LabelDTO>> getNewUserLabelList(@RequestBody UserLabelDTO userLabelDTO){
        return labelService.getNewUserLabelList(userLabelDTO);
    }
}
