import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class PingClient {
	public static void main(String ss[]) {
		try {
			String host = System.getProperty("host","localhost");
			String port = System.getProperty("port","5599");
			Socket s = new Socket(host, Integer.parseInt(port));
			while (true) {
				OutputStream os = s.getOutputStream();
				os.write(("EchoMessage" + System.lineSeparator()).getBytes());
				InputStream is = s.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				System.out.println(new Date().toString() +" " +  br.readLine());
				Thread.sleep(15000);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
