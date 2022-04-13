/**
 * Created by davec on 2015-11-12.
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/*
CREATE TYPE "ICAO" AS
   ("Type" smallint,
"Addr" bytea);
 */

public class ICAO implements SQLData {

    @Override
    public void readSQL(SQLInput aStream, String aTypeName)
    {
        typeName = aTypeName;

        try {
            icaoType = aStream.readShort();
            icaoAddr = aStream.readBytes();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeSQL(SQLOutput aStream)
    {
        try {
            aStream.writeShort(icaoType);
            aStream.writeBytes(icaoAddr);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSQLTypeName()
    {
        return typeName;
    }

    public void setType(short aType)
    {
        icaoType = aType;
    }

    public void setAddr(String aHexAddr)
    {
        int hexAddr = Integer.valueOf(aHexAddr,16);

        icaoAddr = new byte[4];
        icaoAddr[0]=(byte)(hexAddr & 0xff);
        icaoAddr[1]=(byte)(hexAddr >> 16 & 0xff);
        icaoAddr[2]=(byte)(hexAddr >> 32 & 0xff);
        icaoAddr[3]=(byte)(hexAddr >> 48 & 0xff);

    }

    private short icaoType;
    private byte[] icaoAddr;
    private String typeName = "icao";


    // ******* unit test driver
    public static void main(String[] args) throws SQLException
    {
        final String AVLC_SELECT = "SELECT \"RSSI\", \"SymbolCount\", \"ReedSolErr\", \"Quality\", \"FlagCount\", \"LowConfidence\", \"BrokenMsg\", \"BadCRCCount\", \"AVLC\".\"DateTime\", \"TimeStamp\", \"LineNum\", \"SourceFiles\".\"Station\", \"AVLC\".\"Src\", \"Dest\", \"AVLC\".\"Msg\" AS \"AVLCMsg\", \"SQP\".\"Msg\" AS \"SQPMsg\", \"AVLC\".\"RadioAddr\", \"FileName\", \"LogType\", \"SentRecv\", \"AG\", \"CR\", \"P\", \"F\", \"Type\", \"NS\", \"NR\", \"Score\", \"Delay\", \"SREJPairs\", \"XIDType\", \"Information\", \"XIDInfo\", \"BlockID\", \"Label\", \"Address\", \"MSN\", \"Text\", \"Agency\", \"Flight\", Stations.\"RadioNum\", Stations.\"Freq\", COALESCE(Stations.\"Char\", '?') \"Char\", \"UpDownOther\"(\"AVLC\".\"RadioAddr\", \"AVLC\".\"Src\", \"Dest\"), \"RFLength\"(\"AVLC\".\"Msg\"), \"ULReportInd\".\"DateTime\" \"ULReportIndDateTime\", \"CSMADecisionTime\", \"TM1\", \"TM2\", \"TM3\", \"p\""
                + "FROM \"AVLC\""
                + "NATURAL JOIN \"SourceFiles\""
                + "LEFT OUTER JOIN \"SQP\" USING(\"RadioAddr\", \"SQPFileNum\", \"SQPLineNum\")"
                + "LEFT OUTER JOIN \"ULReportInd\" USING(\"RadioAddr\", \"ULReportIndFileNum\", \"ULReportIndLineNum\")"
                + "LEFT OUTER JOIN \"ARINC618\" USING(\"FileNum\", \"LineNum\")"
                + "LEFT OUTER JOIN Stations USING(\"Station\", \"RadioAddr\")";
        final String AVLC_AC_WHERE = "WHERE (\"Dest\" = ? OR \"Src\" = ?) AND \"AVLC\".\"DateTime\" BETWEEN ? AND ?";
        final String STATIONS = "WITH Stations AS"
                + "("
                + "SELECT DISTINCT \"Station\", \"RadioAddr\", \"RadioNum\", \"Freq\", \"Char\""
                + "FROM \"Radios\""
                + "NATURAL JOIN \"SourceFiles\""
                + "LEFT OUTER JOIN \"InstChar\" USING(\"Freq\")"
                + ")";

        String query = STATIONS + AVLC_SELECT + AVLC_AC_WHERE;

        ICAO clnpIcao = new ICAO();
        clnpIcao.setType((short)1);
        clnpIcao.setAddr("400AE7");

        Connection pgConn = DriverManager.getConnection("jdbc:postgresql://localhost/test", "test", "");

        try {
            PreparedStatement stm = pgConn.prepareStatement(query);

            Map<String, Class<?>> newMap = pgConn.getTypeMap();
            if (newMap == null)
            {
                newMap = new java.util.HashMap<String, Class<?>>();
            }
            newMap.put("public.ICAO", ICAO.class);
            pgConn.setTypeMap(newMap);

            Timestamp st = Timestamp.valueOf("2015-10-15 10:00:00.0");
            Timestamp et = Timestamp.valueOf("2015-10-15 12:00:00.0");

            stm.setObject(1, clnpIcao);
            stm.setObject(2, clnpIcao);
            stm.setTimestamp(3,st);
            stm.setTimestamp(4,st);

            ResultSet result = stm.executeQuery();

            List<List<Object>> table = new ArrayList<List<Object>>();
            List<Object> row = new ArrayList<Object>();
            Object colData;

            while (result.next())
            {
                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++)
                {
                    colData = result.getObject(i);
                    if (colData != null)
                        row.add(colData);
                    else
                        row.add("<null>");
                }

                System.out.print("\n row = " + row);

                table.add(row);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }   // ******* end of main()
}   // end of ICAO class
