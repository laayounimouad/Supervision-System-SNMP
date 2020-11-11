import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args){	
					
		
		
	    Progress pr= new Progress();
		Authetification.main(null);
		
		//Application app=new Application();
		//app.main(args);
		//ajouter par mouad
		new IconNotification().setGUI();
		//fin d'ajout 
		MainSnmpV2Receiver main=new MainSnmpV2Receiver();
	}
}
