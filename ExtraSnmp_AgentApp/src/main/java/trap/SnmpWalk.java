package trap;

public class SnmpWalk {

	  public static final int WALK = 1;
	  private SnmpUtil _util = null;

	  public SnmpWalk(String host, String oid){
		_util = new SnmpUtil(host,oid,false,0);
		_util.setOperation(WALK);
	  }
	  public SnmpWalk(String host, String oid, String user, String authProtocol, 
			String authPasshrase, String privProtocol, String privPassphrase) {

		  _util = new SnmpUtil(host,oid,user,authProtocol,authPasshrase,privProtocol,privPassphrase,false,0);
		  _util.setOperation(WALK);
	  }
	  
	  public boolean doWalk() {
		_util.sendAndProcessResponse();
		return _util.snmp_test;
	  }
	  public boolean getSnmp_test() {
		  return _util.getSnmp_Test();
	  }

}

