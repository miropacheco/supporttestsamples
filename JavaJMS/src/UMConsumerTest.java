import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nDurable;
import com.pcbsys.nirvana.client.nDurableAttributes;
import com.pcbsys.nirvana.client.nDurableAttributes.nDurableType;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;

class MySubscriber implements nEventListener {
	private String subname;

	public MySubscriber(String s) {
		subname = s;
	}

	@Override
	public void go(nConsumeEvent arg0) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Date timeNow = new Date();
		System.out.println("got an event" + arg0.getEventID() + "subscriber:" + subname);
		System.out.println("Publised time:" + new Date(arg0.getTimestamp()));
		System.out.println("Time now:" + timeNow);
		System.out.println("Difference in ms:" + String.valueOf(timeNow.getTime() - arg0.getTimestamp()));
		try {
			arg0.ack();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

public class UMConsumerTest {

	@SuppressWarnings("deprecation")
	public UMConsumerTest() throws Exception {
		// PrintStream o = new PrintStream(new File("c:/tmp/events.txt"));

		// Assign o to output stream
		// System.setOut(o);
		String host = System.getProperty("host", "nsp://localhost:9015");

		String[] RNAME = host.split(",");

		nSessionAttributes nsa = new nSessionAttributes(RNAME);
		// nsa.setName("mysession");

		nsa.setReconnectInterval(200);
		nsa.setReconnectImmediately(true);
		nSession mySession = nSessionFactory.create(nsa);

		mySession.init();

		nChannel ch = mySession
				.findChannel(new nChannelAttributes(System.getProperty("channel", "wm/is/TriggerTest/MyDocument")));

		nDurableAttributes durAttr = nDurableAttributes.create(nDurableType.Serial, "mysubscription");
		nDurable named = null;
		try {
			named = ch.getDurableManager().add(durAttr);

		} catch (com.pcbsys.nirvana.client.nNameAlreadyBoundException e) {
			named = ch.getDurableManager().get("mysubscription");
		}
		ch.addSubscriber(new MySubscriber("mysubscription"), named);

		// h.addSubscriber(new MySubscriber("MirosTest"), named);

	}

	public static void main(String[] args) {
		try {
			// System.in.read();
			ExecutorService executor = Executors.newFixedThreadPool(1);
			Runnable runnableTask = () -> {
				try {
					new UMConsumerTest();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
			for (int i = 0; i < 1; i++)
				executor.execute(runnableTask);
			System.out.println("Scheduled");
			executor.shutdown();
			executor.awaitTermination(10000, TimeUnit.DAYS);

			System.out.println("done");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
