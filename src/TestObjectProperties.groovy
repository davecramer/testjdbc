import java.sql.DriverManager

/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 13-01-28
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
class TestObjectProperties
{
    public static void main(String[] args) throws Exception {
      Class.forName("org.postgresql.Driver");
      Properties info = new Properties();
      def foo = new Object()
      def blah = foo.toString()
      info.put("foo", new Object());      // info.getPropert("foo") will return null
      DriverManager.getConnection("foo:bar//baz", info);
  }
}
