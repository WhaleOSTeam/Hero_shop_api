package com.itheima.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
1、如果在HttpServlet 中覆盖了service(ServletRequest,SerlvetResonse)方法
   则这个类的所实现的doGet/doPost都不会再执行了。
   因为service(ServletRequest,SerlvetResonse)是最高接口Servlet定义规范。
   在tomcat调用时，一定会在最终的子类中去找这个方法且调用它。
   如果最终的子类没有则会调用父的service(ServletRequest,SerlvetResonse)。

2、如果覆盖了serivce(HttpServletRequest,HtpServletResponse)
  则会执行httpServlet中的service(ServletRequest,SerlvetResonse),
  但是由于子类中已经覆盖了serivce(HttpServletRequest,HtpServletResponset)所以，
  httpServlet中的serivce(HttpServletRequest,HtpServletResponset)就不再执行了，
  而是直接执行子类中同名同参数方法，且doXxxx也不会执行了，
  因为子类的serivce(HttpServletRequest,HtpServletResponset)没有调用doXxxx.

3、如果继承了HttpServlet没有实现任何的doXxx方法则会抛出一个异常
*/
@SuppressWarnings("all")
public class BaseServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		try {
			//1、获得请求的method的名称
			String methodName = req.getParameter("method");
			methodName = methodName == null ? "execute" : methodName; //默认执行的方法
			//2、获得当前被访问的对象的字节码对象
			Class clazz = this.getClass();//ProductServlet.class ---- UserServlet.class
			//3、获得当前字节码对象的中的指定方法
			Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
			//4、执行相应功能方法
			method.invoke(this,req,resp);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}