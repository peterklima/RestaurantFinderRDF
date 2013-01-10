package at.fhooe.restaurantfinder.shared.bo;

import java.io.Serializable;

public class Address implements Serializable {

	private static final long serialVersionUID = -3949350031310514679L;

	private String id;

	private String street;

	private String houseNumber;

	private String postalCode;

	private String city;

	private String country;

	private double longitude;

	private double latitude;

	public Address() {
	}

	public String getId() {
		return id;
	}

	public void setId(String string) {
		this.id = string;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
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

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Double.toString(latitude).hashCode();
		result = prime * result + Double.toString(longitude).hashCode();
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((houseNumber == null) ? 0 : houseNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.compare(latitude, other.latitude) != 0)
			return false;
		if (Double.compare(longitude, other.longitude) != 0)
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (houseNumber == null) {
			if (other.houseNumber != null)
				return false;
		} else if (!houseNumber.equals(other.houseNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", street=" + street + ", houseNumber=" + houseNumber + ", postalCode=" + postalCode + ", city=" + city + ", country="
				+ country + ", longitude=" + longitude + ", latitude=" + latitude + "]";
	}

	public String toFormattedString() {
		String DELIMITER = ", ";
		String address = "";
		if (street != null){
			address += street;
			if (houseNumber != null){
				address += " " + houseNumber;
			}
			address += DELIMITER;
		}
		boolean useDelimiter = false;
		if (postalCode != null){
			address += postalCode + " ";
			useDelimiter = true;
		}
		if (city != null){
			address += city;
			useDelimiter = true;
		}
		if (useDelimiter){
			address += DELIMITER;
		}
		if (country != null){
			address += country;
		}
		return address;
	}

}
