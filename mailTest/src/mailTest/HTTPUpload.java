package mailTest;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.protocol.Protocol;

public class HTTPUpload {
	public static void main(String s[]) {
		String fileString = "c:/temp/samplefile.txt";
		String protocol = "http";
		String host = "localhost";
		String api = "/api";
		int port = 5575;
		String sessionId = "d232223123123";
		String accessId = "accessId";
		;
		String parent = "parent";
		String directUpload = "directUpload";
		String executionMode = "synchron";

		String urlTemplate = "%s://%s:%d%s?sessionId=%s&accessId=%s&parent=%s&directUpload=%s&executionMode=synchron";

		String url = String.format(urlTemplate, protocol, host, port, api, sessionId, accessId, parent, directUpload,
				executionMode);

		int responseCode = -1;
		String response = "not recieved";
		try {
			File file = new File(fileString);
			if (!file.exists()) {
				throw new RuntimeException("file: '" + file.getAbsolutePath() + "' not accessable");
			}

			PostMethod filePost = new PostMethod(url);

			// internally used InputStream for sending file
			Part[] parts = { new FilePart("content", file.getName(), file) };
			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));

			// -----------------------
			@SuppressWarnings("deprecation")
			Protocol ignorehttpsCertificateValidation = new Protocol("https", new EasySSLProtocolSocketFactory(), port);
			Protocol.registerProtocol("https", ignorehttpsCertificateValidation);
			// -----------------------

			HttpClient client = new HttpClient();

			responseCode = client.executeMethod(filePost);
			response = filePost.getResponseBodyAsString();

			// System.out.println(response);
		} catch (Exception e) {
			throw new RuntimeException("Could not upload document using file: '" + fileString + "' protocol: '"
					+ protocol + "' host: '" + host + "' port: '" + port + "' accessId: '" + accessId + "' sessionId: '"
					+ sessionId + "' parent: '" + parent + "' responseCode: '" + responseCode + "' response: '"
					+ response + "'", e);
		}
	}

}
