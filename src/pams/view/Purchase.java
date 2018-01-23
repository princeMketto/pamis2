package pams.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Purchase {
private final SimpleStringProperty prodname;
private final SimpleIntegerProperty qty;
private final SimpleStringProperty received;
private final SimpleStringProperty expire;
private final SimpleDoubleProperty origprice;
private final SimpleDoubleProperty cellprice;
private final SimpleStringProperty suppl;
private final SimpleStringProperty invo;
public Purchase(String prodname,int qty, String received, String expire, double origprice,double cellprice
		,String suppl,String invo) {
	super();
	this.prodname = new SimpleStringProperty(prodname);
	this.qty =new SimpleIntegerProperty(qty);
	this.received = new SimpleStringProperty(received);
	this.expire =new SimpleStringProperty(expire);
	this.origprice =new SimpleDoubleProperty(origprice);
	this.cellprice =new SimpleDoubleProperty(cellprice);
	this.suppl =new SimpleStringProperty(suppl);
	this.invo =new SimpleStringProperty(invo);

	}
public String getProdname() {
	return prodname.get();
}
public int getQty() {
	return qty.get();
}
public String getReceived() {
	return received.get();
}
public String getExpire() {
	return expire.get();
}
public double getOrigprice() {
	return origprice.doubleValue();
}
public double getCellprice() {
	return cellprice.doubleValue();
}
public String getSuppl() {
	return suppl.get();
}
public String getInvo() {
	return invo.get();
}

}
