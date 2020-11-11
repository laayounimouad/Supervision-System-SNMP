
import java.util.HashMap;

import org.snmp4j.smi.IpAddress;


public class Utilisateurs {
	
	
	private IpAddress ipAddress;
	private String name;
	private Boolean Statue;
	private Boolean SnmpExist;
	private Boolean internetStatue;
	private String bandwidth;
	private String ping;
	private String download;
	private String upload;
	
	
	public Utilisateurs(IpAddress ipAddress) {
		this.ipAddress=ipAddress;
		this.bandwidth=null;
		this.internetStatue=null;
		this.name=null;
		this.SnmpExist=null;
		this.Statue=null;
	}
	public IpAddress getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getStatue() {
		return Statue;
	}
	public void setStatue(Boolean statue) {
		Statue = statue;
	}
	public Boolean getSnmpExist() {
		return SnmpExist;
	}
	public void setSnmpExist(Boolean snmpExist) {
		SnmpExist = snmpExist;
	}
	public Boolean getInternetStatue() {
		return internetStatue;
	}
	public void setInternetStatue(Boolean internetStatue) {
		this.internetStatue = internetStatue;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String ping, String download,String upload) {
		this.bandwidth = ping+" "+download+" "+upload;
	}
	public void setBandwidth() {
		this.bandwidth = this.ping+" "+this.download+" "+this.upload;
	}
	public void setPing(String ping) {
		this.ping=ping;
	}
	public void setDownload(String download) {
		this.download=download;
	}
	public void setUpload(String upload) {
		this.upload=upload;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth=bandwidth;
	}
	public String getPing() {
		return ping;
	}
	public String getDownload() {
		return download;
	}
	public String getUpload() {
		return upload;
	}
	
	@Override
	public String toString() {
		return (name==null) ? ipAddress.toString() : name;
	}

	
	
}
