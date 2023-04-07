package com.gdesign.fisheyemoviesys.controller;

import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.param.UserStarParam;
import com.gdesign.fisheyemoviesys.service.CommentService;
import com.gdesign.fisheyemoviesys.service.UserStarService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/userStar")
public class UserStarController {
    @Resource
    private CommentService commentService;

    @PostMapping(path = "/updateUserStar")
    public ResponseMessageDTO<Boolean> updateUserStar(@RequestBody UserStarParam param){
        return commentService.updateStar(param);
    }
}
