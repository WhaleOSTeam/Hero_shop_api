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
import com.itheima.web.servlet.BaseServlet;

public class AdminCategoryApi extends BaseServlet{
	private static final long serialVersionUID = 1L;
	/*
	 * 查所有的分类
	 * http://localhost:7070/Hero_shop_api/categoryApi?method=findAllCategory&isOpen=
	 * */
	public void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//提供一个List<Category> 转成json字符串
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		List<Category> categoryList = null ;
		String isOpen = request.getParameter("isOpen");
		System.out.println(isOpen == null || isOpen.equals(""));
		if(isOpen == null || isOpen.equals("")) {
			categoryList = service.findAllCategory();
		}
		else{
			int openStat =   Integer.parseInt(isOpen);
			categoryList = service.findCategoryByState(openStat);
		}
		Gson gson = new Gson();
		String json = gson.toJson(categoryList);
		
		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().write(json);
	}
	/*
	 *  增加一个分类 
	 *  http://localhost:7070/Hero_shop_api/categoryApi?method=addCategory&cname=打折皮肤&isOpen=0
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
	 *  http://localhost:7070/Hero_shop_api/categoryApi?method=updataCategory&cid=3cc21f25-687a-433a-8a3e-62a0796a15bd&cname=
	 **/
	public void updataCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//提供一个List<Category> 转成json字符串
		
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		//获得id名
		String cid = request.getParameter("cid");
		//获得分类名
		String cname = request.getParameter("cname");
		cname =  URLDecoder.decode((new String(cname.getBytes("ISO8859-1"), "UTF-8")), "UTF-8");

		Boolean isOK = service.updataCategory(cid,cname);
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
	 *  开启或关闭一个分类
	 *  http://localhost:7070/Hero_shop_api/categoryApi?method=categoryState&cid=3cc21f25-687a-433a-8a3e-62a0796a15bd&isOpen=1
	 **/
	public void categoryState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//提供一个List<Category> 转成json字符串
		
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		//获得分类名
		String cid = request.getParameter("cid");
		//获取开启状态
		String isOpen = request.getParameter("isOpen");
		int openStat =   Integer.parseInt(isOpen);
		
		Boolean isOK = service.categoryState(cid,openStat);
		
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
