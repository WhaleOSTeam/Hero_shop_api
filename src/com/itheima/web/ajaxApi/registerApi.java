package com.itheima.web.ajaxApi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.google.gson.Gson;
import com.itheima.domain.BackMassage;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.utils.CommonsUtils;
import com.itheima.utils.MailUtils;

/**
 * Servlet implementation class registerApi
 */

public class registerApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public registerApi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BackMassage ms = new BackMassage();
		String resMasssage = "";
		//获得表单数据
		Map<String, String[]> properties = request.getParameterMap();
		User user = new User();
		try {
			//自己指定一个类型转换器（将String转成Date）
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
			//映射封装
			BeanUtils.populate(user, properties);
			//private String uid;
			user.setUid(CommonsUtils.getUUID());
			//private String telephone;
			user.setTelephone(null);
			//private int state;//是否激活
			user.setState(0);
			//private String code;//激活码
			String activeCode = CommonsUtils.getUUID();
			user.setCode(activeCode);
			//将user传递给service层
			UserService service = new UserService();
			boolean isRegisterSuccess = service.regist(user);
			//是否注册成功
			if(isRegisterSuccess){
				//发送激活邮件
				String emailMsg = "恭喜您注册成功，请点击下面的连接进行激活账户"
						+ "<a href='http://localhost:7070/HeimaShop/active?activeCode="+activeCode+"'>"
								+ "http://localhost:7070/HeimaShop/active?activeCode="+activeCode+"</a>";
				try {
					MailUtils.sendMail(user.getEmail(),emailMsg);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			    //返回成功信息
				response.setContentType("text/html;charset=UTF-8");
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
