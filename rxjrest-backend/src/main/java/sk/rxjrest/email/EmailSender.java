package sk.rxjrest.email;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailSender {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	public void sendMail(EmailInfo info, Context ctx) throws AddressException, MessagingException {
		
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
	    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart

	    message.setSubject(info.getSubject());
	    message.setFrom(info.getFrom());
	    message.setTo(info.getTo());

	    String htmlContent = null;
	    
	    if (info.getPreferredLanguage() != null) {
	    	
	    	try {
	    		
	    		htmlContent = templateEngine.process(info.getTemplate()+ "_" + info.getPreferredLanguage() + ".html", ctx);

		    } catch (Exception ex) {
		    	
		    	htmlContent = templateEngine.process(info.getTemplate() + ".html", ctx);
		    	
		    }
	    	
	    } else {
	    	
	    	htmlContent = templateEngine.process(info.getTemplate() + ".html", ctx);
	    	
	    }

	    // if you have some replacements in email - for example registration code in link, or UTM source for concrete user
	    if(info.getReplacements() != null){
		    for (Map.Entry<String, String> entry : info.getReplacements().entrySet()) {
		    	
		        final String oldString = entry.getKey();
		        final String newString = entry.getValue();
		        
		        htmlContent = htmlContent.replace(oldString, newString);
		        
		        System.out.println("some shit");
		        
		    }
	    }
	    
	    // Create the HTML body using Thymeleaf
	    message.setText(htmlContent, true); // true = isHtml

	    // Send mail
	    mimeMessage.setSender(new InternetAddress(info.getFrom()));
	    this.mailSender.send(mimeMessage);
	    
	}


}
