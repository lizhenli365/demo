package com.liuwq.demo.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuwq.demo.dao.ProductMapper;
import com.liuwq.demo.entity.Product;
import com.liuwq.demo.enums.ProductStatusEnum;
import com.liuwq.demo.enums.ResponseEnum;
import com.liuwq.demo.service.ICategoryService;
import com.liuwq.demo.service.IProductService;
import com.liuwq.demo.vo.ProductDetailVo;
import com.liuwq.demo.vo.ProductVo;
import com.liuwq.demo.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by 廖师兄
 */
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

	@Autowired
	ICategoryService categoryService;

	@Autowired
	ProductMapper productMapper;

	@Override
	public ResponseVo<ProductDetailVo> detail(Integer productId) {
		Product product = productMapper.selectByPrimaryKey(productId);

		if(product==null||
				product.getStatus().equals(ProductStatusEnum.OFF_SALE.getCode())||
				product.getStatus().equals(ProductStatusEnum.DELETE.getCode())
		){
			return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
		}

		ProductDetailVo productDetailVo = new ProductDetailVo();

		BeanUtils.copyProperties(product,productDetailVo);
		productDetailVo.setStock(productDetailVo.getStock()>100?100:productDetailVo.getStock());
		return ResponseVo.success(productDetailVo);
	}

	@Override
	public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
		//得到所有的子ID
		Set<Integer> categoryIdSet = new HashSet();


        //加上自己
		if(categoryId!=null){
			categoryIdSet.add(categoryId);

			categoryService.findSubCategoryId(categoryId,categoryIdSet);
		}


		//请求 pageNum第几页  pageSize 每页多少
		PageHelper.startPage(pageNum, pageSize);
		List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);

		List<ProductVo> ProductVoList = new ArrayList();

		for(Product product:productList){
			ProductVo productVo = new ProductVo();
		    BeanUtils.copyProperties(product,productVo);
			ProductVoList.add(productVo);
		}

		PageInfo pageInfo = new PageInfo<>(productList);
		pageInfo.setList(ProductVoList);

		return ResponseVo.success(pageInfo);
	}

}
