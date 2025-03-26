import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nDurable;
import com.pcbsys.nirvana.client.nDurableAttributes;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nNameDoesNotExistException;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;

public class SaveEventData implements nEventListener {
	@SuppressWarnings("deprecation")
	public SaveEventData() throws Exception {

		String[] RNAME = { "nsp://127.0.0.1:9050" };

		nSessionAttributes nsa = new nSessionAttributes(RNAME);
		// nsa.setName("mysession");
		nSession mySession = nSessionFactory.create(nsa, System.getProperty("user", "miro"),
				System.getProperty("password", "miro"));
		mySession.init();

		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setName("wm/is/test/AnotherDoc");

		nChannel myChannel = mySession.findChannel(cattrib);
		myChannel.getChannelAttributes().getProtobufDescriptorNames();

		nDurableAttributes attr = nDurableAttributes.create(nDurableAttributes.nDurableType.Shared, "saveMessages");

		nDurable sharedDurable;
		try {

			sharedDurable = myChannel.getDurableManager().get(attr.getName());
		} catch (nNameDoesNotExistException ex) {
			sharedDurable = myChannel.getDurableManager().add(attr);

		}

		System.out.println("got here");
		myChannel.addSubscriber(this, sharedDurable, "", 100);
		byte[] protods = myChannel.getStoreAttributes().getProtobufDescriptorSets()[0];
		FileOutputStream f = new FileOutputStream(new File("/tmp/proto.ds"));
		f.write(protods);
		f.flush();

	}

	public void go(nConsumeEvent evt) {
		System.out.println(evt.getHeader().toString());
		System.out.println(evt.getEventTag());
		Enumeration names = evt.getAttributes().getAttributeNamesEnumeration();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			System.out.println(name + " value" + evt.getAttributes().getAttribute(name));
			
		}
		
		
		System.out.println("Consumedd event " + evt.getHeader().redeliveredCount);
		try {
			evt.ack();

			FileOutputStream f = new FileOutputStream(new File("/tmp/" + evt.getEventID() + ".bin"));
			f.write(evt.getEventData());
			f.flush();
			f.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			new SaveEventData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
