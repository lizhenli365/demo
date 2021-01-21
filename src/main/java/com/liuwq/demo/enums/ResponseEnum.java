package com.liuwq.demo.enums;

import com.alibaba.fastjson.JSONObject;
import com.liuwq.demo.vo.ResponseVo;
import lombok.Getter;


@Getter
public enum ResponseEnum {

	//ERROR(-1, "服务端错误"),
	ERROR(-1, "服务端错误"),

	SUCCESS(0, "成功"),

//	PASSWORD_ERROR(1,"密码错误"),

	USERNAME_EXIST(2, "用户名已存在"),

//	PARAM_ERROR(3, "参数错误"),

	EMAIL_EXIST(4, "邮箱已存在"),

	USERNAME_OR_PASSWORD_ERROR(5,"密码或账号有误"),
	TOKEN_EMPTYE_ERROR(6,"token不能为空"),
	TOKEN_INVALID_ERROR(7,"token不存在或已过期"),
	PRODUCT_OFF_SALE_OR_DELETE(12, "商品下架或删除"),

	VALIDATE_FAILED(13, "参数检验失败"),
	PROODUCT_STOCK_ERROR(15, "库存不正确"),
	PRODUCT_NOT_EXIST(14, "商品不存在"),

	CART_PRODUCT_NOT_EXIST(16, "购物车里无此商品"),

	DELETE_SHIPPING_FAIL(17, "删除收货地址失败"),
	;

	Integer code;

	String desc;

	ResponseEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	@Override
	public String toString() {
		ResponseVo responseVo = ResponseVo.error(this);
		return JSONObject.toJSONString(responseVo);
	}
}
