import java.sql.Connection;
import java.sql.SQLException;

import com.ddtek.jdbc.extensions.ExtEmbeddedConnection;
public class TestUnlocked {
	public static void unlockConnection (Connection c) {
		if (c instanceof ExtEmbeddedConnection) {
            try {
				((ExtEmbeddedConnection) c).unlock("webMethods");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
 
		
	}

}
