import java.sql.SQLException;

import javax.sql.DataSource;

import com.teradata.jdbc.TeraDataSource;

public class JDBCGeneric {
	public static void main(String s[]) {
		DataSource ds;
		try {
			TeraDataSource ts = new TeraDataSource();
			ts.setUser("user");
			ts.setpassword("asdfasdf");
			ts.setDSName("name/foo.com");
			ts.setCHARSET("UTF16");
			ts.setLOGMECH("ldap");
			
			((DataSource)ts).getConnection();
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
