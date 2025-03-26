

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import com.pcbsys.foundation.utils.StringUtils;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.nAdminAPI.nContainer;
import com.pcbsys.nirvana.nAdminAPI.nDurableNode;
import com.pcbsys.nirvana.nAdminAPI.nLeafNode;
import com.pcbsys.nirvana.nAdminAPI.nNode;
import com.pcbsys.nirvana.nAdminAPI.nRealmNode;
import com.pcbsys.nirvana.nAdminAPI.nTopicNode;
// --- <<IS-END-IMPORTS>> ---

public final class MyJavaService

{
	// ---( internal utility methods )---

	final static MyJavaService _instance = new MyJavaService();

	static MyJavaService _newInstance() { return new MyJavaService(); }

	static MyJavaService _cast(Object o) { return (MyJavaService)o; }

	// ---( server methods )---




	public static final void Test (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(Test)>> ---
		// @sigtype java 3.5
		// [i] field:0:required realmURL
		// [i] field:0:required channelName
		// [i] field:0:required triggerName
		// [i] field:0:required userName
		// [i] field:0:required password
		// [i] field:0:required keyStorePass
		// [i] field:0:required keyStorePath
		// [i] field:0:required alias
		// [i] field:0:required trustStorePath
		// [i] field:0:required trustStorePass
		IDataCursor pipelineCursor = pipeline.getCursor();
		String rName = IDataUtil.getString(pipelineCursor, "realmURL");
		String cName = IDataUtil.getString(pipelineCursor, "channelName");
		String tName = IDataUtil.getString(pipelineCursor, "triggerName");
		String uName = IDataUtil.getString(pipelineCursor, "userName");
		String pWord = IDataUtil.getString(pipelineCursor, "password");
		String keyStorePath= IDataUtil.getString(pipelineCursor, "keyStorePath");
		String keyStorePass=IDataUtil.getString(pipelineCursor, "keyStorePass");
		String keyStoreAlias =IDataUtil.getString(pipelineCursor, "alias");
		String trustStorePath= IDataUtil.getString(pipelineCursor, "trustStorePath");
		String trustStorePass =IDataUtil.getString(pipelineCursor, "trustStorePass");
		if (!StringUtils.isEmpty(keyStorePath)) {
			System.getProperties().put("com.softwareag.um.client.ssl.keystore_path", keyStorePath);
		}
		if (!StringUtils.isEmpty(keyStoreAlias)) {
			System.getProperties().put("com.softwareag.um.client.ssl.certificate_alias", keyStoreAlias);
		}
		if (!StringUtils.isEmpty(keyStorePass)) {
			System.getProperties().put("com.softwareag.um.client.ssl.keystore_password", keyStorePass);
		}
		if (!StringUtils.isEmpty(trustStorePath)) {
			System.getProperties().put("com.softwareag.um.client.ssl.truststore_path", trustStorePath);
		}
		 
		if (!StringUtils.isEmpty(trustStorePass)) {
			System.getProperties().put("com.softwareag.um.client.ssl.truststore_password", trustStorePass);
		}
		 
		channels.clear();
		
		nSessionAttributes nsa = null;
		
		
		long queueLengthLong = -1;
		
		ArrayList < IData > list = new ArrayList < IData > ();
		
		// Create a session attributes object
		nRealmNode realm = null;
		/*
		 * try {
		 * 
		 * nsa = new nSessionAttributes(rName); } catch
		 * (com.pcbsys.nirvana.client.nIllegalArgumentException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * // Get the root realm node from the realm admin
		 */		
		try {
		
		    // If no basic authentication is used then use the following code
		    // nRealmNode realm = new nRealmNode(nsa);
		
		    // If basic authentication is used then use the following code
			 nsa = new nSessionAttributes(rName);
		    realm = new nRealmNode(nsa, uName, pWord);
		
		    System.out.println("got connected:" + String.valueOf(realm != null));
		
		
		    realm.waitForEntireNameSpace();
		    if (cName != null) {
		        nNode n = realm.findNode(cName);
		        channels.add(n);
		    } else {
		        searchNodes(realm);
		    }
		    int nChannels = realm.getNoOfChannels();
		
		    for (nNode n: channels) {
		
		        System.out.println(n.getName());
		
		        List < nDurableNode > l = ((nTopicNode) n).getDurableList();
		
		        try {
		            Thread.sleep(5000);// Wait for the API to gather all the data from the channel.
		        } catch (InterruptedException e) {
		            //  TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		        for (nDurableNode nNd: l) {
		
		            //if (nNd.getName().equals(tName))
		            //queueLengthLong = nNd.getDepth();
		
		            IData doc = IDataFactory.create();
		
		            IDataUtil.put(doc.getCursor(), "tName", nNd.getName());
		            IDataUtil.put(doc.getCursor(), "qLength", nNd.getDepth());
		
		            /////
		
		            Date readDate = new Date(nNd.getLastReadTime());
		            Date writeDate = new Date(nNd.getLastWriteTime());
		            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
		            format.setTimeZone(TimeZone.getDefault());
		            String formattedReadTime = format.format(readDate);
		            String formattedWriteTime = format.format(writeDate);
		
		            IDataUtil.put(doc.getCursor(), "qReadTime", formattedReadTime);
		            IDataUtil.put(doc.getCursor(), "qWriteTime", formattedWriteTime);
		
		
		            //////
		
		
		            //IDataUtil.put(doc.getCursor(), "qReadTime", nNd.getLastReadTime());
		            //IDataUtil.put(doc.getCursor(), "qWriteTime", nNd.getLastWriteTime());
		
		
		
		            list.add(doc);
		
		        }
		
		        IDataUtil.put(pipeline.getCursor(), "totalChannels", nChannels);
		
		        IData[] queueStats = list.toArray(new IData[list.size()]);
		        IDataUtil.put(pipeline.getCursor(), "queueStats", queueStats);
		    }
		    System.out.println("closing");
		} catch (Exception e) {
		    e.printStackTrace();
		
		} finally {
		    if (realm != null) {
		        realm.close();
		    }
		
		
		}
		//realm.getSession().close();		
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static ArrayList<nNode> channels = new ArrayList();
	public static void searchNodes(nContainer container) {
	
	  Enumeration children = container.getNodes();
	  while (children.hasMoreElements()) {
	    nNode child = (nNode)children.nextElement();
	    if (child instanceof nContainer) {
	      searchNodes((nContainer)child);
	    } else if (child instanceof nLeafNode) {
	      nLeafNode leaf = (nLeafNode)child;
	      if (leaf.isChannel()) {
	         channels.add(leaf);
	      } 
	      
	    }
	  }
	}
		
	// --- <<IS-END-SHARED>> ---
}

