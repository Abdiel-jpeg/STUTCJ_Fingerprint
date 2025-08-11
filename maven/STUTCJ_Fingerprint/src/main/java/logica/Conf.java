package logica;

public class Conf {
	private double threshold;

	public Conf(double threshold) {
		super();
		this.threshold = threshold;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	@Override
	public String toString() {
		return "Conf [threshold=" + threshold + "]";
	}
}
