package com.liuwq.demo.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisCache {
    /**
     * key的前缀
     * @return
     */
    String keyPrefix();

    /**
     * 过期时间，默认-1 代表不设置过期时间
     * @return
     */
    long expire() default -1L;

    /**
     * 过期时间单位，默认秒
     * @return
     */
    TimeUnit expireTimeUnit() default TimeUnit.SECONDS;
}
