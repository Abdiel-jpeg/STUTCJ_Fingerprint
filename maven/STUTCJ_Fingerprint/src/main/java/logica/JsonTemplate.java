package logica;

public class JsonTemplate {
	private String option;
	private String data;
	
	@Override
	public String toString() {
		return "JsonTemplate{"
				+ "option=" + option + '\''
				+ ", data=" + data + '\''
				+ '}';
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
}
