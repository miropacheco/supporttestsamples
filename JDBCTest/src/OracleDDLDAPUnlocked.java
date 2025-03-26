
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class OracleDDLDAPUnlocked {
	private static final String DATABASE_URL_DEFAULT = "jdbc:as400://EXLAQA:446;databaseName=EXLA";
	private static final String DATABASE_USER_DEFAULT = "sdafadsfsadf";
	private static final String DATABASE_PASSWORD_DEFAULT = "adfasdf";
	private static final String SQL_QUERY = readSQLFile("SQLQuery.sql"); // Change the query as per your requirement

	public void DataDirectConnection() {
	
		try {
			// Establishing connection
			String DATABASE_URL= System.getProperty("DB_URL",DATABASE_URL_DEFAULT);
			String DATABASE_USER= System.getProperty("DB_USER",DATABASE_USER_DEFAULT);
			String DATABASE_PASSWORD= System.getProperty("DB_PASSWORD",DATABASE_PASSWORD_DEFAULT);
			
			Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
		
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			System.out.println("Connected to the database. Compiled version froim Support that unlocks the driver");

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
				String s  = rs.getString(1); // just get the first field
				totalRecords ++;
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

		} catch (SQLException e) {
			e.printStackTrace();
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		}

	}

	public static void main(String s[]) throws InterruptedException {
		new OracleDDLDAPUnlocked().DataDirectConnection();

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
