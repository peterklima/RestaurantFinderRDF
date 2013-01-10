package at.fhooe.restaurantfinder.server.importer;

import java.util.ArrayList;
import java.util.List;

import at.fhooe.restaurantfinder.shared.bo.Address;
import at.fhooe.restaurantfinder.shared.bo.BusinessHours;
import at.fhooe.restaurantfinder.shared.bo.Restaurant;
import at.fhooe.restaurantfinder.shared.bo.Tag;
import at.fhooe.restaurantfinder.shared.bo.Time;
import at.fhooe.restaurantfinder.shared.bo.Weekday;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class RestaurantImporter {
	private static final String TIMEOUT_PARAMETER_NAME = "timeout";
	private static final String TIMEOUT_PARAMETER_VALUE = "20000";
	private static final int RESTAURANT_QUERY_DEFAULT_LIMIT = 10;
	// private static final String DBPEDIA = "http://dbpedia.org/sparql";
	private static final String LINKEDGEODATA = "http://linkedgeodata.org/sparql";

	private static final String QUERY_ID_FIELD = "restaurant";
	private static final String QUERY_NAME_FIELD = "name";
	private static final String QUERY_LATITUDE_FIELD = "latitude";
	private static final String QUERY_LONGITUDE_FIELD = "longitude";
	private static final String QUERY_CITY_FIELD = "city";
	private static final String QUERY_STREET_FIELD = "street";
	private static final String QUERY_HOUSENUMBER_FIELD = "houseNumber";
	private static final String QUERY_POSTALCODE_FIELD = "postalCode";
	private static final String QUERY_HOURS_FIELD = "hours";
	private static final String QUERY_TAGS_FIELD1 = "cuisine1";
	private static final String QUERY_TAGS_FIELD2 = "cuisine2";

	public List<Restaurant> loadRestaurants() {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();

		Query query = getRestaurantQuery();
		QueryExecution qexec = QueryExecutionFactory.sparqlService(LINKEDGEODATA, query);
		((QueryEngineHTTP) qexec).addParam(TIMEOUT_PARAMETER_NAME, TIMEOUT_PARAMETER_VALUE);

		ResultSet result = qexec.execSelect();
		// ResultSetFormatter.out(System.out, result, query);

		while (result.hasNext()) {
			QuerySolution row = result.next();

			Restaurant restaurant = new Restaurant();

			restaurant.setId(row.get(QUERY_ID_FIELD).toString());
			restaurant.setName(row.get(QUERY_NAME_FIELD).asLiteral().getString());
			restaurant.setHours(parseHours(row));
			restaurant.setAddress(parseAddress(row));
			restaurant.setTags(parseTags(row));

			restaurants.add(restaurant);
		}

		qexec.close();
		return restaurants;
	}

	private List<Tag> parseTags(QuerySolution row) {
		List<Tag> tags = new ArrayList<Tag>();
		tags.addAll(extractFromNode(row.get(QUERY_TAGS_FIELD1)));
		tags.addAll(extractFromNode(row.get(QUERY_TAGS_FIELD2)));
		return tags;
	}

	private List<Tag> extractFromNode(RDFNode node) {
		List<Tag> tags = new ArrayList<Tag>();
		if (node != null) {
			String tagString = node.asLiteral().getString();
			String[] split = tagString.split("[,;]");
			for (String tagName : split) {
				Tag tag = new Tag();
				tag.setName(tagName.trim());
				tags.add(tag);
			}
		}
		return tags;
	}

	private List<BusinessHours> parseHours(QuerySolution row) {
		ArrayList<BusinessHours> hours = new ArrayList<BusinessHours>();

		RDFNode node = row.get(QUERY_HOURS_FIELD);
		if (node != null) {
			String hoursString = node.asLiteral().getString();

			String[] hoursStatements = hoursString.split(";");
			for (String statement : hoursStatements) {
				String[] parts = statement.trim().split(" ");
				if (parts[1].contains("-")) {
					String[] timeParts = parts[1].split("-");
					Time startTime = Time.fromString(timeParts[0]);
					Time endTime = Time.fromString(timeParts[1]);

					if (parts[0].contains("-")) {
						String[] days = parts[0].split("-");
						Weekday startDay = Weekday.fromString(days[0]);
						Weekday endDay = Weekday.fromString(days[1]);
						boolean started = false;
						boolean ended = false;
						for (Weekday day : Weekday.values()) {
							if (day.equals(startDay)) {
								started = true;
							}
							if (started && !ended) {
								hours.add(new BusinessHours(day, startTime, endTime));
							}
							if (day.equals(endDay)) {
								ended = true;
							}
						}
					} else {
						Weekday day = Weekday.fromString(parts[0]);
						hours.add(new BusinessHours(day, startTime, endTime));
					}
				}

			}
		}
		return hours;
	}

	private Address parseAddress(QuerySolution row) {
		Address address = new Address();
		RDFNode node = null;

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

	private Query getRestaurantQuery() {
		return getRestaurantQuery(RESTAURANT_QUERY_DEFAULT_LIMIT);
	}

	private Query getRestaurantQuery(int limit) {
		String queryString = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> ";
		queryString += "PREFIX lgd:<http://linkedgeodata.org/> ";
		queryString += "PREFIX lgdo:<http://linkedgeodata.org/ontology/> ";
		queryString += "PREFIX lgdp:<http://linkedgeodata.org/property/> ";
		queryString += "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> ";
		queryString += "SELECT * ";
		queryString += "FROM <http://linkedgeodata.org> ";
		queryString += "WHERE { ";
		queryString += "?" + QUERY_ID_FIELD + " a lgdo:Restaurant . ";
		queryString += "?" + QUERY_ID_FIELD + " rdfs:label ?" + QUERY_NAME_FIELD + " . ";
		queryString += "?" + QUERY_ID_FIELD + " geo:lat ?" + QUERY_LATITUDE_FIELD + " . ";
		queryString += "?" + QUERY_ID_FIELD + " geo:long ?" + QUERY_LONGITUDE_FIELD + " ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " lgdo:cuisine ?" + QUERY_TAGS_FIELD1 + " } ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " lgdp:cuisine ?" + QUERY_TAGS_FIELD2 + " } ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " lgdp:opening_hours ?" + QUERY_HOURS_FIELD + " } ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " lgdo:hasCity ?" + QUERY_CITY_FIELD + " } ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " lgdo:hasStreet ?" + QUERY_STREET_FIELD + " } ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " lgdo:hasHouseNumber ?" + QUERY_HOUSENUMBER_FIELD + " } ";
		queryString += "OPTIONAL { ?" + QUERY_ID_FIELD + " lgdo:hasPostalCode ?" + QUERY_POSTALCODE_FIELD + " } ";

		double latitude = 48.36763948202133;
		double longitude = 14.516989588737488;
		double range = 0.1;
		queryString += "FILTER(";
		queryString += "(?latitude - " + latitude + ") * (?latitude - " + latitude + ") + (?longitude - " + longitude + ") * (?longitude - " + longitude + ")";
		queryString += " < " + range + ") ";

		queryString += "} LIMIT " + limit;
		
		System.out.println(queryString);

		return QueryFactory.create(queryString);
	}
}
