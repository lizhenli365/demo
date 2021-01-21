package com.liuwq.demo.controller;

import com.liuwq.demo.common.CommonPage;
import com.liuwq.demo.common.UserInfoWrapper;
import com.liuwq.demo.entity.User;
import com.liuwq.demo.enums.ResponseEnum;
import com.liuwq.demo.service.UserService;
import com.liuwq.demo.vo.ResponseVo;
import form.UserLoginForm;
import form.UserRegisterForm;
import form.UserUpdateForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    UserInfoWrapper userInfoWrapper;

    @PostMapping("register")
    public ResponseVo register(@Validated @RequestBody UserRegisterForm userForm, BindingResult bindingResult){

        if (bindingResult != null && bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if(fieldError != null){
                return ResponseVo.error(ResponseEnum.VALIDATE_FAILED);
            }
        }
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        return userService.register(user);
       // return ResponseVo.success(0,"成功");
    }

    @PostMapping("login")
    public ResponseVo<String> login(@RequestBody UserLoginForm userForm){
        return userService.login(userForm.getUsername(),userForm.getPassword());
    }

    @PostMapping("getInfo")
    public ResponseVo<User> getInfo(@RequestParam String token){
        return userService.getInfo(token);
    }

    @PostMapping("update")
    public ResponseVo<User> update(@RequestParam String token,@RequestBody UserUpdateForm userUpdateForm){
        return userService.update(token,userUpdateForm);
    }

    @GetMapping("getUserList")
    public ResponseVo<CommonPage> getUserList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        Integer loginUserId = userInfoWrapper.getLoginUserId();
        return userService.getUserList(loginUserId, pageNum, pageSize);
    }
}
