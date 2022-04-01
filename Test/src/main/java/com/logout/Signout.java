package com.logout;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import javax.servlet.http.HttpSession;

@WebServlet("/signout")
public class Signout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		session.invalidate();
		JSONObject jsonData = new JSONObject();
		PrintWriter out = response.getWriter();
		jsonData.put("response_code", 200);
		jsonData.put("response_message", "success");
		out.print(jsonData);
		out.flush();
	}

}
