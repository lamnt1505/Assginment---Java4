package edu.poly.common;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.poly.domain.Email;

public class EmailUtils {
	public static void send(Email email) throws Exception {
		Properties prop = new Properties();//khai bao ppti
		
//		prop.put("mail.stmp.host", "smtp.gmail.com");
//		prop.put("mail.smtp.port", "587");
//		prop.put("mail.smtp.auth", "true");
//		prop.put("mail.smtp.starttls.enable", "true");
		
		
		 Properties props = new Properties();//thog so cau hinh    
         props.put("mail.smtp.host", "smtp.gmail.com");//thog so cau hinh       
         props.put("mail.smtp.socketFactory.port", "465");   
         props.put("mail.smtp.socketFactory.class",    
                   "javax.net.ssl.SSLSocketFactory");    
         props.put("mail.smtp.auth", "true");    
         props.put("mail.smtp.port", "465");    
		
		Session session = Session.getInstance(props, new Authenticator(){//doi tuong chua cac ts cau hinh
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email.getFrom(), email.getFromPassword());
			}
		});
		try {
			Message message = new MimeMessage(session);//tao ra dt message
			
			message.setFrom(new InternetAddress(email.getFrom()));//tl cac ts
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
			message.setSubject(email.getSubject());//tl tieu de mail
			message.setContent(email.getContent(),"text/html; charset=utf-8");//tl nd maik
			
			Transport.send(message);//goi thuc hien pt send
		}catch(Exception e ) {
			e.printStackTrace();//in ra tb loi 
			
			throw e;
		}
	}
}
