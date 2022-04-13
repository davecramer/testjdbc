import java.sql.*;
import java.util.Properties;

public class TestSQLDAta {


    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/test";
        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        try (Connection conn = DriverManager.getConnection(url, props)) {
            conn.createStatement().execute("create table if not exists test(a varchar(180), b varchar(180))");
            //conn.createStatement().execute("create type test_t as(a varchar(180), b varchar(180))");
            Test data[] = new Test[]{new Test("a","b"), new Test("c","d"), new Test("e","f")};
            Array array = conn.createArrayOf("test_t", data);
            PreparedStatement stmt = conn.prepareStatement("insert into test (a, b) (select a, b from unnest(?))");
            stmt.setArray(1, array);
            stmt.execute();
        }
    }
    public static class Test implements SQLData {
        String a;
        String b;

        public Test(String a, String b) {
            this.a = a;
            this.b = b;
        }
        @Override
        public String getSQLTypeName() throws SQLException {
            return "test_t";
        }

        @Override
        public void readSQL(SQLInput stream, String typeName) throws SQLException {

        }

        @Override
        public void writeSQL(SQLOutput stream) throws SQLException {
            stream.writeString("(");
            stream.writeString("1");
            stream.writeString(",");
            stream.writeString("2");
            stream.writeString(")");
        }

        public String toString() {
            return "(" + a +',' + b + ')';
        }
    }
}