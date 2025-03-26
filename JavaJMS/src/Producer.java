
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.naming.InitialContext;
///va_esb/server/source/com/wm/app/b2b/server/dispatcher/um/producer/Producer.java
public class Producer {
	private TopicConnection connection;

	/* Constructor. Establish JMS publisher and subscriber */

	public void doIt() {
		// Obtain a JNDI connection
		
		InitialContext jndi = new InitialContext(System.getProperties());

		// Look up a JMS connection factory
		ConnectionFactory conFactory = (ConnectionFactory) jndi.lookup("ConnectionFactory1");
		

		// Create a JMS connection
		Connection connection = conFactory.createConnection("miro", "miro");

		// Create a session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic t1 = session.createTopic("MyTopic");
		MessageProducer msg = session.createProducer(t1);
		
		TextMessage txt = session.createTextMessage();
		
		txt.setText("sending some test to my server");		
		msg.send(txt);
		System.out.println("Sent Message....");
	}

	/* Run the Chat client */
	public static void main(String[] args) {
		try {

			new Producer().doIt();
			Thread.currentThread().join();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}