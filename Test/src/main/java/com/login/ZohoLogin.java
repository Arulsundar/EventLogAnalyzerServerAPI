package com.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

@WebServlet("/zohologin")
public class ZohoLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         
		 String code = request.getParameter("code");
	        String grantType = "authorization_code";
			String clientId = "1000.P4U2QRQOI4MXGJESITQJQYTTC1RQ3M";
			String clientSecret = "47142fe25754afc674c732b00af013ddec7dd06616";
//	        String clientId = "1000.Q5IQQXHSS4FS4NR0UEB73MC6FDWPFL";
//			String clientSecret = "74bb32e624d54c0ae2a411d7776e8b5afe27a78a25";
	        String redirectUrl = "http://localhost:8080/Test/zohologin";

	        List<NameValuePair> parameters = new ArrayList<>(Arrays.asList(
			    new BasicNameValuePair("client_id", clientId),
			    new BasicNameValuePair("client_secret", clientSecret),
			    new BasicNameValuePair("grant_type", grantType),
	            new BasicNameValuePair("code", code),
	            new BasicNameValuePair("redirect_uri", redirectUrl)
	        ));

			UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameters);

			HttpPost post = new HttpPost("https://accounts.localzoho.com/oauth/v2/token");
			post.setEntity(form);

	        CloseableHttpClient client = HttpClients.createDefault();
	        CloseableHttpResponse response1 = client.execute(post);
	        InputStream is = response1.getEntity().getContent();
	        Scanner scanner = new Scanner(is).useDelimiter("\\A");
	        String resp = scanner.hasNext() ? scanner.nextLine() : "Error";
	        String[] res=resp.split(",");
	        String[] res1=res[0].split(":");
	        String val=res1[1].replaceAll("\"", "");
	        System.out.println(val);
	        scanner.close();
	        
	        HttpGet httpget = new HttpGet("https://accounts.localzoho.com/oauth/user/info");
	        httpget.setHeader("Authorization","Bearer "+val);
	        CloseableHttpClient client1 = HttpClients.createDefault();
	        CloseableHttpResponse response2 = client1.execute(httpget);
	        InputStream is1 = response2.getEntity().getContent();
	        Scanner sc = new Scanner(is1).useDelimiter("\\A");
	        String resp1 = sc.hasNext() ? sc.nextLine() : "Error";
	        String[] names=resp1.split(",");
	        String[] name=names[0].split(":");
	        String val1=name[1].replaceAll("\"", "");
	        System.out.println(val1);
	        sc.close();
	        
	        HttpSession session=request.getSession();
	        session.setAttribute("user", val1);
	        session.setAttribute("role", "ReadAllTables");
	        System.out.println(session.getAttribute("name")+","+session.getAttribute("role"));
	        
	        response.sendRedirect("http://localhost:4200/dash");
	        
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
