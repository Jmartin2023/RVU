package objects;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Utility {
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		
	}
	
	public HashMap<String, String> getCredentials(String path, String[] params) throws SAXException, IOException, ParserConfigurationException {
		
		File xmlFile = new File(path);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);
		
		Element credentialElement = (Element) doc.getElementsByTagName("credentials").item(0);
	    
		HashMap<String, String> credentials = new HashMap<String, String>(params.length);
		int i=0;
		for(String param : params) {
			String text = credentialElement.getElementsByTagName(param).item(0).getTextContent();
			credentials.put(param, text);
			i++;
		}
		
		return credentials;		
	}

}
