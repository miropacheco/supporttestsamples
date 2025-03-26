import java.net.InetAddress;
import java.net.UnknownHostException;

public class ShowHostName {
	public static void main(String s[]) {

		try {
			System.out.println(InetAddress.getByName("10.134.2.30").getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
