package logica;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSubject {
	//private static final byte[] secretKey = Base64.getDecoder().decode(System.getenv("PRIVATE_KEY"));
	public static int limit = 50;
	private Connection conn;

	public DatabaseSubject() throws SQLException, ClassNotFoundException {
		super();
		
		conn = DBConn.getConn();
		
		final String startSQL = "CREATE TABLE IF NOT EXISTS subject ("
				+ "	nreloj INTEGER PRIMARY KEY NOT NULL,"
				+ "	nombre VARCHAR(255) NOT NULL,"
				+ "	apellidoPaterno VARCHAR(255) NOT NULL,"
				+ "	apellidoMaterno VARCHAR(255) NOT NULL,"
				+ "	image MEDIUMBLOB NULL,"
				+ "	template BLOB NULL,"
				+ "	activated INTEGER DEFAULT TRUE"
				+ ");";
		
		var stmt = conn.createStatement();
		stmt.execute(startSQL);
	}
	
	//In case of getting just one subject
	public SubjectDTOImage getSubject(int nreloj) throws Exception {
		final String sql = "SELECT nombre, apellidoPaterno, apellidoMaterno, image, activated FROM subject WHERE nreloj=?";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, nreloj);
		var rs = pstmt.executeQuery();
		rs.next();
		
		String nombre = rs.getString("nombre");
		String apellidoPaterno = rs.getString("apellidoPaterno");
		String apellidoMaterno = rs.getString("apellidoMaterno");
		byte[] image = rs.getBytes("image");
		int activated = rs.getInt("activated");
		
		if (image == null) {
			return new SubjectDTOImage(nreloj, nombre, apellidoPaterno, apellidoMaterno, activated >= 1 ? true : false);
		}
		return new SubjectDTOImage(nreloj, nombre, apellidoPaterno, apellidoMaterno, image, activated >= 1 ? true : false);
	}
	
	//In case of getting multiple subjects 
	public List<SubjectDTO> getSubjectsWithoutImage(int offset, String searchParam) throws SQLException {
		List<SubjectDTO> agremiado = new ArrayList<SubjectDTO>();
		
		PreparedStatement pstmt;
		
		if (searchParam == null) {
			final String sql = "SELECT * FROM subject LIMIT ? OFFSET ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, limit);
			pstmt.setInt(2, offset);

		} else {
			final String sqlSearch = "SELECT * FROM subject WHERE nreloj LIKE ?" +
				" OR nombre LIKE ?" +
				" OR apellidoPaterno LIKE ?" +
				" OR apellidoMaterno LIke ?" +
				" LIMIT ? OFFSET ?" ;

			searchParam = "%" + searchParam + "%";

			pstmt = conn.prepareStatement(sqlSearch);
			pstmt.setString(1, searchParam);
			pstmt.setString(2, searchParam);
			pstmt.setString(3, searchParam);
			pstmt.setString(4, searchParam);
			pstmt.setInt(5, limit);
			pstmt.setInt(6, offset);
		}

		var rs = pstmt.executeQuery();
		
		while (rs.next()) {
			int nreloj = rs.getInt("nreloj");
			String nombre = rs.getString("nombre");
			String apellidoPaterno = rs.getString("apellidoPaterno");
			String apellidoMaterno = rs.getString("apellidoMaterno");
			int activated = rs.getInt("activated");
			
			agremiado.add(new SubjectDTO(nreloj, nombre, apellidoPaterno, apellidoMaterno, activated  >= 1 ? true : false));
		}
		
		return agremiado;
	}
	
	public int getCount() throws SQLException {
		final String sql = "SELECT COUNT(*) AS count FROM subject";

		var stmt = conn.createStatement();
		var rs = stmt.executeQuery(sql);
		rs.next();

		return rs.getInt("count");
	}

	public void addSubjectsToDBTest(String absolutePath) throws Exception {
		List<Subject> agremiado = new ArrayList<Subject>();
		
		final String sql = "INSERT INTO subject (nombre, apellidoPaterno, apellidoMaterno, image, template, activated)"
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		
		File folder = new File(absolutePath);
		File[] listOfFiles = folder.listFiles();
		
		// Explore all files in the absolute dictory and decodes all information into instances
		for (int i = 0; i < listOfFiles.length; i++) {
			byte[] imageEncoded = Files.readAllBytes(Paths.get(absolutePath + listOfFiles[i].getName()));
			String nombre = listOfFiles[i].getName();
			
			agremiado.add(new Subject(nombre, imageEncoded));
		}
		
		//Next we add that instances to the database
		for ( var sujeto : agremiado) {
			var pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sujeto.getNombre());
			pstmt.setString(2, sujeto.getApellidoPaterno());
			pstmt.setString(3, sujeto.getApellidoMaterno());
			pstmt.setBytes(4, sujeto.getImage());
			pstmt.setBytes(5, sujeto.getTemplate().toByteArray());
			pstmt.setInt(6, sujeto.isActivated() ? 1 : 0);
			pstmt.executeUpdate();
		}
	}
	
	public void addSubjectToDB(Subject sujeto) throws Exception {
		final String sql = "INSERT INTO subject VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, sujeto.getNreloj());
		pstmt.setString(2, sujeto.getNombre());
		pstmt.setString(3, sujeto.getApellidoPaterno());
		pstmt.setString(4, sujeto.getApellidoMaterno());
		pstmt.setBytes(5, sujeto.getImage());
		pstmt.setBytes(6, sujeto.getTemplate().toByteArray());
		pstmt.setInt(7, sujeto.isActivated() ? 1 : 0);
		pstmt.executeUpdate();
	}
	
	public void addSubjectToDBWithoutImage(SubjectDTO sujeto) throws SQLException {
		final String sql = "INSERT INTO subject (nreloj, nombre, apellidoPaterno, apellidoMaterno, activated) VALUES (?, ?, ?, ?, ?)";
		
		var pstmt = conn.prepareStatement(sql);
		try {
			pstmt.setInt(1, sujeto.nreloj);
			pstmt.setString(2, sujeto.nombre);
			pstmt.setString(3, sujeto.apellidoPaterno);
			pstmt.setString(4, sujeto.apellidoMaterno);
			pstmt.setInt(5, sujeto.activated ? 1 : 0);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateSubject(Subject sujeto, int nreloj) throws Exception {
		final String sql = "UPDATE subject SET nreloj=?,"
				+ "	nombre=?,"
				+ "	apellidoPaterno=?,"
				+ "	apellidoMaterno=?,"
				+ "	image=?,"
				+ "	template=?,"
				+ "	activated=?"
				+ "	WHERE nreloj=?";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, sujeto.getNreloj());
		pstmt.setString(2, sujeto.getNombre());
		pstmt.setString(3, sujeto.getApellidoPaterno());
		pstmt.setString(4, sujeto.getApellidoMaterno());
		pstmt.setBytes(5, sujeto.getImage());
		pstmt.setBytes(6, sujeto.getTemplate().toByteArray());
		pstmt.setInt(7, sujeto.isActivated() ? 1 : 0);
		pstmt.setInt(8, nreloj);
		pstmt.executeUpdate();
	}
	
	public void updateSubjectWithoutImage(SubjectDTO sujeto, int nreloj) throws SQLException {
		final String sql = "UPDATE subject SET nreloj=?,"
				+ "	nombre=?,"
				+ "	apellidoPaterno=?,"
				+ "	apellidoMaterno=?,"
				+ "	activated=?"
				+ " WHERE nreloj=?";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, sujeto.nreloj);
		pstmt.setString(2, sujeto.nombre);
		pstmt.setString(3, sujeto.apellidoPaterno);
		pstmt.setString(4, sujeto.apellidoMaterno);
		pstmt.setInt(5, sujeto.activated ? 1 : 0);
		pstmt.setInt(6, nreloj);
		pstmt.executeUpdate();
	}
	
	public void deleteSubject(int nreloj) throws SQLException {
		final String sql = "DELETE FROM subject WHERE nreloj=?";
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, nreloj);
		pstmt.executeUpdate();
	}
}
