package at.fhooe.restaurantfinder.shared.bo;

import java.io.Serializable;

public class ContactInfo implements Serializable {

	private static final long serialVersionUID = -1934043334901466150L;

	private Long id;

	private String type;

	private String info;

	public ContactInfo() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
