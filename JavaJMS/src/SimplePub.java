import java.lang.management.ManagementFactory;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nDurable;
import com.pcbsys.nirvana.client.nDurableAttributes;
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
import com.pcbsys.nirvana.client.nDurableAttributes.nDurableType;

public class SimplePub {

	static nSessionAttributes nsa;
	private static nSession mySession;

	public void doIt() {

		byte[] p = new byte[10000];
		nChannelAttributes cattrib = new nChannelAttributes();
		try {
			cattrib.setName("TestChannel");
		} catch (nIllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		nChannel myChannel;
		try {
			myChannel = mySession.findChannel(cattrib);
			
			myChannel.publish(new nConsumeEvent("eventTag".getBytes(), p));

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

	}

	public static void main(String[] args) throws nIllegalArgumentException, nRealmUnreachableException,
			nSecurityException, nSessionNotConnectedException, nSessionAlreadyInitialisedException {
		int TOTALREQUESTS = 25000;
		int TOTALTHEADS = 1;
		String[] RNAME = { System.getProperty("RNAME", "nsp://127.0.0.1:9005") };

		nsa = new nSessionAttributes(RNAME);

		nsa.setName("my session");
		mySession = nSessionFactory.create(nsa, "Administrator", "manage");

		mySession.init();
		nChannelAttributes cattrib = new nChannelAttributes();
		try {
			cattrib.setName("TestChannel");
		} catch (nIllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		nChannel myChannel;
		try {
			myChannel = mySession.findChannel(cattrib);
			nDurableAttributes durAttr = nDurableAttributes.create(nDurableType.Serial, "mysubscription");
			nDurable named = null;
			try {
				named = myChannel.getDurableManager().add(durAttr);

			} catch (com.pcbsys.nirvana.client.nNameAlreadyBoundException e) {
				named = myChannel.getDurableManager().get("mysubscription");
			}
			

			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		

		System.out.println("PID of this process:" + ManagementFactory.getRuntimeMXBean().getName());
		ExecutorService exec = Executors.newFixedThreadPool(TOTALTHEADS);
		try {
			while (true) {

				for (int i = 0; i < TOTALTHEADS; i++) {

					Runnable worker = new Runnable() {

						public void run() {
							long startTime = System.nanoTime();

							for (int k = 0; k < TOTALREQUESTS; k++) {
								// TODO Auto-generated method stub
								new SimplePub().doIt();
							}

							System.out.println("Total time on thread:" + Thread.currentThread().getId() + "="
									+ (System.nanoTime() - startTime) / 1000000);

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