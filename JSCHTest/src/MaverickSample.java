import java.io.IOException;

import com.maverick.events.Event;
import com.maverick.events.EventListener;
import com.maverick.events.EventServiceImplementation;
import com.maverick.events.J2SSHEventCodes;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh.SshException;
import com.maverick.ssh2.Ssh2Context;
import com.sshtools.net.SocketTransport;

public class MaverickSample {
	public static void main(String s[]) {
		try {
			@SuppressWarnings("deprecation")
			SshConnector con = SshConnector.createInstance();
			SftpLogger sftpLog = null;
			EventServiceImplementation.getInstance().addListener(sftpLog);
			final Ssh2Context ssh2Context = (Ssh2Context) con.getContext(SshConnector.SSH2);
			SocketTransport transport = new SocketTransport("test.rebex.net", 22);
			
			SshClient ssh = con.connect(transport, "demo", ssh2Context);
			

		} catch (SshException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class SftpLogger implements EventListener {
	public void processEvent(Event evt) {
		if (evt.getId() == 0 || evt.getId() == 1 || evt.getId() == 2) { // EVENT_HOSTKEY_RECEIVED
																		// ,EVENT_HOSTKEY_REJECTED,EVENT_HOSTKEY_ACCEPTED
			System.out.println(J2SSHEventCodes.messageCodes.get(evt.getId()));
		} else {
			System.out.println(J2SSHEventCodes.messageCodes.get(evt.getId()) + evt.getAllAttributes());
		}
	}
}
