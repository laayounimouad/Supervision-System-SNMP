

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import org.snmp4j.smi.IpAddress;

public class TableUtilisateursModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Object[][] data ;
	static Iterator<IpAddress> it;
	static int i=0;
	String[] columnNames = {"addresse","name",
            "Connecté au réseaux",
            "Snmp Exist",
            "Internet Statue",
            "Bandwidth"};
	
	 public TableUtilisateursModel() {
		 data = new Object[ListeUtilisateurs.getListe().keySet().size()][6];
			it= ListeUtilisateurs.getListe().keySet().iterator();
			i=0;
			ListeUtilisateurs.getListe().values().forEach(e->{
				System.out.println(i);
				data[i][0]=it.next();
				
				data[i][1]=e;
				
//							if(e.getStatue()!=null)	data[i][2]=(e.getStatue()==false)?"Non connecte":"Connecte";
							if(e.getStatue()!=null)	data[i][2]=(e.getStatue()==false)?"Non":"Oui";
							else data[i][2]="En attente";
							
							if((e.getSnmpExist()!=null)) data[i][3]=(e.getSnmpExist()==false)?"Non":"Oui";
							else data[i][3]="En attente";
							
							if(e.getInternetStatue()!=null)data[i][4]=(e.getInternetStatue()==false)?"Non connecte":"Connecte";
							else data[i][4]="En attente";
							
						data[i][5]=(e.getBandwidth()!=null)?e.getBandwidth():"En attente";
							
					i++;
			});
			
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd '=>' HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());
			Application.lblRefresh.setText("derniere actualistaion : "+formatter.format(date));
	}
	 @Override
	    public String getColumnName(int col) {
	        return columnNames[col];
	    }
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.length;
	}
	@Override
	 public boolean isCellEditable(int row, int col)
     { return false	; }
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
		return data[rowIndex][columnIndex];
	}
	public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

}
