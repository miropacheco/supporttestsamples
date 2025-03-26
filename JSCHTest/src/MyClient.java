import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class MyClient implements Runnable{
	static Socket s;
	public static void main(String ss[]) {
		try {
		
			s = new Socket("10.130.213.21",7788);
			Thread t = new Thread(new MyClient());
			t.start();			
			System.in.read();
			s.getInputStream().read();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(5000);
			s.close();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
