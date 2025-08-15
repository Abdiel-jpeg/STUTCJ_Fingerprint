package jsonTemplates;

public class EventoJsonTemplate {
	private String opcion;
	private String id;
	private String titulo;
	private String descripcion;
	
	public String getOpcion() {
		return opcion;
	}
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return "eventoJsonTemplate [titulo=" + titulo + ", descripcion=" + descripcion + "]";
	}
}
