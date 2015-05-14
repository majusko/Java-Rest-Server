package sk.rxjrest.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.OncePerRequestFilter;

import sk.rxjrest.filter.util.RequestUtils;

public abstract class GenericOncePerRequestFilter extends OncePerRequestFilter
{
	public static final List<String> DEFAULT_SUFFIXS = new ArrayList<String>()
		{
			private static final long serialVersionUID = 1L;
			{
				add(".js");
				add(".png");
				add(".jpg");
				add(".gif");
				add(".css");
			}
		};

	private List<String> suffixs = new ArrayList<String>();
	private List<String> excludedPatterns = new ArrayList<String>();

	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
	{
		return RequestUtils.endWith(request, getSuffixs()) || RequestUtils.matchUrl(request, getExcludedPatterns());
	}

	public List<String> getSuffixs()
	{
		return suffixs;
	}

	public void setSuffixs(List<String> suffixs)
	{
		this.suffixs = suffixs;
	}

	public List<String> getExcludedPatterns()
	{
		return excludedPatterns;
	}

	public void setExcludedPatterns(List<String> patterns)
	{
		this.excludedPatterns = patterns;
	}

	public void setExcludedPatternsParam(String patterns)
	{
		excludedPatterns.addAll(Arrays.asList(patterns.split(",")));
	}
}