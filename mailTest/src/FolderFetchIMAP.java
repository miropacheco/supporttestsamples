import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.*;
import javax.mail.search.FlagTerm;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;

public class FolderFetchIMAP {

	public static void main(String[] args) throws MessagingException, IOException {
		IMAPFolder folder = null;
		Store store = null;
		String subject = null;
		Flag flag = null;
		try {
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			//System.out.println("test");
			Session session = Session.getDefaultInstance(props, null);
			//session.setDebug(true);
			
			store = session.getStore("imaps");
			store.connect("imap.googlemail.com", "claudemiro.santos70", "l3tmeG0!");
			//store.connect("outlook.office365.com", "claudemiro.santos70", "l3tmeG0!!"); //letmeg0!!
			
			
			folder = (IMAPFolder)  store.getFolder("Inbox");

			if (!folder.isOpen())	
				folder.open(Folder.READ_WRITE);
			UIDFolder ufolder = (UIDFolder) folder;
			for (int k=1;k <= folder.getMessageCount(); k++) {
				folder.getMessage(k).setFlag(Flag.FLAGGED, false);				
			}
			Message[] messages = folder.search(
				        new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			System.out.println(ufolder.getUIDNext());
			 for ( Message message : messages ) {
			      System.out.println( 
			          "sendDate: " + message.getSentDate()
			          + " subject:" + message.getSubject() );
			    }
			folder.close();
			
			System.out.println("stop it");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}