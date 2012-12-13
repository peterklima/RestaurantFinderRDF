package at.fhooe.restaurantfinder.client;

import java.util.List;

import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("restaurant")
public interface RestaurantService extends RemoteService {
	List<Restaurant> getRestaurants();
}
