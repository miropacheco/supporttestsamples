import java.lang.management.ManagementFactory;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nIllegalChannelMode;
import com.pcbsys.nirvana.client.nMaxBufferSizeExceededException;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nRealmUnreachableException;
import com.pcbsys.nirvana.client.nRequestTimedOutException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAlreadyInitialisedException;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.client.nSessionPausedException;
import com.pcbsys.nirvana.client.nTransaction;
import com.pcbsys.nirvana.client.nTransactionAttributes;
import com.pcbsys.nirvana.client.nTransactionFactory;
import com.pcbsys.nirvana.client.nUnexpectedResponseException;
import com.pcbsys.nirvana.client.nUnknownRemoteRealmException;

public class SimplePub {

	static nSessionAttributes nsa;
	private static nSession mySession;

	public void doIt() {

		long startTime = System.nanoTime();
		byte[] p = new byte[100];
		nConsumeEvent h = new nConsumeEvent("test".getBytes(), p);
		nChannelAttributes cattrib = new nChannelAttributes();
		try {
			cattrib.setName("testACL");
		} catch (nIllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		nQueue myQueue;
		try {
			myQueue = mySession.findQueue(cattrib);
			System.out.println(myQueue.push(h));
			
		} catch (nChannelNotFoundException | nSessionPausedException | nUnknownRemoteRealmException | nSecurityException
				| nSessionNotConnectedException | nIllegalArgumentException | nUnexpectedResponseException
				| nRequestTimedOutException | nIllegalChannelMode e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (nMaxBufferSizeExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		java.util.Arrays.fill(p, (byte) 'a');

		try {

			Vector messages = new Vector();
			messages.addElement(h);
			nTransactionAttributes tattrib = new nTransactionAttributes(myQueue);
			nTransaction myTransaction = nTransactionFactory.create(tattrib);
			myTransaction.publish(messages);
			myTransaction.commit();
		} catch (nSecurityException e) {
			e.printStackTrace();
		}

		if ((System.nanoTime() - startTime) / 1000000 > 100)
			System.out.println("Total time on thread:" + Thread.currentThread().getId() + "="
					+ (System.nanoTime() - startTime) / 1000000);

	}

	public static void main(String[] args) throws nIllegalArgumentException, nRealmUnreachableException,
			nSecurityException, nSessionNotConnectedException, nSessionAlreadyInitialisedException {
		int TOTALREQUESTS = 1;
		int TOTALTHEADS = 1;
		String[] RNAME = { System.getProperty("RNAME", "nsp://127.0.0.1:9050") };

		nsa = new nSessionAttributes(RNAME);

		nsa.setName("my session");
		mySession = nSessionFactory.create(nsa, "Administrator", "manage");

		mySession.init();

		System.out.println("PID of this process:" + ManagementFactory.getRuntimeMXBean().getName());
		ExecutorService exec = Executors.newFixedThreadPool(TOTALTHEADS);
		try {
			while (true) {

				for (int i = 0; i < TOTALREQUESTS; i++) {

					Runnable worker = new Runnable() {

						public void run() {
							// TODO Auto-generated method stub
							new SimplePub().doIt();

						}

					};
					exec.execute(worker);
				}

				try {
					Thread.sleep(1000);
					break;

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}

			}
			exec.shutdown();
			while (!exec.isTerminated())

			{

			}
			System.out.println("\nFinished all threads");

		} catch (Exception e) {
			System.out.println("Got Exepcption in main");

		}
	}
}