package pams.view;

import javafx.beans.property.SimpleStringProperty;

public class AddUser {
private final SimpleStringProperty id;
private final SimpleStringProperty fname;
private final SimpleStringProperty lname;
private final SimpleStringProperty uname;
private final SimpleStringProperty sex;
private final SimpleStringProperty phone;
private final SimpleStringProperty sectn;
public AddUser(String id, String fname, String lname, String uname, String sex, String phone, String sectn) {
	super();
	this.id = new SimpleStringProperty(id);
	this.fname =new SimpleStringProperty(fname);
	this.lname =new SimpleStringProperty(lname);
	this.uname =new SimpleStringProperty(uname);
	this.sex =new SimpleStringProperty(sex);
	this.phone =new SimpleStringProperty(phone);
	this.sectn =new SimpleStringProperty(sectn);
	}
public String getId() {
	return id.get();
}
public String getFname() {
	return fname.get();
}
public String getLname() {
	return lname.get();
}
public String getUname() {
	return uname.get();
}
public String getSex() {
	return sex.get();
}
public String getPhone() {
	return phone.get();
}
public String getSectn() {
	return sectn.get();
}
}