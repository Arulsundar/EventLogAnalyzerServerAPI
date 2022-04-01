package com.signup;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import com.adventnet.authentication.AAAACCOUNT;
import com.adventnet.authentication.AAAACCPASSWORD;
import com.adventnet.authentication.AAAAUTHORIZEDROLE;
import com.adventnet.authentication.AAALOGIN;
import com.adventnet.authentication.AAAPASSWORD;
import com.adventnet.authentication.AAAUSER;
import com.adventnet.authentication.PasswordException;
import com.adventnet.authentication.util.AuthUtil;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;
import com.adventnet.persistence.WritableDataObject;
import com.google.gson.Gson;

@WebServlet("/signin")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String loginName;
		String firstName;
		String lastName;
		String password;
		SigninModel person = new Gson().fromJson(request.getReader(), SigninModel.class);
		firstName = person.getFirstName();
		lastName = person.getLastName();
		loginName = person.getLoginName();
		password = person.getPassword();

		JSONObject jsonData = new JSONObject();
		PrintWriter out = response.getWriter();
		try {
			Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
			DataObject dobj = persistence.constructDataObject();
			Row userRow = new Row(AAAUSER.TABLE);
			userRow.set(AAAUSER.FIRST_NAME, firstName);
			userRow.set(AAAUSER.LAST_NAME, lastName); // optional
			dobj.addRow(userRow);

			Row loginRow = new Row(AAALOGIN.TABLE);
			loginRow.set(AAALOGIN.NAME, loginName);
			dobj.addRow(loginRow);

			Row accRow = new Row(AAAACCOUNT.TABLE);
			accRow.set(AAAACCOUNT.SERVICE_ID, AuthUtil.getServiceId("System"));
			accRow.set(AAAACCOUNT.ACCOUNTPROFILE_ID, AuthUtil.getAccountProfileId("Profile 1"));
			dobj.addRow(accRow);

			Row passwordRow = new Row(AAAPASSWORD.TABLE);
			passwordRow.set(AAAPASSWORD.PASSWORD, password);
			passwordRow.set(AAAPASSWORD.PASSWDPROFILE_ID, AuthUtil.getPasswordProfileId("Profile 1"));
			dobj.addRow(passwordRow);

			Row accPassRow = new Row(AAAACCPASSWORD.TABLE);
			accPassRow.set(AAAACCPASSWORD.ACCOUNT_ID, accRow.get(AAAACCOUNT.ACCOUNT_ID));
			accPassRow.set(AAAACCPASSWORD.PASSWORD_ID, passwordRow.get(AAAPASSWORD.PASSWORD_ID));
			dobj.addRow(accPassRow);

			Row authRoleRow1 = new Row(AAAAUTHORIZEDROLE.TABLE);
			authRoleRow1.set(AAAAUTHORIZEDROLE.ACCOUNT_ID, accRow.get(AAAACCOUNT.ACCOUNT_ID));
			authRoleRow1.set(AAAAUTHORIZEDROLE.ROLE_ID, AuthUtil.getRoleId("ReadAllTables"));
			dobj.addRow(authRoleRow1);
			AuthUtil.createUserAccount(dobj);

			Row r = new Row("Employee");
			r.set("EMP_NAME", loginName);
			r.set("ROLE", "ReadAllTables");
			DataObject d = new WritableDataObject();
			d.addRow(r);
			DataAccess.add(d);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			jsonData.put("response_code", 200);
			jsonData.put("response_message", "success");
			out.print(jsonData);

		} catch (DataAccessException e) {
			e.printStackTrace();
			jsonData.put("response_message", "DataAccessException");
			out.print(jsonData);

		} catch (PasswordException e) {
			jsonData.put("response_message", "PasswordException");
			out.print(jsonData);
		} catch (Exception e) {
			jsonData.put("response_message", "invalid");
			out.print(jsonData);
		}

	}

}
