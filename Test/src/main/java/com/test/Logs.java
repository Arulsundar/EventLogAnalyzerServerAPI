package com.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/list")
public class Logs extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Object user;
	Object role;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		role = req.getSession().getAttribute("role");
		user = req.getSession().getAttribute("user");
		System.out.println(user + "," + role);
		HttpSession session = req.getSession();
		PrintWriter out = resp.getWriter();
		if (session.getAttribute("role").equals(role)) {
			resp.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
			resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		    resp.setIntHeader("Expires", 0);

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
					.query(QueryBuilders.termQuery("User", user)).from(0).size(1000)
					.timeout(new TimeValue(10, TimeUnit.SECONDS));

			SearchRequest searchRequest = new SearchRequest("winlogs").source(searchSourceBuilder);

			RestHighLevelClient client = new RestHighLevelClient(
					RestClient.builder(new HttpHost("localhost", 9200, "http")));
			Map<String, Object> map = new HashMap<>();
			Gson gson = new Gson();
			String json;
			List<String> log = new ArrayList<String>();

			try {
				SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
				if (searchResponse.getHits().getTotalHits().value > 0) {
					SearchHit[] searchHit = searchResponse.getHits().getHits();
					for (SearchHit hit : searchHit) {
						map = hit.getSourceAsMap();
						json = gson.toJson(map);
						log.add(json);
					}
				}
				else {
					JSONObject jsonData = new JSONObject();
					jsonData.put("response_message", "No Data");
					out.print(jsonData);
				}
				client.close();
				out.print(log);
				out.flush();
			}

			catch (Exception e) {
				JSONObject jsonData = new JSONObject();
				jsonData.put("response_message", "ERROR");
				out.print(jsonData);
			}

		} 

	}

}
