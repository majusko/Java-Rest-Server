package sk.rxjrest.filter.util;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;



/**
 * Specialna util trieda, ktora obsahuje pomocne metody na ziskanie path, zistenie o aky typ requestu ide...
 *
 * @author kurtak
 *
 */
public abstract class RequestUtils
{
	/* Specialne atributy, ktore ked su nastavene kasle sa na na klasicke forward atributy podla specifikacie (potrebujeme to kvoli multiple forwardom) */
	public static final String ORIGINAL_REQUEST_URI_ATTRIBUTE = "sk.igaraz.original.request_uri";
	public static final String ORIGINAL_CONTEXT_PATH_ATTRIBUTE = "sk.igaraz.original.context_path";
	public static final String ORIGINAL_SERVLET_PATH_ATTRIBUTE = "sk.igaraz.original.servlet_path";
	public static final String ORIGINAL_PATH_INFO_ATTRIBUTE = "sk.igaraz.original.path_info";
	public static final String ORIGINAL_QUERY_STRING_ATTRIBUTE = "sk.igaraz.original.query_string";

	private RequestUtils()
	{
	}

	/**
	 * @return true, ak bol request dispatchovany cez RequestDispatcher.include
	 */
	public static boolean isIncludeRequest(ServletRequest request)
	{
		return (request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE) != null);
	}

	public static String getOriginatingRelativePath(HttpServletRequest request)
	{
		String queryString = getOriginatingQueryString(request);

		if(StringUtils.hasLength(queryString))
		{
			return getOriginatingPathInfo(request) + "?" + queryString;
		}

		return getOriginatingPathInfo(request);
	}

	/** Metoda natvrdo nastavi aktualny request ako originalny. */
	public static void forceOriginalRequest(ServletRequest request)
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		request.setAttribute(ORIGINAL_REQUEST_URI_ATTRIBUTE, httpRequest.getRequestURI());
		request.setAttribute(ORIGINAL_CONTEXT_PATH_ATTRIBUTE, httpRequest.getContextPath());
		request.setAttribute(ORIGINAL_SERVLET_PATH_ATTRIBUTE, httpRequest.getServletPath());
		request.setAttribute(ORIGINAL_PATH_INFO_ATTRIBUTE, httpRequest.getPathInfo());
		request.setAttribute(ORIGINAL_QUERY_STRING_ATTRIBUTE, httpRequest.getQueryString());
	}

	/**
	 * @return true, ak bol request dispatchovany cez RequestDispatcher.forward
	 */
	public static boolean isForwardRequest(ServletRequest request)
	{
		return (request.getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE) != null) ;
	}

	/**
	 * @return true, ak je forward vynuteny
	 */
	public static boolean isForcedOriginalRequest(ServletRequest request)
	{
		return request.getAttribute(RequestUtils.ORIGINAL_REQUEST_URI_ATTRIBUTE) != null;
	}

	public static String getOriginatingRequestUri(HttpServletRequest request)
	{
		if(isForcedOriginalRequest(request))
		{
			return (String) request.getAttribute(RequestUtils.ORIGINAL_REQUEST_URI_ATTRIBUTE);
		}
		if (isForwardRequest(request))
		{
			return (String) request.getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE);
		}
		else
		{
			return request.getRequestURI();
		}
	}

	/**
	 * Metoda zistuje ci bol request forwardovany a ak bol, povodnu servlet path vytiahne z parametra. V pripade
	 * includovaneho requestu je povodna servlet path priamo v metode getServletPath().
	 */
	public static String getOriginatingServletPath(HttpServletRequest request)
	{
		if(isForcedOriginalRequest(request))
		{
			return (String) request.getAttribute(RequestUtils.ORIGINAL_SERVLET_PATH_ATTRIBUTE);
		}
		if (isForwardRequest(request))
		{
			return (String) request.getAttribute(WebUtils.FORWARD_SERVLET_PATH_ATTRIBUTE);
		}
		else
		{
			return request.getServletPath();
		}
	}

	/**
	 * Metoda zistuje ci bol request forwardovany a ak bol, povodnu servlet path vytiahne z parametra. V pripade
	 * includovaneho requestu je povodna servlet path priamo v metode getContextPath().
	 */
	public static String getOriginatingContextPath(HttpServletRequest request)
	{
		if(isForcedOriginalRequest(request))
		{
			return (String) request.getAttribute(RequestUtils.ORIGINAL_CONTEXT_PATH_ATTRIBUTE);
		}
		if (isForwardRequest(request))
		{
			return (String) request.getAttribute(WebUtils.FORWARD_CONTEXT_PATH_ATTRIBUTE);
		}
		else
		{
			return request.getContextPath();
		}
	}

	public static String getOriginatingPathInfo(HttpServletRequest request)
	{
		if(isForcedOriginalRequest(request))
		{
			return (String) request.getAttribute(RequestUtils.ORIGINAL_PATH_INFO_ATTRIBUTE);
		}
		if (isForwardRequest(request))
		{
			return (String) request.getAttribute(WebUtils.FORWARD_PATH_INFO_ATTRIBUTE);
		}
		else
		{
			return request.getPathInfo();
		}
	}

	public static String getOriginatingQueryString(HttpServletRequest request)
	{
		if(isForcedOriginalRequest(request))
		{
			return (String) request.getAttribute(RequestUtils.ORIGINAL_QUERY_STRING_ATTRIBUTE);
		}
		if (isForwardRequest(request))
		{
			return (String) request.getAttribute(WebUtils.FORWARD_QUERY_STRING_ATTRIBUTE);
		}
		else
		{
			return request.getQueryString();
		}
	}

	/**
	 * Path bez query stringu.
	 */
	public static String getOriginatingPath(HttpServletRequest request)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(RequestUtils.getOriginatingContextPath(request));
		builder.append(RequestUtils.getOriginatingServletPath(request));
		builder.append(RequestUtils.getOriginatingPathInfo(request));

		return builder.toString();
	}

	/**
	 * Path s query stringov.
	 */
	public static String getOriginatingFullPath(HttpServletRequest request)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(RequestUtils.getOriginatingContextPath(request));
		builder.append(RequestUtils.getOriginatingServletPath(request));
		builder.append(RequestUtils.getOriginatingPathInfo(request));

		String queryString = getOriginatingQueryString(request);

		if(StringUtils.hasLength(queryString))
		{
			builder.append("?").append(queryString);
		}

		return builder.toString();
	}

	/** Vrati aktualny path s tym, ze bude obsahovat zoznam parametrov. Ak v parametroch uz tieto parametre su, nahradia sa novymi. */
	public static String getCurrentPathWithParams(HttpServletRequest request, String[]... params)
	{
		String requestPath = getOriginatingPathInfo(request);
		String queryString = getOriginatingQueryString(request);

		if(StringUtils.hasLength(queryString))
		{
			requestPath = requestPath + "?" + queryString;
		}

		return getPathWithParams(requestPath, request, true, params);
	}

	public static String getPathWithParams(String path, HttpServletRequest request, boolean useServletPath, String[]... params)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(RequestUtils.getOriginatingContextPath(request));
		if (useServletPath)
		{
			builder.append(RequestUtils.getOriginatingServletPath(request));
		}

		String queryString = null;

		int queryIndex = path.indexOf('?');
		if (queryIndex != -1)
		{
			queryString = path.substring(queryIndex + 1);
			builder.append(path.substring(0, queryIndex));
		}
		else
		{
			builder.append(path);
		}

		for (String[] paramWithValue : params)
		{
			queryString = updateQueryStringWithParam(queryString, paramWithValue[0], paramWithValue[1]);
		}

		builder.append("?");
		builder.append(queryString);

		return builder.toString();
	}

	public static String getPathWithParams(String path, String[]... params)
	{
		StringBuilder builder = new StringBuilder();

		String queryString = null;

		int queryIndex = path.indexOf('?');
		if (queryIndex != -1)
		{
			queryString = path.substring(queryIndex + 1);
			builder.append(path.substring(0, queryIndex));
		}
		else
		{
			builder.append(path);
		}

		for (String[] paramWithValue : params)
		{
			queryString = updateQueryStringWithParam(queryString, paramWithValue[0], paramWithValue[1]);
		}

		builder.append("?");
		builder.append(queryString);

		return builder.toString();
	}

	/** Vrati aktualnu path bez parametrov. */
	public static String getCurrentPathWithoutParams(HttpServletRequest request, String... params)
	{
		String requestPath = getOriginatingPathInfo(request);
		String queryString = getOriginatingQueryString(request);

		if(StringUtils.hasLength(queryString))
		{
			requestPath = requestPath + "?" + queryString;
		}

		return getPathWithoutParams(requestPath, request, true, params);
	}

	/** Vrati celu path (aj kontextovu aj servlet) bez parametrov. */
	public static String getPathWithoutParams(String path, HttpServletRequest request, boolean useServletPath, String... params)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(RequestUtils.getOriginatingContextPath(request));
		if (useServletPath)
		{
			builder.append(RequestUtils.getOriginatingServletPath(request));
		}

		String queryString = null;

		int queryIndex = path.indexOf('?');
		if (queryIndex != -1)
		{
			queryString = path.substring(queryIndex + 1);
			builder.append(path.substring(0, queryIndex));
		}
		else
		{
			builder.append(path);
		}

		for (String param : params)
		{
			queryString = removeParamFromQueryString(queryString, param);
		}

		builder.append("?");
		builder.append(queryString);

		return builder.toString();
	}

	public static String getPathWithoutParams(String path, String... params)
	{
		StringBuilder builder = new StringBuilder();

		String queryString = null;

		int queryIndex = path.indexOf('?');
		if (queryIndex != -1)
		{
			queryString = path.substring(queryIndex + 1);
			builder.append(path.substring(0, queryIndex));
		}
		else
		{
			builder.append(path);
		}

		for (String param : params)
		{
			queryString = removeParamFromQueryString(queryString, param);
		}

		if(StringUtils.hasLength(queryString))
		{
			builder.append("?");
			builder.append(queryString);
		}

		return builder.toString();
	}

	/** V query stringu aktualizuje hodnotu parametra. */
	public static String updateQueryStringWithParam(String queryString, String param, String value)
	{
		StringBuilder builder = new StringBuilder();

		if(StringUtils.hasLength(queryString))
		{
			int startIndex = queryString.indexOf(param);

			if(startIndex != -1)
			{
				builder.append(queryString.substring(0, startIndex));
				builder.append(param).append('=').append(value);

				int endIndex = queryString.indexOf('&', startIndex);

				if(endIndex != -1)
				{
					builder.append(queryString.substring(endIndex, queryString.length()));
				}
			}
			else
			{
				builder.append(queryString).append('&').append(param).append('=').append(value);
			}
		} else
		{
			builder.append(param).append('=').append(value);
		}

		return builder.toString();
	}

	/** Z query stringu odstrani parameter */
	public static String removeParamFromQueryString(String queryString, String param)
	{
		if (StringUtils.hasLength(queryString))
		{
			int startIndex = queryString.indexOf(param);

			if (startIndex != -1)
			{
				StringBuilder builder = new StringBuilder();

				if(startIndex > 0)
				{
					// Nezoberieme predchadzajuce &
					builder.append(queryString.substring(0, startIndex - 1));
				}

				int endIndex = queryString.indexOf('&', startIndex);

				if (endIndex != -1)
				{
					// Ak sme na zaciatku nepotrebujeme &
					if (startIndex == 0)
					{
						endIndex++;
					}
					builder.append(queryString.substring(endIndex, queryString.length()));
				}

				return builder.toString();
			}
		}

		return null;
	}

	/** Metoda testuje ci request konci jednym zo suffixov. */
	public static boolean endWith(HttpServletRequest request, List<String> suffixs)
	{
		String URI = request.getRequestURI();
		boolean endWith = false;
		Iterator<String> iterator = suffixs.iterator();
		while (iterator.hasNext())
		{
			String suffixTemp = iterator.next();
			endWith |= URI.endsWith(suffixTemp);
			if (endWith)
			{
				return endWith;
			}
		}
		return endWith;
	}

	public static boolean matchUrl(HttpServletRequest request, List<String> patterns)
	{
		if (patterns != null)
		{
			String URI = request.getRequestURI();
			boolean endWith = false;
			Iterator<String> iterator = patterns.iterator();
			while (iterator.hasNext())
			{
				String patternTemp = iterator.next();
				endWith |= URI.matches(patternTemp);
				if (endWith)
				{
					return endWith;
				}
			}
			return endWith;

		}

		return false;
	}


}