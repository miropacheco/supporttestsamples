

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.Random;
// --- <<IS-END-IMPORTS>> ---

public final class ReplyResponseStressTest

{
	// ---( internal utility methods )---

	final static ReplyResponseStressTest _instance = new ReplyResponseStressTest();

	static ReplyResponseStressTest _newInstance() { return new ReplyResponseStressTest(); }

	static ReplyResponseStressTest _cast(Object o) { return (ReplyResponseStressTest)o; }

	// ---( server methods )---




	public static final void PauseRandom (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(PauseRandom)>> ---
		// @sigtype java 3.5
		Random random = new Random();
		
		// Generate a random number from a standard normal distribution (mean = 0,
		// standard deviation = 1)
		double randomNumber = random.nextGaussian();
		
		// Adjust mean and standard deviation as needed
		double mean = 200;
		double standardDeviation = 250;
		randomNumber = mean + standardDeviation * randomNumber;
		long rounded =(long) randomNumber;
		rounded= rounded<=0?200:rounded;
		Thread.currentThread();
		try {
			System.out.println("going to sleep for:" + rounded);
			Thread.currentThread().sleep(rounded);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Something went wrong here");
			e.printStackTrace();
		} 
			
		
			
		// --- <<IS-END>> ---

                
	}
}

