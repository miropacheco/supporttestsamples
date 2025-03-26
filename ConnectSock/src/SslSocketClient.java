
/* SslSocketClient.java
 - Copyright (c) 2014, HerongYang.com, All Rights Reserved.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SslSocketClient {
	public void checkSytanx() {

		TrustManagerFactory tmf;
		try {
			tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

			// get user password and file input stream
			char[] password = "manage".toCharArray();

			try (FileInputStream fis = new FileInputStream("c:/tmp/victor.jks")) {
				ks.load(fis, password);
			}
			tmf.init(ks);

			TrustManager[] trustManagers = tmf.getTrustManagers();

			SSLContext sslContext = SSLContext.getInstance("JKS");
			sslContext.init(null, trustManagers, null);
			SSLSocketFactory f = sslContext.getSocketFactory();
			long startTime = System.nanoTime();
			for (int k = 0; k < 1000; k++) {
				SSLSocket c = (SSLSocket) f.createSocket(System.getProperty("host", "localhost"),
						Integer.parseInt(System.getProperty("port", "5546")));
				c.startHandshake();

			}

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintStream out = System.out;

		SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();

		try {
			SSLSocket c = (SSLSocket) f.createSocket(System.getProperty("host", "localhost"),
					Integer.parseInt(System.getProperty("port", "5546")));
			// .setEnabledCipherSuites(new String []
			// {"TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384","TLS_RSA_WITH_AES_256_CBC_SHA256","TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384","TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384","TLS_DHE_RSA_WITH_AES_256_CBC_SHA256","TLS_DHE_DSS_WITH_AES_256_CBC_SHA256","TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA","TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA","TLS_RSA_WITH_AES_256_CBC_SHA","TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA","TLS_ECDH_RSA_WITH_AES_256_CBC_SHA",
			// "TLS_DHE_RSA_WITH_AES_256_CBC_SHA","TLS_DHE_DSS_WITH_AES_256_CBC_SHA",
			// "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384","TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384","TLS_RSA_WITH_AES_256_GCM_SHA384","TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384","TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384","TLS_DHE_RSA_WITH_AES_256_GCM_SHA384",
			// "TLS_DHE_DSS_WITH_AES_256_GCM_SHA384"});
			// c.setEnabledCipherSuites(new String []
			// {"TLS_AES_256_GCM_SHA384","TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384"});
			// c.setEnabledProtocols(new String[]{"TLSv1.2"});
			// c.setNeedClientAuth(true);

			c.setEnabledProtocols(new String[] { "TLSv1.2" });
			printSocketInfo(c);
			c.startHandshake();
			System.out.println("Disabled algorithms:");
			System.out
					.println("Disabled algorithms:" + java.security.Security.getProperty("jdk.tls.disabledAlgorithms"));
			System.out.println(
					"Disabled algorithms:" + java.security.Security.getProperty("jdk.certpath.disabledAlgorithms"));
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
			BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String m = null;
			// Socket c = new Socket(System.getProperty("host","localhost"),
			// Integer.parseInt(System.getProperty("port","8080")));
			/*
			 * BufferedWriter w = new BufferedWriter(new
			 * OutputStreamWriter(c.getOutputStream())); BufferedReader r = new
			 * BufferedReader(new InputStreamReader(c.getInputStream()));
			 */

			/*
			 * FileInputStream fis= new
			 * FileInputStream(System.getProperty("file","c:/tmp/out.txt")); byte[] buffer =
			 * new byte[1024]; while (true) { int data= fis.read(buffer); if (data==-1)
			 * break; c.getOutputStream().write(buffer,0,data); } while (true) { int b =
			 * c.getInputStream().read(); if (b==-1) { break; } System.out.print((char) b);
			 * } /* while ((m = r.readLine()) != null) { out.println(m); m = in.readLine();
			 * w.write(m, 0, m.length()); w.newLine(); w.flush(); }
			 */
			w.close();
			r.close();
			c.close();
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}

	private static void printSocketInfo(SSLSocket s) {

		System.out.println("Socket class: " + s.getClass());
		System.out.println("   Remote address = " + s.getInetAddress().toString());
		System.out.println("   Remote port = " + s.getPort());
		System.out.println("   Local socket address = " + s.getLocalSocketAddress().toString());
		System.out.println("   Local address = " + s.getLocalAddress().toString());
		System.out.println("   Local port = " + s.getLocalPort());
		System.out.println("   Need client authentication = " + s.getNeedClientAuth());
		SSLSession ss = s.getSession();
		System.out.println("   Cipher suite = " + ss.getCipherSuite());
		System.out.println("   Protocol = " + ss.getProtocol());
	}
}