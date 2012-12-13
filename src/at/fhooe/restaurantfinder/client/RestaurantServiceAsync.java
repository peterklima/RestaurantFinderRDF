package at.fhooe.restaurantfinder.client;

import java.util.List;

import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface RestaurantServiceAsync {
	void getRestaurants(AsyncCallback<List<Restaurant>> callback);
}
