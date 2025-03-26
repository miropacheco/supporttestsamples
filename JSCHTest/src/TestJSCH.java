import java.io.FileInputStream;
import java.io.IOException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class TestJSCH {
	ChannelSftp sftpChannel;

	public static void main(String s[]) throws JSchException, IOException {
		new TestJSCH().doIt();
	}

	public void doIt() throws JSchException, IOException {
		;

		JSch.setLogger(new MyLoggerSSH());

		System.out.println("Running with properties:");

		System.out.println("Host:" + System.getProperty("host"));
		System.out.println("User:" + System.getProperty("user"));
		System.out.println("Password:" + System.getProperty("password"));
		System.out.println("Direcory:" + System.getProperty("directory"));

		// here

		JSch jsch = new JSch();
		jsch.addIdentity(System.getProperty("key","/tmp/rsa/test2"),System.getProperty("password","manage"));
		Session session = null;
		try {
			session = jsch.getSession(System.getProperty("user", "miro"), System.getProperty("host", "wisftp1t.fenoc.corp"),
					22);
			
			// session = jsch.getSession("root","<host>",22);
			session.setConfig("StrictHostKeyChecking", "true");
			//session.setPassword(System.getProperty("password", "manage1"));
			// session.setConfig("PreferredAuthentications", "password");
			session.connect();
			System.out.println(session.getHostKey().getFingerPrint(jsch));

		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // change

		// change it here.

		Channel channel = session.openChannel("sftp");
		ChannelSftp sftpChannel = (ChannelSftp) channel;
		channel.connect();

		FileInputStream fs = new FileInputStream("/tmp/miro.jks");

		try {
			sftpChannel.put(fs, "./outfile.jks", ChannelSftp.APPEND);
		} catch (SftpException e) { // TODO Auto-generated catch block e.printStackTrace(); }

			channel.disconnect();
			session.disconnect();
			System.out.println("done sending files");

		}

	}
}
class MyLoggerSSH implements Logger {
	static java.util.Hashtable name = new java.util.Hashtable();
	static {
		name.put(new Integer(DEBUG), "DEBUG: ");
		name.put(new Integer(INFO), "INFO: ");
		name.put(new Integer(WARN), "WARN: ");
		name.put(new Integer(ERROR), "ERROR: ");
		name.put(new Integer(FATAL), "FATAL: ");
	}

	@Override
	public boolean isEnabled(int arg0) {
		// TODO Auto-generated method stub

		return true;
		/*
		 * if (arg0 <= 0) return true; else return false;
		 */
	}

	@Override
	public void log(int level, String message) {
		// TODO Auto-generated method stub
		System.err.print(name.get(new Integer(level)));
		System.err.println(message);

	}

}
