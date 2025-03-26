import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import com.pcbsys.nirvana.base.events.nEvent;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueueAsyncReader;
import com.pcbsys.nirvana.client.nQueueAsyncTransactionReader;
import com.pcbsys.nirvana.client.nQueueReaderContext;
import com.pcbsys.nirvana.client.nQueueSyncReader;
import com.pcbsys.nirvana.client.nQueueSyncTransactionReader;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;

public class QueueTest implements nEventListener {

	nQueueAsyncTransactionReader reader = null;
	nQueue myQueue = null;
	@SuppressWarnings("unchecked")
	ConcurrentHashMap<Long, nConsumeEvent> events = new ConcurrentHashMap<Long, nConsumeEvent>();
	int rolledback = 0;
	int committed = 0;
	int delivered = 0;
	int pending = 0;

	public QueueTest() throws Exception {
		// construct your session and queue objects here
		String realms = System.getProperty("realm", "nsp://localhost:9005");
		String[] RNAME = realms.split(",");
		nSessionAttributes nsa = new nSessionAttributes(RNAME);
		nSession mySession = nSessionFactory.create(nsa);
		mySession.init();
		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setName(System.getProperty("queue", "myqueue"));
		nQueue myQueue = mySession.findQueue(cattrib);
		// construct the queue reader

		nQueueReaderContext ctx = new nQueueReaderContext(this, 3);

		// reader = myQueue.createTransactionalReader(ctx);

		// reader = myQueue.createAsyncReader(ctx);
		reader = myQueue.createAsyncTransactionalReader(ctx);
		System.out.println("Starting event thread");

		// reader = myQueue.createReader(ctx);

	}
	public void cleanOlderEvents(nConsumeEvent evt) {
		events.entrySet().removeIf(entry -> entry.getValue().getEventID()<=evt.getEventID());
	}
	public static <K, V> long count(ConcurrentHashMap<K, V> map, Predicate<V> predicate) {
        return map.values().stream().filter(predicate).count();
    }

	public void go(nConsumeEvent event) {
		try {
			System.out.println("Consumed event " + event.getEventID());
			delivered++;
			Predicate<nConsumeEvent> olderEvents = old -> old.getEventID() <= event.getEventID();
			 char[] arr = new char[2];
		    Scanner in = new Scanner(System.in);
		    while (System.in.available() > 0) {
		        in.nextLine();         
		    }
		    arr = in.nextLine().toCharArray();
		    
			if (arr[0] == 'C') {
				event.ack();
			     System.out.println("Comitting event" + event.getEventID());
				
				event.ack(); // ack all of the previous events. Set pending to 0
					
					
				committed+= (int) (count(events, olderEvents) + 1);
				cleanOlderEvents(event);
				;
				pending= this.events.keySet().size();
			}
			else if (arr[0]=='R') {
				System.out.println("Rolling back event - It will get sent back to the client" + event.getEventID());
				event.rollback();
				events.remove(event.getEventID());
				
				rolledback += 1;
				//cleanOlderEvents(event);
				//pending= this.events.keySet().size();
			}
			else  {
				System.out.println("This event is pending" + event.getEventID());
				pending ++;
				//events.put(event.getEventID(), event);
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		System.out.println("Consumed event " + event.getEventID());
		delivered++;
		Predicate<nConsumeEvent> olderEvents = old -> old.getEventID() <= event.getEventID();
		try {
			double r = Math.random();

			if (r < 0.2) {
				System.out.println("Rolling back event - It will get sent back to the client" + event.getEventID());
				event.rollback();
				
				rolledback += (int) (count(this.events, olderEvents) +1);
				cleanOlderEvents(event);
				pending= this.events.keySet().size();
				
			} else if (r > 0.8) {
				System.out.println("Comitting event" + event.getEventID());
				
				event.ack(); // ack all of the previous events. Set pending to 0
					
					
				committed+= (int) (count(events, olderEvents) + 1);
				cleanOlderEvents(event);
				;
				pending= this.events.keySet().size();
			
			} else {
				System.out.println("This event is pending" + event.getEventID());
				pending ++;
				events.put(event.getEventID(), event);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	public static void main(String[] args) {
		try {
			QueueTest sqr = new QueueTest();
			// sqr.start();
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {

						try {
							Thread.currentThread().sleep(30000);
							System.out.printf("commited %d delivered %d rolledback %d pending %d\n", sqr.committed,
									sqr.delivered, sqr.rolledback, sqr.pending);
							if (sqr.events.size() > 20) {
								Collection<nConsumeEvent> c = sqr.events.values();
								for(nConsumeEvent e: c) {
									e.ack();
								}
							}
							
							
						}
						 catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
			t.run();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
