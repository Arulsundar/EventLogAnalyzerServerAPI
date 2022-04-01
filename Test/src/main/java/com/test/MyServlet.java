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
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.simple.JSONObject;

import com.google.gson.Gson;

@WebServlet("/logs")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Object user;
	Object role;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		role = req.getSession().getAttribute("role");
		user = req.getSession().getAttribute("user");
		int from=Integer.valueOf(req.getParameter("pg"));
		System.out.println(user + "," + role+","+from);
		int val=(from-1)*100;
		HttpSession session = req.getSession();
		PrintWriter out = resp.getWriter();
		if (session.getAttribute("role").equals(role)) {

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
					.query(QueryBuilders.matchQuery("User", user))
					.sort(new FieldSortBuilder("@timestamp").order(SortOrder.DESC))
					.from(val).size(100)
					.timeout(new TimeValue(10, TimeUnit.SECONDS));

			SearchRequest searchRequest = new SearchRequest("winlogs").source(searchSourceBuilder);

			RestHighLevelClient client = new RestHighLevelClient(
					RestClient.builder(new HttpHost("localhost", 9200, "http")));
			Map<String, Object> map = new HashMap<>();
			Map<String,Object> jsonData = new HashMap<>();
			List<String> log = new ArrayList<String>();
			Gson gson = new Gson();
			JSONObject json = new JSONObject();
			
			try {
				SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
				if (searchResponse.getHits().getTotalHits().value > 0) {
					SearchHit[] searchHit = searchResponse.getHits().getHits();
					for (SearchHit hit : searchHit) {
						map = hit.getSourceAsMap();
						jsonData.put("Time", map.get("@timestamp"));
						jsonData.put("EventId", map.get("EventId"));
						jsonData.put("Type", map.get("Type"));
						jsonData.put("Source", map.get("source"));
						String data=gson.toJson(jsonData);
						log.add(data);
					}
					
					out.print(log);
				}
				else {
					
					json.put("response_message", "No Data");
					out.print(json);
				}
				client.close();
				
				out.flush();
			}

			catch (Exception e) {
				json.put("response_message", "ERROR");
				out.print(json);
			}

		} 

	}


}
