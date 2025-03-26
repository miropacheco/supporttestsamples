import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.postgresql.ds.PGSimpleDataSource;

import com.ibm.as400.access.AS400JDBCDataSource;

public class ConnectDS {

	private static final String SQL_QUERY = readSQLFile("SQLQuery.sql"); // Change the query as per your requirement

	Connection tryConnect() {
		// Declare the JDBC objects.
		Connection connection = null;

		try {
			// Establish the connection.

			PGSimpleDataSource ds = new org.postgresql.ds. PGSimpleDataSource();
			ds.setServerName("ec2-174-129-77-105.compute-1.amazonaws.com:5432");
			try {
				
				connection = ds.getConnection();
				connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

			} catch (SQLException ex) {
				ex.printStackTrace();

			}
		}

		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		}

		return connection;

	}

	public static void main(String[] args) {
		int TOTALREQUESTS = 1;
		int TOTALTRHEADS = 1;

		ExecutorService exec = Executors.newFixedThreadPool(TOTALTRHEADS);

		for (int i = 0; i < TOTALREQUESTS; i++) {

			Runnable worker = new Runnable() {

				public void run() {
					// TODO Auto-generated method stub

					try {
					Connection connection = new ConnectDS().tryConnect();
					System.out.println(
							"Connected to the database. Compiled version froim Support that unlocks the driver");

					// Creating a statement
					Statement statement = connection.createStatement();
					System.out.println("Statement created.");
					// Recording start time
					long startTime = System.nanoTime();

					// Executing the SQL query
					System.out.println("Before Query execute.");
					ResultSet rs = statement.executeQuery(SQL_QUERY);
					System.out.println("Query executed successfully.");
					int totalRecords = 0;
					while (rs.next()) {
						String s = rs.getString(1); // just get the first field
						totalRecords++;
					}
					System.out.println("Total Records:" + totalRecords);

					// Recording end time
					long endTime = System.nanoTime();

					// Calculating elapsed time in milliseconds
					long elapsedTimeInMillis = (endTime - startTime) / 1000000;

					// Convert milliseconds to minutes, seconds, and milliseconds
					long minutes = elapsedTimeInMillis / (1000 * 60);
					long seconds = (elapsedTimeInMillis / 1000) % 60;
					long milliseconds = elapsedTimeInMillis % 1000;

					// Printing the elapsed time
					System.out.println("Elapsed Time: " + minutes + " minutes, " + seconds + " seconds, " + milliseconds
							+ " milliseconds");

					// Closing resources
					statement.close();
					connection.close();
					}
					catch (SQLException e) {
						e.printStackTrace();
						
					}

				}

			};
			exec.execute(worker);

		}
		try {
			exec.awaitTermination(10000, TimeUnit.SECONDS);
			exec.shutdown();
			System.out.println("finished waiting");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String readSQLFile(String fileName) {
		StringBuilder stringBuilder = new StringBuilder();

		// Get the class loader
		ClassLoader classLoader = OracleDDLDAPUnlocked.class.getClassLoader();

		// Get the input stream of the resource using the class loader
		try (InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(fileName));
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

}