import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nDurable;
import com.pcbsys.nirvana.client.nDurableAttributes;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nNamedObject;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nDurableAttributes.nDurableType;

public class NativeConsumerOld implements nEventListener {

	@SuppressWarnings("deprecation")
	public void doIt() throws Exception {
		// PrintStream o = new PrintStream(new File("c:/tmp/events.txt"));

		// Assign o to output stream
		// System.setOut(o);

		String[] RNAME = { "nsp://localhost:9000" };

		nSessionAttributes nsa = new nSessionAttributes(RNAME);
		// nsa.setName("mysession");
		nSession mySession = nSessionFactory.create(nsa, System.getProperty("user", "test"),
				System.getProperty("password", "manage"));

		mySession.init();
		// mySession.enableThreading(20);

		mySession.enableThreading(false);
		mySession.enableThreading(10);

		System.out.println(mySession.getThreadPoolSize());
		// session.enableThreading(true); // this has no effect at this point

		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setName("docEsr");
		// cattrib.setName("test");
		// cattrib.setName("mychannel1");
		nChannel myChannel = mySession.findChannel(cattrib);

		myChannel.getChannelAttributes().getProtobufDescriptorNames();
		nDurable named = null;
		nDurableAttributes durAttr = nDurableAttributes.create(nDurableType.Named, "test");
		named = null;
		try {
			named = myChannel.getDurableManager().add(durAttr);

		} catch (com.pcbsys.nirvana.client.nNameAlreadyBoundException e) {
			named = myChannel.getDurableManager().get("test");
		}
		myChannel.addSubscriber(this, named);

	}

	public void go(nConsumeEvent evt) {
		// System.out.println(evt.getHeader().toString());
		// ((nProtobufEvent) evt)
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(evt.getTimestamp());
		SimpleDateFormat sdf = new SimpleDateFormat("hh:m:ss MM/dd/yyyy");
		Long tId = Thread.currentThread().getId();

		// System.out.println(sdf.format(cal.getTime()));
		// System.out.println("Consumed event " + evt.getEventID());
		// System.out.println("Consumed event " + evt.getHeader().redeliveredCount);
		Pdt.CcEsrV5_canonical_docEsr evtData = null;
		try {
			// Thread.sleep(100);
			evtData = null;

			evtData = Pdt.CcEsrV5_canonical_docEsr.parseFrom(evt.getEventData());

	

			FileOutputStream f = new FileOutputStream(new File("C:/tmp/data/" + evt.getEventID() + ".bin"),true);
			f.write(evt.getEventData());
			f.flush();
			f.close();
			evt.ack();
			System.out.println("parsing event:" + evt.getEventID());
			f = new FileOutputStream(new File("C:/tmp/data/data" + evt.getEventID() + ".json"),true);
			f.write((evtData.toString()).getBytes());
			f.close();

			// System.out.println(new String(evt.getEventData()));
		} catch (InvalidProtocolBufferException ee) {

			evtData = (Pdt.CcEsrV5_canonical_docEsr) ee.getUnfinishedMessage();
			System.out.println("error parsing event:" + evt.getEventID());
			try {
				evt.rollback();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public static void main(String[] args) {
		try {
			new NativeConsumerOld().doIt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
