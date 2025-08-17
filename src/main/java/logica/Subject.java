package logica;

import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintTemplate;

public class Subject {
	private int nreloj;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private byte[] image;
	private FingerprintTemplate template;
	private boolean activated;
	//An instance for options for Digital Persona 45000 Fingerprint Reader with 512 DPI images
	private final FingerprintImageOptions options = new FingerprintImageOptions().dpi(512); 
	private final Crypto crypto = new Crypto();
	
	//Constructor for retreaving subjects from database
	public Subject(int nreloj, String nombre, String apellidoPaterno, String apellidoMaterno, byte[] image,
			byte[] template, boolean activated) throws Exception {
		super();
		
		this.nreloj = nreloj;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.image = crypto.decryptImage(image);
		this.template = new FingerprintTemplate(new FingerprintImage(this.image, options));
		this.activated = activated;
	}

	//Constructor for new subjects
	public Subject(int nreloj, String nombre, String apellidoPaterno, String apellidoMaterno, byte[] image,
			byte[] template) {
		super();
		this.nreloj = nreloj;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.image = image;
		this.template = new FingerprintTemplate(new FingerprintImage(this.image, options));
		this.activated = true;
	}

	//This constructor method is called to build the template in case it's not given
	public Subject(int nreloj, String nombre, String apellidoPaterno, String apellidoMaterno, byte[] image, boolean activated) throws Exception {
		super();
		this.nreloj = nreloj;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.image = crypto.encryptImage(image);
		this.template = new FingerprintTemplate(new FingerprintImage(image, options));
		this.activated = activated;
	}

	//It's only here to add test querys to the database
	public Subject(String nombre, byte[] image) {
		super();
		nreloj = 0; //This doesn't mind 'cause the database is only handling test subjects and generating ids from 1 to 100, 
					//which in real nreloj does not exists
		this.nombre = nombre;
		this.apellidoPaterno = "tepi";
		this.apellidoMaterno = "colacola";
		this.image = image;
		this.template = new FingerprintTemplate(new FingerprintImage(this.image, options));
		this.activated = true;
	}

	public int getNreloj() {
		return nreloj;
	}

	public void setNreloj(int nreloj) {
		this.nreloj = nreloj;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public FingerprintTemplate getTemplate() {
		return template;
	}

	public void setTemplate(FingerprintTemplate template) {
		this.template = template;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	
}
