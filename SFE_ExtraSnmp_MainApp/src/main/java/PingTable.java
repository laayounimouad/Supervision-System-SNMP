import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class PingTable extends AbstractTableModel{
	public static int count=0;
	//tableau de type trap
	 private final Agent[] Agent;
	 //en tete du tableau
	    private final String[] entetes = {"Ip", "Nom", "Etat Ping"};
		
	    public PingTable(){
	        super();	        //array list qui va contenir les elememts a afficher en jtable
	        ArrayList<Agent> al = new ArrayList();
	        ArrayList<String> alIp = new ArrayList();
	        alIp=new Array_IP().Arrayip();
	        for(String el: alIp)
	        	al.add(new Agent(new Ip(el)));
	        
			 Agent = new Agent[al.size()];
	        int i = 0;
	        while (i < al.size()){
	            Agent[i] = al.get(i);
	            i++;
	        }   
	    }
	    public int getRowCount() {
	        return Agent.length;
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
	                return Agent[rowIndex].getIp();
	            case 1:
	                return Agent[rowIndex].getIp().name();
	            case 2:
	                return Agent[rowIndex].getEtatPing();
	            default:
	                return null; //Ne devrait jamais arriver
	        }
	    }

	
}
