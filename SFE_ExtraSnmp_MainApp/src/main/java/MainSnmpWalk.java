public class MainSnmpWalk{

	public MainSnmpWalk(){
	}

	public static SnmpWalkTable s;
	public static void main(String[] args){
		System.out.println("Doing SNMPv2 walk..");
		//SnmpWalk walk = new SnmpWalk("127.0.0.1","1.3.6.1.2.1.25.6.3.1.5");
		SnmpWalk walk = new SnmpWalk("192.168.1.9","1.3.6.1.2.1.1.5");
		s=walk.doWalk();
		s.afficherWalkTable();
/*
		System.out.println("Doing SNMPv3 walk..");
		walk = new SnmpWalk("127.0.0.1","1.3.6.1.2.1.1",
			"kschmidt","MD5","mysecretpass","DES","mypassphrase");
		walk.doWalk();*/
	}
}

