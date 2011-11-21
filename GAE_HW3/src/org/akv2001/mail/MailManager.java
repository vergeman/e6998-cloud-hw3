package org.akv2001.mail;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.akv2001.datastore.Woeid;

public class MailManager {
	String msgBody;
	String _from;
	String _from_name;
	String _to;
	String _to_name;
	String _subject;
	Message msg;

	public MailManager(String from, String from_name, String to,
			String to_name, String subject) {
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		msg = new MimeMessage(session);
		
		_from = from;
		_from_name = from_name;

		_to = to;
		_to_name = to_name;

		_subject = subject;
	}
	
	public void BuildMessage(List<Woeid> coldest, List<Woeid> hottest,  List<Woeid> all) {
		StringBuilder sb = new StringBuilder("Your Maximum and Minimum Temperatures\n\n");
		
		sb.append("--Coldest Areas--\n");
		for (Woeid w : coldest)  {
			build(sb, w);
		}
		
		sb.append("\n--Hottest Areas--\n");
		for (Woeid w : hottest) {
			build(sb, w);
		}
		
		sb.append("\n\n\n--All results--\n");
		for (Woeid w : all) {
			build(sb, w);
		}
		
		msgBody = sb.toString();
		
	}

	private void build(StringBuilder sb, Woeid w) {
		sb.append(w.getCity());
		sb.append(", ");
		sb.append(w.getRegion());
		sb.append(".  \t\t");
		sb.append(w.getTemp());
		sb.append("\n");
	}
	
	
	public void PrintMessage() {
		System.out.println(msgBody);
	}
	
	
	public void Send() {

		try {

			msg.setFrom(new InternetAddress(_from, _from_name));
			msg.addRecipient(Message.RecipientType.TO, 
					new InternetAddress(_to, _to_name));
			msg.setSubject(_subject);
			msg.setText(msgBody);
			Transport.send(msg);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
