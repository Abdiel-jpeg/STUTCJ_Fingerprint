package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jsonTemplates.JsonTemplate;
import logica.DatabaseAsistencia;
import logica.HTTPHandling;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

import com.google.gson.Gson;

/**
 * Servlet implementation class SvAsistencia
 */
@WebServlet("/SvAsistencia")
public class SvAsistencia extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DatabaseAsistencia dbAsistencia;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public SvAsistencia() throws SQLException, ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
        this.dbAsistencia = new DatabaseAsistencia();
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
		
		//byte[] image = Base64.getDecoder().decode(json.getData());
		byte[] image = Base64.getDecoder().decode(new String(json.getData()).getBytes("UTF-8"));
		
		try {
			var sujeto = dbAsistencia.tomarAsistencia(image);
			var jsonStr = new Gson().toJson(sujeto);
			
			HTTPHandling.handleResponse(response, "ok", jsonStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			HTTPHandling.handleResponse(response, "error", "Error en el backend");
		}
	}

}
