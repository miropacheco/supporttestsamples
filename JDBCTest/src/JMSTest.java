import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSTest {
	public void doIt() {
		long startTime = System.nanoTime();
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				System.getProperty("CONTEXT_FACTORY", "com.pcbsys.nirvana.nSpace.NirvanaContextFactory"));
		String rname = System.getProperty("RNAME");

		if (rname == null) {
			env.put(Context.PROVIDER_URL, System.getProperty("PROVIDER_URL", "nsp://127.0.0.1:9000"));
		} else {
			env.put(Context.PROVIDER_URL, System.getProperty("PROVIDER_URL", rname));
		}
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, System.getProperty("PRINCIPAL", "Administrator"));
		env.put(Context.SECURITY_CREDENTIALS, System.getProperty("PASSWORD", "manage"));
		try {
			Context ctx = new InitialContext(env);
			ConnectionFactory c = (ConnectionFactory) ctx.lookup("EventFactory");
			Connection con = c.createConnection("Administrator","manage");
			con.close();

		} catch (NamingException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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


			while (!exec.isTerminated())
			{
				Thread.currentThread().sleep(5000);

			}
			System.out.println("\nFinished all threads");
			exec.shutdown();

		} catch (Exception e) {
			System.out.println("Got Exepcption in main");

		}
	}
}