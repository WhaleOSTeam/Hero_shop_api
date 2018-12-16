package com.itheima.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.itheima.dao.CategoryDao;
import com.itheima.domain.Category;
import com.itheima.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	@Resource
	private CategoryDao categoryDao;
	public List<Category> findAllCategory(){
		List<Category> list = categoryDao.findAllCategory();
		return list;
	}
}
