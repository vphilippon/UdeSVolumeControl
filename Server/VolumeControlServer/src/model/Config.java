package model;

import java.io.Serializable;

public class Config implements Serializable{
	private static final long serialVersionUID = 4907608852944084713L;
	
	private Integer _id;
	private String _configName;
	private Double _x;
	private Double _y;
    private Integer _radius;
    private Integer _volumeRingtone;
    private Integer _volumeNotification;

	public Config(Integer id, String configName, Double x, Double y, Integer radius, Integer volumeRingtone, Integer volumeNotification) {
		_id = id;
		_configName = configName;
		_x = x;
		_y = y;
		_radius = radius;
		_volumeRingtone = volumeRingtone;
		_volumeNotification = volumeNotification;
		
		// TODO add other volumes, and maybe structure/refactor
	}
	
	public Integer getConfigId() {
		return _id;	
	}
	
	public void setConfigId(Integer id) {
		_id = id;	
	}
	
	public String getConfigName() {
		return _configName;	
	}
	
	public void setConfigName(String configName) {
		_configName = configName;	
	}
	
	public Double getCenterX() {
		return _x;	
	}
	
	public void setCenterX(Double x) {
		_x = x;	
	}
	
	public Double getCenterY() {
		return _y;	
	}
	
	public void setCenterY(Double y) {
		_y = y;	
	}
	
	public Integer getRadius() {
		return _radius;	
	}
	
	public void setRadius(Integer radius) {
		_radius = radius;	
	}
	
	public Integer getVolumeRingtone() {
		return _volumeRingtone;	
	}
	
	public void setVolumeRingtone(Integer volumeRingtone) {
		_volumeRingtone = volumeRingtone;	
	}
	
	public Integer getVolumeNotification() {
		return _volumeNotification;	
	}
	
	public void setVolumeNotification(Integer volumeNotification) {
		_volumeNotification = volumeNotification;	
	}
}
