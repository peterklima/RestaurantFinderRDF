package at.fhooe.restaurantfinder.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.fhooe.restaurantfinder.client.RestaurantService;
import at.fhooe.restaurantfinder.shared.bo.Address;
import at.fhooe.restaurantfinder.shared.bo.Restaurant;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class RestaurantServiceImpl extends RemoteServiceServlet implements RestaurantService {

	public static final String DBPEDIA = "http://dbpedia.org/sparql";
	public static final String LINKEDGEODATA = "http://linkedgeodata.org/sparql";

	public List<Restaurant> getRestaurants() {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();

//		http://dbpedia.org/sparql
		// dbpedia: select *
		// where {
		// ?restaurant a <http://schema.org/Restaurant> .
		// ?restaurant dbpprop:country ?country .
		// #?restaurant dcterms:subject <http://dbpedia.org/class/yago/RestaurantsInNewYorkCity> .
		// }

//		http://dbpedia.org/class/yago/IndianIngredients
		
//		http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=select+distinct+%3FConcept+where+%7B%5B%5D+a+%3FConcept%7D&format=text%2Fhtml&timeout=0&debug=on
		
		try {
			String queryStr = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> PREFIX lgd:<http://linkedgeodata.org/> PREFIX lgdo:<http://linkedgeodata.org/ontology/> PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>"
					+ "SELECT * "
					+ "FROM <http://linkedgeodata.org>"
					+ "{ "
					+

					"?restaurant a lgdo:Restaurant . "
					+ "?restaurant geo:lat ?latitude."
					+ "?restaurant geo:long ?longitude."
					+ "?restaurant rdfs:label ?name"
					+

					"} LIMIT 10";

			Query query = QueryFactory.create(queryStr);

			QueryExecution qexec = QueryExecutionFactory.sparqlService(LINKEDGEODATA, query);
			((QueryEngineHTTP) qexec).addParam("timeout", "10000");

			ResultSet rs = qexec.execSelect();

			// ResultSetFormatter.out(System.out, rs, query);

			while (rs.hasNext()) {
				System.out.println(rs.getRowNumber());

				Binding binding = rs.nextBinding();
				System.out.println("binding: " + binding.toString());

				Restaurant restaurant = new Restaurant();
				Address address = new Address();
				restaurant.setAddress(address);

				Iterator<Var> vars = binding.vars();
				while (vars.hasNext()) {
					Var var = vars.next();
					Node node = binding.get(var);

					// set id
					if (node.isURI()) {
						restaurant.setId(node.toString());
					}

					// set name of restaurant
					if (var.getName().equals("name")) {
						restaurant.setName(node.toString().replace("\"", ""));
					}

					// set latitude
					if (var.getName().equals("latitude")) {
						if (node.isLiteral()) {
							try {
								address.setLatitude(Double.parseDouble(node.getLiteralValue().toString()));
							} catch (Exception exception) {
								System.out.println("parsing exception on parsing latitude");
							}
						}
					}

					// set longitude
					if (var.getName().equals("longitude")) {
						if (node.isLiteral()) {
							try {
								address.setLongitude(Double.parseDouble(node.getLiteralValue().toString()));
							} catch (Exception exception) {
								System.out.println("parsing exception on parsing logitude");
							}
						}
					}
				}
				restaurants.add(restaurant);
			}

			qexec.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return restaurants;

	}

}
