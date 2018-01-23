package pams.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Inventory {
private final SimpleStringProperty id;
private final SimpleStringProperty name;
private final SimpleStringProperty date;
private final SimpleIntegerProperty qty;
private final SimpleIntegerProperty qtyused;
private final SimpleIntegerProperty qtyremain;
private final SimpleStringProperty remak;
private final SimpleStringProperty status;
private final SimpleStringProperty view;
public Inventory(String id,String name, String date, int qty, int qtyused, int qtyremain, String remak, String status,String view) {
	super();
	this.name = new SimpleStringProperty(name);
	this.id = new SimpleStringProperty(id);
	this.date =new SimpleStringProperty(date);
	this.qtyused =new SimpleIntegerProperty(qtyused);
	this.qty =new SimpleIntegerProperty(qty);
	this.qtyremain =new SimpleIntegerProperty(qtyremain);
	this.remak =new SimpleStringProperty(remak);
	this.status =new SimpleStringProperty(status);
	this.view =new SimpleStringProperty(view);
	
		}
public String getView() {
	return view.get();
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
public int getQtyused() {
	return qtyused.get();
}
public int getQtyremain() {
	return qtyremain.get();
}
public String getRemak() {
	return remak.get();
}
public String getStatus() {
	return status.get();
}

}
