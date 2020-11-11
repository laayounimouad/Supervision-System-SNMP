
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.snmp4j.smi.IpAddress;

public class ListeUtilisateurs {

	static private HashMap<IpAddress, Utilisateurs> liste =new HashMap<IpAddress, Utilisateurs>();
	public ListeUtilisateurs() {
		
	}
	public ListeUtilisateurs (HashMap<IpAddress, Utilisateurs> liste) {
		ListeUtilisateurs.liste=liste;
	}
	public static HashMap<IpAddress, Utilisateurs> getListe() {
		return liste;
	}
	public static void resetListe() {
		Collection<Utilisateurs> c=ListeUtilisateurs.getListe().values();
		c.forEach(e->resetUtilisateur(e));
	}
	public static void resetUtilisateur(Utilisateurs u) {
		
		Ip ip=new Ip(u.getIpAddress().toString());
		String name=ip.name();
		if(!name.contains("Name not found")) {
			u.setStatue(true);
			u.setSnmpExist(true);
			
		}
		else {
			Ping p=new Ping(ip);
			try {
				if(p.sendPingRequest()) {
					u.setStatue(true);
					u.setSnmpExist(false);
					
				}
				else {
					u.setStatue(false);
				}
			} catch (UnknownHostException e) {
				u.setStatue(false);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				u.setStatue(false);
				e.printStackTrace();
			}
			u.setBandwidth(null);
			u.setInternetStatue(null);
			u.setDownload(null);u.setUpload(null);u.setPing(null);
		}
		
		
		Application.getStatueTable().setModel(new TableUtilisateursModel());

	}
	public static void setListe(HashMap<IpAddress, Utilisateurs> liste) {
		ListeUtilisateurs.liste = liste;
	}
	public static boolean addToListe(Utilisateurs u,IpAddress address) {
		if(liste.containsKey(address)) return false;
		u.setName(new Ip(address.toString()).name());
		liste.put(address,u);
		resetUtilisateur(u);
		Application.getStatueTable().setModel(new TableUtilisateursModel());
		return true;
	}
	public static Utilisateurs deletefromListe(IpAddress address) {
		Utilisateurs u=liste.remove(address);
				if(Application.getStatueTable()!=null)Application.getStatueTable().setModel(new TableUtilisateursModel());

		return u; 
	}
	public static void remplirListe() {
		ArrayList<String>  al=new Array_IP().Array();
		Iterator<String> it=al.iterator();
		while(it.hasNext()) {
			String n=it.next();
			Utilisateurs u=new Utilisateurs(new IpAddress(n.split(":")[1]));
			Ip ip=new Ip(n.split(":")[1]);
			String name=ip.name();
			if(!name.contains("Name not found")) {
				u.setStatue(true);
				u.setSnmpExist(true);
			}
			else {
				Ping p=new Ping(ip);
				try {
					if(p.sendPingRequest()) {
						u.setStatue(true);
						u.setSnmpExist(false);
					}
					else {
						u.setStatue(false);
					}
				} catch (UnknownHostException e) {
					u.setStatue(false);
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					u.setStatue(false);
					e.printStackTrace();
				}
			}
			 u.setName(n.split(":")[0]);
			liste.put(new IpAddress(n.split(":")[1]), u);
			
		}

	}
	public static void updateUtilisateur(IpAddress ipAddress,String data) {
		Utilisateurs u=liste.get(ipAddress);
		if(u==null) {
			u=new Utilisateurs(ipAddress);
			u.setName(new Ip(ipAddress.toString()).name());;
			liste.put(ipAddress,u);
		}
		u.setStatue(true);
		u.setSnmpExist(true);
		if(data.contains("internet")) {
			if(data.contains("internet_up")) {
				u.setInternetStatue(true);
				u.setBandwidth(null);
				u.setDownload(null);
				u.setUpload(null);
				u.setPing(null);
				}
			if(data.contains("internet_down")) {
				u.setInternetStatue(false);
				u.setBandwidth(null);
				u.setDownload(null);
				u.setUpload(null);
				u.setPing(null);
			}
			if(data.contains("internet tres lente")) {
				u.setInternetStatue(true);
				u.setBandwidth("Debit faible");
			}
		}
		else if(data.contains("download")) u.setDownload(data);
		else if(data.contains("upload")) u.setUpload(data);
		else if(data.contains("ping")) u.setPing(data);
		
		if(u.getPing()!=null && u.getDownload()!=null && u.getUpload()!=null) { u.setBandwidth();}
			
		if(Application.getStatueTable()!=null)Application.getStatueTable().setModel(new TableUtilisateursModel());
	}	
}