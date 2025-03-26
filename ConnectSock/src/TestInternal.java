
public class TestInternal {
	public static void main(String s[]) {
		/*cl1 c = new cl1();
		c.i = 10;
		cl1 d = new cl1();
		d.i = 20;
		System.out.printf("%d %d",c.i, d.i);*/
		new TestInternal().new cl1();
	}
	
	class cl1 {
		public int i;
	}

}
