package sk.rxjrest.filter;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

public abstract class WebApplicationContextOncePerRequestFilter extends GenericOncePerRequestFilter
{
	protected String dispatcherServletName;

	protected WebApplicationContext webApplicationContext;

	protected WebApplicationContext getWebApplicationContext() {
		if (webApplicationContext == null) {
			webApplicationContext = (WebApplicationContext) getServletContext().getAttribute(FrameworkServlet.SERVLET_CONTEXT_PREFIX + dispatcherServletName);
		}
		return webApplicationContext;
	}

	public void setDispatcherServletName(String dispatcherServletName) {
		this.dispatcherServletName = dispatcherServletName;
	}

	@Override
	public void destroy() {
		super.destroy();
		webApplicationContext = null;
	}
	
}
