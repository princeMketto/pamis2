package pams.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Price {
private final SimpleStringProperty id;
private final SimpleStringProperty name;
private final SimpleDoubleProperty price;
private final SimpleDoubleProperty pricewhol;
public Price(String id,String name, double price,double pricewhol ) {
	super();
	this.name = new SimpleStringProperty(name);
	this.id = new SimpleStringProperty(id);
	this.price =new SimpleDoubleProperty(price);
	this.pricewhol =new SimpleDoubleProperty(pricewhol);

	}
public String getId() {
	return id.get();
}
public String getName() {
	return name.get();
}
public double getPrice() {
	return price.get();
}
public double getPricewhol() {
	return pricewhol.get();
}

}
