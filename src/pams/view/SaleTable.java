package pams.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SaleTable {
private final SimpleStringProperty tranID;
private final SimpleStringProperty tranDate;
private final SimpleStringProperty product;
private final SimpleStringProperty cusname;
private final SimpleStringProperty invoname;
private final SimpleIntegerProperty amount;
private final SimpleIntegerProperty profit;
public SaleTable(String tranID, String tranDate, String product, String cusname, String invoname, int amount, int profit) {
	super();
	this.tranID = new SimpleStringProperty(tranID);
	this.tranDate = new SimpleStringProperty(tranDate);
	this.product = new SimpleStringProperty(product);
	this.cusname = new SimpleStringProperty(cusname);
	this.invoname = new SimpleStringProperty(invoname);
	this.amount = new SimpleIntegerProperty(amount);
	this.profit = new SimpleIntegerProperty(profit);
}
public String getProduct() {
	return product.get();
}
public String getTranID() {
	return tranID.get();
}
public String getTranDate() {
	return tranDate.get();
}
public String getCusname() {
	return cusname.get();
}
public String getInvoname() {
	return invoname.get();
}
public int getAmount() {
	return amount.get();
}
public int getProfit() {
	return profit.get();
}

}
