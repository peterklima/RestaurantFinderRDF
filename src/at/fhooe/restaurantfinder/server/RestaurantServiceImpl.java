package at.fhooe.restaurantfinder.server;

import java.util.List;

import at.fhooe.restaurantfinder.client.RestaurantService;
import at.fhooe.restaurantfinder.database.RestaurantLoader;
import at.fhooe.restaurantfinder.database.SDBWrapper;
import at.fhooe.restaurantfinder.importer.RestaurantImporter;
import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class RestaurantServiceImpl extends RemoteServiceServlet implements RestaurantService {

	public List<Restaurant> getRestaurants() {
//		http://dbpedia.org/sparql
		// dbpedia: select *
		// where {
		// ?restaurant a <http://schema.org/Restaurant> .
		// ?restaurant dbpprop:country ?country .
		// #?restaurant dcterms:subject <http://dbpedia.org/class/yago/RestaurantsInNewYorkCity> .
		// }

//		http://dbpedia.org/class/yago/IndianIngredients
		
		// list all concepts
		// select distinct ?concept where { [] a ?concept } limit 10
//		http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=select+distinct+%3FConcept+where+%7B%5B%5D+a+%3FConcept%7D&format=text%2Fhtml&timeout=0&debug=on
		
//			String queryStr = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> PREFIX lgd:<http://linkedgeodata.org/> PREFIX lgdo:<http://linkedgeodata.org/ontology/> PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>"
//					+ "SELECT * "
//					+ "FROM <http://linkedgeodata.org>"
//					+ "{ "
//					+
//
//					"?restaurant a lgdo:Restaurant . "
//					+ "?restaurant geo:lat ?latitude."
//					+ "?restaurant geo:long ?longitude."
//					+ "?restaurant rdfs:label ?name"
//					+
//
//					"} LIMIT 10";
		
		
		// recipes
		// http://linkedrecipes.org/schema
		
//		PREFIX rdfs: <htp://ww.w3.org/2000/01/rdf-schema#>
//			PREFIX recipe: <htp://linkedrecipes.org/schema/>
//
//			SELECT ?label ?recipe { 
//			    ?recipe a recipe:Recipe . 
//			    ?recipe rdfs:label ?label.  
//
//			    ?curry recipe:ingredient_of ?recipe .
//			    ?curry rdfs:label ?curry_label . 
//			    FILTER (REGEX(STR(?curry_label), 'curry', 'i'))
//
//			    ?chicken recipe:ingredient_of ?recipe .
//			    ?chicken rdfs:label ?chicken_label . 
//			    FILTER (REGEX(STR(?chicken_label), 'chicken', 'i'))
//			}

//		RestaurantImporter restaurantImporter = new RestaurantImporter();
//		return restaurantImporter.loadRestaurants();
		
		RestaurantImporter restaurantImporter = new RestaurantImporter();
		List<Restaurant> restaurantList = restaurantImporter.loadRestaurants(10);

		SDBWrapper sdbWrapper = new SDBWrapper();

		for (Restaurant restaurant : restaurantList) {
			sdbWrapper.add(restaurant);
		}

		RestaurantLoader restaurantLoader = new RestaurantLoader();
		return restaurantLoader.load(100);
	}

}
