package jsonTemplates;

public class ConfJsonTemplate {
	private double threshold;
	private int limitAgremiado;
	private int limitEvento;
	private int limitAsistencia;
	
	public double getThreshold() {
		return threshold;
	}
	public int getLimitAgremiado() {
		return limitAgremiado;
	}
	public int getLimitEvento() {
		return limitEvento;
	}
	public int getLimitAsistencia() {
		return limitAsistencia;
	}
	@Override
	public String toString() {
		return "confJsonTemplate [threshold=" + threshold + ", limitAgremiado=" + limitAgremiado + ", limitEvento="
				+ limitEvento + ", limitAsistencia=" + limitAsistencia + "]";
	}
}
