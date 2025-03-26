
public class Example1 {
	int i = 0;

	public void doIt() {

		Runnable r = new Runnable() {

			@Override
			public void run() {
				
				while (i < 100) {

					System.out.println("Value of I is " + i + "Thread name: Bonus question");
					i++;
				}

			}
		};
		Thread t = new Thread(r, "Thread1");
		Thread t1 = new Thread(r, "Thread2");
		t.start();
		t1.start();

	}

	public static void main(String s[]) {
	
		new Example1().doIt();

	}

}
