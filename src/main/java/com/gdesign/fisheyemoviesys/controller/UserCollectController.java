package com.gdesign.fisheyemoviesys.controller;

import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.param.UserCollectParam;
import com.gdesign.fisheyemoviesys.entity.param.UserStarParam;
import com.gdesign.fisheyemoviesys.service.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/userCollect")
public class UserCollectController {
    @Resource
    private CommentService commentService;

    @PostMapping(path = "/updateUserCollect")
    public ResponseMessageDTO<Boolean> updateUserStar(@RequestBody UserCollectParam param){
        return commentService.updateCollect(param);
    }
}
