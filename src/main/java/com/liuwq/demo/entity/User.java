package com.liuwq.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

// 生成set，get方法
@Data
// 生成toString方法
@ToString
// 生成无参构造函数
//@NoArgsConstructor
// 生成有参构造函数
@AllArgsConstructor
@Mapper
public class User {

    private Integer id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

    private Date createTime;

    private Date updateTime;

    public User() {
    }

    public User(String username, String password, String email, Integer role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

}
