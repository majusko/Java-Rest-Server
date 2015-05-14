package sk.rxjrest.domain.user;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;

import sk.rxjrest.domain.BaseDomain;
import sk.rxjrest.domain.visit.Visit;

@Entity
@Table(name="User")
@SQLDelete(sql = "UPDATE User set inactiveFlag = 1 WHERE id = ?")
public class User extends BaseDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(length=128)
	private String name;

	@Column(length=128)
	private String surname;
	
	private String image;
	
	@Index(name = "login")
	@Column(length=128)
	private String login;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
	private List<Device> devices;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
	private List<Visit> visits;
	
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

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}
	
	@Override
	public boolean equals(Object obj) {  
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		if (!super.equals(obj)) {return false;}
		return true;
	}
	
	@Override     
    public int hashCode() {     
		int hash = super.hashCode();
		return hash;     
    } 
	
}