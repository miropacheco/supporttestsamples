import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class GetHostKey {
	
	public static void main(String[] s) {
		JSch jsch = new JSch();
		JSch.setLogger(new MyLoggerSSH());
		Session session=null;
		try {
			session = jsch.getSession("adfasdf", s[0], 22);
			session.setPassword("adfsadff");
			session.connect();
			System.out.println(session.getHostKey().getKey());
			

			
		} catch (Exception e) {
			System.out.println(session.getHostKey().getKey());
			e.printStackTrace();
		}
	}
}
