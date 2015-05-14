package sk.rxjrest.dao.transfer;

import java.io.Serializable;

import sk.rxjrest.dao.generic.common.FieldType;
import sk.rxjrest.dao.generic.common.FilterOperator;
import sk.rxjrest.exception.RestErrorCode;
import sk.rxjrest.exception.RestException;

public class PerzistentCondition implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fieldName;
	private Object value;
	private FieldType fieldType;
	private FilterOperator fieldOperator;
	private boolean or;
	
	protected PerzistentCondition() {
		super();
	}
	
	public PerzistentCondition(String fieldName, Object value, FieldType fieldType,
			FilterOperator fieldOperator, boolean or) throws RestException {
		super();
		
		if(fieldName != null && value != null && fieldType != null && fieldOperator != null){
			
			this.fieldName = fieldName;
			this.value = value;
			this.fieldType = fieldType;
			this.fieldOperator = fieldOperator;
			this.or = or;

		} else {
			throw new RestException(RestErrorCode.INVALID_INPUT, "Missing conditions for DB DTO build.");
		}
		
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public FilterOperator getFieldOperator() {
		return fieldOperator;
	}

	public void setFieldOperator(FilterOperator fieldOperator) {
		this.fieldOperator = fieldOperator;
	}

	public boolean isOr() {
		return or;
	}

	public void setOr(boolean or) {
		this.or = or;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PerzistentCondition other = (PerzistentCondition) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		return true;
	}
	
}
