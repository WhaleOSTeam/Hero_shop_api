package com.itheima.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.itheima.dao.AdminDao;
import com.itheima.domain.Category;
import com.itheima.domain.Order;
import com.itheima.domain.Product;

public interface AdminService {

	public List<Category> findAllCategory() throws SQLException;
    public Boolean addCategory(Category c) throws SQLException;
	public void saveProduct(Product product) throws SQLException;

	public List<Order> findAllOrders();

	public List<Map<String, Object>> findOrderInfoByOid(String oid);
	public Boolean categoryState(String cid, int openStat);
	public Boolean updataCategory(String cid, String cname);
	public List<Category> findCategoryByState(int openStat);

}
