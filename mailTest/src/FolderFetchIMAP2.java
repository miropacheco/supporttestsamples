import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.imap.IMAPFolder;

public class FolderFetchIMAP2 {

	public static void main(String[] args) throws MessagingException, IOException {
		IMAPFolder folder = null;
		Store store = null;
		String subject = null;
		Flag flag = null;
		try {
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			props.put("mail.imap.ssl.enable", "true"); // required for Gmail
			//props.put("mail.imap.sasl.enable", "true");
			//props.put("mail.imap.sasl.mechanisms", "XOAUTH2");
			//props.put("mail.imap.auth.login.disable", "true");
			//props.put("mail.imap.auth.plain.disable", "true");
			props.put("mail.debug", "true");
			props.put("mail.debug.auth", "true");
			//System.out.println("test");
			Session session = Session.getStore(props, null);
			//session.setDebug(true);
			
			store = session.getStore("imap");
			store.connect("imap.googlemail.com", "claudemiro.santos70", "l3tmeG0!");
			//store.connect("imap.googlemail.com", "claudemiro.santos70", "ya29.a0AX9GBdWmTIooe4jmPZuOT0OJAjKex82_TcW6jDAT8ZU0HF7HSXek4RSBqDbVTNfsZaFtGp6lMRIBHf-ZQ7y2XyqiR-ls5Jr4AfrFUuzUyY0_IUuoziVUz8C4WOUAIIi1MW1zopaGukPmqM-iKjLz6E5wwxmiaCgYKAf0SARISFQHUCsbCsyQKKEKtenZFoXUhCOEm7w0163");
			
			//store.connect("outlook.office365.com", "claudemiro.santos50@outlook.com","EwB4A8l6BAAUkj1NuJYtTVha+Mogk+HEiPbQo04AAdD24ffEt4KzzAZnLI3tW1DpKLucj1qVbainxkLDNtyXcm4bA/ik8uKfVXEYb5yKVTiKOtpQSPWWwfceUzajs4DVwel40LZFBeO/FLUefMOpLaVnmiUEOV7BzsOomEiomDKMC3XH4Iu7yxCovTRvyepfhE59ehi1m16Z+U2A5Mqm/HXYVCbxd0wiYpb2zenDRrtN4o7tVS1yBj17irrvsvA/kV43pw+z5bu+xbrQTUwxA0hL30LySYQEAJqZ6PRzgRg05n8gdFVkNJGnZzcK2apRclYsicCJVfm89momnN/rUvPDwk456LxjMPqVvZlWCUY291lG4K7qfkzPm/yIvhIDZgAACNekhbhdY/KLSAKigl9bd9NY4qDZVEjud1gZzrWl9EJnLfcwVezc75VDtmScsuu1YBe/YrB3V2oNVRUXrG/ijU4qfBgwKq7yMDkc1I4Oq5x5ObilOxGzH1o+ondX6ht0sIoPIHJ916xsP4HCurHvBVQP4jSM6IILSXV5npZ9n+mIlKi8LZWbRaio3K/SBerXFHBxf7Hm5JqAkJxjYF8T+GmOFC6LqhW5m7l8tQsM7160pgS5wLJDpBqeZ8lWiqeyASMgbyfLhCxiwPl8kCs5Xct3n6JpdnXDJzySGAmQfNJSShrj+Nol/GzYIxpHvuBI7sGp1vTMg+p5nbsMnIacUIGz3NRwhYppImsYNy2TQt0vavFvHtd9splKJbzUTrij46GjO4ejY4tVR/O3P86LvWyzUu8EAPC0GOXuSlj3f6Drh5kifEbsQRfmoPsG1cGWcaITXumSMMgBILeO6oCG/HNK3/ZZdcTsXRcyJ5TE4BSzpIUJ9bvz47cpueNqUa3EeJfIbDqHojmec/CwNRJD27ETT8LTLFGw1208WUqYRztL+CqURWE0eNylco0SEwDwLgkgpBkZQnBcECpjBkVhdeYJ/KjbFENRjoWaNWNqBF3/HEK+cbkVdCVA1f4zNsdy79TOnmsKRKg/RPyglWjzWVCAnK1kEwVXcSn6ItlttsiA2rJvc4QVsOXb7C5c0YQPY8d16AC4F8XJFZ+JFiqKo8jpkf1F+jBawaLHZdJqlnCH+MqrYGL6ei2x+U+czD/vKMvPnheyyh/9ey11wEi4PROd0pMC");
			
			/*
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
			*/
			
			
			System.out.println("stop it");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}