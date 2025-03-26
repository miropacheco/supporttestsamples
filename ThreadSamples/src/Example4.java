import java.io.IOException;

public class Example4 {
	Integer exclusiveObject= new Integer(0);
	
	public void doIt() {
		Runnable r = new Runnable() {
			

			@Override
			public void run() {
						
				
				// TODO Auto-generated method stub
				System.out.println("waiting to get my turn:" + Thread.currentThread().getId());
				synchronized (exclusiveObject) {
					System.out.println("got my turn" + Thread.currentThread().getId());
					exclusiveObject ++;					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(exclusiveObject);
				}
			
			}
			
		};
		for (int k = 0; k < 5; k++) {
			Thread t = new Thread(r, "Thread" + k);
			t.start();
		}

		
	}
	
	public static void main(String s[]) throws IOException {
		new Example4().doIt();
		System.in.read();
		
		
	}

}
