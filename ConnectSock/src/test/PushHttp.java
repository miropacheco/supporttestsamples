package test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class PushHttp {
	static int connCount = 0;
	static int errors = 0;
	static int non200 = 0;;

	public void connect(String url, String id, int port) {
		long startTime = System.nanoTime();
		CloseableHttpClient client = HttpClients.custom().build();

		// (1) Use the new Builder API (from v4.3)
		String auth = "Administrator:manage";
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		String authHeaderValue = "Basic " + new String(encodedAuth);
		HttpUriRequest request = RequestBuilder.get().setUri(url + id)
				// (2) Use the included enum
				.setHeader("Content-Type", "application/json").setHeader("Authorization", authHeaderValue)
				// (3) Or your own
				.build();

		try {
			CloseableHttpResponse response = client.execute(request);
			InputStream in = new BufferedInputStream(response.getEntity().getContent());
			byte[] buffer = new byte[in.available()];
			int bytesRead = in.read(buffer);
			String out = new String(buffer);
			if (out.indexOf(id.concat("added from trigger")) == -1) {
				System.out.println("Houston, we have a problem " + out + " Elapsed time:" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "port:"+ port );
			}
			if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) > 60000) {
				System.out.println("Elapsed time:" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "port:"+ port );
			}
			/*else {
				System.out.println("Success" + id + " -port:" + port + " !");
			}*/

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void connect1(String url) {
		try {
			connCount++;
			URL obj = new URL(url);
			String auth = "Administrator:manage";
			byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
			String authHeaderValue = "Basic " + new String(encodedAuth);
			// System.out.println("Submitting request " + Thread.currentThread().getId());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// con.setReadTimeout(5000);
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", authHeaderValue);
			con.setRequestProperty("Accept", "application/json; charset=utf-8");
			int i = con.getResponseCode();

			if (i != 200) {
				non200++;
			}
			InputStream in = new BufferedInputStream(con.getInputStream());
			while (true) {
				int c = (int) in.read();
				if (c != -1) {
					System.out.print((char) c);
				} else
					break;
			}

			System.out.println(i);

		} catch (Exception e) {
			errors++;
			e.printStackTrace();

		}

	}

	public static void main(String s[]) {
		int TOTALREQUESTS =30000;
		int TOTALTRHEADS = 300;
		ExecutorService exec = Executors.newFixedThreadPool(TOTALTRHEADS);

		for (int i = 0; i < TOTALREQUESTS; i++) {

			Runnable worker = new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					UUID uuid = UUID.randomUUID();

					// Convert UUID to String
					String guid = uuid.toString();
					 Random random = new Random();
				    int result = random.nextInt(2);
				    int port;
				    if (result==1)
				    	port = 5510;
				    else 
				    	port=5512;

					new PushHttp().connect(
							"http://localhost:" + port + "/restv2/ReplyResponseStressTest:new_rest_1/new_rest_1/", guid,port);

				}

			};
			exec.execute(worker);
		}
		try {
			Thread.currentThread().sleep(10000);
			exec.shutdown();
			while (!exec.awaitTermination(50000, TimeUnit.SECONDS)) {
				System.out.println("Waiting");
				System.out.println("Done requests:" + connCount);

				System.out.println(non200);

			}

			System.out.println("errors: " + errors);
			System.out.println("Total: " + connCount);
			System.out.println("non200: " + non200);
			System.out.println("finished waiting");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
