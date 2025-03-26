import java.util.Properties;
import java.util.logging.Logger;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

public class TopicConsumer {
	private TopicConnection connection;
	//private boolean keepRunning = true;
	//private int mCount = 0;
	private static Logger LOG;


	/* Constructor. Establish JMS publisher and subscriber */
	public void doIt(Properties prop) throws Exception {
		// Obtain a JNDI connection

		InitialContext jndi = new InitialContext();

		System.out.println("Created connection");
		// Look up a JMS connection factory
		Properties properties = new Properties();
		properties.load(this.getClass().getResourceAsStream("jndi.properties"));
		Context context = new InitialContext(properties);

		ConnectionFactory factory = (ConnectionFactory) context.lookup(System.getProperty("connectionFactory"));
		// Create a JMS connection
		Connection connection = factory.createConnection();

		// Create two JMS session objects
		System.out.println("Creating session");
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		System.out.println("Created session");

		System.out.println("Creating consumer");

		System.out.println("Setting up message Listenger");
		connection.start();
		Topic t = (Topic) jndi.lookup(System.getProperty("topic"));

		// jndi.close();
		TopicSubscriber ts = session.createDurableSubscriber(t, System.getProperty("subscriber"), "", false);


		// MessageConsumer c = session.createConsumer(t);
		// MessageConsumer c1 = session.createConsumer(t);
		// MessageConsumer c2 = session.createConsumer(t);
		System.out.println("going to process those events");
		if (true) {
			ts.setMessageListener(new MessageListener() {				

				public void onMessage(Message arg0) { // TODO Auto-generated method stub try
					
					
					try {
						if (arg0 instanceof TextMessage) 
							System.out.println("Got a text message" + ((TextMessage) arg0).getText());
						else if (arg0 instanceof BytesMessage) {
							byte[] b = new byte[(int) ((BytesMessage) arg0).getBodyLength()];
							((BytesMessage) arg0).readBytes(b);
							System.out.println("Got a byte message:" + new String(b));;
						}
						else 
							System.out.println("Got another type of message:" + arg0.getClass().getName());
							
						arg0.acknowledge();
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					/*
					 * ExecutorService executor = Executors.newSingleThreadExecutor();
					 * executor.submit(() -> readAndAck(arg0));
					 */
				}
			}); // }
		}
		// c1.setMessageListener(c.getMessageListener());

		else {

			while (true) {

				Message m = ts.receive(5000);
				/*
				 * ExecutorService executor = Executors.newSingleThreadExecutor();
				 * executor.submit(() -> readAndAck(m));
				 */
				if (m != null) {
					LOG.info("Holding a message");
					Thread.sleep(60000);
					LOG.info("ACK a message Sync");
					m.acknowledge();

				}

				/*
				 * 
				 * readAndAck(m);
				 * 
				 * if (m != null) { LOG.info("ACK a message Sync"); m.acknowledge();
				 * 
				 * }
				 */

			}
		}

	}
	/*
	 * 
	 * private void readAndAck(Message m) { // TODO Auto-generated method stub if (m
	 * != null) { // m.acknowledge(); System.out.println(m.getJMSMessageID());
	 * 
	 * LOG.info("gotta a message"); try { //
	 * Thread.currentThread().sleep(Math.round(Math.random() * 10000));
	 * Thread.currentThread().sleep(1000); } catch (InterruptedException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * mCount++; if (mCount % 3 == 0 && mCount > 0) {
	 * 
	 * LOG.info("ACK a message"); mCount = 0; try { m.acknowledge(); } catch
	 * (JMSException e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * }
	 * 
	 * }
	 * 
	 * }
	 */

	/* Close the JMS connection */
	public void close() throws JMSException {
		connection.close();
	}

	/* Run the Chat client */
	public static void main(String[] args) {
		try {
			LOG = Logger.getLogger(TopicConsumer.class.getName());
			// args[0]=topicName; args[1]=username; args[2]=password
			TopicConsumer cons = new TopicConsumer();
			cons.doIt(System.getProperties());
			Thread.currentThread().join();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onException(JMSException arg0) {
		System.out.println("Got exception:" + arg0.getMessage());

	}
}