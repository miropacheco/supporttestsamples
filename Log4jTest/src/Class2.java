import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class Class2 {
	private static final Logger logger = LogManager.getLogger("CreateTeamMemberLinkageAppLogger");
	
	public void doIt() {
		
		logger.info("Logging to test log");
	}
	public static void main(String s[]) {
	   new Class2().doIt();
	}

}
