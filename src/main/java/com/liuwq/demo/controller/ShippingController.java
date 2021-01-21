package com.liuwq.demo.controller;


import com.liuwq.demo.service.IShippingService;
import com.liuwq.demo.vo.ResponseVo;
import form.ShippingForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by 廖师兄
 */
@RestController
public class ShippingController {

	@Autowired
	private IShippingService shippingService;

	@PostMapping("/shippings")
	public ResponseVo add(@Valid @RequestBody ShippingForm form) {

		return shippingService.add(10, form);
	}

	@DeleteMapping("/shippings/{shippingId}")
	public ResponseVo delete(@PathVariable Integer shippingId) {

		return shippingService.delete(1, shippingId);
	}
//
	@PutMapping("/shippings/{shippingId}")
	public ResponseVo update(@PathVariable Integer shippingId,
							 @Valid @RequestBody ShippingForm form) {

		return shippingService.update(10, shippingId, form);
	}
//
	@GetMapping("/shippings")
	public ResponseVo list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
						   @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

		return shippingService.list(10, pageNum, pageSize); }
}
