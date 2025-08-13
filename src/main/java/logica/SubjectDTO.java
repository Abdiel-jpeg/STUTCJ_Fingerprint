package logica;

public class SubjectDTO {
	public int nreloj;
	public String nombre;
	public String apellidoPaterno;
	public String apellidoMaterno;
	public boolean activated;
	
	public SubjectDTO(int nreloj, String nombre, String apellidoPaterno, String apellidoMaterno, boolean activated) {
		super();
		this.nreloj = nreloj;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.activated = activated;
	}
}
