package com.liuwq.demo.aop;

import com.liuwq.demo.annotation.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class RedisCacheAop {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 切入点
     * 匹配com.liuwq.demo.controller包及其子包下的所有类的所有方法
     */
    @Pointcut("@annotation(com.liuwq.demo.annotation.RedisCache)")
    public void aa() {
    }

    @Around("aa()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Exception {
        // 1 拿到目标方法参数，数组
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        if (args == null || args.length == 0 || args[0] == null) {
            log.error("{} 方法中没有传入id参数", methodName);
            throw new IllegalAccessException("err : " + methodName + " aop method parameter is null");
        }

        // 2 获取目标方法注解的属性
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RedisCache redisCache = method.getAnnotation(RedisCache.class);
        String keyPrefix = redisCache.keyPrefix();
        long expire = redisCache.expire();
        TimeUnit expireTimeUnit = redisCache.expireTimeUnit();
        if (StringUtils.isEmpty(keyPrefix)) {
            log.error("{} aop method parameter must have kepPrefix property", methodName);
            throw new IllegalAccessException("err : " + methodName + " aop method parameter must have kepPrefix property");
        }

        String key = new StringBuilder().append(keyPrefix).append("_").append(args[0]).toString();
        log.info("保存到redis的key为[{}]", key);
        synchronized (key.intern()) {
            // 3 redis中获取到数据，不需要执行目标方法，直接返回数据
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj != null) {
                log.info("缓存中获取到数据：[{}]", obj);
                return obj;
            }

            // 4 redis中获取不到数据，执行目标方法查询数据，并放入redis中
            try {
                obj = joinPoint.proceed();
                if (expire > 0) {
                    redisTemplate.opsForValue().set(key, obj, expire, expireTimeUnit);
                } else {
                    redisTemplate.opsForValue().set(key, obj);
                }
                log.info("执行目标方法查询到数据：[{}]，放入redis中", obj);
            } catch (Throwable t) {
                log.error("执行目标方法异常:", t);
                t.printStackTrace();
            }
            return obj;
        }
    }

}
