package com.itheima.web.ajaxApi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.itheima.domain.BackMassage;
import com.itheima.domain.Category;
import com.itheima.service.AdminService;
import com.itheima.utils.BeanFactory;
import com.itheima.utils.CommonsUtils;
import com.itheima.utils.JedisPoolUtils;
import com.itheima.web.servlet.BaseServlet;

import redis.clients.jedis.Jedis;

public class AdminCategoryApi extends BaseServlet{
	private static final long serialVersionUID = 1L;
	/*
	 * 查所有的分类
	 * http://39.105.112.212:8080/Hero_shop_api/categoryApi?method=findAllCategory&isOpen=
	 * http://localhost:7070/Hero_shop_api/categoryApi?method=findAllCategory&isOpen=
	 * */
	public void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		Gson gson = new Gson();
		String json = "";
		//提供一个List<Category> 转成json字符串
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		List<Category> categoryList = null ;
		String isOpen = request.getParameter("isOpen");
		System.out.println(isOpen == null || isOpen.equals(""));
		//全部查询
		if(isOpen == null || isOpen.equals("")) {
			Jedis jedis = JedisPoolUtils.getJedis();
			String categoryListJson = jedis.get("categoryListJson");
			if(categoryListJson==null){
				//从数据库中查
				System.out.println("从数据库中查");
				categoryList = service.findAllCategory();
				json = gson.toJson(categoryList);
				jedis.set("categoryListJson", json);
			}else {
				//使用缓存中的数据
				 System.out.println("从redis中查");
				json = categoryListJson;
			}
			
		}
		//条件查询
		else{
			int openStat =   Integer.parseInt(isOpen);
			categoryList = service.findCategoryByState(openStat);
			json = gson.toJson(categoryList);
		}
		
		 
		
	
		
		response.getWriter().write(json);
	}
	/*
	 *  增加一个分类 
	 *  http://39.105.112.212:8080/Hero_shop_api/categoryApi?method=addCategory&cname=清仓处理&isOpen=0
	 **/
	public void addCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//提供一个List<Category> 转成json字符串
		
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		//Map<String, String[]> map = request.getParameterMap();
		//获得分类名
		String cname = request.getParameter("cname");
		cname =  URLDecoder.decode((new String(cname.getBytes("ISO8859-1"), "UTF-8")), "UTF-8");
		Category c = new Category();
		c.setCname(cname);
		//获取开启状态
		String isOpen = request.getParameter("isOpen");
		int openStat =   Integer.parseInt(isOpen);
		c.setIsOpen(openStat);
//		try {
//			BeanUtils.populate(c, map);
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		c.setCid(CommonsUtils.getUUID());
		Boolean isOK = service.addCategory(c);
		//System.out.println(isOK);
		
		BackMassage ms = new BackMassage();
		if(isOK) {
			ms.setError(0);
			ms.setMessage("success");
		}else {
			ms.setError(1);
			ms.setMessage("fail");
		}
		Gson gson = new Gson();
		String json = gson.toJson(ms);
		response.getWriter().write(json);
	}
	/*
	 *  编辑一个分类
	 *  http://localhost:7070/Hero_shop_api/categoryApi?
	 *  method=updataCategory&cid=&cname=&isOpen=
	 **/
	public void updataCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//提供一个List<Category> 转成json字符串
		
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		//获得id名
		String cid = request.getParameter("cid");
		//获得分类名
		String cname = request.getParameter("cname");
		cname =  URLDecoder.decode((new String(cname.getBytes("ISO8859-1"), "UTF-8")), "UTF-8");
        //获得开启关闭状态
		String isOpen = request.getParameter("isOpen");
		int openStat =   Integer.parseInt(isOpen);
		Boolean isOK = service.updataCategory(cid,cname,openStat);
		//System.out.println(isOK);
		
		BackMassage ms = new BackMassage();
		if(isOK) {
			ms.setError(0);
			ms.setMessage("success");
		}else {
			ms.setError(1);
			ms.setMessage("fail");
		}
		Gson gson = new Gson();
		String json = gson.toJson(ms);
		response.getWriter().write(json);
	}
}
