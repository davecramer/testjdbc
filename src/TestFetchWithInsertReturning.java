import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class TestFetchWithInsertReturning {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/test", "test", "test")) {
            conn.prepareStatement("DROP TABLE IF EXISTS test_table").execute();
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS test_table (id SERIAL PRIMARY KEY, cnt INT NOT NULL)").execute();

            conn.setAutoCommit(false);
            for (int fetchSize = 0; fetchSize < 4; fetchSize++) {
                System.out.println("FetchSize=" + fetchSize);

                try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO test_table (cnt) VALUES (1), (2) RETURNING id", RETURN_GENERATED_KEYS)) {
                    stmt.setFetchSize(fetchSize);

                    int ret = stmt.executeUpdate();
                    System.out.println("executeUpdate result: " + ret);

                    ResultSet rs = stmt.getGeneratedKeys();
                    System.out.print("ids: ");
                    while (rs.next()) {
                        System.out.print(rs.getInt(1) + " ");
                    }
                    System.out.print("\n\n");
                }
            }
        } catch (Exception ex ){
            ex.printStackTrace();
        }
    }
}
