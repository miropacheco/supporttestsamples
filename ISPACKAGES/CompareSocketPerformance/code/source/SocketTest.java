

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import com.wm.data.IData;
// --- <<IS-END-IMPORTS>> ---

public final class SocketTest

{
	// ---( internal utility methods )---

	final static SocketTest _instance = new SocketTest();

	static SocketTest _newInstance() { return new SocketTest(); }

	static SocketTest _cast(Object o) { return (SocketTest)o; }

	// ---( server methods )---




	public static final void SocketTest (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(SocketTest)>> ---
		// @sigtype java 3.5
		// [i] field:0:required host
		// [i] field:0:required port
		// [i] field:0:required trustStore
		// [i] field:0:required password
		TrustManagerFactory tmf;
		try {
			IDataCursor cursor = pipeline.getCursor();
			cursor.first("trustStore");
			String trustStore=(String) cursor.getValue();
			cursor.first("password");
			String password=(String) cursor.getValue();
		
			cursor.first("host");
			String host=(String) cursor.getValue();
			cursor.first("port");			
			String port =(String) cursor.getValue();
			
			
			
			tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		
			// get user password and file input stream
			
		
			try (FileInputStream fis = new FileInputStream(trustStore)) {
				ks.load(fis, password.toCharArray());
			}
			tmf.init(ks);
		
			TrustManager[] trustManagers = tmf.getTrustManagers();
		
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustManagers, null);
			SSLSocketFactory f = sslContext.getSocketFactory();
			long startTime = System.nanoTime();
			for (int k = 0; k < 1000; k++) {
				SSLSocket c = (SSLSocket) f.createSocket(host,
						Integer.parseInt(port));
				c.startHandshake();
				c.close();
		
			}
			long endTime = System.nanoTime();
			System.out.println("Elapsed Time:" + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
		
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
		
		
		    
		// --- <<IS-END>> ---

                
	}
}

