
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	public static void main(String[] args) {
		final String fromEmail = "claudemiro.santos70"; // requires valid  id
		final String password = "l3tmeG0!"; // correct password for id
		
		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host . I tested with gmail
		props.put("mail.smtp.port", "587"); // TLS P	ort
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS
		props.putAll(System.getProperties());

		// create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);

		try {

			Message message = new MimeMessage(session);
			// change it here to match your address
			message.setFrom(new InternetAddress("claudemiro.santos70@gmail.com"));
			// send to whoever you want. Try your own address to make it easer.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("claudemirop@gmail.com"));
			message.setSubject("Testing TLS");
			message.setText("Dear Mail Crawler," + "\n\n Please do not spam my email!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}
}