import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class Formulaire {
 
	String nom,prenom,tel,email,user,pass,OldPass;

	public Formulaire(String nom, String prenom, String tel, String email, String user, String pass, String oldPass) {
		this.nom = nom;
		this.prenom = prenom;
		this.tel = tel;
		this.email = email;
		this.user = user;
		this.pass = pass;
		this.OldPass = oldPass;
	}
	
	public Boolean updateForm() {
		Connection conn=(Connection) DbConnection.getConnection();
		final String sql ="select password from admin where ID= 1 ";
		String Old = null;
		
		 String query = "update admin set nom = ?,prenom= ?,email= ?,num_tel=?,Login= ?,password=? where ID = 1";
	      java.sql.PreparedStatement preparedStmt = null;
		try {
		  	java.sql.Statement ps = conn.createStatement();
			ResultSet rs=ps.executeQuery(sql); 
			while (rs.next())
		         Old = rs.getString(1);
			if(!(Old.equals(this.OldPass))) return false;
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, this.nom);
			preparedStmt.setString(2, this.prenom);
			preparedStmt.setString(4, this.tel);
			preparedStmt.setString(3, this.email);
			preparedStmt.setString(5, this.user);
			preparedStmt.setString(6, this.pass);

		     // execute the java preparedstatement
		    preparedStmt.executeUpdate();
		    System.out.println("updated");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		return true;
		}
	
	public String[] selectForm() {
		String tab[]=new String[7];
		Connection conn=(Connection) DbConnection.getConnection();
		final String sql ="select nom ,prenom,email,num_tel,Login,password from admin where ID = 1 ";	
		try {
		  	java.sql.Statement ps = conn.createStatement();
			ResultSet rs=ps.executeQuery(sql); 
			while (rs.next())
				for(int i=1;i<7;i++) {
		       tab[i]=rs.getString(i);
				}
			
		     // execute the java prepared statement

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return tab;
		}
	}

