package com.gdesign.fisheyemoviesys.constants;

/**
 * @author ycy
 */
public interface Constants {
    /**
     * redis存储的验证码的key
     */
    String CAPTCHA_KEY = "captcha";

    /**
     * 默认页码
     */
    Integer DEFAULT_PAGE_NO = 1;

    /**
     * 默认每页大小
     */
    Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 空值
     */
    Long EMPTY_NUM = 0L;

    /**
     * 普通用户值
     */
    Long ORDINARY_NUM = 2L;

    /**
     * 用户禁言状态
     */
    Integer PROHIBITIONS_STATUS=1;

    /**
     * 后台管理项目网址
     */
    String ADMIN_WEB = "http://localhost:8200";

    /**
     * 用户项目网址
     */
    String USER_WEB = "http://localhost:8300";

    /**
     * 头像存储路径
     */
    String IMG_PATH = "http://localhost:8100/img";

    /**
     * 电影海报存储路径
     */
    String POSTER_PATH="http://localhost:8100/poster";

    /**
     * 电影查询时查询全部
     */
    String MOVIE_ALL="全部";
}
