

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.snmp4j.smi.IpAddress;

import com.mysql.jdbc.Connection;

import javax.swing.JProgressBar;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Application extends JFrame {

	
	static JLabel lblRefresh = new JLabel("");
	private JPanel contentPane;
	private JTable table;
	public static JTable StatueTable;
	private JTextField textFieldList;
	private int test=0;
	Connection conn=(Connection) DbConnection.getConnection();
	static Application frame;
	private JTextField textName;
	private JTextField textPrname;
	private JTextField textEmail;
	private JTextField textUser;
	private JTextField textPass;
	private JTextField textAncPass;
	private JTextField textTel;
	private JTextField Form[]= new JTextField[7];
	public static boolean etat=false;
	private JTextField textFieldJour;
	private JTextField textFieldMois;
	private JTextField textFieldAnnee;
	private JTextField textFieldip;
	public ListeUtilisateurs listeUti;
    boolean time=false;
    Timer timerP = null ;
    TimerTask taskP;

	/**
	 * Launch the application.
	 */
	public static JTable getStatueTable() {
		return StatueTable;
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Application();
					//ajoute par mouad
						frame.setName("application principale");
						// pour savoir si la fenetre est affiche dans l'ecran ou non
					//fin de modification
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}		     
			}});

	      }
	

	/**
	 * Create the frame.
	 */
	public Application() {
		ListeUtilisateurs.remplirListe();

		setResizable(false);
		setBackground(Color.LIGHT_GRAY);
		//modification par mouad :
		
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//fin de modification
		setBounds(100, 100, 1192, 756);
		setIconImage(Toolkit.getDefaultToolkit().getImage("images/S_Logo.jpg"));

		contentPane = new JPanel();
		contentPane.setBackground(new Color(51, 102, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(186, 11, 976, 694);
		tabbedPane.setBorder(null);
		tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 13));
		tabbedPane.setForeground(new Color(51, 102, 153));
		tabbedPane.setBackground(SystemColor.textHighlightText);
		contentPane.add(tabbedPane);
		
		JPanel panel_Trap = new JPanel();
		panel_Trap.setBorder(null);
		tabbedPane.addTab("Trap", null, panel_Trap, null);
		panel_Trap.setLayout(new BorderLayout(0, 0));
		JPanel panelTable = new JPanel();
		panel_Trap.add(panelTable,BorderLayout.CENTER);
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(null);
		panel_3.setBackground(new Color(51, 102, 153));
		panel_Trap.add(panel_3, BorderLayout.WEST);
		panel_3.setLayout(new GridLayout(5, 1, 0, 0));
		
		/*creation du tableau*/
		
		JTable table = new JTable(new ModeleStatiqueObjet());
		
		table.getTableHeader().setSize(30,30);
		TableColumnModel columnModel = table .getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(150);
		columnModel.getColumn(1).setPreferredWidth(521);
		columnModel.getColumn(2).setPreferredWidth(300);
		panelTable.add(table.getTableHeader(),BorderLayout.NORTH);
		panelTable.setLayout(null);
		JScrollPane pane = new JScrollPane(table);
		pane.setBounds(0, 0, 971, 664);
		panelTable.add(pane);
		DefaultListCellRenderer centerRenderer = new DefaultListCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		//rafraichire jtable chaque 10s
		 Timer timer = new Timer(); 
	        TimerTask task = new RafraichissementTable(table,etat);
	        timer.schedule(task, 0, 10000); 
	
		UIManager UI=new UIManager();
		 UI.put("OptionPane.background",new ColorUIResource(51, 102, 153));
		 UI.put("OptionPane.messageForeground", Color.WHITE);
		 UI.put("Panel.background",new ColorUIResource(51,102,153));
		 UI.put("Button.background",Color.WHITE);
		
			        //table statue
			        
			        JPanel panel_Statues = new JPanel();
			        panel_Statues.setBorder(null);
			        tabbedPane.addTab("Status", null, panel_Statues, null);
			        panel_Statues.setLayout(new BorderLayout(0, 0));
			        JPanel panelStatueTable = new JPanel();
			        panelStatueTable.setBackground(SystemColor.control);
			        panel_Statues.add(panelStatueTable,BorderLayout.CENTER);
			        JPanel panel_311 = new JPanel();
			        panel_311.setBorder(null);
			        panel_311.setBackground(new Color(51, 102, 153));
			        panel_Statues.add(panel_311, BorderLayout.WEST);
			        panel_311.setLayout(new GridLayout(5, 1, 0, 0));
			        
	 StatueTable = new JTable(new TableUtilisateursModel());
			         StatueTable.getColumnModel().getColumn(0).setPreferredWidth(20);
			         StatueTable.getColumnModel().getColumn(1).setPreferredWidth(40);
			         StatueTable.getColumnModel().getColumn(2).setPreferredWidth(35);
			         StatueTable.getColumnModel().getColumn(3).setPreferredWidth(20);
			         StatueTable.getColumnModel().getColumn(4).setPreferredWidth(20);
			         
//			StatueTable.getTableHeader().setSize(30,30);
//			TableColumnModel columnModel11 = StatueTable .getColumnModel();
//			columnModel11.getColumn(0).setPreferredWidth(150);
//			columnModel11.getColumn(1).setPreferredWidth(521);
//			columnModel11.getColumn(2).setPreferredWidth(300);
			panelStatueTable.add(StatueTable.getTableHeader(),BorderLayout.NORTH);
			panelStatueTable.setLayout(null);
			JScrollPane scrollStatueTable = new JScrollPane(StatueTable);
			scrollStatueTable.setBounds(0, 0, 971, 500);
			panelStatueTable.add(scrollStatueTable);
			
				        JPanel panel1 = new JPanel();
				        panel1.setBounds(250, 426, 10, 10);
				        panelStatueTable.add(panel1);
				        
				        JLabel lblAlert2 = new JLabel("\r\n");
				        lblAlert2.setFont(new Font("Tahoma", Font.ITALIC, 13));
				        lblAlert2.setForeground(Color.red);
				        lblAlert2.setBounds(168, 634, 326, 30);
				        panelStatueTable.add(lblAlert2);
				        
				        
	        JButton btnActualiser1 = new JButton("Actualiser");
	        //	        btnActualiser1.setFont(new Font("Tahoma", Font.PLAIN, 13));
	        	        btnActualiser1.setForeground(new Color(51, 102, 153));
	        	        btnActualiser1.setBackground(Color.WHITE);
	        	        btnActualiser1.setBounds(168, 580, 200, 60);
	        	        panelStatueTable.add(btnActualiser1);
	        	        btnActualiser1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		lblAlert2.setText("");
        		ListeUtilisateurs.resetListe();
			    StatueTable.setModel(new TableUtilisateursModel()); 
        	}
        });
	        	        
	        	        
	        	        	        
	        	        	        
	        	        	        
	        	        	        JButton btnAjouter = new JButton("Ajouter");
	        	        	        
