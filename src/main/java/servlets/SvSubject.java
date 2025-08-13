package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jsonTemplates.subjectJsonTemplate;
import logica.DatabaseSubject;
import logica.HTTPHandling;
import logica.Subject;
import logica.SubjectDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Base64;

import com.google.gson.Gson;

/**
 * Servlet implementation class SvSubject
 */
@WebServlet("/SvSubject")
public class SvSubject extends HttpServlet {
	private DatabaseSubject dbSubject;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvSubject() {
        super();
        // TODO Auto-generated constructor stub
        try {
			this.dbSubject = new DatabaseSubject();
		} catch (SQLException | ClassNotFoundException e) {
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
			String nrelojStr = request.getParameter("nreloj");
			String offsetStr = request.getParameter("offset");
			
			//Handle abscence of limit and offset
			int nreloj = (nrelojStr != null) ? Integer.parseInt(nrelojStr) : 0;
			int offset = (offsetStr != null) ? Integer.parseInt(offsetStr) : 1;
			
			String json;
			
			//If the client sends nreloj 0 means that wants to retrieave all subjects from database
			if (nreloj == 0) {
				var sujetos = dbSubject.getSubjectsWithoutImage(offset);
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
			HTTPHandling.handleResponse(response, "error", "\"Error en el servidor: \"" + e.getMessage());
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		var json = new Gson().fromJson(HTTPHandling.getBody(request), subjectJsonTemplate.class);
		
		try {
			switch(json.getOpcion()) {
			case "subjectDelete":
				dbSubject.deleteSubject(json.getNreloj());
				
				
				HTTPHandling.handleResponse(response, "ok", "\"Se ha eliminado con exito el sujeto\"");
				break;
			case "subjectUpdate":
				dbSubject.updateSubject(new Subject(json.getNreloj(), 
						json.getNombre(),
						json.getApellidoPaterno(),
						json.getApellidoMaterno(),
						Base64.getDecoder().decode(new String(json.getFingerprintImage().getBytes("UTF-8"))),
						json.getActivado() >= 1 ? true : false), json.getUpdateSelector());
				
				HTTPHandling.handleResponse(response, "ok", "\"se ha actualizado el sujeto con exito\"");
				
				break;
			case "subjectUpdateWithoutImage":
				dbSubject.updateSubjectWithoutImage(new SubjectDTO(json.getNreloj(),
						json.getNombre(),
						json.getApellidoPaterno(),
						json.getApellidoMaterno(),
						json.getActivado() >= 1 ? true : false), json.getUpdateSelector());
				
				HTTPHandling.handleResponse(response, "ok", "\"se ha actualizado el sujeto con exito\"");
				
				break;
			case "subjectAdd":
				dbSubject.addSubjectToDB(new Subject(json.getNreloj(),
						json.getNombre(),
						json.getApellidoPaterno(),
						json.getApellidoMaterno(),
						Base64.getDecoder().decode(new String(json.getFingerprintImage().getBytes("UTF-8"))),
						json.getActivado() >= 1 ? true: false));
				
				HTTPHandling.handleResponse(response, "ok", "\"se ha añadido el sujeto con exito\"");
				
				break;
			case "subjectAddWithoutImage":
				dbSubject.addSubjectToDBWithoutImage(new SubjectDTO(json.getNreloj(),
						json.getNombre(),
						json.getApellidoPaterno(),
						json.getApellidoMaterno(),
						json.getActivado() >= 1 ? true : false));
				
				HTTPHandling.handleResponse(response, "ok", "\"se ha añadido el sujeto con exito\"");
				
				break;
			default:
				HTTPHandling.handleResponse(response, "error", "\"No existe tal opcion. Opciones disponibles: subjectDelete, subjectUpdate, "
						+ "subjectUpdateWithoutImage, subjectAdd, subjectAddWithoutImage\"");
			}
				
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			HTTPHandling.handleResponse(response, "error", "\"ups, ha sucedido un error\"");
		}
	}
}
