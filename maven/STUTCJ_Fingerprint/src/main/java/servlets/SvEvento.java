package servlets;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.google.gson.Gson;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jsonTemplates.eventoJsonTemplate;
import logica.DatabaseEvento;
import logica.Evento;
import logica.HTTPHandling;

/**
 * Servlet implementation class SvEvento
 */
@WebServlet("/SvEvento")
public class SvEvento extends HttpServlet implements Servlet {
	private DatabaseEvento dbEvento;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvEvento() {
        super();
        // TODO Auto-generated constructor stub
        
        try {
			this.dbEvento = new DatabaseEvento();
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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		try {
			String idStr = request.getParameter("id");
			String limitStr = request.getParameter("limit");
			String offsetStr = request.getParameter("offset");
			
			//Handle abscence of limit and offset
			int id = (idStr != null) ? Integer.parseInt(idStr) : 0;
			int limit = (limitStr != null) ? Integer.parseInt(limitStr) : 0;
			int offset = (offsetStr != null) ? Integer.parseInt(offsetStr) : 1;
			
			String json;
			
			//If the client sends nreloj 0 means that wants to retrieave all subjects from database
			if (id == 0) {
				var sujetos = dbEvento.getEventos(limit, offset);
				json = new Gson().toJson(sujetos);
				
			} else {
				var sujeto = dbEvento.getEvento(id);
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
		
		var json = new Gson().fromJson(HTTPHandling.getBody(request), eventoJsonTemplate.class);
		System.out.print(json);
		
		try {
			switch(json.getOpcion()) {
			case "eventoDelete":
				dbEvento.deleteEvento(Integer.parseInt(json.getId()));
				
				break;
			case "eventoUpdate":
				dbEvento.updateEvento(new Evento(Integer.parseInt(json.getId()), json.getTitulo(), json.getDescripcion()), Integer.parseInt(json.getId()));
				
				break;
				
			case "eventoAdd":
				dbEvento.addEvento(json.getTitulo(), json.getDescripcion());
				
				break;
			default:
				HTTPHandling.handleResponse(response, "error", "No existe tal opcion. Opciones disponibles: eventoDelete, eventoUpdate, eventoAdd");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
