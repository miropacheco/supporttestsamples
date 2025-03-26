

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.ISRuntimeException;
// --- <<IS-END-IMPORTS>> ---

public final class HistoryDemo

{
	// ---( internal utility methods )---

	final static HistoryDemo _instance = new HistoryDemo();

	static HistoryDemo _newInstance() { return new HistoryDemo(); }

	static HistoryDemo _cast(Object o) { return (HistoryDemo)o; }

	// ---( server methods )---




	public static final void Release (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(Release)>> ---
		// @sigtype java 3.5
		synchronized(myLock) {	
		myLock.notifyAll();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void ThrowError (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(ThrowError)>> ---
		// @sigtype java 3.5
		 try {
			synchronized(myLock) {
			 myLock.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new ISRuntimeException("This is my exception");
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static Object myLock = new Object();
	// --- <<IS-END-SHARED>> ---
}

