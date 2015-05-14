package sk.rxjrest.domain.visit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;

import sk.rxjrest.domain.BaseDomain;
import sk.rxjrest.domain.user.User;
import sk.rxjrest.dto.visit.VisitFormDto;

@Entity
@Table(name="Visit")
@SQLDelete(sql = "UPDATE Visit set inactiveFlag = 1 WHERE id = ?")
public class Visit extends BaseDomain implements Serializable {

	public Visit() {
		super();
	}

	public Visit(VisitFormDto visit, User user) {
		super(new Date(), new Date());
		this.temp = visit.getTemp();
		this.humidity = visit.getHumidity();
		this.pressure = visit.getPressure();
		this.windSpeed = visit.getWindSpeed();
		this.windDeg = visit.getWindDeg();
		this.city = visit.getCity();
		this.country = visit.getCountry();
		this.user = user;
	}

	private static final long serialVersionUID = 1L;

	@Column(precision = 11, scale = 3)
	private BigDecimal temp;

	@Column(precision = 11, scale = 3)
	private BigDecimal humidity;

	@Column(precision = 11, scale = 3)
	private BigDecimal pressure;

	@Column(precision = 11, scale = 3)
	private BigDecimal windSpeed;

	@Column(precision = 11, scale = 3)
	private BigDecimal windDeg;

	private String city;

	private String country;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	@NotFound(action=NotFoundAction.IGNORE)
	private User user;

	public BigDecimal getTemp() {
		return temp;
	}

	public void setTemp(BigDecimal temp) {
		this.temp = temp;
	}

	public BigDecimal getHumidity() {
		return humidity;
	}

	public void setHumidity(BigDecimal humidity) {
		this.humidity = humidity;
	}

	public BigDecimal getPressure() {
		return pressure;
	}

	public void setPressure(BigDecimal pressure) {
		this.pressure = pressure;
	}

	public BigDecimal getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(BigDecimal windSpeed) {
		this.windSpeed = windSpeed;
	}

	public BigDecimal getWindDeg() {
		return windDeg;
	}

	public void setWindDeg(BigDecimal windDeg) {
		this.windDeg = windDeg;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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