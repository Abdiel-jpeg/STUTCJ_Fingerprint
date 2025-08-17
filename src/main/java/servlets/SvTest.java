package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jsonTemplates.JsonTemplate;
import logica.DBConn;
import logica.DatabaseSubject;
import logica.HTTPHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;

import com.google.gson.Gson;

/**
 * Servlet implementation class SvTest
 */
@WebServlet("/SvTest")
public class SvTest extends HttpServlet {
	private DatabaseSubject dbSubject; 
	private static final long serialVersionUID = 1L;
       
    /**
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public SvTest() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
        
        try {
 			this.dbSubject = new DatabaseSubject();
 		} catch (SQLException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		var json = new Gson().fromJson(HTTPHandling.getBody(request), JsonTemplate.class);
		
		switch (json.getOption()) {
		case "processFiles":
			try {
				dbSubject.addSubjectsToDBTest(json.getData());
				
				HTTPHandling.handleResponse(response, "ok", "\"Datos probablemente añadidos a la BBDD\"");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				HTTPHandling.handleResponse(response, "error", "\"Error al ejecutar Test: " + e.getMessage() + "\"");
			}
			
			break;
			
		case "emptySubjectTable":
			try {
				var conn = DBConn.getConn();
				final String sql = "DELETE FROM subject WHERE 1=1";
				var stmt = conn.createStatement();
				stmt.execute(sql);
				
				HTTPHandling.handleResponse(response, "ok", "\"La BBDD fue limpiada exitosamente\"");
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				HTTPHandling.handleResponse(response, "error", "\"Hubo un problema limpiando la BBDD\"");
			}
			
			break;
		case "addSubjectsFromCSV":
			//System.out.println(json.getData());
			try {
				var connCSV = DBConn.getConn();
				final String sqlCSV = "INSERT INTO subject (nreloj, nombre, apellidoPaterno, apellidoMaterno) VALUES (?, ?, ?, ?)";
				
				BufferedReader reader = new BufferedReader(new StringReader(json.getData()));
				
				String line = reader.readLine();
				
				while ((line = reader.readLine()) != null) {
					String[] values = line.split(",");
					
					//System.out.println(values[0] + values[1] + values[2] + values[3]);
					int nreloj = Integer.parseInt(values[0]);
					String apellidoPaterno = values[1];
					String apellidoMaterno = values[2];
					String nombre = values[3];
					
					var pstmtCSV = connCSV.prepareStatement(sqlCSV);
					pstmtCSV.setInt(1, nreloj);
					pstmtCSV.setString(2, nombre);
					pstmtCSV.setString(3, apellidoPaterno);
					pstmtCSV.setString(4, apellidoMaterno);
					pstmtCSV.executeUpdate();
					
					HTTPHandling.handleResponse(response, "ok", "\"Datos añadidos correctamente al a BBDD\"");
				}
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				HTTPHandling.handleResponse(response, "error", "\"Error en la BBDD\"");
			}
		
			break;
		default:
			HTTPHandling.handleResponse(response, "error", "\"Opcion no disponible en la API\"");
		}
	}

}
