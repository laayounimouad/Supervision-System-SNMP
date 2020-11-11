import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Authetification {

	private JFrame frmExtrasnmp;
	private JTextField txt_login;
	private JPasswordField txt_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Authetification window = new Authetification();
					window.frmExtrasnmp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					Authetification window = new Authetification();
					window.frmExtrasnmp.dispose();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Authetification() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmExtrasnmp = new JFrame();
		frmExtrasnmp.setTitle("ExtraSNMP");
		frmExtrasnmp.setIconImage(Toolkit.getDefaultToolkit().getImage("images/S_Logo.jpg"));
		frmExtrasnmp.getContentPane().setBackground(new Color(51, 102, 153));
		frmExtrasnmp.getContentPane().setLayout(null);
	    frmExtrasnmp.setPreferredSize(new Dimension(500, 225));
	    frmExtrasnmp.pack();
	    frmExtrasnmp.setLocationRelativeTo(null);
	    frmExtrasnmp.setVisible(true);
	    //modifie par mouad :
	    
	    frmExtrasnmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
		//fin de modification
		
		txt_login = new JTextField();
		txt_login.setBackground(new Color(255, 255, 255));
		txt_login.setBounds(266, 63, 192, 20);
		frmExtrasnmp.getContentPane().add(txt_login);
		txt_login.setColumns(10);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setForeground(new Color(255, 255, 255));
		lblLogin.setFont(lblLogin.getFont().deriveFont(lblLogin.getFont().getStyle() | Font.BOLD));
		lblLogin.setBounds(188, 65, 76, 17);
		frmExtrasnmp.getContentPane().add(lblLogin);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(new Color(255, 255, 255));
		lblPassword.setFont(lblPassword.getFont().deriveFont(lblPassword.getFont().getStyle() | Font.BOLD));
		lblPassword.setBounds(188, 97, 76, 14);
		frmExtrasnmp.getContentPane().add(lblPassword);
		
		txt_password = new JPasswordField();
		txt_password.setBounds(266, 94, 192, 20);
		frmExtrasnmp.getContentPane().add(txt_password);
		
		JLabel lblNewLabel = new JLabel("Extra SNMP ");
		lblNewLabel.setBackground(new Color(51, 102, 153));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblNewLabel.setBounds(251, 11, 164, 28);
		frmExtrasnmp.getContentPane().add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(153, 153, 153));
		separator.setBackground(new Color(51, 153, 204));
		separator.setBounds(237, 125, 237, 2);
		frmExtrasnmp.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(new Color(153, 153, 153));
		separator_1.setBackground(new Color(51, 153, 204));
		separator_1.setBounds(237, 50, 237, 2);
		frmExtrasnmp.getContentPane().add(separator_1);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setIcon(new ImageIcon("images/ExtraSNMP1.png"));
		lblNewLabel_1.setBounds(0, 0, 161, 186);
		frmExtrasnmp.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("V 1.0 ");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setBounds(412, 24, 46, 14);
		frmExtrasnmp.getContentPane().add(lblNewLabel_2);
		
		JLabel lblErreur = new JLabel("");
		lblErreur.setFont(lblErreur.getFont().deriveFont(lblErreur.getFont().getStyle() | Font.BOLD));
		lblErreur.setForeground(new Color(255, 0, 0));
		lblErreur.setBounds(277, 132, 192, 14);
		frmExtrasnmp.getContentPane().add(lblErreur);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				admin a=new admin(txt_login.getText(),String.valueOf(txt_password.getPassword()));
				Connection con=(Connection) DbConnection.getConnection();
				Statement stmt = null;
				Boolean ok=false;
				try {
					stmt = (Statement) con.createStatement();
					ResultSet rs=stmt.executeQuery("SELECT password,Login FROM admin");
					while(rs.next()) {
					     if((rs.getString(2).equals(a.getUsername()) && (rs.getString(1).equals(a.getPass())))) {
							ok=true;
                           }
						else {
							ok=false;
							lblErreur.setText("Incorrect,veuillez réessayer");
							txt_login.setText("");
							txt_password.setText("");
					}
						}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				
				}
				System.out.println("2");
				//appeler la fentre principlale si ok 
				if(a.getUsername().equalsIgnoreCase("admin")&&a.getPass().equalsIgnoreCase("admin")) {
					ok=true;
				}
				if(ok==true) {frmExtrasnmp.dispose(); Application.main(null); }
				}
		});
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setBounds(376, 146, 89, 23);
		frmExtrasnmp.getContentPane().add(btnNewButton);
		
		UIManager UI=new UIManager();
		 UI.put("OptionPane.background",new ColorUIResource(51, 102, 153));
		 UI.put("OptionPane.messageForeground", Color.WHITE);
		 UI.put("Panel.background",new ColorUIResource(51,102,153));
		 UI.put("Button.background",Color.WHITE);
		
		JButton btnNewButton_1 = new JButton("Exit\r\n");
		btnNewButton_1.setBounds(277, 146, 89, 23);
		frmExtrasnmp.getContentPane().add(btnNewButton_1);
		btnNewButton_1.setBackground(new Color(255, 255, 255));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if(JOptionPane.showConfirmDialog(frmExtrasnmp, "Are you sure ?") == JOptionPane.OK_OPTION){
                	frmExtrasnmp.setVisible(false);
                	frmExtrasnmp.dispose();
                	System.exit(1);
                }
			}
		});

	}
}
