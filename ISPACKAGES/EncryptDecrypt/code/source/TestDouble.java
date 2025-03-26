

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.IOException;
import com.wm.app.b2b.server.ISRuntimeException;
// --- <<IS-END-IMPORTS>> ---

public final class TestDouble

{
	// ---( internal utility methods )---

	final static TestDouble _instance = new TestDouble();

	static TestDouble _newInstance() { return new TestDouble(); }

	static TestDouble _cast(Object o) { return (TestDouble)o; }

	// ---( server methods )---




	public static final void test (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(test)>> ---
		// @sigtype java 3.5
		try {
			System.out.println("Entry");
			Thread.currentThread().sleep(1000);
			int a = 0;
			//int b = 1200/a;
			System.out.println("Exit");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		throw new ISRuntimeException("test");
		
			
		// --- <<IS-END>> ---

                
	}
}

