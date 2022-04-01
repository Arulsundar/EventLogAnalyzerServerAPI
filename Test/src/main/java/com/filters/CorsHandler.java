package com.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("*")
public class CorsHandler implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String clientOrigin = request.getHeader("origin");

		if (clientOrigin != null && clientOrigin.startsWith("http://localhost:")) {
			response.setHeader("Access-Control-Allow-Origin", clientOrigin);
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
			response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		    response.setDateHeader("Expires", 0);
		}

		filterChain.doFilter(req, resp);
	}
}
