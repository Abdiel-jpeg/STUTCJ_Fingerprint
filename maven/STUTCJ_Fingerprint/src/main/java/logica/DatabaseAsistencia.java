package logica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

public class DatabaseAsistencia {
	private final String url = "jdbc:sqlite:database.db";
	private Connection conn;
	public static double threshold = 15;

	public DatabaseAsistencia() throws SQLException, ClassNotFoundException {
		super();
		
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection(url);
		
		final String startSQL = "CREATE TABLE IF NOT EXISTS asistencia ("
				+ "	idEvento INTEGER,"
				+ "	nreloj INTEGER,"
				+ "	FOREIGN KEY (idEvento) REFERENCES evento(id),"
				+ "	FOREIGN KEY (nreloj) REFERENCES subject(nreloj)"
				+ ");";
		
		var stmt = conn.createStatement();
		stmt.execute(startSQL);
	}
	
	public SubjectDTO tomarAsistencia(byte[] probeEncoded) throws SQLException {
		var probeTemplate = new FingerprintTemplate(new FingerprintImage(probeEncoded));
		
		final String sql = "SELECT nreloj, nombre, apellidoPaterno, apellidoMaterno, template, activated FROM subject";
		
		//Retreive all fingerprints templates in the database
		var stmt = conn.createStatement();
		var rs = stmt.executeQuery(sql);
		
		var matcher = new FingerprintMatcher(probeTemplate);
        SubjectDTO match = null;
        double max = Double.NEGATIVE_INFINITY;
		
		while (rs.next()) {
			if(rs.getString("template") == null) {
				continue;
			}
			
			double similarity = matcher.match(new FingerprintTemplate(rs.getBytes("template")));
			String nombre = rs.getString("nombre");
			System.out.println("Similarity with "  + rs.getString("nombre") + " is " + similarity);
            if (similarity > max) {
            	max = similarity;
            	int nreloj = rs.getInt("nreloj");
            	String apellidoPaterno = rs.getString("apellidoPaterno");
            	String apellidoMaterno = rs.getString("apellidoMaterno");
            	int activated = rs.getInt("activated");
            	
            	match = new SubjectDTO(nreloj, nombre, apellidoPaterno, apellidoMaterno, activated >= 1 ? true : false);
             }
		}
	    return max >= threshold ? match : null;
    }
}
