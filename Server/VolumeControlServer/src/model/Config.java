package model;

import java.io.Serializable;

public class Config implements Serializable{
	private static final long serialVersionUID = 4907608852944084713L;
	
	private Integer _id;
	private Integer _x;
	private Integer _y;
    private Double _radius;
    private Integer _volumeMain;

	public Config(Integer id, Integer x, Integer y, Double radius, Integer volumeMain) {
		_id = id;
		_x = x;
		_y = y;
		_radius = radius;
		_volumeMain = volumeMain;
		
		// TODO add other volumes, and maybe structure/refactor
	}
	
	public Integer getConfigId() {
		return _id;	
	}
	
	public void setConfigId(Integer id) {
		_id = id;	
	}
	
	public Integer getCenterX() {
		return _x;	
	}
	
	public void setCenterX(Integer x) {
		_x = x;	
	}
	
	public Integer getCenterY() {
		return _y;	
	}
	
	public void setCenterY(Integer y) {
		_y = y;	
	}
	
	public Double getRadius() {
		return _radius;	
	}
	
	public void setRadius(Double radius) {
		_radius = radius;	
	}
	
	public Integer getVolumeMain() {
		return _volumeMain;	
	}
	
	public void setVolumeMain(Integer volumeMain) {
		_volumeMain = volumeMain;	
	}
}
