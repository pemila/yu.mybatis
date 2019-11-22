package com.pemila;

import com.pemila.dao.UserMapper;
import com.pemila.model.User;

/**
 * @author pemila
 * @date 2019/11/22 11:17
 **/
public class TestMybatis {

	public static void main(String[] args) {
		MySqlSession sqlSession = new MySqlSession();
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		User user = mapper.getUserById("1");
		System.out.println(user);
	}

}
