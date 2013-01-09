package at.fhooe.restaurantfinder.database;

import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class RestaurantConverter {
	private static final String NAME_PROPERTY = "label";

	public static void addToModel(Restaurant restaurant, Model model) {
		Resource resource = model.createResource("http://linkedgeodata.org/triplify/node60116296");

		Property label = model.createProperty(SDBWrapper.PREFIX_RDFS, NAME_PROPERTY);
		Literal name = model.createLiteral(restaurant.getName());
		model.add(resource, label, name);
	}

}
