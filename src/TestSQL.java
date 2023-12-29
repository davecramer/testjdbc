/**
 * Test SQL Data Type
 */

import java.sql.*;



/**
 * Custom SQL Testing
 */
public class TestSQL {

    // Database constants
    public static final String DB_TABLE			= "TestTable";		// If the table name is changed, the SQL constants that follow needs to be updated too
    public static final String DB_URL			= "jdbc:postgresql://localhost:5432/test";
    public static final String DB_USER_ID		= "test";
    public static final String DB_USER_PASSWORD	= "test";

    // SQL statement constants
    public static final String	SQL_CREATE_TABLE =
            "Create table if not exists TestTable (" +
                    "ID                    serial not null," +
                    "CharValue             char(32)," +
                    "VarCharValue          varchar(255)," +
                    "TextValue             Text," +
                    "BinaryValue           ByteA," +
                    "IntValue              Int," +
                    "FloatValue            Real," +
                    "DoubleValue           Double Precision," +
                    "BooleanValue          Boolean," +
                    "DateValue             Date," +
                    "TimeValueNoTimeZone   Time(3) without time zone," +
                    "TimeValueWithTimeZone Time(3) with time zone," +
                    "TimestampNoTimeZone   TimeStamp(3) without time zone default Current_TimeStamp," +
                    "TimestampWithTimeZone TimeStamp(3) with time zone default Current_TimeStamp" +
                    ")";
    public static final String	SQL_DROP_TABLE   = "Drop table if exists TestTable";

    /**
     * No need to instantiate this class.
     */
    private TestSQL() {


    }


    /**
     * Testing entry point
     *
     * @param strArgs the arguments passed in.
     */
    public static void main(String[] strArgs) {


        test();

    }


