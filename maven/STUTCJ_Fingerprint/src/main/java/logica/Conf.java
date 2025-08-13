package logica;

public class Conf {
	private double threshold;
	private int limit;
	
	public Conf(double threshold, int limit) {
		super();
		this.threshold = threshold;
		this.limit = limit;
	}
	
	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "Conf [threshold=" + threshold + ", limit=" + limit + "]";
	}
}
