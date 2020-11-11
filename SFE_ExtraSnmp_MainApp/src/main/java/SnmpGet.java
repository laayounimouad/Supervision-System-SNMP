
public class SnmpGet {
	  public static final int SNMPGET = 4;

	  private SnmpUtil_Get _util = null;
	public SnmpGet(String host, String oid){
		_util = new SnmpUtil_Get(host,oid);
	  }
	
	public info_OID Get() {
		_util.sendAndProcessResponse();
		return _util.getInfo_OID();
	  }
	}
