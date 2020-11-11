package main;


import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.State;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.snmp4j.smi.IpAddress;

import Graphic.GUI;
import internet.connected.Connectivity;
import internet.speed.Speed;
import recherche.inform.ChercherManager;
import trap.SnmpTrap;
import trap.SnmpWalk;


public class Main {
	
	public GUI gui;
	Connectivity con=new Connectivity();
	Speed s=new Speed();
	
	SnmpTrap internet_up;
	SnmpTrap internet_down;
	SnmpTrap download_speed;
	SnmpTrap upload_speed;
	SnmpTrap ping_speed;
	
	static File f;
	static FileWriter fw ;
	static boolean statue=false;
	
	public boolean internet_up_trap_sent	=	false;
	public boolean internet_down_trap_sent	=	false;
	public boolean speed_test_trap_sent		=	false; 
	public boolean speed_test_tres_lent		=	false;
	public IpAddress address = null;
	boolean snmp_test;
	final DecimalFormat dec= new DecimalFormat("#.##");

	public boolean redemarrer=true;
	public void initialise() {
		internet_up_trap_sent	=	false;
		 internet_down_trap_sent	=	false;
		 speed_test_trap_sent		=	false; 
		 speed_test_tres_lent		=	false;
			 address = null;
		 statue=false;
		 redemarrer=true;
		 s=new Speed();
	}
	public static void writeInFileANDRegister(String string) {
		boolean trouve=false;
		try {
		
			BufferedReader bf=new BufferedReader(new FileReader(f));
			String line;
			while((line=bf.readLine())!=null) {
	if(line.contains(string)) {
		trouve=true;
	}
			}
			bf.close();
		if(!trouve) {
			fw = new FileWriter(f,true);
			fw.write(string+"\n");
			fw.close();
			ProcessBuilder ps = new ProcessBuilder("cmd","/c",".\\mini_app\\Installer_SNMP.exe","trapdest","Local",string+"");
			Process p=ps.start();
			p.waitFor();
		}
		} catch (IOException | InterruptedException e) {
			System.out.println("file error");
		}
	}
	
