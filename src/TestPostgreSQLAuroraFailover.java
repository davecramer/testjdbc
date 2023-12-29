import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.SQLExceptionOverride;
//import software.aws.rds.jdbc.proxydriver.ds.ProxyDriverDataSource;


import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.*;

import static java.lang.Thread.sleep;

public class TestPostgreSQLAuroraFailover {
    public static String[] endpoints = {"reader1.cgnh50a2ovor.us-east-1.rds.amazonaws.com",
    "database-1-instance-1.cgnh50a2ovor.us-east-1.rds.amazonaws.com",
    "database-1-instance-1-us-east-1b.cgnh50a2ovor.us-east-1.rds.amazonaws.com"};

    public static void main(String[] args) {
        DataSource ds;

        if (true) {
            HikariConfig config = new HikariConfig();
            config.setPoolName("postgres");
            config.setMinimumIdle(1);
            config.setMaximumPoolSize(10);
            config.setConnectionTestQuery("select 1");
            //config.setDriverClassName("org.postgresql.Driver");
            config.setDriverClassName("software.aws.rds.jdbc.proxydriver.Driver");
            config.setJdbcUrl("aws-proxy-jdbc:postgresql://" + endpoints[0] + "/postgres");
            config.addDataSourceProperty("user", "postgres");
            config.addDataSourceProperty("password", "password");
            config.setExceptionOverrideClassName(TestPostgreSQLAuroraFailover.HikariCPSQLException.class.getName());

            ds = new HikariDataSource(config);
        } else {
/*
            ds = new ProxyDriverDataSource();
            ((ProxyDriverDataSource) ds).setJdbcUrl("jdbc:postgresql://" + endpoints[0] + "/postgres");
            ((ProxyDriverDataSource) ds).setUser("postgres");
            ((ProxyDriverDataSource) ds).setPassword("password");

 */
        }



        try (Connection connection = ds.getConnection()) {
            while (true) {
                try {
                    System.out.println("Getting status");
                    try (Statement statement = connection.createStatement();
                         ResultSet rs = statement.executeQuery("select * from aurora_replica_status()")) {
                        while (rs.next()) {
                            System.out.println("Server Id -> " + rs.getString("server_id")
                                    + " Session Id -> " + rs.getString("session_id"));
                        }

                    }
                } catch (SQLException sqlex) {
                    System.out.println("Exception ->" + sqlex.getMessage() + "SQL State: " + sqlex.getSQLState());
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
        } catch ( SQLException ex ) {
            System.out.println(ex.getMessage());
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
