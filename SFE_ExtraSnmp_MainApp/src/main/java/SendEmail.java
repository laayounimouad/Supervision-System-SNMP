import javax.mail.*;
import javax.mail.internet.*;

import com.mysql.jdbc.Connection;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SendEmail {

   final String senderEmailID = "nissanquashqai12@gmail.com\r\n";
final String senderPassword = "azerty123.";
final String emailSMTPserver = "smtp.gmail.com";
final String emailServerPort = "465";
String receiverEmailID = null;
static String emailSubject = "Mail";
static String emailBody = ":)";
Boolean etat=true;
  public SendEmail(String Subject,
  String Body){
	  Connection conn=(Connection) DbConnection.getConnection();
		final String sql ="select email from admin where ID = 1 ";	
		try {
		  	java.sql.Statement ps = conn.createStatement();
			ResultSet rs=ps.executeQuery(sql); 
			while (rs.next())
		    receiverEmailID=rs.getString(1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			etat=false;
		}
   
  // Receiver Email Address
  this.receiverEmailID=receiverEmailID; 
  // Subject
  this.emailSubject=Subject;
  // Body
  this.emailBody=Body;
  Properties props = new Properties();
  props.put("mail.smtp.user",senderEmailID);
  props.put("mail.smtp.host", emailSMTPserver);
  props.put("mail.smtp.port", emailServerPort);
  props.put("mail.smtp.starttls.enable", "true");
  props.put("mail.smtp.auth", "true");
  props.put("mail.smtp.socketFactory.port", emailServerPort);
  props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
  props.put("mail.smtp.socketFactory.fallback", "false");
  SecurityManager security = System.getSecurityManager();
  try{  
  Authenticator auth = new SMTPAuthenticator();
  Session session = Session.getInstance(props, auth);
  MimeMessage msg = new MimeMessage(session);
  msg.setText(emailBody);
  msg.setSubject(emailSubject);
  msg.setFrom(new InternetAddress(senderEmailID));
  msg.addRecipient(Message.RecipientType.TO,
  new InternetAddress(receiverEmailID));
  Transport.send(msg);
 }
  
  catch (Exception mex){
	  StringWriter sw = new StringWriter();
      mex.printStackTrace(new PrintWriter(sw));
      ErorFile er=new ErorFile("error_mail",sw.toString());
  etat=false;
  }
  
  
  }
  public class SMTPAuthenticator extends javax.mail.Authenticator
  {
  public PasswordAuthentication getPasswordAuthentication()
  {
  return new PasswordAuthentication(senderEmailID, senderPassword);
  }
  }
     
}