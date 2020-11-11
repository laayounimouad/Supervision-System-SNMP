import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Array_IP {
	ArrayList<String> al = new ArrayList<String>();

	public ArrayList<String> Array() {
		ArrayList<String> al = new ArrayList<String>();
		 Connection con=(Connection) DbConnection.getConnection();
			Statement stmt = null;
			Boolean ok=false;
			try {
				stmt = (Statement) con.createStatement();
				ResultSet rs=stmt.executeQuery("SELECT IP,nom from agent");
				while(rs.next()) {
					Ip ip=new Ip(rs.getString(1));
					if(rs.getString(2)==null)
				         al.add(ip.name()+" :"+ip);
					else al.add(rs.getString(2)+" :"+ip);
				         System.out.println(ip);
			                     } 
				}
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
	                                  }
			this.al=al;
			return al;
}
	
	public ArrayList<String> Arrayip() {
		ArrayList<String> al = new ArrayList<String>();
		 Connection con=(Connection) DbConnection.getConnection();
			Statement stmt = null;
			Boolean ok=false;
			try {
				stmt = (Statement) con.createStatement();
				ResultSet rs=stmt.executeQuery("SELECT IP from agent");
				while(rs.next()) {
					Ip ip=new Ip(rs.getString(1));
				         al.add(ip.toString());
			                     } 
				}
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
	                                  }
			this.al=al;
			return al;
}


	
	}
