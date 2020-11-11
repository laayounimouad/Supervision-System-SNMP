import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTable;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import com.mysql.jdbc.StringUtils;

public class SnmpWalkTable {
	
	ArrayList<info_OID> WalkTable=new ArrayList<info_OID>();
	private String adresse;
	private String[][] Tab;
	private String Oid;
	
	public static enum TypeWalk {
		App_instaled,Info_general,Services,Interfaces,Rcpu,Rbandwith,Rinterface,sinfo,sip,sinterface,soid,Info_stockage,RouterAddressListe;
	}
	public SnmpWalkTable(String adresse) {	
		this.adresse=adresse;
	}
	
	public void setoid(String Oid) {
		this.Oid=Oid;
	}
	public ArrayList<info_OID> getWalkTable() {
		return WalkTable;
	}
	public void setWalkTable(ArrayList<info_OID> WalkTable) {
		this.WalkTable = WalkTable;
	}
	
	public void addOIDWalkTable(info_OID vb) {
		WalkTable.add(vb);
	}
	public String afficherWalkTable() {
        String walkT = "";
		for(info_OID a : WalkTable)
			walkT=walkT+""+a;
		 return walkT.split("=")[2].replaceAll("\\]","");

		}
	
	public void inittab(int n,String oid) {
		ArrayList<info_OID> arr=faitWalk(oid);
		Tab=new String[arr.size()][n];
		arr.clear();
	}
	public void inittab(int n,int m) {
		Tab=new String[m][n];
	}
	public void afficherTab() {
		for(int i=0;i<Tab.length;i++) {
			for(int j=0;j<Tab[i].length;j++) {
				System.out.print("=>"+Tab[i][j]);
			}
			System.out.println();
		}
	}
	public void remplirColonne(int colonne,String oid) {
		int i=0;
		ArrayList<info_OID> arr=new ArrayList<info_OID>();
		arr =faitWalk(oid);
		Iterator<info_OID> lio=arr.iterator();
		//"1.3.6.1.2.1.25.6.3.1.1"
		while(lio.hasNext()) {
			Tab[i][colonne]=lio.next().getVariable().toString();
			i++;
		}
		arr.clear();
	}
	public ArrayList<info_OID> faitWalk(String oid){
		
		SnmpWalk indexWalk = new SnmpWalk(adresse,oid);
		return indexWalk.doWalk().getWalkTable();
	}
	public info_OID faitGet(String oid) {
		SnmpGet get=new SnmpGet(adresse, oid);
		return get.Get();
	}
	public JTable jtableWalk(TypeWalk typewalk) {
		if(typewalk==TypeWalk.App_instaled) {
			String [] col= {"id","nom","date"};
			String [] oid= {"1.3.6.1.2.1.25.6.3.1.1","1.3.6.1.2.1.25.6.3.1.2","1.3.6.1.2.1.25.6.3.1.5"};
			return jtableWalk(col, oid);
		}
		if(typewalk==TypeWalk.Info_general) {
			String [] nom_info = {"system description","system Up Time Instance","date du system","system Contact",
					"system Name","System Location","RAM"};
			String [] oid = {"1.3.6.1.2.1.1.1.0","1.3.6.1.2.1.1.3.0","1.3.6.1.2.1.25.1.2.0","1.3.6.1.2.1.1.4.0",
					"1.3.6.1.2.1.1.5.0","1.3.6.1.2.1.1.6.0","1.3.6.1.2.1.25.2.2.0"};
			String [] temp = {"",""};
			return new JTable(infoTable(nom_info, oid), temp);
		}
		if(typewalk==TypeWalk.Info_stockage) {
			String [] col= {"index","Storage Description","Type",
					"Taille de Stockage","Taille utilisé","Allocation Erreur"
					
			};
			String [] oid = {"1.3.6.1.2.1.25.2.3.1.1","1.3.6.1.2.1.25.2.3.1.3","1.3.6.1.2.1.25.2.3.1.2",
					"1.3.6.1.2.1.25.2.3.1.5","1.3.6.1.2.1.25.2.3.1.6","1.3.6.1.2.1.25.2.3.1.7"
			};
			return jtableWalk(col, oid);
		}
		if(typewalk==TypeWalk.RouterAddressListe) {
			String [] col = {"Addresse IP","Addresse MAC","type"};
			String [] oid = {"1.3.6.1.2.1.4.22.1.3","1.3.6.1.2.1.4.22.1.2","1.3.6.1.2.1.4.22.1.4"};
			return jtableWalk(col, oid);
		}
		if(typewalk==TypeWalk.Interfaces) {
			String [] col= {"nom","type","Mac Address",
					"Status","Données entré","Données sortie","Speed","erreurs","traps enabled"
					};
			String [] oid = {"1.3.6.1.2.1.55.1.5.1.2","1.3.6.1.2.1.31.1.1.1.18","1.3.6.1.2.1.2.2.1.6",
					"1.3.6.1.2.1.2.2.1.8","1.3.6.1.2.1.2.2.1.10","1.3.6.1.2.1.2.2.1.15","1.3.6.1.2.1.31.1.1.1.15","1.3.6.1.2.1.2.2.1.13","1.3.6.1.2.1.31.1.1.1.14"};
			return jtableWalk(col, oid, calculer_index("1.3.6.1.2.1.55.1.5.1.2"));
		}
		if(typewalk==TypeWalk.Rcpu) {
			String [] col= {"cpu"};
			String [] oid= {"1.3.6.1.4.1.9.2.1.58"};
			return jtableWalk(col, oid);
		}
		if(typewalk==TypeWalk.Rbandwith) {
			String [] col= {"memory"};
			String [] oid= {"1.3.6.1.4.1.9.9.48.1.1.1.6.1"};
			return jtableWalk(col, oid);
		}
		if(typewalk==TypeWalk.Rinterface) {
			String [] col = {"in","out","speed"};
			String [] oid = {"1.3.6.1.2.1.2.2.1.10.0",".1.3.6.1.2.1.2.2.1.16.0","1.3.6.1.2.1.2.2.1.5.0"};
			return jtableWalk(col, oid);
		}
		if(typewalk==TypeWalk.sinfo) {
			String [] nom_info = {"system description","system Up Time Instance","date du system","system Contact",
					"system Name","System Location"};
			String [] oid = {"1.3.6.1.2.1.1.1.0","1.3.6.1.2.1.1.3.0","1.3.6.1.2.1.25.1.2.0","1.3.6.1.2.1.1.4.0",
					"1.3.6.1.2.1.1.5.0","1.3.6.1.2.1.1.6.0"};
			String [] temp = {"",""};
			return new JTable(infoTable(nom_info, oid), temp);
		}
		if(typewalk==TypeWalk.sip) {
			String [] col= {"Adresse ip","net mask","adresse brodcast","taille max en byte"};
			String [] oid= {"1.3.6.1.2.1.4.20.1.1","1.3.6.1.2.1.4.20.1.3","1.3.6.1.2.1.4.20.1.4","1.3.6.1.2.1.4.20.1.5"};
			return jtableWalk(col, oid);
		}
		if(typewalk==TypeWalk.sinterface) {
			String [] col= {"index","description","speed byte/s","statut operations"};
			String [] oid= {"1.3.6.1.2.1.2.2.1.1","1.3.6.1.2.1.2.2.1.2","1.3.6.1.2.1.2.2.1.5","1.3.6.1.2.1.2.2.1.8"};
			return jtableWalk(col, oid);
		}
		if(typewalk==TypeWalk.sip) {
			String [] col= {"interfaces"};
			String [] oid= {"1.3.6.1.2.1.4.20.1.1"};
			return jtableWalk(col, oid);
		}
		if(typewalk==TypeWalk.soid) {
			String [] col= {"resultat"};
			String [] oid= {this.Oid};
			return jtableWalk(col, oid);
		}
		return null;
	}
	public String[] calculer_index(String oid) {
		int i=0;
		ArrayList<info_OID> arr=faitWalk(oid);
		String [] temp= new String[arr.size()];
		Iterator<info_OID> lio=arr.iterator();
		
		while(lio.hasNext()) {
			info_OID t=lio.next();
			temp[i]=t.getOid().split("\\.")[t.getOid().split("\\.").length-1];
					i++;
		}
		arr.clear();
		return temp;
	}
	public String[][] infoTable(String [] nom_info,String [] oid){
		inittab(2, oid.length);
		for(int i=0;i<oid.length;i++) {
			Tab[i][0]=nom_info[i];
			Tab[i][1]=faitGet(oid[i]).getVariable();
		}
		return Tab;
		
	}
	public JTable jtableWalk(String[] col,String[] oid,String[] oid_a_afficher) {
		inittab(col.length,oid_a_afficher.length);
		for(int i=0;i<oid.length;i++) {
			{
				int z=0;
				ArrayList<info_OID> arr=new ArrayList<info_OID>();
				arr =faitWalk(oid[i]);
				Iterator<info_OID> lio=arr.iterator();
				while(lio.hasNext()) {
					info_OID temp=lio.next();
					for(int x=0;x<oid_a_afficher.length;x++) {
						if(temp.getOid().split("\\.")[temp.getOid().split("\\.").length-1].equals(oid_a_afficher[x])) {
						Tab[x][i]=temp.getVariable();
						}
					}
				}
				arr.clear();
			}
		}
		return new JTable(Tab,col);

	}
	public JTable jtableWalk(String[] col,String[] oid) {
		inittab(col.length,oid[0]);
		for(int i=0;i<oid.length;i++) {
			remplirColonne(i,oid[i]);
		}
		
		return new JTable(Tab,col);
	}	
}
