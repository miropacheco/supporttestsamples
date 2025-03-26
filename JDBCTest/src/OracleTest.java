
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.jdbc.pool.OracleDataSource;

public class OracleTest {
	static int connCount = 0;
	private static Logger logger;
	public void DataDirectConnection() {
		
		try {
			//Connection connection = DriverManager.getConnection("jdbc:wm:oracle://localhost:10389;LDAPDISTINGUISHEDNAME=cn=OracleContext,dc=ent,dc=duke-energy,dc=com","user","password");
			//Connection connection = DriverManager.getConnection("jdbc:wm:oracle://eusprod.duke-energy.com:1389;serviceName=XE","wm107mws","manage1");
			System.out.println("got a connection");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Connection connect(String URL, int threshold) {
		try {
			connCount++;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			long startTime = System.nanoTime();
			OracleDataSource oracleRootSource = new OracleDataSource();
			oracleRootSource.setURL(URL);
			/*
			 * oracleRootSource.setURL(
			 * "jdbc:oracle:thin:@(description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=dehnguln.adb.us-ashburn-1.oraclecloud.com))(connect_data=(service_name=g72034f4bd7cd08_db2b_high.adb.oraclecloud.com))(security=(ssl_server_cert_dn=\"CN=adwc.uscom-east-1.oraclecloud.com, OU=Oracle BMCS US, O=Oracle Corporation, L=Redwood City, ST=California, C=US\")))"
			 * );
			 */
			// oracleRootSource.setDatabaseName(""); // put here your database name and
			// uncomment it.
			// oracleRootSource.setConnectionProperties(System.getProperties());
			oracleRootSource.setUser(System.getProperty("user", "wm10_11")); // set your user
			oracleRootSource.setLoginTimeout(3000);
			oracleRootSource.setPassword(System.getProperty("password", "manage1")); // set your password

			java.util.Properties prop = new java.util.Properties();
			// prop.put("includeSynonyms", "true");
			// prop.put("oracle.net.encryption_client","REQUESTED"); // put here your
			// properties

			oracleRootSource.setConnectionProperties(prop);
			Connection c = oracleRootSource.getConnection();			

			/* if (((System.nanoTime() - startTime) / 1_000_000) > threshold) */
			logger.debug("debug message going to the logs");
			logger.info("Currrent thread:" + Thread.currentThread().getId() + " took: "
					+ (System.nanoTime() - startTime) / 1_000_000);
			logger.trace("Trace message going to the logs");
			logger.error("Error message going to the logs");
			return c;

		} catch (Exception e) {
			logger.error("Exception:" + e);
			e.printStackTrace();

		}
		return null;

	}

	public static void main(String s[]) throws InterruptedException {
		int TOTALTRHEADS = 1;
		logger = LogManager.getLogger(OracleTest.class);
		ExecutorService exec = Executors.newFixedThreadPool(TOTALTRHEADS);

		while (true) {

			Runnable worker = new Runnable() {

				public void run1() {
					// TODO Auto-generated method stub

					Connection c = new OracleTest().connect(
							System.getProperty("URL", "jdbc:oracle:thin:@localhost:1521:xe"),
							Integer.parseInt(System.getProperty("threshold", "10")));
					try {
						Statement st = c.createStatement();
						st.execute("select * from IS_USER_TASKS");
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						if (c != null)
							c.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void run() {
				    new OracleTest().DataDirectConnection();
					
				}

			};
			exec.execute(worker);
			Thread.sleep(3000); // change here the interval between executions
			break;
		}
		
		/*
		 * try { exec.awaitTermination(30, TimeUnit.SECONDS); exec.shutdown();
		 * System.out.println(connCount); System.out.println("finished waiting"); }
		 * catch (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 */
	}
}
