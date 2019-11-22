package com.pemila;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 读取并解析配置信息，返回处理后的Environment
 * @author pemila
 * @date 2019/11/18 17:35
 **/
public class MyConfiguration {

	private static  ClassLoader loader = ClassLoader.getSystemClassLoader();

	/** 读取database配置并返回数据库连接*/
	public Connection build(String resource){
		try {
			InputStream stream = loader.getResourceAsStream(resource);
			SAXReader reader = new SAXReader();
			Document document = reader.read(stream);
			Element root = document.getRootElement();
			return evalDataSource(root);
		} catch (Exception e) {
			throw new RuntimeException("eval xml error "+resource);
		}
	}

	private Connection evalDataSource(Element node) throws ClassNotFoundException {
		if(!"database".equals(node.getName())){
			throw new RuntimeException("root should be <database>");
		}

		String driverClassName = null,url = null,username = null,password = null;
		for(Object item : node.elements("property")){
			Element i = (Element) item;
			String value = getValue(i);
			String name = i.attributeValue("name");
			if (name == null || value == null) {
				throw new RuntimeException("[database]: <property> should contain name and value");
			}

			switch (name){
				case "url": url = value;break;
				case "username": username = value;break;
				case "password": password = value;break;
				case "driverClassName": driverClassName = value;break;
				default: throw  new RuntimeException("[database]: <property> unkown name");
			}
		}
		Class.forName(driverClassName);
		Connection connection = null;
		try {
			assert url != null;
			connection = DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/** 获取property属性的值*/
	private String getValue(Element node) {
		return node.hasContent()? node.getText():node.attributeValue("value");
	}



	public MapperBean readMapper(String path){
		MapperBean mapper = new MapperBean();
		try{
			InputStream stream = loader.getResourceAsStream(path);
			SAXReader reader = new SAXReader();
			Document document  = reader.read(stream);
			Element root = document.getRootElement();

			mapper.setInterfaceName(root.attributeValue("namespace").trim());
			List<Function> list = new ArrayList<>();
			for(Iterator rootIter = root.elementIterator();rootIter.hasNext();){
				Function fun = new Function();
				Element e = (Element) rootIter.next();
				fun.setSqlType(e.getName().trim());
				fun.setFuncName(e.attributeValue("id").trim());
				fun.setSql(e.getTextTrim());
				String resultType= e.attributeValue("resultType").trim();
				Object newInstance = null;
				try {
					newInstance = Class.forName(resultType).newInstance();
				}catch (Exception ex){
					ex.printStackTrace();
				}
				fun.setResultType(newInstance);
				list.add(fun);
			}
			mapper.setList(list);
		}catch (Exception e){
			e.printStackTrace();
		}
		return mapper;
	}

}
