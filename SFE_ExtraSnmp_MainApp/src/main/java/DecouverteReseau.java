import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.Timer;
public class DecouverteReseau{
	public static void main(String args[]) {
		DecouverteReseau d=new DecouverteReseau();
		d.getNetworkIPs();
	}
    static byte[] ip = null;
public ArrayList<Ip> getNetworkIPs() {
	ArrayList<Ip> a=new ArrayList<Ip>();

    try {
        ip = InetAddress.getLocalHost().getAddress();
    } catch (Exception e) {
             // exit method, otherwise "ip might not have been initialized"
    }
    for(int i=1;i<=254;i++) {
        final int j = i;  // i as non-final variable cannot be referenced from inner class
        new Thread(new Runnable() {   // new thread for parallel execution
            public void run() {
                try {
                    ip[3] = (byte)j;
                    InetAddress address = InetAddress.getByAddress(ip);
                    String output = address.toString().substring(1);
                    if (address.isReachable(20000)) {
                    	if(!new Ip(output).compare(new Array_IP().Arrayip(),output)) {
                    		a.add(new Ip(output));
                    	}
                    }/* else {
                        System.out.println("Not Reachable: "+output);
                    }*/
                } catch (Exception e) {
                }
            }
        }).start();     // dont forget to start the thread
    }
    new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
        }
    }).start();
    try {
		Thread.sleep(6000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return a;
    }
}
