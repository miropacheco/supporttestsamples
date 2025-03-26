import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server {
	SSLServerSocket sock;
	Socket client = null;

	public void doIt() {

		System.out.println("KB: " + (double) (Runtime.getRuntime().maxMemory()) / (1024 * 1024));

		System.getProperties();

		String clientCert = System.getProperties().getProperty("clientCert", "false");
		System.out.println(System.getProperty("file.encoding"));
		System.out.println("Default CS:" + Charset.defaultCharset());
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		System.out.println(sdf.format(new Date()));
		DateTimeFormatter errorDateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SS");

		System.out.println("Test miro:"
				+ errorDateFormat.format(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("UTC"))));
		System.out.println(
				"Test miro2:" + errorDateFormat.format(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));
		;

		String secure = System.getProperties().getProperty("secure", "false");
		ServerSocket serverSock = null;
		try {
			if (secure.equalsIgnoreCase("true")) {
				SSLContext sc1 = SSLContext.getDefault();
				System.out.println(sc1.getDefaultSSLParameters().getWantClientAuth());

				System.out.println("got the default context");

				KeyStore ks = KeyStore.getInstance("jks");
				// ks.load(new FileInputStream("c:/tmp/lowercert_mysigner_.p12")
				// "manage".toCharArray()); // CHANGE THIS
				// LINE
				ks.load(new FileInputStream("C:/sag1015/common/conf/keystore.jks"), "manage".toCharArray()); // CHANGE
				// THIS
				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				kmf.init(ks, "manage".toCharArray()); // CHANGE THIS LINE
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(kmf.getKeyManagers(), null, null);
				SSLServerSocketFactory ssf = sc.getServerSocketFactory();
				sock = (SSLServerSocket) ssf.createServerSocket(7878);
				sock.setEnabledProtocols(new String[] { "TLSv1.2", "TLSv1.1" });
				sock.setWantClientAuth(false);
				for (String x : sock.getEnabledCipherSuites()) {
					System.out.println(x);
				}
		
			} 
			else {
				serverSock = new ServerSocket(7878);
			}
			while (true) {
				if (sock!=null) {
					client = (SSLSocket) sock.accept();
				}
				else {
					
					client = serverSock.accept();
				}

				// TODO Auto-generated method stub
				System.out.println("Got another client:" + String.valueOf(client.getPort()));
				// client.getOutputStream().write("oracle".getBytes());
				;
				// client.getOutputStream().flush();
				Thread t = new Thread(new Runnable() {
					InputStream b = client.getInputStream();
					public void run() {
						int count = 0;
						try {
							while (true) {
								int ret;


								ret = (int) b.read();


								if (ret == -1) {
									System.out.println("data is over: " + count);
									;
									break;
								}

								byte c = (byte) (ret & 0xFF);
								if ((c >= 32 && c < 127) || (c == 13 || c == 10))
									System.out.print((char) c);
								else
									System.out.print(ret);
								count++;

							}
						}
						catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}});
				t.start();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
			}
		});
		t.start();

	}

	public static void main(String st[]) {
		new Server().doIt();

	}

}