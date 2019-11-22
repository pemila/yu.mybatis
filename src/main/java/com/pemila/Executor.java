package com.pemila;

/**
 * @author zhangchao
 * @date 2019/11/19 18:17
 **/
public interface Executor {
	public <T> T query(String statement,Object param);
}
