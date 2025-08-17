package logica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

public class DatabaseAsistencia {
	private Connection conn;
	private Crypto crypto;
	public static double threshold = 15;
	public static int limit = 50;

	public DatabaseAsistencia() throws SQLException, ClassNotFoundException {
		super();
		
		crypto = new Crypto();

		conn = DBConn.getConn();
		
		final String startSQL = "CREATE TABLE IF NOT EXISTS asistencia ("
				+ "	idEvento INTEGER,"
				+ "	nreloj INTEGER,"
				+ "	FOREIGN KEY (idEvento) REFERENCES evento(id),"
				+ "	FOREIGN KEY (nreloj) REFERENCES subject(nreloj)"
				+ ");";
		
		var stmt = conn.createStatement();
    stmt.execute(startSQL);
	}
	
  public List<SubjectDTO> getSubjects(int idEvento, int offset) throws SQLException {
    List<SubjectDTO> subjects = new ArrayList<SubjectDTO>();

    final String sql = "SELECT" + 
		" s.nreloj," +
		" s.nombre," +
		" s.apellidoPaterno," +
		" s.apellidoMaterno" + 
		" FROM asistencia a" +
		" LEFT JOIN subject s" +
		" ON a.nreloj = s.nreloj" +
		" WHERE a.idEvento = ? " + 
		" LIMIT ? OFFSET ?";

    var pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, idEvento);
    pstmt.setInt(2, limit);
    pstmt.setInt(3, offset);
    var rs = pstmt.executeQuery();

    while (rs.next()) {
      int nreloj = rs.getInt("nreloj");
      String nombre = rs.getString("nombre");
      String apellidoPaterno = rs.getString("apellidoPaterno");
      String apellidoMaterno = rs.getString("apellidoMaterno");

      subjects.add(new SubjectDTO(nreloj, nombre, apellidoPaterno, apellidoMaterno, true));
    }

    return subjects;
  }

	public SubjectDTO tomarAsistencia(byte[] probeEncoded, int idEvento) throws SQLException {
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

		var assistanceTaken = (max >= threshold) ? match : null;

		if (assistanceTaken == null) {
			return null;
		}
		registrarAsistencia(idEvento, assistanceTaken.nreloj);

		return assistanceTaken;
    }

	private void registrarAsistencia(int idEvento, int nreloj) throws SQLException {
		final String sql = "INSERT INTO asistencia (idEvento, nreloj) VALUES (?, ?)";

		System.out.println(idEvento);
		System.out.println(nreloj);
		
		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, idEvento);
		pstmt.setInt(2, nreloj);
		pstmt.executeUpdate();
	}

	public void delAsistencia(int idEvento, int nreloj) throws SQLException {
		final String sql = "DELETE FROM asistencia WHERE idEvento = ? AND nreloj = ?";

		var pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, idEvento);
		pstmt.setInt(2, nreloj);
		pstmt.executeUpdate();
	}
}
