package logica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseEvento {
	private final String url = "jdbc:sqlite:database.db";
	private Connection conn;

	public DatabaseEvento() throws SQLException, ClassNotFoundException {
		super();
		
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection(url);
		
		final String startSQL = "CREATE TABLE IF NOT EXISTS evento ("
				+ "	id INTEGER PRIMARY KEY NOT NULL,"
				+ "	titulo VARCHAR(255) NOT NULL,"
				+ "	descripcion VARCHAR(255)"
				+ ");";
		
		var stmt = conn.createStatement();
		stmt.execute(startSQL);
	}
	
	//Get only one event
	public Evento getEvento(int id) throws SQLException {
		final String sql = "SELECT * FROM evento WHERE id=?";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);
		var rs = pstmt.executeQuery();
		
		String nombre = rs.getString("titulo");
		String descripcion = rs.getString("descripcion");
		
		return new Evento(id, nombre, descripcion);
	}
	
	//Return multiple events
	public List<Evento> getEventos(int limit, int offset) throws SQLException {
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
	
	//Create new event
	public void addEvento(String titulo, String descripcion) throws SQLException {
		final String sql = "INSERT INTO evento (titulo, descripcion) VALUES (?, ?)";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,  titulo);
		pstmt.setString(2, descripcion);
		pstmt.executeUpdate();
	}
	
	//Update events
	public void updateEvento(Evento event, int id) throws SQLException {
		final String sql = "UPDATE evento SET id=?,"
				+ "	titulo=?,"
				+ "	descripcion=?"
				+ "	WHERE id=?";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, event.getId());
		pstmt.setString(1, event.getTitulo());
		pstmt.setString(1, event.getDescripcion());
		pstmt.executeUpdate();
	}
	
	//Delete event
	public void deleteEvento(int id) throws SQLException {
		final String sql = "DELETE FROM evento WHERE id=?";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
	}
}
