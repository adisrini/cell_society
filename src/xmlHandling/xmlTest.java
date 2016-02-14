package xmlHandling;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public class xmlTest {
  private Document document;

  public xmlTest() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.newDocument();
    }catch (ParserConfigurationException parserException) {
      parserException.printStackTrace();
    }

    Element root = document.createElement("root");
    document.appendChild(root);

    // add comment to XML document
    Comment simpleComment = document.createComment("This is a simple contact list");
    root.appendChild(simpleComment);

    // add child element
    Node contactNode = createContactNode(document);
    root.appendChild(contactNode);

    // add processing instruction
    ProcessingInstruction pi = document.createProcessingInstruction("myInstruction",
        "action silent");
    root.appendChild(pi);

    // add CDATA section
    CDATASection cdata = document.createCDATASection("I can add <, >, and ?");
    root.appendChild(cdata);

    // write the XML document to disk
    try {

      // create DOMSource for source XML document
      Source xmlSource = new DOMSource(document);

      // create StreamResult for transformation result
      Result result = new StreamResult(new FileOutputStream("myDocument.xml"));

      // create TransformerFactory
      TransformerFactory transformerFactory = TransformerFactory.newInstance();

      // create Transformer for transformation
      Transformer transformer = transformerFactory.newTransformer();

      transformer.setOutputProperty("indent", "yes");

      // transform and deliver content to client
      transformer.transform(xmlSource, result);
    }

    // handle exception creating TransformerFactory
    catch (TransformerFactoryConfigurationError factoryError) {
      System.err.println("Error creating " + "TransformerFactory");
      factoryError.printStackTrace();
    }catch (TransformerException transformerError) {
      System.err.println("Error transforming document");
      transformerError.printStackTrace();
    }    catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  public Node createContactNode(Document document) {

    // create FirstName and LastName elements
    Element firstName = document.createElement("FirstName");
    Element lastName = document.createElement("LastName");

    firstName.appendChild(document.createTextNode("First Name"));
    lastName.appendChild(document.createTextNode("Last Name"));

    // create contact element
    Element contact = document.createElement("contact");

    // create attribute
    Attr genderAttribute = document.createAttribute("gender");
    genderAttribute.setValue("F");

    // append attribute to contact element
    contact.setAttributeNode(genderAttribute);
    contact.appendChild(firstName);
    contact.appendChild(lastName);

    return contact;
  }

  public static void main(String args[]) {
    xmlTest buildXml = new xmlTest();
  }
}