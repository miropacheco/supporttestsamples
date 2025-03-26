
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class MyLdapTest {
	private String domain;
	private String ldapHost;
	private String searchBase;
	private static final Logger LOGGER = Logger.getLogger(MyLdapTest.class.getName());

	public MyLdapTest() {
		this.domain = "YOUR DOMAIN";
		this.ldapHost = "ldap://localhost:10386";
		this.searchBase = "OU=Users;OU=System"; // YOUR SEARCH BASE IN LDAP
	}

	public MyLdapTest(String domain, String host, String dn) {
		this.domain = domain;
		this.ldapHost = host;
		this.searchBase = dn;
	}

	public Map<String, Object> authenticate(String user, String pass) {
		long startTime = System.nanoTime();
		String returnedAtts[] = { "sn", "givenName", "name", "userPrincipalName", "displayName", "memberOf" };
		String searchFilter = "(&(objectClass=user)(sAMAccountName=" + user + "))";

		// Create the search controls
		SearchControls searchCtls = new SearchControls();
		searchCtls.setReturningAttributes(returnedAtts);

		// Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapHost);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, user);
		env.put(Context.SECURITY_CREDENTIALS, pass);

		LdapContext ctxGC = null;

		try {

			// This is the actual Authentication piece. Will throw
			// javax.naming.AuthenticationException
			// if the users password is not correct. Other exceptions may include IO (server
			// not found) etc.
			ctxGC = new InitialLdapContext(env, null);

			// Now try a simple search and get some attributes as defined in returnedAtts
			NamingEnumeration<SearchResult> answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				Attributes attrs = sr.getAttributes();
				Map<String, Object> amap = null;
				if (attrs != null) {
					amap = new HashMap<String, Object>();
					NamingEnumeration<?> ne = attrs.getAll();
					while (ne.hasMore()) {
						Attribute attr = (Attribute) ne.next();
						if (attr.size() == 1) {
							amap.put(attr.getID(), attr.get());
						} else {
							HashSet<String> s = new HashSet<String>();
							NamingEnumeration n = attr.getAll();
							while (n.hasMoreElements()) {
								s.add((String) n.nextElement());
							}
							amap.put(attr.getID(), s);
						}
					}
					ne.close();
				}
				ctxGC.close(); // Close and clean up
				return amap;
			}
		} catch (NamingException nex) {
			nex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if ((System.nanoTime() - startTime) / 1000000 > 100)
			System.out.println("Total time on thread:" + Thread.currentThread().getId() + "="
					+ (System.nanoTime() - startTime) / 1000000);

		return null;
	}

	public static void main(String s[]) {

		Handler handlerObj = new ConsoleHandler();
		handlerObj.setLevel(Level.ALL);
		int TOTALREQUESTS = 1;
		int TOTALTHEADS = 1;
		try {
			/*
			 * 
			 * ExecutorService exec = Executors.newFixedThreadPool(TOTALTHEADS); try { while
			 * (true) {
			 * 
			 * for (int i = 0; i < TOTALREQUESTS; i++) {
			 * 
			 * Runnable worker = new Runnable() {
			 * 
			 * public void run() { // TODO Auto-generated method stub
			 * 
			 * MyLdapTest t = new MyLdapTest("","ldaps://localhost:10636","cn=ou=system");
			 * t.authenticate("uid=admin,ou=system", "secret");
			 * 
			 * 
			 * }
			 * 
			 * };
			 * 
			 * exec.execute(worker); }
			 * 
			 * try { Thread.sleep(5000); break; } catch (Exception e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); break; }
			 * 
			 * } exec.shutdown(); while (!exec.isTerminated())
			 * 
			 * {
			 * 
			 * } System.out.println("\nFinished all threads");
			 */
			MyLdapTest t = new MyLdapTest("", System.getProperty("server", "ldap://localhost:10389"), "");
			t.authenticate("uid=admin,ou=system", "secrtt");

		} catch (Exception e) {
			System.out.println("Got Exepcption in main");

		}

	}

}