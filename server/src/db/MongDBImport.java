package db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.text.ParseException;

public class MongDBImport {
	public static void main(String[] args) throws ParseException {
		MongoClient mongoClient = new MongoClient();
		MongoDatabase db = mongoClient.getDatabase(DBUtil.DB_NAME);
		db.getCollection("users").insertOne(
				new Document()
						.append("first_name", "John")
						.append("last_name", "Smith")
						.append("password", "password")
						.append("user_id", "3229c1097c00d497a0fd282d586be050"));
		mongoClient.close();
	}
}
