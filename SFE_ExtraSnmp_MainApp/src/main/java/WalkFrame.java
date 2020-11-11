
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class WalkFrame {

	JFrame frmExtraSnmp;
	private JTable table;
	private String Title;
	private Ip IP;
    private String oid; 
    private SnmpWalkTable.TypeWalk typewalk;
	 //Create the application.
	
	public WalkFrame(String Title,Ip IP,String OID) {
		this.Title=Title;
		this.IP=IP;
		this.oid=OID;
		typewalk=SnmpWalkTable.TypeWalk.soid;
		initialize();
	}
	public WalkFrame(String Title,Ip IP,SnmpWalkTable.TypeWalk typewalk) {
		this.Title=Title;
		this.IP=IP;
		this.typewalk=typewalk;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmExtraSnmp = new JFrame();
		frmExtraSnmp.getContentPane().setBackground(SystemColor.control);
		frmExtraSnmp.setTitle("Extra Snmp");
		frmExtraSnmp.setResizable(false);
		frmExtraSnmp.setBounds(100, 100, 1000, 631);
	//	frmExtraSnmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmExtraSnmp.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Information IP");
		lblNewLabel.setForeground(new Color(51, 102, 153));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBackground(SystemColor.control);
		lblNewLabel.setBounds(307, 11, 166, 35);
		frmExtraSnmp.getContentPane().add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 68, 954, 513);
		frmExtraSnmp.getContentPane().add(scrollPane);
		
		table = new JTable();
        SnmpWalkTable s=new SnmpWalkTable (this.IP.toString());
        s.setoid(oid);
		//String [] col= {"id","nom"}; //colonnes		
		table=s.jtableWalk(typewalk);// la fonction jtableWalk return un Jtable rempli
		
		JScrollPane sp= new JScrollPane(table);
		scrollPane.setViewportView(table);
		
		JLabel lblip = new JLabel(this.IP.toString());
		lblip.setBackground(Color.BLACK);
		lblip.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblip.setBounds(337, 43, 141, 14);
		frmExtraSnmp.getContentPane().add(lblip);
		
		JLabel lblTitle = new JLabel(this.Title);
		lblTitle.setBackground(new Color(51, 102, 153));
		lblTitle.setForeground(new Color(51, 102, 153));
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTitle.setBounds(446, 24, 166, 14);
		frmExtraSnmp.getContentPane().add(lblTitle);
	}
}
