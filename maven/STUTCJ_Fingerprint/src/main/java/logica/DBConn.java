package logica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
	private static final String url = "jdbc:mysql://127.0.0.1:9006/estadiasdb";
	private static final String username = "estadias";
	private static final String password = "1234";
	private static Connection conn;
	
	public DBConn() throws SQLException {}
	
	public static Connection getConn() throws SQLException, ClassNotFoundException {
		if (conn == null || conn.isClosed()) {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		}
		
		return conn;
	}
 }
