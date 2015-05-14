package sk.rxjrest.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
public class MainEndpoint extends BaseEndpoint {

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @RequestMapping(method = RequestMethod.GET, value = "/")
	public ModelAndView homaPage(HttpServletRequest request,  HttpServletResponse response){
		return new ModelAndView("index");
	}
    
}
