import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.nAdminAPI.nClusterNode;
import com.pcbsys.nirvana.nAdminAPI.nRealmNode;

public class CheckNodeStatus {

	private static String host;
	private static String username;
	private static String password;

	private static void setRootNode(String host) throws Exception {
		nSessionAttributes sessionAttributes = new nSessionAttributes(host);

		// sessionAttributes.setRequestPriorityConnection(true);
		nRealmNode RootNode = new nRealmNode(sessionAttributes, username, password);
		RootNode.waitForEntireNameSpace(5000);
		
		while (true) {
			checkRealmStatus(RootNode);
			Thread.sleep(5000);
		}

	}

	private static void checkRealmStatus(nRealmNode node) {
		if ( !node.isConnected()) {
			System.out.println("Node is disconnected");
			return;
		}
		if (!node.isClustered()) {
			System.out.println("Returning online. Not clustered. " + node.getName());
			return;
		}
		nClusterNode cluster = node.getCluster();
		if (cluster == null) {
			System.out.println(
					"NirvanaRealmManager.getClusteredRuntimeStatus(), node is clustered but unable to get the cluster node, returning status ERROR for node.  "
							+ node

									.getName());
			System.out.println("Return error");
			return;
		}
		if (cluster.isOnline()) {
			nRealmNode masterNode = cluster.getMaster();
			if (masterNode != null && node.getName().equals(masterNode.getName())) {
				System.out.println(
						"NirvanaRealmManager.getClusteredRuntimeStatus(), returning status ONLINE_MASTER for clustered node "
								+ node

										.getName());
				
				return;
			}
			System.out.println("NirvanaRealmManager.getClusteredRuntimeStatus(), returning status ONLINE_SLAVE for clustered node "
							+ node

									.getName());
			return;
		}
		System.out.println(
				"NirvanaRealmManager.getClusteredRuntimeStatus(), returning status ERROR for clustered node " + node

						.getName() + " line 75");
	}

	public static void main(String obj[]) throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(1);

		executorService.execute(new Runnable() {
		    public void run() {
				host = System.getProperty("host", "nsp://localhost:9002");
				username = System.getProperty("user", "miro");
				password = System.getProperty("password", "");

				try {
					setRootNode(host);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});

		executorService = Executors.newFixedThreadPool(1);

		executorService.execute(new Runnable() {
		    public void run() {
				host = System.getProperty("host1", "nsp://localhost:9000");
				username = System.getProperty("user", "miro");
				password = System.getProperty("password", "");

				try {
					setRootNode(host);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
		executorService = Executors.newFixedThreadPool(1);

		executorService.execute(new Runnable() {
		    public void run() {
				host = System.getProperty("host2", "nsp://localhost:9003");
				username = System.getProperty("user", "miro");
				password = System.getProperty("password", "");
				try {
					setRootNode(host);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		

	}

}
