package pams.view;

import javafx.beans.property.SimpleStringProperty;
//SimpleStringProperty
public class Supplier {
	private final SimpleStringProperty Sid;
	private final SimpleStringProperty Sname;
	private final SimpleStringProperty Saddress;
	private final SimpleStringProperty Semail;
	private final SimpleStringProperty Scontact;
	private final SimpleStringProperty Snote;
	private final SimpleStringProperty Saction;

	public Supplier(String Sid, String Sname, String Saddress,String Semail,
			String Scontact, String Snote, String Saction) {
		super();
		this.Sid = new SimpleStringProperty(Sid);
		this.Sname =new SimpleStringProperty(Sname);
		this.Saddress =new SimpleStringProperty(Saddress);
		this.Semail =new SimpleStringProperty(Semail);
		this.Scontact =new SimpleStringProperty(Scontact);
		this.Snote =new SimpleStringProperty(Snote);
		this.Saction =new SimpleStringProperty(Saction);
	}
	
	public String getSemail() {
		return Semail.get();
	}

	public String getSid() {
		return Sid.get();
	}
	public String getSname() {
		return Sname.get();
	}
	public String getSaddress() {
		return Saddress.get();
	}
	public String getScontact() {
		return Scontact.get();
	}
	public String getSnote() {
		return Snote.get();
	}

	public String getSaction() {
		return Saction.get();
	}
	
}
