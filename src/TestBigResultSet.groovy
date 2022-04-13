import java.sql.Connection
import java.sql.DriverManager

class TestBigResultSet {
     static public void main(String[] args) {
        Properties properties = new Properties()
        properties.setProperty ( org.postgresql.PGProperty.USER.getName ( ), "davec" )
        properties.setProperty ( org.postgresql.PGProperty.PASSWORD.getName ( ), "password" )

        Connection con = DriverManager.getConnection('jdbc:postgresql://localhost:5432/test', properties)
    }
}
