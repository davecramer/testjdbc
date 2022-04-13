package org.postgresql.util;

import java.sql.*;

public class TestEnum {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost/test";
        //url = "jdbc:pgsql://localhost:5432/test";

        try (Connection con = DriverManager.getConnection(url, "test", "test")) {
            con.createStatement().execute("drop table if EXISTS  enumtable");
          //  con.createStatement().execute("CREATE TYPE rainbow AS ENUM('red', 'orange', 'yellow', 'green', 'blue', 'purple')");
            con.createStatement().execute("CREATE TABLE enumtable ( id  serial PRIMARY KEY, r rainbow)");
            DatabaseMetaData metaData = con.getMetaData();
            try (ResultSet columns = metaData.getColumns(con.getCatalog(), "", "enumtable", ""))
            {
                while (columns.next())
                {
                    System.out.println(columns.getString("COLUMN_NAME"));
                    System.out.println(columns.getString("TYPE_NAME"));
                }
            }
            try (PreparedStatement ps = con.prepareStatement("insert into enumtable (  r) values (?)")){
                ps.setObject(1,"red", Types.OTHER);
                ps.execute();
            }

            try (ResultSet rs = con.createStatement().executeQuery("select * from enumtable"))
            {
                ResultSetMetaData rsmd = rs.getMetaData();
                System.out.println(rsmd.getColumnTypeName(2));

            }
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
