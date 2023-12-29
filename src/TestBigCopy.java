import java.sql.Connection;

import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.postgresql.copy.CopyOut;

public class TestBigCopy {
    public static void main(String args[]) throws Exception {
        String url = "jdbc:postgresql://localhost/test";
        String user = "test";
        String password = "test";
        try (Connection conn = java.sql.DriverManager.getConnection(url,user, password)) {
            PGConnection pgConnection = conn.unwrap(PGConnection.class);
            CopyManager copyAPI = pgConnection.getCopyAPI();
            String sql = "COPY (SELECT generate_series(1, 2000000) x) to STDOUT";
            CopyOut cp = copyAPI.copyOut(sql);

            long numRowsRead = 0;
            long totalBytes = 0;
            try {
                byte[] buf;
                while ((buf = cp.readFromCopy()) != null) {
                    numRowsRead++;
                    totalBytes += buf.length;
                }
                long handledRowCount = cp.getHandledRowCount();
                System.out.printf("Success: handledRowCount=%d numRowsRead=%s totalBytes=%s\n", handledRowCount, numRowsRead, totalBytes);
            } catch (Exception e) {
                System.out.printf("Error: %s; numRowsRead=%s totalBytes=%s\n", e.getMessage(), numRowsRead, totalBytes);
                throw e;
            }
        }
    }
}
