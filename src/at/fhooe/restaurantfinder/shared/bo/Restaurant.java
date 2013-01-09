package at.fhooe.restaurantfinder.shared.bo;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {
	
	private static final long serialVersionUID = 281909316735095930L;

	private String id;
	
	private String name;
	
	private String description;
	
	private Address address;
	
	private String imageUrl;
	
	private float pointsOutOfTen;
	
	private int priceRangeOutOfThree;
	
	private Category category;
	
	private List<ContactInfo> contactInfos;
	
	private List<Tag> tags;
	
	private List<BusinessHours> hours;
	
	private List<DayRange> closedDays;
	
	public Restaurant(){
	}
	
	public Restaurant(String id, String name, String description, Address address, Category category, String imageUrl, float pointsOutOfTen){
		this.id = id;
		this.name = name;
		this.description = description;
		this.address = address;
		this.category = category;
		this.imageUrl = imageUrl;
		this.pointsOutOfTen = pointsOutOfTen;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public float getPointsOutOfTen() {
		return pointsOutOfTen;
	}

	public void setPointsOutOfTen(float pointsOutOfTen) {
		this.pointsOutOfTen = pointsOutOfTen;
	}

	public int getPriceRangeOutOfThree() {
		return priceRangeOutOfThree;
	}

	public void setPriceRangeOutOfThree(int priceRangeOutOfThree) {
		this.priceRangeOutOfThree = priceRangeOutOfThree;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<ContactInfo> getContactInfos() {
		return contactInfos;
	}

	public void setContactInfos(List<ContactInfo> contactInfos) {
		this.contactInfos = contactInfos;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<BusinessHours> getHours() {
		return hours;
	}

	public void setHours(List<BusinessHours> hours) {
		this.hours = hours;
	}

	public List<DayRange> getClosedDays() {
		return closedDays;
	}

	public void setClosedDays(List<DayRange> closedDays) {
		this.closedDays = closedDays;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Restaurant other = (Restaurant) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", address=" + address + ", category=" + category + ", tags=" + tags + "]";
	}

}
