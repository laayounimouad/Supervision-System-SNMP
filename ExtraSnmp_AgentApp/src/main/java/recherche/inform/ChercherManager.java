package recherche.inform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.snmp4j.smi.IpAddress;

import main.Main;
import trap.SnmpTrap;
	
public class ChercherManager extends Thread{
	
	static ArrayList<NetworkInterface> PourTest=new ArrayList<NetworkInterface>();
	static ArrayList<NetworkInterface> PourRecherche=new ArrayList<NetworkInterface>();
	static InetAddress addresseReseau;
	static InetAddress addresse=null;
	static String user_computerName="";

	IpAddress address=null;
	static File f;
	static BufferedReader br ;
	static FileReader fr;
	public boolean isFinished=false;
	public boolean isFinished() {
		return isFinished;
	}
	public IpAddress getAddress() {
		return address;
	}
	private static boolean stop=false;  
	public void setStop() {
		stop=true;
	}
	public boolean getStop() {
		return stop;
	}
	public void resetstop() {
		stop=false;
	}
	public void run() {
		System.out.println("commence");
		address=chercher();
		
	System.out.println("finished");
	isFinished=true;
	}

	public static boolean detecterChangement(Main m) {
		while(true) {
			PourTest.clear();
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		Enumeration e1 = null;
		try {
			e1 = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e11) {
			// TODO Auto-generated catch block
			e11.printStackTrace();
		}
		
		while(e1.hasMoreElements())
		{
		    NetworkInterface n1 = (NetworkInterface) e1.nextElement();
		    try {
		    	if(n1.isUp()) {
		    		PourTest.add(n1);


				
		    	}
			} catch (SocketException e11) {
				// TODO Auto-generated catch block
				e11.printStackTrace();
			}
		}
		System.out.println(PourRecherche.size()+""+PourTest.size());
		if(PourRecherche.size()!=PourTest.size()) {
			PourRecherche.clear();
			PourRecherche.addAll(PourTest);
			 return true;
		}
		if(m.address!=null) return false;
	}
	}
	public static InetAddress getCurrentAddress() {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("google.com", 80));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return socket.getLocalAddress();
		
	}
 
	public static void  setInformContenu() {
 		 user_computerName="";
 		ProcessBuilder username=new ProcessBuilder("cmd","/c","set","USERNAME");
		Process psUser;
		try {
			psUser = username.start();

	 		BufferedReader inUser = new BufferedReader(new InputStreamReader(psUser.getInputStream()));
			String lineUser;
			user_computerName+=inUser.readLine().split("=")[1];
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		ProcessBuilder computername=new ProcessBuilder("cmd","/c","set","COMPUTERNAME");
		Process pscomputer;
		try {
			pscomputer = computername.start();

	 		BufferedReader incomputer = new BufferedReader(new InputStreamReader(pscomputer.getInputStream()));
			String linecomputer;
			user_computerName+=":"+incomputer.readLine().split("=")[1];
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
	}
	
	
 	public static IpAddress chercherAvecFichier() throws IllegalArgumentException{
 		ProcessBuilder pb=new ProcessBuilder("cmd","/c","set","homedrive");
		pb.redirectErrorStream(true);
			Process ps = null;
			try {
				ps = pb.start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			String line = null;
			try {
				line=in.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if(line==null) f=new File("C:\\SnmpAgentAddresse\\addresses.txt");
		else f = new File(line.split("=")[1].substring(0, 2)+"\\SnmpAgentAddresse\\addresses.txt");
	    if (!f.exists())
	      return null;
	    try {
    	br = new BufferedReader(new FileReader(f));
    	line=null;
    	
			while((line=br.readLine())!=null) {
				SnmpTrap inform = new SnmpTrap(line,
						"1.3.6.1.4.1.2789.2005.1={s}"+user_computerName+"",2);
					inform.doTrap();
				if(inform.getInformReceived()) return new IpAddress(line);

			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
 	}
 	
 	public static IpAddress chercher() {

 		setInformContenu();
 		
 		List f;
 		addresse=null;
		ProcessBuilder pb=new ProcessBuilder("ping","-4","SnmpMaager");
		pb.redirectErrorStream(true);
		try {
			Process ps=pb.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			String line;
			
			while ((line = in.readLine()) != null) {
				if(line.contains("Ping request could not find")) {
					System.out.println("Ping request could not find");
				}
				else if(line.startsWith("Pinging")) {
					SnmpTrap inform = new SnmpTrap(line.split("\\[")[1].split("\\]")[0],
							"1.3.6.1.4.1.2789.2005.1={s}"+user_computerName+"",2);
						inform.doTrap();
					if(inform.getInformReceived()) return new IpAddress(line.split("\\[")[1].split("\\]")[0]);

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(stop) return null;
		IpAddress resultat5=null;
		System.out.println("qweee");
		try {
		resultat5=chercherAvecFichier();
		}catch(IllegalArgumentException e) {
			System.out.println("inside");
		}
		if(resultat5!=null) 		return resultat5;
		System.out.println("wertzui");
		SnmpTrap inform = new SnmpTrap("127.0.0.1",
				"1.3.6.1.4.1.2789.2005.1={s}"+user_computerName+"",2);
			inform.doTrap();
		if(inform.getInformReceived()) return new IpAddress("127.0.0.1");
		if(stop) return null;
 		IpAddress resultat=null;;
 		addresse=getCurrentAddress();
 	if(addresse!=null)
		 resultat=chercherAddresse(addresse.toString().split("/")[1]);
		
		
		
		if(resultat!=null) 		return resultat;
		if(stop) return null;
//		rechreche dans les autres interfaces qui ont des addresses privée
		Enumeration e = null;
		try {
			e = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PourRecherche.clear();
		while(e.hasMoreElements())
		{
		    NetworkInterface n = (NetworkInterface) e.nextElement();
		    Enumeration ee = n.getInetAddresses();
		  		
		    try {
		    	if(n.isUp()) {
		    		
		    		PourRecherche.add(n);
		    		
				    System.out.println(n);
				    while (ee.hasMoreElements())
				    {
				    	if(stop) return null;
				        InetAddress i = (InetAddress) ee.nextElement();
				        if(i instanceof Inet4Address) {
				        	System.out.println(i);

				        	if(!i.equals(addresse) || addresse==null) {
				        		 if (i.isSiteLocalAddress()) {
				        			 System.out.println("recherche started and finished");
				        				IpAddress resultat1=chercherAddresse(i.toString().split("/")[1]);
				        				if(resultat1!=null) 		return resultat1;

				        	        }

				        	}
				        }
				    }
		    	}
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    
		    
		}
		
		
//		 e = null;
//		try {
//			e = NetworkInterface.getNetworkInterfaces();
//		} catch (SocketException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		while(e.hasMoreElements())
//		{
//		    NetworkInterface n = (NetworkInterface) e.nextElement();
//		    Enumeration ee = n.getInetAddresses();
//		  		
//		    try {
//		    	if(n.isUp()) {
//				    System.out.println(n);
//				    while (ee.hasMoreElements())
//				    {
//
//				        InetAddress i = (InetAddress) ee.nextElement();
//				        if(i instanceof Inet4Address) {
//				        	System.out.println(i);
//
//				        	if(!i.equals(addresse)) {
//				        		 if (i.isLinkLocalAddress()) {
//				        			 IpAddress resultat1=chercherAddresse(i.toString().split("/")[1]);
//				        				if(resultat1!=null) 		return resultat1;				        	        }
//
//				        	}
//				        }
//				    }
//		    	}
//			} catch (SocketException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		    
//		    
//		}
	return null;	
 	}

 	public static IpAddress chercherAddresse(String network) {

	network=network.substring(0, addresse.toString().lastIndexOf("."));
 		for (int i=0;i<255;i++) {

 			System.out.println(new StringBuffer(network).append(i)+"");
			SnmpTrap inform = new SnmpTrap(new StringBuffer(network).append(i)+"",
					"1.3.6.1.4.1.2789.2005.1={s}"+user_computerName+"",2);
				inform.doTrap();
				if(inform.getInformReceived()) return new IpAddress(new StringBuffer(network).append(i)+"");
		}


 		return null;
 		
 	}
}
