import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.snmp4j.smi.IpAddress;

import com.mysql.jdbc.Connection;

public class Ip {

	private String ip;
	public Ip (String ip) {
		this.ip=ip;
	}
	public String getIp() {
		return ip;
	}

	public boolean verify() {
		boolean etat=true;
		 String[] parties = this.ip.split("\\.");
		 System.out.println(parties.length);
		 if(parties.length==4) {
		  //La plage d'un nombre est entre 0 et 255
		  for(int i = 0 ; i < 4; i++){
		      //Convertir en entier et tester 
			  try {
		      if(Integer.parseInt(parties[i])<0 || Integer.parseInt(parties[i])>255)
		      etat=false;
			  }catch (Exception e) {
				etat=false;
			}
		  }
		 }else etat=false;
		 
		  return etat;		
	}
	
//	public void add() {
//		
//		
//		
//		//ajouter dans la base de donnees
//		Connection con=(Connection) DbConnection.getConnection();
//		String query1 = " insert into agent (IP,id_admin)"
//		        + " values (?,?)";
//	  PreparedStatement preparedStmt1;
//	try {
//		preparedStmt1 = con.prepareStatement(query1);
//	      preparedStmt1.setString (1,ip.toString());
//	      preparedStmt1.setString(2, "1");
//	      preparedStmt1.execute();
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	}
	
	public boolean compare(ArrayList<String> arrayList,String ip) {
		Boolean result=false;
		for(String a : arrayList ) {
			if(a.toString().equals(ip)) {result=true; return result;}
		}
		return result;
		
	}
	@Override
	public String toString() {
		return ip+"";
	}
	//modifie par mouad 
	
	public String name() {
		info_OID s,s1;
		try {
		SnmpGet get=new SnmpGet(ip,"1.3.6.1.2.1.1.4.0");
		s=get.Get();
		if(!s.getVariable().equals(""))return s.getVariable();
		else {
			SnmpGet get2=new SnmpGet(ip,"1.3.6.1.2.1.1.5.0");
			s1=get2.Get();
		}
		}catch (Exception e) {
			return "Name not found";
		}
		return s1.getVariable();
	}
	
	public String mac() {
		return ip+"";
	}
	
	
}
