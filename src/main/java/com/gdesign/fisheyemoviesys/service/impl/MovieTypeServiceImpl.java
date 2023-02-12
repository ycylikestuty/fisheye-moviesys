package com.gdesign.fisheyemoviesys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdesign.fisheyemoviesys.entity.MovieTypeDO;
import com.gdesign.fisheyemoviesys.mapper.MovieTypeMapper;
import com.gdesign.fisheyemoviesys.service.MovieTypeService;
import org.springframework.stereotype.Service;

/**
 * @author ycy
 */
@Service
public class MovieTypeServiceImpl extends ServiceImpl<MovieTypeMapper, MovieTypeDO> implements MovieTypeService {
}
