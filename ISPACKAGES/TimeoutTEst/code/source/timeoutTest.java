

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class timeoutTest

{
	// ---( internal utility methods )---

	final static timeoutTest _instance = new timeoutTest();

	static timeoutTest _newInstance() { return new timeoutTest(); }

	static timeoutTest _cast(Object o) { return (timeoutTest)o; }

	// ---( server methods )---




	public static final void WaitJavaService (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(WaitJavaService)>> ---
		// @sigtype java 3.5
		try {
			Thread.currentThread().sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Seems that someone is impatient");
			e.printStackTrace();
			throw new ServiceException("Shit happened");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	Object obj = new Object();
	// --- <<IS-END-SHARED>> ---
}

