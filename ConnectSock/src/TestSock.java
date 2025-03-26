import java.io.IOException;
import java.net.Socket;


public class TestSock {
	public static void main(String s[]) throws InterruptedException {
		try {
			Socket client = new Socket("localhost",7878);			
		    Thread.sleep(10000);// just giving some time 
			//client.getOutputStream().write("QUIT\n".getBytes()); // comment this out to see the issue 
			client.getOutputStream().flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
	}

}
