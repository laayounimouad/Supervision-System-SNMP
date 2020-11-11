

public class Trap {
int id;
private String ip_port;
private String Date_Time;
private String msg_trap;

public Trap(int id,String ip_port, String msg_trap, String Date_Time) {
	super();
	this.id=id;
	this.ip_port = ip_port;
	this.msg_trap = msg_trap;
	this.Date_Time = Date_Time;

}

public String toString() {
	return "date_trap=" + Date_Time + ", ip_port=" + ip_port + ", msg_trap=" + msg_trap;
}

public String getDate_Time() {
	return Date_Time;
}


public String getIp_port() {
	return ip_port;
}

public String getMsg_trap() {
	return msg_trap;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}


}
