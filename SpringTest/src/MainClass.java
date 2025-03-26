import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainClass {
   public static void main(String[] args) {
	  //System.getenv().put("FACTORY","com.pcbsys.nirvana.nSpace.NirvanaContextFactory");
	   //System.setSecurityManager(new SecurityManager());
	   System.setProperty("java.security.policy","file:/C:/ZuluJvm/jre/lib/security/java.policy");
	   System.out.println(System.getSecurityManager());
	   System.setSecurityManager(new SecurityManager());
	   System.out.println(System.getSecurityManager());
	   System.out.println(System.getProperty("java.security.policy"));
	   System.setSecurityManager(null);
      ApplicationContext context = 
             new ClassPathXmlApplicationContext("Beans.xml");

      /*try {
    	Object x = new Object();
    	synchronized (x) {
			x.wait();
		}
		
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/

      
   }
}