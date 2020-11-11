import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class WalkFrameMain {

	JFrame frmExtraSnmp;
	private Ip IP;
	private JTextField textField_Per_oid;

	public WalkFrameMain(Ip ip) {
		this.IP=ip;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmExtraSnmp = new JFrame();
		frmExtraSnmp.getContentPane().setBackground(new Color(51, 102, 153));
		frmExtraSnmp.setResizable(false);
		frmExtraSnmp.setTitle("Extra Snmp");
		frmExtraSnmp.setBounds(100, 100, 684, 405);
		//frmExtraSnmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmExtraSnmp.getContentPane().setLayout(null);
		frmExtraSnmp.setIconImage(Toolkit.getDefaultToolkit().getImage("images/S_Logo.jpg"));

		JButton btninfo = new JButton("Description general");
		btninfo.setForeground(new Color(51, 102, 153));
		btninfo.setBackground(SystemColor.control);
		btninfo.setBounds(24, 79, 156, 36);
		frmExtraSnmp.getContentPane().add(btninfo);
		btninfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WalkFrame window = new WalkFrame(btninfo.getText(),IP,SnmpWalkTable.TypeWalk.Info_general);
					window.frmExtraSnmp.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		JButton btnApp = new JButton("Application installe\r\n");
		btnApp.setForeground(new Color(51, 102, 153));
		btnApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WalkFrame window = new WalkFrame(btnApp.getText(),IP,SnmpWalkTable.TypeWalk.App_instaled);
					window.frmExtraSnmp.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnApp.setBackground(SystemColor.control);
		btnApp.setBounds(24, 128, 156, 36);
		frmExtraSnmp.getContentPane().add(btnApp);
		
		JButton btnNewButton_1 = new JButton("List des interfaces\r\n");
		btnNewButton_1.setBackground(SystemColor.control);
		btnNewButton_1.setForeground(new Color(51, 102, 153));
		btnNewButton_1.setBounds(24, 175, 156, 36);
		frmExtraSnmp.getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WalkFrame window = new WalkFrame(btnNewButton_1.getText(),IP,SnmpWalkTable.TypeWalk.Interfaces);
					window.frmExtraSnmp.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JButton btnNewButton_11 = new JButton("Information stockage\r\n");
		btnNewButton_11.setBackground(SystemColor.control);
		btnNewButton_11.setForeground(new Color(51, 102, 153));
		btnNewButton_11.setBounds(24, 225, 156, 36);
		frmExtraSnmp.getContentPane().add(btnNewButton_11);
		btnNewButton_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WalkFrame window = new WalkFrame(btnNewButton_11.getText(),IP,SnmpWalkTable.TypeWalk.Info_stockage);
					window.frmExtraSnmp.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
	
		JButton btnSip = new JButton("Table de routage\r\n");
		btnSip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WalkFrame window = new WalkFrame(btnSip.getText(),IP,SnmpWalkTable.TypeWalk.sip);
					window.frmExtraSnmp.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSip.setBackground(SystemColor.control);
		btnSip.setForeground(new Color(51, 102, 153));
		btnSip.setBounds(256, 148, 156, 36);
		frmExtraSnmp.getContentPane().add(btnSip);
		
		JButton btncpu = new JButton("Monitor CPU");
		btncpu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WalkFrame window = new WalkFrame(btncpu.getText(),IP,SnmpWalkTable.TypeWalk.Rcpu);
					window.frmExtraSnmp.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btncpu.setBackground(SystemColor.control);
		btncpu.setForeground(new Color(51, 102, 153));
		btncpu.setBounds(487, 148, 156, 36);
		frmExtraSnmp.getContentPane().add(btncpu);
		
		JButton btnbandwith = new JButton("Bandwidth in/out/speed");
		btnbandwith.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WalkFrame window = new WalkFrame(btnbandwith.getText(),IP,SnmpWalkTable.TypeWalk.Rbandwith);
					window.frmExtraSnmp.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnbandwith.setBackground(SystemColor.control);
		btnbandwith.setForeground(new Color(51, 102, 153));
		btnbandwith.setBounds(487, 195, 156, 36);
		frmExtraSnmp.getContentPane().add(btnbandwith);
		
		JLabel lblTitle = new JLabel("Information IP\r\n");
		lblTitle.setForeground(SystemColor.controlLtHighlight);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitle.setBounds(266, 11, 189, 27);
		frmExtraSnmp.getContentPane().add(lblTitle);
		
		JLabel lblSousTitle = new JLabel("OID Personalise");
		lblSousTitle.setBackground(SystemColor.text);
		lblSousTitle.setFont(new Font("Arial", lblSousTitle.getFont().getStyle(), 13));
		lblSousTitle.setForeground(SystemColor.textHighlightText);
		lblSousTitle.setBounds(232, 288, 197, 14);
		frmExtraSnmp.getContentPane().add(lblSousTitle);
        System.out.println(IP.toString());
		JLabel lblip = new JLabel(IP.toString());
		lblip.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblip.setForeground(Color.WHITE);
		lblip.setBounds(287, 39, 107, 14);
		frmExtraSnmp.getContentPane().add(lblip);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.WHITE);
		separator.setBackground(Color.GRAY);
		separator.setBounds(211, 74, 16, 173);
		frmExtraSnmp.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setForeground(Color.WHITE);
		separator_1.setBackground(Color.GRAY);
		separator_1.setBounds(447, 74, 16, 173);
		frmExtraSnmp.getContentPane().add(separator_1);
		
		JLabel lblRouteur = new JLabel("Routeur");
		lblRouteur.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRouteur.setForeground(Color.WHITE);
		lblRouteur.setBounds(536, 63, 70, 14);
		frmExtraSnmp.getContentPane().add(lblRouteur);
		
		JLabel lblSwitch = new JLabel("Switch");
		lblSwitch.setForeground(Color.WHITE);
		lblSwitch.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSwitch.setBounds(307, 63, 70, 14);
		frmExtraSnmp.getContentPane().add(lblSwitch);
		
		JLabel lblPc = new JLabel("PC");
		lblPc.setForeground(Color.WHITE);
		lblPc.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPc.setBounds(90, 63, 70, 14);
		frmExtraSnmp.getContentPane().add(lblPc);
		
		JButton btnInterface = new JButton("List des Ip connecte\r\n");
		btnInterface.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						try {
							WalkFrame window = new WalkFrame(btnInterface.getText(),IP,SnmpWalkTable.TypeWalk.RouterAddressListe);
							window.frmExtraSnmp.setVisible(true);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
			}
		});
		btnInterface.setForeground(new Color(51, 102, 153));
		btnInterface.setBackground(SystemColor.menu);
		btnInterface.setBounds(487, 99, 156, 36);
		frmExtraSnmp.getContentPane().add(btnInterface);
		
		JButton btnScpu = new JButton("Switch Info");
		btnScpu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WalkFrame window = new WalkFrame(btnScpu.getText(),IP,SnmpWalkTable.TypeWalk.sinfo);
					window.frmExtraSnmp.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnScpu.setForeground(new Color(51, 102, 153));
		btnScpu.setBackground(SystemColor.menu);
		btnScpu.setBounds(256, 99, 156, 36);
		frmExtraSnmp.getContentPane().add(btnScpu);
		
		JButton btnupTime = new JButton("Interfaces switch");
		btnupTime.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					WalkFrame window = new WalkFrame(btnupTime.getText(),IP,SnmpWalkTable.TypeWalk.sinterface);
					window.frmExtraSnmp.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnupTime.setForeground(new Color(51, 102, 153));
		btnupTime.setBackground(SystemColor.menu);
		btnupTime.setBounds(256, 195, 156, 36);
		frmExtraSnmp.getContentPane().add(btnupTime);
		
		textField_Per_oid = new JTextField();
		textField_Per_oid.setBounds(157, 313, 255, 20);
		frmExtraSnmp.getContentPane().add(textField_Per_oid);
		textField_Per_oid.setColumns(10);
		
		JButton btnWalk = new JButton("Walk");
		btnWalk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WalkFrame window = new WalkFrame(btnWalk.getText(),IP,textField_Per_oid.getText());
					window.frmExtraSnmp.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnWalk.setBackground(SystemColor.menu);
		btnWalk.setForeground(new Color(51, 102, 153));
		btnWalk.setBounds(449, 312, 89, 23);
		frmExtraSnmp.getContentPane().add(btnWalk);
		
	}
}
