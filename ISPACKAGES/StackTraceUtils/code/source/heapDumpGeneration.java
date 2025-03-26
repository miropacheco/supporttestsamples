

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.client.Context;
import com.wm.lang.ns.NSName;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import com.sun.management.HotSpotDiagnosticMXBean;
import com.wm.app.b2b.server.diagnostic.RuntimeDataCollector;
// --- <<IS-END-IMPORTS>> ---

public final class heapDumpGeneration

{
	// ---( internal utility methods )---

	final static heapDumpGeneration _instance = new heapDumpGeneration();

	static heapDumpGeneration _newInstance() { return new heapDumpGeneration(); }

	static heapDumpGeneration _cast(Object o) { return (heapDumpGeneration)o; }

	// ---( server methods )---




	public static final void heapDump (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(heapDump)>> ---
		// @sigtype java 3.5
	//com.wm.app.b2b.server.InvokeState.doThreadInvoke("heapDumpGeneration","Test", new Values(),-1);
   //Context c = new Context();
   //try {
	//c.setAuthentication("Administrator","manage");
	//c.connect("localhost:5558");
	//c.invokeThreaded("wm.server.admin","getDiagnosticData", new Values());
    // change to match your path
	com.wm.app.b2b.server.diagnostic.DataCollector.getDiagnosticData(new Values());
	
	String file ="/tmp/heap.hprof";
	dumpHeap(file, true);
	
	
	
	
		// --- <<IS-END>> ---

                
	}



	public static final void startCheckingMemoy (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(startCheckingMemoy)>> ---
		// @sigtype java 3.5
		// [i] field:0:required memory
		if (isRunning) {
		    System.out.println("Returning");
		    
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
					System.out.println("usedMemory:" + usedMemory);
					if (usedMemory > memory) {
						try {
							heapDump(pipeline);
							keepRunning = false;
							
						} catch (ServiceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
				catch (Exception e) {
					e.printStackTrace();				}
				isRunning = false;
				keepRunning = true;
				System.out.println("Stopped running");
				
			}
		};
		 Thread t = new Thread(r, "thread dump check");
		 t.start();
		  
			
		// --- <<IS-END>> ---

                
	}



	public static final void stopCheckingMemory (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stopCheckingMemory)>> ---
		// @sigtype java 3.5
	keepRunning = false;
	
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	public static boolean keepRunning = true;
	public static boolean isRunning = false;
	public static long memory = 200;
	 // This is the name of the HotSpot Diagnostic MBean
	private static final String HOTSPOT_BEAN_NAME =
	     "com.sun.management:type=HotSpotDiagnostic";
	// field to store the hotspot diagnostic MBean 
	private static volatile HotSpotDiagnosticMXBean hotspotMBean;
	static void dumpHeap(String fileName, boolean live) {
	    // initialize hotspot diagnostic MBean
	    initHotspotMBean();
	    try {
	        hotspotMBean.dumpHeap(fileName, live);
	    } catch (RuntimeException re) {
	        throw re;
	    } catch (Exception exp) {
	        throw new RuntimeException(exp);
	    }
	}
	// initialize the hotspot diagnostic MBean field
	private static void initHotspotMBean() {
	    if (hotspotMBean == null) {
	        synchronized (heapDumpGeneration.class) {
	            if (hotspotMBean == null) {
	                hotspotMBean = getHotspotMBean();
	            }
	        }
	    }
	}
	// get the hotspot diagnostic MBean from the
	// platform MBean server
	private static HotSpotDiagnosticMXBean getHotspotMBean() {
	    try {
	        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
	        HotSpotDiagnosticMXBean bean = 
	            ManagementFactory.newPlatformMXBeanProxy(server,
	            HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
	        return bean;
	    } catch (RuntimeException re) {
	        throw re;
	    } catch (Exception exp) {
	        throw new RuntimeException(exp);
	    }
	}	
	// --- <<IS-END-SHARED>> ---
}

