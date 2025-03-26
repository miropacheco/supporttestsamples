

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class stopandwait

{
	// ---( internal utility methods )---

	final static stopandwait _instance = new stopandwait();

	static stopandwait _newInstance() { return new stopandwait(); }

	static stopandwait _cast(Object o) { return (stopandwait)o; }

	// ---( server methods )---




	public static final void StopAndWaitForNotify (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(StopAndWaitForNotify)>> ---
		// @sigtype java 3.5
		   try {
			int seconds = 30; // change this line to extend the wait
		    System.out.println("waiting up to " + seconds +" seconds");
		    synchronized (lock) {
		    	lock.wait(seconds*10000);
		    }
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   System.out.println("We got it fred");
		// --- <<IS-END>> ---

                
	}



	public static final void WakeEveryone (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(WakeEveryone)>> ---
		// @sigtype java 3.5
		synchronized(lock) {
		lock.notifyAll();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static Object lock= new Object();
	// --- <<IS-END-SHARED>> ---
}

