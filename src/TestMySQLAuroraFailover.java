import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.SQLExceptionOverride;
//import software.aws.rds.jdbc.proxydriver.ds.ProxyDriverDataSource;

import javax.sql.DataSource;
import java.sql.*;

import static java.lang.Thread.sleep;

public class TestMySQLAuroraFailover {
    public static String[] endpoints = {"database-2-instance-1.cgnh50a2ovor.us-east-1.rds.amazonaws.com",
    "database-2-instance-1-us-east-1b.cgnh50a2ovor.us-east-1.rds.amazonaws.com"};

    public static void main(String[] args) {

        java.security.Security.setProperty("networkaddress.cache.ttl" , "5");
        HikariConfig config = new HikariConfig();
        config.setPoolName("mysql");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(5000);
        config.setConnectionTestQuery("select 1");
        // current driver
        config.setDriverClassName("software.aws.rds.jdbc.mysql.Driver");
        config.setJdbcUrl("jdbc:mysql:aws://"+endpoints[0] + "/mysql");
        // proxy driver
        //config.setDriverClassName("software.aws.rds.jdbc.proxydriver.Driver");
        //config.setJdbcUrl("aws-proxy-jdbc:mysql://"+endpoints[0] + "/mysql");
        config.addDataSourceProperty("user", "admin");
        config.addDataSourceProperty("password", "password");
        config.setLeakDetectionThreshold(100);
        config.setExceptionOverrideClassName(HikariCPSQLException.class.getName());

        DataSource ds = new HikariDataSource(config);
        try (Connection connection = ds.getConnection()) {
            while (true) {
                try {
                    System.out.println("Getting status");
                    try (Statement statement = connection.createStatement();
                         ResultSet rs = statement.executeQuery(" select * from information_schema.replica_host_status")) {
                        while (rs.next()) {
                            System.out.println("Server Id -> " + rs.getString("server_id")
                                    + " Session Id -> " + rs.getString("session_id"));
                        }
                    }
                    try (Statement statement = connection.createStatement();
                         ResultSet rs = statement.executeQuery("SHOW GLOBAL VARIABLES LIKE 'read_only'")) {
                        if (rs.next()) {
                            System.out.println(" Connection is: " + (rs.getString(2).equals("ON") ? "Read Only" : "Write Only"));
                        }
                    }
                } catch (SQLException sqlex ){
                    System.out.println("Exception ->" + sqlex.getMessage() + "SQL State: " + sqlex.getSQLState() );
                    String sqlState = sqlex.getSQLState();
                    if (!sqlState.equalsIgnoreCase("08S02") &&
                            !sqlState.equalsIgnoreCase("08007")) {
                        throw sqlex;
                    }
                }
                try {
                    sleep(1000);
                } catch (Exception ex) {
                }
            }

        }catch (Exception ex ){
            System.err.println(ex.getMessage());
        }
    }
    public static class HikariCPSQLException implements SQLExceptionOverride {
        public Override adjudicate(final SQLException sqlException) {
            String sqlState = sqlException.getSQLState();
            if (sqlState.equalsIgnoreCase("08S02") ||
                    sqlState.equalsIgnoreCase("08007")) {
                return Override.DO_NOT_EVICT;
            } else {
                return Override.CONTINUE_EVICT;
            }
        }
    }

}
