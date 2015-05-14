package sk.rxjrest.email;

import java.util.Map;

public class EmailInfo {
	
	private String from;
	private String to;
	private String subject;
	private String template;
	private Map<String, String> replacements;

	private String preferredLanguage;

	public EmailInfo(String from, String to, String subject, String template, String preferredLanguage, Map<String, String> replacements) {
		super();
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.template = template;
		this.preferredLanguage = preferredLanguage;
		this.replacements = replacements;
	}


	public String getPreferredLanguage() {
		return preferredLanguage;
	}


	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}


	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public Map<String, String> getReplacements() {
		return replacements;
	}
	public void setReplacements(Map<String, String> replacements) {
		this.replacements = replacements;
	}
	
}
