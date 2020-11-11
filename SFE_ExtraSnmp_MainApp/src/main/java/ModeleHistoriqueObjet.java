import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class ModeleHistoriqueObjet extends AbstractTableModel {

	//tableau de type trap
	 private  Trap[] trap=null;
	 //en tete du tableau
	    private final String[] entetes = {"Adresse IP", "Trap", "Date/Time"};
	    Trap t;
	    java.util.Date date = new Date();
		Object param = new java.sql.Date(date.getTime());
	    public ModeleHistoriqueObjet(String Hdate,String Hip){
	        super();	        //array list qui va contenir les elememts a afficher en jtable
	        ArrayList<Trap> alTrap = new ArrayList();
	        Connection con=(Connection) DbConnection.getConnection();
			Statement stmt = null;
			try {
				stmt = (Statement) con.createStatement();
				ResultSet rs=stmt.executeQuery("SELECT oid.id_oid,IP,text_oid,Date_time FROM oid,agent,posseder where oid.id_oid=posseder.id_oid and posseder.ID=agent.ID ORDER BY Date_Time ASC");
				while(rs.next()) {
				        t= new Trap(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getTimestamp(4).toString());

					String date=t.getDate_Time().split(" ")[0].toString();
					if(Hip.equals("") &&  date.equals(Hdate)) alTrap.add(t); 
					else if(Hdate.equals("--") && t.getIp_port().equals(Hip)) alTrap.add(t); 
					else if(t.getIp_port().equals(Hip) && date.equals(Hdate)) alTrap.add(t);
				}
				}
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				}
			
			 trap = new Trap[alTrap.size()];
	        int i = 0;
	        while (i < alTrap.size()){
	            trap[i] = alTrap.get(i);
	            i++;
	        }
	        
	   
	    }
			
	    public int getRowCount() {
	        try{
	        	return trap.length;
	        }catch(Exception e) {
	        	return 0;
	        }
	    }
	 
	    public int getColumnCount() {
	        return entetes.length;
	    }
	 
	    public String getColumnName(int columnIndex) {
	        return entetes[columnIndex];
	    }
	 
	    public Object getValueAt(int rowIndex, int columnIndex) {
	        switch(columnIndex){
	            case 0:
	                return trap[rowIndex].getIp_port();
	            case 1:
	                return trap[rowIndex].getMsg_trap();
	            case 2:
	                return trap[rowIndex].getDate_Time();
	            default:
	                return null; //Ne devrait jamais arriver
	        }
	    }

	
	}
	