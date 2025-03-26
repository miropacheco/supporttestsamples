import java.lang.management.ManagementFactory;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class Ldap {
	public void doIt() {
		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
		long startTime = System.nanoTime();
		Hashtable<String, String> environment = new Hashtable<String, String>();

		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		environment.put(Context.PROVIDER_URL, "ldap://localhost:10389");
		environment.put(Context.SECURITY_AUTHENTICATION, "simple");
		environment.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
		environment.put(Context.SECURITY_CREDENTIALS, "secret");
		environment.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(30000));
		environment.put(Context.REFERRAL, "follow");
		try {
			DirContext context = new InitialDirContext(environment);
			System.out.println("Connected..");
			System.out.println(context.getEnvironment());
			context.close();
		} catch (AuthenticationNotSupportedException exception) {
			exception.printStackTrace();
			System.out.println("The authentication is not supported by the server");
		}

		catch (AuthenticationException exception) {
			exception.printStackTrace();
			System.out.println("Incorrect password or username");
		}

		catch (NamingException exception) {
			exception.printStackTrace();
			System.out.println("Error when trying to create the context:" + exception.toString());
		}
		if ((System.nanoTime() - startTime) / 1000000 > 100)
			System.out.println("Total time on thread:" + Thread.currentThread().getId() + "="
					+ (System.nanoTime() - startTime) / 1000000);

	}

	public static void main(String[] args) {
		int TOTALREQUESTS = 1;
		int TOTALTHEADS = 1;
	
		ExecutorService exec = Executors.newFixedThreadPool(TOTALTHEADS);
		try {

			for (int i = 0; i < TOTALREQUESTS; i++) {

				Runnable worker = new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						new Ldap().doIt();

					}

				};
				exec.execute(worker);
			}

			exec.shutdown();
			while (!exec.isTerminated()) {
				

			}
			System.out.println("\nFinished all threads");

		} catch (Exception e) {
			System.out.println("Got Exepcption in main");

		}
	}
}