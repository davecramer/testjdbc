import java.sql.*;
import java.util.Properties;
//mport software.aws.rds.jdbc.mysql.shading.com.mysql.cj.jdbc.ha.plugins.AWSSecretsManagerPluginFactory;

public class TestSecretsManager {

    private static final String CONNECTION_STRING = "jdbc:mysql:aws://database-2-instance-1.cgnh50a2ovor.us-east-1.rds.amazonaws.com:3306/mysql?autoReconnect=true";
    private static final String SECRET_ID = "secretId";
    private static final String REGION = "us-east-1";

    public static void main(String[] args) throws SQLException {
        final Properties properties = new Properties();
        // Enable the AWS Secrets Manager Plugin:
       // properties.setProperty("connectionPluginFactories", AWSSecretsManagerPluginFactory.class.getName());

        // Set these properties so secrets can be retrieved:
        properties.setProperty("secretsManagerSecretId", SECRET_ID);
        properties.setProperty("secretsManagerRegion", REGION);

        // Try and make a connection:
        try (final Connection conn = DriverManager.getConnection(CONNECTION_STRING, properties);
             final Statement statement = conn.createStatement();
             final ResultSet rs = statement.executeQuery("SELECT 1")) {
            while (rs.next()) {
                System.out.println( "Result is : " + rs.getInt(1));
            }
        }
    }
}