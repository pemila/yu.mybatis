package com.pemila;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author pemila
 * @date 2019/11/19 18:21
 **/
public class MyMapperProxy implements InvocationHandler {

	private MySqlSession mySqlSession;
	private MyConfiguration myConfiguration;


	public MyMapperProxy(MyConfiguration myConfiguration, MySqlSession mySqlSession) {
		this.myConfiguration = myConfiguration;
		this.mySqlSession = mySqlSession;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		MapperBean readMapper = myConfiguration.readMapper("UserMapper.xml");
		if(!method.getDeclaringClass().getName().equals(readMapper.getInterfaceName())){
			// xml中定义的接口与传入的method的接口不一致
			System.out.println("mapper xml bean does not match this method interface");
			return null;
		}

		List<Function> list = readMapper.getList();

		if(list!=null&&!list.isEmpty()){
			Function function = list.stream().filter(e-> e.getFuncName().equals(method.getName())).findAny().orElse(null);
			if(function!=null){
				return mySqlSession.selectOne(function.getSql(),String.valueOf(args[0]));
			}
		}
		return null;
	}
}
