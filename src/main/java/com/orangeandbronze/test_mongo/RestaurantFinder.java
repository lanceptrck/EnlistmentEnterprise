package com.orangeandbronze.test_mongo;

import java.util.Arrays;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;

import org.apache.poi.util.SystemOutLogger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class RestaurantFinder {

	public static void main(String[] args) {
		try (MongoClient mongoClient = new MongoClient("localhost")) {
			MongoDatabase db = mongoClient.getDatabase("test");
			// db.getCollection("restaurants").find().forEach((Consumer<Document>)
			// System.out::println);

			// with param
			// db.getCollection("restaurants").find(new Document("borough", "Manhattan"))
			// .forEach((Consumer<Document>) System.out::println);

			// two params
			// db.getCollection("restaurants").find(new Document("cuisine",
			// "Italian").append("address.zipcode", "10075"))
			// .forEach((Consumer<Document>) System.out::println);

			// with operator as parameter.
			// db.getCollection("restaurants").find(new Document("grades.score", new
			// Document("$gt", 30)))
			// .forEach((Consumer<Document>) System.out::println);

			// static import com.mongodb.client.model.Filters.*
			// replace using new Document everytime
			// use mongo identifiers
			// db.getCollection("restaurants").find(eq("borough",
			// "Manhattan")).forEach((Consumer) System.out::println);

			// use helper method. import import static com.mongodb.client.model.Sorts.*;
			// db.getCollection("restaurants").find().sort(ascending("borough", "cuisine"))
			// .forEach((Consumer) System.out::println);

			// aggregation framework
			db.getCollection("restaurants")
					.aggregate(Arrays.asList(new Document("$match", new Document("cuisine", "Filipino")),
							new Document("$group",
									new Document("_id", "$borough").append("count", new Document("$sum", 1))),
							new Document("$sort", new Document("_id", 1)),
							new Document("$out", "filipinoRestaurantsByBorough")))
					.forEach((Consumer<Document>) System.out::println);

			//use helper methods. import static import 
			//static com.mongodb.client.model.Accumulators.*;
			//import static com.mongodb.client.model.Aggregates.*;
			db.getCollection("restaurants").aggregate(Arrays.asList(
					match(new Document("cuisine", "Filipino")),
					group("$borough", sum("count", 1)), 
					sort(ascending("_id")), 
					out("filipinoRestaurantsByBorough")))
					.forEach((Consumer) System.out::println);
		}
	}

}
