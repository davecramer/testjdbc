import org.postgresql.PGResultSetMetaData;
import org.postgresql.core.Field;

import java.sql.*;
import java.util.Properties;

public class TestBinary {
    public static void main(String[] args) throws SQLException
    {
        String url = "jdbc:postgresql://localhost/test";
        Connection conn = null;


        Properties connectionProps = new Properties();
        connectionProps.put("user", "test");
        connectionProps.put("password", "password");
        conn = DriverManager.getConnection(url, connectionProps);

// create table b1 (id bigserial primary key, t text, f float4, d float8, n numeric(8,2), b bool, c char, ch varchar(10), i2 int2, i4 int4, ts timestamp, tz timestamp with time zone);
        try (PreparedStatement fs = conn
                .prepareStatement("SELECT * from b1")) {

            ((org.postgresql.PGStatement) fs).setPrepareThreshold(-1);
            try(ResultSet rs = fs.executeQuery()){
                rs.next();
                // id int8
                if (getFormat(rs,1) != Field.BINARY_FORMAT) throw new AssertionError();
                // t text
                if (getFormat(rs,2) != Field.TEXT_FORMAT) throw new AssertionError();
                // f float
                if (getFormat(rs,3) != Field.BINARY_FORMAT) throw new AssertionError();
                // d float8
                if (getFormat(rs,4) != Field.BINARY_FORMAT) throw new AssertionError();
                // n numeric
                if (getFormat(rs,5) != Field.TEXT_FORMAT) throw new AssertionError();
                // b bool
                if (getFormat(rs,6) != Field.TEXT_FORMAT) throw new AssertionError();
                // c char
                if (getFormat(rs,7) != Field.TEXT_FORMAT) throw new AssertionError();
                // ch varchar
                if (getFormat(rs,8) != Field.TEXT_FORMAT) throw new AssertionError();
                // i2 int2
                if (getFormat(rs,9) != Field.BINARY_FORMAT) throw new AssertionError();
                // i4 int4
                if (getFormat(rs,10) != Field.BINARY_FORMAT) throw new AssertionError();
                // ts timestamp
                if (getFormat(rs,11) != Field.BINARY_FORMAT) throw new AssertionError();
                // tz timestamp with time zone
                if (getFormat(rs,12) != Field.BINARY_FORMAT) throw new AssertionError();

            }
        }
    }
    private static int getFormat(ResultSet results, int field) throws SQLException {
        return ((PGResultSetMetaData) results.getMetaData()).getFormat(field);
    }
}
