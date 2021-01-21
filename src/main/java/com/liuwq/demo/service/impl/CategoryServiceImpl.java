package com.liuwq.demo.service.impl;

import com.liuwq.demo.dao.CategoryMapper;
import com.liuwq.demo.entity.Category;
import com.liuwq.demo.enums.ResponseEnum;
import com.liuwq.demo.service.ICategoryService;
import com.liuwq.demo.vo.CategoryIdVo;
import com.liuwq.demo.vo.CategoryVo;
import com.liuwq.demo.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    public CategoryMapper categoryMapper;

    @Override
    public ResponseVo<List<CategoryVo>> selectAll(){
        List<Category> categories = categoryMapper.selectAll();
        if(categories==null){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        List<CategoryVo> categoryVoLists = new ArrayList<>();

        for(Category category:categories){
            if(category.getParentId().equals(0)){
                CategoryVo categoryVo = new CategoryVo();
                BeanUtils.copyProperties(category,categoryVo);
                categoryVoLists.add(categoryVo);
            }
        }

        findSubCategory(categoryVoLists,categories);
        categoryVoLists.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
        return ResponseVo.success(categoryVoLists);


    };

    private void findSubCategory(List<CategoryVo> categoryVoLists, List<Category> categories){

//先遍历一级类目  再遍历所有的  找出Pid == id;
        for(CategoryVo categoryVoList:categoryVoLists){

            List<CategoryVo> subCategoryVolist = new ArrayList<>();

            for(Category category:categories){

                if (categoryVoList.getId().equals(category.getParentId())){
                    CategoryVo subCategoryVo = new CategoryVo();
                    BeanUtils.copyProperties(category,subCategoryVo);
                    subCategoryVolist.add(subCategoryVo);
                  //
                }
            }
            subCategoryVolist.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
            categoryVoList.setSubCategories(subCategoryVolist);
            findSubCategory(categoryVoList.getSubCategories(),categories);

        }
    }
//得到所有子类目的ID
    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet){
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id,resultSet,categories);
    }

    @Override
    public ResponseVo<CategoryIdVo> selectCategory(Integer id) {

       Category category = categoryMapper.selectByPrimaryKey(id);

       if(category!=null){
           CategoryIdVo categoryIdVo =new CategoryIdVo();
           BeanUtils.copyProperties(category,categoryIdVo);
           return ResponseVo.success(categoryIdVo);
       }
        return ResponseVo.error(ResponseEnum.ERROR);
    };

    public void findSubCategoryId(Integer id, Set<Integer> resultSet,List<Category> categories){
        for (Category category:categories){
            if(category.getParentId().equals(id)){
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(),resultSet,categories);
            }

        }
    }



}
