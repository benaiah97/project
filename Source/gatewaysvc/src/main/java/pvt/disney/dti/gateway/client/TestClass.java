package pvt.disney.dti.gateway.client;

import pvt.disney.dti.gateway.util.UtilityXML;

public class TestClass {

	public static void main(String[] args) {

		String testString = "<?xml version=\"1.0\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"" + "" + " xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> <soapenv:Header/> <soapenv:Body> " + " <tns:Command xmlns:quer=\"http://services.nexus.wdw.com/NexusTicketingService/types/QueryTicketRequest\"" + "	xmlns:tns=\"http://services.nexus.wdw.com/NexusTicketingService/types/QueryTicketAnswer\"> " + "<tns:Header><tns:ReferenceNumber>999720510</tns:ReferenceNumber><tns:RequestNumber>893516" + "</tns:RequestNumber><tns:SessionId>893516</tns:SessionId><tns:RequestType>Query</tns:RequestType>" + "<tns:RequestSubType>QueryTicket</tns:RequestSubType><tns:ValueMsg><tns:ValuedQty>0</tns:ValuedQty>" + "<tns:NonValuedQty>0</tns:NonValuedQty></tns:ValueMsg></tns:Header><tns:QueryTicket><tns:Error>" + "<tns:ErrorCode>2</tns:ErrorCode><tns:ErrorShortDescription>OPER_FAILED</tns:ErrorShortDescription>" + "<tns:ErrorDescription>CANNOT FIND ANY REQUESTED TICKET</tns:ErrorDescription></tns:Error>" + "<tns:TicketInfo><tns:Item>1</tns:Item><tns:ItemStatus>201</tns:ItemStatus><tns:Ticket><tns:TDSSN>" + "<tns:Site>PDW</tns:Site><tns:Station>CAS005</tns:Station><tns:Date>2013-01-09</tns:Date>" + "<tns:TicketId>8</tns:TicketId></tns:TDSSN></tns:Ticket></tns:TicketInfo></tns:QueryTicket>" + "<tns:Gateway><tns:LicenceId>7</tns:LicenceId><tns:IPAddress>10.89.49.207</tns:IPAddress>" + "<tns:MACAddress>00-50-56-C0-00-08</tns:MACAddress></tns:Gateway></tns:Command></soapenv:Body>" + "</soapenv:Envelope>";

		// Namespace?
		int envelope = testString.indexOf(":Envelope");
		int firstTagLt = testString.substring(0, envelope).lastIndexOf("<");
		System.out
				.println("Tag starts at " + firstTagLt + " and :Envelope starts at " + envelope);
		String nameSpace = testString.substring(firstTagLt + 1, envelope);
		System.out.println("Namespace is " + nameSpace);

		// Location of Command opening tag?
		int bodyStart = testString.indexOf("<" + nameSpace + ":Body>");
		int bodyStartLength = new String("<" + nameSpace + ":Body>").length();
		int commandStart = testString.indexOf("<", bodyStart + bodyStartLength);

		// Location of Command Closing tag?
		int bodyEnd = testString.indexOf("</" + nameSpace + ":Body>");
		int commandEnd = bodyEnd;

		String requestSubstring = testString
				.substring(commandStart, commandEnd);

		System.out.println("Response is: [" + requestSubstring + "]");

		String soapFault = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"" + " xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Header/><soapenv:Body>" + "<soapenv:Fault><faultcode>soapenv:Server</faultcode><faultstring>java.lang.NullPointerException</faultstring>" + "</soapenv:Fault></soapenv:Body></soapenv:Envelope>";

		System.out.println(UtilityXML.getTagData(soapFault, "faultcode"));
		System.out.println(UtilityXML.getTagData(soapFault, "faultstring"));

	}

}
