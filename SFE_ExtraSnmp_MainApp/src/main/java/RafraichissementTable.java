import java.util.TimerTask;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class RafraichissementTable extends TimerTask{

	       private boolean etat;
		   private JTable table;
		   private boolean Statue=false;
		   
		   public RafraichissementTable(JTable table,boolean EmailEtat){
		      this.table=table;
		      this.etat=EmailEtat;
		      Statue=false;
		      
		   }
		   public RafraichissementTable(JTable table){
			      this.table=table;
			      Statue=true;
			   }


			@Override
		public void run() {
				if(Statue==false)
			// TODO Auto-generated method stub
					try{
			    table.setModel(new ModeleStatiqueObjet()); 
			}catch(Exception e){
				 table.setModel(null); 
				e.printStackTrace();
			}
				else if(Statue==true) {
					ListeUtilisateurs.resetListe();
					
					table.setModel(new TableUtilisateursModel());
					
					table.getColumnModel().getColumn(0).setPreferredWidth(20);
					 table.getColumnModel().getColumn(1).setPreferredWidth(40);
					 table.getColumnModel().getColumn(2).setPreferredWidth(35);
					 table.getColumnModel().getColumn(3).setPreferredWidth(20);
					 table.getColumnModel().getColumn(4).setPreferredWidth(20);
					 table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				}
				     
			 
		}

}
