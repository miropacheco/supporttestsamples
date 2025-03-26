
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class HelloWorld extends AbstractHandler {
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// Declare response encoding and types
		response.setContentType("text/html; charset=utf-8");

		// Declare response status code
		response.setStatus(HttpServletResponse.SC_OK);

		// Write back response
	    /*try {
			Thread.currentThread().sleep(200000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.in.read();
		response.getWriter().println("<h1>Hello World 123</h1>");

		// Inform jetty that this request has now been handled
		baseRequest.setHandled(true);
	}

	public static void main(String[] args) throws Exception {

		Server server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8999);

		HttpConfiguration https = new HttpConfiguration();
		https.addCustomizer(new SecureRequestCustomizer());

		/*SslContextFactory sslContextFactory = new SslContextFactory(System.getProperty("javax.net.ssl.keyStore"));
		sslContextFactory.setKeyStorePassword(System.getProperty("javax.net.ssl.keyStorePassword"));
		sslContextFactory.setKeyStoreType(System.getProperty("javax.net.ssl.keyStoreType"));
		sslContextFactory.setExcludeCipherSuites(".*NULL.*", ".*RC4.*", ".*MD5.*", ".*DES.*", ".*DSS.*");
		sslContextFactory.setIncludeCipherSuites("TLS_DHE_RSA.*", "TLS_ECDHE.*");

		ServerConnector sslConnector = new ServerConnector(server,
				new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));

		sslConnector.setPort(8989);
		*/
		server.setConnectors(new Connector[] { connector });

		server.setHandler(new HelloWorld());
		server.setRequestLog(new RequestLog() {

			@Override
			public void log(Request arg0, Response arg1) {
				// TODO Auto-generated method stub
				try {
					InputStream is = arg0.getInputStream();
					while (true) {
						byte ret = (byte) is.read();
						if (ret == -1)
							break;
						char c = (char) (ret & 0xFF);
						System.out.print(c);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println(arg0);

			}
		});

		server.start();
		server.join();
	}
}
