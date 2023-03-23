package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.constants.Constants;
import com.gdesign.fisheyemoviesys.entity.SensitiveWordDO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.SensitiveWordDTO;
import com.gdesign.fisheyemoviesys.entity.enums.CodeEnum;
import com.gdesign.fisheyemoviesys.entity.enums.DeleteEnum;
import com.gdesign.fisheyemoviesys.entity.param.SensitiveWordQuery;
import com.gdesign.fisheyemoviesys.mapper.SensitiveWordMapper;
import com.gdesign.fisheyemoviesys.service.SensitiveWordService;
import com.gdesign.fisheyemoviesys.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ycy
 */
@Service
@Slf4j
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWordDO> implements SensitiveWordService {
    @Override
    public ResponseMessageDTO<PageResultDTO<SensitiveWordDTO>> pageQuerySensitiveWordByCondition(SensitiveWordQuery query) {
        PageResultDTO<SensitiveWordDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<SensitiveWordDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<SensitiveWordDO> queryWrapper = new LambdaQueryWrapper<>();
            //查询全部未被删除的违禁词记录
            queryWrapper.eq(SensitiveWordDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .orderByDesc(SensitiveWordDO::getCreateTime);
            IPage<SensitiveWordDO> sensitiveWordDOPage = this.page(page, queryWrapper);
            List<SensitiveWordDO> sensitiveWordDORecords = sensitiveWordDOPage.getRecords();
            if (CollectionUtils.isEmpty(sensitiveWordDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<SensitiveWordDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            pageResultDTO.setRows(ConversionUtils.transformList(sensitiveWordDORecords, SensitiveWordDTO.class));
            pageResultDTO.setTotal(sensitiveWordDOPage.getTotal());
            pageResultDTO.setPageSize(sensitiveWordDOPage.getSize());
            pageResultDTO.setPage(sensitiveWordDOPage.getCurrent());
            pageResultDTO.setTotalPage(sensitiveWordDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<SensitiveWordDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("违禁词分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<String> updateSensitiveWord(SensitiveWordDTO sensitiveWordDTO) {
        LambdaUpdateWrapper<SensitiveWordDO> movieUpdateWrapper = new LambdaUpdateWrapper<SensitiveWordDO>()
                .eq(SensitiveWordDO::getId, sensitiveWordDTO.getId())
                .eq(SensitiveWordDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .set(SensitiveWordDO::getWord, sensitiveWordDTO.getWord());
        SensitiveWordDO sensitiveWordDO = new SensitiveWordDO();
        if (this.update(sensitiveWordDO, movieUpdateWrapper)) {
            return ResponseMessageDTO.success("修改成功");
        }
        return ResponseMessageDTO.success("修改异常");
    }

    @Override
    public ResponseMessageDTO<String> deleteSensitiveWord(Long[] ids) {
        //逻辑删除违禁词
        List<Long> newIds = Arrays.asList(ids);
        //保证有修改时间
        SensitiveWordDO sensitiveWordDO = new SensitiveWordDO();
        LambdaUpdateWrapper<SensitiveWordDO> sensitiveWordUpdateWrapper = new LambdaUpdateWrapper<SensitiveWordDO>()
                .eq(SensitiveWordDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .in(SensitiveWordDO::getId, newIds)
                .set(SensitiveWordDO::getDeleted, DeleteEnum.DELETE.getCode());
        if (this.update(sensitiveWordDO, sensitiveWordUpdateWrapper) ) {
            return ResponseMessageDTO.success("删除成功");
        }
        return ResponseMessageDTO.success("删除失败");
    }

    @Override
    public ResponseMessageDTO<String> addSensitiveWord(SensitiveWordDTO sensitiveWordDTO) {
        SensitiveWordDO sensitiveWordDO = new SensitiveWordDO();
        BeanUtils.copyProperties(sensitiveWordDTO, sensitiveWordDO);
        if (this.save(sensitiveWordDO)) {
            return ResponseMessageDTO.success("新增成功");
        }
        return ResponseMessageDTO.success("新增失败");
    }
}
