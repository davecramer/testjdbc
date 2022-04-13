import java.sql.Connection;
import java.sql.DriverManager;
/*
jdbc:postgresql://p.3gvvpq22wzhmrek3ygepnqnt6m.db.postgresbridge.com/postgres?user=postgres&password=cM8SThRDIxkmzbOnKNIeCpL8fpuWyZL38C1Qnpsu366Y2zswk8YTmogGSOIerNB5&ssl=true
 */
public class CrunchyBridge {
    public static void main(String[] args) {
        String host = "p.xnspqxup4zd7dhlr2zc7jp23me.db.postgresbridge.com";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://p.3gvvpq22wzhmrek3ygepnqnt6m.db.postgresbridge.com/postgres?user=postgres&password=cM8SThRDIxkmzbOnKNIeCpL8fpuWyZL38C1Qnpsu366Y2zswk8YTmogGSOIerNB5&sslmode=prefer")){

        }catch ( Exception ex ){
            ex.printStackTrace();
        }

    }
}
