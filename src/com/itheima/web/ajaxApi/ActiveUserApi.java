package com.itheima.web.ajaxApi;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.service.UserService;

public class ActiveUserApi extends HttpServlet{
    /*
     * http://localhost:7070/Hero_shop_api/activeApi?activeCode=3c659361-ba69-4ac1-b165-52c18aa0fa4c
     * */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		//获得激活码
		String activeCode = req.getParameter("activeCode");
		
		UserService service = new UserService();
		service.active(activeCode);
		
		resp.getWriter().write("恭喜你账户激活成功请登录");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

	
	

}
