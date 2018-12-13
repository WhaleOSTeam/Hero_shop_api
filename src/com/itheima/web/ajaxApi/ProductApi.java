package com.itheima.web.ajaxApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.itheima.domain.Category;
import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import com.itheima.utils.JedisPoolUtils;
import com.itheima.web.servlet.BaseServlet;

import net.sf.json.JSONObject;
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
	/*
	 * 根据商品的类别获得商品的列表
	 * http://localhost:7070/Hero_shop_api/productApi?method=productList&cid=1&currentPage=1&currentCount=5
	 * */
	public void productList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String productListJson = "";
		//获得-cid
		String cid = request.getParameter("cid");
        //获取当前页数-currentPage
		String currentPageStr = request.getParameter("currentPage");
		if(currentPageStr==null) currentPageStr="1";
		int currentPage = Integer.parseInt(currentPageStr);
		//获取每页显示多少条
		String currentCount = request.getParameter("currentCount");
		if(currentCount==null) currentCount="12";
		int psize = Integer.parseInt(currentCount);
		

		ProductService service = new ProductService();
		PageBean<Product> pageBean = service.findProductListByCid(cid,currentPage,psize);
		Gson gson = new Gson();
		productListJson = gson.toJson(pageBean);
		//request.setAttribute("pageBean", pageBean);
		//request.setAttribute("cid", cid);

		//定义一个记录历史商品信息的集合
		//List<Product> historyProductList = new ArrayList<Product>();

		//获得客户端携带名字叫pids的cookie
		/*Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie cookie:cookies){
				if("pids".equals(cookie.getName())){
					String pids = cookie.getValue();//3-2-1
					String[] split = pids.split("-");
					for(String pid : split){
						Product pro = service.findProductByPid(pid);
						historyProductList.add(pro);
					}
				}
			}
		}*/

		//将历史记录的集合放到域中
		//request.setAttribute("historyProductList", historyProductList);
		
		response.getWriter().write(productListJson);
	}
	//http://localhost:7070/Hero_shop_api/productApi?method=demo
	public void demo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//https://www.cnblogs.com/joahyau/p/6736637.html
		String[] strArr = {"办公厅","政策研究室","法制司"};
		ArrayList<Map<String,String>> json = new ArrayList<Map<String,String>>();
		
		for(int i = 0 ; i < strArr.length; i++) {
			Map<String,String> map = new HashMap<String, String>();
			
			map.put("pname",strArr[i]);
			map.put("name", "name"+i);
			json.add(map);
		}
		System.out.println(json);
		//JSONObject jsons = JSONObject.fromObject(json);
		
//			JSONObject object = new JSONObject();
//			JSONObject resultGroup = new JSONObject();
//			for(int i=0;i<17;i++) {
//				JSONObject resultInfo = new JSONObject();
//				String key = "key"+i;
//				String name = "name"+i;
//				String othername = "othername"+i;
//				String creditcode = "creditcode"+i;
//				String ywfw = "ywfw"+i;
//				String address = "address"+i;
//				resultInfo.put(name, i*i);
//				resultInfo.put(othername, i*i);
//				resultInfo.put(creditcode, i*i);
//				resultInfo.put(ywfw, i*i);
//				resultInfo.put(address, i*i);
//				
//				resultGroup.put(key, resultInfo);
//			}
//			object.put("result", resultGroup);
//			System.out.println(resultGroup);
		//response.getWriter().println(jsons);
	}
}
