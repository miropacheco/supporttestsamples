
import java.io.IOException;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nIllegalChannelMode;
import com.pcbsys.nirvana.client.nRealmUnreachableException;
import com.pcbsys.nirvana.client.nRequestTimedOutException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSessionAlreadyInitialisedException;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.client.nSessionPausedException;
import com.pcbsys.nirvana.client.nUnexpectedResponseException;
import com.pcbsys.nirvana.client.nUnknownRemoteRealmException;
import com.pcbsys.nirvana.nAdminAPI.nAdminIllegalArgumentException;
import com.pcbsys.nirvana.nAdminAPI.nBaseAdminException;
import com.pcbsys.nirvana.nAdminAPI.nContainer;
import com.pcbsys.nirvana.nAdminAPI.nDurableNode;
import com.pcbsys.nirvana.nAdminAPI.nLeafNode;
import com.pcbsys.nirvana.nAdminAPI.nQueueNode;
import com.pcbsys.nirvana.nAdminAPI.nRealmNode;
import com.pcbsys.nirvana.nAdminAPI.nTopicNode;


public class ConnectForAdmin {

	private static String host;
	private static String username;
	private static String password;

	private static void setRootNode() throws IOException, nBaseAdminException, nIllegalArgumentException,
			InterruptedException, nChannelNotFoundException, nSessionPausedException, nUnknownRemoteRealmException,
			nSecurityException, nSessionNotConnectedException, nUnexpectedResponseException, nRequestTimedOutException,
			nIllegalChannelMode {
		
		
		
		nSessionAttributes sessionAttributes = new nSessionAttributes(host);
		//System.out.println(InetAddress.getByName("10.248.75.194").getHostName());

		// sessionAttributes.setRequestPriorityConnection(true);
		nRealmNode RootNode = new nRealmNode(sessionAttributes, username, password);
		System.out.println(RootNode.getName());
		System.out.println(RootNode.getAliasName());
		System.out.println("ID" + RootNode.getSession().getRemoteId());
	    RootNode.getTotalPublished();
	    RootNode.getTotalSubscribed();
	    nQueueNode nQueue = (nQueueNode) RootNode.findNode("myqueue");
	    System.out.println(nQueue.getDepth());
	    
		
		RootNode.waitForEntireNameSpace();
		RootNode.addObserver(new myObserver());
		
		Enumeration e = RootNode.getNodes();		
		while (e.hasMoreElements()) {
			Object o = e.nextElement();		
			if (o instanceof nTopicNode) {
				System.out.println(((nTopicNode) o).getName());
			}
			if (o instanceof nContainer) {
				((nContainer) o).getNodes(); 			}
			System.out.println(e.getClass().getName());
		}
		
						
		
				

	}
	public static void main(String obj[]) throws nIllegalArgumentException, nRealmUnreachableException,
			nSecurityException, nSessionNotConnectedException, nSessionAlreadyInitialisedException, IOException,
			nBaseAdminException, InterruptedException, nChannelNotFoundException, nSessionPausedException,
			nUnknownRemoteRealmException, nUnexpectedResponseException, nRequestTimedOutException, nIllegalChannelMode {
		host = System.getProperty("host", "nsp://localhost:9001");
		username = System.getProperty("user", "Administrator");
		password = System.getProperty("password", "manage");

		setRootNode();
	
	}

}
class myObserver implements Observer {

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (arg0 instanceof nDurableNode) {
			System.out.println("babab"+((nDurableNode) arg0).getDepth());
			System.out.println(((nDurableNode) arg0).getTransactionDepth());
		}
		 if (arg0 instanceof nContainer) {
			    if (arg1 instanceof nLeafNode) {
			      nLeafNode leaf = (nLeafNode)arg1;
			      nContainer cont = (nContainer)arg0;
			      try {
					if (cont.findNode(leaf) == null) {
					    // node has been deleted
					    System.out.println("Node "+leaf.getName()+" removed");
					  } else {
					    // node has been added
					    System.out.println("Node "+leaf.getName()+" added");
					  }
				} catch (nAdminIllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    }
			  }
			 
		
		
	}
	
	
}
