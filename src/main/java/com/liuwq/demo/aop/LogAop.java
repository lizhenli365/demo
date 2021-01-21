package com.liuwq.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAop {

    /**
     * 切入点
     * 匹配com.liuwq.demo.controller包及其子包下的所有类的所有方法
     */
    @Pointcut("execution(* com.liuwq.demo.controller.*.*(..))")
    public void pointCut(){
    }

    @Before("pointCut()")
    public void beforeAdvice(JoinPoint joinPoint){
        System.out.println("----------- 2.前置通知 -----------");
    }

    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void afterThrowingAdvice(JoinPoint joinPoint, NullPointerException e){
        System.out.println("----------- 后置异常通知 -----------");
    }

    @AfterReturning(value = "pointCut()", returning = "keys")
    public void afterReturningAdvice(JoinPoint joinPoint, String keys){
        System.out.println("----------- 后置返回通知 -----------");
        System.out.println("后置返回通知的返回值：" + keys);
    }

    @After("pointCut()")
    public void afterAdvice(){
        System.out.println("----------- 4.最终通知 -----------");
    }

    /**
     * A、前置通知：使用org.aspectj.lang.annotation 包下的@Before注解声明。
     * B、后置返回通知：使用org.aspectj.lang.annotation 包下的@AfterReturning注解声明。
     * C、后置异常通知：使用org.aspectj.lang.annotation 包下的@AfterThrowing注解声明。
     * D、后置最终通知：使用org.aspectj.lang.annotation 包下的@After注解声明。
     * E、环绕通知：使用org.aspectj.lang.annotation 包下的@Around注解声明。
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        System.out.println("----------- 1.前环绕通知 -----------");
        // 记录下请求内容
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        Object proceed = null;
        try {
            // 执行目标方法
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("----------- 5.后环绕通知 -----------");
        // 处理完请求，返回内容
        log.info("RESPONSE : " + proceed);
        return proceed;
    }

}
