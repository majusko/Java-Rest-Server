package sk.rxjrest.dto.visit;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitFormDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal temp;
	private BigDecimal humidity;
	private BigDecimal pressure;
	private BigDecimal windSpeed;
	private BigDecimal windDeg;
	private String city;
	private String country;
	
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
	
}