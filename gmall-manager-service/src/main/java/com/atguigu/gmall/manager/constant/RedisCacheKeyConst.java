package com.atguigu.gmall.manager.constant;

/**
 * redis中所有缓存使用的key的规定
 */
public class RedisCacheKeyConst {

    public static final String SKU_INFO_PREFIX = "sku:";
    public static final String SKU_INFO_SUFFIX = ":info";
    public static final Integer SKU_INFO_TIMEOUT = 60*60*24;//缓存过期时间
    public static final Integer LOCK_TIMEOUT = 3;//锁的默认超时时间
    public static final String LOCK_SKU_INFO = "gmall:lock:sku";

    //商品热度在redis中保存的值
    public static final String SKU_HOT_SCORE = "gmall:sku:hotScore";

}
