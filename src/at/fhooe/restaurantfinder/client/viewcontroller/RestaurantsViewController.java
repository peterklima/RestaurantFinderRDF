package at.fhooe.restaurantfinder.client.viewcontroller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import at.fhooe.restaurantfinder.client.RestaurantService;
import at.fhooe.restaurantfinder.client.RestaurantServiceAsync;
import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;

public class RestaurantsViewController extends HTMLPanel {

	private static final String MARKER_RESTAURANT = "images/marker_restaurant.png";

	private RestaurantServiceAsync service = GWT.create(RestaurantService.class);

	private CellTable<Restaurant> table;

	private Column<Restaurant, String> columnId;

	private Column<Restaurant, String> columnName;

	private Column<Restaurant, String> columnAddress;

	private GoogleMap map;

	public RestaurantsViewController() {
		super("");

		initGoogleMaps();

		add(initTable());
	}

	private void initGoogleMaps() {
	    LatLng myLatLng = LatLng.create(0, 0);
	    MapOptions myOptions = MapOptions.create();
	    myOptions.setZoom(1);
	    myOptions.setCenter(myLatLng);
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	    map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
	}

	private Widget initTable() {
		table = new CellTable<Restaurant>();
		// data provider
		AsyncDataProvider<Restaurant> dataProvider = new AsyncDataProvider<Restaurant>() {
			@Override
			protected void onRangeChanged(HasData<Restaurant> display) {
				update();
			}
		};
		dataProvider.addDataDisplay(table);

		// column id
		columnId = new Column<Restaurant, String>(new TextCell()) {
			@Override
			public String getValue(Restaurant object) {
				return object.getId();
			}
		};
		columnId.setSortable(true);
		table.addColumn(columnId, "id");

		// column name
		columnName = new Column<Restaurant, String>(new TextCell()) {
			@Override
			public String getValue(Restaurant object) {
				return object.getName();
			}
		};
		columnName.setSortable(true);
		table.addColumn(columnName, "name");

		// column address
		columnAddress = new Column<Restaurant, String>(new TextCell()) {
			@Override
			public String getValue(Restaurant object) {
				return object.getAddress().toFormattedString();
			}
		};
		columnAddress.setSortable(false);
		table.addColumn(columnAddress, "address");

		// connect sort handler with table
		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);
		table.getColumnSortList().push(columnId);
		table.getColumnSortList().push(columnName);

		return table;
	}

	private void update() {
		service.getRestaurants(new AsyncCallback<List<Restaurant>>() {

			@Override
			public void onSuccess(List<Restaurant> result) {
				updateTable(result);

				updateMap(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();

				Window.alert(caught.getMessage());
			}
		});
	}

	private void updateMap(List<Restaurant> result) {
		if (result != null && map != null) {
			for (Restaurant restaurant : result) {
				MarkerOptions markerOptions = MarkerOptions.create();
				markerOptions.setPosition(LatLng.create(restaurant.getAddress().getLatitude(), restaurant.getAddress().getLongitude()));
				markerOptions.setMap(map);
				markerOptions.setIcon(MARKER_RESTAURANT);
				markerOptions.setTitle(restaurant.getName());
				markerOptions.setClickable(true);
				Marker.create(markerOptions);
			}
			
			zoomToFit(result);
		}
	}

	private void zoomToFit(List<Restaurant> result) {
		double minLat = 0;
		double maxLat = 0;
		double minLon = 0;
		double maxLon = 0;
 		for (Restaurant restaurant : result) {
			double latitude = restaurant.getAddress().getLatitude();
			double longitude = restaurant.getAddress().getLongitude();
			if (latitude > maxLat){
				maxLat = latitude;
			}
			if (latitude < minLat){
				minLat = latitude;
			}
			if (longitude > maxLon){
				maxLon = longitude;
			}
			if (longitude < minLon){
				minLon = longitude;
			}
			if (minLat == 0 && minLon == 0){
				// init
				minLat = maxLat;
				minLon = maxLon;
			}
		}
 		LatLngBounds bounds = LatLngBounds.create(LatLng.create(minLat, minLon), LatLng.create(maxLat, maxLon));
 		map.fitBounds(bounds);
	}

	protected void updateTable(List<Restaurant> result) {
		if (result != null) {
			// sort
			final ColumnSortInfo columnSortInfo = table.getColumnSortList().get(0);
			final Column<?, ?> sortByColumn = columnSortInfo.getColumn();

			Collections.sort(result, new Comparator<Restaurant>() {

				@Override
				public int compare(Restaurant o1, Restaurant o2) {
					int comparison = 0;
					if (sortByColumn.equals(columnId)) {
						comparison = o1.getId().compareTo(o2.getId());
					} else if (sortByColumn.equals(columnName)) {
						comparison = o1.getName().compareTo(o2.getName());
					}
					if (table.getColumnSortList().get(0).isAscending()) {
						return comparison;
					} else {
						return -comparison;
					}
				}

			});

			table.setRowData(result);
		} else {
			table.setRowCount(0);
		}

	}

}
