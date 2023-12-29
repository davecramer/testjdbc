import java.sql.*;
import java.time.*;
import java.util.HashMap;
import java.util.Properties;

/*
create table public."datetimearray" (
        "timestamptz" timestamp with time zone[] null
        );
        write simple java code:
 */
public class TestTimestampArray {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        try ( Connection conn = DriverManager.getConnection(url, props) ){
            conn.setAutoCommit(false);
            LocalDateTime localDateTime = LocalDateTime.parse("2015-02-20T06:30:00");
            ZoneOffset offset = ZoneOffset.of("+02:00");
            OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, offset);
            final int TIMESTAMPTZ = 1;
            try (PreparedStatement statement = conn
                    .prepareStatement("insert into public.datetimearray values(?)")) {
                {
                    final Array array = conn.createArrayOf("timestamptz",
                            new OffsetDateTime[] {offsetDateTime, offsetDateTime});
                    statement.setObject(TIMESTAMPTZ, array, Types.ARRAY);
                }
                statement.execute();
                conn.commit();
            }
            try (Statement statement = conn.createStatement();
                 ResultSet rs = statement.executeQuery("select * from datetimearray")) {
                if (rs.next()) {
                    {
                        Array array = rs.getArray(TIMESTAMPTZ);
                        System.out.println(String.format("%20s: %d", "baseType", array.getBaseType()));
                        System.out.println(String.format("%20s: %s", "baseType", array.getBaseTypeName()));
                        Object object = rs.getObject(TIMESTAMPTZ);
                        System.out.println(String.format("%20s: %s", "object", object));
                        System.out.println(String.format("%20s: %s", "object class", object.getClass().getName()));
                        {
                            Object javaArrayObject = array.getArray();
                            if (javaArrayObject instanceof Object[]) {
                                Object[] javaArray = (Object[]) javaArrayObject;
                                for (Object element : javaArray) {
                                    System.out.println(String.format("%20s: %s", "array element", element));
                                    System.out.println(String.format("%20s: %s", "array element class", element.getClass()));
                                }
                            }
                        }
                        {
                            HashMap<String, Class<?>> map = new HashMap<>();
                            map.put("timestamptz", OffsetDateTime.class);
                            Object javaArrayObject = array.getArray(map);
                            if (javaArrayObject instanceof Object[]) {
                                Object[] javaArray = (Object[]) javaArrayObject;
                                for (Object element : javaArray) {
                                    System.out.println(element);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(SQLException ex ){
            ex.printStackTrace();
        }
    }
}

