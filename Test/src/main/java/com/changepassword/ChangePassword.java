package com.changepassword;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.adventnet.authentication.PasswordException;
import com.adventnet.authentication.util.AuthUtil;
import com.google.gson.Gson;

@WebServlet("/changepassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String loginName, currentPassword, newPassword;
		ChangePasswordModel changePasswordModel = new Gson().fromJson(request.getReader(), ChangePasswordModel.class);
		loginName = changePasswordModel.getLoginName();
		currentPassword = changePasswordModel.getCurrentPassword();
		newPassword = changePasswordModel.getNewPassword();

		JSONObject jsonData = new JSONObject();
		PrintWriter out = response.getWriter();

		try {
			AuthUtil.changePassword(loginName, "System", currentPassword, newPassword);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			jsonData.put("response_code", 200);
			jsonData.put("response_message", "success");
			out.print(jsonData);
		} catch (PasswordException e) {
			jsonData.put("response_code", 400);
			jsonData.put("response_message", "invalid");
			out.print(jsonData);
		}

	}

}
