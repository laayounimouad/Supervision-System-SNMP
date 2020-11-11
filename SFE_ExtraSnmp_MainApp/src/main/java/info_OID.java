

public class info_OID {

	private String oid;
	private String variable;

	

	public info_OID(String oid, String variable) {
		super();
		this.oid = oid;
		this.variable = variable;
	}



	public String getOid() {
		return oid;
	}



	public void setOid(String oid) {
		this.oid = oid;
	}



	public String getVariable() {
		return variable;
	}



	public void setVariable(String variable) {
		this.variable = variable;
	}



	@Override
	public String toString() {
		return "info_Oid [String=" + oid + ", String=" + variable + "]";
	}
	
	
	
}
