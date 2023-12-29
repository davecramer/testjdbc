import org.openjdk.jmh.infra.Blackhole;
import org.postgresql.util.JdbcBlackHole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestFormatBinary {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "davecra","test")){
           try (Statement statement = connection.createStatement()) {
               statement.execute("create temp table foo as select (l&32767)::int2 as i,l&(-1::int4) as j,l::int8 as k,md5(random()::text) as t from generate_series(1,100000) s(l)");
               long startTime = System.nanoTime();
               try (ResultSet rs = statement.executeQuery("select * from foo")) {
                   while (rs.next()) {
                       rs.getInt(1);
                       rs.getInt(2);
                       rs.getInt(3);
                       rs.getString(4);
                   }
               }
               System.out.println("Text Elapsed time:  " + (System.nanoTime()-startTime));
               statement.execute("set format_binary='20,21,23, 25'");
               startTime = System.nanoTime();
               try (ResultSet rs = statement.executeQuery("select * from foo")) {
                   while (rs.next()) {
                       rs.getInt(1);
                       rs.getInt(2);
                       rs.getInt(3);
                       rs.getString(4);
                   }
               }
               System.out.println("Text Elapsed time:  " + (System.nanoTime()-startTime));
           }
        }catch ( Exception ex ){
            ex.printStackTrace();
        }
    }
}
