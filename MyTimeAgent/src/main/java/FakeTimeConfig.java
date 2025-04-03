
public class FakeTimeConfig {
	public static volatile   long offsetValue=0 ;
	public static  long getOffsetValue() {
		return offsetValue;
		
	}
	public  static  void settOffsetValue(long offset) {
		offsetValue = offset;
		
	}
	
}
