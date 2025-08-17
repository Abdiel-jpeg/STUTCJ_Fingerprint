package logica;

public class Conf {
	private double threshold;
	private int limitAgremiado;
	private int limitEvento;
	private int limitAsistencia;

	public Conf(double threshold, int limitAgremiado, int limitEvento, int limitAsistencia) {
		this.threshold = threshold;
		this.limitAgremiado = limitAgremiado;
		this.limitEvento = limitEvento;
		this.limitAsistencia = limitAsistencia;
	}
	
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
		return "Conf [threshold=" + threshold + ", limitAgremiado=" + limitAgremiado + ", limitEvento=" + limitEvento
				+ ", limitAsistencia=" + limitAsistencia + "]";
	}
}
