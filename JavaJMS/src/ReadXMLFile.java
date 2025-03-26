import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ReadXMLFile {
	public static void main(String[] args) {

	  SAXBuilder builder = new SAXBuilder();
	  File xmlFile = new File("c:\\tmp\\wsdl\\AuthenticationService.wsdl");

	  try {

		Document document = (Document) builder.build(xmlFile);
		Element e = document.getRootElement();
		XMLOutputter out = new XMLOutputter();
		out.setFormat(Format.getPrettyFormat());
		out.output(document, new FileWriter("c:\\tmp\\file.xml"));
	  
	  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }	}
}
