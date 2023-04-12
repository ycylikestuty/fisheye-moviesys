package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gdesign.fisheyemoviesys.entity.MovieDO;
import com.gdesign.fisheyemoviesys.entity.dto.MovieDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.Result;
import com.gdesign.fisheyemoviesys.entity.param.MovieQuery;
import com.gdesign.fisheyemoviesys.entity.param.SpecialMovieQuery;
import com.gdesign.fisheyemoviesys.entity.param.UserCollectQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ycy
 */
public interface MovieService extends IService<MovieDO> {

    /**
     * 分页条件查询
     *
     * @param query 查询条件
     * @return 电影集
     */
    ResponseMessageDTO<PageResultDTO<MovieDTO>> pageQueryMovieByCondition(MovieQuery query);

    /**
     * 根据传入的类型名集合获取电影
     *
     * @param typeList 类型名集
     * @return 电影id集
     */
    ResponseMessageDTO<List<Long>> getMoviesByTypeId(List<String> typeList);

    /**
     * 逻辑删除电影
     *
     * @param ids 电影id
     * @return 是否删除成功
     */
    ResponseMessageDTO<String> deleteMovie(Long[] ids);

    /**
     * 修改电影
     *
     * @param movieDTO 电影信息
     * @return 修改成功与否
     */
    ResponseMessageDTO<String> updateMovie(MovieDTO movieDTO);

    /**
     * 新增电影
     *
     * @param movieDTO 电影信息
     * @return 新增成功与否
     */
    ResponseMessageDTO<String> addMovie(MovieDTO movieDTO);

    /**
     * 根据电影名模糊查询电影id
     *
     * @param movieName 电影名
     * @return 电影id集
     */
    ResponseMessageDTO<List<Long>> getMovieIdByMovieName(String movieName);

    /**
     * 获取高分的十个电影
     * @return
     */
    ResponseMessageDTO<List<MovieDTO>> getHighScoreMovies();

    /**
     * 根据用户id获取推荐用户的电影
     * @param userId 用户id
     * @return 推荐电影集合
     */
//    ResponseMessageDTO<List<MovieDTO>> getUserLikeMovies(Long userId);

    /**
     * 根据id查询电影
     * @param Id
     * @return 电影
     */
    ResponseMessageDTO<MovieDTO> getMovieById(Long Id);

    /**
     * 更新电影的评分
     * @param movieDTO 电影
     * @return 更新成功
     */
    ResponseMessageDTO<Boolean> updateMovieScore(MovieDTO movieDTO);

    /**
     * 分页条件查询
     *
     * @param query 查询条件
     * @return 电影集
     */
    ResponseMessageDTO<PageResultDTO<MovieDTO>> getCollectMovies(UserCollectQuery query);

    /**
     * 根据电影名称获取电影集合
     * @param movieName 电影名称
     * @return 电影集合
     */
    ResponseMessageDTO<List<MovieDTO>> getMoviesByName(String movieName);

    /**
     * 获取电影的全部年份
     * @return
     */
    ResponseMessageDTO<List<String>> getAllMovieYear();

    /**
     * 获取电影的全部地区
     * @return
     */
    ResponseMessageDTO<List<String>> getAllMovieArea();

    /**
     * 分页条件查询
     *
     * @param query 查询条件
     * @return 电影集
     */
    ResponseMessageDTO<PageResultDTO<MovieDTO>> getAllMovieByTypeAreaYear(SpecialMovieQuery query);

    /**
     * 返回电影url
     *
     * @param file
     * @return
     */
    ResponseMessageDTO<String> getPosterUrl(MultipartFile file) ;
}
