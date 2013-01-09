package at.fhooe.restaurantfinder.database;

import java.util.ArrayList;
import java.util.List;

import at.fhooe.restaurantfinder.shared.bo.Address;
import at.fhooe.restaurantfinder.shared.bo.BusinessHours;
import at.fhooe.restaurantfinder.shared.bo.Restaurant;
import at.fhooe.restaurantfinder.shared.bo.Tag;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class RestaurantLoader {
	private static final int QUERY_DEFAULT_LIMIT = 100;

	private static final String QUERY_ID_FIELD = "restaurant";
	private static final String QUERY_NAME_FIELD = "name";
	private static final String QUERY_LATITUDE_FIELD = "latitude";
	private static final String QUERY_LONGITUDE_FIELD = "longitude";
	private static final String QUERY_CITY_FIELD = "city";
	private static final String QUERY_STREET_FIELD = "street";
	private static final String QUERY_HOUSENUMBER_FIELD = "houseNumber";
	private static final String QUERY_POSTALCODE_FIELD = "postalCode";
	private static final String QUERY_HOURS_FIELD = "hours";
	private static final String QUERY_TAGS_FIELD = "tags";

	private Model model;

	public List<Restaurant> load() {
		return load(0, QUERY_DEFAULT_LIMIT);
	}

	public List<Restaurant> load(int limit) {
		return load(0, limit);
	}

	public List<Restaurant> load(int offset, int limit) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();

		SDBWrapper sdbWrapper = new SDBWrapper();
		model = sdbWrapper.getModel();
		Query restaurantQuery = getRestaurantQuery(offset, limit);
		QueryExecution execution = QueryExecutionFactory.create(restaurantQuery, model);

		ResultSet result = execution.execSelect();

		while (result.hasNext()) {
			QuerySolution row = result.next();

			Restaurant restaurant = new Restaurant();

			String restaurantId = row.get(QUERY_ID_FIELD).toString();
			restaurant.setId(restaurantId);
			restaurant.setName(row.get(QUERY_NAME_FIELD).asLiteral().getString());
			restaurant.setTags(getTagsByRestaurantId(restaurantId));
			restaurant.setHours(getBusinessHoursByRestaurantId(restaurantId));
			restaurant.setAddress(parseAddress(row));

			restaurants.add(restaurant);
		}
		execution.close();

		return restaurants;
	}

	private Address parseAddress(QuerySolution row) {
		Address address = new Address();
		RDFNode node = null;

		address.setId(row.get(QUERY_ID_FIELD).toString());
		address.setLatitude(row.get(QUERY_LATITUDE_FIELD).asLiteral().getDouble());
		address.setLongitude(row.get(QUERY_LONGITUDE_FIELD).asLiteral().getDouble());

		node = row.get(QUERY_CITY_FIELD);
		if (node != null) {
			address.setCity(node.asLiteral().getString());
		}

		node = row.get(QUERY_STREET_FIELD);
		if (node != null) {
			address.setStreet(node.asLiteral().getString());
		}

		node = row.get(QUERY_HOUSENUMBER_FIELD);
		if (node != null) {
			address.setHouseNumber(node.asLiteral().getString());
		}

		node = row.get(QUERY_POSTALCODE_FIELD);
		if (node != null) {
			address.setPostalCode(node.asLiteral().getString());
		}

		return address;
	}

	private List<Tag> getTagsByRestaurantId(String restaurantId) {
		List<Tag> tags = new ArrayList<Tag>();

		Query tagQuery = getTagQuery(restaurantId);
		QueryExecution execution = QueryExecutionFactory.create(tagQuery, model);
		ResultSet result = execution.execSelect();
		while (result.hasNext()) {
			QuerySolution row = result.next();
			Tag tag = new Tag();
			tag.setName(row.get(QUERY_TAGS_FIELD).toString());
			tags.add(tag);
		}
		execution.close();

		return tags;
	}

	private List<BusinessHours> getBusinessHoursByRestaurantId(String restaurantId) {
		List<BusinessHours> businessHours = new ArrayList<BusinessHours>();

		Query businessHoursQuery = getBusinessHoursQuery(restaurantId);
		QueryExecution execution = QueryExecutionFactory.create(businessHoursQuery, model);
		ResultSet result = execution.execSelect();
		while (result.hasNext()) {
			QuerySolution row = result.next();
			BusinessHours hour = BusinessHours.fromString(row.get(QUERY_HOURS_FIELD).toString());
			businessHours.add(hour);
		}
		execution.close();

		return businessHours;
	}

	private Query getTagQuery(String restaurantId) {
		String queryString = "PREFIX rfo:<http://restaurantfinder.local/ontology/>";
		queryString += "SELECT * ";
		queryString += "WHERE { ";
		queryString += "<" + restaurantId + "> <rfo:tag> ?" + QUERY_TAGS_FIELD + " ";
		queryString += "} ";
		queryString += "LIMIT " + QUERY_DEFAULT_LIMIT;

		return QueryFactory.create(queryString);
	}

	private Query getBusinessHoursQuery(String restaurantId) {
		String queryString = "PREFIX rfo:<http://restaurantfinder.local/ontology/>";
		queryString += "SELECT * ";
		queryString += "WHERE { ";
		queryString += "<" + restaurantId + "> <rfo:businessHours> ?" + QUERY_HOURS_FIELD + " ";
		queryString += "} ";
		queryString += "LIMIT " + QUERY_DEFAULT_LIMIT;

		return QueryFactory.create(queryString);
	}

	private Query getRestaurantQuery(int offset, int limit) {
		String queryString = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>";
		queryString += "PREFIX lgdo:<http://linkedgeodata.org/ontology/>";
		queryString += "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> ";
		queryString += "SELECT * ";
		queryString += "WHERE { ";
		queryString += "?" + QUERY_ID_FIELD + " <rdfs:label> ?" + QUERY_NAME_FIELD + " . ";
		queryString += "?" + QUERY_ID_FIELD + " <geo:lat> ?" + QUERY_LATITUDE_FIELD + " . ";
		queryString += "?" + QUERY_ID_FIELD + " <geo:long> ?" + QUERY_LONGITUDE_FIELD + " ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " <lgdo:hasCity> ?" + QUERY_CITY_FIELD + " } ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " <lgdo:hasStreet> ?" + QUERY_STREET_FIELD + " } ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " <lgdo:hasHouseNumber> ?" + QUERY_HOUSENUMBER_FIELD + " } ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " <lgdo:hasPostalCode> ?" + QUERY_POSTALCODE_FIELD + " } ";
		queryString += "} ";
		queryString += "OFFSET " + offset;
		queryString += "LIMIT " + limit;

		return QueryFactory.create(queryString);
	}
}
