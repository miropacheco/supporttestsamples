

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
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.nAdminAPI.nContainer;
import com.pcbsys.nirvana.nAdminAPI.nDurableNode;
import com.pcbsys.nirvana.nAdminAPI.nLeafNode;
import com.pcbsys.nirvana.nAdminAPI.nNode;
import com.pcbsys.nirvana.nAdminAPI.nQueueNode;
import com.pcbsys.nirvana.nAdminAPI.nRealmNode;
import com.pcbsys.nirvana.nAdminAPI.nTopicNode;
// --- <<IS-END-IMPORTS>> ---

public final class Test

{
	// ---( internal utility methods )---

	final static Test _instance = new Test();

	static Test _newInstance() { return new Test(); }

	static Test _cast(Object o) { return (Test)o; }

	// ---( server methods )---




	public static final void testNSPS (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(testNSPS)>> ---
		// @sigtype java 3.5
		nRealmNode realm = null;
		try {
				
				
			    // If no basic authentication is used then use the following code
			    // nRealmNode realm = new nRealmNode(nsa);
			    // If basic authentication is used then use the following code
				nSessionAttributes nsa = null;
				 nsa = new nSessionAttributes("nsps://ce1y71lseapp003:9043");
			    realm = new nRealmNode(nsa, "N306240", "manage");
			    System.out.println("got connected:" + String.valueOf(realm != null));
			    realm.waitForEntireNameSpace();
			    Thread.sleep(5000);
					} catch (Exception e) {
					    e.printStackTrace();
					} finally {
					    if (realm != null) {
					        realm.close();
					    }
		
					}
			
		// --- <<IS-END>> ---

                
	}
}

