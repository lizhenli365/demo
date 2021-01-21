package com.liuwq.demo.interceptor;

import com.liuwq.demo.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("==========jin  ru pre  handle ====");
        /*String requestURI = request.getRequestURI();
        if (requestURI.indexOf("/user/login") > -1 || requestURI.indexOf("/user/register") > -1) {
            return true;
        }*/

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html;charset=UTF-8");//这句话是解决乱码的
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            returnJson(response, ResponseEnum.TOKEN_EMPTYE_ERROR.toString());
            return false;
        }

        Object user = redisTemplate.opsForValue().get(token);
        if (user == null) {
            returnJson(response, ResponseEnum.TOKEN_INVALID_ERROR.toString());
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("================= jin ru  after ===============");
    }

    private void returnJson(HttpServletResponse response, String result){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(result);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("response error",e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }


}