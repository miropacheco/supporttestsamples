import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB2DDConnection {
	public void DataDirectConnection() {

		try {
			String user = System.getProperty("user", "user");
			String password = System.getProperty("password", "password");

			Connection connection = DriverManager.getConnection(System.getProperty("url", "brokenURL"), user,
					password);
			connection.close();
			System.out.println("got a connection");
		} catch (SQLException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		}

	}

	public static void main(String s[]) throws InterruptedException {
		new DB2DDConnection().DataDirectConnection();

	}

}