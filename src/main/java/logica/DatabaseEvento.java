package logica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseEvento {
	private static Connection conn;
	public static int limit = 10;

	public static void initializeDatabase() throws SQLException, ClassNotFoundException {
		conn = DBConn.getConn();
		
		final String startSQL = "CREATE TABLE IF NOT EXISTS evento ("
				+ "	id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,"
				+ "	titulo VARCHAR(255) NOT NULL,"
				+ "	descripcion VARCHAR(255)"
				+ ");";
		
		var stmt = conn.createStatement();
		stmt.execute(startSQL);
	}
	
	//Get only one event
	public static Evento getEvento(int id) throws SQLException, ClassNotFoundException {
		initializeDatabase();
		final String sql = "SELECT * FROM evento WHERE id=?";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);
		var rs = pstmt.executeQuery();
		
		rs.next();
		String nombre = rs.getString("titulo");
		String descripcion = rs.getString("descripcion");
		
		return new Evento(id, nombre, descripcion);
	}
	
	//Return multiple events
	public static List<Evento> getEventos(int offset) throws SQLException, ClassNotFoundException {
		initializeDatabase();
		List<Evento> eventos = new ArrayList<Evento>();
		
		final String sql = "SELECT * FROM evento LIMIT ? OFFSET ?";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, limit);
		pstmt.setInt(2, offset);
		var rs = pstmt.executeQuery();
		
		while (rs.next()) {
			int id = rs.getInt("id");
			String nombre = rs.getString("titulo");
			String descripcion = rs.getString("descripcion");
			
			eventos.add(new Evento(id, nombre, descripcion));
		}
		
		return eventos;
	}

	public static int getCount() throws SQLException {
		final String sql = "SELECT COUNT(*) AS count FROM evento";
	
		var stmt = conn.createStatement();
		var rs = stmt.executeQuery(sql);
		rs.next();
	
		return rs.getInt("count");
	  }
	
	//Create new event
	public static void addEvento(String titulo, String descripcion) throws SQLException, ClassNotFoundException {
		initializeDatabase();
		final String sql = "INSERT INTO evento (titulo, descripcion) VALUES (?, ?)";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,  titulo);
		pstmt.setString(2, descripcion);
		pstmt.executeUpdate();
	}
	
	//Update events
	public static void updateEvento(Evento event, int id) throws SQLException, ClassNotFoundException {
		initializeDatabase();
		final String sql = "UPDATE evento SET titulo=?, descripcion=? WHERE id=?";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, event.getTitulo());
		pstmt.setString(2, event.getDescripcion());
		pstmt.setInt(3, event.getId());
		pstmt.executeUpdate();
	}
	
	//Delete event
	public static void deleteEvento(int id) throws SQLException, ClassNotFoundException {
		initializeDatabase();
		final String sqlEvento = "DELETE FROM evento WHERE id=?";
		final String sqlAsistencia = "DELETE FROM asistencia WHERE idEvento = ?";
		
		//First delete all dependencies in foreign keys
		var pstmtAsistencia = conn.prepareStatement(sqlAsistencia);
		pstmtAsistencia.setInt(1, id);
		pstmtAsistencia.executeUpdate();

		//Last delete evento itslef
		var pstmtEvento = conn.prepareStatement(sqlEvento);
		pstmtEvento.setInt(1, id);
		pstmtEvento.executeUpdate();
	}
}
