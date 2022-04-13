import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

public class Foo {

    static String ngPrefix = "jdbc:pgsql"
    static String pgPrefix = "jdbc:postgresql"
    static String url="$pgPrefix://localhost/test"

    static String usr = "test"

    public static void main(String ...args ) {
        Connection con = DriverManager.getConnection(url, usr, "test")

        Statement st = con.createStatement()
        st.execute("create table if not exists test24(t time)")
        st.execute("insert into test24(t) values ('24:00:00')")

        ResultSet rs = st.executeQuery("select '00:00:05.123456'::time as t")


        if (rs.next()) {
            Timestamp time = rs.getTimestamp(1, )

            LocalTime lt = time.toLocalTime()
            println time
        }
        st.execute("drop table test24")
    }
}
