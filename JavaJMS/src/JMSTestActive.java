

import java.util.Properties;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
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

public class JMSTestActive {
	private TopicConnection connection;

	/* Constructor. Establish JMS publisher and subscriber */
	public void doIt(Properties prop) throws Exception {
		// Obtain a JNDI connection

		InitialContext jndi = new InitialContext(); 

		System.out.println("Created connection");
		// Look up a JMS connection factory
		ConnectionFactory conFactory = (ConnectionFactory) jndi.lookup("local_um");

		// Create a JMS connection
		Connection connection = conFactory.createConnection();

		// Create two JMS session objects
		System.out.println("Creating session");
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		System.out.println("Created session");

		System.out.println("Creating consumer");

		System.out.println("Setting up message Listenger");
		connection.start();
//		Topic t = (Topic) jndi.lookup("testJMSTopic");	
		//Queue t = (Topic) jndi.lookup(prop.getProperty("topic"));						

		//TopicSubscriber ts = session.createDurableSubscriber(t, "Subscriber", "", false);
		Queue q = (Queue) jndi.lookup("mirotest");
		MessageConsumer mc = session.createConsumer(q);
		mc.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message arg0) {
				// TODO Auto-generated method stub
				try {
					if (arg0 instanceof TextMessage) 
						System.out.println("Got a text message" + ((TextMessage) arg0).getText());
					else if (arg0 instanceof BytesMessage) {
						byte[] b = new byte[(int) ((BytesMessage) arg0).getBodyLength()];
						((BytesMessage) arg0).readBytes(b);
						System.out.println("Got a byte message:" + new String(b) + " message id" + arg0.getJMSMessageID());;
					}
					else 
						System.out.println("Got another type of message:" + arg0.getClass().getName());
						
					arg0.acknowledge();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
			}
		});
		
		/*
		

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

				
				}
			}); // }
		}*/
		

	}

	/* Close the JMS connection */
	public void close() throws JMSException {
		connection.close();
	}

	/* Run the Chat client */
	public static void main(String[] args) {
		try {
		
			JMSTestActive cons = new JMSTestActive();
			
	
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