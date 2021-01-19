package com.liuwq.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.liuwq.demo.annotation.RedisCache;
import com.liuwq.demo.common.CommonPage;
import com.liuwq.demo.dao.UserMapper;
import com.liuwq.demo.entity.User;
import com.liuwq.demo.enums.ResponseEnum;
import com.liuwq.demo.enums.RoleEnum;
import com.liuwq.demo.service.UserService;
import com.liuwq.demo.vo.ResponseVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 注册
     */
   @Override
   public ResponseVo register(User user){
//       userMapper.countByEmail();
       //username不能重复

       int countByUsername = userMapper.countByUsername(user.getUsername());
       int countByEmail = userMapper.countByEmail(user.getEmail());


       if(countByUsername>0){
           return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
       }
       if(countByEmail>0){
           return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
       }
//设置管理员身份
       user.setRole(RoleEnum.ADMIN.getCode());

//       System.out.println("user.getPassword()="+user.getPassword());
//       System.out.println("user.getPassword().getBytes(StandardCharsets.UTF_8)="+user.getPassword().getBytes(StandardCharsets.UTF_8));
//       System.out.println("DigestUtils="+DigestUtils.md5DigestAsHex(
//               user.getPassword().getBytes(StandardCharsets.UTF_8)
//       ));

       //MD5摘要算法(Spring自带) 密码加密
       user.setPassword(DigestUtils.md5DigestAsHex(
               user.getPassword().getBytes(StandardCharsets.UTF_8)
       ));

       int resultCount = userMapper.insertSelective(user);
       if (resultCount == 0) {
           return ResponseVo.error(ResponseEnum.ERROR);
       }
       return ResponseVo.success();

    };

    /**
     * 登录
     */
    /*public ResponseVo<User> login(String username, String password){
        User user =  userMapper.selectByUsername(username);

        if(user!=null){
            if(user.getPassword().equalsIgnoreCase(
                    DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)
            ))){
                user.setPassword("");
                // 把用戶信息存儲到redis，key=token，随机数，
                String token = UUID.randomUUID().toString().replace("-", "");
                redisTemplate.opsForValue().set(token, user.getId());
                return ResponseVo.success(user);
            }

            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);

    };*/

    public ResponseVo<String> login(String username, String password){
        User user =  userMapper.selectByUsername(username);
        if(user!=null){
            if(user.getPassword().equalsIgnoreCase(
                    DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)
                    ))){
                user.setPassword("");
                // 把用戶信息存儲到redis，key=token，随机数，
                String token = UUID.randomUUID().toString().replace("-", "");
                redisTemplate.opsForValue().set(token, user, 60 * 60 * 24 * 7, TimeUnit.SECONDS);
                return ResponseVo.success(token);
            }

            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);

    }

    public ResponseVo<User> getInfo(String token){

        if(!StringUtils.isEmpty(token)){
            // 根据token，从redis中获取用户id
            User user = (User) redisTemplate.opsForValue().get(token);
            return ResponseVo.success(user);
        }
        return ResponseVo.error(ResponseEnum.TOKEN_INVALID_ERROR);

    }

    @Override
    @RedisCache(keyPrefix = "mall_user_getUserList")
    public ResponseVo<CommonPage> getUserList(Integer loginUserId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.getUserList();
        CommonPage<User> userCommonPage = CommonPage.restPage(userList);
        return ResponseVo.success(userCommonPage);
    }

    ;

    /*public static void main(String[] args) {
        String s = UUID.randomUUID().toString();
        System.out.println(s);
        String token = UUID.randomUUID().toString().replace("-", "");
        System.out.println(token);
    }*/
}
