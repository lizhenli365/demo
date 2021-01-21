package com.liuwq.demo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.liuwq.demo.enums.ResponseEnum;
import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseVo<T> {
    private Integer status;

    private String msg;

    private T data;

    private ResponseVo() {}

    private ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    //失败 无数据返回
    public static ResponseVo error(ResponseEnum responseEnum) {
        return new ResponseVo(responseEnum.getCode(), responseEnum.getDesc());
    }

    //失败 无数据返回
    public static ResponseVo error(String msg) {
        return new ResponseVo(ResponseEnum.ERROR.getCode(), msg);
    }

    //成功 无数据返回
    public static <T> ResponseVo<T> success() {
        return new ResponseVo(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDesc());
    }

    //成功 有数据返回
    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo(ResponseEnum.SUCCESS.getCode(), data);
    }


}
