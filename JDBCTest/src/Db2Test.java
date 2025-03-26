import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ibm.db2.jcc.DB2SimpleDataSource;



public class Db2Test {
	public void connect() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			System.out.println("Request start time:" + sdf.format(new Date()) + Thread.currentThread().getId());
			long startTime = System.nanoTime();
			DB2SimpleDataSource dataSource = new DB2SimpleDataSource();
			dataSource.setUser("TESTUSER");
			dataSource.setPassword("TESTPASSWD");
			dataSource.setServerName("localhost");
			dataSource.setDatabaseName("TESTDB");
			dataSource.setPortNumber(50000); //
			dataSource.setCurrentSchema("DB2TEST");
			dataSource.setDriverType(4);
			dataSource.setSecurityMechanism(dataSource.ENCRYPTED_USER_AND_DATA_SECURITY);
			dataSource.setSslConnection(true);
			dataSource.setSslTrustStoreLocation("localion");
			dataSource.setSslTrustStorePassword("password");			
			dataSource.getConnection();

			dataSource.getConnection();
			if ((System.nanoTime() - startTime) / 1000000 > 0)
				System.out.println("Total time on thread:" + Thread.currentThread().getId() + "="
						+ (System.nanoTime() - startTime) / 1000000);
		} catch (Exception e) {
			e.printStackTrace();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			System.out.println("End time:" + sdf.format(new Date()) + Thread.currentThread().getId());

		}

	}

	public static void main(String s[]) {
		int TOTALREQUESTS = 1;
		int TOTALTRHEADS = 1;
		ExecutorService exec = Executors.newFixedThreadPool(TOTALTRHEADS);

		while (true) {
			System.out.println("Going for a round");
			for (int i = 0; i < TOTALREQUESTS; i++) {

				Runnable worker = new Runnable() {

					public void run() {
						// TODO Auto-generated method stub

						new Db2Test().connect();

					}

				};
				exec.execute(worker);
			}

			try {
				Thread.sleep(60000 * 5);
				// Thread.sleep(30);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}

	}
}
