import org.postgresql.PGProperty;

import java.sql.*;
import java.util.Properties;

public class TestJson {
    public static void main(String[] args) throws SQLException {

        Properties connectionProperties =  new Properties();
        connectionProperties.setProperty(PGProperty.USER.getName(),"test");
        connectionProperties.setProperty(PGProperty.PASSWORD.getName(),"test");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/test", connectionProperties)) {

            try (ResultSet rs = conn.createStatement().executeQuery("select * from j") ) {
                rs.next();
                String s = rs.getString(1);
                System.out.println(s);
            }
            boolean result = conn.createStatement().execute(" CREATE TABLE if not exists ArrayTypes (\n" +
                    "                row_id        INTEGER NOT NULL,\n" +
                    "                json_array JSON ARRAY,\n" +
                    "                PRIMARY KEY (row_id)\n" +
                    "        );\n");

            result = conn.createStatement().execute("INSERT INTO ArrayTypes (\n" +
                    "                row_id,\n" +
                    "                json_array\n" +
                    "        )\n" +
                    "        VALUES (\n" +
                    "                1,\n" +
                    "                ARRAY ['{\"key1\": \"value\", \"key2\": 2}', '{\"key1\": \"value\", \"key2\": 2}'] :: JSON ARRAY");

            try (PreparedStatement s = conn.prepareStatement("select * from arraytypes")) {
                try (ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(rs.getString(1));
                    }
                }
            }
        }
    }
}
