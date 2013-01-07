package at.fhooe.restaurantfinder.server;

import java.util.List;

import at.fhooe.restaurantfinder.client.RestaurantService;
import at.fhooe.restaurantfinder.server.importer.RestaurantImporter;
import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class RestaurantServiceImpl extends RemoteServiceServlet implements RestaurantService {

	public List<Restaurant> getRestaurants() {
		RestaurantImporter restaurantImporter = new RestaurantImporter();
		return restaurantImporter.loadRestaurants();
	}

}
