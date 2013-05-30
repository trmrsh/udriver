/*=====================================================*/
/*                                                     */
/* Copyright (c) University of Warwick 2005            */
/*                                                     */
/*=====================================================*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Integer;
import java.net.Socket;
import java.net.ServerSocket;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

// XML stuff

import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/** Server for testing some aspects of Ultracam client.
 */

public class Server {
  
    public Server (int port) {

	try {
	    
	    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder        domBuilder = domFactory.newDocumentBuilder(); 
	    Document newDoc = domBuilder.newDocument();

	    // Root element
            Element rootElement = newDoc.createElement("response");
            newDoc.appendChild(rootElement);

	    Element sourceElement = newDoc.createElement("source");
	    Text textNode = newDoc.createTextNode("Camera server");
	    sourceElement.appendChild(textNode);
	    rootElement.appendChild(sourceElement);

	    Element statusElement = newDoc.createElement("status");
	    statusElement.setAttribute("camera",   "WARNING");
	    statusElement.setAttribute("software", "WARNING");
	    rootElement.appendChild(statusElement);

	    Element stateElement = newDoc.createElement("state");
	    stateElement.setAttribute("camera",   "ERROR");
	    stateElement.setAttribute("software", "ERROR");
	    rootElement.appendChild(stateElement);

            // Transform & send the document to the terminal
            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = tranFactory.newTransformer();

	    StringWriter stringWriter = new StringWriter();
            aTransformer.transform(new DOMSource(newDoc), new StreamResult(stringWriter));

	    String message = stringWriter.toString();

	    ServerSocket ss = new ServerSocket(port);
	    
	    for(;;){
		Socket         client = ss.accept();
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		_readText(in);

		System.out.println("Attempting response to client");

		PrintWriter   out = new PrintWriter(client.getOutputStream());
		out.print("HTTP/1.1 200 \r\n");
		out.print("Content-Type: text/plain\r\n");

		out.print("Content-Length: " + message.length() + "\r\n\r\n");
		out.print(message);
	    
		System.out.println("Server responded to request on port " + port);

		out.close();
		in.close();
		client.close();
	    }	
	}
	catch(Exception e) {
	    e.printStackTrace();
	    System.err.println(e);
	}
    }

    // Main program
    public static void main(String[] args) {
	System.out.println("Starting server on port " + args[0]);
	Server s = new Server(Integer.parseInt(args[0]));
    }

    /** Read from an InputStream into a string */
    private static void _readText(BufferedReader input) {
	try{
	    String line;
	    System.out.println("");
	    while((line = input.readLine()) != null && !line.equals("")){
		System.out.println(line);
	    }
	    System.out.println(line);
	}
	catch(Exception e){
	    System.out.println(e);
	}
    }
 }


