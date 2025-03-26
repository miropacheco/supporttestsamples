import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Class1 {
	private static final Logger logger = LogManager.getLogger("CreateTeamMemberLinkageAppLogger");
	
	public void doIt() {
		
		logger.info("From definition");
	}

	public static void main(String s[]) {
		new Class1().doIt();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
