	
import java.io.IOException;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.asn1.BER;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.PDUFactory;

public class SnmpUtil_Get extends Thread implements PDUFactory {

	public static final int SNMPGET = 4;
	
	private Target<Address> _target;
	private static Address _address;
	private OctetString _community = new OctetString("Local");
	private static Snmp _snmp = null;
	private int _port = 161;
	private static info_OID info_oid;
	
	private Vector<VariableBinding> _vbs = new Vector<VariableBinding>();

	protected int _operation = SNMPGET;
	public info_OID getInfo_OID() {
		return info_oid;
	}
	public SnmpUtil_Get(String host, String varbind){
		_vbs = getVariableBinding(varbind);
		_address = new UdpAddress(host+"/"+_port);
		LogFactory.setLogFactory(new LogFactory());
		BER.setCheckSequenceLength(false);		
		}

	private Snmp createSnmpSession() throws IOException {
		Snmp snmp;
		if (_address instanceof TcpAddress) {
			AbstractTransportMapping<TcpAddress> transport = new DefaultTcpTransportMapping();
			snmp =  new Snmp(transport);
		} else {
			AbstractTransportMapping<UdpAddress> transport= new DefaultUdpTransportMapping();
			snmp =  new Snmp(transport);
		}
		// Could save some CPU cycles:
		// transport.setAsyncMsgProcessingSupported(false);
		return snmp;
	}

	private Target<Address> createTarget() {
			CommunityTarget<Address> target = new CommunityTarget<Address>();
			target.setCommunity(_community);
			return target;
	}

	public PDU send() throws IOException {
		_snmp = createSnmpSession();
		this._target = createTarget();
		_target.setVersion(SnmpConstants.version2c);
		_target.setAddress(_address);
		_target.setRetries(1);
		_target.setTimeout(1000);
		_snmp.listen();

		PDU request = createPDU(_target);
		for (int i=0; i<_vbs.size(); i++) {
			request.add((VariableBinding)_vbs.get(i));
		}

		PDU response = null;
			response = SnmpGet(_snmp, request, _target);
			
		_snmp.close();
		return response;
	}
	public PDU createPDU(Target target) {
		PDU request;
			request = new PDU();
		request.setType(PDU.GET);
		return request;
	}
	private Vector<VariableBinding> getVariableBinding(String varbind) {
		Vector<VariableBinding> v = new Vector<VariableBinding>(varbind.length());
		String oid = null;
		char type = 'i';
		String value = null;
		int equal = varbind.indexOf("={");
		if (equal > 0) {
			oid = varbind.substring(0, equal);
			type = varbind.charAt(equal+2);
			value = varbind.substring(varbind.indexOf('}')+1);
		}else{
			v.add(new VariableBinding(new OID(varbind)));
			return v;
		}

		VariableBinding vb = new VariableBinding(new OID(oid));
		if (value != null) {
			Variable variable;
			switch (type) {
				case 'i':
					variable = new Integer32(Integer.parseInt(value));
					break;
				case 'u':
					variable = new UnsignedInteger32(Long.parseLong(value));
					break;
				case 's':
					variable = new OctetString(value);
					break;
				case 'x':
					variable = OctetString.fromString(value, ':', 16);
					break;
				case 'd':
					variable = OctetString.fromString(value, '.', 10);
					break;
				case 'b':
					variable = OctetString.fromString(value, ' ', 2);
					break;
				case 'n':
					variable = new Null();
					break;
				case 'o':
					variable = new OID(value);
					break;
				case 't':
					variable = new TimeTicks(Long.parseLong(value));
					break;
				case 'a':
					variable = new IpAddress(value);
					break;
				default:
					throw new IllegalArgumentException("Variable type "+type+
					       " not supported");
			}
			vb.setVariable(variable);
		}
		v.add(vb);
		return v;
	  }

		public PDU SnmpGet(Snmp snmp, PDU request, Target<Address> target) throws IOException {
			request.setNonRepeaters(0);
			PDU response = null;
			
				ResponseEvent<Address> responseEvent = _snmp.send(request, target);
				response = responseEvent.getResponse();
				VariableBinding vb = response.get(0);
				String vb_variable=vb.getVariable().toString();

				if(vb_variable.contains(":")){
					if(vb_variable.split(":").length==8)
						vb_variable=HexStrConver.dateformat(vb_variable);
					else if(vb_variable.split(":").length>8)
						vb_variable=HexStrConver.testHex2Str(vb_variable);
				}
				vb_variable=OidTranslate.translateContenu(vb.getOid().toString(),vb_variable ,_address.toString());
				info_oid=null;
				info_oid=new info_OID(vb.getOid().toString(), vb_variable );
				System.out.println(response);
				return response;
			}
			
	public void sendAndProcessResponse() {
		try {
			PDU response = this.send();
			if (response == null) {
				System.out.println("Request timed out.");
			} 
		} catch (IOException ex) {
			System.err.println("Error while trying to send request: " +
				ex.getMessage());
				ex.printStackTrace();
		}
	}
	public PDU createPDU(MessageProcessingModel messageProcessingModel) {
		// TODO Auto-generated method stub
		return null;
	}
}