

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.dispatcher.Configurator;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.wm.data.IData;
// --- <<IS-END-IMPORTS>> ---

public final class AsyncThread

{
	// ---( internal utility methods )---

	final static AsyncThread _instance = new AsyncThread();

	static AsyncThread _newInstance() { return new AsyncThread(); }

	static AsyncThread _cast(Object o) { return (AsyncThread)o; }

	// ---( server methods )---




	public static final void AsyncThreadService (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(AsyncThreadService)>> ---
		// @sigtype java 3.5
		/*Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (keepRunning) {
					try {
						String[] s = SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
						for (String st:s) {
							System.out.println(st);
						}
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Service.doThreadInvoke("AsyncThread","ServiceCalledByThread",null,0);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
			
		});
		t.run();*/
		
		 ExecutorService pool = Executors.newFixedThreadPool(20);
		 for (int k=0; k < 1; k++) {
			 pool.execute(new Runnable() {
				
				@Override
				public void run() {
					if (keepRunning) {
						System.out.println("got here");
						Service.doThreadInvoke("MyJavaService","new_flowservice",null,0);
					}
					
				}
			});
		   if (!keepRunning) {
			   break;
		   }
		 }
		 try {
			if (!pool.awaitTermination(1, TimeUnit.HOURS)) {
			     pool.shutdownNow(); // Cancel currently executing tasks
			     // Wait a while for tasks to respond to being cancelled
			     try {
					if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					     System.err.println("Pool did not terminate");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		// --- <<IS-END>> ---

                
	}



	public static final void StopService (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(StopService)>> ---
		// @sigtype java 3.5
		keepRunning = false;
		System.out.println(System.getProperty("maverick.license.filename"));
			
		// --- <<IS-END>> ---

                
	}



	public static final void new_javaService (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(new_javaService)>> ---
		// @sigtype java 3.5
		try {
			Configurator.setHostId("Mirovisk");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static boolean keepRunning=true;
		
	// --- <<IS-END-SHARED>> ---
}

