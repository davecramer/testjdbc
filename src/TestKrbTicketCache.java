import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class TestKrbTicketCache {

    private static String CONFIG_ITEM_NAME = "ticketCache";
    private static String KRBLOGIN_MODULE = "com.sun.security.auth.module.Krb5LoginModule";

    /**
     * Equivalent of:
     *
     * {@code
     *
     * ticketCache {
     * com.sun.security.auth.module.Krb5LoginModule required
     * refreshKrb5Config=false
     * useTicketCache=true
     * doNotPrompt=true
     * useKeyTab=false
     * renewTGT=false
     * isInitiator=false debug=true; };
     *
     * }
     *
     */
    static class CustomKrbConfig extends Configuration {

        @Override
        public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
            if (CONFIG_ITEM_NAME.equals(name)) {
                Map<String, String> options = new HashMap<>();
                options.put("refreshKrb5Config", Boolean.FALSE.toString());
                options.put("useTicketCache", Boolean.TRUE.toString());
                options.put("doNotPrompt", Boolean.TRUE.toString());
                options.put("useKeyTab", Boolean.FALSE.toString());
                options.put("isInitiator", Boolean.FALSE.toString());
                options.put("renewTGT", Boolean.FALSE.toString());
                options.put("debug", Boolean.TRUE.toString());
                return new AppConfigurationEntry[] {
                        new AppConfigurationEntry(KRBLOGIN_MODULE,
                                LoginModuleControlFlag.REQUIRED, options) };
            }
            return null;
        }

    }

    private static void setupConfiguration() {
        Configuration.setConfiguration(new CustomKrbConfig());
    }

    boolean credentialCacheExists() {
        LoginContext lc = null;
        try {
            lc = new LoginContext(CONFIG_ITEM_NAME, new CallbackHandler() {

                @Override
                public void handle(Callback[] callbacks)
                        throws IOException, UnsupportedCallbackException {
                    // config has doNotPrompt, so it should never happen
                    throw new RuntimeException("Should not happen!");
                }

            });
            lc.login();
        } catch (LoginException e) {
            System.out.println("LoginException: cache not existing?");
            return false;
        }
        Subject sub = lc.getSubject();
        return sub != null;
    }

    public static void main(String[] args) throws Exception {
        setupConfiguration();
        TestKrbTicketCache test = new TestKrbTicketCache();
        System.out.println("cache exists: " + ( test.credentialCacheExists() ? "yes" : "no"));
    }

}
