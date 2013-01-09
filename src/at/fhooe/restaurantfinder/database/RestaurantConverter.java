package at.fhooe.restaurantfinder.database;

import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.hp.hpl.jena.rdf.model.Model;

public class RestaurantConverter extends BOConverter {
	private static final String NAME_PROPERTY = "label";
	private static final String DESCRIPTION_PROPERTY = "description";

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
		AddressConverter.addToModel(restaurant.getAddress(), model);
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
