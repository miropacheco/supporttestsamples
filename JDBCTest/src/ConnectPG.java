import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.postgresql.ds.PGSimpleDataSource;

public class ConnectPG {
	Connection tryConnect() {
		// Declare the JDBC objects.
		Connection con = null;

		try {
			// Establish the connection.
			PGSimpleDataSource source = new PGSimpleDataSource();
			source.setUrl("set here the url");			
			con = source.getConnection();

		}

		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		}

		return con;

	}

	public static void main(String[] args) {
		int TOTALREQUESTS = 1;
		int TOTALTRHEADS = 1;
		ExecutorService exec = Executors.newFixedThreadPool(TOTALTRHEADS);

		for (int i = 0; i < TOTALREQUESTS; i++) {

			Runnable worker = new Runnable() {

				public void run() {
					// TODO Auto-generated method stub

					Connection c = new ConnectPG().tryConnect();
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
			exec.awaitTermination(10000, TimeUnit.SECONDS);
			exec.shutdown();
			System.out.println("finished waiting");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}