package com.liuwq.demo.service;

import com.liuwq.demo.vo.CategoryIdVo;
import com.liuwq.demo.vo.CategoryVo;
import com.liuwq.demo.vo.ResponseVo;

import java.util.List;
import java.util.Set;

/**
 * Created by 廖师兄
 */
public interface ICategoryService {

	 ResponseVo<List<CategoryVo>> selectAll();

     void findSubCategoryId(Integer id, Set<Integer> resultSet);

     ResponseVo<CategoryIdVo> selectCategory(Integer id);
}
