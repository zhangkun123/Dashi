package api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;

import db.DBConnection;
import db.MongoDBConnection;
import db.MySQLConnection;

/**
 * Servlet implementation class SearchRestaurants
 */
@WebServlet(description = "Search Restaurants near a location with latitude and longitude", urlPatterns = { "/restaurants" })
public class SearchRestaurants extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DBConnection connection = new MySQLConnection();
	//private static final DBConnection connection = new MongoDBConnection();

	public SearchRestaurants() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// allow access only if session exists
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			response.setStatus(403);
			return;
		}
		JSONArray array = null;
		if (request.getParameterMap().containsKey("user_id") &&
				request.getParameterMap().containsKey("lat") &&
				request.getParameterMap().containsKey("lon")) {
			String userId = request.getParameter("user_id");
			double lat = Double.parseDouble(request.getParameter("lat"));
			double lon = Double.parseDouble(request.getParameter("lon"));
			array = connection.searchRestaurants(userId, lat, lon);
		}
		RpcParser.writeOutput(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
