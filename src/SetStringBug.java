
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;

public class SetStringBug
{
	public static void main( String[] argv )
	{
		try {
			Connection c = DriverManager.getConnection
				("jdbc:postgresql:test");

			PreparedStatement s = c.prepareStatement
				("select ?::text");

			s.setString( 1, "Gary's Document" );

			ResultSet r = s.executeQuery();

			r.next();

			System.out.println( r.getString( 1 ) );
				
		} catch ( SQLException e ) {
			e.printStackTrace();
			System.exit( 2 );
		}
      
	}
}
