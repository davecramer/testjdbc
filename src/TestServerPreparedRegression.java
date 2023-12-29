import org.postgresql.PGProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/*
SELECT codecon_uuid, codecon_text, coding_code, coding_user_selected, coding_display, coding_system, coding_version, coding_uuid, conceptType FROM ( (SELECT codecon_uuid, codecon_text, coding_code, coding_user_selected, coding_display, coding_system, coding_version, coding_uuid, 'category' AS conceptType FROM file_index_category

         INNER JOIN codeable_concept

         ON  codecon_uuid  =  fidxcat_codecon_uuid

         INNER JOIN codeable_concept_coding

         ON  codeconc_codecon_uuid  =  codecon_uuid

         INNER JOIN coding

         ON  coding_uuid  =  codeconc_coding_uuid

         WHERE ( (  fidxcat_fidx_seq = $1  ) ))

         UNION

        (SELECT codecon_uuid, codecon_text, coding_code, coding_user_selected, coding_display, coding_system, coding_version, coding_uuid, 'security label' AS conceptType FROM file_index_security_label

         INNER JOIN codeable_concept

         ON  codecon_uuid  =  fisl_codecon_uuid

         INNER JOIN codeable_concept_coding

         ON  codeconc_codecon_uuid  =  codecon_uuid

         INNER JOIN coding

         ON  coding_uuid  =  codeconc_coding_uuid

         WHERE ( (  fisl_fidx_seq = $2  ) )) ) as union_query ORDER BY codecon_uuid NULLS FIRST, conceptType NULLS FIRST
 */
public class TestServerPreparedRegression {

    static String QUERY = "SELECT codecon_uuid, codecon_text, coding_code, coding_user_selected, coding_display, coding_system, coding_version, coding_uuid, conceptType FROM ( (SELECT codecon_uuid, codecon_text, coding_code, coding_user_selected, coding_display, coding_system, coding_version, coding_uuid, 'category' AS conceptType FROM file_index_category\n" +
            "\n" +
            "         INNER JOIN codeable_concept\n" +
            "\n" +
            "         ON  codecon_uuid  =  fidxcat_codecon_uuid\n" +
            "\n" +
            "         INNER JOIN codeable_concept_coding\n" +
            "\n" +
            "         ON  codeconc_codecon_uuid  =  codecon_uuid\n" +
            "\n" +
            "         INNER JOIN coding\n" +
            "\n" +
            "         ON  coding_uuid  =  codeconc_coding_uuid\n" +
            "\n" +
            "         WHERE ( (  fidxcat_fidx_seq = ?  ) ))\n" +
            "\n" +
            "         UNION\n" +
            "\n" +
            "        (SELECT codecon_uuid, codecon_text, coding_code, coding_user_selected, coding_display, coding_system, coding_version, coding_uuid, 'security label' AS conceptType FROM file_index_security_label\n" +
            "\n" +
            "         INNER JOIN codeable_concept\n" +
            "\n" +
            "         ON  codecon_uuid  =  fisl_codecon_uuid\n" +
            "\n" +
            "         INNER JOIN codeable_concept_coding\n" +
            "\n" +
            "         ON  codeconc_codecon_uuid  =  codecon_uuid\n" +
            "\n" +
            "         INNER JOIN coding\n" +
            "\n" +
            "         ON  coding_uuid  =  codeconc_coding_uuid\n" +
            "\n" +
            "         WHERE ( (  fisl_fidx_seq = ?  ) )) ) as union_query ORDER BY codecon_uuid NULLS FIRST, conceptType NULLS FIRST";
    public static void main(String[] args) {
        Properties connectionProperties =  new Properties();
        connectionProperties.setProperty(PGProperty.USER.getName(),"test");
        connectionProperties.setProperty(PGProperty.PASSWORD.getName(),"test");
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/test", connectionProperties)) {
            conn.createStatement().execute("drop table if exists coding");
            conn.createStatement().execute("drop table if exists codeable_concept ");
            conn.createStatement().execute("drop table if exists codeable_concept_coding");
            conn.createStatement().execute("drop table if exists file_index_security_label");
            conn.createStatement().execute("drop table if exists file_index_category");

            conn.createStatement().execute("create table if not exists coding (coding_uuid text)");
            conn.createStatement().execute("create table if not exists codeable_concept(codecon_uuid text, fidxcat_codecon_uuid text)");
            conn.createStatement().execute("create table if not exists codeable_concept_coding(codeconc_codecon_uuid text)");
            conn.createStatement().execute( "create table if not exists file_index_security_label(fisl_fidx_seq serial primary key, fidxcat_codecon_uuid text, codecon_text text, coding_code text, coding_user_selected text, coding_display text, coding_system text, coding_version text, codeconc_coding_uuid text, fisl_codecon_uuid text,  security_label text)");
            conn.createStatement().execute( "create table if not exists file_index_category(fidxcat_fidx_seq serial primary key, fisl_codecon_uuid text, codecon_text text, coding_code text, coding_user_selected text, coding_display text, coding_system text, coding_version text,  codeconc_coding_uuid text, security_label text)");
            try (PreparedStatement preparedStatement = conn.prepareStatement(QUERY)){
                preparedStatement.setInt(1, 1);
                preparedStatement.setInt(2, 1);
                for ( int i = 0; i < 7; i++){
                    if ( i==5 )
                        System.err.println("Fifth iteration");
                    try (ResultSet rs = preparedStatement.executeQuery()){
                        while(rs.next());
                    }
                }
            }
        } catch ( Exception ex) {
            ex.printStackTrace();
        }

    }
}
