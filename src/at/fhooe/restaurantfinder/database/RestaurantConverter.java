package at.fhooe.restaurantfinder.database;

import java.util.List;

import at.fhooe.restaurantfinder.shared.bo.BusinessHours;
import at.fhooe.restaurantfinder.shared.bo.Category;
import at.fhooe.restaurantfinder.shared.bo.Restaurant;
import at.fhooe.restaurantfinder.shared.bo.Tag;

import com.hp.hpl.jena.rdf.model.Model;

public class RestaurantConverter extends BOConverter {
	private static final String NAME_PROPERTY = "label";
	private static final String DESCRIPTION_PROPERTY = "description";
	private static final String HOURS_PROPERTY = "businessHours";

	public static void addToModel(Restaurant restaurant, Model model) {
		RestaurantConverter restaurantConverter = new RestaurantConverter(model);
		restaurantConverter.addRestaurant(restaurant);
	}

	private RestaurantConverter(Model model) {
		this.model = model;
	}

	public void addRestaurant(Restaurant restaurant) {
		this.resource = model.createResource(restaurant.getId());
		addNameIfNotNull(restaurant.getName());
		addDescriptionIfNotNull(restaurant.getDescription());
		addTagsIfNotNull(restaurant.getTags());
		addCategoryIfNotNull(restaurant.getCategory());
		addBusinessHoursIfNotNull(restaurant.getHours());
		AddressConverter.addToModel(restaurant.getAddress(), model);
	}

	private void addBusinessHoursIfNotNull(List<BusinessHours> hours) {
		if (hours != null) {
			for (BusinessHours hour : hours) {
				addLiteral(SDBWrapper.PREFIX_LOCAL, HOURS_PROPERTY, hour.toString());
			}
		}
	}

	private void addTagsIfNotNull(List<Tag> tags) {
		if (tags != null) {
			for (Tag tag : tags) {
				addLiteral(SDBWrapper.PREFIX_LOCAL, "tag", tag.getName());
			}
		}
	}

	private void addCategoryIfNotNull(Category category) {
		if (category != null) {
			addLiteral(SDBWrapper.PREFIX_LOCAL, "category", category.getName());
		}
	}

	private void addNameIfNotNull(String name) {
		if (name != null) {
			addLiteral(SDBWrapper.PREFIX_RDFS, NAME_PROPERTY, name);
		}
	}

	private void addDescriptionIfNotNull(String description) {
		if (description != null) {
			addLiteral(SDBWrapper.PREFIX_LOCAL, DESCRIPTION_PROPERTY, description);
		}
	}
}
