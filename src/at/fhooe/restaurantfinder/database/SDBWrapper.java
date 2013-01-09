package at.fhooe.restaurantfinder.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.hp.hpl.jena.graph.GraphEvents;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
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
	private static final String RDFS = "rdfs";
	public static final String PREFIX_RDFS = RDFS + ":";
	
	private static final String LOCAL = "rfo";
	public static final String PREFIX_LOCAL = LOCAL + ":";
	
	private static final String LGDO = "lgdo";
	public static final String PREFIX_LGDO = LGDO + ":";

	private SDBConnection connection;
	private Store store;

	public SDBWrapper() {
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			Connection jdbcConnection = DriverManager.getConnection("jdbc:google:rdbms://restaurantfinder_rdf/restaurantfinder_rdf", "restaurant_rdf",
					"bwRnABvjqRreWNmu");
			connection = SDBFactory.createConnection(jdbcConnection);

			StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);
			storeDesc.engineType = MySQLEngineType.InnoDB;
			store = SDBFactory.connectStore(connection, storeDesc);
			store.getLoader().setChunkSize(5000);
			store.getLoader().setUseThreading(false);
			if (!StoreUtils.isFormatted(store)) {
				System.out.println("format store");
				store.getTableFormatter().create();
				Model model = SDBFactory.connectDefaultModel(store);
				model.setNsPrefix(RDFS, "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
				model.setNsPrefix(LGDO, "http://linkedgeodata.org/ontology/");
				model.setNsPrefix(LOCAL, "http://restaurantfinder.local/ontology/");
			} else {
				System.out.println("truncate store");
				store.getTableFormatter().truncate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		connection.close();
		store.close();
		super.finalize();
	}

	public SDBConnection getConnection() {
		return connection;
	}

	public Store getStore() {
		return store;
	}

	public void add(Restaurant restaurant) {
		System.out.println("add data to store");
		Model model = SDBFactory.connectDefaultModel(store);
		model.notifyEvent(GraphEvents.startRead);
		try {
			RestaurantConverter.addToModel(restaurant, model);
		} finally {
			model.notifyEvent(GraphEvents.finishRead);
		}
	}

	public ResultSet query(String queryString) {
		System.out.println("query data");
		Model model = SDBFactory.connectDefaultModel(store);
		Query query = QueryFactory.create(queryString);
		QueryExecution execution = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = execution.execSelect();
		ResultSetFormatter.out(System.out, resultSet, query);
		execution.close();
		return resultSet;
	}
}
