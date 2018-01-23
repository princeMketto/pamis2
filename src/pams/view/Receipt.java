package pams.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Receipt {
	private final SimpleStringProperty prodname;
	private final SimpleIntegerProperty discount;
	private final SimpleIntegerProperty price;
	private final SimpleIntegerProperty qty;
	private final SimpleIntegerProperty amount;
	public Receipt(String prodname, int qty, int price, int discount, int amount) {
		super();
		this.prodname = new SimpleStringProperty(prodname);
		this.discount =new SimpleIntegerProperty(discount);
		this.price =new SimpleIntegerProperty(price);
		this.qty =new SimpleIntegerProperty(qty);
		this.amount =new SimpleIntegerProperty(amount);
	
		}
	public String getProdname() {
		return prodname.get();
	}
	public int getDiscount() {
		return discount.get();
	}
	public int getPrice() {
		return price.get();
	}
	public int getQty() {
		return qty.get();
	}
	public int getAmount() {
		return amount.get();
	}
}
