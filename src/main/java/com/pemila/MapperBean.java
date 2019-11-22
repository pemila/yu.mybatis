package com.pemila;

import lombok.Data;

import java.util.List;

/**
 * @author pemila
 * @date 2019/11/18 17:59
 **/
@Data
public class MapperBean {
	/** 接口名*/
	private String interfaceName;
	/** 接口中的所有方法*/
	private List<Function> list;
}

@Data
class Function {
	private String sqlType;
	private String funcName;
	private String sql;
	private Object resultType;
	private String parameterType;
}

