import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.UIManager;

import org.snmp4j.smi.IpAddress;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.awt.SystemColor;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class DecouverteReseauFrame implements Runnable {


	/**
	 * Create the application.
	 */
	public DecouverteReseauFrame() {
		
		JFrame frame = new JFrame();
		frame.getContentPane().setBackground(new Color(51, 102, 153));
		frame.setResizable(false);
		frame.setTitle("Extra Snmp");
		frame.setBounds(100, 100, 518, 450);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.control);
		panel.setBounds(172, 0, 340, 421);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(102, 11, 171, 326);
		panel.add(scrollPane);
				
		JButton btnAdd = new JButton("Ajouter");
		
		JLabel lblNewLabel = new JLabel("Decouverte reseau");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setToolTipText("");
		lblNewLabel.setBounds(10, 11, 163, 37);
		frame.getContentPane().add(lblNewLabel);
		
		JTextPane txtpnCetteDecouverteReseau = new JTextPane();
		txtpnCetteDecouverteReseau.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtpnCetteDecouverteReseau.setText("Cette decouverte reseau n'affiche pas par default les adresses ip deja ajouter.\r\n\r\n\r\nAstuce :\r\n     Vous pouvez utilisez une selection multiple en tenant la touche CTRL enfonc\u00E9e.");
		txtpnCetteDecouverteReseau.setForeground(new Color(255, 255, 255));
		txtpnCetteDecouverteReseau.setBackground(new Color(51, 102, 153));
		txtpnCetteDecouverteReseau.setEditable(false);
		txtpnCetteDecouverteReseau.setBounds(10, 82, 152, 227);
		frame.getContentPane().add(txtpnCetteDecouverteReseau);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/S_Logo.jpg"));
		
		ArrayList<Ip> a=new ArrayList<Ip>();
		DecouverteReseau d=new DecouverteReseau();
		a=d.getNetworkIPs();
		
		JList list = new JList(a.toArray());
		scrollPane.setViewportView(list);
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query1 = " insert into agent (IP,id_admin)"
				        + " values (?,?)";
				Connection con=(Connection) DbConnection.getConnection();
			  java.sql.PreparedStatement preparedStmt1;
			  int[] selectedIx = list.getSelectedIndices();
			  
			  
			try {
			    // Get all the selected items using the indices
			    for (int i = 0; i < selectedIx.length; i++) {
			    	ListeUtilisateurs.addToListe(new Utilisateurs(new IpAddress(list.getModel().getElementAt(selectedIx[i]).toString())), new IpAddress(list.getModel().getElementAt(selectedIx[i]).toString()));
				preparedStmt1 = con.prepareStatement(query1);
				preparedStmt1.setString (1,list.getModel().getElementAt(selectedIx[i]).toString());
			      preparedStmt1.setString(2, "1");
			      preparedStmt1.execute();
			    }
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	frame.setVisible(false);
        	frame.dispose();
			}
		});
		btnAdd.setForeground(new Color(51, 102, 153));
		btnAdd.setBounds(201, 367, 89, 23);
		panel.add(btnAdd);
		
		JButton btnAnnuler = new JButton("Annuler\r\n");
		btnAnnuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	frame.setVisible(false);
            	frame.dispose();
			}
		});
		btnAnnuler.setForeground(new Color(51, 102, 153));
		btnAnnuler.setBounds(102, 367, 89, 23);
		panel.add(btnAnnuler);
		frame.setVisible(true);

		
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */

	@Override
	public void run() {
		System.out.println("runed");
		// TODO Auto-generated method stub
		
}
public static void main(String args[]) {
	DecouverteReseauFrame d =new DecouverteReseauFrame();
}
}