    /**
     * Performs the test.
     */
    public static void test() {

        // Local variables
        Connection			connection			= null;
        DatabaseMetaData	databaseMetaData	= null;
        ResultSet			resultSet			= null;
        Statement			statement			= null;

        // Just in case
        try {

            // Create a connection to the DB
            log("Connecting to the DB.");
            connection = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_USER_PASSWORD);

            // Get the associated DatabaseMetaData
            databaseMetaData = connection.getMetaData();

            // Show the database and driver information
            log();
            log("DB Product: " + databaseMetaData.getDatabaseProductName() + " version " + databaseMetaData.getDatabaseProductVersion());
//			log("DB Product Name: " + databaseMetaData.getDatabaseProductName());
//			log("DB Product Version: " + databaseMetaData.getDatabaseProductVersion());
//			log("DB Product Version: " + databaseMetaData.getDatabaseMajorVersion() + '.' + databaseMetaData.getDatabaseMinorVersion());
            log("DB Driver: " + databaseMetaData.getDriverName() + " version " + databaseMetaData.getDriverVersion());
//			log("DB Driver Name: " + databaseMetaData.getDriverName());
//			log("DB Driver Version: " + databaseMetaData.getDriverVersion());
//			log("DB Driver Version: " + databaseMetaData.getDriverMajorVersion() + '.' + databaseMetaData.getDatabaseMinorVersion());
            log();

            statement = connection.createStatement();

            log("Dropping the table if it exists.");
            statement.execute(SQL_DROP_TABLE);

            log("Creating the table.");
            statement.execute(SQL_CREATE_TABLE);

            // Show the column info
            log();
            showColumnInfo(databaseMetaData, DB_TABLE);

            // Completed without error
            log();
            log("All done.");

        } catch (Exception e) {

            log("Unexpected " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);

        } finally {

            // Ensure the result-set gets closed
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log("Unexpected " + e.getClass().getSimpleName() + " while closing the DB result-set: " + e.getMessage(), e);
                }
                resultSet = null;
            }

            // Ensure the statement gets closed
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log("Unexpected " + e.getClass().getSimpleName() + " while closing the DB statement: " + e.getMessage(), e);
                }
                statement = null;
            }

            // Ensure the connection gets closed
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log("Unexpected " + e.getClass().getSimpleName() + " while closing the DB connection: " + e.getMessage(), e);
                }
                connection = null;
            }

        }

    }


    /**
     * Show the column info for each column in the supplied table.
     *
     * @param databaseMetaData the DatabaseMetaData to use.
     * @param strTable the name of the table to show the column info for.
     *
     * @throws SQLException if one should occur.
     */
    private static void showColumnInfo(DatabaseMetaData	databaseMetaData, String strTable) throws SQLException {

        // Local variables
        String		strColumnName;
        ResultSet	resultsetColumns	= null;
        int			intDataType;
        String		strDataType;

        // Ensure things get closed
        try {

            // Get the column info with case adjustment as necessary
            resultsetColumns = databaseMetaData.getColumns(null, null, strTable.toLowerCase(), null);
            if (resultsetColumns.next()) {

                do {

                    // Get the column information
                    strColumnName = resultsetColumns.getString("COLUMN_NAME");
                    intDataType = resultsetColumns.getInt("DATA_TYPE");

                    // Get the text of the DataType
                    switch (intDataType) {
                        case Types.ARRAY:
                            strDataType = "Array";
                            break;
                        case Types.BIGINT:
                            strDataType = "BigInt";
                            break;
                        case Types.BINARY:
                            strDataType = "Binary";
                            break;
                        case Types.BIT:
                            strDataType = "Bit";
                            break;
                        case Types.BLOB:
                            strDataType = "BLOB";
                            break;
                        case Types.BOOLEAN:
                            strDataType = "Boolean";
                            break;
                        case Types.CHAR:
                            strDataType = "Char";
                            break;
                        case Types.CLOB:
                            strDataType = "CLOB";
                            break;
                        case Types.DATALINK:
                            strDataType = "DataLink";
                            break;
                        case Types.DATE:
                            strDataType = "Date";
                            break;
                        case Types.DECIMAL:
                            strDataType = "Decimal";
                            break;
                        case Types.DISTINCT:
                            strDataType = "Distinct";
                            break;
                        case Types.DOUBLE:
                            strDataType = "Double";
                            break;
                        case Types.FLOAT:
                            strDataType = "Float";
                            break;
                        case Types.INTEGER:
                            strDataType = "Integer";
                            break;
                        case Types.JAVA_OBJECT:
                            strDataType = "JavaObject";
                            break;
                        case Types.LONGNVARCHAR:
                            strDataType = "LongNVarChar";
                            break;
                        case Types.LONGVARBINARY:
                            strDataType = "LongVarBinary";
                            break;
                        case Types.LONGVARCHAR:
                            strDataType = "LongVarChar";
                            break;
                        case Types.NCHAR:
                            strDataType = "NChar";
                            break;
                        case Types.NCLOB:
                            strDataType = "NCLOB";
                            break;
                        case Types.NULL:
                            strDataType = "Null";
                            break;
                        case Types.NUMERIC:
                            strDataType = "Numeric";
                            break;
                        case Types.NVARCHAR:
                            strDataType = "NVarChar";
                            break;
                        case Types.OTHER:
                            strDataType = "Other";
                            break;
                        case Types.REAL:
                            strDataType = "Real";
                            break;
                        case Types.REF:
                            strDataType = "Ref";
                            break;
                        case Types.REF_CURSOR:
                            strDataType = "RefCursor";
                            break;
                        case Types.ROWID:
                            strDataType = "RowID";
                            break;
                        case Types.SMALLINT:
                            strDataType = "SmallInt";
                            break;
                        case Types.SQLXML:
                            strDataType = "SQL_XML";
                            break;
                        case Types.STRUCT:
                            strDataType = "Struct";
                            break;
                        case Types.TIME:
                            strDataType = "Time";
                            break;
                        case Types.TIME_WITH_TIMEZONE:
                            strDataType = "TimeWithTimeZone";
                            break;
                        case Types.TIMESTAMP:
                            strDataType = "Timestamp";
                            break;
                        case Types.TIMESTAMP_WITH_TIMEZONE:
                            strDataType = "TimestampWithTimeZone";
                            break;
                        case Types.TINYINT:
                            strDataType = "TinyInt";
                            break;
                        case Types.VARBINARY:
                            strDataType = "VarBinary";
                            break;
                        case Types.VARCHAR:
                            strDataType = "VarChar";
                            break;
                        default:
                            strDataType = "Unknown(" + intDataType + ")";
                    }

                    // Show the DataType for the column
                    log("The data type for column '" + strColumnName + "' is: " + strDataType);

                } while (resultsetColumns.next());

            } else {

                log("Unable to obtain column information for table '" + strTable + "'!");

            }


        } finally {

            // Ensure the ResultSet gets closed
            if (resultsetColumns != null) {
                try {
                    resultsetColumns.close();
                } catch (SQLException e) {
                    log("SQLUtil.getTableColumnInfo - Unexpected SQLException closing the ResultSet:", e);
                }
                resultsetColumns = null;
            }

        }

    }


    /**
     * Logs an empty line.
     */
    private static void log() {

        System.out.println();

    }


    /**
     * Logs the supplied line.
     *
     * @param strMessage the message to log.
     */
    private static void log(String strMessage) {

        System.out.println(strMessage);

    }


    /**
     * Logs the supplied line.
     *
     * @param strMessage the message to log.
     * @param throwable the Throwable to log.
     */
    private static void log(String strMessage, Throwable throwable) {

        System.out.println(strMessage);
        throwable.printStackTrace(System.out);

    }


}
