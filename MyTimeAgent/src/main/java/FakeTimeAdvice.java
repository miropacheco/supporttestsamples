import net.bytebuddy.asm.Advice;

public class FakeTimeAdvice {
	static {
        System.out.println("[FakeTimeAdvice] loaded  by: " + FakeTimeAdvice.class.getClassLoader());
    }
    
    @Advice.OnMethodExit
    public static void intercept(@Advice.Return(readOnly = false) long currentTimeMills) {
		//System.out.println("[FakeTimeAdvice] Intercepted! Original time: " + currentTimeMills);
		//currentTimeMills += Long.parseLong(System.getProperty("timeOffset","0")); // Add 5 seconds for testing
		System.out.println("Offsetting" + FakeTimeConfig.offsetValue);
		currentTimeMills += FakeTimeConfig.offsetValue;
		//System.out.println("[FakeTimeAdvice] Adjusted time: " + currentTimeMills);
	}
	
}