	public static void creer_fichier() {
		ProcessBuilder pb=new ProcessBuilder("cmd","/c","set","homedrive");
		pb.redirectErrorStream(true);
			Process ps = null;
			try {
				ps = pb.start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			String line = null;
			try {
				line=in.readLine();
				in.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if(line==null) {
			System.out.println("null");
			f=new File("C:\\SnmpAgentAddresse\\addresses.txt");
			new File("C:\\SnmpAgentAddresse").mkdir();
			}
		else {
			f = new File(line.split("=")[1].substring(0, 2)+"\\SnmpAgentAddresse\\addresses.txt");
			System.out.println("true");
			new File(line.split("=")[1].substring(0, 2)+"\\SnmpAgentAddresse").mkdir();
		}
	    if (!f.exists())
	      try {
	    	  if(line==null) {
	  			new File("C:\\SnmpAgentAddresse").mkdir();
	  			}
	  		else {
	  			new File(line.split("=")[1].substring(0, 2)+"\\SnmpAgentAddresse").mkdir();
	  		}
	        f.createNewFile();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }  
	    
    
	     
	}
	
	public void initInternetTrap() {
		internet_up=new SnmpTrap(address.toString(),
				"1.3.6.1.4.1.2789.2005.1={s}internet_up",1);
		internet_down=new SnmpTrap(address.toString(),
				"1.3.6.1.4.1.2789.2005.1={s}internet_down",1);
	}
	public void initSpeedTrap(String download,String upload,String ping) {
		download_speed=new SnmpTrap(address.toString(),
				"1.3.6.1.4.1.2789.2005.1={s}"+download+"",1);
		upload_speed=new SnmpTrap(address.toString(),
				"1.3.6.1.4.1.2789.2005.1={s}"+upload+"",1);
		ping_speed=new SnmpTrap(address.toString(),
				"1.3.6.1.4.1.2789.2005.1={s}"+ping+"",1);
	}
	public boolean methodePrincipale() {
		int i=0;
		while(true) {
			i++;
				try {
				
				Thread.sleep(4000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				
			}
				if(i>30) {
				if(!new SnmpTrap(address.toString(),"1.3.6.1.4.1.9.10={s}TEST",2).doTrap()) {
					if(s.isAlive()) {
						
						s.setStop();
						try {
							s.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					return true;
				}
				i=0;
				}
			if(con.getStatue()) { 

				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				if(internet_up_trap_sent==false) {
					gui.ModifiyinfoGUI("internet Statut : connecter", 0);
					internet_up_trap_sent=true;
					internet_down_trap_sent=false;
					internet_up.doTrap();
					gui.ModifiyinfoGUI("trap envoyer : succes", 1);

				}
				
			if(!speed_test_trap_sent) {	
				
					if(s.getState()==State.NEW || s.getState()==State.TERMINATED) {
						s=null;
						s=new Speed();
						s.setName("Internet Vitesse Thread");

						s.resetstop();
						s.start();

					}
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if(!speed_test_tres_lent) {	
					if(s.timeOut && !s.ServerConnection) {
						if(!speed_test_tres_lent)
							{
								System.out.println("pleeeeeaaaasssee");
								new SnmpTrap(address.toString(),"1.3.6.1.4.1.2789.2005.1.1={s}internet tres lente",1).doTrap();
								speed_test_tres_lent=true;
							}
							
						}
					}
				if(s.GetSpeedStatue()) {
					
					
						initSpeedTrap("download : "+dec.format(s.GetDownloadResult()) + " Mbps","upload : "+dec.format(s.GetUploadResult()) + " Mbps","ping : " +dec.format(s.GetPingResult()) + " ms");
						download_speed.doTrap();
						upload_speed.doTrap();
						ping_speed.doTrap();
						speed_test_trap_sent=true;
						speed_test_tres_lent=false;
					}	
				}				
			}
			else {
				if(s.isAlive()) {
					
				s.setStop();
				try {
					s.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				
				System.out.println("interrupted:"+s.isAlive());
				
		
				if(internet_down_trap_sent==false) {
					gui.ModifiyinfoGUI("internet Statut : deconnecter", 0);
					internet_down_trap_sent=true;
					internet_up_trap_sent=false;
					speed_test_trap_sent=false;
					speed_test_tres_lent=false;
					internet_down.doTrap();
					gui.ModifiyinfoGUI("trap envoyer : succes", 1);
				}
			
			}
			
			
		}
	}

	public boolean test_snmp() {
		SnmpWalk indexWalk = new SnmpWalk("127.0.0.1","1.3.6.1.2.1.1.1");
		return 		indexWalk.doWalk();

	}
	
	public void recherche() {
		
		boolean deja_commence=false;
		ChercherManager c=new ChercherManager();
		c.setName("Chercher Manager");
		c.resetstop();
		gui.ModifyMenuGUI("recherche d'adresse du Manager En cours...", 0);

		
			
			do {
			if(!deja_commence) c.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if(c.getAddress()!=null) { address=c.getAddress();}
			if(address==null && c.isFinished()) {
				gui.trayIcon.displayMessage("Manager Non Trouvé", "Appuyer sur le button de redemarrage quand tu connecte au reseau du manager ou contacter le manager", MessageType.ERROR);
				gui.ModifyMenuGUI("Manager Non trouvé", 0);
				break;
			}
			
			
			deja_commence=true;
			}while(address==null);
			
			if(c.isAlive()) {
				c.resetstop();
				
			}

	}
	public static void main(String[] args) {
		creer_fichier();

//		try {
//			TimeUnit.SECONDS.sleep(60);
//		} catch (InterruptedException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		Main m=new Main();
		m.gui=new GUI();

		do {
			m.initialise();
			 

		m.gui.setGUI();
		  m.snmp_test=m.test_snmp();

		if(!m.snmp_test) 
		{
				ProcessBuilder pr=new ProcessBuilder("cmd","/c",".\\mini_app\\Installer_SNMP.exe");
				try {
				
				Process ps=pr.start();
			
				m.gui.ModifyMenuGUI("installation du SNMP", 0);
					ps.waitFor();
				} catch (InterruptedException 	| IOException e1) {				}
				do {
					try {
				
						TimeUnit.SECONDS.sleep(30);

						m.snmp_test=m.test_snmp();
				
					} catch (InterruptedException e) { }

				}while(!m.snmp_test);
				
			}
		
		m.gui.setButtonAjouterManuellement(m);
		boolean commence=false;
		while(m.address==null) {
			
			
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if(statue==true) {
				m.redemarrer=true;
				commence=false;
				statue=false;
			}
			
		if(m.redemarrer) {

		m.recherche();
		if(m.address==null) {
			m.gui.ajouterButtonRedemarrer(m);
			if(commence==false) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
					statue=ChercherManager.detecterChangement(m);		
					}
				}, "DetecterChangement").start();
				commence=true;
				}
				
		}
		m.redemarrer=false;

				}

		}
		writeInFileANDRegister(m.address.toString());
		
		if(m.gui.popup.getItemCount()==4)m.gui.popup.remove(3);
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(m.gui.popup.getItemCount()==3)m.gui.popup.remove(2);
		
		
		// apres addresse de mnager a ete trouve :
		
		

		

		m.gui.ModifyMenuGUI("Manager Addresse : "+m.address.toString(),0);
	
		m.initInternetTrap();
		m.con=null;
		m.con=new Connectivity();
		m.con.resetStop();
		m.con.setName("internet Thread");
		m.con.start();
			try {
				TimeUnit.SECONDS.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean temp=false;
			temp=m.methodePrincipale();
		if(temp) {
			
			m.con.setStop();
			System.out.println("redemarrage");
			try {
				m.con.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
	}while(true);
		
	}
	
}

	