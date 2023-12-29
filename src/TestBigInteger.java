
import org.postgresql.util.PSQLException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



class TestBigInteger{
    static final String DB_URL = "jdbc:postgresql://localhost/test"; // TODO set DB URL here
    static final String DB_USERNAME = "davecra"; // TODO set DB user here
    static final String DB_PASSWORD = "password"; // TODO set DB password here

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DROP TABLE IF EXISTS IntegerOverflowTest;");
                statement.execute("CREATE TABLE IntegerOverflowTest (a int8 NOT NULL);");
                statement.execute("INSERT INTO IntegerOverflowTest (a) VALUES (9223372036854775807);");
                statement.execute("INSERT INTO IntegerOverflowTest (a) VALUES (1);");
            }

            for (int i = 0; i < 100; i++) {
                System.out.println("i = " + i);
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(a) FROM IntegerOverflowTest;")) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        resultSet.next();
                        long l = resultSet.getLong(1);
                        System.out.println("sum(a) -> " + l);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
