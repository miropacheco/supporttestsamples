import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

public class PingServer {
	public static void main(String s[]) throws IOException {
		ServerSocket ss;
		String port = System.getProperty("port","5599");;
		ss = new ServerSocket(Integer.parseInt(port));
		while (true) {
			try {

				Socket cl = ss.accept();
				while (true) {
					BufferedReader is = new BufferedReader(new InputStreamReader(cl.getInputStream()));
					String incomingLine = is.readLine();
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(cl.getOutputStream()));
					writer.write(incomingLine);
					writer.newLine();
					writer.flush();
					cl.getOutputStream().flush();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
