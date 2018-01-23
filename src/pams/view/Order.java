package pams.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Order {
	private final SimpleStringProperty id;
	private final SimpleStringProperty name;
	private final SimpleStringProperty gen;
	private final SimpleStringProperty desc;
	private final SimpleIntegerProperty qty;
	private final SimpleStringProperty vendor;
	public Order(String id, String name, String gen,
			String desc,int qty, String vendor) {
		super();
		this.id = new SimpleStringProperty(id);
		this.name = new SimpleStringProperty(name);
		this.gen = new SimpleStringProperty(gen);
		this.desc = new SimpleStringProperty(desc);
		this.qty = new SimpleIntegerProperty(qty);
		this.vendor = new SimpleStringProperty(vendor);
	}
	public int getQty() {
		return qty.get();
	}
	public String getId() {
		return id.get();
	}
	public String getName() {
		return name.get();
	}
	public String getGen() {
		return gen.get();
	}
	public String getDesc() {
		return desc.get();
	}
	public String getVendor() {
		return vendor.get();
	}
	
}
