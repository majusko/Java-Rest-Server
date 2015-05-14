package sk.rxjrest.dao.transfer;

import java.io.Serializable;
import java.util.List;

public class PerzistentResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<?> values;
	private PerzistentCount allValuesCount;
	
	protected PerzistentResult() {
		super();
	}
	
	public PerzistentResult(List<?> values, PerzistentCount allValuesCount) {
		super();
		this.values = values;
		this.allValuesCount = allValuesCount;
	}
	
	public List<?> getValues() {
		return values;
	}
	public void setValues(List<?> values) {
		this.values = values;
	}
	public PerzistentCount getAllValuesCount() {
		return allValuesCount;
	}
	public void setAllValuesCount(PerzistentCount allValuesCount) {
		this.allValuesCount = allValuesCount;
	}

}
