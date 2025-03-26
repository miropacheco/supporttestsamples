package testSubscriber;

import java.util.logging.Logger;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

public class QueueConsumer {
	private TopicConnection connection;
	private boolean keepRunning = true;
	private int mCount = 0;
	private static Logger LOG;
	

	public void stopIt() {
		keepRunning = false;
	}

	/* Constructor. Establish JMS publisher and subscriber */
	public void doIt(Properties prop) throws Exception {
		// Obtain a JNDI connection

		InitialContext jndi = new InitialContext(prop);

		System.out.println("Created connection");
		// Look up a JMS connection factory
		ConnectionFactory conFactory = (ConnectionFactory) jndi.lookup(prop.getProperty("connectionFactory"));

		// Create a JMS connection
		Connection connection = conFactory.createConnection("user", "user");

		// Create two JMS session objects
		System.out.println("Creating session");
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);		
		System.out.println("Created session");

		System.out.println("Creating consumer");

		System.out.println("Setting up message Listenger");
		connection.start();
		Queue q = (Queue) jndi.lookup("MyQueue");

		// jndi.close();
		MessageConsumer ts = session.createConsumer((Destination) q);

		// MessageConsumer c = session.createConsumer(t);
		// MessageConsumer c1 = session.createConsumer(t);
		// MessageConsumer c2 = session.createConsumer(t);
		System.out.println("going to process those events");
		if (false) {
			ts.setMessageListener(new MessageListener() {

				public void onMessage(Message arg0) { // TODO Auto-generated method stub try

					ExecutorService executor = Executors.newSingleThreadExecutor();
					executor.submit(() -> readAndAck(arg0));
				}
			}); // }
		}
		// c1.setMessageListener(c.getMessageListener());

		else {

			while (true) {
				try {
			    session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			    
			    ts = session.createConsumer((Destination) q, "",false);
				Message m = ts.receive(5000);
				
				ExecutorService executor = Executors.newSingleThreadExecutor();
				executor.submit(() -> readAndAck(m));
				
				
				/*if (m != null) {
					LOG.info("Holding a message");
					Thread.sleep(60000);
					LOG.info("ACK a message Sync");					
					m.acknowledge();

				}*/

				/*
				 * 
				 * readAndAck(m);
				 * 
				 * if (m != null) { LOG.info("ACK a message Sync"); m.acknowledge();
				 * 
				 * }
				 */
				}
				catch (Exception e) {
					e.printStackTrace();
					Thread.sleep(11000);
				}

			}
		}

	}

	private void readAndAck(Message m) {
		// TODO Auto-generated method stub
		if (m != null) { // m.acknowledge(); System.out.println(m.getJMSMessageID());

			LOG.info("gotta a message");
			try {
				// Thread.currentThread().sleep(Math.round(Math.random() * 10000));
				Thread.currentThread().sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mCount++;
			if (mCount % 50 == 0 && mCount > 0) {

				LOG.info("ACK a message");
				mCount = 0;
				try {
					m.acknowledge();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			

		}

	}

	/* Close the JMS connection */
	public void close() throws JMSException {
		connection.close();
	}

	/* Run the Chat client */
	public static void main(String[] args) {
		try {
			LOG = Logger.getLogger(QueueConsumer.class.getName());
			// args[0]=topicName; args[1]=username; args[2]=password
			QueueConsumer cons = new QueueConsumer();
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