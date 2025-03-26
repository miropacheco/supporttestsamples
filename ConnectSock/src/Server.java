import java.io.FileInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * Example printout of this program <?xml version='1.0'
 * encoding='UTF-8'?><soapenv:Envelope xmlns:soapenv=
 * "http://schemas.xmlsoap.org/soap/envelope/" ><soapenv:Header>
 * <wsse:Security xmlns:wsse=
 * "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
 * soapenv:mustUnderstand="1"><wsu:Timestamp xmlns:wsu=
 * "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
 * wsu
 * :Id="Timestamp-992359490"><wsu:Created>2016-02-18T16:18:10.876Z</wsu:Created
 * ><wsu:Expires>2016-02-18T16:23:10.876Z</wsu:Expires></wsu:Timestamp><wsse:
 * UsernameToken xmlns:wsu=
 * "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
 * wsu:Id="UsernameToken-1227945828"><wsse:Username>user1</wsse:Username><wsse:
 * Password Type=
 * "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest"
 * >WDnBQUdWF1V/M2iZXkHkvLPM2XE=</wsse:Password><wsse:Nonce>QKHhCmfMTaEqt2hig5+
 * gZQ==</wsse:Nonce><wsu:Created>2016-02-18T16:18:10.877Z</wsu:Created></wsse:
 * UsernameToken></wsse:Security></soapenv:Header><soapenv:Body
 * /></soapenv:Envelope>
 * 
 */

public class Server {
	public static void showCiphers() {
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

		String[] defaultCiphers = ssf.getDefaultCipherSuites();
		String[] availableCiphers = ssf.getSupportedCipherSuites();

		TreeMap ciphers = new TreeMap();

		for (int i = 0; i < availableCiphers.length; ++i)
			ciphers.put(availableCiphers[i], Boolean.FALSE);

		for (int i = 0; i < defaultCiphers.length; ++i)
			ciphers.put(defaultCiphers[i], Boolean.TRUE);

		System.out.println("Default\tCipher");
		for (Iterator i = ciphers.entrySet().iterator(); i.hasNext();) {
			Map.Entry cipher = (Map.Entry) i.next();

			if (Boolean.TRUE.equals(cipher.getValue()))
				System.out.print('*');
			else
				System.out.print(' ');

			System.out.print('\t');
			System.out.println(cipher.getKey());
		}
	}

	public static void main(String st[]) throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			UnrecoverableKeyException, KeyManagementException {

		String secure = System.getProperties().getProperty("secure", "false");
		
		showCiphers();
		//System.out.println(System.getProperties().get("javax.net.ssl.keyStore"));
		//System.setProperty("javax.net.ssl.keyStorePassword", "manage1121");
		//System.setProperty("javax.net.ssl.keyStore", "c:/tmp/victor.jks");
		//System.out.println(System.getProperty("file.encoding"));
		/*
		 * Set<String> algs = new TreeSet<>(); for(String s:
		 * Security.getAlgorithms("Cipher")) { System.out.println(s); }
		 * System.out.println("key factory"); for(String s:
		 * Security.getAlgorithms("KeyPairGenerator")) { System.out.println(s); }
		 */
		// java.util.Arrays.asList(((SSLServerSocketFactory)
		// javax.net.ssl.SSLServerSocketFactory.getDefault()).getSupportedCipherSuites()).stream().forEach(System.out::println);

		/*
		 * for (Provider provider : Security.getProviders()) {
		 * provider.getServices().stream() .filter(s -> "Cipher".equals(s.getType()))
		 * .map(Service::getAlgorithm) .forEach(algs::add); }
		 */
		// algs.forEach(System.out::println);

		try {
			// showCiphers()
			InputStream b;
			if (secure.equalsIgnoreCase("true")) {
				/*
				 * SSLServerSocketFactory sslsocketfactory = (SSLServerSocketFactory)
				 * SSLServerSocketFactory.getDefault();
				 * 
				 * SSLServerSocket sslsocket = (SSLServerSocket)
				 * sslsocketfactory.createServerSocket(1256);
				 */

				// sslsocket.accept();
				/*
				 * SSLContext.getDefault(); KeyStore ks = KeyStore.getInstance("jks");
				 * ks.load(new FileInputStream("c:/tmp/lowercert_mysigner_.p12"),
				 * "manage".toCharArray()); // CHANGE THIS // LINE KeyManagerFactory kmf =
				 * KeyManagerFactory.getInstance("SunX509"); kmf.init(ks,
				 * "manage".toCharArray()); // CHANGE THIS LINE SSLContext sc =
				 * SSLContext.getInstance("TLS");
				 */
				SSLContext sc1 = SSLContext.getDefault();				
				System.out.println(sc1.getDefaultSSLParameters().getWantClientAuth());
				
				System.out.println("got the default context");
				

				KeyStore ks = KeyStore.getInstance("p12");
				// ks.load(new FileInputStream("c:/tmp/lowercert_mysigner_.p12"),
				// "manage".toCharArray()); // CHANGE THIS
				// LINE
				ks.load(new FileInputStream("C:/tmp/victor.p12"), "manage".toCharArray()); // CHANGE
																											// THIS
				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				kmf.init(ks, "manage".toCharArray()); // CHANGE THIS LINE
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(kmf.getKeyManagers(), null, null);
				SSLServerSocketFactory ssf = sc.getServerSocketFactory();
				SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(7878);
				s.setEnabledProtocols(new String[] { "TLSv1.2","TLSv1.1" });
				s.setWantClientAuth(false);
				for (String x : s.getEnabledCipherSuites()) {
					System.out.println(x);
				}

				SSLSocket client = (SSLSocket) s.accept();
				b = client.getInputStream();

			} else {
				System.out.println("non secure version");
				ServerSocket s = new ServerSocket(7878);
				Socket client = s.accept();
				b = client.getInputStream();
			}

			while (true) {
				byte ret = (byte) b.read();
				char c = (char) (ret & 0xFF);
				System.out.print(c);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("This is the bad thing handling");
			e.printStackTrace();

		}
	}
}
