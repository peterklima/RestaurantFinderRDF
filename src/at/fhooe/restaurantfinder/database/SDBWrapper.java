package at.fhooe.restaurantfinder.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.hp.hpl.jena.graph.GraphEvents;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.MySQLEngineType;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.sdb.util.StoreUtils;

public class SDBWrapper {
	private static final String DATABASE = "restaurantfinder_rdf";
	private static final String USER = "restaurant_rdf";
	private static final String PASSWORD = "bwRnABvjqRreWNmu";

	private static final String RDFS = "rdfs";
	private static final String LOCAL = "rfo";
	private static final String LGDO = "lgdo";
	private static final String GEO = "geo";

	public static final String PREFIX_RDFS = RDFS + ":";
	public static final String PREFIX_LOCAL = LOCAL + ":";
	public static final String PREFIX_LGDO = LGDO + ":";
	public static final String PREFIX_GEO = GEO + ":";

	private SDBConnection connection;
	private Store store;

	public SDBWrapper() {
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			Connection jdbcConnection = DriverManager.getConnection("jdbc:google:rdbms://restaurantfinder_rdf/" + DATABASE, USER, PASSWORD);
			connection = SDBFactory.createConnection(jdbcConnection);

			StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);
			storeDesc.engineType = MySQLEngineType.InnoDB;
			store = SDBFactory.connectStore(connection, storeDesc);
			store.getLoader().setChunkSize(5000);
			store.getLoader().setUseThreading(false);
			if (!StoreUtils.isFormatted(store)) {
				formatStore();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void formatStore() {
		store.getTableFormatter().create();
		Model model = getModel();
		model.setNsPrefix(RDFS, "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix(LGDO, "http://linkedgeodata.org/ontology/");
		model.setNsPrefix(LOCAL, "http://restaurantfinder.local/ontology/");
		model.setNsPrefix(GEO, "http://www.w3.org/2003/01/geo/wgs84_pos#");
		model.commit();
	}

	@Override
	protected void finalize() throws Throwable {
		connection.close();
		store.close();
		super.finalize();
	}

	public void add(Restaurant restaurant) {
		Model model = getModel();
		model.notifyEvent(GraphEvents.startRead);
		try {
			RestaurantConverter.addToModel(restaurant, model);
		} finally {
			model.notifyEvent(GraphEvents.finishRead);
		}
	}

	public Model getModel() {
		return SDBFactory.connectDefaultModel(store);
	}
}
