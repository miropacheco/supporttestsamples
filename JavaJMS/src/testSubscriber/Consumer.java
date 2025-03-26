package testSubscriber;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TopicConnection;
import javax.naming.InitialContext;



public class Consumer{
    private TopicConnection connection;
    private boolean keepRunning = true;
    public void stopIt() {
    	keepRunning = false;
    }


    /* Constructor. Establish JMS publisher and subscriber */    
    public void doIt(@SuppressWarnings("deprecation") Properties prop)
    throws Exception {
        // Obtain a JNDI connection
        

        InitialContext jndi = new InitialContext();
        
        System.out.println("Created connection");
        // Look up a JMS connection factory
        ConnectionFactory conFactory =
        (ConnectionFactory)jndi.lookup("ConnectionFactory1");

        // Create a JMS connection
        Connection connection =
        conFactory.createConnection("miro","miro");

        // Create two JMS session objects
        System.out.println("Creating session");
        Session session =
        connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        System.out.println("Created session");
        
        System.out.println("Creating consumer");        

        System.out.println("Setting up message Listenger");
        connection.start();
        Queue q = (Queue) jndi.lookup("MyQueue");
        MessageConsumer queueConsumer = session.createConsumer(q);
        System.in.read();
       /*
        
        queueConsumer.setMessageListener(new MessageListener() {
			
			public void onMessage(Message arg0) {
				// TODO Auto-generated method stub
				try {
					System.out.println(arg0.getJMSMessageID());
					arg0.acknowledge();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});*/        
        while (true) {
        	
        	Message m = queueConsumer.receive(600000);
        	if (m !=null) {
        		//m.acknowledge();
        		System.out.println(m.getJMSMessageID());
        	}
        	else {
        		System.out.println("leaving this think");
        		break;
        	}
        	
        }
    
        
        
        
        

    }
    /* Close the JMS connection */
    public void close( ) throws JMSException {
        connection.close( );
    }
    /* Run the Chat client */
    public static void main(String [] args){
        try{
            // args[0]=topicName; args[1]=username; args[2]=password
            Consumer cons = new Consumer();
            cons.doIt(System.getProperties());
            Thread.currentThread().join();


            

        } catch (Exception e){ e.printStackTrace( ); }
    }
	public void onException(JMSException arg0) {
		System.out.println("Got exception:" + arg0.getMessage());
 
		
	}
}