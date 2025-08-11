package logica;

import java.util.Base64;

public class SubjectDTOImage {
	public int nreloj;
	public String nombre;
	public String apellidoPaterno;
	public String apellidoMaterno;
	public String fingerprintImage;
	public boolean activated;
	
	public SubjectDTOImage(int nreloj, String nombre, String apellidoPaterno, String apellidoMaterno, byte[] encodedImage, boolean activated) {
		super();
		this.nreloj = nreloj;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.fingerprintImage = new String(Base64.getEncoder().encode(encodedImage));
		this.activated = activated;
	}
}
