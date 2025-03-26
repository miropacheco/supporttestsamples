

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nDurable;
import com.pcbsys.nirvana.client.nDurableAttributes;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nDurableAttributes.nDurableType;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import org.apache.commons.lang.RandomStringUtils;
import com.wm.lang.ns.NSName;
import com.wm.app.b2b.server.ServiceThread;
// --- <<IS-END-IMPORTS>> ---

public final class logissue

{
	// ---( internal utility methods )---

	final static logissue _instance = new logissue();

	static logissue _newInstance() { return new logissue(); }

	static logissue _cast(Object o) { return (logissue)o; }

	// ---( server methods )---




	public static final void createConsumers (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(createConsumers)>> ---
		// @sigtype java 3.5
 ExecutorService pool = Executors.newFixedThreadPool(500);
 for (int k=0; k < 500; k++) {
	 pool.execute(new Runnable() {

		@Override
		public void run() {
			try {
			// TODO Auto-generated method stub
			String host = System.getProperty("host", "nsp://localhost:9005");

			String[] RNAME = host.split(",");

			nSessionAttributes nsa = new nSessionAttributes(RNAME);
			// nsa.setName("mysession");

			nSession mySession = nSessionFactory.create(nsa, System.getProperty("user","miro"), System.getProperty("password","manage"));
			nsa.setReconnectInterval(200);

			mySession.init();

			nChannel ch = mySession.findChannel(new nChannelAttributes(System.getProperty("channel","wm/is/MyJavaService/TestDoc"))); // put whatever document you want here
			String s = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

			nDurableAttributes durAttr = nDurableAttributes.create(nDurableType.Shared, "mysubscription" + s);
			nDurable named = null;
			try {
				named = ch.getDurableManager().add(durAttr);

			} catch (com.pcbsys.nirvana.client.nNameAlreadyBoundException e) {
				named = ch.getDurableManager().get("mysubscription" + s  );
			}
			ch.addSubscriber(new nEventListener() {
				
				@Override
				public void go(nConsumeEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			},named,null,25); 
		
					
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
			
		 
	 });
 }
	
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static boolean keepRunning=true;
	// --- <<IS-END-SHARED>> ---
}

