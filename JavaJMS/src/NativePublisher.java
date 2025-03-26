import com.example.tutorial.AddressBookProtos.Person;
import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nIllegalChannelMode;
import com.pcbsys.nirvana.client.nMaxBufferSizeExceededException;
import com.pcbsys.nirvana.client.nProtobufEvent;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueueReaderContext;
import com.pcbsys.nirvana.client.nQueueSyncReader;
import com.pcbsys.nirvana.client.nRealmUnreachableException;
import com.pcbsys.nirvana.client.nRequestTimedOutException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAlreadyInitialisedException;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.client.nSessionPausedException;
import com.pcbsys.nirvana.client.nUnexpectedResponseException;
import com.pcbsys.nirvana.client.nUnknownRemoteRealmException;

class SyncReader implements nEventListener {

	@Override
	public void go(nConsumeEvent arg0) {
		System.out.println("got an event sync");
		System.out.println(arg0.getEventData().length==0);

	}

}

public class NativePublisher {

	static public void main(String s[]) throws nRealmUnreachableException, nSecurityException,
			nSessionNotConnectedException, nSessionAlreadyInitialisedException {

		String[] RNAME = { System.getProperty("RNAME", "nsp://127.0.0.1:9050") };

		try {
			nSessionAttributes nsa = new nSessionAttributes(RNAME);
			nsa.setName("my session");
			nSession mySession = nSessionFactory.create(nsa, "mironative", "miro");
			mySession.init();
			try {

				nChannelAttributes cattrib = new nChannelAttributes();
				cattrib.setName("MyQueue");
				// cattrib.setClusterWide(true);
				nQueue myQueue = mySession.findQueue(cattrib);
				// byte[] b = Files.readAllBytes(new File("c:/tmp/data233.bin").toPath());
				long i = 0;
				myQueue.createAsyncReader(new nQueueReaderContext(new SyncReader(), 10));

				/*nQueueSyncReader rsync = myQueue.createReader(new nQueueReaderContext(new nEventListener() {

					@Override
					public void go(nConsumeEvent arg0) {
						System.out.println("got an event reader");
						try {
							arg0.ack();
							Thread.sleep(500);
						} catch (Exception e) {

							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}));*/
				/*
				while (true) {
					nConsumeEvent event = rsync.pop();
					new SyncReader().go(event);
					Thread.sleep(5000);
					if (event == null) {
						break;
					}

				}
				*/

				while (true && i < 1) {
					// nConsumeEvent n = new nProtobufEvent(createMessage(),"Person");

					// nConsumeEvent n = new nConsumeEvent("",("test" +
					// i).concat(getStringWithLengthAndFilledWithCharacter(10000, '1')).getBytes());
					// nProtobufEvent n = new nProtobufEvent(b,"dispatch_testDoc");
					// nEventAttributes attrs = new nEventAttributes();
					// nEventProperties props = new nEventProperties();
					// props.put("ProtobufMessageType","dispatch_testDoc");
					// props.put("F1","B");
					// n.setProperties(props);
					// n.setSubscriberName("shared-durable");
					byte[] p = new byte[100];
					java.util.Arrays.fill(p, (byte) 'a');
					nConsumeEvent h = new nConsumeEvent("test".getBytes(), null);
					myQueue.push(h);
					Thread.sleep(1000);
					i++;
					// myChannel.publish(new nConsumeEvent("evt1",createMessage()));
				}
				// System.out.println("finished");
				// Thread.sleep(10000);
			} catch (nSessionPausedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (nUnexpectedResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (nRequestTimedOutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (nChannelNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (nUnknownRemoteRealmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (nIllegalChannelMode e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (nMaxBufferSizeExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (nIllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
