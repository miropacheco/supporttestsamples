import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.pcbsys.nirvana.base.nConstants;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nRealmUnreachableException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAlreadyInitialisedException;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;

public class ConnectAndWait {
	static int i = 0;

	public static void main(String s[]) throws IOException {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		ArrayList<nSession> myList = new ArrayList();

		for (i = 0; i < 2; i++) {

			Runnable worker = new Runnable() {

				public void run() {

					// TODO Auto-generated method stub
					String[] RNAME = { System.getProperty("rname", "nsps://localhost:9100") };
					nSessionAttributes nsa;
					nConstants.setReconnectInterval(100);
					// TODO Auely(true);
					// nConstants.setInitialConnectionTimeout(5);
					try {
						nsa = new nSessionAttributes(RNAME);
						nsa.setName("my session");
					    nSession mySession = nSessionFactory.create(nsa, "miro" + i, "miro" + i);
						//nSession mySession = nSessionFactory.create(nsa);

						mySession.init();
						myList.add(mySession);

					} catch (nRealmUnreachableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (nSecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (nSessionNotConnectedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (nSessionAlreadyInitialisedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (nIllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			executor.execute(worker);
		}
		try {
			System.out.print("got here");
			executor.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
