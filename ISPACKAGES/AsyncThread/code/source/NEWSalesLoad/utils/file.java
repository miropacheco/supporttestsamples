package NEWSalesLoad.utils;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.Server;
import NEWSalesLoad.utils.*;
// --- <<IS-END-IMPORTS>> ---

public final class file

{
	// ---( internal utility methods )---

	final static file _instance = new file();

	static file _newInstance() { return new file(); }

	static file _cast(Object o) { return (file)o; }

	// ---( server methods )---




	public static final void AnotherJava (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(AnotherJava)>> ---
		// @sigtype java 3.5
		try {
			Server.invokeService("NEWSalesLoad.utils.file:new_javaService",null);
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("no exception this time");
			
		// --- <<IS-END>> ---

                
	}



	public static final void new_javaService (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(new_javaService)>> ---
		// @sigtype java 3.5
		try { 
		FilePatternFilter fpf = new FilePatternFilter();
		}
		catch (Exception e) {
			System.out.println("Got an exception");
			e.printStackTrace();
		}
		// --- <<IS-END>> ---

                
	}
}

