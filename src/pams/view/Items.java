package pams.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Items {
private final SimpleStringProperty id;
private final SimpleStringProperty prodname;
private final SimpleStringProperty category;
private final SimpleDoubleProperty price;
private final SimpleIntegerProperty qty;
private final SimpleDoubleProperty amount;
private final SimpleStringProperty action;
public Items(String id,String prodname, String category, double price, int qty, double amount, String action) {
	super();
	this.prodname = new SimpleStringProperty(prodname);
	this.id = new SimpleStringProperty(id);
	this.category =new SimpleStringProperty(category);
	this.price =new SimpleDoubleProperty(price);
	this.qty =new SimpleIntegerProperty(qty);
	this.amount =new SimpleDoubleProperty(amount);
	this.action =new SimpleStringProperty(action);
	}
public String getProdname() {
	return prodname.get();
}
public String getId() {
	return id.get();
}
public String getCategory() {
	return category.get();
}
public double getPrice() {
	return price.get();
}
public int getQty() {
	return qty.get();
}
public double getAmount() {
	return amount.get();
}
public String getAction() {
	return action.get();
}

}
