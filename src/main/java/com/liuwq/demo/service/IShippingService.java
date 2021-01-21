package com.liuwq.demo.service;

import com.github.pagehelper.PageInfo;
import com.liuwq.demo.vo.ResponseVo;
import form.ShippingForm;

import java.util.Map;

/**
 * Created by 廖师兄
 */
public interface IShippingService {

	ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form);
//
	ResponseVo delete(Integer uid, Integer shippingId);
//
	ResponseVo update(Integer uid, Integer shippingId, ShippingForm form);
//
	ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);
}
