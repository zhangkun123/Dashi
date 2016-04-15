package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import model.Restaurant;

import org.json.JSONObject;

public class MySQLImport {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = null;
			String line = null;

			try {
				conn = DriverManager.getConnection(DBUtil.MYSQL_URL);
			} catch (SQLException e) {
				System.out.println("SQLException " + e.getMessage());
				System.out.println("SQLState " + e.getSQLState());
				System.out.println("VendorError " + e.getErrorCode());
			}
			if (conn == null) {
				return;
			}
			//Step 1 Drop tables. 
			Statement stmt = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS history";
			stmt.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS restaurants";
			stmt.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS users";
			stmt.executeUpdate(sql);

			//Step 2: create tables
			sql = "CREATE TABLE restaurants "
					+ "(business_id VARCHAR(255) NOT NULL, "
					+ " name VARCHAR(255), " + "categories VARCHAR(255), "
					+ "city VARCHAR(255), " + "state VARCHAR(255), "
					+ "stars FLOAT," + "full_address VARCHAR(255), "
					+ "latitude FLOAT, " + " longitude FLOAT, "
					+ "image_url VARCHAR(255), " + "url VARCHAR(255), "
					+ " PRIMARY KEY ( business_id ))";
			stmt.executeUpdate(sql);
						
			sql = "CREATE TABLE users "
					+ "(user_id VARCHAR(255) NOT NULL, "
					+ " password VARCHAR(255) NOT NULL, "
					+ " first_name VARCHAR(255), last_name VARCHAR(255), "
					+ " PRIMARY KEY ( user_id ))";
			stmt.executeUpdate(sql);
			
			sql = "CREATE TABLE history "
					+ "(visit_history_id bigint(20) unsigned NOT NULL AUTO_INCREMENT, "
					+ " user_id VARCHAR(255) NOT NULL , "
					+ " business_id VARCHAR(255) NOT NULL, " 
					+ " last_visited_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, "
					+ " PRIMARY KEY (visit_history_id),"
					+ "FOREIGN KEY (business_id) REFERENCES restaurants(business_id),"
					+ "FOREIGN KEY (user_id) REFERENCES users(user_id))";
			stmt.executeUpdate(sql);

			//Step 3: insert data
			BufferedReader reader = new BufferedReader(new FileReader(
					"../dataset/yelp_academic_dataset_business.json"));
			while ((line = reader.readLine()) != null) {
				JSONObject restaurant = new JSONObject(line);
				String business_id = restaurant.getString("business_id");
				String name = Restaurant.parseString(restaurant.getString("name"));
				String categories = Restaurant.parseString(Restaurant.jsonArrayToString(restaurant
						.getJSONArray("categories")));
				String city = Restaurant.parseString(restaurant.getString("city"));
				String state = restaurant.getString("state");
				String fullAddress = Restaurant.parseString(restaurant
						.getString("full_address"));
				double stars = restaurant.getDouble("stars");
				double latitude = restaurant.getDouble("latitude");
				double longitude = restaurant.getDouble("longitude");
				String imageUrl = "http://www.example.com/img.JPG";
				String url = "http://www.yelp.com";
				sql = "INSERT INTO restaurants " + "VALUES ('" + business_id
						+ "', '" + name + "', '" + categories + "', '"
						+ city + "', '" + state + "', " + stars + ", '"
						+ fullAddress + "', " + latitude + "," + longitude
						+ ", '" +imageUrl + "', '" + url + "')";
				System.out.println(sql);
				stmt.executeUpdate(sql);
			}
			reader.close();
			
			sql = "INSERT INTO users " + "VALUES (\"1111\", \"3229c1097c00d497a0fd282d586be050\", \"John\", \"Smith\")";
			stmt.executeUpdate(sql);

			System.out.println("Done Importing");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
