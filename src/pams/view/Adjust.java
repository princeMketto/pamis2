package pams.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Adjust {
private final SimpleStringProperty id;
private final SimpleStringProperty name;
private final SimpleStringProperty date;
private final SimpleStringProperty remak;
private final SimpleIntegerProperty qty;
private final SimpleStringProperty issue;
public Adjust(String id,String name, String date, String remak, int qty,String issue) {
	super();
	this.name = new SimpleStringProperty(name);
	this.id = new SimpleStringProperty(id);
	this.issue =new SimpleStringProperty(issue);
	this.qty =new SimpleIntegerProperty(qty);
	this.date =new SimpleStringProperty(date);
	this.remak =new SimpleStringProperty(remak);

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
public int getQty() {
	return qty.get();
}
public String getRemak() {
	return remak.get();
}
public String getIssue() {
	return issue.get();
}

}
