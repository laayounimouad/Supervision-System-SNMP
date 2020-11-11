package trap;
public class SnmpTrap {

	SnmpUtil _util = null;
	private boolean InformReceived=false;
	public boolean getInformReceived() {
		return InformReceived;
	}
	public SnmpTrap(String host, String varbind, int type){
		_util = new SnmpUtil(host,varbind,false,type);
	}

	public SnmpTrap(String host, String varbind, int type, String user, String authProtocol, 
		  String authPasshrase, String privProtocol, String privPassphrase) {
		  _util = new SnmpUtil(host,varbind,user,authProtocol,authPasshrase,privProtocol,privPassphrase,false,type);
	}

	public boolean doTrap() {
		_util.sendAndProcessResponse();
		if(_util.InformReceived) {
			InformReceived=_util.InformReceived;
		}
		return _util.InformReceived;
	}

}
