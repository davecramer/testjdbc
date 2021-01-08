/**
 * Created by davec on 2015-11-09.
 */
import org.postgresql.*;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

public class ArrayNPE {

    private final String url;
    private final String login;
    private final String password;

    public ArrayNPE(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public static void main(String[] args) throws SQLException {

        ArrayNPE main = new ArrayNPE("jdbc:postgresql://localhost:5432/test", "test", "test");

        main.prepareDatabase();
        DriverManager.setLogWriter(new PrintWriter(System.err));
//        org.postgresql.Driver.setLogLevel(org.postgresql.Driver.DEBUG);
     //   main.separateConnections();
        main.singleConnection();
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, login, password);

    }

    public void singleConnection() throws SQLException {
        System.out.println("Testing using single connection...");

        try (Connection connection = openConnection()) {
            for (int i = 0; i < 10; i++) {
                try {
                    findDepartmentInfos(connection);
                } catch (Exception e) {
                    System.out.println("failed after " + i + " iterations");
                    e.printStackTrace();
                    return;
                }
            }
        }

        System.out.println("ok");
    }

    public void separateConnections() throws SQLException {
        System.out.println("Testing using separate connections...");

        for (int i = 0; i < 10; i++) {
            try {
                try (Connection connection = openConnection()) {
                    findDepartmentInfos(connection);
                }
            } catch (Exception e) {
                System.out.println("failed after " + i + " iterations");
                e.printStackTrace();
                return;
            }

        }

        System.out.println("ok");
    }

    private void prepareDatabase() throws SQLException {
        try (Connection connection = openConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DROP TABLE IF EXISTS employee");
                statement.execute("DROP TABLE IF EXISTS department");

                statement.execute("CREATE TABLE department (id INT PRIMARY KEY)");
                statement.execute("CREATE TABLE employee (id INT PRIMARY KEY, department_id INT NOT NULL REFERENCES department)");

                statement.execute("INSERT INTO department (id) VALUES (1)");
            }
        }
    }

    private void findDepartmentInfos(Connection connection) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement("SELECT '{}'::int[]")) {
            ((org.postgresql.PGStatement)ps).setPrepareThreshold(-1);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Array array = rs.getArray(1);
                    if (!rs.wasNull())
                    {
                        ResultSet ars  = array.getResultSet();
                        int sqlType = ars.getMetaData().getColumnType(1); // this throws NPE
                        if (sqlType != Types.INTEGER)
                            System.err.println("error");
                    }


                }
            }
        }
    }
}