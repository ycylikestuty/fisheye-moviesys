package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.MovieDO;
import com.gdesign.fisheyemoviesys.entity.dto.MovieDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.param.MovieQuery;

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
}
