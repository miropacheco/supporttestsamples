import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.nAdminAPI.nBaseAdminException;
import com.pcbsys.nirvana.nAdminAPI.nLeafNode;
import com.pcbsys.nirvana.nAdminAPI.nRealmNode;

public class GetQueueSize {
	public static void main(String s[]) {

		try {
			nSessionAttributes sessionAttributes;
			sessionAttributes = new nSessionAttributes("nsps://localhost:9767");
			sessionAttributes.setTruststore("c:/temp/testum.jks","manage");
			ExecutorService executor = Executors.newFixedThreadPool(1);
			
			nRealmNode RootNode;
	
			RootNode = new nRealmNode(sessionAttributes, "clpa", "test");
			RootNode.waitForEntireNameSpace();			
			nLeafNode q = (nLeafNode) RootNode.findNode("MyQueue");
			System.out.println(q.getCurrentNumberOfEvents());
			long totalEvents = q.getCurrentNumberOfEvents();
			
			 
			//nSessionFactory.shutdown();
			RootNode.close();

		} catch (nBaseAdminException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (nIllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
