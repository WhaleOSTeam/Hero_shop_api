package com.itheima.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.itheima.domain.Category;
import com.itheima.domain.Order;
import com.itheima.domain.Product;
import com.itheima.utils.DataSourceUtils;
import com.itheima.utils.JedisPoolUtils;

import redis.clients.jedis.Jedis;

public class AdminDao {
	
	public Boolean updataCategory(String cid, String cname,int openStat) {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "UPDATE category set cname = ? , isOpen = ? where cid = ? ";
		try {
			int res = runner.update(sql,cname,openStat,cid);
			if(res > 0) {
				Jedis jedis = JedisPoolUtils.getJedis();
				jedis.del("categoryListJson");
			}
			return  res > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
    public Boolean addCategory(Category category) throws SQLException{
    	QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
    	String sql = "insert into category values(?,?,?)";
    	int re = runner.update(sql,category.getCid(),category.getCname(),category.getIsOpen());
    	if(re > 0) {
    		Jedis jedis = JedisPoolUtils.getJedis();
			jedis.del("categoryListJson");
    	}
		return re > 0;
    }
    public List<Category> findCategoryByState(int openStat) throws SQLException {
    	QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category where isOpen = ?";
		return runner.query(sql, new BeanListHandler<Category>(Category.class),openStat);
	}
	public List<Category> findAllCategory() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category";
		return runner.query(sql, new BeanListHandler<Category>(Category.class));
	}
    // product
	public void saveProduct(Product product) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
		runner.update(sql, product.getPid(),product.getPname(),product.getMarket_price(),
				product.getShop_price(),product.getPimage(),product.getPdate(),
				product.getIs_hot(),product.getPdesc(),product.getPflag(),product.getCategory().getCid());
	}

	public List<Order> findAllOrders() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders";
		return runner.query(sql, new BeanListHandler<Order>(Order.class));
	}

	public List<Map<String, Object>> findOrderInfoByOid(String oid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select p.pimage,p.pname,p.shop_price,i.count,i.subtotal "+
					" from orderitem i,product p "+
					" where i.pid=p.pid and i.oid=? ";
		return runner.query(sql, new MapListHandler(), oid);
	}
	

}
