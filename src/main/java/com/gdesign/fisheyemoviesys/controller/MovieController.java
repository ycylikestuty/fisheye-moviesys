package com.gdesign.fisheyemoviesys.controller;

import com.gdesign.fisheyemoviesys.annotation.Log;
import com.gdesign.fisheyemoviesys.entity.dto.MovieDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.param.MovieQuery;
import com.gdesign.fisheyemoviesys.service.MovieService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/movie")
public class MovieController {

    @Resource
    private MovieService movieService;

    @PostMapping(path = "/list")
    public ResponseMessageDTO<PageResultDTO<MovieDTO>> list(@RequestBody MovieQuery movieQuery) {
        return movieService.pageQueryMovieByCondition(movieQuery);
    }

    @DeleteMapping(path = "/delete")
    @Log(title = "电影列表", businessType = "删除电影")
    @PreAuthorize("hasAnyAuthority('movie:delete')")
    public ResponseMessageDTO<String> delete(Long[] ids) {
        return movieService.deleteMovie(ids);
    }

    @PostMapping(path = "/update")
    @Log(title = "电影列表", businessType = "修改电影")
    @PreAuthorize("hasAnyAuthority('movie:update')")
    public ResponseMessageDTO<String> update(@RequestBody MovieDTO movieDTO) {
        return movieService.updateMovie(movieDTO);
    }

    @PostMapping(path = "/add")
    @Log(title = "电影列表", businessType = "新增电影")
    @PreAuthorize("hasAnyAuthority('movie:add')")
    public ResponseMessageDTO<String> add(@RequestBody MovieDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }

}
