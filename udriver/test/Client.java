/*=====================================================*/
/*                                                     */
/* Copyright (c) University of Warwick 2005            */
/*                                                     */
/*=====================================================*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.Integer;
import java.net.Socket;
import java.net.URL;

/** Simple client
 */

public class Client {
  
    public Client  (int port) {

	try {
	    URL url = new URL("http://localhost:" + port + "/get?test");
	    String reply = _readText(url.openStream());
	    System.out.println("Reply = \"" + reply + "\"");
	}
	catch(Exception e) {
	    e.printStackTrace();
	    System.err.println(e);
	}
    }

    // Main program
    public static void main(String[] args) {
	System.out.println("Starting client on port " + args[0]);
	Client s = new Client(Integer.parseInt(args[0]));
    }

    private static String _readText(InputStream inputStream) {
	try{
	    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	    char[]       cbuff = new char[2048];
	    StringBuffer sbuff = new StringBuffer();
	    int len;
	    while((len = inputStreamReader.read(cbuff)) != -1){
		sbuff.append(cbuff, 0, len);
	    }
	    return sbuff.toString();
	}
	catch(Exception e){
	    System.out.println(e);
	    return "_readText failed";
	}
    }

 }


