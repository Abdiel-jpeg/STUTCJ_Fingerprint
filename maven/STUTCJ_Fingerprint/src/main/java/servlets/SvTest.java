package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jsonTemplates.JsonTemplate;
import logica.DatabaseSubject;
import logica.HTTPHandling;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;

/**
 * Servlet implementation class SvTest
 */
@WebServlet("/SvTest")
public class SvTest extends HttpServlet {
	private DatabaseSubject dbSubject; 
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvTest() {
        super();
        // TODO Auto-generated constructor stub
        
        try {
 			this.dbSubject = new DatabaseSubject();
 		} catch (ClassNotFoundException | SQLException e) {
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
				
				HTTPHandling.handleResponse(response, "ok", "Datos probablemente añadidos a la BBDD");
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				HTTPHandling.handleResponse(response, "error", "Error al ejecutar Test: " + e.getMessage());
			}
			
			break;
			
		case "emptySubjectTable":
			var conn = dbSubject.getConn();
			final String sql = "DELETE FROM subject WHERE 1=1";
		
			try {
				var stmt = conn.createStatement();
				stmt.execute(sql);
				
				HTTPHandling.handleResponse(response, "ok", "La BBDD fue limpiada exitosamente");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				HTTPHandling.handleResponse(response, "error", "Hubo un problema limpiando la BBDD");
			}
		
			
		default:
			HTTPHandling.handleResponse(response, "error", "Opcion no disponible en la API");
		}
	}

}
