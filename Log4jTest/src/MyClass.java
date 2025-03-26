import java.net.URLClassLoader;

public class MyClass {
	public static void main(String s[]) {
		for (int i = 0; i < 200000; i++) {

			new Class1().doIt();
			new Class2().doIt();
		}
	}

}
