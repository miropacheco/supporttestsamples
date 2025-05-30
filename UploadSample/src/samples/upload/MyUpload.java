package samples.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class MyUpload
 */
@WebServlet("/MyUpload")
@MultipartConfig
public class MyUpload extends HttpServlet {
	private final static Logger LOGGER = Logger.getLogger(MyUpload.class
			.getCanonicalName());
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyUpload() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getParameter("Signature");
		response.getWriter().println("Answer");
	
	}
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getOutputStream()				.write("<html lang=\"en\">    <head>        <title>File Upload</title>        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">    </head>    <body>        <form method=\"POST\" action=\"http://localhost:7878/UploadSample/MyUpload\" enctype=\"multipart/form-data\" >            File:            <input type=\"file\" name=\"file\" id=\"file\" /> <br/>            Destination:            <input type=\"text\" value=\"/tmp\" name=\"destination\"/>            </br>            <input type=\"submit\" value=\"MyUpload\" name=\"upload\" id=\"upload\" />        </form>    </body></html>"						.getBytes());
		response.getOutputStream().write("http://mcclpa01:7878/edge_dev/FRM_0000080146/DirectUploads/testing/test.csv?Signature=gvSj79ZsihEplxTOFB%2BSn4X4YKE%3D&Expires=1468621102&AWSAccessKeyId=AKIAJLKSG6M7AJSICLLA&x-amz-server-side-encryption=AES256&x-amz-acl=authenticated-read".getBytes());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		// Create path components to save the file
		final String path = request.getParameter("destination"); 
		final Part filePart = request.getPart("file");
		final String fileName = getFileName(filePart);

		OutputStream out = null;
		InputStream filecontent = null;
		final PrintWriter writer = response.getWriter();

		try {
			out = new FileOutputStream(new File(path + File.separator
					+ fileName));
			filecontent = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			writer.println("New file " + fileName + " created at " + path);
			LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
					new Object[] { fileName, path });
		} catch (FileNotFoundException fne) {
			writer.println("You either did not specify a file to upload or are "
					+ "trying to upload a file to a protected or nonexistent "
					+ "location.");
			writer.println("<br/> ERROR: " + fne.getMessage());

			LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
					new Object[] { fne.getMessage() });
		} finally {
			if (out != null) {
				out.close();
			}
			if (filecontent != null) {
				filecontent.close();
			}
			if (writer != null) {
				writer.close();
			}
		}

	}

	private String getFileName(final Part part) {
		final String partHeader = part.getHeader("content-disposition");
		LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;
	}

}
