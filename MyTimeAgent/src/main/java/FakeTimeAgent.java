import java.awt.font.TransformAttribute;
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.matcher.ElementMatchers;

public class FakeTimeAgent {
	public static long myTimeOffset = 0; // Default offset (no change)
    //@Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
	  public static long intercept() {
	        long fakeTime = 50000; // Offset by 5 sec
	        System.out.println("[FakeTimeAdvice] Intercepted! Returning fake time: " + fakeTime);
	        return fakeTime;
	    }
	    
	@Advice.OnMethodExit
	public static void intercept(@Advice.Return(readOnly = false) long currentTimeMills) {
		//System.out.println("[FakeTimeAdvice] Intercepted! Original time: " + currentTimeMills);
		if (myTimeOffset==0) {
			myTimeOffset = Long.parseLong(System.getProperty("timeOffset","0"));
			
		}
		//currentTimeMills += Long.parseLong(System.getProperty("timeOffset","0")); // Add 5 seconds for testing
		
		currentTimeMills += myTimeOffset;
		//System.out.println("[FakeTimeAdvice] Adjusted time: " + currentTimeMills);
	}
	
	//@Advice.OnMethodExit
	 public static long currentTimeMills(@Advice.Return long originalReturnValue) {
	        System.out.println("originalReturnValue = " + originalReturnValue);

	        return originalReturnValue ;
	    }
	

	public static void premain(String agentArgs, Instrumentation inst) {
		if (agentArgs != null) {
			try {
				FakeTimeAgent.myTimeOffset = Long.parseLong(agentArgs); // Convert seconds to milliseconds
				
				FakeTimeConfig.offsetValue = myTimeOffset;
				FakeTimeConfig.settOffsetValue(myTimeOffset);
				System.out.println(FakeTimeConfig.getOffsetValue());
				System.out.println("[FakeTimeAgent] Time offset set to: " + FakeTimeAgent.myTimeOffset + " ms");
			} catch (NumberFormatException e) {
				System.err.println("[FakeTimeAgent] Invalid offset. Using default time.");
			}
		}
		
		System.out.println("[FakeTimeAgent] Installing agent...");

		// **Manually add agent JAR to bootstrap classpath** 
		try { 
		    File agentJar = new File(FakeTimeAgent.class.getProtectionDomain().getCodeSource().getLocation().toURI()); 
		    inst.appendToBootstrapClassLoaderSearch(new JarFile(agentJar));
		   
		    System.out.println("[FakeTimeAgent] JAR added to bootstrap classpath: " + agentJar.getAbsolutePath());
		} catch (Exception e) { 
		    System.err.println("[FakeTimeAgent] Failed to append to bootstrap classpath: " + e.getMessage());
		}
		
		try {
		    Class.forName("FakeTimeAdvice", true, null);  // Null forces bootstrap classloader
		    System.out.println("[FakeTimeAgent] Successfully loaded FakeTimeAdvice.");
		} catch (ClassNotFoundException e) {
		    System.err.println("[FakeTimeAgent] Failed to load FakeTimeAdvice: " + e.getMessage());
		}
		
		try {
		    Class.forName("FakeTimeConfig", true, null);  // Null forces bootstrap classloader
		    System.out.println("[FakeTimeAgent] Successfully loaded FakeTimeConfig.");
		} catch (ClassNotFoundException e) {
		    System.err.println("[FakeTimeAgent] Failed to load FakeTimeAdvice: " + e.getMessage());
		}
		
		
		//ClassFileLocator classFileLocator = ClassFileLocator.ForClassLoader.of(FakeTimeAdvice.class.getClassLoader());
		//System.out.println("[FakeTimeAgent] FakeTimeAdvice loaded by: " + (classLoader1 == null ? "Bootstrap ClassLoader" : classLoader1.getClass().getName()));
		

		new AgentBuilder.Default()
		    .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION) // Allows transformation of already-loaded classes 
		    .with(AgentBuilder.TypeStrategy.Default.REBASE)
		    .enableNativeMethodPrefix("native")
		    .ignore(ElementMatchers.none())	
		    //.with(AgentBuilder.Listener.StreamWriting.toSystemOut())
		    .type(ElementMatchers.named("java.lang.System")) // Target the System class
		    .transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
		        System.out.println("[FakeTimeAgent] Transforming System.currentTimeMillis()...");
		        ClassLoader.getSystemResource("FakeTimeAdvice.class");
		        return builder.method(ElementMatchers.named("currentTimeMillis")).
		       intercept(Advice.to(FakeTimeAgent.class, ClassFileLocator.ForClassLoader.of(FakeTimeAgent.class.getClassLoader())));
		      
		       /* return builder.method(ElementMatchers.named("currentTimeMillis"))
		               .intercept(Advice.to(FakeTimeAdvice.class, classFileLocator)); */


		    }).installOn(inst);

		  
		System.out.println("Bootstrap classpath: " + System.getProperty("sun.boot.class.path"));
		System.out.println("[FakeTimeAgent] Agent installed and transfttt	ormation applied.");
	}
}
