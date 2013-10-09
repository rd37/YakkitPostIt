package util;
import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendMail {
	
	
	public SendMail(){}

	public void sendmail( String subject,String request)
	{    
		try {
			Properties propsTLS = new Properties();
			propsTLS.put("mail.transport.protocol", "smtp");
			propsTLS.put("mail.smtp.host", "smtp.gmail.com");
			propsTLS.put("mail.smtp.auth", "true");
			propsTLS.put("mail.smtp.starttls.enable", "true"); // GMail requires 
			
			Session sessionTLS = Session.getInstance(propsTLS);
			sessionTLS.setDebug(true);
			
			Message messageTLS = new MimeMessage(sessionTLS);
			//messageTLS.setFrom(new InternetAddress("ron.desmarais@gmail.com", "EC2 PostIt WebApplication"));
			messageTLS.setFrom(new InternetAddress("overseer@yakkit.com", "EC2 PostIt WebApplication"));
			messageTLS.setRecipients(Message.RecipientType.TO, InternetAddress.parse("overseer@yakkit.com")); // 
			
			messageTLS.setSubject(subject);
			messageTLS.setText(request);
			
			Transport transportTLS = sessionTLS.getTransport();
			//transportTLS.connect("smtp.gmail.com", 587, "ron.desmarais@gmail.com", "rjd@828!");
			transportTLS.connect("smtp.gmail.com", 587, "overseer@yakkit.com", "eW6gQtz3");
			
			transportTLS.sendMessage(messageTLS, messageTLS.getAllRecipients());
			transportTLS.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		// Recipient's email ID needs to be mentioned.
		String to = dest;
		
		// Sender's email ID needs to be mentioned
		String from = "admin.yakkit.com@ec2_50.com";
		
		// Assuming you are sending email from localhost
		String host = "smtp.gmail.com";
		
		// Get system properties
		Properties properties = System.getProperties();
		
		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		
		try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO,
			                          new InternetAddress(to));
			
			// Set Subject: header field
			message.setSubject(request);
			
			// Now set the actual message
			message.setText("This is actual message");
			
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
		*/
	}

}
