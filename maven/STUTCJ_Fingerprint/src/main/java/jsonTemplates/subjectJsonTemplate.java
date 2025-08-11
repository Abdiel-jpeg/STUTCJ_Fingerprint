package jsonTemplates;

public class subjectJsonTemplate {
	private String opcion,
		nombre,
		apellidoPaterno,
		apellidoMaterno,
		fingerprintImage;
	private int nreloj, activado, updateSelector;

	public String getOpcion() {
		return opcion;
	}

	public int getUpdateSelector() {
		return updateSelector;
	}

	public void setUpdateSelector(int updateSelector) {
		this.updateSelector = updateSelector;
	}

	public void setOpcion(String opcion) {
		this.opcion = opcion;
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
	public String getFingerprintImage() {
		return fingerprintImage;
	}

	public void setFingerprintImage(String fingerprintImage) {
		this.fingerprintImage = fingerprintImage;
	}

	public int getNreloj() {
		return nreloj;
	}

	public void setNreloj(int nreloj) {
		this.nreloj = nreloj;
	}

	public int getActivado() {
		return activado;
	}

	public void setActivado(int activado) {
		this.activado = activado;
	}

	@Override
	public String toString() {
		return "subjectJsonTemplate [opcion=" + opcion + ", nombre=" + nombre + ", apellidoPaterno=" + apellidoPaterno
				+ ", apellidoMaterno=" + apellidoMaterno + ", fingerprintImage=" + fingerprintImage + ", nreloj="
				+ nreloj + ", activado=" + activado + ", updateSelector=" + updateSelector + "]";
	}
}
