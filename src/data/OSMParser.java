package data;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class OSMParser {

	private MapData data;

	/**
	 * Creates a new OSMParser object
	 */

	public OSMParser(){}

	/**
	 * Parses the specified file and returns it's data in a MapData object
	 * @param f The File to be parsed
	 * @return The MapData object containing the data from the parsed file
	 */
	public MapData parse(File f) throws IOException, ParserConfigurationException, SAXException{

		data = new MapData();

		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(false);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		OSMHandler handler = new OSMHandler();
		xmlReader.setContentHandler(handler);
		InputStream stream = null;

		try {
			stream = new FileInputStream(f);
			InputSource source = new InputSource(stream);
			xmlReader.parse(source);
		} catch(IOException x) {
			throw x;
		} finally {
			if(stream != null)
				stream.close();
		}

		return data;
	}

	class OSMHandler extends DefaultHandler {

		private Element lastElement;

		/** Current character data. */
		private String cdata;

		/** Attributes of the current element. */
		private Attributes attributes;

		/**
		 * Get the most recently encountered CDATA.
		 */
		public String getCdata() {
			return cdata;
		}

		/**
		 * Get the attributes of the most recently encountered XML element.
		 */
		public Attributes getAttributes() {
			return attributes;
		}

		/**
		 * Method called by SAX parser when start of document is encountered.
		 */
		public void startDocument() {}

		/**
		 * Method called by SAX parser when end of document is encountered.
		 */
		public void endDocument() {}

		/**
		 * Method called by SAX parser when start tag for XML element is
		 * encountered.
		 */
		public void startElement(String namespaceURI, String localName,
								 String qName, Attributes atts) {

			if(qName.equals("bounds")){
				Bounds bounds = new Bounds(atts.getValue("maxlon"), atts.getValue("minlon"),
						atts.getValue("maxlat"), atts.getValue("minlat"));
				data.setBounds(bounds);
			}

			if(qName.equals("node")){
				Node node = new Node(atts.getValue("id"), atts.getValue("lat"), atts.getValue("lon"));
				data.addElement(node);
				lastElement = node;
			}

			if(qName.equals("way")){
				Way way = new Way(atts.getValue("id"));
				data.addElement(way);
				lastElement = way;
			}

			if(qName.equals("nd")){
				if(lastElement instanceof Way){
					Way w = (Way) lastElement;
					String nodeRef = atts.getValue("ref");
					w.addNode(data.getNode(nodeRef));
				}
			}

			if(qName.equals("relation")){
				Relation relation = new Relation(atts.getValue("id"));
				data.addElement(relation);
				lastElement = relation;
			}

			if(qName.equals("member")){
				if(lastElement instanceof Relation){
					Relation relation = (Relation) lastElement;
					if(atts.getValue("type").equals("way"))
						relation.addMember(data.getWay(atts.getValue("ref")));
					if(atts.getValue("type").equals("node"))
						relation.addMember(data.getNode(atts.getValue("ref")));
					if(atts.getValue("type").equals("relation"))
						relation.addMember(data.getRelation(atts.getValue("ref")));
				}
			}

			if(qName.equals("tag")){
				lastElement.addTag(atts.getValue("k"), atts.getValue("v"));
			}
		}

		/**
		 * Method called by SAX parser when end tag for XML element is
		 * encountered.  This can occur even if there is no explicit end
		 * tag present in the document.
		 */
		public void endElement(String namespaceURI, String localName,
							   String qName) throws SAXParseException {}

		/**
		 * Method called by SAX parser when character data is encountered.
		 */
		public void characters(char[] ch, int start, int length)
				throws SAXParseException {
			cdata = (new String(ch, start, length)).trim();
		}
	}
}
