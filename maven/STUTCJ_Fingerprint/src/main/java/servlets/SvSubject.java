package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.DatabaseSubject;
import logica.JsonTemplate;
import logica.HTTPHandling;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.google.gson.Gson;

/**
 * Servlet implementation class SvSubject
 */
@WebServlet("/SvSubject")
public class SvSubject extends HttpServlet {
	private DatabaseSubject dbSubject;
	private static final long serialVersionUID = 1L;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public SvSubject() throws SQLException, ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
        this.dbSubject = new DatabaseSubject();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		try {
			String nrelojStr = request.getParameter("nreloj");
			String limitStr = request.getParameter("limit");
			String offsetStr = request.getParameter("offset");
			
			//Handle abscence of limit and offset
			int nreloj = (nrelojStr != null) ? Integer.parseInt(nrelojStr) : 0;
			int limit = (limitStr != null) ? Integer.parseInt(limitStr) : 0;
			int offset = (offsetStr != null) ? Integer.parseInt(offsetStr) : 1;
			
			String json;
			
			//If the client sends nreloj 0 means that wants to retrieave all subjects from database
			if (nreloj == 0) {
				var sujetos = dbSubject.getSubjectsWithoutImage(limit, offset);
				json = new Gson().toJson(sujetos);
				
			} else {
				var sujeto = dbSubject.getSubject(nreloj);
				json = new Gson().toJson(sujeto);
			}
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			HTTPHandling.handleResponse(response, "error", "Error en el servidor: " + e.getMessage());
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		var json = new Gson().fromJson(HTTPHandling.getBody(request), JsonTemplate.class);
		System.out.print(json);
		
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
		default:
			HTTPHandling.handleResponse(response, "error", "Opcion no disponible en la API");
		}
	}
}
