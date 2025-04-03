import java.util.Date;

public class TestTime {
	public static void main(String s[]) {
	     if (System.getSecurityManager() != null) {
	            System.out.println("Security Manager is enabled!");
	        } else {
	            System.out.println("Security Manager is NOT enabled.");
	        }
		System.out.println(new Date());
		System.out.println(System.currentTimeMillis()); 
		System.out.println(mytest()); 
	}
	public static long mytest(){
		return 1500l;
	}
	
}
