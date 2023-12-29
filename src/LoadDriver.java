public class LoadDriver {
    public static void main(String[] args) throws ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
    }
}
