package com.gdesign.fisheyemoviesys.controller;

import com.gdesign.fisheyemoviesys.entity.UserLabelDO;
import com.gdesign.fisheyemoviesys.entity.dto.LabelDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.UserLabelDTO;
import com.gdesign.fisheyemoviesys.entity.param.UserStarParam;
import com.gdesign.fisheyemoviesys.service.UserLabelService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/userLabel")
public class UserLabelController {
    @Resource
    private UserLabelService userLabelService;

}
