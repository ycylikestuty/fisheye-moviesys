package com.gdesign.fisheyemoviesys.entity.param;

import com.gdesign.fisheyemoviesys.constants.Constants;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author ycy
 * 通用分页查询类
 */
public class CommonPageQuery implements Serializable {
    private static final long serialVersionUID = -6211958147440623942L;

    /**
     * 页号
     */
    private Integer pageNum;

    /**
     * 分页大小
     */
    private Integer pageSize;

    public Integer getPageSize() {
        return Objects.isNull(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
    }

    public Integer getPageNum() {
        return Objects.isNull(pageNum) ? Constants.DEFAULT_PAGE_NO : pageNum;
    }
}
