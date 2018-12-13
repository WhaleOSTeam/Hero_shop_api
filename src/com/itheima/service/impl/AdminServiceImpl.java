package com.itheima.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.itheima.dao.AdminDao;
import com.itheima.domain.Category;
import com.itheima.domain.Order;
import com.itheima.domain.Product;
import com.itheima.service.AdminService;

public class AdminServiceImpl implements AdminService{
	@Override
	public Boolean updataCategory(String cid, String cname) {
		AdminDao dao = new AdminDao();
		return dao.updataCategory(cid,cname);
	}
	@Override
	public Boolean categoryState(String cid, int openStat) {
		AdminDao dao = new AdminDao();
		return dao.categoryState(cid,openStat);
	}
	@Override
	public Boolean addCategory(Category c) throws SQLException{
		AdminDao dao = new AdminDao();
		
		try {
			return dao.addCategory(c);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<Category> findCategoryByState(int openStat) {
		AdminDao dao = new AdminDao();
		try {
			return dao.findCategoryByState(openStat);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Category> findAllCategory() throws SQLException {
		AdminDao dao = new AdminDao();
		try {
			return dao.findAllCategory();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void saveProduct(Product product) throws SQLException {
		AdminDao dao = new AdminDao();
		dao.saveProduct(product);
	}

	public List<Order> findAllOrders() {
		AdminDao dao = new AdminDao();
		List<Order> ordersList = null;
		try {
			ordersList = dao.findAllOrders();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ordersList;
	}

	public List<Map<String, Object>> findOrderInfoByOid(String oid) {
		AdminDao dao = new AdminDao();
		List<Map<String, Object>> mapList = null;
		try {
			mapList = dao.findOrderInfoByOid(oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapList;
	}
	
	
}
