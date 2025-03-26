

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.diagnostic.RuntimeDataCollector;
import java.lang.management.ManagementFactory;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
// --- <<IS-END-IMPORTS>> ---

public final class stackTraceGeneration

{
	// ---( internal utility methods )---

	final static stackTraceGeneration _instance = new stackTraceGeneration();

	static stackTraceGeneration _newInstance() { return new stackTraceGeneration(); }

	static stackTraceGeneration _cast(Object o) { return (stackTraceGeneration)o; }

	// ---( server methods )---




	public static final void dumpStackTrace (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(dumpStackTrace)>> ---
		// @sigtype java 3.5
	long usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / ( 1024 * 1024 );
	
	
	System.out.println("Memory before dump:" + usedMemory);
	String s = RuntimeDataCollector.getThreadDump();
	System.out.println(s);
		// --- <<IS-END>> ---

                
	}



	public static final void startCheckingMemCPU (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(startCheckingMemCPU)>> ---
		// @sigtype java 3.5
		// [i] field:0:required memory
		if (isRunning) {
		    System.out.println("Already running");
		    
			return;
		}
		else {
			keepRunning = true;
		}
		
		
		IDataCursor cursor = pipeline.getCursor();
		if (cursor.first("memory"))
			memory =Long.parseLong((String)cursor.getValue());
		
		Runnable r =  new Runnable() {
			
			public void run() {
				System.out.println("Starting thread");
				isRunning = true;
				try {
				while (keepRunning) {
					long usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / ( 1024 * 1024 );
					 MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
					 ObjectName beanName;
					
						beanName = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);
		
					 Object attribute =platformMBeanServer.getAttribute(beanName, "SystemCpuLoad");
					 long cpu = Math.round(((Double) attribute).doubleValue() * 100);
					
					if (cpu > 80) { // change this line
						try {
							System.out.println("Dumping out stack trace. Memory:" + memory + " cpu:" + cpu);
							// change your action here
							dumpStackTrace(pipeline);
							
						} catch (ServiceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						Thread.currentThread().sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				} catch (MalformedObjectNameException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InstanceNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (AttributeNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ReflectionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MBeanException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				isRunning = false;
				keepRunning = true;
				System.out.println("Stopped running");
				
			}
		};
		 Thread t = new Thread(r, "thread dump check");
		 t.start();
		  
			
		// --- <<IS-END>> ---

                
	}



	public static final void stopCheckingMemCPU (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stopCheckingMemCPU)>> ---
		// @sigtype java 3.5
	keepRunning = false;
	
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	public static boolean keepRunning = true;
	public static boolean isRunning = false;
	public static long memory = 200;
		
	// --- <<IS-END-SHARED>> ---
}

