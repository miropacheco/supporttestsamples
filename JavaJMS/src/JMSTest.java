import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class JMSTest {
	public void doIt() {
		long startTime = System.nanoTime();
		// Hashtable<String, String> env = new Hashtable<String, String>();
		/*-Djava.naming.provider.url=nsp://localhost:9010  -Djava.naming.factory.initial=com.pcbsys.nirvana.nSpace.NirvanaContextFactory -DconnectionFactory=UM_JMS_JNDI  -DLOGLEVEL=7
				-Djavax.net.ssl.trustStore=c:\tmp\miro0510.jks -Djavax.net.ssl.truststorePassword=manage  -Djava.naming.security.principal=user1 -Djava.naming.security.credentials=user1password
			*/
		try {
			/*
			 * Properties props = new Properties();
			 * props.setProperty("java.naming.factory.initial",
			 * "com.ibm.msg.client.wmq.WMQConstants");// Set  the Initial Context Factory
			 * value from Settings > Messaging > JNDI Settings > JNDI Provider Alias >
			 * Create screen of ISadmin                        
			 * props.setProperty("java.naming.provider.url",
			 * "file:///esb/sag/JNDI-Directory/");// Set  the  Provider URL value from
			 * Settings > Messaging > JNDI Settings > JNDI Provider Alias > Create screen of
			 * ISadmin                         
			 * props.setProperty("java.naming.security.principal", "doit.batadsi");//
			 * Set  the  Security Principal value from Settings > Messaging > JNDI Settings
			 * > JNDI Provider Alias > Create screen of ISadmin                         
			 * props.setProperty("java.naming.security.credentials", "TRu-H+wrkk4w");//
			 * Set  the  Security Credentials value from Settings > Messaging > JNDI
			 * Settings > JNDI Provider Alias > Create screen of ISadmin
			 */

			Context ctx = new InitialContext(System.getProperties());
			NamingEnumeration<NameClassPair> list = ctx.list("");
			while (list.hasMore()) {
				System.out.println(list.next().getName()); // just prints out the connection factory we found. you can
															// comment this whole section
			}
			;
			/*

			ConnectionFactory c = (ConnectionFactory) ctx.lookup(System.getProperty("factory", "test"));
			Connection con = c.createConnection();
			/*
			 * Session s = con.createSession(false, Session.CLIENT_ACKNOWLEDGE); Queue q =
			 * (Queue) ctx.lookup("testqueue"); MessageConsumer mc = s.createConsumer(q);
			 * 
			 * mc.setMessageListener(new MessageListener() {
			 * 
			 * @Override public void onMessage(Message arg0) { // TODO Auto-generated method
			 * stub try { if (arg0 instanceof TextMessage)
			 * System.out.println("Got a text message" + ((TextMessage) arg0).getText());
			 * else if (arg0 instanceof BytesMessage) { byte[] b = new byte[(int)
			 * ((BytesMessage) arg0).getBodyLength()]; ((BytesMessage) arg0).readBytes(b);
			 * System.out.println("Got a byte message:" + new String(b) + " message id" +
			 * arg0.getJMSMessageID());; Thread.currentThread().sleep(1000); } else
			 * System.out.println("Got another type of message:" +
			 * arg0.getClass().getName());
			 * 
			 * s.recover(); } catch (JMSException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } catch (InterruptedException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 * 
			 * 
			 * 
			 * } });
			 * 
			 * con.start(); System.in.read();
			 */
			//con.close();

			// con.close();*/
			/*
			 * s.close(); con.close(); ctx.close();
			 */

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/*catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if ((System.nanoTime() - startTime) / 1000000 > 1000)
			System.out.println("Total time on thread:" + Thread.currentThread().getId() + "="
					+ (System.nanoTime() - startTime) / 1000000);

	}

	public static void main(String[] args) {
		int TOTALREQUESTS = 1;
		int TOTALTRHEADS = 1;
		byte[] x = new byte[100000];
		System.out.println(Runtime.getRuntime().freeMemory());
		System.out.println(Runtime.getRuntime().maxMemory());
		System.out.println(Runtime.getRuntime().totalMemory());

		ExecutorService exec = Executors.newFixedThreadPool(TOTALTRHEADS);
		try {

			for (int i = 0; i < TOTALREQUESTS; i++) {

				Runnable worker = new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						new JMSTest().doIt();

					}

				};
				exec.execute(worker);
			}

			while (!exec.isTerminated()) {
				Thread.currentThread().sleep(50000000);

			}
			System.out.println("\nFinished all threads");
			exec.shutdown();

		} catch (Exception e) {
			System.out.println("Got Exepcption in main");

		}
	}
}