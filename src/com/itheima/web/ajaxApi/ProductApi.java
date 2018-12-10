package com.itheima.web.ajaxApi;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.itheima.domain.Category;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import com.itheima.utils.JedisPoolUtils;
import com.itheima.web.servlet.BaseServlet;

import redis.clients.jedis.Jedis;

public class ProductApi extends BaseServlet{
	private static final long serialVersionUID = 1L;
	
	//显示首页热门商品和最新商品
	//http://localhost:7070/Hero_shop_api/productApi?method=index&prdTpye=new
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		Gson gson = new Gson();
		String prolist = "";
		String isHot = request.getParameter("prdTpye");
		if(isHot.equals("hot")) {
			//准备热门商品---List<Product>
			List<Product> hotProductList = service.findHotProductList();
			prolist = gson.toJson(hotProductList);
		}else if(isHot.equals("new")) {
			//准备最新商品---List<Product>
			System.out.println(isHot);
			List<Product> newProductList = service.findNewProductList();
			prolist = gson.toJson(newProductList);
		}

		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(prolist);

	}
	//显示商品的类别的的功能
	//http://localhost:7070/Hero_shop_api/productApi?method=categoryList
	public void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		//先从缓存中查询categoryList 如果有直接使用 没有在从数据库中查询 存到缓存中
		//1、获得jedis对象 连接redis数据库
		Jedis jedis = JedisPoolUtils.getJedis();
		String categoryListJson = jedis.get("categoryListJson");
		//2、判断categoryListJson是否为空
		if(categoryListJson==null){
			System.out.println("缓存没有数据 查询数据库");
			//准备分类数据
			List<Category> categoryList = service.findAllCategory();
			Gson gson = new Gson();
			categoryListJson = gson.toJson(categoryList);
			jedis.set("categoryListJson", categoryListJson);
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}
}
