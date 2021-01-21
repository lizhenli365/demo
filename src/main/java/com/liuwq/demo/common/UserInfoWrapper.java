package com.liuwq.demo.common;

import com.liuwq.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserInfoWrapper {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 返回当前登陆用户id
     * @return
     */
    public Integer getLoginUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader("token");
        Object o = redisTemplate.opsForValue().get(token);
        if (o != null) {
            User user = (User) o;
            return user.getId();
        }
        return null;
    }

}
