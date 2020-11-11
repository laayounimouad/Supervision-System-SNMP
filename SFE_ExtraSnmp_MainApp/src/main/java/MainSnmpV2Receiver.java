

public class MainSnmpV2Receiver{

	public MainSnmpV2Receiver( ){
		V2TrapReceiver v2 = new V2TrapReceiver("0.0.0.0");
		v2.listen( );
	}

}