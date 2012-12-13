package at.fhooe.restaurantfinder.client;

import java.util.List;

import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RestaurantFinderRDF implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel.get().add(new HTML("Restaurant Finder loaded."));
		
		RestaurantServiceAsync service = GWT.create(RestaurantService.class);
		
		service.getRestaurants(new AsyncCallback<List<Restaurant>>() {
			
			@Override
			public void onSuccess(List<Restaurant> result) {
				RootPanel.get().clear();
				for (Restaurant restaurant : result) {
					RootPanel.get().add(new HTML(restaurant.toString()));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}
		});
	}
}
