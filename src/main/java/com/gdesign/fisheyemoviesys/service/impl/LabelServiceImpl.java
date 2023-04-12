package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.constants.Constants;
import com.gdesign.fisheyemoviesys.entity.*;
import com.gdesign.fisheyemoviesys.entity.dto.*;
import com.gdesign.fisheyemoviesys.entity.enums.*;
import com.gdesign.fisheyemoviesys.entity.param.LabelQuery;
import com.gdesign.fisheyemoviesys.mapper.LabelMapper;
import com.gdesign.fisheyemoviesys.service.*;
import com.gdesign.fisheyemoviesys.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ycy
 */
@Service
@Slf4j
public class LabelServiceImpl extends ServiceImpl<LabelMapper, LabelDO> implements LabelService {
    @Resource
    private UserLabelService userLabelService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserService userService;

    @Resource
    private UserCollectService userCollectService;

    @Resource
    private MovieTypeService movieTypeService;

    @Resource
    private MovieService movieService;

    @Resource
    private TypeService typeService;

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
                .eq(UserLabelDO::getStatus, UserLabelEnum.AFTER.getCode())
                .eq(UserLabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
        Integer total = userLabelService.count(queryWrapper);
        //根据传入的标签类型查询user_label中的不同字段 并根据标签id统计分布情况
        LambdaQueryWrapper<UserLabelDO> userLabelQueryWrapper = new LambdaQueryWrapper<UserLabelDO>()
                .eq(UserLabelDO::getStatus, UserLabelEnum.AFTER.getCode())
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

    @Override
    public ResponseMessageDTO<List<LabelDTO>> getOldUserLabelList(UserLabelDTO userLabelDTO) {
        List<LabelDTO> result = new ArrayList<>();
        LambdaQueryWrapper<UserLabelDO> userLabelDOLambdaQueryWrapper = new LambdaQueryWrapper<UserLabelDO>()
                .eq(UserLabelDO::getUserId, userLabelDTO.getUserId())
                .eq(UserLabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .eq(UserLabelDO::getStatus, userLabelDTO.getStatus());
        UserLabelDO userLabelDO = userLabelService.getOne(userLabelDOLambdaQueryWrapper);
        //地区
        String area = userLabelDO.getArea();
        //将String类型先转化为String[]，再转化为List<String>类型
        List<String> areaArray = Arrays.asList(area.split(","));
        areaArray.forEach(
                item -> {
                    LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                            .eq(LabelDO::getId, Long.valueOf(item))
                            .eq(LabelDO::getKind, KindEnum.AREA.getCode());
                    LabelDO labelDO = this.getOne(labelDOLambdaQueryWrapper);
                    LabelDTO labelDTO = new LabelDTO();
                    labelDTO.setName(labelDO.getName());
                    labelDTO.setRate(Double.valueOf(1));
                    result.add(labelDTO);
                }
        );
        //年份
        String year = userLabelDO.getYear();
        //将String类型先转化为String[]，再转化为List<String>类型
        List<String> yearArray = Arrays.asList(year.split(","));
        yearArray.forEach(
                item -> {
                    LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                            .eq(LabelDO::getId, Long.valueOf(item))
                            .eq(LabelDO::getKind, KindEnum.YEAR.getCode());
                    LabelDO labelDO = this.getOne(labelDOLambdaQueryWrapper);
                    LabelDTO labelDTO = new LabelDTO();
                    labelDTO.setName(labelDO.getName());
                    labelDTO.setRate(Double.valueOf(1));
                    result.add(labelDTO);
                }
        );
        //类型
        String type = userLabelDO.getGenre();
        //将String类型先转化为String[]，再转化为List<String>类型
        List<String> typeArray = Arrays.asList(type.split(","));
        typeArray.forEach(
                item -> {
                    LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                            .eq(LabelDO::getId, Long.valueOf(item))
                            .eq(LabelDO::getKind, KindEnum.GENRE.getCode());
                    LabelDO labelDO = this.getOne(labelDOLambdaQueryWrapper);
                    LabelDTO labelDTO = new LabelDTO();
                    labelDTO.setName(labelDO.getName());
                    labelDTO.setRate(Double.valueOf(1));
                    result.add(labelDTO);
                }
        );
        return ResponseMessageDTO.success(result);
    }

    @Override
    public Result registerUser(UserDTO userDTO) {
        //验证用户账号是否唯一
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, userDTO.getUsername())
                .eq(UserDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
        UserDO queryUser = userService.getOne(queryWrapper);
        if (null != queryUser) {
            return Result.fail("该用户账号已经存在，请重新输入");
        }
        //新增user表
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userDTO, userDO);
        userDO.setImg("https://img1.doubanio.com/view/group_topic/l/public/p560183288.webp");
        userDO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        //新增user_role表,角色固定为普通用户
        UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setRoleId(Constants.ORDINARY_NUM);
        //新增user_label表
        UserLabelDO userLabelDO = new UserLabelDO();
//        userLabelDO.setStatus(UserLabelEnum.BEFORE.getCode());
        //从label表中根据kind和name查询出id，再将id组合成字符串
        //地区
        List<Long> areaIds = userDTO.getArea();
        List<String> areaString = new ArrayList<>();
        areaIds.forEach(
                item -> {
                    log.info("item:" + item);
                    for (AreaEnum areaItem : AreaEnum.values()) {
                        //注意areaItem的code类型为Integer，要进行转换
                        if (item.equals(Long.valueOf(areaItem.getCode()))) {
                            log.info("areaItem:" + areaItem.getArea());
                            LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                                    .eq(LabelDO::getName, areaItem.getArea())
                                    .eq(LabelDO::getKind, KindEnum.AREA.getCode());
                            areaString.add(this.getOne(labelDOLambdaQueryWrapper).getId().toString());
                        }
                    }
                }
        );
        String area = areaString.stream().collect(Collectors.joining(","));
        //年份
        List<String> yearIds = userDTO.getYear();
        List<String> yearString = new ArrayList<>();
        yearIds.forEach(
                item -> {
                    LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                            .eq(LabelDO::getName, item)
                            .eq(LabelDO::getKind, KindEnum.YEAR.getCode());
                    yearString.add(this.getOne(labelDOLambdaQueryWrapper).getId().toString());
                }
        );
        String year = yearString.stream().collect(Collectors.joining(","));
        //类型
        List<String> typeIds = userDTO.getType();
        List<String> typeString = new ArrayList<>();
        typeIds.forEach(
                item -> {
                    LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                            .eq(LabelDO::getName, item)
                            .eq(LabelDO::getKind, KindEnum.GENRE.getCode());
                    typeString.add(this.getOne(labelDOLambdaQueryWrapper).getId().toString());
                }
        );
        String type = typeString.stream().collect(Collectors.joining(","));
        if (userService.save(userDO)) {
            userRoleDO.setUserId(userDO.getId());
            //保存userLabel状态为0的数据同时，保存状态为1的数据，两者数据相同
            userLabelDO.setStatus(UserLabelEnum.BEFORE.getCode());
            userLabelDO.setUserId(userDO.getId());
            userLabelDO.setArea(area);
            userLabelDO.setYear(year);
            userLabelDO.setGenre(type);
            if (userRoleService.save(userRoleDO) && userLabelService.save(userLabelDO)) {
                userLabelDO = new UserLabelDO();
                userLabelDO.setStatus(UserLabelEnum.AFTER.getCode());
                userLabelDO.setUserId(userDO.getId());
                userLabelDO.setArea(area);
                userLabelDO.setYear(year);
                userLabelDO.setGenre(type);
                if (userLabelService.save(userLabelDO)) {
                    return Result.succ("注册成功");
                }
                return Result.succ("注册失败");
            }
            return Result.fail("注册失败");
        }
        return Result.fail("注册失败");
    }

    @Override
    public ResponseMessageDTO<List<LabelDTO>> getNewUserLabelList(UserLabelDTO userLabelDTO) {
        //从user_collect中获取收藏的电影
        LambdaQueryWrapper<UserCollectDO> userCollectDOLambdaQueryWrapper = new LambdaQueryWrapper<UserCollectDO>()
                .eq(UserCollectDO::getUserId, userLabelDTO.getUserId())
                .eq(UserCollectDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .eq(UserCollectDO::getKind, CollectKindEnum.MOVIE.getCode());
        UserCollectDO userCollectDO = userCollectService.getOne(userCollectDOLambdaQueryWrapper);
        //如果该用户没有收藏，那么直接返回注册时的标签和权重
        if (Objects.isNull(userCollectDO)) {
            UserLabelDTO noCollectUserLabelDTO = new UserLabelDTO();
            noCollectUserLabelDTO.setUserId(userLabelDTO.getUserId());
            noCollectUserLabelDTO.setStatus(UserLabelEnum.BEFORE.getCode());
            List<LabelDTO> result = this.getOldUserLabelList(noCollectUserLabelDTO).getResult();
            return ResponseMessageDTO.success(result);
        }
        List<String> stringMovieIds = Arrays.asList(userCollectDO.getCollectIds().split(","));
        List<Long> movieIds = stringMovieIds.stream().map(Long::valueOf).collect(Collectors.toList());
        //类型
        //从movie_type表中查询出该电影的type_id,再在type表中根据type_id找出type_name,最后在label中根据type_name对应的label_id
        List<String> typeList = new ArrayList<>();
        movieIds.forEach(
                item -> {
                    LambdaQueryWrapper<MovieTypeDO> movieTypeDOLambdaQueryWrapper = new LambdaQueryWrapper<MovieTypeDO>()
                            .eq(MovieTypeDO::getMovieId, item)
                            .eq(MovieTypeDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
                    MovieTypeDO movieTypeDO = movieTypeService.getOne(movieTypeDOLambdaQueryWrapper);
                    String[] typeArray = movieTypeDO.getTypeId().split(",");
                    for (String x : typeArray) {
                        LambdaQueryWrapper<TypeDO> typeDOLambdaQueryWrapper = new LambdaQueryWrapper<TypeDO>()
                                .eq(TypeDO::getId, Long.valueOf(x))
                                .eq(TypeDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
                        TypeDO typeDO = typeService.getOne(typeDOLambdaQueryWrapper);
                        LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                                .eq(LabelDO::getName, typeDO.getName())
                                .eq(LabelDO::getKind, KindEnum.GENRE.getCode())
                                .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
                        LabelDO labelDO = this.getOne(labelDOLambdaQueryWrapper);
                        typeList.add(labelDO.getId().toString());
                    }
                }
        );
        typeList.forEach(
                item -> log.info("type:" + item)
        );
        //地区
        //先从movie表中获取area，再从AreaEnum中获取area_name，最后从label中获取id
        List<String> areaList = new ArrayList<>();
        movieIds.forEach(
                item -> {
                    String areaName = new String();
                    LambdaQueryWrapper<MovieDO> movieDOLambdaQueryWrapper = new LambdaQueryWrapper<MovieDO>()
                            .eq(MovieDO::getId, item)
                            .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
                    MovieDO movieDO = movieService.getOne(movieDOLambdaQueryWrapper);
                    for (AreaEnum areaEnum : AreaEnum.values()) {
                        if (areaEnum.getCode().equals(movieDO.getArea())) {
                            areaName = areaEnum.getArea();
                        }
                    }
                    LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                            .eq(LabelDO::getName, areaName)
                            .eq(LabelDO::getKind, KindEnum.AREA.getCode())
                            .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
                    LabelDO labelDO = this.getOne(labelDOLambdaQueryWrapper);
                    areaList.add(labelDO.getId().toString());
                }
        );
        for (String x : areaList) {
            log.info("area:" + x);
        }
        //年份
        //先从movie表中获取year，再在label中根据year获取label_id
        List<String> yearList = new ArrayList<>();
        movieIds.forEach(
                item -> {
                    LambdaQueryWrapper<MovieDO> movieDOLambdaQueryWrapper = new LambdaQueryWrapper<MovieDO>()
                            .eq(MovieDO::getId, item)
                            .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
                    String yearString = movieService.getOne(movieDOLambdaQueryWrapper).getYear();
                    LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                            .eq(LabelDO::getKind, KindEnum.YEAR.getCode())
                            .eq(LabelDO::getName, yearString)
                            .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
                    LabelDO labelDO = this.getOne(labelDOLambdaQueryWrapper);
                    yearList.add(labelDO.getId().toString());
                }
        );
        for (String x : yearList) {
            log.info("year:" + x);
        }
        //对三个list进行去重，并将去重结果存入user_label表
        String type = typeList.stream().distinct().collect(Collectors.joining(","));
        String year = yearList.stream().distinct().collect(Collectors.joining(","));
        String area = areaList.stream().distinct().collect(Collectors.joining(","));
        log.info("area:" + area + "year:" + year + "type:" + type);
        //更新数据库，因为必定用户注册时已经保存了kind=1的数据
        UserLabelDO userLabelDO = new UserLabelDO();
        LambdaUpdateWrapper<UserLabelDO> userLabelDOLambdaUpdateWrapper = new LambdaUpdateWrapper<UserLabelDO>()
                .eq(UserLabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .eq(UserLabelDO::getStatus, UserLabelEnum.AFTER.getCode())
                .eq(UserLabelDO::getUserId, userLabelDTO.getUserId())
                .set(UserLabelDO::getArea, area)
                .set(UserLabelDO::getGenre, type)
                .set(UserLabelDO::getYear, year);
        userLabelService.update(userLabelDO, userLabelDOLambdaUpdateWrapper);
        List<LabelDTO> result = new ArrayList<>();
        //获取标签权重
        //地区
        //set可以去重，保证了里面的每个元素都是已经出现的
        Set<String> areaSet = new HashSet<>(areaList);
        for (String item : areaSet) {
            //根据label_id获取label_name
            LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                    .eq(LabelDO::getId, Long.valueOf(item))
                    .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .eq(LabelDO::getKind, KindEnum.AREA.getCode());
            LabelDO labelDO = this.getOne(labelDOLambdaQueryWrapper);
            LabelDTO resultLabelDTO = new LabelDTO();
            resultLabelDTO.setName(labelDO.getName());
            //使用Collections.frequency函数统计List中每个元素出现的次数
            log.info("rate:" + Double.valueOf(Collections.frequency(areaList, item)));
            resultLabelDTO.setRate(Double.valueOf(Collections.frequency(areaList, item)));
            result.add(resultLabelDTO);
        }
        //年份
        Set<String> yearSet = new HashSet<>(yearList);
        for (String item : yearSet) {
            //根据label_id获取label_name
            LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                    .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .eq(LabelDO::getKind, KindEnum.YEAR.getCode())
                    .eq(LabelDO::getId, Long.valueOf(item));
            LabelDO labelDO = this.getOne(labelDOLambdaQueryWrapper);
            LabelDTO resultLabelDTO = new LabelDTO();
            resultLabelDTO.setName(labelDO.getName());
            resultLabelDTO.setRate(Double.valueOf(Collections.frequency(yearList, item)));
            result.add(resultLabelDTO);
        }
        //类型
        Set<String> typeSet = new HashSet<>(typeList);
        for (String item : typeSet) {
            //根据label_id获取label_name
            LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                    .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .eq(LabelDO::getKind, KindEnum.GENRE.getCode())
                    .eq(LabelDO::getId, Long.valueOf(item));
            LabelDO labelDO = this.getOne(labelDOLambdaQueryWrapper);
            LabelDTO resultLabelDTO = new LabelDTO();
            resultLabelDTO.setName(labelDO.getName());
            resultLabelDTO.setRate(Double.valueOf(Collections.frequency(typeList, item)));
            result.add(resultLabelDTO);
        }
        return ResponseMessageDTO.success(result);
    }

    @Override
    public ResponseMessageDTO<List<MovieDTO>> getUserLikeMovies(Long userId) {
        //根据user_id获取user_label
        LambdaQueryWrapper<UserLabelDO> userLabelDOLambdaQueryWrapper = new LambdaQueryWrapper<UserLabelDO>()
                .eq(UserLabelDO::getUserId, userId)
                .eq(UserLabelDO::getStatus, UserLabelEnum.AFTER.getCode());
        UserLabelDO userLabelDO = userLabelService.getOne(userLabelDOLambdaQueryWrapper);
        String year = userLabelDO.getYear();
        String area = userLabelDO.getArea();
        String type = userLabelDO.getGenre();
        //获取所有未被删除的电影
        LambdaQueryWrapper<MovieDO> movieDOLambdaQueryWrapper = new LambdaQueryWrapper<MovieDO>()
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode());
        List<MovieDO> movieDOList = movieService.list(movieDOLambdaQueryWrapper);
        //获取每个电影在user_label中出现的次数，采用hashMap存储,key为movie_id,value为所有的label出现次数
        HashMap<Long, Integer> hashMap = new HashMap<>();
        for (MovieDO item : movieDOList) {
            Integer count = 0;
            //年份
            //将电影的year转化为label_id
            LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper = new LambdaQueryWrapper<LabelDO>()
                    .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .eq(LabelDO::getName, item.getYear())
                    .eq(LabelDO::getKind, KindEnum.YEAR.getCode());
            LabelDO labelDO = this.getOne(labelDOLambdaQueryWrapper);
            if (year.contains(labelDO.getId().toString())) {//user_label的year字符串包含该电影year
                count++;
            }
            //地区
            //从AreaEnum找出电影的地区中文，再通过地区中文查询出label_id
            for (AreaEnum areaEnum : AreaEnum.values()) {
                if (item.getArea().equals(areaEnum.getCode())) {
                    LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper1 = new LambdaQueryWrapper<LabelDO>()
                            .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                            .eq(LabelDO::getKind, KindEnum.AREA.getCode())
                            .eq(LabelDO::getName, areaEnum.getArea());
                    LabelDO labelDO1 = this.getOne(labelDOLambdaQueryWrapper1);
                    if (area.contains(labelDO1.getId().toString())) {
                        count++;
                    }
                }
            }
            //类型
            //从movie_type表中根据movie_id查询出type字符串
            //注意该type字符串的id不等于label_id，所以要再进行转换
            LambdaQueryWrapper<MovieTypeDO> movieTypeDOLambdaQueryWrapper = new LambdaQueryWrapper<MovieTypeDO>()
                    .eq(MovieTypeDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .eq(MovieTypeDO::getMovieId, item.getId());
            String movieTypes = movieTypeService.getOne(movieTypeDOLambdaQueryWrapper).getTypeId();
            //根据type_id查询出type_name，再根据type_name查询出label_id
            List<Long> movieTypeIds = Arrays.asList(movieTypes.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
            LambdaQueryWrapper<TypeDO> typeDOLambdaQueryWrapper = new LambdaQueryWrapper<TypeDO>()
                    .eq(TypeDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .in(TypeDO::getId, movieTypeIds);
            List<String> typeNameList = typeService.list(typeDOLambdaQueryWrapper).stream().map(TypeDO::getName).collect(Collectors.toList());
            for (String typeItem : typeNameList) {//此处不采用forEach
                //forEach使用的是lambda表达式，可以简单的把lambda表达式理解为匿名内部类
                //而匿名内部类的变量必须用final修饰
                LambdaQueryWrapper<LabelDO> labelDOLambdaQueryWrapper2 = new LambdaQueryWrapper<LabelDO>()
                        .eq(LabelDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                        .eq(LabelDO::getKind, KindEnum.GENRE.getCode())
                        .eq(LabelDO::getName, typeItem);
                LabelDO labelDO2 = this.getOne(labelDOLambdaQueryWrapper2);
                if (type.contains(labelDO2.getId().toString())) {
                    count++;
                }
            }
            hashMap.put(item.getId(), count);
        }
        //按照value对hashMap进行排序
        List<Map.Entry<Long, Integer>> sortList = new ArrayList<>(hashMap.entrySet());
        sortList.sort(new Comparator<Map.Entry<Long, Integer>>() {
            @Override
            public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        //获取推荐前十的电影
        List<Long> likeMovieIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            log.info(sortList.get(i).getKey() + ": " + sortList.get(i).getValue());
            //根据hashMap的key即movie_id查询出movie
            likeMovieIds.add(sortList.get(i).getKey());
        }
        LambdaQueryWrapper<MovieDO> movieDOLambdaQueryWrapper1 = new LambdaQueryWrapper<MovieDO>()
                .eq(MovieDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .in(MovieDO::getId, likeMovieIds);
        List<MovieDO> resultMovieDOList = movieService.list(movieDOLambdaQueryWrapper1);
        List<MovieDTO> result = ConversionUtils.transformList(resultMovieDOList, MovieDTO.class);
        return ResponseMessageDTO.success(result);
    }
}
