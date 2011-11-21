package org.akv2001.datastore;




import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Persistent;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Blob;


/*
 * Title, City, Region, Country, Humidity, Visibility, Sunrise, and Sunset
 */
@Entity(name = "Woeid")
public class Woeid implements Serializable {



	@Id
	private String key;
	
	private String title;

	//yweather:location
	private String city;
	private String region;
	private String country;
	
	//yweather:atmosphere 
	private Double humidity;
	private Double visibility;
	
	//yweather:astronomy
	private String sunrise;
	private String sunset;
	
	//description CDATA
	private String img_url;
	
	@Persistent
	private Blob img;
	private String img_type;
	
	//yweather:condition
	private String curr_text;
	private Double temp;
	private String date;
	
	//yweather:forecast
	private Double low;
	private Double high;
	private String forecast_text;
	
	/*when entity was saved*/
	private Date timestamp;
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Double getHumidity() {
		return humidity;
	}
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}
	public Double getVisibility() {
		return visibility;
	}
	public void setVisibility(Double visibility) {
		this.visibility = visibility;
	}
	public String getSunrise() {
		return sunrise;
	}
	public void setSunrise(String sunrise) {
		this.sunrise = sunrise;
	}
	public String getSunset() {
		return sunset;
	}
	public void setSunset(String sunset) {
		this.sunset = sunset;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getCurr_text() {
		return curr_text;
	}
	public void setCurr_text(String curr_text) {
		this.curr_text = curr_text;
	}
	public Double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}
	public Double getLow() {
		return low;
	}
	public void setLow(Double low) {
		this.low = low;
	}
	public Double getHigh() {
		return high;
	}
	public void setHigh(Double high) {
		this.high = high;
	}
	public String getForecast_text() {
		return forecast_text;
	}
	public void setForecast_text(String forecast_text) {
		this.forecast_text = forecast_text;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Blob getImg() {
		return img;
	}
	public void setImg(byte[] bytes) {
		this.img = new Blob(bytes);
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getImg_type() {
		return img_type;
	}
	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}
	

}
