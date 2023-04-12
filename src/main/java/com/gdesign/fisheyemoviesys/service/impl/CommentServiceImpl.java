package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.constants.Constants;
import com.gdesign.fisheyemoviesys.entity.*;
import com.gdesign.fisheyemoviesys.entity.dto.CommentDTO;
import com.gdesign.fisheyemoviesys.entity.dto.MovieDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.enums.*;
import com.gdesign.fisheyemoviesys.entity.param.*;
import com.gdesign.fisheyemoviesys.mapper.CommentMapper;
import com.gdesign.fisheyemoviesys.service.*;
import com.gdesign.fisheyemoviesys.utils.ConversionUtils;
import com.gdesign.fisheyemoviesys.utils.SensitiveWordInit;
import com.gdesign.fisheyemoviesys.utils.SensitiveWordUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author ycy
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentDO> implements CommentService {
    @Resource
    private UserService userService;

    @Resource
    private MovieService movieService;

    @Resource
    private RoleService roleService;

    @Resource
    private UserStarService userStarService;

    @Resource
    private UserCollectService userCollectService;

    @Resource
    private SensitiveWordService sensitiveService;

    /**
     * 分页条件查询评论
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @Override
    public ResponseMessageDTO<PageResultDTO<CommentDTO>> pageQueryCommentByCondition(CommentQuery query) {
        PageResultDTO<CommentDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<CommentDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<CommentDO> queryWrapper = new LambdaQueryWrapper<>();

            //获取用户名类似的user_id集 和 电影名类似的movie_id集
            List<Long> userId = userService.getUserIdByLikeUserName(query.getUserName()).getResult();
            List<Long> movieId = movieService.getMovieIdByMovieName(query.getMovieName()).getResult();
            if (CollectionUtils.isEmpty(userId) || CollectionUtils.isEmpty(movieId)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            } else {
                //查询条件为空值或nll时，查询全部未被删除的评论记录
                queryWrapper.eq(CommentDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                        .in(CommentDO::getUserId, userId)
                        .in(CommentDO::getMovieId, movieId)
                        .eq(null != query.getStatus(), CommentDO::getStatus, query.getStatus())
                        .like(StringUtils.isNotBlank(query.getDetail()), CommentDO::getDetail, query.getDetail())
                        .orderByDesc(CommentDO::getCreateTime);
            }
            IPage<CommentDO> commentDOPage = this.page(page, queryWrapper);
            List<CommentDO> commentDORecords = commentDOPage.getRecords();
            if (CollectionUtils.isEmpty(commentDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            List<CommentDTO> commentDTOList = ConversionUtils.transformList(commentDORecords, CommentDTO.class);
            //设置返回的用户名、电影名
            commentDTOList.forEach(
                    item -> {
                        item.setMovieName(movieService.getById(item.getMovieId()).getName());
                        item.setUserName(userService.getById(item.getUserId()).getUsername());
                    }
            );
            pageResultDTO.setRows(commentDTOList);
            pageResultDTO.setTotal(commentDOPage.getTotal());
            pageResultDTO.setPageSize(commentDOPage.getSize());
            pageResultDTO.setPage(commentDOPage.getCurrent());
            pageResultDTO.setTotalPage(commentDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<CommentDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("评论分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<String> updateCommentStatus(CommentDTO commentDTO) {
        //获取前端传过来的状态，进行判断并修改
        Integer oldStatus = commentDTO.getStatus();
        Integer newStatus = oldStatus;
        //获取登录用户的权限
        //获得当前登录用户的用户名,注意不是昵称和id =》根据用户名获取角色id
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("当前登录的用户名为" + username);
        LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, username);
        UserDO userDO = userService.getOne(userQueryWrapper);
        LambdaQueryWrapper<RoleDO> roleQueryWrapper = new LambdaQueryWrapper<RoleDO>()
                .inSql(RoleDO::getId, "select role_id from user_role where user_id = " + userDO.getId());
        RoleDO roleDO = roleService.getOne(roleQueryWrapper);
        if (roleDO.getCode().equals(RoleEnum.ADMIN.getMsg())) {
            //身份为管理员，则对评论的状态只有三种：正常=》加精，加精=》正常，举报=》正常
            if (oldStatus.equals(CommentStatusEnum.NORMAL.getCode())) {//正常=》加精
                newStatus = CommentStatusEnum.REFINEMENT.getCode();
            } else {//举报=》正常，加精=》正常
                newStatus = CommentStatusEnum.NORMAL.getCode();
            }

        } else if (roleDO.getCode().equals(RoleEnum.ORDINARY.getMsg())) {
            //身份为普通用户，则对评论的状态只有一种：正常=》举报
            if (oldStatus.equals(CommentStatusEnum.NORMAL.getCode())) {
                newStatus = CommentStatusEnum.REPORT.getCode();
            }
        }
        //更新
        LambdaUpdateWrapper<CommentDO> updateWrapper = new LambdaUpdateWrapper<CommentDO>()
                .eq(CommentDO::getId, commentDTO.getId())
                .set(CommentDO::getStatus, newStatus);
        CommentDO commentDO = new CommentDO();
        if (this.update(commentDO, updateWrapper)) {
            return ResponseMessageDTO.success("修改成功");
        }
        return ResponseMessageDTO.success("修改失败");
    }

    @Override
    public ResponseMessageDTO<String> deleteCommentsByIds(Long[] ids) {
        List<Long> commentIds = Arrays.asList(ids);
        LambdaUpdateWrapper<CommentDO> commentDOLambdaUpdateWrapper = new LambdaUpdateWrapper<CommentDO>()
                .eq(CommentDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .set(CommentDO::getDeleted, DeleteEnum.DELETE.getCode())
                .in(CommentDO::getId, commentIds);
        //逻辑删除影评的同时删除用户收藏
        //将List<Long>转化为String类型
        String commentString = commentIds.stream().map(Objects::toString).collect(Collectors.joining("|"));
        log.info(commentString);
        LambdaQueryWrapper<UserCollectDO> userCollectDOLambdaQueryWrapper = new LambdaQueryWrapper<UserCollectDO>()
                .inSql(UserCollectDO::getId, "SELECT id FROM user_collect WHERE kind=2 and collect_ids REGEXP  ('" + commentString + "')");
        List<UserCollectDO> userCollectIds = userCollectService.list(userCollectDOLambdaQueryWrapper);
        userCollectIds.forEach(
                item -> {
                    String newCollectIds = item.getCollectIds();
                    //删除删除的影评id
                    List<String> collectIdsList = new ArrayList<>(Arrays.asList(newCollectIds.split(",")));
                    Iterator iterator = collectIdsList.iterator();
                    while (iterator.hasNext()) {
                        String items = (String) iterator.next();
                        commentIds.forEach(
                                itemComment -> {
                                    if (items.equals(itemComment.toString())) {
                                        iterator.remove();
                                    }
                                }
                        );
                    }
                    newCollectIds = collectIdsList.stream().collect(Collectors.joining(","));
                    LambdaUpdateWrapper<UserCollectDO> userCollectDOLambdaUpdateWrapper = new LambdaUpdateWrapper<UserCollectDO>()
                            .eq(UserCollectDO::getKind, CollectKindEnum.COMMENT.getCode())
                            .eq(UserCollectDO::getId, item.getId())
                            .set(UserCollectDO::getCollectIds, newCollectIds);
                    userCollectService.update(userCollectDOLambdaUpdateWrapper);
                }
        );
        CommentDO commentDO = new CommentDO();
        if (this.update(commentDO, commentDOLambdaUpdateWrapper)) {
            return ResponseMessageDTO.success("删除成功");
        }
        return ResponseMessageDTO.success("删除失败");
    }

    @Override
    public ResponseMessageDTO<CommentDTO> getRefinementCommentsByMovieId(Long movieId) {
        //根据电影id查询出唯一的加精影评
        LambdaQueryWrapper<CommentDO> queryWrapper = new LambdaQueryWrapper<CommentDO>()
                .eq(CommentDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                .eq(CommentDO::getMovieId, movieId)
                .eq(CommentDO::getStatus, CommentStatusEnum.REFINEMENT.getCode());
        CommentDO commentDO = this.getOne(queryWrapper);
        if (Objects.isNull(commentDO)) {
            //没有加精影评，返回空数据
            return ResponseMessageDTO.success(new CommentDTO());
        }
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(commentDO, commentDTO);
        //获得当前登录用户的用户名,注意不是昵称和id =》根据用户名获取用户id
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("当前登录的用户名为" + username);
        LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, username);
        UserDO userDO = userService.getOne(userQueryWrapper);
        //设置返回的用户昵称、用户头像、当前用户是否点赞、是否收藏
        commentDTO.setUserNickName(userService.getById(commentDTO.getUserId()).getNickname());
        commentDTO.setUserImg(userService.getById(commentDTO.getUserId()).getImg());
        commentDTO.setFlagStar(this.getStarByUserId(userDO.getId(), commentDTO.getId()));
        commentDTO.setFlagCollect(userCollectService.getCollectByUserId(userDO.getId(), commentDTO.getId(), CollectKindEnum.COMMENT.getCode()));
        return ResponseMessageDTO.success(commentDTO);
    }

    @Override
    public ResponseMessageDTO<PageResultDTO<CommentDTO>> getHotComments(SpecialCommentQuery query) {
        PageResultDTO<CommentDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<CommentDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<CommentDO> queryWrapper = new LambdaQueryWrapper<>();
            //查询全部未被删除的评论记录
            queryWrapper.eq(CommentDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .eq(CommentDO::getMovieId, query.getMovieId())
                    .ne(CommentDO::getStatus, CommentStatusEnum.REFINEMENT.getCode())
                    .orderByDesc(CommentDO::getStar);
            IPage<CommentDO> commentDOPage = this.page(page, queryWrapper);
            List<CommentDO> commentDORecords = commentDOPage.getRecords();
            if (CollectionUtils.isEmpty(commentDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            //获得当前登录用户的用户名,注意不是昵称和id =》根据用户名获取用户id
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.info("当前登录的用户名为" + username);
            LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<UserDO>()
                    .eq(UserDO::getUsername, username);
            UserDO userDO = userService.getOne(userQueryWrapper);
            List<CommentDTO> commentDTOList = ConversionUtils.transformList(commentDORecords, CommentDTO.class);
            //设置返回的用户昵称、用户头像、当前用户是否点赞、是否收藏
            commentDTOList.forEach(
                    item -> {
                        item.setUserImg(userService.getById(item.getUserId()).getImg());
                        item.setUserNickName(userService.getById(item.getUserId()).getNickname());
                        item.setFlagStar(this.getStarByUserId(userDO.getId(), item.getId()));
                        item.setFlagCollect(userCollectService.getCollectByUserId(userDO.getId(), item.getId(), CollectKindEnum.COMMENT.getCode()));
                    }
            );
            pageResultDTO.setRows(commentDTOList);
            pageResultDTO.setTotal(commentDOPage.getTotal());
            pageResultDTO.setPageSize(commentDOPage.getSize());
            pageResultDTO.setPage(commentDOPage.getCurrent());
            pageResultDTO.setTotalPage(commentDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<CommentDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("评论分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<PageResultDTO<CommentDTO>> getLastComments(SpecialCommentQuery query) {
        PageResultDTO<CommentDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<CommentDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<CommentDO> queryWrapper = new LambdaQueryWrapper<>();
            //查询全部未被删除的评论记录
            queryWrapper.eq(CommentDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .eq(CommentDO::getMovieId, query.getMovieId())
                    .ne(CommentDO::getStatus, CommentStatusEnum.REFINEMENT.getCode())
                    .orderByDesc(CommentDO::getCreateTime);
            IPage<CommentDO> commentDOPage = this.page(page, queryWrapper);
            List<CommentDO> commentDORecords = commentDOPage.getRecords();
            if (CollectionUtils.isEmpty(commentDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            //获得当前登录用户的用户名,注意不是昵称和id =》根据用户名获取用户id
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.info("当前登录的用户名为" + username);
            LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<UserDO>()
                    .eq(UserDO::getUsername, username);
            UserDO userDO = userService.getOne(userQueryWrapper);
            List<CommentDTO> commentDTOList = ConversionUtils.transformList(commentDORecords, CommentDTO.class);
            //设置返回的用户昵称、用户头像、当前用户是否点赞、是否收藏
            commentDTOList.forEach(
                    item -> {
                        item.setUserImg(userService.getById(item.getUserId()).getImg());
                        item.setUserNickName(userService.getById(item.getUserId()).getNickname());
                        item.setFlagStar(this.getStarByUserId(userDO.getId(), item.getId()));
                        item.setFlagCollect(userCollectService.getCollectByUserId(userDO.getId(), item.getId(), CollectKindEnum.COMMENT.getCode()));
                    }
            );
            pageResultDTO.setRows(commentDTOList);
            pageResultDTO.setTotal(commentDOPage.getTotal());
            pageResultDTO.setPageSize(commentDOPage.getSize());
            pageResultDTO.setPage(commentDOPage.getCurrent());
            pageResultDTO.setTotalPage(commentDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<CommentDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("评论分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public Boolean getStarByUserId(Long userId, Long commentId) {
        //考虑用户没有点赞记录的情况
        LambdaQueryWrapper<UserStarDO> queryWrapper = new LambdaQueryWrapper<UserStarDO>()
                .eq(UserStarDO::getUserId, userId);
        UserStarDO userStarDO = userStarService.getOne(queryWrapper);
        if (Objects.isNull(userStarDO)) {
            return false;
        }
        String commentIds = userStarDO.getCommentIds();
        if (commentIds.contains(String.valueOf(commentId)) == true) {
            return true;
        }
        return false;
    }

    @Override
    public ResponseMessageDTO<Boolean> updateStar(UserStarParam userStarParam) {
        //根据用户id查询出原来的点赞字符串
        LambdaQueryWrapper<UserStarDO> userStarDOLambdaQueryWrapper = new LambdaQueryWrapper<UserStarDO>()
                .eq(UserStarDO::getUserId, userStarParam.getUserId());
        //根据评论id查询出原来的点赞数
        LambdaQueryWrapper<CommentDO> commentDOLambdaQueryWrapper = new LambdaQueryWrapper<CommentDO>()
                .eq(CommentDO::getId, userStarParam.getCommentId());
        UserStarDO userStarDO = userStarService.getOne(userStarDOLambdaQueryWrapper);
        CommentDO commentDO = this.getOne(commentDOLambdaQueryWrapper);
        String newCommentIds = userStarDO.getCommentIds();
        Long newStar = commentDO.getStar();
        //根据评论id查询是否已经点赞
        if (this.getStarByUserId(userStarParam.getUserId(), userStarParam.getCommentId())) {//已经点赞则 减字符串 减点赞人数
            List<String> commentIdsList = new ArrayList<>(Arrays.asList(newCommentIds.split(",")));
            //使用Arrays.asList（）转化数组为list时生成的ArrayList实际上是Arrays的内部类ArrayList，不是java.util.ArrayList
            // 这两个ArrayList虽然都是继承 AbstractList，但是Arrays的内部类ArrayList没有重写 AbstractList的add和remove方法
            // 所以Arrays生成的ArrayList使用add或者remove方法会直接抛出UnsupportedOperationException异常
            //因此，在使用Arrays.asList（）做转化时，若要进行修改操作，就要再转化一次
            Iterator iterator = commentIdsList.iterator();
            while (iterator.hasNext()) {
                String item = (String) iterator.next();
                if (item.equals(userStarParam.getCommentId().toString())) {
                    iterator.remove();
                }
            }
            newCommentIds = commentIdsList.stream().collect(Collectors.joining(","));
            newStar--;
        } else {//否则 加字符串 加点赞人数
            newCommentIds += "," + userStarParam.getCommentId().toString();
            newStar++;
        }
        LambdaUpdateWrapper<UserStarDO> userStarDOLambdaUpdateWrapper = new LambdaUpdateWrapper<UserStarDO>()
                .eq(UserStarDO::getUserId, userStarParam.getUserId())
                .set(UserStarDO::getCommentIds, newCommentIds);
        LambdaUpdateWrapper<CommentDO> commentDOLambdaUpdateWrapper = new LambdaUpdateWrapper<CommentDO>()
                .eq(CommentDO::getId, userStarParam.getCommentId())
                .set(CommentDO::getStar, newStar);
        if (userStarService.update(userStarDOLambdaUpdateWrapper) && this.update(commentDOLambdaUpdateWrapper)) {
            return ResponseMessageDTO.success(true);
        }
        return ResponseMessageDTO.success(false);
    }

    @Override
    public ResponseMessageDTO<Boolean> updateCollect(UserCollectParam userCollectParam) {
        //根据用户id和收藏种类查询出原来的收藏字符串
        LambdaQueryWrapper<UserCollectDO> userCollectDOLambdaQueryWrapper = new LambdaQueryWrapper<UserCollectDO>()
                .eq(UserCollectDO::getUserId, userCollectParam.getUserId())
                .eq(UserCollectDO::getKind, userCollectParam.getKind());
        UserCollectDO userCollectDO = userCollectService.getOne(userCollectDOLambdaQueryWrapper);
        String newCollectIds = userCollectDO.getCollectIds();
        //若种类为评论即2则根据收藏id查询出原来的收藏数
        Long newCollect = Long.valueOf(-1);
        if (Objects.equals(userCollectParam.getKind(), CollectKindEnum.COMMENT.getCode())) {
            LambdaQueryWrapper<CommentDO> commentDOLambdaQueryWrapper = new LambdaQueryWrapper<CommentDO>()
                    .eq(CommentDO::getId, userCollectParam.getCollectId());
            CommentDO commentDO = this.getOne(commentDOLambdaQueryWrapper);
            newCollect = commentDO.getCollect();
        }
        //根据收藏id查询是否已经收藏
        if (userCollectService.getCollectByUserId(userCollectParam.getUserId(), userCollectParam.getCollectId(), userCollectParam.getKind())) {//已经收藏则 减字符串
            List<String> collectIdsList = new ArrayList<>(Arrays.asList(newCollectIds.split(",")));
            Iterator iterator = collectIdsList.iterator();
            while (iterator.hasNext()) {
                String item = (String) iterator.next();
                if (item.equals(userCollectParam.getCollectId().toString())) {
                    iterator.remove();
                }
            }
            newCollectIds = collectIdsList.stream().collect(Collectors.joining(","));
            if (Objects.equals(userCollectParam.getKind(), CollectKindEnum.COMMENT.getCode())) {
                //若种类为评论即2则更新收藏数
                newCollect--;
            }
        } else {//否则 加字符串
            newCollectIds += "," + userCollectParam.getCollectId().toString();
            if (Objects.equals(userCollectParam.getKind(), CollectKindEnum.COMMENT.getCode())) {
                newCollect++;
            }
        }


        //根据收藏id查询是否已经收藏
//        if (this.getCollectByUserId(userCollectParam.getUserId(), userCollectParam.getCollectId(),userCollectParam.getKind())) {//已经收藏则 减字符串 减收藏人数
//            List<String> collectIdsList = new ArrayList<>(Arrays.asList(newCollectIds.split(",")));
//            Iterator iterator = collectIdsList.iterator();
//            while (iterator.hasNext()) {
//                String item = (String) iterator.next();
//                if (item.equals(userCollectParam.getCollectId().toString())) {
//                    iterator.remove();
//                }
//            }
//            newCollectIds = collectIdsList.stream().collect(Collectors.joining(","));
//            newCollect--;
//        } else {//否则 加字符串 加点赞人数
//            newCollectIds += "," + userCollectParam.getCollectId().toString();
//            newCollect++;
//        }
        LambdaUpdateWrapper<UserCollectDO> userCollectDOLambdaUpdateWrapper = new LambdaUpdateWrapper<UserCollectDO>()
                .eq(UserCollectDO::getUserId, userCollectParam.getUserId())
                .eq(UserCollectDO::getKind, userCollectParam.getKind())
                .set(UserCollectDO::getCollectIds, newCollectIds);
        if (userCollectService.update(userCollectDOLambdaUpdateWrapper)) {
            if (Objects.equals(userCollectParam.getKind(), CollectKindEnum.COMMENT.getCode())) {
                LambdaUpdateWrapper<CommentDO> commentDOLambdaUpdateWrapper = new LambdaUpdateWrapper<CommentDO>()
                        .eq(CommentDO::getId, userCollectParam.getCollectId())
                        .set(CommentDO::getCollect, newCollect);
                if (this.update(commentDOLambdaUpdateWrapper)) {
                    return ResponseMessageDTO.success(true);
                }
            }
            return ResponseMessageDTO.success(true);
        }
        return ResponseMessageDTO.success(false);
    }

    @Override
    public ResponseMessageDTO<String> addComment(CommentDTO commentDTO) {
        //校验该用户是否被禁言
        LambdaQueryWrapper<UserDO> userDOLambdaQueryWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getId, commentDTO.getUserId());
        UserDO userDO = userService.getOne(userDOLambdaQueryWrapper);
        if (userDO.getStatus().equals(Constants.PROHIBITIONS_STATUS)) {
            return ResponseMessageDTO.success("您正被禁言，无法书写评论!");
        }
        //处理违禁词
        //1.初始化敏感词库对象
        SensitiveWordInit sensitiveWordInit = new SensitiveWordInit();
        //2.从数据库中获取敏感词对象集合
        List<SensitiveWordDO> sensitiveWords = sensitiveService.list();
        //3.构建敏感词库
        Map sensitiveWordMap = sensitiveWordInit.initKeyWord(sensitiveWords);
        //4.传入SensitivewordEngine类中的敏感词库
        SensitiveWordUtils.sensitiveWordMap = sensitiveWordMap;
        String detail = commentDTO.getDetail();
        //5.得到敏感词有哪些，传入2表示获取所有敏感词
        String newDetail = SensitiveWordUtils.replaceSensitiveWord(detail, 2, "*");
        CommentDO commentDO = new CommentDO();
        BeanUtils.copyProperties(commentDTO, commentDO);
        commentDO.setDetail(newDetail);
        if (this.save(commentDO)) {
            return ResponseMessageDTO.success("发表成功");
        }
        return ResponseMessageDTO.success("发表失败");
    }

    @Override
    public ResponseMessageDTO<Boolean> updateCommentStatusById(Long commentId) {
        LambdaUpdateWrapper<CommentDO> updateWrapper = new LambdaUpdateWrapper<CommentDO>()
                .eq(CommentDO::getId, commentId)
                .set(CommentDO::getStatus, CommentStatusEnum.REPORT.getCode());
        CommentDO commentDO = new CommentDO();
        if (this.update(commentDO, updateWrapper)) {
            return ResponseMessageDTO.success(true);
        }
        return ResponseMessageDTO.success(false);
    }

    @Override
    public ResponseMessageDTO<PageResultDTO<CommentDTO>> getLastCollectComments(UserCollectQuery query) {
        PageResultDTO<CommentDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<CommentDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<CommentDO> queryWrapper = new LambdaQueryWrapper<>();

            //根据用户id和kind查询收藏字符串
            LambdaQueryWrapper<UserCollectDO> userCollectDOLambdaQueryWrapper = new LambdaQueryWrapper<UserCollectDO>()
                    .eq(UserCollectDO::getUserId, query.getUserId())
                    .eq(UserCollectDO::getKind, query.getKind());
            UserCollectDO userCollectDO = userCollectService.getOne(userCollectDOLambdaQueryWrapper);
            if (Objects.isNull(userCollectDO)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            String collectIds = userCollectDO.getCollectIds();
            //将sting类型的字符串转化为List<Long>类型即3,6,8=》[3,6,8]
            List<Long> collectId = Arrays.asList(collectIds.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
            //查询全部未被删除的评论记录
            queryWrapper.eq(CommentDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .in(CommentDO::getId, collectId)
                    .orderByDesc(CommentDO::getCreateTime);
            IPage<CommentDO> commentDOPage = this.page(page, queryWrapper);
            List<CommentDO> commentDORecords = commentDOPage.getRecords();
            if (CollectionUtils.isEmpty(commentDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            List<CommentDTO> commentDTOList = ConversionUtils.transformList(commentDORecords, CommentDTO.class);
            pageResultDTO.setRows(commentDTOList);
            pageResultDTO.setTotal(commentDOPage.getTotal());
            pageResultDTO.setPageSize(commentDOPage.getSize());
            pageResultDTO.setPage(commentDOPage.getCurrent());
            pageResultDTO.setTotalPage(commentDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<CommentDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("评论分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<PageResultDTO<CommentDTO>> getHotCollectComments(UserCollectQuery query) {
        PageResultDTO<CommentDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<CommentDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<CommentDO> queryWrapper = new LambdaQueryWrapper<>();

            //根据用户id和kind查询收藏字符串
            LambdaQueryWrapper<UserCollectDO> userCollectDOLambdaQueryWrapper = new LambdaQueryWrapper<UserCollectDO>()
                    .eq(UserCollectDO::getUserId, query.getUserId())
                    .eq(UserCollectDO::getKind, query.getKind());
            UserCollectDO userCollectDO = userCollectService.getOne(userCollectDOLambdaQueryWrapper);
            if (Objects.isNull(userCollectDO)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            String collectIds = userCollectDO.getCollectIds();
            //将sting类型的字符串转化为List<Long>类型即3,6,8=》[3,6,8]
            List<Long> collectId = Arrays.asList(collectIds.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
            //查询全部未被删除的评论记录
            queryWrapper.eq(CommentDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .in(CommentDO::getId, collectId)
                    .orderByDesc(CommentDO::getCollect);
            IPage<CommentDO> commentDOPage = this.page(page, queryWrapper);
            List<CommentDO> commentDORecords = commentDOPage.getRecords();
            if (CollectionUtils.isEmpty(commentDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            List<CommentDTO> commentDTOList = ConversionUtils.transformList(commentDORecords, CommentDTO.class);
            pageResultDTO.setRows(commentDTOList);
            pageResultDTO.setTotal(commentDOPage.getTotal());
            pageResultDTO.setPageSize(commentDOPage.getSize());
            pageResultDTO.setPage(commentDOPage.getCurrent());
            pageResultDTO.setTotalPage(commentDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<CommentDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("评论分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<Boolean> deleteCollect(UserCollectParam param) {
        //根据用户id、kind查询收藏字符串
        LambdaQueryWrapper<UserCollectDO> userCollectDOLambdaQueryWrapper = new LambdaQueryWrapper<UserCollectDO>()
                .eq(UserCollectDO::getUserId, param.getUserId())
                .eq(UserCollectDO::getKind, param.getKind());
        UserCollectDO userCollectDO = userCollectService.getOne(userCollectDOLambdaQueryWrapper);
        String newCollect = userCollectDO.getCollectIds();
        //删除该收藏
        List<String> collectIdsList = new ArrayList<>(Arrays.asList(newCollect.split(",")));
        Iterator iterator = collectIdsList.iterator();
        while (iterator.hasNext()) {
            String item = (String) iterator.next();
            if (item.equals(param.getCollectId().toString())) {
                iterator.remove();
            }
        }
        newCollect = collectIdsList.stream().collect(Collectors.joining(","));
        LambdaUpdateWrapper<UserCollectDO> userCollectDOLambdaUpdateWrapper = new LambdaUpdateWrapper<UserCollectDO>()
                .eq(UserCollectDO::getId, userCollectDO.getId())
                .set(UserCollectDO::getCollectIds, newCollect);
        //若收藏类型为评论，则减少删除数量
        if (Objects.equals(param.getKind(), CollectKindEnum.COMMENT.getCode())) {
            LambdaQueryWrapper<CommentDO> commentDOLambdaQueryWrapper = new LambdaQueryWrapper<CommentDO>()
                    .eq(CommentDO::getId, param.getCollectId());
            CommentDO commentDO = this.getOne(commentDOLambdaQueryWrapper);
            Long newCollectNum = commentDO.getCollect() - 1;
            LambdaUpdateWrapper<CommentDO> commentDOLambdaUpdateWrapper = new LambdaUpdateWrapper<CommentDO>()
                    .eq(CommentDO::getId, commentDO.getId())
                    .set(CommentDO::getCollect, newCollectNum);
            this.update(new CommentDO(), commentDOLambdaUpdateWrapper);
        }
        if (userCollectService.update(new UserCollectDO(), userCollectDOLambdaUpdateWrapper)) {
            return ResponseMessageDTO.success(true);
        }
        return ResponseMessageDTO.success(false);
    }

    @Override
    public ResponseMessageDTO<PageResultDTO<CommentDTO>> getCommentsByUserId(UserCommentQuery query) {
        PageResultDTO<CommentDTO> pageResultDTO = new PageResultDTO<>();
        try {
            Page<CommentDO> page = new Page<>();
            page.setCurrent(query.getPageNum());
            page.setSize(query.getPageSize());
            LambdaQueryWrapper<CommentDO> queryWrapper = new LambdaQueryWrapper<CommentDO>()
                    .eq(CommentDO::getDeleted, DeleteEnum.NO_DELETE.getCode())
                    .eq(CommentDO::getUserId, query.getUserId())
                    .orderByDesc(CommentDO::getCreateTime);
            IPage<CommentDO> commentDOPage = this.page(page, queryWrapper);
            List<CommentDO> commentDORecords = commentDOPage.getRecords();
            if (CollectionUtils.isEmpty(commentDORecords)) {
                return ResponseMessageDTO.success(PageResultDTO
                        .<CommentDTO>builder()
                        .page(Long.valueOf(query.getPageNum()))
                        .pageSize(Long.valueOf(query.getPageSize()))
                        .rows(Collections.emptyList())
                        .total(Constants.EMPTY_NUM)
                        .build());
            }
            List<CommentDTO> commentDTOList = ConversionUtils.transformList(commentDORecords, CommentDTO.class);
            //设置返回的电影名
            commentDTOList.forEach(
                    item -> {
                        item.setMovieName(movieService.getById(item.getMovieId()).getName());
                    }
            );
            pageResultDTO.setRows(commentDTOList);
            pageResultDTO.setTotal(commentDOPage.getTotal());
            pageResultDTO.setPageSize(commentDOPage.getSize());
            pageResultDTO.setPage(commentDOPage.getCurrent());
            pageResultDTO.setTotalPage(commentDOPage.getPages());
        } catch (Exception e) {
            return ResponseMessageDTO
                    .<PageResultDTO<CommentDTO>>builder()
                    .code(CodeEnum.QUERY_ERROR.getCode())
                    .message("评论分页异常")
                    .success(Boolean.FALSE)
                    .build();
        }
        return ResponseMessageDTO.success(pageResultDTO);
    }

    @Override
    public ResponseMessageDTO<String> updateCommentDetail(CommentDTO commentDTO) {
        String detail = commentDTO.getDetail();
        //处理评论中是否有违禁词
        //1.初始化敏感词库对象
        SensitiveWordInit sensitiveWordInit = new SensitiveWordInit();
        //2.从数据库中获取敏感词对象集合
        List<SensitiveWordDO> sensitiveWords = sensitiveService.list();
        //3.构建敏感词库
        Map sensitiveWordMap = sensitiveWordInit.initKeyWord(sensitiveWords);
        //4.传入SensitivewordEngine类中的敏感词库
        SensitiveWordUtils.sensitiveWordMap = sensitiveWordMap;
        //5.得到敏感词有哪些，传入2表示获取所有敏感词
        String newDetail = SensitiveWordUtils.replaceSensitiveWord(detail, 2, "*");
        LambdaUpdateWrapper<CommentDO> updateWrapper = new LambdaUpdateWrapper<CommentDO>()
                .eq(CommentDO::getId, commentDTO.getId())
                .set(CommentDO::getDetail, newDetail);
        CommentDO commentDO = new CommentDO();
        if (this.update(commentDO, updateWrapper)) {
            return ResponseMessageDTO.success("修改成功");
        }
        return ResponseMessageDTO.success("修改失败");
    }
}
