
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;


public class DbConnection {
	private static Connection cnx=null;
	private static String DB_connection="jdbc:mysql://localhost:3306/";
	private static String DB_name="extrasnmp";
	private static String DB_user="root";
	private static String DB_password="";
	static boolean ignorer=false;
	static File f;
	static FileWriter fw ;
	public static void readInFile() {
		f=new File("DB_info.txt");
		
	    if (!f.exists())
	      try {
	        f.createNewFile();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }  

try {
		
			BufferedReader bf=new BufferedReader(new FileReader(f));
			String line;
			while((line=bf.readLine())!=null) {
	if(line.contains("name")) {
		if(line.split(";")[0].split("=").length==1) DB_name="";
		DB_name=line.split(";")[0].split("=")[1]+"";
	}
	if(line.contains("user")) {
		if(line.split(";")[0].split("=").length==1) DB_user="";
		DB_user=line.split(";")[0].split("=")[1]+"";
	}
	if(line.contains("password")) {
		if(line.split(";")[0].split("=").length==1) DB_password="";
		else DB_password=line.split(";")[0].split("=")[1]+"";
	}
	if(line.contains("connection")) {
		if(line.split(";")[0].split("=").length==1) DB_connection="";
		DB_connection=line.split(";")[0].split("=")[1]+"";
	}
			}
			bf.close();
		
		} catch (IOException e) {
			System.out.println("file error");
		}
	}

	public void setDB_name(String DB_name ) {
		this.DB_name=DB_name;
	}
	public void setDB_user(String DB_user ) {
		this.DB_user=DB_user;
	}
	public void setDB_password(String DB_password) {
		this.DB_password=DB_password;
	}
	public static Connection getConnection()
	{
		readInFile();
		if (cnx == null)
		{
		try {
			//1-Charger le Driver
			Class.forName("com.mysql.jdbc.Driver");
			//2-Creation Connextion
			cnx=DriverManager.getConnection(DB_connection+DB_name,DB_user,DB_password);
			//cnx=DriverManager.getConnection("jdbc:mysql://www.db4free.net:3306/extrasnmp2020","dandensoufiane","BRJirZB#a.5_Nm_");
		} catch (Exception e) {
			// TODO Auto-generated catch block
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String[] op= {"quitter l'application","continue","ignorer toujours"};
            int i=4;
            if(!ignorer) {
            	i=JOptionPane.showOptionDialog(null, "erreur dans les informations du Base Donnees \n modifier les informations dans le fichier DB_info.txt sur le dossier du logiciel", "erreur", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, op, null);
            }
            if(i==2) ignorer=true;
            ErorFile er =new ErorFile("error_database",sw.toString());
		    if(i==0)System.exit(0);
		}
		}
		return cnx;
		
	}
	public static void Db_addAddress(String address,String name,Connection conn) {
		String query1 = " insert into agent (IP,id_admin,nom)"
		        + " values (?,?,?)";
		
	  java.sql.PreparedStatement preparedStmt1;
	try {
		preparedStmt1 = conn.prepareStatement(query1);
		preparedStmt1.setString (1,address);
	      preparedStmt1.setString(2, "1");
	      preparedStmt1.setString(3, name);
	      preparedStmt1.execute();
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	}
	public static void Db_deleteAddresse(String address,Connection conn) {
		   String sql = "delete from agent where IP= ?";
		    java.sql.PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,address);
		        stmt.executeUpdate();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	public static void Db_updateName(String address,String name,Connection conn) {
		 String query = "update agent set nom = ? where ID = ?";
	      java.sql.PreparedStatement preparedStmt;
		try {
			final String sql ="select ID from agent where IP= ? ";
		  	java.sql.PreparedStatement ps = conn.prepareStatement(sql);
		    ps.setString(1,address);
			ResultSet rs2=ps.executeQuery(); 
		
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, name);
			while(rs2.next())
		    preparedStmt.setInt(2, rs2.getInt(1));
		     // execute the java preparedstatement
		    preparedStmt.executeUpdate();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	
}