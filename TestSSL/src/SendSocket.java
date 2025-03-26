import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SendSocket {
	public static void  main(String s[]) {		
	    SSLSocketFactory factory =	    		
                (SSLSocketFactory)SSLSocketFactory.getDefault();
	    try {
	    	
			SSLSocket socket =
			        (SSLSocket)factory.createSocket("porttest-current.leaseaccelerator.com", 443);			
			socket.startHandshake();
			OutputStream os = socket.getOutputStream();
			FileInputStream fis= new FileInputStream("c:/temp/data.txt");
			byte[] b = new byte[50000];
			while (true) {
				int totBytes = fis.read(b);
				System.out.println(totBytes);
				os.write(b,0,totBytes);
				break;
			}
			fis.close();
			BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
			b = new byte[30000];
		    int totBytes = is.read(b);
		    String result = new String(b,0,totBytes);
		    System.out.println(result);
		    
			os.close();
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    

		
	}

}
