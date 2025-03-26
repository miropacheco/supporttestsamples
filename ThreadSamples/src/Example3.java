import java.io.IOException;

public class Example3 {
    static int i=0;
	public void doIt() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				while (i < 10) {
					System.out.println("Value of I is " + i + "Thread name: Bonus question");
					i++;
				}

			}
		};
		Thread t = new Thread(r,"Thread1");
		Thread t1 = new Thread(r,"Thread2");
		t.start();
		t1.start();

	}

	public static void main(String s[]) {
		new Example3().doIt();
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Calling second time");
		new Example3().doIt();

	}

}
