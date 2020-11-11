package Graphic;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.snmp4j.smi.IpAddress;

import main.Main;


public class GUI {
	
	

	MenuItem internet_stat = new MenuItem("internet Statut : En cours ....");
	MenuItem trap_stat = new MenuItem("trap sent au Manager : Non");
	MenuItem trap_stat2 = new MenuItem();
	MenuItem redemarrer = new MenuItem("redemarrer application");
	
	MenuItem item1 = new MenuItem("recherche d'adresse du Manager En cours...");
	MenuItem ajout_manuelle = new MenuItem("ajouter addresse manuellement");
	String imagePath=".\\images\\S_Logo.jpg";
    public	PopupMenu popup =null;
    public 	TrayIcon trayIcon=null;
    public SystemTray tray=null;
    Menu menu= new Menu("info");;
    
	public GUI() {
//		
//		if(popup==null) popup = new PopupMenu();
//	    popup.removeAll();
//		Image image = Toolkit.getDefaultToolkit().getImage(this.imagePath);
//	    trayIcon = new TrayIcon(image,"EXTRA SNMP", popup);
//	    trayIcon.setToolTip("EXTRA SNMP Agent");
////	    trayIcon.setImageAutoSize(true);
//
//	    
//	    
//	    menu.add(internet_stat);
//	    menu.add(trap_stat);
//		
//
//	   
//	    popup.add(item1);
//	    popup.add(menu);
//trayIcon.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("clicked");
//			}
//		});
//if(tray==null)
//	    try {
//	    	tray = SystemTray.getSystemTray();
//	        tray.add(trayIcon);
//	    } catch (AWTException e) {
//	        System.out.println("TrayIcon could not be added.");
//	    }
	}

	public void ajouterButtonRedemarrer(Main main) {
		redemarrer.setEnabled(true);
		 redemarrer.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        	main.redemarrer=true;
		        	redemarrer.setEnabled(false);
		        }});
		    popup.add(redemarrer);
	}
	
	
	public void ModifiyinfoGUI(String label,int index) {
		menu.getItem(index).setLabel(label);
	}
	public void ModifyMenuGUI(String label,int index) {
		popup.getItem(index).setLabel(label);
	}
	public void ajouterItemMenu(String label) {
		menu.add(new MenuItem(label));
	}
	public void setButtonAjouterManuellement(Main main){
	 MenuItem ajout_manuelle = new MenuItem("ajouter addresse manuellement");

	popup.add(ajout_manuelle);
		ajout_manuelle.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
				JFrame f=new JFrame();
				ImageIcon icon = new ImageIcon("images/S_Logo2.jpg");
				
				f.setIconImage(Toolkit.getDefaultToolkit().createImage("images/img.ico"));
	        	JTextField text= new JTextField(25);
			 	JLabel l=new JLabel("<html> <font color='white'>Entrez l'addresse du Manager:</font></html>");
		        JButton b= new JButton("valider");
		        JPanel p= new JPanel();
		        b.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							if(Inet4Address.getByName(text.getText()).getHostAddress().equals(text.getText())) {
								main.address=new IpAddress(text.getText());
								f.setVisible(false);
							}
							else {
								JOptionPane.showMessageDialog(null, "error : verifier que vous avez entrez une valide addresse ");

							}
							} catch (UnknownHostException e1) {
								JOptionPane.showMessageDialog(null, "error : verifier que vous avez entrez une valide addresse ");
		}
					}
				});
		        f.setTitle("Ajout Manuelle");
		        p.setLayout(new FlowLayout(FlowLayout.CENTER));
		        p.add(l);
		        p.add(text);
		        p.add(b);
		        p.add(new JLabel());
		        p.add(new JLabel(icon));
		        p.setBackground(Color.DARK_GRAY);
		        f.add(p);
		        f.setLocationRelativeTo(null);
		        f.setVisible(true);
		        f.setResizable(false);
		        f.setSize(400, 200);
	        
			
			
		}
	});
 }

	
	public void setGUI() {
		if(popup==null) popup = new PopupMenu();
	    popup.removeAll();
	  if(trayIcon==null)  {
	    Image image = Toolkit.getDefaultToolkit().getImage(this.imagePath);
	    trayIcon = new TrayIcon(image,"EXTRA SNMP", popup);
	    trayIcon.setToolTip("EXTRA SNMP Agent");
	    trayIcon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked");
			}
		});
	    }  
	    
	    menu.add(internet_stat);
	    menu.add(trap_stat);
		

	   
	    popup.add(item1);
	    popup.add(menu);
	   
if(tray==null)
	    try {
	    	tray = SystemTray.getSystemTray();
	        tray.add(trayIcon);
	    } catch (AWTException e) {
	        System.out.println("TrayIcon could not be added.");
	    }
	    
		
	}
}
