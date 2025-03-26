import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteQuery {
	private String userName;
	private String password;
	String serverHost;
	String portNumber;
	private String queryFile;

	public static void main(String s[]) throws IOException, SQLException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");			
			ExecuteQuery queryObj = new ExecuteQuery();
			queryObj.execute();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void execute() throws IOException, SQLException {
		serverHost = System.getProperties().getProperty("server");
		portNumber = System.getProperties().getProperty("port");
		queryFile = System.getProperties().getProperty("queryFile");
	    BufferedReader is = new BufferedReader(new FileReader(queryFile));
	    String sql = is.readLine();
	    is.close();
	    Connection c = getConnection();
		ResultSet rs = c.createStatement().executeQuery(sql);
		int total = 0;
		while(rs.next()) {
			total ++;
		}
		System.out.println(total);

		
	}

	public Connection getConnection() throws SQLException {

		Connection conn = null;

		// jdbc:oracle:thin:hr/hr@//localhost:1522
		System.out.println("jdbc:oracle:thin:@"
				+ serverHost + ":"
				+ portNumber );
		conn = DriverManager.getConnection("jdbc:oracle:thin:@"
				+ serverHost + ":"
				+ portNumber, System.getProperties());

		System.out.println("Connected to database");
		return conn;
	}

}
