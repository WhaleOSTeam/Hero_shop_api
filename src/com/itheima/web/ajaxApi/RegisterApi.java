package com.itheima.web.ajaxApi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.junit.Test;

import com.google.gson.Gson;
import com.itheima.domain.BackMassage;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.utils.CommonsUtils;
import com.itheima.utils.MailUtils;

public class RegisterApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterApi() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		Gson gson = new Gson();
		BackMassage ms = new BackMassage();
		String resMasssage = "";
		//获得表单数据
		Map<String, String[]> properties = request.getParameterMap();
		User user = new User();
		try {
			//将获取到的参数封装到实体中
			packageParam(user,properties);
			//private String uid;
			user.setUid(CommonsUtils.getUUID());
			//private String telephone;
			user.setTelephone(null);
			//private int state;//是否激活0-表示未激活
			user.setState(0);
			//private String code;//激活码
			String activeCode = CommonsUtils.getUUID();
			user.setCode(activeCode);//设置激活码用来使用邮件激活用户
			//将user传递给service层
			UserService service = new UserService();
			boolean isRegisterSuccess = service.regist(user);
			//是否注册成功
			if(isRegisterSuccess){
				//发送激活邮件
				sendEmail(user,activeCode);
			    //返回成功信息
				ms.setError(0);
				ms.setMessage("success");
				resMasssage = gson.toJson(ms);
				response.getWriter().write(resMasssage);
			}else{
				//跳转到失败的提示页面
				ms.setError(1);
				ms.setMessage("failed");
				resMasssage = gson.toJson(ms);
				response.getWriter().write(resMasssage);
			}
			
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
    @Test
    public void send(){
    	try {
			MailUtils.sendMail("438081265@qq.com","nihao");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	private void sendEmail(User user ,String activeCode) {
		String emailMsg = "恭喜您注册成功，请点击下面的连接进行激活账户"
				+ "<a href='http://localhost:7070/Hero_shop_api/activeApi?activeCode="+activeCode+"'>"
						+ "http://localhost:7070/Hero_shop_api/activeApi?activeCode="+activeCode+"</a>";
		try {
			System.out.println(user.getEmail());
			MailUtils.sendMail(user.getEmail(),emailMsg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	private void packageParam(User user, Map<String, String[]> properties) throws IllegalAccessException, InvocationTargetException {
		//自己指定一个类型转换器（将String转成Date）
		/*
		 * BeanUtils是依赖ConvertUtils来完成实际上的类型转换，
		 * 但是有时候我们可能需要自定义转换器来完成特殊需求的类型转换
		 * 自定义类型转换器步骤： 
           1、定义一个实现类实现Converter接口
           2、调用ConvertUtils.register方法，注册该转换器
		 * */
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(Class clazz, Object value) {
				//将string转成date
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date parse = null;
				try {
					parse = format.parse(value.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return parse;
			}
		}, Date.class);
		//映射封装 populate可以帮助我们把Map里的键值对值拷贝到bean的属性值中
		BeanUtils.populate(user, properties);
		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
