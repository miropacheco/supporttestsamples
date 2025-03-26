

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.nAdminAPI.*;
import com.softwareag.is.log.Log;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;
// --- <<IS-END-IMPORTS>> ---

public final class Monitoring

{
	// ---( internal utility methods )---

	final static Monitoring _instance = new Monitoring();

	static Monitoring _newInstance() { return new Monitoring(); }

	static Monitoring _cast(Object o) { return (Monitoring)o; }

	// ---( server methods )---




	public static final void getClientsFromUM_Test_1 (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getClientsFromUM_Test_1)>> ---
		// @sigtype java 3.5
		// [i] field:0:required realmURL
		// [i] field:0:required umUser
		// [i] field:0:required umKey
		// [i] field:0:required truststorePath
		// [i] field:0:required storeKey
		// [o] field:2:required clients
		IDataCursor pipelineCursor = pipeline.getCursor();
		realmURL = IDataUtil.getString( pipelineCursor, "realmURL" );
		umUser = IDataUtil.getString( pipelineCursor, "umUser" );
		umPassword = IDataUtil.getString( pipelineCursor, "umKey" );
		truststorePath = IDataUtil.getString( pipelineCursor, "truststorePath" );
		storePassword = IDataUtil.getString( pipelineCursor, "storeKey" );
		pipelineCursor.destroy();
		
		
		nSessionAttributes nsa;
		try {
			nsa = new nSessionAttributes(realmURL);
			nsa.setTruststore(truststorePath, storePassword);
			realmNode = createRealmNode(nsa);
		   
			//Invoke retrieval
			populateDurables();
		
			//Build output
			String[][] returnValue = new String[ channelDetails.size()][9];
				 
			int index11=0;
			for (Map<String,String> m:channelDetails) {
		
			// 	Make a string array of the keys of the hash and sort them
					String[] hashKeys = new String[ m.size() ];
					int index=0;
					Iterator<String> hashIterator = m.keySet().iterator();
					while(hashIterator.hasNext()) {
						hashKeys[index] = hashIterator.next();
						index++;
					}
					Arrays.sort(hashKeys);
			
					// Build the return values
						returnValue[index11][0] = m.get( hashKeys[8] );
						returnValue[index11][1] = m.get( hashKeys[3] );
						returnValue[index11][2] = m.get( hashKeys[0] );
						returnValue[index11][3] = m.get( hashKeys[4] );
						returnValue[index11][4] = m.get( hashKeys[2] );
						returnValue[index11][5] = m.get( hashKeys[5] );
						returnValue[index11][6] = m.get( hashKeys[1] );
						returnValue[index11][7] = m.get( hashKeys[7] );
						returnValue[index11][8] = m.get( hashKeys[6] );
						index11++;
		
		 } 
		 
		IDataCursor pipelineCursorA = pipeline.getCursor();
		 IDataUtil.put(pipelineCursorA, "clients", returnValue);
		 pipelineCursorA.destroy();
		 instanceCount=0;
		} catch (Exception e1) {
			e1.printStackTrace(); 
			Log.debug("----error----"+e1.toString());
		}
		 finally {
			channelDetails.clear();
		    close();	
		}    
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	 private static final long MAX_WAIT_TIME = 10;
	 private static final TimeUnit WAIT_TIME_UNIT = TimeUnit.SECONDS;
	 private static nRealmNode realmNode;
	 private static List<nDurableNode> durables;
	 private static Set<Map<String, String>> channelDetails = new HashSet<Map<String, String>>();
	 private static Map<String, String> triggerDetails ;
	 private static String realmURL;
	 private static String umUser;
	 private static String umPassword;
	 private static String truststorePath;
	 private static String storePassword;
	 private static int instanceCount = 0;
	 
	 
	 /**
	  * Get durables and sort by a chosen field
	  */
	 private static void populateDurables() throws Exception {
		// TODO Auto-generated method stub
	
	     nChannelAttributes[] ncas =com.pcbsys.nirvana.client.nSessionAttributes().getChannels();
	    
	     if (ncas.length == 0) {
	    	 Log.debug("No channels found on  server");
	
	    
	     }
	     for (nChannelAttributes nca : ncas){
	    	 if (nca.getChannelMode()==nChannelAttributes.CHANNEL_MODE) 
	    	 {
	         nTopicNode topicNode = (nTopicNode) realmNode.findNode(nca.getName());
	         if(topicNode.getDurableList().size()!=0)
	         	{
	        	 	durables = topicNode.getDurableList();
	        	 	for (nDurableNode node : durables)
	         // If we need to wait for the node to be updated add an observer.
	        	 		{
	        	 			if (node != null && node.getLastReadTime() == 0) 
	        	 				{
	        	 					node.addObserver(new DurableObserver());
	        	 				}
	        	 		}
	         	}
	         }
	     }
	
	     // Count down until all observers have found an update to the node
	     try {
	       if (!DurableObserver.await()) {
	         Log.debug("---!Durable Observer = wait ---"+DurableObserver.await());
	       }
	     } catch (InterruptedException ie) {
	    	ie.printStackTrace();
			Log.debug("----error----"+ie.toString());
	     } 
	     finally {
			close();
		}
	}
	 
