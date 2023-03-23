package com.gdesign.fisheyemoviesys.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.gdesign.fisheyemoviesys.constants.Constants;
import com.gdesign.fisheyemoviesys.entity.dto.Result;
import com.gdesign.fisheyemoviesys.entity.dto.UserDTO;
import com.gdesign.fisheyemoviesys.service.UserService;
import com.gdesign.fisheyemoviesys.utils.RedisUtil;
import com.gdesign.fisheyemoviesys.utils.UploadUtil;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ycy
 */
@RestController
@Slf4j
public class KaptchaController {
    @Resource
    private Producer producer;

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        //生成随机码，代表某一个用户
        String key = UUID.randomUUID().toString().replace("-", "");
        //生成字符串验证码
        String code = producer.createText();
        //生成验证码图片
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        //使用base64对图片进行编码
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String img = str + encoder.encode(outputStream.toByteArray());
        //设置过期时间为2分钟
        redisUtil.hset(Constants.CAPTCHA_KEY, key, code, 2 * 60);
        System.out.println("key：" + key);
        System.out.println("code：" + code);
        //返回随机码和验证码图片给前端 为map形式
//        return ResponseMessageDTO.success(
//                //map的key必须要与前端对应
//                MapUtil.builder()
//                .put("base64", img)
//                .put("key", key)
//                .build());
        return Result.succ(
                MapUtil.builder().put("base64", img).put("key", key).build()
        );
    }

    @GetMapping("/userInfo")
    public Result userInfo(Principal principal) {
//        UserDTO userDTO = (UserDTO) redisUtil.get("User:" + principal.getName());
        UserDTO userDTO = userService.getUserByUserName(principal.getName()).getResult();
        Map<String, Object> map = new HashMap<>();
        map.put("user", userDTO);
        return Result.succ(map);
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        if (!file.isEmpty()) {
            String uploadImg = UploadUtil.uploadImg(file);
            if (StrUtil.isEmpty(uploadImg)) {
                return Result.fail("图片上传失败");
            }
            return Result.succ(Constants.IMG_PATH + uploadImg);
        }
        return Result.fail("图片上传失败！");
    }
}
