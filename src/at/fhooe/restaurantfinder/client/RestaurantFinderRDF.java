package at.fhooe.restaurantfinder.client;

import at.fhooe.restaurantfinder.client.viewcontroller.RestaurantsViewController;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RestaurantFinderRDF implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RestaurantsViewController restaurantsViewController = new RestaurantsViewController();
		RootPanel.get().add(restaurantsViewController);
	}
}
