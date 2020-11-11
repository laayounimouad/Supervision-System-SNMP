	
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.asn1.BER;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
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
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.ThreadPool;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class SnmpUtil extends Thread implements PDUFactory, CommandResponder {

	public static final int DEFAULT = 0;
	public static final int WALK = 1;
	public static final int SNMPGET = 4;
	
    public boolean recu=false;
	private Target<Address> _target;
	private static Address _address;
	private OID _authProtocol;
	private OID _privProtocol;
	private OctetString _privPassphrase;
	private OctetString _authPassphrase;
	private OctetString _community = new OctetString("Local");
	private OctetString _contextEngineID;
	private OctetString _contextName = new OctetString();
	private OctetString _securityName = new OctetString();
	private static Snmp _snmp = null;
	private int _numThreads = 1;
	private int _port = 0;
	private ThreadPool _threadPool = null;
	private boolean _isReceiver = false;
	private OctetString _authoritativeEngineID = new OctetString("1234567");
	private TransportMapping<UdpAddress> _transport = null;
	private TimeTicks _sysUpTime = new TimeTicks(0);
	private OID _trapOID = new OID("1.3.6.1.4.1.2789.2005");
	private static info_OID info_oid;
	private static ArrayList<info_OID> WalkTable=new ArrayList<info_OID>(); 
	
	private int _version = 0;
	private int _retries = 1;
	private int _timeout = 1000;
	private int _pduType = 0;
	private Vector<VariableBinding> _vbs = new Vector<VariableBinding>();

	protected int _operation = DEFAULT;
	/*static {
		if (System.getProperty("log4j.configuration") == null) {
			BasicConfigurator.configure();
		}
	}*/
	public static ArrayList<info_OID> getwalktable (){
		return WalkTable;
	}
	public info_OID getInfo_OID() {
		return info_oid;
	}
	public SnmpWalkTable GetWalkTable() {
		SnmpWalkTable s=new SnmpWalkTable("");
		s.setWalkTable(WalkTable);
		return s;
	}
	public SnmpUtil(String host, String varbind, boolean receiver, int type){
		_version = SnmpConstants.version2c;
		_isReceiver = receiver;
		if(type == 2){
			_pduType = PDU.INFORM;
		} else if (type == 1) {
			_pduType = PDU.TRAP;
		}
		else if(type == 3) _pduType=PDU.SET;
		setPort();
		if(!_isReceiver) {
			init(host,varbind);
		} else {
			initReceiver(host);
		}
	}

	public SnmpUtil(String host, String varbind, String user, String authProtocol,
		  String authPasshrase, String privProtocol, String privPassphrase,
		  boolean receiver,int type) {

		_version = SnmpConstants.version3;
		_isReceiver = receiver;
		_privPassphrase = new OctetString(privPassphrase);
		_authPassphrase = new OctetString(authPasshrase);
		_securityName = new OctetString(user);
		if (authProtocol.equals("MD5")) {
			_authProtocol = AuthMD5.ID;
		} else if (authProtocol.equals("SHA")) {
			_authProtocol = AuthSHA.ID;
		}

		if (privProtocol.equals("DES")) {
			_privProtocol = PrivDES.ID;
		} else if ((privProtocol.equals("AES128")) || (privProtocol.equals("AES"))) {
			_privProtocol = PrivAES128.ID;
		} else if (privProtocol.equals("AES192")) {
			_privProtocol = PrivAES192.ID;
		} else if (privProtocol.equals("AES256")) {
			_privProtocol = PrivAES256.ID;
		}
		if(type == 2){
			_pduType = PDU.INFORM;
		} else if (type == 1) {
			_pduType = PDU.TRAP;
		}
		setPort();
		if(!_isReceiver) {
			init(host,varbind);
		} else {
			initReceiver(host);
		}
	}

	public void setVersion(int version) {
		  _version = version;
	}

	public void setOperation(int operation) {
		_operation = operation;
		if(_operation == WALK ){
			 _pduType = PDU.GETNEXT;
		}
		if(_operation == SNMPGET) {
			_pduType=PDU.GET;
		}
	}

	public int getOperation() {
		  return _operation;
	}

	public int getPduType() {
		return _pduType;
	}

	public void setPort() {
		if(_pduType == PDU.INFORM || _pduType == PDU.TRAP ||
			_isReceiver) {
			_port = 162;
		}else{
			_port = 161;
		}
	}

	public void init(String host, String varbind){
		_vbs = getVariableBinding(varbind);
		if(_pduType == PDU.INFORM || _pduType == PDU.TRAP) {
			//checkTrapVariables(_vbs);
		}
		_address = new UdpAddress(host+"/"+_port);
		LogFactory.setLogFactory(new LogFactory());
		BER.setCheckSequenceLength(false);
	}

	public Vector<VariableBinding> getVariableBindings() {
		return _vbs;
	}

	private void addUsmUser(Snmp snmp) {
		snmp.getUSM().addUser(_securityName, new UsmUser(_securityName,
			_authProtocol,
			_authPassphrase,
			_privProtocol,
			_privPassphrase));
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
		if (_version == SnmpConstants.version3) {
			USM usm = new USM(SecurityProtocols.getInstance(),
				new OctetString(MPv3.createLocalEngineID()), 0);
			SecurityModels.getInstance().addSecurityModel(usm);
			addUsmUser(snmp);
		}
		return snmp;
	}

	private Target<Address> createTarget() {
		if (_version == SnmpConstants.version3) {
			UserTarget<Address> target = new UserTarget<Address>();
			if (_authPassphrase != null) {
				if (_privPassphrase != null) {
					target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
				} else {
					target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
				}
			} else {
				target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
			}
			target.setSecurityName(_securityName);
			return target;
		} else {
			CommunityTarget<Address> target = new CommunityTarget<Address>();
			target.setCommunity(_community);
			return target;
		}
	}

	public PDU send() throws IOException {
		_snmp = createSnmpSession();
		this._target = createTarget();
		_target.setVersion(_version);
		_target.setAddress(_address);
		_target.setRetries(_retries);
		_target.setTimeout(_timeout);
		_snmp.listen();

		PDU request = createPDU(_target);
		for (int i=0; i<_vbs.size(); i++) {
			request.add((VariableBinding)_vbs.get(i));
		}

		PDU response = null;
		if(_operation == WALK) {
			response = walk(_snmp, request, _target);
		}
		else if(_operation == SNMPGET) {
			response = SnmpGet(_snmp, request, _target);
		}
		else{
			ResponseEvent<Address> responseEvent;
			long startTime = System.currentTimeMillis();
			responseEvent = _snmp.send(request, _target);
			if (responseEvent != null) {
				response = responseEvent.getResponse();
				System.out.println("Received response after "+
					 (System.currentTimeMillis()-startTime)+" millis");
			}
		}
		_snmp.close();
		return response;
	}

	protected static void printVariableBindings(PDU response) {
		for (int i=0; i<response.size(); i++) {
			VariableBinding vb = response.get(i);
			System.out.println(vb.toString());
		}
	}

	protected static void printReport(PDU response) {
		if (response.size() < 1) {
			System.out.println("REPORT PDU does not contain a variable binding.");
			return;
		}

		VariableBinding vb = response.get(0);
		OID oid =vb.getOid();
		if (SnmpConstants.usmStatsUnsupportedSecLevels.equals(oid)) {
			System.out.print("REPORT: Unsupported Security Level.");
		} else if (SnmpConstants.usmStatsNotInTimeWindows.equals(oid)) {
			System.out.print("REPORT: Message not within time window.");
		} else if (SnmpConstants.usmStatsUnknownUserNames.equals(oid)) {
			System.out.print("REPORT: Unknown user name.");
		} else if (SnmpConstants.usmStatsUnknownEngineIDs.equals(oid)) {
			System.out.print("REPORT: Unknown engine id.");
		} else if (SnmpConstants.usmStatsWrongDigests.equals(oid)) {
			System.out.print("REPORT: Wrong digest.");
		} else if (SnmpConstants.usmStatsDecryptionErrors.equals(oid)) {
			System.out.print("REPORT: Decryption error.");
		} else if (SnmpConstants.snmpUnknownSecurityModels.equals(oid)) {
			System.out.print("REPORT: Unknown security model.");
		} else if (SnmpConstants.snmpInvalidMsgs.equals(oid)) {
			System.out.print("REPORT: Invalid message.");
		} else if (SnmpConstants.snmpUnknownPDUHandlers.equals(oid)) {
			System.out.print("REPORT: Unknown PDU handler.");
		} else if (SnmpConstants.snmpUnavailableContexts.equals(oid)) {
			System.out.print("REPORT: Unavailable context.");
		} else if (SnmpConstants.snmpUnknownContexts.equals(oid)) {
			System.out.print("REPORT: Unknown context.");
		} else {
			System.out.print("REPORT contains unknown OID ("
			 + oid.toString() + ").");
		}

		System.out.println(" Current counter value is " +
		       vb.getVariable().toString() + ".");
	}

	public PDU createPDU(Target target) {
		PDU request;
		if (_target.getVersion() == SnmpConstants.version3) {
			request = new ScopedPDU();
			ScopedPDU scopedPDU = (ScopedPDU)request;
			if (_contextEngineID != null) {
				scopedPDU.setContextEngineID(_contextEngineID);
			}
			if (_contextName != null) {
				scopedPDU.setContextName(_contextName);
			}
		} else {
			request = new PDU();
		}
		request.setType(_pduType);
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
			
		
	  private static PDU walk(Snmp snmp, PDU request, Target<Address> target)
		throws IOException {
		request.setNonRepeaters(0);
		OID rootOID = request.get(0).getOid();
		PDU response = null;
		int objects = 0;
		int requests = 0;
		long startTime = System.currentTimeMillis();
		do {
			requests++;
			ResponseEvent<Address> responseEvent = _snmp.send(request, target);
			response = responseEvent.getResponse();
			if (response != null) {
				objects += response.size();
			}
		}
		
		while (!processWalk(response, request, rootOID));

		System.out.println();
		System.out.println("Total requests sent:    "+requests);
		System.out.println("Total objects received: "+objects);
		System.out.println("Total walk time:        "+
			       (System.currentTimeMillis()-startTime)+" milliseconds");
		return response;
	  }

	  private static boolean processWalk(PDU response, PDU request, OID rootOID) {
		  
		if ((response == null) || (response.getErrorStatus() != 0)
			|| (response.getType() == PDU.REPORT)) {
			return true;
		}
		boolean finished = false;
		OID lastOID = request.get(0).getOid();
		for (int i=0; (!finished) && (i<response.size()); i++) {
			VariableBinding vb = response.get(i);
			if ((vb.getOid() == null) || (vb.getOid().size() < rootOID.size())
				|| (rootOID.leftMostCompare(rootOID.size(), vb.getOid()) != 0)) {
				finished = true;
			} else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
				System.out.println(vb.toString());
				finished = true;
			} else if (vb.getOid().compareTo(lastOID) <= 0) {
				System.out.println("Variable received is not lexicographic successor of requested one:");
				System.out.println(vb.toString() + " <= "+lastOID);
				finished = true;
			} else {
				String vb_variable=vb.getVariable().toString();
				if(vb_variable.contains(":")){
					if(vb_variable.split(":").length==8)
						vb_variable=HexStrConver.dateformat(vb_variable);
					else if(vb_variable.split(":").length>8)
						vb_variable=HexStrConver.testHex2Str(vb_variable);
				}
				vb_variable=OidTranslate.translateContenu(vb.getOid().toString(),vb_variable,_address.toString());

				WalkTable.add(new info_OID(vb.getOid().toString(), vb_variable));
				lastOID = vb.getOid();
			}
		}
		if (response.size() == 0) {
			finished = true;
		}
		if (!finished) {
			VariableBinding next = response.get(response.size()-1);
			next.setVariable(new Null());
			request.set(0, next);
			request.setRequestID(new Integer32(0));
		}
		return finished;
	}

	public void initReceiver(String host) {
		Address address = new UdpAddress(host + "/" + _port);
			boolean stoped=false;
			boolean working=false;
		do {
		try {
			_transport = new DefaultUdpTransportMapping((UdpAddress) address);
			
			stoped=true;
		} catch(IOException ioex) {
			if(!working) {
			try {
				System.out.println("addresse ou port déja utilisé!");
				System.out.println("desactication de service qui utilise le port en cours .....");
				final Process proc =Runtime.getRuntime().exec("cmd /c .\\mini_app\\Stop_SNMPTRAP.exe");
				working=true;
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			}
			
			stoped=false;
			
		}
		}while(!stoped);
		if(working) System.out.println("desactivation reussi ...... program vas completer le fonctionnement");
		
		_threadPool = ThreadPool.create(this.getClass().getName(), _numThreads);

		MessageDispatcher mtDispatcher =
			new MultiThreadedMessageDispatcher(_threadPool, new MessageDispatcherImpl());

		// add message processing models
		mtDispatcher.addMessageProcessingModel(new MPv1());
		mtDispatcher.addMessageProcessingModel(new MPv2c());

		// add all security protocols
		SecurityProtocols.getInstance().addDefaultProtocols();

		_snmp = new Snmp(mtDispatcher, _transport);
		if(_snmp != null){
			_snmp.addCommandResponder(this);
		} else {
			System.out.println("Unable to create Target object");
			System.exit(-1);
		}

		if(_version == SnmpConstants.version3) {
			mtDispatcher.addMessageProcessingModel(new MPv3());

			MPv3 mpv3 =
				(MPv3)_snmp.getMessageProcessingModel(MessageProcessingModel.MPv3);

			USM usm = new USM(SecurityProtocols.getInstance(),
				new OctetString(mpv3.createLocalEngineID()), 0);

			SecurityModels.getInstance().addSecurityModel(usm);

			if (_authoritativeEngineID != null) {
				_snmp.setLocalEngine(_authoritativeEngineID.getValue(), 0, 0);
			}

			this.addUsmUser(_snmp);
		}
	}

	public synchronized void listen() {
		try {
			
			_transport.listen();
			
		} catch(IOException ioex) {
			System.out.println("Unable to listen: " + ioex);
			System.exit(-1);
		}

		System.out.println("Waiting for traps..");
		try {
			this.wait();//Wait for traps to come in
			
		} catch (InterruptedException ex) {
			System.out.println("Interrupted while waiting for traps: " + ex);
			System.exit(-1);
		}
	}

	public synchronized void processPdu(CommandResponderEvent e) {
		String[] tab;
		String ip = null;
		 ip=e.getPeerAddress().toString().split("/")[0];
		PDU command = e.getPDU();
		if (command != null) {
			if ((command.getType() != PDU.TRAP) &&
					(command.getType() != PDU.V1TRAP) &&
					(command.getType() != PDU.REPORT) &&
					(command.getType() != PDU.RESPONSE)) {
					command.setErrorIndex(0);
					command.setErrorStatus(0);
					command.setType(PDU.RESPONSE);
					StatusInformation statusInformation = new StatusInformation();
					StateReference<Address> ref = e.getStateReference();
					try {
						e.getMessageDispatcher().returnResponsePdu(e.
							getMessageProcessingModel(),
							e.getSecurityModel(),
							e.getSecurityName(),
							e.getSecurityLevel(),
							command,
							e.getMaxSizeResponsePDU(),
							ref,
							statusInformation);
					}catch (MessageException ex) {
						System.err.println("Error while sending response: "+ex.getMessage());
					}
					if(!new Ip(ip).compare(new Array_IP().Array(), ip)) 
						DbConnection.Db_addAddress(ip,new Ip(ip).name(), DbConnection.getConnection());

//						new Ip(ip).add();
					//ajouter dans la liste
					if(!ListeUtilisateurs.getListe().containsKey(new IpAddress(ip))) {
					Utilisateurs uti=new Utilisateurs(new IpAddress(ip));
					String name=new Ip(ip).name();
					if(!name.equalsIgnoreCase("Name not found")) uti.setName(name);
					uti.setStatue(true);
					uti.setSnmpExist(true);
					ListeUtilisateurs.addToListe(uti, new IpAddress(ip));
					}
				}
			else{
			/*affichage snmp receiver*/
			tab=e.getPDU().toString().split("VBS\\[")[1].split(";")[0].split("=");
		    recu =true;
		    		if(!tab[0].contains("1.3.6.1.2.1.2.2.1.1.")) {
						ListeUtilisateurs.updateUtilisateur(new IpAddress(ip), tab[1]);
					}
			try {
				if(IconNotification.notification) if(IconNotification.getTrayIcon()!=null) IconNotification.getTrayIcon().displayMessage("Une nouvelle trap est generer", "",MessageType.INFO);
				//if(!tab[0].contains("1.3.6.1.2.1.2.2.1.1.")) {
				Connection con=(Connection) DbConnection.getConnection();
			    Statement stmt;
							 // the mysql insert statement
			      String query = " insert into oid (oid,text_oid,type)"
			        + " values (?, ?, ?)";
			      // create the mysql insert preparedstatement
			      PreparedStatement preparedStmt = con.prepareStatement(query);
			      preparedStmt.setString (1, tab[0]);

					if(!tab[0].contains("1.3.6.1.2.1.2.2.1.1.")) {
						//ListeUtilisateurs.updateUtilisateur(new IpAddress(ip), tab[1]);
			      preparedStmt.setString (2, OidTranslate.translate(command.toString()));
					}
					else
					    preparedStmt.setString (2, OidTranslate.generic(command.toString()));
			      preparedStmt.setString(3, "Trap");
			      preparedStmt.execute();
				
       			  	stmt = (Statement) con.createStatement();
					ResultSet rs=stmt.executeQuery("select max(id_oid) from oid;"); 
					
					final String sql ="select ID from agent where IP= ? ";
				  	PreparedStatement ps = con.prepareStatement(sql);
				    ps.setString(1, ip);
					ResultSet rs2=ps.executeQuery();  
					
			      // the mysql insert statement
			      String query2 = " insert into posseder (ID,id_oid)"
			        + " values (?, ?)";
			      // create the mysql insert preparedstatement
			      PreparedStatement preparedStmt2 = con.prepareStatement(query2);
					while(rs2.next())
			      preparedStmt2.setInt (1, rs2.getInt(1));
					while(rs.next())  
			      preparedStmt2.setInt (2, rs.getInt(1));
				  // execute the preparedstatement
			      preparedStmt2.execute();
			      // execute the preparedstatement
				
			}catch (Exception e2) {
				System.out.println("erreur");
				e2.printStackTrace();
			}
			}}			
		}
	

	public void sendAndProcessResponse() {
		try {
			PDU response = this.send();
			if ((getPduType() == PDU.TRAP) ||
				(getPduType() == PDU.REPORT) ||
				(getPduType() == PDU.V1TRAP) ||
				(getPduType() == PDU.RESPONSE)) {
				System.out.println(PDU.getTypeString(getPduType()) +
					" sent successfully");
			} else if (response == null) {
				System.out.println("Request timed out.");
			} else if (response.getType() == PDU.REPORT) {
				printReport(response);
			} else if (getOperation() == WALK) {
				System.out.println("End of walked subtree '"+
					((VariableBinding)getVariableBindings().get(0)).getOid()+
					"' reached at:");
				printVariableBindings(response);
			} else {
				System.out.println("Received something strange: requestID=" +
					response.getRequestID() +
					", errorIndex=" +
					response.getErrorIndex() + ", " +
					"errorStatus=" + response.getErrorStatusText()+
					"("+response.getErrorStatus()+")");
				printVariableBindings(response);
			}
		} catch (IOException ex) {
			System.err.println("Error while trying to send request: " +
				ex.getMessage());
				ex.printStackTrace();
		}
	}

	private void checkTrapVariables(Vector<VariableBinding> vbs) {
		if ((_pduType == PDU.INFORM) || (_pduType == PDU.TRAP)) {
			if ((vbs.size() == 0) ||
				((vbs.size() > 1) &&
				(!((VariableBinding) vbs.get(0)).getOid().equals(SnmpConstants.
				sysUpTime)))) {
				vbs.add(0, new VariableBinding(SnmpConstants.sysUpTime, _sysUpTime));
			}
			if ((vbs.size() == 1) || ((vbs.size() > 2) &&
				(!((VariableBinding) vbs.get(1)).getOid().equals(SnmpConstants.
				snmpTrapOID)))) {
				vbs.add(1, new VariableBinding(SnmpConstants.snmpTrapOID, _trapOID));
			}
		}
	}

	public PDU createPDU(MessageProcessingModel messageProcessingModel) {
		// TODO Auto-generated method stub
		return null;
	}
}

