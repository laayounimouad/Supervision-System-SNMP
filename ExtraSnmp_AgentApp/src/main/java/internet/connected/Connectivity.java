package internet.connected;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import org.snmp4j.mp.SnmpConstants;

import main.Main;




public class Connectivity extends Thread{
	
	private static boolean deconnected=false;
	private static boolean connected=false;
	private static boolean statue=false;
	private static boolean stop=false;  
	public boolean getConnected() {
		return connected;
	}
	public boolean getDeconnected() {
		return deconnected;
	}
	public boolean getStatue() {
		return statue;
	}
	public void setStop() {
		stop=true;
	}
	public boolean getStop() {
		return stop;
	}
	public void resetStop() {
		stop=false;
	}
	
	 public  void run() 
	    { 
		 try {
	         URL url = new URL("http://www.google.com");
	         URLConnection connection = url.openConnection();
	         connection.connect();
	         System.out.println("Internet is connected");
	         statue=true;
	         connected=true;
	         vrai();
	      } catch (Exception e) {
	         System.out.println("Internet is not connected");
	         statue=false;
	         deconnected=true;
	         faux();
	      }
	    } 
	public static void vrai() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		while(!stop) {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
		         URL url = new URL("http://www.google.com");
		         URLConnection connection = url.openConnection();
		         connection.connect();
		      } catch (Exception e) {
		         System.out.println("Internet is not connected");
		         statue=false;
		         deconnected=true;
		         connected=false;
		         faux();
		      }
		}
	}
	public static void faux() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		while(!stop) {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
		         URL url = new URL("http://www.google.com");
		         URLConnection connection = url.openConnection();
		         connection.connect();
		         System.out.println("Internet is connected");
		         statue=true;
		         connected=true;
		         deconnected=false;
		         vrai();
		      } catch (Exception e) {
		        
		      }
		}
	}
}