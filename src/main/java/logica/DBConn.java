package logica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
	//private static final String url = "jdbc:mysql://127.0.0.1:9006/estadiasdb";
	private static final String database = System.getenv("MYSQL_DATABASE");
	private static final String url = "jdbc:mysql://db:3306/" + database;
	private static final String username = System.getenv("MYSQL_USER");
	private static final String password =System.getenv("MYSQL_PASSWORD");
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
