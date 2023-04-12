package com.gdesign.fisheyemoviesys.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gdesign.fisheyemoviesys.annotation.Log;
import com.gdesign.fisheyemoviesys.entity.dto.*;
import com.gdesign.fisheyemoviesys.entity.param.MovieQuery;
import com.gdesign.fisheyemoviesys.entity.param.SpecialMovieQuery;
import com.gdesign.fisheyemoviesys.entity.param.UserCollectQuery;
import com.gdesign.fisheyemoviesys.service.LabelService;
import com.gdesign.fisheyemoviesys.service.MovieService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/movie")
public class MovieController {

    @Resource
    private MovieService movieService;

    @Resource
    private LabelService labelService;

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

    @GetMapping(path = "/getHighScoreMovies")
    public ResponseMessageDTO<List<MovieDTO>> getHighScoreMovies() {
        return movieService.getHighScoreMovies();
    }

    @PostMapping(path = "/getMovie")
    public ResponseMessageDTO<MovieDTO> getMovieById(@RequestParam Long Id) {
        return movieService.getMovieById(Id);
    }

    @PostMapping(path = "/updateMovieScore")
    public ResponseMessageDTO<Boolean> updateMovieScore(@RequestBody MovieDTO movieDTO) {
        return movieService.updateMovieScore(movieDTO);
    }

    @PostMapping(path = "/getCollectMovies")
    public ResponseMessageDTO<PageResultDTO<MovieDTO>> getCollectMovies(@RequestBody UserCollectQuery query) {
        return movieService.getCollectMovies(query);
    }

    @PostMapping(path = "/getMoviesByName")
    public ResponseMessageDTO<List<MovieDTO>> getMoviesByName(@RequestBody String movieName) {
        return movieService.getMoviesByName(movieName);
    }

    @GetMapping(path = "/getAllMovieYear")
    public ResponseMessageDTO<List<String>> getAllMovieYear() {
        return movieService.getAllMovieYear();
    }

    @GetMapping(path = "/getAllMovieArea")
    public ResponseMessageDTO<List<String>> getAllMovieArea() {
        return movieService.getAllMovieArea();
    }

    @PostMapping(path = "/getAllMovieByTypeAreaYear")
    public ResponseMessageDTO<PageResultDTO<MovieDTO>> getAllMovieByTypeAreaYear(@RequestBody SpecialMovieQuery query) {
        return movieService.getAllMovieByTypeAreaYear(query);
    }

    @PostMapping(path = "/getUserLikeMovies")
    public ResponseMessageDTO<List<MovieDTO>> getUserLikeMovies(@RequestParam Long userId){
        return labelService.getUserLikeMovies(userId);
    }

    @PostMapping(path = "/getPosterUrl")
    public ResponseMessageDTO<String> getPosterUrl(@RequestBody MultipartFile file)  {
        return movieService.getPosterUrl(file);
    }

}
