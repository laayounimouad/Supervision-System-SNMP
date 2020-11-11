	// Java Program to Ping an IP address 
	import java.io.*; 
	import java.net.*; 
	  
public class Ping {
private static Ip ip;

	  public Ping(Ip ip) {
	super();
	this.ip = ip;
}

	// Sends ping request to a provided IP address 
	  public boolean sendPingRequest() throws UnknownHostException, IOException 
	  { 
	    InetAddress geek = InetAddress.getByName(ip.toString()); 
	   System.out.println("Sending Ping Request to " + ip.toString()); 
	    if (geek.isReachable(5000)) { 
	    return true;
	    }
	    else
	     return false;
	  } 
	  
	} 

