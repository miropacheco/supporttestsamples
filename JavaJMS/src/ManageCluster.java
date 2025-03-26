import com.pcbsys.foundation.logger.storelogger.StoreLogConfigLevel;
import com.pcbsys.nirvana.client.nBaseClientException;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nRequestTimedOutException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.nAdminAPI.nAdminIllegalArgumentException;
import com.pcbsys.nirvana.nAdminAPI.nBaseAdminException;
import com.pcbsys.nirvana.nAdminAPI.nClusterMemberConfiguration;
import com.pcbsys.nirvana.nAdminAPI.nClusterNode;
import com.pcbsys.nirvana.nAdminAPI.nConfigGroup;
import com.pcbsys.nirvana.nAdminAPI.nConfigurationException;
import com.pcbsys.nirvana.nAdminAPI.nRealmNode;

public class ManageCluster {
	public static void main(String s[]) throws nIllegalArgumentException, nBaseAdminException {
		
		/*String[] RNAME= {"nsps://127.0.0.1:10410", "nsps://127.0.0.1:10411","nsps://127.0.0.1:10412"};
		nRealmNode realms[] = new nRealmNode[RNAME.length];
		nClusterMemberConfiguration[] config = new nClusterMemberConfiguration[RNAME.length];
		for (int x = 0; x < RNAME.length; x++) {
		  // you don't have to create the realm nodes
		  // here, since the member configuration will create
		  // them for you from the RNAME values
		  realms[x] = new nRealmNode(new nSessionAttributes(RNAME[x]));
		  config[x]=new nClusterMemberConfiguration(realms[x], true);
		}
		nClusterNode cluster = nClusterNode.create("cluster1", config);
		*/
	nRealmNode  realmNode = new nRealmNode(new nSessionAttributes("nsp://localhost:9000"));
	 
	// get trace logging config group
	nConfigGroup logConfGrp = realmNode.getConfigGroup( "Trace Logging Config");
	 
	// configure log level to info
	logConfGrp.find("TraceStoreLogLevel").setValue(
	   String.valueOf(StoreLogConfigLevel.TRACE.ordinal()));
	// configure the maximum size trace.log file can reach in MB
	logConfGrp.find("TraceStoreLogSize").setValue(Integer.toString(10));
	// configure the maximum size the trace folder can reach in MB
	logConfGrp.find("TraceFolderLogSize").setValue(Integer.toString(1024));
	// enable trace logger for all stores
	//logConfGrp.find("TraceStores").setValue("wm.isserialTest.pubDocument1");
	logConfGrp.find("TraceStores").setValue("wm/is/serialTest/pubDocument1");
	//logConfGrp.find("TraceStores").setValue("*");
	 
	// save configuration
	realmNode.commitConfig(logConfGrp);
	realmNode.close();
	}
}