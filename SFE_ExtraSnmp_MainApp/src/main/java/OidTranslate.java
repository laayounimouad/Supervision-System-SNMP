import java.text.DecimalFormat;

import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

public class OidTranslate {

	public static String translate(String pdu) { 
		String[] tab; String ch = "";
	//tab=pdu.toString().split("VBS\\[")[1].split(";")[0].split("=");
		int t=pdu.toString().split("VBS\\[")[1].split(";").length;
		for(int i=0;i<t;i++) {
			tab=pdu.toString().split("VBS\\[")[1].split(";")[i].split("=");
			if(!isNumeric(tab[1])) {
			ch=ch+" "+tab[1];
			ch=ch.replaceAll("\\]", "");
					}
			}
		
		return ch;
		}
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	    	strNum=strNum.replaceAll("\\s", "");
	        int d = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static String generic(String ch) {
		if(ch.split("\\,")[3].split("\\=")[1].equals("0")) return "Cold Start";
		else if(ch.split("\\,")[3].split("\\=")[1].equals("1")) return"Warm Start";
		else if(ch.split("\\,")[3].split("\\=")[1].equals("2")) return"Link Down";
		else if(ch.split("\\,")[3].split("\\=")[1].equals("3")) return"Link Up";
		else if(ch.split("\\,")[3].split("\\=")[1].equals("4")) return"Authentification Failure !!";
		else if(ch.split("\\,")[3].split("\\=")[1].equals("5")) return"egp Neighbbor Loss";
		else if(ch.split("\\,")[3].split("\\=")[1].equals("6")) return"Enterprise Specific";
		return null;
		//V1TRAP[reqestID=0,timestamp=3 days, 21:50:50.20,enterprise=1.3.6.1.4.1.311.1.1.3.1.1,genericTrap=2,specificTrap=0, VBS[1.3.6.1.2.1.2.2.1.1.28 = 28]]
		//			if(command.getType()==PDU.V1TRAP) { PDUv1 commandv1=(PDUv1) command; System.out.println(commandv1.getGenericTrap());}
		
	}
	public static String bytesToGbytes(String var) {
		return new DecimalFormat("#.##").format((((Double.parseDouble(var)/1024)/1024)/1024));
	}
	public static String kbytesToGbytes(String var) {
			return new DecimalFormat("#.##").format(((Double.parseDouble(var)/1024)/1024))+"";
		}
	public static String getStatus(String var) {
		if(var.equalsIgnoreCase("1")) return "up";
		if(var.equalsIgnoreCase("2")) return "down";
		if(var.equalsIgnoreCase("3")) return "testing";
		if(var.equalsIgnoreCase("4")) return "unkown";
		if(var.equalsIgnoreCase("5")) return "dormant";
		if(var.equalsIgnoreCase("6")) return "notPresent";
		if(var.equalsIgnoreCase("7")) return "lowerLayerDown";
		return var;
	}
	public static String translateContenu(VariableBinding vb,String ip) {
		String oid=vb.getOid().toString();
		String var=vb.getVariable().toString();
		return translateContenu(oid,var,ip);
	}
	public static String storageType(String var) {
		if(var.equalsIgnoreCase("1.3.6.1.2.1.25.2.1.2")) return "RAM";
		if(var.equalsIgnoreCase("1.3.6.1.2.1.25.2.1.3")) return "Virtual Memory";
		if(var.equalsIgnoreCase("1.3.6.1.2.1.25.2.1.4")) return "Fixed Disk";
		if(var.equalsIgnoreCase("1.3.6.1.2.1.25.2.1.5")) return "Removable Disk";
		if(var.equalsIgnoreCase("1.3.6.1.2.1.25.2.1.6")) return "Floppy Disk";
		if(var.equalsIgnoreCase("1.3.6.1.2.1.25.2.1.7")) return "Compact Disc";
		if(var.equalsIgnoreCase("1.3.6.1.2.1.25.2.1.8")) return "Ram Disk";
		if(var.equalsIgnoreCase("1.3.6.1.2.1.25.2.1.9")) return "Flash Memory";
		if(var.equalsIgnoreCase("1.3.6.1.2.1.25.2.1.10")) return "Network Disk";
		else return "Autre";
	}
	public static double getStorageAllocationUnits(String oid,String ip) {
		try {
			SnmpGet g=new SnmpGet(ip, oid);
			Double d=Double.parseDouble(g.Get().getVariable());
			
		return d;
		}catch(Exception e) {
			return 1;
		}
	}
	public static String translateContenu(String oid,String var,String ip) {
//		if(oid.startsWith("1.3.6.1.2.1")) {
			if(oid.startsWith("1.3.6.1.2.1.2.2.1")) {
				if(oid.startsWith("1.3.6.1.2.1.2.2.1.5")) {
					return kbytesToGbytes((Double.parseDouble(var)/8)+"")+" Mb/s";
				}
				if(oid.startsWith("1.3.6.1.2.1.2.2.1.7") || oid.startsWith("1.3.6.1.2.1.2.2.1.8")) {
					return getStatus(var);
				}
				if(oid.startsWith("1.3.6.1.2.1.2.2.1.10") || oid.startsWith("1.3.6.1.2.1.2.2.1.15")) {
					return kbytesToGbytes(var)+" Mb";
				}
				
			}
			if(oid.startsWith("1.3.6.1.2.1.4")) {
				if(oid.startsWith("1.3.6.1.2.1.4.22.1.4")) {
					if(var.equalsIgnoreCase("1")) return "Other";
					if(var.equalsIgnoreCase("2")) return "Invalid";
					if(var.equalsIgnoreCase("3")) return "Dynamic";
					if(var.equalsIgnoreCase("4")) return "Static";
				}
			}
			if(oid.startsWith("1.3.6.1.2.1.25")) {
				if(oid.startsWith("1.3.6.1.2.1.25.2.2")) {
						return kbytesToGbytes(var)+" Gb";
				}
				if(oid.startsWith("1.3.6.1.2.1.25.2.3.1")) {
					if(oid.startsWith("1.3.6.1.2.1.25.2.3.1.2")) {
							return storageType(var);
					}
					if(oid.startsWith("1.3.6.1.2.1.25.2.3.1.5") || oid.startsWith("1.3.6.1.2.1.25.2.3.1.6")) {
						return bytesToGbytes(((getStorageAllocationUnits("1.3.6.1.2.1.25.2.3.1.4."+oid.charAt(oid.length()-1), ip.split("/")[0]))*Double.parseDouble(var))+"")+" Gb";
					}
				}
			}
			if(oid.startsWith("1.3.6.1.2.1.31.1.1.1")) {

				if(oid.startsWith("1.3.6.1.2.1.31.1.1.1.14")) {
					if(var.equalsIgnoreCase("1")) return "enabled";
					if(var.equalsIgnoreCase("2")) return "disabled";
				}
				if(oid.startsWith("1.3.6.1.2.1.31.1.1.1.15")) {
					return kbytesToGbytes((Double.parseDouble(var)*125000)+"")+" Mb/s";
				}
				if(oid.startsWith("1.3.6.1.2.1.31.1.1.1.17")) {
					if(var.equalsIgnoreCase("1")) return "oui";
					if(var.equalsIgnoreCase("2")) return "non";
				}
				
			}
//		}
		return var;
	}

}
