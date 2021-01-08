import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.DriverManager
import java.sql.ResultSet

/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 13-07-29
 * Time: 8:15 AM
 * To change this template use File | Settings | File Templates.
 */
class MetaDataTest
{

  public static void main (String []args)
  {
    Class.forName('org.postgresql.Driver')
    Connection con = DriverManager.getConnection('jdbc:postgresql://localhost/test','test','')
    def stmt = con.createStatement()

    stmt.execute("""create table person (FIRST_NAME character varying(100) NOT NULL,
            LAST_NAME character varying(100) NOT NULL,
            FIRST_NAME_PARENT_1 character varying(100),
            LAST_NAME_PARENT_1 character varying(100),
            FIRST_NAME_PARENT_2 character varying(100),
            LAST_NAME_PARENT_2 character varying(100),
            CONSTRAINT PERSON_pkey PRIMARY KEY (FIRST_NAME , LAST_NAME ),
            CONSTRAINT PARENT_1_fkey FOREIGN KEY (FIRST_NAME_PARENT_1, LAST_NAME_PARENT_1)
            REFERENCES PERSON (FIRST_NAME, LAST_NAME) MATCH SIMPLE
            ON UPDATE CASCADE ON DELETE CASCADE,
            CONSTRAINT PARENT_2_fkey FOREIGN KEY (FIRST_NAME_PARENT_2, LAST_NAME_PARENT_2)
            REFERENCES PERSON (FIRST_NAME, LAST_NAME) MATCH SIMPLE
            ON UPDATE CASCADE ON DELETE CASCADE )""")

    try{
    DatabaseMetaData dbmd = con.getMetaData();
    ResultSet rs = dbmd.getImportedKeys(null, "", "person");
    for (int j=0; rs.next(); j++ )
    {

      String pkTableName = rs.getString( "PKTABLE_NAME" );
      assert pkTableName == 'person'

      String pkColumnName = rs.getString( "PKCOLUMN_NAME" );

      assert "first_name" == pkColumnName || 'last_name'== pkColumnName

      String fkTableName = rs.getString( "FKTABLE_NAME" );
      assert  "person" == fkTableName

      String fkColumnName = rs.getString( "FKCOLUMN_NAME" );


      String fkName = rs.getString( "FK_NAME" );

      if ( fkName == 'parent_1_fkey' )
        assert fkColumnName == 'last_name_parent_1' || fkColumnName == 'first_name_parent_1'
      else if ( fkName == 'parent_2_fkey' )
        assert fkColumnName == 'last_name_parent_2' || fkColumnName == 'first_name_parent_2'

      else if ( fkName == 'parent_1_key ')
        assert fkColumnName == 'first_name_parent_1'
      else if ( fkName == 'parent_2_key')
        assert fkColumnName == 'first_name_parent_2'

    }
    }finally
    {
      stmt.execute("drop table person")
      con.close()
    }
  }


}
