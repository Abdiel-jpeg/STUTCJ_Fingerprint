package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jsonTemplates.confJsonTemplate;
import logica.DatabaseAsistencia;
import logica.HTTPHandling;
import logica.Conf;

import java.io.IOException;

import com.google.gson.Gson;

/**
 * Servlet implementation class SvConf
 */
@WebServlet("/SvConf")
public class SvConf extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvConf() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		var jsonStr = new Gson().toJson(new Conf(DatabaseAsistencia.threshold));
		
		HTTPHandling.handleResponse(response, "ok", jsonStr);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		var json = new Gson().fromJson(HTTPHandling.getBody(request), confJsonTemplate.class);
		
		//DatabaseAsistencia.threshold = Double.parseDouble(request.getParameter("threshold"));
		DatabaseAsistencia.threshold = json.getThreshold();
		
		HTTPHandling.handleResponse(response, "ok", "\"Configuracion actualizada correctamente\"");
	}

}
