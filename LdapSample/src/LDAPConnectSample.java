import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import netscape.ldap.LDAPConnection;
import netscape.ldap.factory.JSSESocketFactory;

public class LDAPConnectSample {
	public void doIt() {
		/* Step 1: Create a new connection. */

		LDAPConnection ld = new LDAPConnection();
		ld.setSocketFactory(new JSSESocketFactory(null));

		try {

			/* Step 2: Connect to an LDAP server. */
			// System.out.println("Reaching out to server on thread:" +
			// Thread.currentThread().getId());
			long startTime = System.nanoTime();

			ld.connect(3, "localhost", 10636, "uid=admin,ou=system", "secret");

			if ((System.nanoTime() - startTime) / 1000000 > 1000)
				System.out.println("Total time on thread:" + Thread.currentThread().getId() + "="
						+ (System.nanoTime() - startTime) / 1000000);

			String[] x = { "" };

			ld.search("", ld.SCOPE_SUB, "ou=users,ou=system", x, false);

			/*
			 * Step 3: Authenticate to the server.
			 * 
			 * If you do not specify a version number,
			 * 
			 * this method authenticates your client
			 * 
			 * as an LDAP v2 client (not LDAP v3).
			 */

			// ld.authenticate(3, "uid=admin,ou=system", "secret1");

			/* Step 5: Disconnect from the server when done. */

			ld.disconnect();

		} catch (Exception e) {

			System.out.println("Error: " + e.toString());

		}

	}

	public static void main(String[] args) throws InterruptedException

	{
		int TOTALREQUESTS = 1;
		int TOTALTHEADS = 1;
		ExecutorService exec = Executors.newFixedThreadPool(TOTALTHEADS);
		try {
			while (true) {

				for (int i = 0; i < TOTALREQUESTS; i++) {

					Runnable worker = new Runnable() {

						public void run() {
							// TODO Auto-generated method stub
							new LDAPConnectSample().doIt();

						}

					};
					exec.execute(worker);
				}

				try {
					Thread.sleep(1000);
					break;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}

			}
			exec.shutdown();
			while (!exec.isTerminated())

			{

			}
			System.out.println("\nFinished all threads");

		} catch (Exception e) {
			System.out.println("Got Exepcption in main");

		}
	}

}