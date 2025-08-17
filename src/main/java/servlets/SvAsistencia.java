package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jsonTemplates.AsistenciaJsonTemplate;
import logica.DatabaseAsistencia;
import logica.HTTPHandling;

import java.io.IOException;
import java.io.PrintWriter;
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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
    String idEventoStr = request.getParameter("idEvento");
    String offsetStr = request.getParameter("offset");

    int idEvento = (idEventoStr != null) ? Integer.parseInt(idEventoStr) : null;
    int offset = (offsetStr != null) ? Integer.parseInt(offsetStr) : null;
    
    try{
      var sujetos = dbAsistencia.getSubjects(idEvento, offset);
      String json = new Gson().toJson(sujetos);

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter out = response.getWriter();
      out.print(json);
      out.flush();

    } catch(SQLException e) {
      HTTPHandling.handleResponse(response, "error", "\"Error recibiendo datos: " + e.getMessage() + "\"");
    }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		var json = new Gson().fromJson(HTTPHandling.getBody(request), AsistenciaJsonTemplate.class);

		switch(json.getOption()) {
			case "fingerprintImage":
				//byte[] image = Base64.getDecoder().decode(json.getData());
				byte[] image = Base64.getDecoder().decode(new String(json.getEncodedImage()).getBytes("UTF-8"));

				try {
					var sujeto = dbAsistencia.tomarAsistencia(image, json.getIdEvento());

					if (!sujeto.activated) {
						HTTPHandling.handleResponse(response, "error", "\"El usuario: " + 
						sujeto.nombre + 
						" con número de reloj: " +
						sujeto.nreloj +
						"está desactivado. No puede tomar asistencia\"");

						break;
					}

					var jsonStr = new Gson().toJson(sujeto);
					
					HTTPHandling.handleResponse(response, "ok", jsonStr);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					HTTPHandling.handleResponse(response, "error", "\"Error en el backend\"");
				}
			
				break;
			case "deleteAssistance":
				try {
					dbAsistencia.delAsistencia(json.getIdEvento(), json.getNreloj());

					HTTPHandling.handleResponse(response, "ok", "\"Asistencia eliminada correctamente\"");
				} catch(SQLException e) {
					HTTPHandling.handleResponse(response, "error", "\"Error eliminadno la asistencia\"");
				}

				break;
			default:
				HTTPHandling.handleResponse(response, "error", "\"No existe tal opción. Disponibles: fingerprintImage\"");
		}
	}

}
