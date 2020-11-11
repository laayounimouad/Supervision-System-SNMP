import java.io.IOException;

public class Agent {

	private Ip ip;
	private String etatPing;
	public Agent(Ip ip) {
		super();
		this.ip = ip;
		try {
			if(new Ping(ip).sendPingRequest())
				this.etatPing = "Active";
			else
				this.etatPing = "Inactive";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Ip getIp() {
		return ip;
	}

	public String getEtatPing() {
		return etatPing;
	}
	
	
}
