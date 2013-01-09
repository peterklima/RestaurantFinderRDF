package at.fhooe.restaurantfinder.database;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class BOConverter {

	protected Resource resource;
	protected Model model;

	protected void addLiteral(String nameSpace, String localName, String value) {
		Property predicate = model.createProperty(nameSpace, localName);
		Literal object = model.createLiteral(value);
		model.add(resource, predicate, object);
	}

	protected void addLiteral(String nameSpace, String localName, double value) {
		Property predicate = model.createProperty(nameSpace, localName);
		Literal object = model.createTypedLiteral(value);
		model.add(resource, predicate, object);
	}

}