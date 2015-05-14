package sk.rxjrest.domain.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

@Entity
@Table(name="Device")
public class Device implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, precision=11)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Index(name = "accessToken")
	@Column(length=64)
	private String accessToken;

	@Index(name = "refreshToken")
	@Column(length=64)
	private String refreshToken;

	private Date accessTokenExpire;

	@Index(name = "installationId")
	private String installationId;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="userId", nullable=false)
	@Where(clause = "inactiveFlag = 0")
	@NotFound(action=NotFoundAction.IGNORE)
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getAccessTokenExpire() {
		return accessTokenExpire;
	}

	public void setAccessTokenExpire(Date accessTokenExpire) {
		this.accessTokenExpire = accessTokenExpire;
	}

	public String getInstallationId() {
		return installationId;
	}

	public void setInstallationId(String installationId) {
		this.installationId = installationId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessToken == null) ? 0 : accessToken.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((refreshToken == null) ? 0 : refreshToken.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Device){
			if (this == obj)
				return true;
			if (getClass() != obj.getClass())
				return false;
			Device other = (Device) obj;
			if (accessToken == null) {
				if (other.accessToken != null)
					return false;
			} else if (!accessToken.equals(other.accessToken))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (refreshToken == null) {
				if (other.refreshToken != null)
					return false;
			} else if (!refreshToken.equals(other.refreshToken))
				return false;
			return true;
		} else {
			return false;
		}
	}
	
}
