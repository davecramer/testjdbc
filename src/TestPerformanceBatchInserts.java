import org.postgresql.PGConnection;
import org.postgresql.PGProperty;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.copy.PGCopyOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.Properties;

public class TestPerformanceBatchInserts {
    static String URL = "jdbc:postgresql://database-1.cluster-ro-cgnh50a2ovor.us-east-1.rds.amazonaws.com/postgres";

    static Properties getConnectionProperties() {
        Properties properties = new Properties();
        PGProperty.USER.set(properties,"postgres");
        PGProperty.PASSWORD.set(properties,"password");
        PGProperty.REWRITE_BATCHED_INSERTS.set(properties,true);
        PGProperty.PREPARE_THRESHOLD.set(properties,-1);
        return properties;
    }
    static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS customer(\n" +
                    "customer_id bigint NOT NULL primary key, \n" +
                    "name text,\n" +
                    "phone text,\n" +
                    "address text);");
        }

    }
    static String createString(){
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < 100; i++) {
            sb.append("1234567890123456");
        }
        return sb.toString();
    }
    public static void main(String[] args) {

        int totalRows = Integer.parseInt(args[1]);
        int batchSize = Integer.parseInt(args[2]);
        int bufferSize =  1024;
        if ( args.length == 4 ) {
            bufferSize = Integer.parseInt(args[3]);
        }
        System.out.println(String.format("Test Using %s with %d rows %d batch size ", args[0], totalRows, batchSize));
        if (args[0].compareToIgnoreCase("insert") == 0 ) {
            insertWithInsert(totalRows, batchSize);
        } else if (args[0].compareToIgnoreCase("insertcopy") == 0 ) {
            insertCopyRows(totalRows, batchSize);
        } else if (args[0].compareToIgnoreCase("copy") == 0 ) {
            insertWithCopy(totalRows, batchSize,bufferSize);
        }
    }
    static void insertWithInsert(int totalRows, int batchSize) {
        int actualRows = 0;
        String insertString = createString();
        Properties properties = getConnectionProperties();
        int primaryId=0;
        try (Connection connection = DriverManager.getConnection(URL, properties)) {
            createTable(connection);
            long startTime = System.currentTimeMillis();
            try (PreparedStatement pstmt = connection.prepareStatement("insert into customer (customer_id, name, phone, address) values (?,?,?,?)")) {
                while(totalRows > 0) {
                    for (int j = 0; j < batchSize; j++) {
                        pstmt.setInt(1, primaryId++);
                        pstmt.setString(2, "Dave Cramer");
                        pstmt.setString(3, "519 939 0336");
                        pstmt.setString(4, insertString);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                    actualRows += batchSize;
                    totalRows -= batchSize;
                }
            }
            long duration = System.currentTimeMillis() - startTime;
            double transactionsPerSecond = (double)actualRows/duration*1000;
            System.out.println(String.format("Total time %d for %d rows to be insert TPS %f", duration, actualRows, transactionsPerSecond));
            try (Statement statement = connection.createStatement()) {
                statement.execute("drop table customer");
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
    static void insertWithCopy(int totalRows, int batchSize, int bufferSize) {
        Properties properties = getConnectionProperties();
        String insertString = createString();
        int primaryId=0;
        int actualRows=0;
        try (Connection connection = DriverManager.getConnection(URL, properties)) {
            createTable(connection);
            long startTime = System.currentTimeMillis();
            PGConnection pgConnection = connection.unwrap(PGConnection.class);
            String sql = "COPY customer FROM STDIN";

            OutputStream os = new PGCopyOutputStream( pgConnection, sql, bufferSize);
            while(totalRows > 0) {
                String row = new String();

                for (int j = 0; j < batchSize; j++) {
                    row += String.format("%d\t%s\t%s\t%s\n", primaryId++, "Dave Cramer", "519 939 0336", insertString );
                }
                byte[] buf = row.getBytes();
                os.write(buf);
                actualRows += batchSize;
                totalRows -= batchSize;
            }
            os.close();
            long duration = System.currentTimeMillis() - startTime;
            double transactionsPerSecond = (double)actualRows/duration*1000;
            System.out.println(String.format("Total time %d for %d rows to be insert TPS %f", duration, actualRows, transactionsPerSecond));
            try (Statement statement = connection.createStatement()) {
                statement.execute("drop table customer");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex ) {
            ex.printStackTrace();
        }
    }

    static void insertCopyRows(int totalRows, int batchSize)  {
        String sql = "COPY customer FROM STDIN";
        Properties properties = getConnectionProperties();
        String insertString = createString();
        int primaryId=0;

        try (Connection connection = DriverManager.getConnection(URL, properties)) {
            createTable(connection);
            CopyManager copyAPI = ((PGConnection) connection).getCopyAPI();
            CopyIn cp = copyAPI.copyIn(sql);

            long startTime = System.currentTimeMillis();
            for (int i=0; i < totalRows; i++) {
                byte[] buf = String.format("%d\t%s\t%s\t%s\n", primaryId++, "Dave Cramer", "519 939 0336", insertString ).getBytes();
                cp.writeToCopy(buf, 0, buf.length);
            }
            cp.endCopy();
            long duration = System.currentTimeMillis() - startTime;
            double transactionsPerSecond = (double)totalRows/duration*1000;
            System.out.println(String.format("Total time %d for %d rows to be insert TPS %f", duration, totalRows, transactionsPerSecond));
            try (Statement statement = connection.createStatement()) {
                statement.execute("drop table customer");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
