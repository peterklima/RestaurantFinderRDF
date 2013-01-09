package at.fhooe.restaurantfinder.server;

import java.util.ArrayList;
import java.util.List;

import at.fhooe.restaurantfinder.client.RestaurantService;
import at.fhooe.restaurantfinder.database.SDBWrapper;
import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class RestaurantServiceImpl extends RemoteServiceServlet implements RestaurantService {

	public List<Restaurant> getRestaurants() {
//		RestaurantImporter restaurantImporter = new RestaurantImporter();
//		return restaurantImporter.loadRestaurants();
		
		SDBWrapper sdbWrapper = new SDBWrapper();
		sdbWrapper.add();
		
		sdbWrapper.query("PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>" +
				"PREFIX lgdo:<http://linkedgeodata.org/ontology/>" +
				"SELECT * " +
				"WHERE { " +
				"?s <rdfs:label> ?name " +
				"} LIMIT 10");
		return new ArrayList<Restaurant>();
	}

}
