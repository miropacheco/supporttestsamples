import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;

public class TestKey {
	ChannelSftp sftpChannel;

	public static void main(String s[]) throws JSchException, IOException {
		new TestKey().doIt();
	}


	public void doIt() throws JSchException, IOException {
		;

		JSch.setLogger(new MyLogger());
				
		System.out.println("Running with properties:");
		
		System.out.println("Host:" + System.getProperty("host"));
		System.out.println("User:" + System.getProperty("user"));
		System.out.println("Password:" + System.getProperty("password"));
		System.out.println("Direcory:" + System.getProperty("directory"));

		// here


		System.out.println("Getting connected");

		System.out.println("connected successfully");

		JSch jsch = new JSch();
		//jsch.addIdentity(System.getProperty("keyPath"),System.getProperty("password"));
		Session session = null;
		try {
			session = jsch.getSession(System.getProperty("user", "root"), System.getProperty("host"), 22);
			//session = jsch.getSession("root","<host>",22);
			session.setConfig("StrictHostKeyChecking", "false");
			session.setPassword(System.getProperty("password", "manage"));
			session.setConfig("PreferredAuthentications","password");			
			session.connect();

		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // change

		 
		// change it here.
		final File folder = new File(System.getProperty("directory"));
		File[] listFiles = folder.listFiles();
		Channel channel = session.openChannel("sftp");
		ChannelSftp sftpChannel = (ChannelSftp) channel;
		channel.connect();

		/*File outFile = new File(System.getProperty("directory")+ "/outputLocal.txt")7;
		for (File file : listFiles) {
			// TODO Auto-generated method stub
			FileInputStream fs;
			try {
				System.out.println("Sending file:" + file.getName());

				fs = new FileInputStream(file);
				localAppend(outFile, file);
				sftpChannel.put(fs, "./output.txt", ChannelSftp.APPEND);

			} catch (FileNotFoundException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		*/
		channel.disconnect();
		session.disconnect();
		System.out.println("done sending files");


	}


	private void localAppend(File outFile, File fs) throws IOException {
		// TODO Auto-generated method stub
		byte[] fc = FileUtils.readFileToByteArray(fs);
		
		FileUtils.writeByteArrayToFile(outFile, fc, true);
	}

}

class MyLogger implements Logger {
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
