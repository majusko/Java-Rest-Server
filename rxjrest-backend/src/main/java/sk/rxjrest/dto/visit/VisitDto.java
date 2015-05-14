package sk.rxjrest.dto.visit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import sk.rxjrest.domain.user.User;
import sk.rxjrest.domain.visit.Visit;
import sk.rxjrest.dto.user.UserDto;

public class VisitDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date created;
	private Date changed;
	private BigDecimal temp;
	private BigDecimal humidity;
	private BigDecimal pressure;
	private BigDecimal windSpeed;
	private BigDecimal windDeg;
	private String city;
	private String country;
	private UserDto user;
	
	public VisitDto(Visit visit, boolean plain) {
		super();
		
		this.id = visit.getId();
		this.created = visit.getCreated();
		this.changed = visit.getChanged();
		this.temp = visit.getTemp();
		this.humidity = visit.getHumidity();
		this.pressure = visit.getPressure();
		this.windSpeed = visit.getWindSpeed();
		this.windDeg = visit.getWindDeg();
		this.city = visit.getCity();
		this.country = visit.getCountry();
		
		if(!plain){
			
			final User user = visit.getUser();
			
			if(user != null){
				this.user = new UserDto(user);
			}
		}
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

	public void setUser(UserDto user) {
		this.user = user;
	}

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

	public UserDto getUser() {
		return user;
	}
	
}