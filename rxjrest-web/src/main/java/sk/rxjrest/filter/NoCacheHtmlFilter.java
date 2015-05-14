package sk.rxjrest.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Filter that prevents browser from caching filtered pages/resources
 * Caching is not desired, because with using Ajax, site changes after it is loaded and after using back button user does not get the site in state he expects
 * @author kmetkojan
 *
 */
public class NoCacheHtmlFilter extends WebApplicationContextOncePerRequestFilter
{
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
	{

		String[] fragments = request.getRequestURI().split("/");
		if (fragments.length < 3 || (!fragments[2].equals("scripts") && !fragments[2].equals("styles") && !fragments[2].equals("img"))) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
			httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0
			httpResponse.setDateHeader("Expires", 0); // Proxies.
		}
		chain.doFilter(request, response);
	}



}