//	        btnAjouter.setFont(new Font("Tahoma", Font.PLAIN, 13));
	        	        	        btnAjouter.setForeground(new Color(51, 102, 153));
	        	        	        btnAjouter.setBackground(Color.WHITE);
	        	        	        btnAjouter.setBounds(168, 511, 200, 60);
	        	        	        panelStatueTable.add(btnAjouter);
	        	        	        btnAjouter.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					lblAlert2.setText("");
					String address=JOptionPane.showInputDialog(Application.this, "Entrez l'addresse à ajouter ", "Ajout ", JOptionPane.INFORMATION_MESSAGE);
					if(address==null || !new Ip(address).verify() || ListeUtilisateurs.getListe().containsKey(new IpAddress(address)) ) {
						lblAlert2.setText("l'addresse saisie n'est pas valide ou déja existante"); 
					}
					else {
						ListeUtilisateurs.addToListe(new Utilisateurs(new IpAddress(address)), new IpAddress(address));
						DbConnection.Db_addAddress(address,new Ip(address).name() ,conn);
						lblAlert2.setText("");
					}
				}
			});
	        	        	        
			JButton btnDiscover1 = new JButton("Decouverte reseau\r\n");
			btnDiscover1.setForeground(new Color(51, 102, 153));
			btnDiscover1.setBackground(Color.WHITE);
			btnDiscover1.setBounds(434, 580, 200, 60);
			panelStatueTable.add(btnDiscover1);	
			btnDiscover1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {	
	        		lblAlert2.setText("");

					   new DecouverteReseauFrame();
				}
			});
			
			
	        JButton btnModifier= new JButton("Modifier");
	        btnModifier.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					lblAlert2.setText("");
					if(StatueTable.getSelectedRow()==-1) lblAlert2.setText("veuillez selectionner un utilisateur depuis le tableau !");
					else {
						Utilisateurs u=(Utilisateurs)StatueTable.getModel().getValueAt(StatueTable.getSelectedRow(), 1);
						lblAlert2.setText("");
						String name=JOptionPane.showInputDialog(Application.this, "Entrez le nouveau nom d'utilisateur \nAncien nom :"+u.getName()+" address:"+u.getIpAddress(),"modification du nom" ,JOptionPane.INFORMATION_MESSAGE);
						
						if(name!=null && !name.equalsIgnoreCase("")) {
							
							ListeUtilisateurs.getListe().get(u.getIpAddress()).setName(name);
								
							StatueTable.setModel(new TableUtilisateursModel());
							
							DbConnection.Db_updateName(u.getIpAddress().toString(), name, conn);
							
							new SnmpSet(u.getIpAddress().toString(), "1.3.6.1.2.1.1.4.0={s}"+name+"").doSet();;
						}
						else {
							lblAlert2.setText("Aucun nom n'a etait saisie");
						}
						
					}
					

				}
			});
	        //	        btnModifier.setFont(new Font("Tahoma", Font.PLAIN, 13));
	        	        btnModifier.setForeground(new Color(51, 102, 153));
	        	        btnModifier.setBackground(Color.WHITE);
	        	        btnModifier.setBounds(434, 514, 200, 55);
	        	        panelStatueTable.add(btnModifier);
	        	        
	        	        	        JButton btnSupprimer= new JButton("Supprimer");
	        	        	        
