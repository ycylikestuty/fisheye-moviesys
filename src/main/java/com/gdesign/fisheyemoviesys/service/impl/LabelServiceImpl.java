package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.constants.Constants;
import com.gdesign.fisheyemoviesys.entity.LabelDO;
import com.gdesign.fisheyemoviesys.entity.UserLabelDO;
import com.gdesign.fisheyemoviesys.entity.dto.LabelDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.enums.CodeEnum;
import com.gdesign.fisheyemoviesys.entity.enums.DeleteEnum;
import com.gdesign.fisheyemoviesys.entity.enums.KindEnum;
import com.gdesign.fisheyemoviesys.entity.param.LabelQuery;
import com.gdesign.fisheyemoviesys.mapper.LabelMapper;
import com.gdesign.fisheyemoviesys.service.LabelService;
import com.gdesign.fisheyemoviesys.service.UserLabelService;
import com.gdesign.fisheyemoviesys.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ycy
 */
@Service
@Slf4j
public class LabelServiceImpl extends ServiceImpl<LabelMapper, LabelDO> implements LabelService {
    @Resource
    private UserLabelService userLabelService;

    @Override
    public ResponseMessageDTO<PageResultDTO<LabelDTO>> pageQueryLabelByCondition(LabelQuery query) {
        PageResultDTO<LabelDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<LabelDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<LabelDO> queryWrapper = new LambdaQueryWrapper<>();
            //查询条件为空值或nll时，查询全部未被删除的 标签记录
            queryWrapper.eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .like(StringUtils.isNotBlank(query.getName()), LabelDO::getName, query.getName())
                    .eq(null != query.getKind(), LabelDO::getKind, query.getKind())
                    .orderByDesc(LabelDO::getCreateTime);
            IPage<LabelDO> labelDOPage = this.page(page, queryWrapper);
            List<LabelDO> labelDORecords = labelDOPage.getRecords();
            if (CollectionUtils.isEmpty(labelDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<LabelDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            pageResultDTO.setRows(ConversionUtils.transformList(labelDORecords, LabelDTO.class));
            pageResultDTO.setTotal(labelDOPage.getTotal());
            pageResultDTO.setPageSize(labelDOPage.getSize());
            pageResultDTO.setPage(labelDOPage.getCurrent());
            pageResultDTO.setTotalPage(labelDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<LabelDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("用户分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<String> deleteLabel(Long[] ids) {
        List<Long> newIds = Arrays.asList(ids);
        LambdaUpdateWrapper<LabelDO> labelUpdateWrapper = new LambdaUpdateWrapper<LabelDO>()
                .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .in(LabelDO::getId, newIds)
                .set(LabelDO::getDeleted, DeleteEnum.DELETE.getCode());
        LabelDO labelDO = new LabelDO();
        if (this.update(labelDO, labelUpdateWrapper) == false) {
            return ResponseMessageDTO.success("删除失败");
        }
        return ResponseMessageDTO.success("删除成功");
    }

    @Override
    public ResponseMessageDTO<List<LabelDTO>> countLabelByCondition(LabelDTO labelDTO) {
        List<LabelDTO> result = new ArrayList<>();
        //根据标签kind和name获取未被删除的用户标签id
        LambdaQueryWrapper<LabelDO> labelQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .eq(LabelDO::getName, labelDTO.getName())
                .eq(LabelDO::getKind, labelDTO.getKind());
        Long id = this.getOne(labelQueryWrapper).getId();
        //获取未被删除的用户标签数量，不是标签数量
        LambdaQueryWrapper<UserLabelDO> queryWrapper = new LambdaQueryWrapper<UserLabelDO>()
                .eq(UserLabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
        Integer total = userLabelService.count(queryWrapper);
        //根据传入的标签类型查询user_label中的不同字段 并根据标签id统计分布情况
        LambdaQueryWrapper<UserLabelDO> userLabelQueryWrapper = new LambdaQueryWrapper<UserLabelDO>()
                .eq(UserLabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
        if (KindEnum.YEAR.getCode().equals(labelDTO.getKind())) {//年份
            userLabelQueryWrapper
                    .like(UserLabelDO::getYear, id)
                    .select(UserLabelDO::getYear);
        } else if (KindEnum.AREA.getCode().equals(labelDTO.getKind())) {//地区
            userLabelQueryWrapper
                    .like(UserLabelDO::getArea, id)
                    .select(UserLabelDO::getArea);
        } else if (KindEnum.GENRE.getCode().equals(labelDTO.getKind())) {//风格
            userLabelQueryWrapper
                    .like(UserLabelDO::getGenre, id)
                    .select(UserLabelDO::getGenre);
        }
        //设置保留两位小数:DecimalFormat(返回String类型)，BigDecimal
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Integer count = userLabelService.count(userLabelQueryWrapper);
        Double rate = Double.valueOf(count) / total;
        Double newRate = Double.valueOf(decimalFormat.format(rate));
        log.info(decimalFormat.format(rate));
        labelDTO.setName("喜欢");
        labelDTO.setRate(newRate);
        result.add(labelDTO);

        LabelDTO newLabelDTO = new LabelDTO();
        newLabelDTO.setName("不喜欢");
        newLabelDTO.setRate(Double.valueOf(decimalFormat.format(1 - newRate)));
        result.add(newLabelDTO);
        return ResponseMessageDTO.success(result);
    }
}
