package com.itheima.web.ajaxApi;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.itheima.domain.BackMassage;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.web.servlet.BaseServlet;

public class UserApi extends BaseServlet{

	/**
	 * http://localhost:7070/Hero_shop_api/userApi?method=login&username=jacye.li&password=1234567&autoLogin=autoLogin
	 */
	private static final long serialVersionUID = 1L;
	//用户登录
		public void login(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			HttpSession session = request.getSession();

			//获得输入的用户名和密码
			String username = request.getParameter("username");
			String password = request.getParameter("password");
            String respJson = "";
            Gson gson = new Gson();
            BackMassage ms = new BackMassage();
			//将用户名和密码传递给service层
			UserService service = new UserService();
			//判断用户是否激活
			boolean isActive = false;
			
			try {
				isActive = service.getActiveState(username);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(isActive) {
				User user = null;
				try {
					user = service.login(username,password);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println(user);
				//判断用户是否登录成功 user是否是null
				if(user!=null){
					//判断用户是否激活
					
					//登录成功
					//***************判断用户是否勾选了自动登录*****************
					String autoLogin = request.getParameter("autoLogin");
					if("autoLogin".equals(autoLogin)){
						//要自动登录
						//创建存储用户名的cookie
						Cookie cookie_username = new Cookie("cookie_username",user.getUsername());
						cookie_username.setMaxAge(10*60);
						//创建存储密码的cookie
						Cookie cookie_password = new Cookie("cookie_password",user.getPassword());
						cookie_password.setMaxAge(10*60);
	
						response.addCookie(cookie_username);
						response.addCookie(cookie_password);
	
					}
					//将user对象存到session中
					session.setAttribute("user", user);
					
	                //登陆成功
					ms.setError(0);
					ms.setMessage("success");
					respJson = gson.toJson(ms);
				}else{
					ms.setError(1);
					ms.setMessage("username or password is error");
					respJson = gson.toJson(ms);
				}
			} else {
				ms.setError(2);
				ms.setMessage("user is not active");//用户未激活
				respJson = gson.toJson(ms);
			}
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(respJson);
		}			
		//用户注销
		public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
			HttpSession session = request.getSession();
			//从session中将user删除
			session.removeAttribute("user");
			
			//将存储在客户端的cookie删除掉
			Cookie cookie_username = new Cookie("cookie_username","");
			cookie_username.setMaxAge(0);
			//创建存储密码的cookie
			Cookie cookie_password = new Cookie("cookie_password","");
			cookie_password.setMaxAge(0);

			response.addCookie(cookie_username);
			response.addCookie(cookie_password);
			
			
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			
		}	
     
}