//	        btnSupprimer.setFont(new Font("Tahoma", Font.PLAIN, 13));
	        	        	        btnSupprimer.setForeground(new Color(51, 102, 153));
	        	        	        btnSupprimer.setBackground(Color.WHITE);
	        	        	        btnSupprimer.setBounds(727, 514, 200, 55);
	        	        	        panelStatueTable.add(btnSupprimer);
	        	        	        btnSupprimer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
	        	        	        		lblAlert2.setText("");

					if(StatueTable.getSelectedRow()==-1) {
						lblAlert2.setText("selectionner un utilisateur depuis le tableau !" );
					}
					else {
						lblAlert2.setText("");
						
						String address=(  (Utilisateurs)StatueTable.getModel().getValueAt(StatueTable.getSelectedRow(), 1)   ).getIpAddress().toString();
						
		                if(JOptionPane.showConfirmDialog(frame, "Voulez vous vraiment suprimmer l'adresse IP :" +address+" ?") == JOptionPane.OK_OPTION){
		                	
		                	ListeUtilisateurs.deletefromListe(new IpAddress(address));
						
		                	DbConnection.Db_deleteAddresse(address, conn);
		                	
					}
				}}
			});
	        	        	        
	        	        	        JButton btnInfo_Ip= new JButton("INFO IP");
	        	        	        
