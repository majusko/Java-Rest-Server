package sk.eglu.appsupport.web.jetty;

import java.io.IOException;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * Class to start web portal on developers machine
 */
public class RXJRestStart {

    RXJRestStart(){
        super();
    }

    /**
     * Main function, starts the jetty server.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

		try {
			
			final Resource jettyXml = Resource.newSystemResource("jetty.xml");
			final XmlConfiguration configuration = new XmlConfiguration(jettyXml.getInputStream());
		    // Configuration of non-default port
		    final SelectChannelConnector connector = new SelectChannelConnector();
		    // Add our web application
		    final WebAppContext application = new WebAppContext();
			final Server server = (Server)configuration.configure();
			
		    connector.setPort(8096);
		    connector.setMaxIdleTime(300000); 
		
		    application.setContextPath("/");
		    application.setWar("src/main/webapp"); // WEB-INF directory instead war
		    application.setDefaultsDescriptor("src/test/resources/webdefault.xml");
		    application.setDescriptor("src/main/webapp/WEB-INF/web.xml");
	
		    server.setConnectors(new Connector[] {connector});
	        server.setHandler(application);
	        server.start();
	        server.join();
	        
	    }  catch (Exception e) {
	        e.printStackTrace();
	        System.exit(100);
	    }
        
    }
}
