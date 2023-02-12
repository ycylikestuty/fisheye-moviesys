package com.gdesign.fisheyemoviesys.controller;

import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.TypeDTO;
import com.gdesign.fisheyemoviesys.service.TypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ycy
 */
@RestController
@RequestMapping("/type")
public class TypeController {
    @Resource
    private TypeService typeService;

    @GetMapping(path = "/list")
    public ResponseMessageDTO<List<TypeDTO>> getAllType() {
        return typeService.getAllType();
    }

    @PostMapping(path = "/getTypeById")
    /**
     * 该方法只需传递一个参数，可以使用注解@RequestParam
     * 也可以使用JSONObject与@RequestBody注解配合接收参数
     * 也可以让前端只传送该参数的json（采用本方法）
     */
    public ResponseMessageDTO<List<TypeDTO>> getTypeById(@RequestBody Long movieId) {
//        Long movieId=object.getLong("movieId");
        return typeService.getTypeById(movieId);
    }

}
