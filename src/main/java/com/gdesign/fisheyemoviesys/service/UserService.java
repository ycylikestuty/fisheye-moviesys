package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gdesign.fisheyemoviesys.entity.UserDO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.Result;
import com.gdesign.fisheyemoviesys.entity.dto.UserDTO;
import com.gdesign.fisheyemoviesys.entity.param.UserQuery;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author ycy
 */
public interface UserService extends IService<UserDO> {
    /**
     * 根据准确的用户名查询
     *
     * @param username 准确的用户名
     * @return 用户信息
     */
    ResponseMessageDTO<UserDTO> getUserByUserName(String username);

    /**
     * 根据模糊的用户名查询
     *
     * @param username 模糊的用户名
     * @return 用户id集
     */
    ResponseMessageDTO<List<Long>> getUserIdByLikeUserName(String username);

    String getUserAuthorityByUserName(String username);

    /**
     * 分页条件查询用户
     *
     * @param query 查询条件
     * @return 用户集合
     */
    ResponseMessageDTO<PageResultDTO<UserDTO>> pageQueryUserByCondition(UserQuery query);

    /**
     * 修改用户
     *
     * @param userDTO 用户信息
     * @return 修改成功与否
     */
    ResponseMessageDTO<String> updateUser(UserDTO userDTO);

    /**
     * 删除用户
     *
     * @param ids 用户id
     * @return删除成功与否
     */
    ResponseMessageDTO<String> deleteUser(Long[] ids);

    /**
     * 修改用户头像
     *
     * @param file
     * @param userJson
     * @return
     */
    Result updateImage(MultipartFile file, String userJson) throws JsonProcessingException;

    /**
     * 修改用户密码
     *
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @param principal
     * @return
     */
    Result updatePassword(String oldPassword, String newPassword, String confirmPassword, Principal principal);

    /**
     * 注册用户
     * @param userDTO 用户信息
     * @return 注册成功与否
     */
//    Result registerUser(UserDTO userDTO);

    /**
     * 更改用户端信息
     * @param userDTO 用户信息
     * @return 是否更改成功
     */
    ResponseMessageDTO<String> updateUserInfo(UserDTO userDTO);
}
