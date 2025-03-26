

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
// --- <<IS-END-IMPORTS>> ---

public final class statistics

{
	// ---( internal utility methods )---

	final static statistics _instance = new statistics();

	static statistics _newInstance() { return new statistics(); }

	static statistics _cast(Object o) { return (statistics)o; }

	// ---( server methods )---




	public static final void FinishCollection (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(FinishCollection)>> ---
		// @sigtype java 3.5
		// [i] field:0:required Label
		IDataCursor c = pipeline.getCursor();
		c.first("Label");
		String label = (String) c.getValue();
		Long finalTime = System.currentTimeMillis() - startTime.get();
			StatValues st=null;
			if (myMap.containsKey(label)) {
		
				WrappedStats ws= myMap.get(label);
				if (ws!=null) {
					ws.updateStats(finalTime);
				}
			}
			// else someone deleteted the stats in between the calls. Nothing to do
				
			
			
			
		// --- <<IS-END>> ---

                
	}



	public static final void GetValues (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(GetValues)>> ---
		// @sigtype java 3.5
		// [i] field:0:required Label
		IDataCursor c = pipeline.getCursor();
		c.first("Label");
		String label = (String)(c.getValue());
		
			
			if (myMap.containsKey(label)) {  
				WrappedStats ws = myMap.get(label); 
				c.last();
				c.insertAfter("results", ws.consolidate()); 
			
			}
			
		// --- <<IS-END>> ---

                
	}



	public static final void ResetAll (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(ResetAll)>> ---
		// @sigtype java 3.5
		myMap.clear();
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void StartCollection (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(StartCollection)>> ---
		// @sigtype java 3.5
		// [i] field:0:required Label
		IDataCursor c = pipeline.getCursor();
		c.first("Label");
		startTime.set(System.currentTimeMillis());
		
		String label = (String)(c.getValue());
		if (!myMap.containsKey(label)) {
			WrappedStats wrStats = new WrappedStats();
		
		    myMap.putIfAbsent(label,wrStats);
		 
		}
		
		
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static ThreadLocal<Long> startTime = ThreadLocal.withInitial(() -> 0l); 
	static ConcurrentHashMap<String, WrappedStats> myMap = new ConcurrentHashMap<String, WrappedStats>();
	// --- <<IS-END-SHARED>> ---
}

