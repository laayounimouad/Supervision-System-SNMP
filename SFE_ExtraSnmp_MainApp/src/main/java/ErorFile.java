import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ErorFile {
	public static void main(String args[]){
	  		
		ErorFile er=new ErorFile("r", "testing");
	}
	 String nom, txt;
	public ErorFile(String nom,String txt) {
        this.nom=nom;
        this.txt=txt;
        creer_fichier();
	}
public void creer_fichier() {
	 File f = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"\\"+this.nom+".txt");
	    if (f.exists())
	      try {
	        f.createNewFile();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
		try {
			FileWriter fw = new FileWriter(f);
	        fw.write(this.txt);
	        fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
