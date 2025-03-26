import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nDurable;
import com.pcbsys.nirvana.client.nDurableAttributes;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nEventProperties;
import com.pcbsys.nirvana.client.nNameDoesNotExistException;
import com.pcbsys.nirvana.client.nProtobufEvent;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;

import Pdt.ST_System_SIMON_adapters_mq_notifications_ntfy_CNTC_LOG_REQPublishDocumentOrBuilder;

public class PublishEventFile {
	@SuppressWarnings("deprecation")
	public PublishEventFile() throws Exception {

		String[] RNAME = { "nsp://localhost:9000" };

		nSessionAttributes nsa = new nSessionAttributes(RNAME);
		// nsa.setName("mysession");
		nSession mySession = nSessionFactory.create(nsa, System.getProperty("user", "miro"),
				System.getProperty("password", "miro"));
		mySession.init();

		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setName("wm/is/ST_System_SIMON/adapters/mq/notifications/ntfy_CNTC_LOG_REQPublishDocument");

		nChannel myChannel = mySession.findChannel(cattrib);
		myChannel.getChannelAttributes().getProtobufDescriptorNames();

		nDurableAttributes attr = nDurableAttributes.create(nDurableAttributes.nDurableType.Shared, "saveMessages");
		// FileInputStream fis = new FileInputStream("/users/clpa/downloads/7.bin");

		Pdt.ST_System_SIMON_adapters_mq_notifications_ntfy_CNTC_LOG_REQPublishDocument obj = Pdt.ST_System_SIMON_adapters_mq_notifications_ntfy_CNTC_LOG_REQPublishDocument
				.parseFrom(new FileInputStream("c:/tmp/7.bin"));
		System.out.println(obj.getClass().getName());
		nProtobufEvent evt = new nProtobufEvent(obj.toByteArray(), "ST_System_SIMON_adapters_mq_notifications_ntfy_CNTC_LOG_REQPublishDocument");
		 myChannel.publish(evt);
		// nEventProperties nprop = new nProtobufEvent();
		// nprop.put("ProtobufMessageType", "test_MyDocument");

		/*
		 * nConsumeEvent evt = new
		 * nConsumeEvent("wm:c840e1b0-1f64-11ee-8cc5-000000000009",nprop,fis.
		 * readAllBytes()); myChannel.publish(evt);
		 */
		/*
		 * 
		 * nDurable sharedDurable; try {
		 * 
		 * sharedDurable = myChannel.getDurableManager().get(attr.getName()); } catch
		 * (nNameDoesNotExistException ex) { sharedDurable =
		 * myChannel.getDurableManager().add(attr);
		 * 
		 * }
		 * 
		 * System.out.println("got here"); myChannel.addSubscriber(this, sharedDurable,
		 * "", 100); byte[] protods =
		 * myChannel.getStoreAttributes().getProtobufDescriptorSets()[0];
		 * FileOutputStream f = new FileOutputStream(new File("/tmp/proto.ds"));
		 * f.write(protods); f.flush();
		 */

	}

	public void go(nConsumeEvent evt) {
		System.out.println(evt.getHeader().toString());
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
			new PublishEventFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
