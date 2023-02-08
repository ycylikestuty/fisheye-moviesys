package com.gdesign.fisheyemoviesys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdesign.fisheyemoviesys.entity.MenuDO;
import com.gdesign.fisheyemoviesys.entity.dto.NavMenuDTO;

import java.util.List;

/**
 * @author ycy
 */
public interface MenuService extends IService<MenuDO> {
    List<NavMenuDTO> getCurrentUserNav();
}
