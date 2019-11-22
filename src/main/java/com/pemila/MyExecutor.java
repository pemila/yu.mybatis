package com.pemila;

import com.pemila.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author pemila
 * @date 2019/11/19 18:16
 **/
public class MyExecutor implements Executor{

	private MyConfiguration xmlConfiguration = new MyConfiguration();

	@Override
	public <T> T query(String sql, Object param) {
		ResultSet resultSet = null;


		try(Connection connection = getConnection();
				PreparedStatement pre =connection.prepareStatement(sql)
			){
			//设置参数
			pre.setString(1,param.toString());
			resultSet = pre.executeQuery();

			User u = new User();
			while (resultSet.next()){
				u.setId(resultSet.getString(1));
				u.setUserName(resultSet.getString(2));
				u.setPassword(resultSet.getString(3));
			}
			return (T) u;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
				if(resultSet!=null){
					try {
						resultSet.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
		}
		return null;
	}

	private Connection getConnection() {
		return xmlConfiguration.build("database.xml");
	}
}
