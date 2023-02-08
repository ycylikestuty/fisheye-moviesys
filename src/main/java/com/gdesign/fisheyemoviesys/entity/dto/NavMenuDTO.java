package com.gdesign.fisheyemoviesys.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ycy
 */
@Data
public class NavMenuDTO implements Serializable {
    private Long id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private Integer orderNum;
    private List<NavMenuDTO> children = new ArrayList<>();
}
