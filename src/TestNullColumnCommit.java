import com.sun.javafx.binding.StringFormatter;

import java.sql.*;
import java.util.Properties;


public class TestNullColumnCommit {
    public static void main(String []args) {
        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        try {
            Connection conn = DriverManager.getConnection(url, props);
            conn.setAutoCommit(false);
            try (Statement statement = conn.createStatement() ) {
                statement.executeUpdate("CREATE TABLE if not exists  TEST_COMMIT_AFTER_SQL(COLUMN1 INT NOT NULL)");
                conn.commit();

// Successfull insert which is lost
                statement.executeUpdate("INSERT INTO TEST_COMMIT_AFTER_SQL(COLUMN1) VALUES(5)");

                try {
                    // Invalid insert that should make the transaction fail
                    statement.executeUpdate("INSERT INTO TEST_COMMIT_AFTER_SQL(COLUMN1) VALUES (NULL)");
                } catch (SQLException ex) {
                   ex.printStackTrace();
                }

                try {
                    // Expected failure
                    conn.commit();
                    SQLWarning warning = conn.getWarnings();
                    ResultSet result = statement.executeQuery("SELECT * FROM TEST_COMMIT_AFTER_SQL");


                    if (result.next() ) {
                        int value = result.getInt(1);
                        System.out.println(StringFormatter.format( "Value is {0}", value));
                    } else {
                        System.out.println("There is nothing in the table");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }


    }
}
