public class SnmpSet {

	  private SnmpUtil _util = null;

	  public SnmpSet(String host, String varbind){
		_util = new SnmpUtil(host,varbind,false,3);
	  }

	  public SnmpSet(String host, String varbind, String user, String authProtocol, 
		String authPasshrase, String privProtocol, String privPassphrase) {

		_util = new SnmpUtil(host,varbind,user,authProtocol,
			authPasshrase,privProtocol,privPassphrase,false,0);
	  }

	  public void doSet() {
		  _util.sendAndProcessResponse();
	  }
}