	public static void close() throws Exception {
	   // Close the session we opened
	   if (realmNode != null) {
			realmNode.close();
	   }
	   // Exit successfully
	 }
	  
	
	private static nRealmNode createRealmNode(nSessionAttributes nsa) throws Exception {
		  nRealmNode realmNode = null;
	      try {
	    	  realmNode = new nRealmNode(nsa, umUser, umPassword);
	    	  realmNode.waitForEntireNameSpace(5000);
	      } catch (Exception ex) {
			ex.printStackTrace();
			Log.debug("----error----"+ex.toString());			  
	      }
		  finally {
		    //close zone members
	    	close();
		  }
	      return realmNode;
	}
	  
	
	private static class DurableObserver implements Observer {
	
	    // Allow main thread to signal when it is ready
	    private static final CountDownLatch startSignal = new CountDownLatch(1);
	    private static CountDownLatch doneSignal;
	    //private static int instanceCount = 0;
	
	    DurableObserver() {
	      ++instanceCount;
	    }
	
	    // Called by the main thread when ready to wait for nodes to be populated
	    static boolean await() throws InterruptedException {
	      doneSignal = new CountDownLatch(instanceCount);
	      startSignal.countDown();
	      return doneSignal.await(MAX_WAIT_TIME, WAIT_TIME_UNIT);
	    }
	
	    @Override
	    public void update(Observable o, Object arg) {
	      try {
	        startSignal.await();
	      } catch (Exception ignored) {
	      }
	
	      nDurableNode node = (nDurableNode) o;
	      String channelName = node.getParent().getAbsolutePath();
	      String triggerName=node.getAliasName();
	    
	      String depth = Long.toString(node.getDepth());
	      
	      String depthTX = Long.toString(node.getTransactionDepth());
	      String storeSize = Long.toString(node.getStoreSize());
	      String lastEID = Long.toString(node.getLastEventId());
	      
	      long lastReadEpoch = ((node.getLastReadTime()));
	      Instant date = Instant.ofEpochMilli ( lastReadEpoch );	
	 		 ZonedDateTime zdt = ZonedDateTime.ofInstant ( date , ZoneOffset.systemDefault() );
	 		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "EEE MMM dd HH:mm:ss zzz yyyy" );
	 		 String lastRead = formatter.format ( zdt );
	      
	      long lastWriteEpoch = (( node.getLastWriteTime()));
	      Instant date_1 = Instant.ofEpochMilli ( lastWriteEpoch );	
	 		 ZonedDateTime zdt_1 = ZonedDateTime.ofInstant ( date_1 , ZoneOffset.systemDefault() );
	 		 DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern ( "EEE MMM dd HH:mm:ss zzz yyyy" );
	 		 String lastWrite = formatter_1.format ( zdt_1 );
	
	      triggerDetails= new HashMap<String, String>();
	      triggerDetails.put("triggerName",triggerName);
	      triggerDetails.put("depth",depth);
	      triggerDetails.put("clientGroup","IntegrationServer");
	      triggerDetails.put("isConnected","true");
	      triggerDetails.put("appName","IntegrationServer");
	      triggerDetails.put("isLocked","false");
	      //triggerDetails.put("storeSize",storeSize);
	      //triggerDetails.put("lastEID",lastEID);
	      triggerDetails.put("channelName",channelName);
	      triggerDetails.put("lastWrite",lastWrite);
	      triggerDetails.put("lastRead",lastRead);
	      channelDetails.add(triggerDetails);
	
	      if (node.getLastReadTime() != 0) {
	        node.deleteObserver(this);
	        doneSignal.countDown();
	      }
	      }
	  
	
	// --- <<IS-END-SHARED-SOURCE-AREA>> ---
	
	/**
	 * The service implementations given below are read-only and show only the
	 * method definitions and not the complete implementation.
	 */
	public static final void getClientsFromUM_test(IData pipeline) throws ServiceException {
	}
	public static final void test(IData pipeline) throws ServiceException {
	}
	}
	
	// --- <<IS-BEGIN-SHARED-SOURCE-AREA>> ---
	
		
	// --- <<IS-END-SHARED>> ---
}

