package jsonTemplates;

public class confJsonTemplate {
	private double threshold;
	private int limit;
	
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
		return "confJsonTemplate [threshold=" + threshold + ", limit=" + limit + "]";
	}
	
}
