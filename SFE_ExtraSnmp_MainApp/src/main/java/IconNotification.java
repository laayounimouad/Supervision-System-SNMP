import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 
 * @author Mouad Laayouni
 *
 */
public class IconNotification {

	
	MenuItem item1 = new MenuItem("Application en cours...");
	MenuItem item2 = new MenuItem("Exit");
	MenuItem ouvrir = new MenuItem("Ouvrir");

	public static boolean notification=false;
	MenuItem ajout_manuelle = new MenuItem("ajouter addresse manuellement");
	String imagePath=".\\images\\S_Logo.jpg";
    public	PopupMenu popup =null;
    public static 	TrayIcon trayIcon=null;
    public SystemTray tray=null;
    Menu menu= new Menu("info");
    public static TrayIcon getTrayIcon() {
    	return trayIcon;
    }
	public IconNotification() {
		
	}
	public void setGUI() {
		if(popup==null) popup = new PopupMenu();
	    popup.removeAll();
	  if(trayIcon==null)  {
	    Image image = Toolkit.getDefaultToolkit().getImage(this.imagePath);
	    trayIcon = new TrayIcon(image,"EXTRA SNMP", popup);
	    trayIcon.setToolTip("EXTRA SNMP Manager");
	   trayIcon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Frame[] f=Frame.getFrames();
				System.out.println(f.length);
				for(Frame fr: f) {
					if(fr.getName().equals("application principale") && !fr.isShowing()) fr.setVisible(true);
				}
				
				}
		});
	    }  
	  ouvrir.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Frame[] f=Frame.getFrames();
			System.out.println(f.length);
			for(Frame fr: f) {
				if(fr.getName().equals("application principale") && !fr.isShowing()) fr.setVisible(true);
			}
			
			}
	});
	   
	   item2.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	});
	    popup.add(item1);
	    popup.add(ouvrir);
	    popup.add(item2);
	    if(tray==null)
	    try {
	    	tray = SystemTray.getSystemTray();
	        tray.add(trayIcon);
	    } catch (AWTException e) {
	        System.out.println("TrayIcon could not be added.");
	    }	
	}	
}
