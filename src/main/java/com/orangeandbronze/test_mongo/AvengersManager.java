package com.orangeandbronze.test_mongo;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Consumer;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class AvengersManager {

	public static void main(String[] args) {
		Document ironMan = new Document();
		ironMan.append("firstname", "Tony");
		ironMan.put("lastname", "Stark");
		System.out.println(ironMan);

		try (MongoClient mongoClient = new MongoClient("localhost")) {
			MongoDatabase db = mongoClient.getDatabase("marvel");
			db.getCollection("avengers").insertMany(Arrays.asList(ironMan));
			db.getCollection("avengers").find().forEach((Consumer<Document>) System.out::println);
		}
	}

}
