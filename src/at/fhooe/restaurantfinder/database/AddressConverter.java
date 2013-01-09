package at.fhooe.restaurantfinder.database;

import at.fhooe.restaurantfinder.shared.bo.Address;

import com.hp.hpl.jena.rdf.model.Model;

public class AddressConverter extends BOConverter {

	private static final String CITY_PROPERTY = "hasCity";
	private static final String POSTALCODE_PROPERTY = "hasPostalCode";
	private static final String STREET_PROPERTY = "hasStreet";
	private static final String HOUSENUMBER_PROPERTY = "hasHouseNumber";

	public static void addToModel(Address address, Model model) {
		AddressConverter converter = new AddressConverter(model);
		converter.addAddress(address);
	}

	private AddressConverter(Model model) {
		this.model = model;
	}

	public void addAddress(Address address) {
		this.resource = model.createResource(address.getId());
		addCityIfNotNull(address.getCity());
		addPostalCodeIfNotNull(address.getPostalCode());
		addStreetIfNotNull(address.getStreet());
		addHouseNumberIfNotNull(address.getHouseNumber());
	}

	private void addCityIfNotNull(String city) {
		if (city != null) {
			addLiteral(SDBWrapper.PREFIX_LGDO, CITY_PROPERTY, city);
		}
	}

	private void addPostalCodeIfNotNull(String postalCode) {
		if (postalCode != null) {
			addLiteral(SDBWrapper.PREFIX_LGDO, POSTALCODE_PROPERTY, postalCode);
		}
	}

	private void addStreetIfNotNull(String street) {
		if (street != null) {
			addLiteral(SDBWrapper.PREFIX_LGDO, STREET_PROPERTY, street);
		}
	}

	private void addHouseNumberIfNotNull(String houseNumber) {
		if (houseNumber != null) {
			addLiteral(SDBWrapper.PREFIX_LGDO, HOUSENUMBER_PROPERTY, houseNumber);
		}
	}
}
