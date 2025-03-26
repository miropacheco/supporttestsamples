import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Example5 {
	public Integer exclusiveObject=0;
	public void doIt() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("waiting to get my turn:" + Thread.currentThread().getId());
				synchronized (exclusiveObject) {
					System.out.println("got my turn" + Thread.currentThread().getId());
					exclusiveObject ++;					
					try {
						URL url = new URL("https://webservices.netsuite.com/wsdl/v2023_1_0/netsuite.wsdl");
						HttpsURLConnection con =   (HttpsURLConnection) url.openConnection();
						con.setRequestMethod("GET");						
						int responseCode = con.getResponseCode();
						System.out.println("GET Response Code :: " + responseCode);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(exclusiveObject);
				}
			
			}
			
		};
		for (int k = 0; k < 1; k++) {
			Thread t = new Thread(r, "Thread" + k);
			t.start();
		}

		
	}
	
	public static void main(String s[]) {
		new Example5().doIt();
		
		
	}

}
