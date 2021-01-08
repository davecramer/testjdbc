import java.sql.*;

/**
 * Created by davec on 2015-10-01.
 */
public class TestDatabaseMetaData
{
    public static void main(String []args) throws Exception
    {
        String url = "jdbc:postgresql://localhost/test";
        //url = "jdbc:pgsql://localhost:5432/test";

        try (Connection con = DriverManager.getConnection(url, "test", ""))
        {
            con.createStatement().execute("drop table if EXISTS  test_table");
            con.createStatement().execute("create table test_table (id bigserial primary key, foobar text)");

            System.out.println("#######Via Database Metadata");
            DatabaseMetaData metaData = con.getMetaData();
            try (ResultSet columns = metaData.getColumns(con.getCatalog(), "", "", "id"))
            {
                while (columns.next())
                {
                    System.out.println(columns.getString("TABLE_NAME"));
                }
            }

            System.out.println("#######Via information_schema");
            try (Statement st = con.createStatement())
            {
                try (ResultSet columns = st
                        .executeQuery("select table_name from information_schema.columns where column_name='id'")) {
                    while (columns.next())
                    {
                        System.out.println(columns.getString("TABLE_NAME"));
                    }
                }
            }
            con.createStatement().execute("drop table test_table");
        }
    }
}
