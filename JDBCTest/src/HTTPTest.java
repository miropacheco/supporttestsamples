import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class HTTPTest {
	public void doIt() {
		HttpURLConnection connection;
		try {
			
			connection = (HttpURLConnection) new URL("http://localhost:5579/invoke/sssss/test").openConnection();
			String encoded = Base64.getEncoder().encodeToString(("testtest:manage").getBytes(StandardCharsets.UTF_8));  //Java 8
			connection.setRequestProperty("Authorization", "Basic "+encoded);
			connection.setRequestProperty("Keep-connected", "true");
			connection.setRequestMethod("POST");			
			InputStream is = connection.getInputStream();
			is.read();
			//is.close();
			//connection.disconnect();
			


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		int TOTALREQUESTS = 250;
		int TOTALTHEADS = 200;
		byte[] x = new byte[100];
		System.out.println(Runtime.getRuntime().freeMemory());
		System.out.println(Runtime.getRuntime().maxMemory());		
		System.out.println(Runtime.getRuntime().totalMemory());

		ExecutorService exec = Executors.newFixedThreadPool(TOTALTHEADS);
		try {
			while (true) {

				for (int i = 0; i < TOTALREQUESTS; i++) {

					Runnable worker = new Runnable() {

						public void run() {
							// TODO Auto-generated method stub
							new HTTPTest().doIt();

						}

					};
					exec.execute(worker);
				}

				try {
					Thread.sleep(1000);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				break;

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