package com.pemila;

import java.lang.reflect.Proxy;

/**
 * @author pemila
 * @date 2019/11/19 18:15
 **/
public class MySqlSession {

	private Executor executor = new MyExecutor();

	private MyConfiguration myConfiguration = new MyConfiguration();

	public <T> T selectOne(String statement,Object param){
		return executor.query(statement,param);
	}

	public <T> T getMapper(Class<T> clazz){
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new MyMapperProxy(myConfiguration, this));
	}
}
