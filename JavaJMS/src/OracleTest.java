
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import oracle.jdbc.pool.OracleDataSource;

public class OracleTest {
	static int connCount=0;
	public Connection connect() {
		try {
			connCount++;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			long startTime = System.nanoTime();
			OracleDataSource oracleRootSource = new OracleDataSource();
			oracleRootSource.setURL("jdbc:oracle:thin:@localhost:7878:xe");
			Properties prop = new Properties();
			prop.setProperty("oracle.jdbc.ReadTimeout", "15000");
			oracleRootSource.setConnectionProperties(prop);
		
			// oracleRootSource.setDatabaseName(""); // put here your database name and
			// uncomment it.
			// oracleRootSource.setConnectionProperties(System.getProperties());
			oracleRootSource.setUser("wm107"); // set your user
			//oracleRootSource.setLoginTimeout(3000);
			oracleRootSource.setPassword("manage"); // set your password
			System.out.println(oracleRootSource.getUser());

			//java.util.Properties prop = new java.util.Properties();
			//prop.put("includeSynonyms", "true");
			//prop.put("oracle.net.encryption_client","REQUESTED"); // put here your properties

			oracleRootSource.setConnectionProperties(prop);
			Connection c = oracleRootSource.getConnection();
			
			if (((System.nanoTime() - startTime) / 1_000_000) > 500)
				System.out.println("Currrent thread:" + Thread.currentThread().getId() + " took: "
						+ (System.nanoTime() - startTime) / 1_000_000);
			return c;

		} catch (Exception e) {	
			e.printStackTrace();

		}
		return null;

	}

	public static void main(String s[]) {
		int TOTALREQUESTS = 1;
		int TOTALTRHEADS = 1;
		ExecutorService exec = Executors.newFixedThreadPool(TOTALTRHEADS);

		for (int i = 0; i < TOTALREQUESTS; i++) {

			Runnable worker = new Runnable() {

				public void run() {
					// TODO Auto-generated method stub

					Connection c = new OracleTest().connect();
					try {
						if (c != null)
							c.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
			exec.execute(worker);
		}
		try {
			exec.awaitTermination(2, TimeUnit.MINUTES);
			exec.shutdown();
			System.out.println(connCount);
			System.out.println("finished waiting");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
