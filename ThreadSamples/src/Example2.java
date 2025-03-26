import java.io.IOException;

public class Example2 {
	public int i = 0;
	public void doIt(){
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while (i < 20) {
					System.out.println("Value of I is " + i + "Thread name: Bonus question");
					i++;
				
				}

			}
		};
		for (int k = 0; k < 5; k++) {
			Thread t = new Thread(r, "Thread" + k);
			t.start();
		}
	}

	public static void main(String s[]) {
		new Example2().doIt();
	
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
