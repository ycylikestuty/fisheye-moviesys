package com.gdesign.fisheyemoviesys.controller;

import com.gdesign.fisheyemoviesys.annotation.Log;
import com.gdesign.fisheyemoviesys.entity.dto.CommentDTO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.param.CommentQuery;
import com.gdesign.fisheyemoviesys.entity.param.SpecialCommentQuery;
import com.gdesign.fisheyemoviesys.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @PostMapping(path = "/list")
    public ResponseMessageDTO<PageResultDTO<CommentDTO>> list(@RequestBody CommentQuery query) {
        return commentService.pageQueryCommentByCondition(query);
    }

    @PostMapping(path = "/update")
    @Log(title = "影评列表", businessType = "修改影评")
    @PreAuthorize("hasAnyAuthority('comment:update')")
    public ResponseMessageDTO<String> update(@RequestBody CommentDTO commentDTO) {
        return commentService.updateCommentStatus(commentDTO);
    }

    @DeleteMapping(path = "/delete")
    @Log(title = "影评列表", businessType = "删除影评")
    @PreAuthorize("hasAnyAuthority('comment:delete')")
    public ResponseMessageDTO<String> delete(Long[] ids) {
        return commentService.deleteCommentsByIds(ids);
    }

    @PostMapping(path = "/getRefinementComment")
    public ResponseMessageDTO<CommentDTO> getRefinementCommentsByMovieId(@RequestParam Long movieId){
        return commentService.getRefinementCommentsByMovieId(movieId);
    }

    @PostMapping(path = "/getHotCommentList")
    public ResponseMessageDTO<PageResultDTO<CommentDTO>> getHotComments(@RequestBody SpecialCommentQuery query) {
        return commentService.getHotComments(query);
    }

    @PostMapping(path = "/getLastCommentList")
    public ResponseMessageDTO<PageResultDTO<CommentDTO>> getLastComments(@RequestBody SpecialCommentQuery query) {
        return commentService.getLastComments(query);
    }

    @PostMapping(path = "/addComment")
    public ResponseMessageDTO<Boolean> addComment(@RequestBody CommentDTO commentDTO) {
        return commentService.addComment(commentDTO);
    }

    @PostMapping(path = "/updateCommentStatusById")
    public ResponseMessageDTO<Boolean> updateCommentStatusById(@RequestParam Long commentId) {
        return commentService.updateCommentStatusById(commentId);
    }
}
