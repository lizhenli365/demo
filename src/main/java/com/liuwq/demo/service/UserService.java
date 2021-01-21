package com.liuwq.demo.service;

import com.liuwq.demo.common.CommonPage;
import com.liuwq.demo.entity.User;
import com.liuwq.demo.vo.ResponseVo;
import form.UserUpdateForm;

public interface UserService {
    /**
     * 注册
     */
    ResponseVo register(User user);

    /**
     * 登录生成token
     */
    ResponseVo<String> login(String username, String password);

    /**
     * 登陆根据token获取用户信息
     * @param token
     * @return
     */
    ResponseVo<User> getInfo(String token);

    ResponseVo<User> update(String token, UserUpdateForm userUpdateForm);

    ResponseVo<CommonPage> getUserList(Integer loginUserId, Integer pageNum, Integer pageSize);
}
