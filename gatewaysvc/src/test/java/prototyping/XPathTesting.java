/**
 * 
 */
package prototyping;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author smoon
 *
 */
public class XPathTesting {
	static Document document = null;
	
	/**
	 * Test xpath.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testXpath() throws Exception {
		
        
     //List list = document.selectNodes("//QueryTicket");
     
	//grab the root node
     Node rootNode = document.selectSingleNode( "/" );
     Logger.getAnonymousLogger().info("rootNode=" + rootNode.asXML()+"\n");
     
     //grab a single query ticket node (in this case we only have one anyway)
     Node queryTicketNode = document.selectSingleNode("/QueryTicket");
     Logger.getAnonymousLogger().info("queryTicketNode=" + queryTicketNode.asXML() +"\n");
    
     //try to grab a non-existant node
     Node noSuchNode = document.selectSingleNode("/NoSuchNode");
   	 Logger.getAnonymousLogger().info("value when selecting non-existant node noSuchNode=='"+noSuchNode + "'\n");

    //grabe the value of an elment in the queryticket node
    String valUbergeek = queryTicketNode.valueOf( "ubergeek" );
    Logger.getAnonymousLogger().info("child element of QueryTicketNode valUbergeek='" + valUbergeek +"'\n");
    
    //try to grab the value of a non existant element in the root node
    String valNoSuchValue = rootNode.valueOf( "NoSuchValue" );
    Logger.getAnonymousLogger().info("nonexistant child element value valNoSuchValue='" + valNoSuchValue +"'\n");

	}
	/**
	 * 
	 * Test xpath.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testXpath2() throws Exception {
		Logger.getAnonymousLogger().info("Testing xpath2");
		String xml="<?xml version=\"1.0\" ?>" +
			
 "<Envelope>" +
 "<Header>" +
  "<MessageID>289157</MessageID> " +
  "<MessageType>Orders</MessageType> " +
  "<SourceID>WDPRONADLR</SourceID> " +
  "<TimeStamp>2009-05-20 14:09:09</TimeStamp> " +
  "<EchoData /> " +
  "<SystemFields /> " +
  "</Header>" +
 "<Body>" +
  "<Username /> " +
  "<Password /> " +
  "<OrderID>DLR774195903</OrderID> " +
  "<GalaxyOrderID>2109163</GalaxyOrderID> " +
  "<Status>2</Status> " +
  "<AuthCode>677704</AuthCode> " +
  "<ResponseText>APPROVED 677704</ResponseText> " +
  "<AVSResultCode>Z</AVSResultCode> " +
  "<CVNResultCode></CVNResultCode> " +
  "</Body>" +
  "</Envelope>";
		org.dom4j.Document response = DocumentHelper.parseText(xml);
	    String galaxyOrderID = response.valueOf( "//GalaxyOrderID" );
	    String authCode= response.valueOf( "//AuthCode" );
	    String ResponseText= response.valueOf( "//ResponseText" );
	    String AVSResultCode= response.valueOf( "//AVSResultCode" );
	    String CVNResultCode= response.valueOf( "//CVNResultCode" );
	    Logger.getAnonymousLogger().info("galaxyOrderID = " + galaxyOrderID);
	    Logger.getAnonymousLogger().info("authCode = " + authCode);
	    Logger.getAnonymousLogger().info("ResponseText = " + ResponseText);
	    Logger.getAnonymousLogger().info("AVSResultCode = " + AVSResultCode);
	    Logger.getAnonymousLogger().info("CVNResultCode = " + CVNResultCode);
	    
	    
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		document = DocumentHelper.createDocument();
        Element root = document.addElement( "QueryTicket" );

        Element ticket1 = root.addElement( "Ticket" )
            .addAttribute( "code", "TDJ01" )
            .addAttribute( "location", "WDW" )
            .addText( "MYW3D Uber Pass" );
        
        Element ticket2 = root.addElement( "Ticket" )
            .addAttribute( "code", "CBY01" )
            .addAttribute( "location", "AK" )
    .addText( "Base Ticket" );
       
        Element ubergeek = root.addElement( "ubergeek" ).addText( "Shannon" );
        
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
	static void prettyPrint() {
	    try {
			XMLWriter writer = new XMLWriter( OutputFormat.createPrettyPrint() );
			writer.write( document );
			writer.close();
		} catch (UnsupportedEncodingException e) {
			// TO DO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TO DO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}
}
