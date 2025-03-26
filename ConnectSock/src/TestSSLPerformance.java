import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class TestSSLPerformance {
	private static String host;
	private static String port;
	private static String password;
	private static String trustStore;

	public static void main(String s[])
			throws NoSuchAlgorithmException, NumberFormatException, UnknownHostException, IOException, CertificateException, KeyStoreException, KeyManagementException {
		host = System.getProperty("host", "localhost");
		port = System.getProperty("port", "8787");
		trustStore=System.getProperty("trustStore","c:\\tmp\\trust.jks");
		password=System.getProperty("password","manage");
			
		
		SSLContext sslContext = SSLContext.getInstance("TLS");	
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
	
		// get user password and file input stream
		
	
		try (FileInputStream fis = new FileInputStream(trustStore)) {
			ks.load(fis, password.toCharArray());
		}
		tmf.init(ks);
	
		TrustManager[] trustManagers = tmf.getTrustManagers();	
		sslContext.init(null, trustManagers, null);
		SSLSocketFactory f = sslContext.getSocketFactory();
		long startTime = System.nanoTime();
		for (int k = 0; k < 1000; k++) {
			SSLSocket c = (SSLSocket) f.createSocket(host, Integer.parseInt(port));
			c.startHandshake();
			c.close();

		}
		long endTime = System.nanoTime();
		System.out.println("Elapsed Time:" + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

	}

}
