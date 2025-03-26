import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class ShowMemory {
	public static void main(String s[]) {
		OperatingSystemMXBean mbean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();

		System.out.println( "Free memory(MB):" + ((com.sun.management.OperatingSystemMXBean) mbean).getFreePhysicalMemorySize()/(1024*1024));
		System.out.println( "Total memory(MB):" + ((com.sun.management.OperatingSystemMXBean) mbean).getTotalPhysicalMemorySize()/(1024*1024));
	}

}
