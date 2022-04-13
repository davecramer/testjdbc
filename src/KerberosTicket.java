
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class KerberosTicket {
    public static void main(String[] args) {

        String value = System.getProperty("java.security.auth.login.config");
        long iterations = 10000;
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++ ) {
            credentialCacheExists("test1");
        }
        System.out.println( " java time (ns): " + ((System.nanoTime() - startTime) / iterations));

        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++ ) {
            nativeCredentialCacheExists();
        }
        System.out.println( " native time (ns): " + ((System.nanoTime() - startTime) / iterations));

    }
    /*
    static {
        Configuration.setConfiguration(new CustomKrbConfig());
    }
*/
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
                options.put("com.sun.security.auth.module.Krb5LoginModule", "required");
                options.put("refreshKrb5Config", Boolean.FALSE.toString());
                options.put("useTicketCache", Boolean.TRUE.toString());
                options.put("doNotPrompt", Boolean.FALSE.toString());
                options.put("useKeyTab", Boolean.TRUE.toString());
                options.put("isInitiator", Boolean.FALSE.toString());
                options.put("renewTGT", Boolean.FALSE.toString());
                options.put("debug", Boolean.FALSE.toString());
                return new AppConfigurationEntry[] {
                        new AppConfigurationEntry(KRBLOGIN_MODULE,
                                LoginModuleControlFlag.REQUIRED, options) };
            }
            return null;
        }

    }
    public static boolean credentialCacheExists(String principal) {
        LoginContext lc = null;
        Configuration.setConfiguration(new CustomKrbConfig());
        Configuration configuration = Configuration.getConfiguration();
        AppConfigurationEntry[] entries = configuration.getAppConfigurationEntry(CONFIG_ITEM_NAME);

        try {
            lc = new LoginContext(CONFIG_ITEM_NAME, new CallbackHandler() {

                @Override
                public void handle(Callback[] callbacks)
                        throws IOException, UnsupportedCallbackException {
                    for (Callback callback : callbacks) {
                        if (callback instanceof NameCallback) {
                            NameCallback nc = (NameCallback) callback;
                            nc.setName(principal);
                        }
                    }
                }

            });
            lc.login();
        } catch (LoginException e) {
            return false;
        }
        Subject sub = lc.getSubject();
        return sub != null;
    }

    public static boolean nativeCredentialCacheExists() {
        try {
            @SuppressWarnings({"nullness"})
            sun.security.krb5.Credentials credentials =
                    sun.security.krb5.Credentials.acquireTGTFromCache(null, null);
            return credentials != null;
        } catch (Exception ex) {
            return false;
        }
    }
}
