import java.net.*;


public class MyServer {
	public static void main(String s[]) {

	try {
		    ServerSocket serverSocket = new ServerSocket(7788);
		    Socket clientSocket = serverSocket.accept();
		    System.in.read();
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	}
	

}
