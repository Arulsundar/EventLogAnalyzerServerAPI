package com.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import com.adventnet.authentication.PAM;
import com.adventnet.db.api.RelationalAPI;
import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.DataSet;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.SelectQueryImpl;
import com.adventnet.ds.query.Table;
import com.google.gson.Gson;

@WebServlet("/login")
public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username, password;
		LoginModel person = new Gson().fromJson(request.getReader(), LoginModel.class);
		username = person.getUsername();
		password = person.getPassword();

		JSONObject jsonData = new JSONObject();
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();

		try {
			PAM.login(username, password, "System", request);
			RelationalAPI relapi = RelationalAPI.getInstance();
			Connection con = relapi.getConnection();
			SelectQueryImpl sq = new SelectQueryImpl(new Table("Employee"));
			Criteria c = new Criteria(new Column("Employee", "EMP_NAME"), username, QueryConstants.EQUAL);
			sq.setCriteria(c);
			sq.addSelectColumn(new Column("Employee", "ROLE"));
			System.out.println(sq.toString());

			DataSet ds = relapi.executeQuery(sq, con);

			String role = null;
			while (ds.next())
				role = (String) ds.getValue(1);
			System.out.println(role);
			session.setAttribute("role", "ReadAllTables");
			session.setAttribute("user", username);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			jsonData.put("response_code", 200);
			jsonData.put("response_message", "success");
			out.print(jsonData);

		} catch (Exception e) {
			e.printStackTrace();
			jsonData.put("response_code", 400);
			jsonData.put("response_message", "Invalid");
			out.print(jsonData);
		}
	}

}
