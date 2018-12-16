package com.itheima.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.itheima.domain.Category;
import com.itheima.service.AdminService;
import com.itheima.service.CategoryService;
import com.itheima.utils.BeanFactory;
import com.itheima.utils.JedisPoolUtils;
import com.itheima.utils.NoteResult;

import redis.clients.jedis.Jedis;

/**
 * http://localhost:8080/Hero_shop_api/categoryApi/findAllCategory.do?isOpen=1
 * @author Administrator
 *
 */
@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;
	
	@RequestMapping("/categoryApi/findAllCategory.do")
	@ResponseBody
	public List<Category> findAllCategory(String isOpen){
		List<Category> categoryList = null ;
		if(isOpen == null || isOpen.equals("")) {
			categoryList = service.findAllCategory();
		}
		return categoryList;
	}
}
