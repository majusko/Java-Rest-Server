package sk.rxjrest.dto.user;

import java.io.Serializable;
import java.util.Date;

import sk.rxjrest.domain.user.User;

public class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Date created;
	private Date changed;
	private String name;
	private String surname;
	private String image;
	private String login;
	
	public UserDto(User user) {
		super();
		this.name = user.getName();
		this.surname = user.getSurname();
		this.image = user.getImage();
		this.login = user.getLogin();
		this.id = user.getId();
		this.created = user.getCreated();
		this.changed = user.getChanged();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getChanged() {
		return changed;
	}
	public void setChanged(Date changed) {
		this.changed = changed;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}