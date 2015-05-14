package sk.rxjrest.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleCORSFilter extends WebApplicationContextOncePerRequestFilter {
	public void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

		final HttpServletResponse response = (HttpServletResponse) res;
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Headers", ""
				+ "Accept, Accept-Charset, Accept-Encoding, Accept-Language,"
				+ "Accept-Datetime, Authorization, Cache-Control, Connection, Cookie,Content-Length,"
				+ "Content-MD5, Content-Type, Date, Expect, From, Host, If-Match,"
				+ "If-Modified-Since, If-None-Match, If-Range, If-Unmodified-Since, Max-Forwards,"
				+ "Origin,Pragma,Proxy-Authorization,Range,Referer,TE,User-Agent,Upgrade,"
				+ "Via,Warning,X-Requested-With,DNT,X-Forwarded-For,X-Forwarded-Proto,Front-End-Https,"
				+ "X-Http-Method-Override,X-ATT-DeviceId,X-Wap-Profile,Proxy-Connection");
		
		chain.doFilter(req, res);
		
	}
	
}