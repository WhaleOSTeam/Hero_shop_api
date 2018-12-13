package com.itheima.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtils {
	
	private static JedisPool pool = null;
	
	static{
		
		//加载配置文件
		InputStream in = JedisPoolUtils.class.getClassLoader().getResourceAsStream("redis.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//获得池子配置对象
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(Integer.parseInt(pro.get("redis.maxIdle").toString()));//最大闲置个数
		poolConfig.setMinIdle(Integer.parseInt(pro.get("redis.minIdle").toString()));//最小闲置个数
		poolConfig.setMaxTotal(Integer.parseInt(pro.get("redis.maxTotal").toString()));//最大连接数
				
		//redis 的基础项
		String ADDR = pro.getProperty("redis.url").toString();
		int PORT = Integer.parseInt(pro.get("redis.port").toString());
		int TIMEOUT = Integer.parseInt(pro.get("redis.timeOut").toString());
		String AUTH = pro.get("redis.AUTH").toString();
		pool = new JedisPool(poolConfig,ADDR,PORT,TIMEOUT,AUTH);
		/*
		 * config 配置项
		 * ADDR   redis服务器地址
		 * PORT   redis端口号
		 * TIMEOUT 延时数
		 * AUTH   redis密码
		 * jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
		 * 
		 * */
	}

	//获得jedis资源的方法
	public static Jedis getJedis(){
		return pool.getResource();
	}
	
	public static void main(String[] args) {
		Jedis jedis = getJedis();
		System.out.println(jedis.get("xxx"));
	}
	
	
	
	
}
