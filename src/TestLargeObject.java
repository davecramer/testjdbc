import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


public class TestLargeObject {
    public static void main(String[] args) throws Exception{
        String url = "jdbc:postgresql://localhost:5432/test";
        Properties props = new Properties();
        props.setProperty("user", "davec");
        props.setProperty("password", "");

        try(Connection conn = DriverManager.getConnection(url, props)){
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("create table if not exists testblob( id name,lo oid)");
            }
            LargeObjectManager lom = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
            long oid = lom.createLO(LargeObjectManager.READWRITE);
            LargeObject blob = lom.open(oid);

            byte []buf = new byte[2048];
            for (int i=0; i< buf.length; i++) {
                buf[i]='a';
            }
            int i= 1000000/buf.length;
            for ( int j=i; j > 0; j--)  {
                blob.write(buf, 0, buf.length);
            }
            InputStream is = blob.getInputStream();
            for ( int j = i; j > 0; j--) {
                is.read(buf, 0, buf.length);
            }
            blob.close();
        }
    }

}
