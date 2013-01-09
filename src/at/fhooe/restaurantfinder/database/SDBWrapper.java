package at.fhooe.restaurantfinder.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.MySQLEngineType;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.sdb.util.StoreUtils;

public class SDBWrapper {
	private SDBConnection connection;
	private Store store;

	public SDBWrapper() {
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			Connection jdbcConnection = DriverManager.getConnection("jdbc:google:rdbms://restaurantfinder_rdf/restaurantfinder_rdf", "restaurant_rdf",
					"bwRnABvjqRreWNmu");
			connection = SDBFactory.createConnection(jdbcConnection);

			StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutSimple, DatabaseType.MySQL);
			storeDesc.engineType = MySQLEngineType.InnoDB;
			store = SDBFactory.connectStore(connection, storeDesc);
			if (!StoreUtils.isFormatted(store)) {
				store.getTableFormatter().create();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public SDBConnection getConnection() {
		return connection;
	}

	public Store getStore() {
		return store;
	}

}
