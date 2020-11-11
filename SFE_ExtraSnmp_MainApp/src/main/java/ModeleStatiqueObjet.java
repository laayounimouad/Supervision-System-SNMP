import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class ModeleStatiqueObjet extends AbstractTableModel {

	public static int count=0;
	//tableau de type trap
	 private  Trap[] trap;
	 //en tete du tableau
	    private final String[] entetes = {"Adresse IP", "Trap", "Date/Time"};
	    Trap t;
	    java.util.Date date = new Date();
		Object param = new java.sql.Date(date.getTime());
	    public ModeleStatiqueObjet(){
			super();	        //array list qui va contenir les elememts a afficher en jtable
	        this.trap = null;
	        ArrayList<Trap> alTrap = new ArrayList();
	        Connection con=(Connection) DbConnection.getConnection();
			Statement stmt = null;
			try {
				stmt = (Statement) con.createStatement();
				ResultSet rs=stmt.executeQuery("SELECT oid.id_oid,IP,text_oid,Date_time FROM oid,agent,posseder where oid.id_oid=posseder.id_oid and posseder.ID=agent.ID ORDER BY Date_Time ASC");
				while(rs.next()) {
				        t= new Trap(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getTimestamp(4).toString());
				        if(t.getDate_Time().contains(param.toString())) alTrap.add(t);}
				         if (Application.isEtat() && count!=alTrap.size())
                         if(alTrap.size()!=0 && count!=0) { new SendEmail("ExtraSnmp trap", "Bonjour,veuillez trouvez ci-dessouss une trap generer par votre reseau:\n\n\n"+alTrap.get(alTrap.size()-1).toString()); 							
				         }
				         count=alTrap.size();
				}
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return ;
				}
			
			 trap = new Trap[alTrap.size()];
	        int i = 0;
	        while (i < alTrap.size()){
	            trap[i] = alTrap.get(i);
	            i++;
	        }
	        
	   
	    }
			
	    public int getRowCount() {
	    	try {
	        return trap.length;
	    	}catch(Exception e) {
	    		e.printStackTrace();
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
	
	