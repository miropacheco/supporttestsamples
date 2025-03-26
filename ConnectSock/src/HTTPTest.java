import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTPTest {
	public static void main(String st[]) throws IOException {
		String url = System.getProperty("url");
		URL server = new URL(url);		
		HttpsURLConnection connection = (HttpsURLConnection) server
				.openConnection();
		
		connection.connect();
		InputStream in = connection.getInputStream();
		while (true) {
			int b = in.read();
			if (b == -1) {
				break;
			}
			
			System.out.print((char)b);
		}
		
	}

}
