
	import java.awt.BorderLayout;
	import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.ImageIcon;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JProgressBar;
	import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Font;


	public class Progress implements Runnable {
		public static void main(String[] args) {
		    Progress pr= new Progress();
		}
		JFrame frameProg = new JFrame();
		public Progress() {
	run();
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			JProgressBar progressbar = new JProgressBar(0, 100);
			progressbar.setIndeterminate(true);
			progressbar.setForeground(new Color(50, 205, 50));
			progressbar.setBounds(10, 206, 414, 20);
			frameProg.getContentPane().setBackground(Color.WHITE);
			frameProg.setIconImage(Toolkit.getDefaultToolkit().getImage("images/S_Logo.jpg"));
			frameProg.setSize(459, 318);
			frameProg.setResizable(false);
			frameProg.setLocationRelativeTo(null);
			frameProg.getContentPane().setLayout(null);
			frameProg.getContentPane().add(progressbar);
			
			JLabel lblAlert = new JLabel("Veuillez patientez quelque secondes ...");
			lblAlert.setForeground(Color.BLACK);
			lblAlert.setBounds(121, 237, 236, 20);
			frameProg.getContentPane().add(lblAlert);
			
			JLabel lblVotreApplicationEst = new JLabel("Votre application est en cours de demarrage.");
			lblVotreApplicationEst.setBackground(Color.WHITE);
			lblVotreApplicationEst.setForeground(Color.BLACK);
			lblVotreApplicationEst.setBounds(108, 258, 260, 20);
			frameProg.getContentPane().add(lblVotreApplicationEst);
			
			JLabel lblNewLabel = new JLabel("New label");
			lblNewLabel.setBounds(133, 11, 161, 186);
			lblNewLabel.setIcon(new ImageIcon("images/ExtraSNMP1.png"));

			frameProg.getContentPane().add(lblNewLabel);
			frameProg.setTitle("Loading...");
			frameProg.setVisible(true);
			frameProg.setDefaultCloseOperation(frameProg.EXIT_ON_CLOSE);

			        for(int i = 0; i < 100; i++){
			            try {
			                Thread.sleep(100);
			            } catch (Exception e) {
			                //exception
			            }
			            progressbar.setValue(i);
			        }
			        
					frameProg.dispose();
			    }
		}	
	
