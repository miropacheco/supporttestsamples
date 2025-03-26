

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
// --- <<IS-END-IMPORTS>> ---

public final class changeContext

{
	// ---( internal utility methods )---

	final static changeContext _instance = new changeContext();

	static changeContext _newInstance() { return new changeContext(); }

	static changeContext _cast(Object o) { return (changeContext)o; }

	// ---( server methods )---




	public static final void TestContext (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(TestContext)>> ---
		// @sigtype java 3.5
		try {
			// 
			/*System.setProperty("javax.net.ssl.keyStorePassword","manage2");
			System.setProperty("javax.net.ssl.keyStore","c:\\tmp\\namanstest.jks");*/			
			SSLContext.getDefault();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(e); 
		}
		// --- <<IS-END>> ---

                
	}



	public static final void changeContext (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(changeContext)>> ---
		// @sigtype java 3.5
		KeyStore ks;
		try {
			ks = KeyStore.getInstance("jks");
			ks.load(new FileInputStream("../../../common/conf/keystore.jks"), "manage".toCharArray()); // CHANGE THIS LINE
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, "manage".toCharArray()); // CHANGE THIS LINE
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);
			SSLContext.setDefault(sc);
			
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
 
	// --- <<IS-END-SHARED>> ---
}

