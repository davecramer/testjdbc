import org.postgresql.PGProperty;

import java.sql.*;
import java.util.Properties;

public class TestAlias {
    private static final String POSTGRES_CONNECT_STRING = "jdbc:postgresql://localhost/test";

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost/test";
        String pgurl = "jdbc:pgsql://localhost/test";


        Properties props = new Properties();
        PGProperty.USER.set(props, "test");
        PGProperty.PASSWORD.set(props, "test");


        try {
            new TestAlias().testReadMetadata(props);


/*
            String sql = "select t_name as name from t_1";
            try (Connection conn = DriverManager.getConnection(pgurl, props)) {
                try (PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery();
                ) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    // bug is here:
                    // expect: columnName is t_name, columnLabel is name,actual: columnName and columnLabel are both name
                    String columnName = rsmd.getColumnName(1);
                    String columnLabel = rsmd.getColumnLabel(1);
                    System.err.println("Column name is: " + columnName);
                    System.err.println("Column label is: " + columnLabel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
*/
        } catch (Exception e) {
            e.printStackTrace();
            //

        }
    }


    public void testReadMetadata(Properties p) throws Exception{


        System.out.println("Results for Simple Join query: ");

        runQuery(getQuery1(), p);



        System.out.println("Results for SELECT COL from TABLE query");

        runQuery(getQuery2("subscriptions", "schedule_id"), p);

        runQuery(getQuery2("schedules", "id"), p);



        System.out.println("Now use getMetaData");

        getMetadata("subscriptions", "schedule_id", p);

        getMetadata("schedules", "id", p);

    }



    private String getQuery1(){

        return  "SELECT\n" +

                "s.schedule_id AS subscriptions_schedule_id,\n" +

                "sch.id AS schedules_id \n" +

                "FROM " +

                "subscriptions s\n" +

                "INNER JOIN \n" +

                "schedules sch\n" +

                "ON s.schedule_id = sch.id\n" +

                "WHERE ( 1=2 )";

    }



    private String getQuery2(String table, String column){

        return  "SELECT\n" +

                column +

                " FROM " +

                table +

                " WHERE ( 1=2 )";

    }



    private void runQuery (String query, Properties p) throws Exception {

        try (

                Connection conn = DriverManager.getConnection(POSTGRES_CONNECT_STRING, p);

                Statement st = conn.createStatement();

                ResultSet rs = st.executeQuery(query);

        ) {

            ResultSetMetaData rsmd = rs.getMetaData();

            int i =1;

            while (i <= rsmd.getColumnCount()) {

                System.err.println("Field is nullable: " + rsmd.getTableName(i) + "." + rsmd.getColumnName(i) + (rsmd.isNullable(i) != 0 ? " true" : " false"));

                i++;

            }

        }

    }



    private void getMetadata (String tableName, String columnName, Properties p) throws Exception{

        try (

                Connection conn = DriverManager.getConnection(POSTGRES_CONNECT_STRING, p);

        ) {

            DatabaseMetaData md = conn.getMetaData();

            ResultSet rs = md.getColumns("workgroup", "public", tableName, columnName);

            while (rs.next()) {

                System.out.println("Field is nullable: " + rs.getString(3) + "." + rs.getString(4) + ": " + (0 != rs.getInt(11)));

            }

        }

    }






}
