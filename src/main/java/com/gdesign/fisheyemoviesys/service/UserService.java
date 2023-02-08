package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.UserDO;
import com.gdesign.fisheyemoviesys.entity.dto.PageResultDTO;
import com.gdesign.fisheyemoviesys.entity.dto.ResponseMessageDTO;
import com.gdesign.fisheyemoviesys.entity.dto.UserDTO;
import com.gdesign.fisheyemoviesys.entity.param.UserQuery;

/**
 * @author ycy
 */
public interface UserService extends IService<UserDO> {
    ResponseMessageDTO<UserDTO> getUserByUserName(String username);

    String getUserAuthorityByUserName(String username);

    /**
     * 分页条件查询用户
     * @param query 查询条件
     * @return 用户集合
     */
    ResponseMessageDTO<PageResultDTO<UserDTO>> pageQueryUserByCondition(UserQuery query);

    /**
     * 修改用户
     * @param userDTO 用户消息
     * @return 修改成功与否
     */
    ResponseMessageDTO<String> updateUser(UserDTO userDTO);

    /**
     * 删除用户
     * @param ids 用户id
     * @return删除成功与否
     */
    ResponseMessageDTO<String> deleteUser(Long[] ids);

}
