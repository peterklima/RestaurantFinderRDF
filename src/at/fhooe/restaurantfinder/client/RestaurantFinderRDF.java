package at.fhooe.restaurantfinder.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RestaurantFinderRDF implements EntryPoint {
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel.get().add(new HTML("Restaurant Finder loaded."));
		String input = "foo";
		greetingService.greetServer(input, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) {
			}
		});
	}
}