//	        btnInfo_Ip.setFont(new Font("Tahoma", Font.PLAIN, 13));
	        	        	        btnInfo_Ip.setForeground(new Color(51, 102, 153));
	        	        	        btnInfo_Ip.setBackground(Color.WHITE);
	        	        	        btnInfo_Ip.setBounds(727, 580, 200, 55);
	        	        	        panelStatueTable.add(btnInfo_Ip);
	        	        	        
	        	        	        btnInfo_Ip.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
	        	        	        		lblAlert2.setText("");

					if(StatueTable.getSelectedRow()==-1) {
						lblAlert2.setText("selectionner un utilisateur depuis le tableau !");
					}
					else {
						lblAlert2.setText("");
						try {
							WalkFrameMain window = new WalkFrameMain(new Ip(  (  (Utilisateurs)StatueTable.getModel().getValueAt(StatueTable.getSelectedRow(), 1)   ).getIpAddress().toString()   ));
							window.frmExtraSnmp.setVisible(true);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
	        	        	        
	        	        	            
	        	        	            
			lblRefresh.setFont(new Font("Tahoma", Font.ITALIC, 13));
			lblRefresh.setForeground(new Color(107, 142, 35));
			lblRefresh.setBounds(645, 634, 326, 30);
			panelStatueTable.add(lblRefresh);
		
	        JPanel panel_Historique = new JPanel();
	        panel_Historique.setBackground(SystemColor.controlHighlight);
	        panel_Historique.setToolTipText("");
	        tabbedPane.addTab("Historique\r\n", null, panel_Historique, null);
	        panel_Historique.setLayout(null);
	        
	        
	        textFieldJour = new JTextField();
	        textFieldJour.setBounds(557, 530, 53, 20);
	        panel_Historique.add(textFieldJour);
	        textFieldJour.setColumns(10);
	        
	        textFieldMois = new JTextField();
	        textFieldMois.setBounds(678, 530, 53, 20);
	        panel_Historique.add(textFieldMois);
	        textFieldMois.setColumns(10);
	        
	        textFieldAnnee = new JTextField();
	        textFieldAnnee.setBounds(801, 530, 86, 20);
	        panel_Historique.add(textFieldAnnee);
	        textFieldAnnee.setColumns(10);
	        
	        JButton btnSearch = new JButton("chercher\r\n");
	
	        btnSearch.setBounds(412, 607, 122, 23);
	        panel_Historique.add(btnSearch);
	        
	        JLabel lblip = new JLabel("Adresse ip\r\n");
	        lblip.setBounds(31, 533, 62, 14);
	        panel_Historique.add(lblip);
	        
	        JLabel lblJour = new JLabel("jour");
	        lblJour.setBounds(523, 533, 46, 14);
	        panel_Historique.add(lblJour);
	        
	        JLabel lblMois = new JLabel("mois");
	        lblMois.setBounds(642, 533, 46, 14);
	        panel_Historique.add(lblMois);
	        
	        JLabel lblAnnee = new JLabel("Annee\r\n");
	        lblAnnee.setBounds(757, 533, 46, 14);
	        panel_Historique.add(lblAnnee);
	        
	        JLabel lblComment = new JLabel("Gardez le champ vide pour tout les dates ou tout les adresses\r\n");
	        lblComment.setBounds(89, 561, 370, 14);
	        panel_Historique.add(lblComment);
	        
	        textFieldip = new JTextField();
	        textFieldip.setBounds(103, 530, 219, 20);
	        panel_Historique.add(textFieldip);
	        textFieldip.setColumns(10);
	        
	        JTable table_Historique = new JTable(new ModeleHistoriqueObjet("",""));
			table_Historique.getTableHeader().setSize(30,30);
	        table_Historique.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	        JScrollPane scrollPane_Historique = new JScrollPane(table_Historique);
	        scrollPane_Historique.setToolTipText("Historique\r\n");
	        scrollPane_Historique.setEnabled(true);
	        scrollPane_Historique.setBounds(0, 0, 971, 470);
			
			panel_Historique.add(table.getTableHeader(),BorderLayout.NORTH);
	        panel_Historique.add(scrollPane_Historique);
	        
	         
	        JPanel panel_conf = new JPanel();
	        panel_conf.setBackground(SystemColor.control);
	        tabbedPane.addTab("configuration", null, panel_conf, null);
	        panel_conf.setLayout(null);
	        
	        textName = new JTextField();
	        textName.setBounds(160, 129, 215, 20);
	        panel_conf.add(textName);
	        textName.setColumns(10);
	        Form[0]=textName;
	        
	        JLabel lblName = new JLabel("Nom");
	        lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        lblName.setBounds(34, 132, 46, 14);
	        panel_conf.add(lblName);
	        
	        textPrname = new JTextField();
	        textPrname.setBounds(160, 193, 215, 20);
	        panel_conf.add(textPrname);
	        textPrname.setColumns(10);
	        Form[1]=textPrname;
	        
	        		
	        		textEmail = new JTextField();
	        		textEmail.setBounds(160, 269, 215, 20);
	        		panel_conf.add(textEmail);
	        		textEmail.setColumns(10);
	        		Form[2]=textEmail;
	        		
	        		JLabel lblPrname = new JLabel("Prenom");
	        		lblPrname.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        		lblPrname.setBounds(34, 196, 46, 14);
	        		panel_conf.add(lblPrname);
	        		
	        		JLabel lblEmail = new JLabel("Email");
	        		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        		lblEmail.setBounds(34, 272, 46, 14);
	        		panel_conf.add(lblEmail);
	        		
	        		textUser = new JTextField();
	        		textUser.setBounds(160, 338, 215, 20);
	        		panel_conf.add(textUser);
	        		textUser.setColumns(10);
	        		Form[4]=textUser;
	        		
	        		textPass = new JPasswordField();
	        		textPass.setBounds(160, 460, 215, 20);
	        		panel_conf.add(textPass);
	        		textPass.setColumns(10);
	        		Form[5]=textPass;
	        		
	        		textAncPass = new JPasswordField();
	        		textAncPass.setBounds(160, 520, 215, 20);
	        		panel_conf.add(textAncPass);
	        		textAncPass.setColumns(10);
	        		Form[6]=textAncPass;
	        		
	        		JLabel lblAncPass = new JLabel("Ancien mot de passe\r\n");
	        		lblAncPass.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        		lblAncPass.setBounds(34, 522, 116, 14);
	        		panel_conf.add(lblAncPass);
	        		
	        		textTel = new JTextField();
	        		textTel.setBounds(160, 397, 215, 20);
	        		panel_conf.add(textTel);
	        		textTel.setColumns(10);
	        		Form[3]=textTel;
	        		
	        		for(int i=0;i<7;i++)
	        			Form[i].setEditable(false);
	        		
	        		JLabel lblUser = new JLabel("Nom d'utilisateur\r\n");
	        		lblUser.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        		lblUser.setBounds(34, 341, 116, 14);
	        		panel_conf.add(lblUser);
	        		
	        		JLabel lblPass = new JLabel("Mot de passe\r\n");
	        		lblPass.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        		lblPass.setBounds(34, 462, 116, 14);
	        		panel_conf.add(lblPass);
	        		
	        		JLabel lblSaved = new JLabel("");
	        		lblSaved.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        		lblSaved.setForeground(new Color(0, 100, 0));
	        		lblSaved.setBounds(243, 605, 200, 14);
	        		panel_conf.add(lblSaved);
	        		
	        		JButton btnSaveConf = new JButton("Enregistrer\r\n");
	        		btnSaveConf.setEnabled(false);
	        		btnSaveConf.addActionListener(new ActionListener() {
	        			public void actionPerformed(ActionEvent e) {
	        				Formulaire f=new Formulaire(textName.getText(), textPrname.getText(), textTel.getText(), textEmail.getText(), textUser.getText(),textPass.getText(),textAncPass.getText());
	        				if(f.updateForm()) {
	        					lblSaved.setForeground(new Color(0, 128, 0));
	        					lblSaved.setText("Transaction reussite");
	        					for(int i=0;i<7;i++)
	        						Form[i].setEditable(false);
	        					btnSaveConf.setEnabled(false);
	        					 
	        				}
	        				else {
	        					lblSaved.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        					lblSaved.setForeground(Color.red);
	        					lblSaved.setText("erreur !! veuillez ressayer");
	        				}

	        				Timer time = new Timer(); 
	        		        TimerTask tasks = new TimerTask() {
	        								@Override
	        					public void run() {
	        						// TODO Auto-generated method stub
	        						lblSaved.setText(""); 
	        					}
	        				};   
	        				time.schedule(tasks, 2000);
	        			}
	        		});
	        		
	        		btnSaveConf.setFont(new Font("Tahoma", Font.PLAIN, 13));
	        		btnSaveConf.setBackground(Color.WHITE);
	        		btnSaveConf.setForeground(new Color(51, 102, 153));
	        		btnSaveConf.setBounds(259, 571, 116, 23);
	        		panel_conf.add(btnSaveConf);
	        		
	        		JButton btnEdit = new JButton("edit\r\n");
	        		btnEdit.addActionListener(new ActionListener() {
	        			public void actionPerformed(ActionEvent e) {
	        				if(btnSaveConf.isEnabled()) {
	        				btnSaveConf.setEnabled(false);
	        				for(int i=0;i<7;i++)
	        					Form[i].setEditable(false);
	        				}
	        				else {
	        					btnSaveConf.setEnabled(true);
	        					for(int i=0;i<7;i++)
	        						Form[i].setEditable(true);
	        					Formulaire fo=new Formulaire("", "", "", "", "", "", "");
	        					String t[]=new String[7];
	        					t=fo.selectForm();
	        					for(int x=0,y=1;x<7 && y<7;x++ ,y++) {
	        						Form[x].setText(t[y]);
	        					}

	        				}
	        			}
	        		});
	        		btnEdit.setBackground(Color.WHITE);
	        		btnEdit.setForeground(new Color(51, 102, 153));
	        		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 13));
	        		btnEdit.setBounds(180, 571, 63, 23);
	        		panel_conf.add(btnEdit);
	        		
	        		JLabel lblTel = new JLabel("Tel\r\n");
	        		lblTel.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        		lblTel.setBounds(34, 400, 46, 14);
	        		panel_conf.add(lblTel);
	        		
	        		JSeparator separator_2 = new JSeparator();
	        		separator_2.setOrientation(SwingConstants.VERTICAL);
	        		separator_2.setBackground(Color.WHITE);
	        		separator_2.setForeground(new Color(51, 102, 153));
	        		separator_2.setBounds(479, 85, 27, 486);
	        		panel_conf.add(separator_2);
	        		
	        		JLabel lblConfTitle1 = new JLabel("Formulaire d'administrateur\r\n");
	        		lblConfTitle1.setFont(new Font("Tahoma", Font.BOLD, 18));
	        		lblConfTitle1.setForeground(new Color(51, 102, 153));
	        		lblConfTitle1.setBounds(130, 37, 255, 23);
	        		panel_conf.add(lblConfTitle1);
	        		
	        		JLabel lblConfTitle2 = new JLabel("Service Email\r\n\r\n");
	        		lblConfTitle2.setForeground(new Color(51, 102, 153));
	        		lblConfTitle2.setFont(new Font("Tahoma", Font.BOLD, 18));
	        		lblConfTitle2.setBounds(659, 37, 200, 23);
	        		panel_conf.add(lblConfTitle2);
	        		
	        		JTextPane txtpnLeServiceEmail = new JTextPane();
	        		txtpnLeServiceEmail.setEditable(false);
	        		txtpnLeServiceEmail.setBackground(SystemColor.control);
	        		txtpnLeServiceEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        		txtpnLeServiceEmail.setText("Le service Email vous permttra apres son activation de recevoir les trap dans votre adresses email tapez dans le formulaire a gauche.\r\n");
	        		txtpnLeServiceEmail.setBounds(608, 108, 263, 82);
	        		panel_conf.add(txtpnLeServiceEmail);
	        		
	        		JLabel lblConfSuceess = new JLabel("");
	        		lblConfSuceess.setForeground(new Color(0, 100, 0));
	        		lblConfSuceess.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        		lblConfSuceess.setBounds(656, 257, 187,14);
	        		panel_conf.add(lblConfSuceess);
	        		
	        		JButton btnActive = new JButton("Activer\r\n");
	        		btnActive.setFont(new Font("Tahoma", Font.PLAIN, 13));
	        		btnActive.setForeground(new Color(51, 102, 153));
	        		btnActive.setBackground(Color.WHITE);
	        		btnActive.setBounds(659, 211, 144, 35);
	        		
	        				btnActive.addActionListener(new ActionListener() {
	        					
	        					@Override
	        					public void actionPerformed(ActionEvent e) {
	        						// TODO Auto-generated method stub
	        						if(etat==true) {
	        						etat = false;
	        						btnActive.setForeground(new Color(51, 102, 153));
	        						btnActive.setBackground(Color.WHITE);
	        						btnActive.setText("Activer");
	        						lblConfSuceess.setText("service desactiver avec succes");	
	        						}
	        						else if(etat==false){
	        							etat=true;
	        							btnActive.setForeground(Color.WHITE);
	        							btnActive.setBackground(new Color(51, 102, 153));
	        							btnActive.setText("desactiver");
	        							lblConfSuceess.setText("service activer avec succes");
	        		
	        						}
	        					}
	        				});
	        				panel_conf.add(btnActive);       
	        				
		JSeparator separator_21 = new JSeparator();
		separator_21.setOrientation(SwingConstants.HORIZONTAL);
		separator_21.setBackground(Color.WHITE);
		separator_21.setForeground(new Color(51, 102, 153));
		separator_21.setBounds(510, 292, 480 , 27);
		panel_conf.add(separator_21);
		
		JLabel lblConfTitle21 = new JLabel("Service Notification\r\n\r\n");
		lblConfTitle21.setForeground(new Color(51, 102, 153));
		lblConfTitle21.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblConfTitle21.setBounds(640, 302, 200, 23);
		panel_conf.add(lblConfTitle21);
		
		JTextPane txtpnLeServiceNotification = new JTextPane();
		txtpnLeServiceNotification.setEditable(false);
		txtpnLeServiceNotification.setBackground(SystemColor.control);
		txtpnLeServiceNotification.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtpnLeServiceNotification.setText("Le service Notification vous permettra de recevoir des notifications quand une trap est generer.\r\n");
		txtpnLeServiceNotification.setBounds(608, 365, 263, 60);
		panel_conf.add(txtpnLeServiceNotification);
		
		JLabel lblConfSuceess1 = new JLabel("");
		lblConfSuceess1.setForeground(new Color(0, 100, 0));
		lblConfSuceess1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblConfSuceess1.setBounds(656, 491 , 250,14);
		panel_conf.add(lblConfSuceess1);
		
		JButton btnActive1 = new JButton("Activer\r\n");
		btnActive1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnActive1.setForeground(new Color(51, 102, 153));
		btnActive1.setBackground(Color.WHITE);
		btnActive1.setBounds(659, 445, 144, 35);
		
				btnActive1.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if(IconNotification.notification==true) {
						IconNotification.notification = false;
						btnActive1.setForeground(new Color(51, 102, 153));
						btnActive1.setBackground(Color.WHITE);
						btnActive1.setText("Activer");
						lblConfSuceess1.setText("Notifications desactiver avec succes");	
						}
						else if(IconNotification.notification==false){
							IconNotification.notification=true;
							btnActive1.setForeground(Color.WHITE);
							btnActive1.setBackground(new Color(51, 102, 153));
							btnActive1.setText("desactiver");
							lblConfSuceess1.setText("service activer avec succes");
		
						}
					}
				});
				panel_conf.add(btnActive1);       
	        		btnSearch.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
						    table_Historique.setModel(new ModeleHistoriqueObjet(textFieldAnnee.getText()+"-"+textFieldMois.getText()+"-"+textFieldJour.getText(),textFieldip.getText())); 
						}
					});

					        
	        JTextPane txtpnBienvenueDansVotre = new JTextPane();
	        txtpnBienvenueDansVotre.setEditable(false);
	        txtpnBienvenueDansVotre.setFont(new Font("Tahoma", Font.PLAIN, 14));
	        txtpnBienvenueDansVotre.setText("Bienvenue dans votre application ExtraSnmp de gestion d'un park informatique .Il est vivement recommender de consulter le manuel d'utilisation.\r\nVeuillez inserer vos informations personnels ainsi que changer le mot de passe par default pour la securite de votre application.");
	        txtpnBienvenueDansVotre.setForeground(Color.WHITE);
	        txtpnBienvenueDansVotre.setBackground(new Color(51, 102, 153));
	        txtpnBienvenueDansVotre.setBounds(21, 87, 155, 281);
	        contentPane.add(txtpnBienvenueDansVotre);
	        
	        JSeparator separator = new JSeparator();
	        separator.setBounds(21, 49, 155, 15);
	        contentPane.add(separator);
	        
	        JSeparator separator_1 = new JSeparator();
	        separator_1.setBounds(21, 391, 155, 15);
	        contentPane.add(separator_1);
	        
	        JLabel lblNewLabel_1 = new JLabel("EXTRA\r\n SNMP");
	        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));
	        lblNewLabel_1.setForeground(SystemColor.text);
	        lblNewLabel_1.setBounds(4, 6, 137, 33);
	        contentPane.add(lblNewLabel_1);
	        
	        JLabel lblNewLabel_2 = new JLabel("V 1.0");
	        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
	        lblNewLabel_2.setForeground(SystemColor.text);
	        lblNewLabel_2.setBounds(139, 17, 36, 14);
	        contentPane.add(lblNewLabel_2);
	        
	        JButton btnReport = new JButton("Generer rapport");
	        btnReport.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		try {
						UIManager.setLookAndFeel(
						    UIManager.getSystemLookAndFeelClassName());
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| UnsupportedLookAndFeelException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        		JFileChooser fileChooser = new JFileChooser();
	        		
	        		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	            	fileChooser.setDialogTitle("veuillez specifier un dossier");   
	            	ReportPdf f; 
	            	int userSelection = fileChooser.showSaveDialog(frame);            	 
	            	File selectedDir = fileChooser.getSelectedFile();
	            	if ( !selectedDir.isDirectory() ) {
	            		selectedDir = selectedDir.getParentFile();
	            	}
	            	f=new ReportPdf(selectedDir);
	        	}
	        });
	        btnReport.setBackground(SystemColor.textHighlightText);
	        btnReport.setForeground(new Color(51, 102, 153));
	        btnReport.setBounds(27, 437, 137, 23);
	        contentPane.add(btnReport);
			
			/*creation du tableau*/
			
			
	        JComboBox comboBoxAct = new JComboBox();
	        comboBoxAct.setFont(new Font("Tahoma", Font.PLAIN, 11));
	        comboBoxAct.setModel(new DefaultComboBoxModel(new String[] {"300s", "60s", "50s", "30s"}));
	        comboBoxAct.setSelectedIndex(0);
	        comboBoxAct.setBounds(42, 580, 69, 22);
	        
	        panelStatueTable.add(comboBoxAct);
	        
	        JSeparator separator_1_1 = new JSeparator();
	        separator_1_1.setOrientation(SwingConstants.VERTICAL);
	        separator_1_1.setBounds(148, 511, 10, 127);
	        panelStatueTable.add(separator_1_1);
	        
	        JLabel lblNewLabel = new JLabel("Duree d'actualisation");
	        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
	        lblNewLabel.setForeground(new Color(51, 102, 153));
	        lblNewLabel.setBounds(10, 557, 138, 14);
	        panelStatueTable.add(lblNewLabel);
	        
	        comboBoxAct.addActionListener(new ActionListener() {	
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					timerP.cancel();		
					timerP = new Timer();
					taskP.cancel();
			        taskP = new RafraichissementTable(StatueTable);
			        timerP.schedule(taskP,0, convertTime(comboBoxAct.getSelectedItem().toString()));
					DefaultListCellRenderer centerRenderer11 = new DefaultListCellRenderer();
					centerRenderer11.setHorizontalAlignment(JLabel.CENTER);
			        System.out.println("1=  "+convertTime(comboBoxAct.getSelectedItem().toString()));

				}
			});
	        timerP = new Timer();
	        taskP = new RafraichissementTable(StatueTable);
	        timerP.schedule(taskP,0, convertTime(comboBoxAct.getSelectedItem().toString()));
	        System.out.println("2=  "+convertTime(comboBoxAct.getSelectedItem().toString()));
			DefaultListCellRenderer centerRenderer11 = new DefaultListCellRenderer();
			centerRenderer11.setHorizontalAlignment(JLabel.CENTER);
	}	


	public static boolean isEtat() {
		// TODO Auto-generated method stub
		return etat;
	}
	private Long convertTime(String time) {
		   String numberOnly= time.replaceAll("[^0-9]", "");
		return Long.parseLong(numberOnly)*1000;
	}
}
