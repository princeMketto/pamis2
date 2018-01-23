package pams.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Profoma {
private final SimpleStringProperty id;
private final SimpleStringProperty name;
private final SimpleStringProperty date;
private final SimpleDoubleProperty amount;
private final SimpleStringProperty address;
private final SimpleStringProperty phone;
public Profoma(String id,String date, double amount, String name, String address,String phone) {
	super();
	this.name = new SimpleStringProperty(name);
	this.id = new SimpleStringProperty(id);
	this.phone =new SimpleStringProperty(phone);
	this.amount =new SimpleDoubleProperty(amount);
	this.date =new SimpleStringProperty(date);
	this.address =new SimpleStringProperty(address);

	}
public String getId() {
	return id.get();
}
public String getName() {
	return name.get();
}
public String getDate() {
	return date.get();
}
public double getAmount() {
	return amount.get();
}
public String getAddress() {
	return address.get();
}
public String getPhone() {
	return phone.get();
}

}
