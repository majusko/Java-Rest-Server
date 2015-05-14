package sk.rxjrest.dto.common;

import java.io.Serializable;

public class ErrorDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title;
	private String description;
	private String code;
	
	public ErrorDto() {
		super();
	}
	
	public ErrorDto(String title, String description, String code) {
		super();
		this.title = title;
		this.description = description;
		this.code = code;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ErrorDto [title=" + title + ", description=" + description
				+ ", code=" + code + "]";
	}

}
