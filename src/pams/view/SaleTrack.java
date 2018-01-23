package pams.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SaleTrack {
	private final SimpleStringProperty id;
	private final SimpleStringProperty name;
	private final SimpleStringProperty item;
	private final SimpleStringProperty date;
	private final SimpleIntegerProperty qty;
	private final SimpleStringProperty typ;
	private final SimpleDoubleProperty pric;
	private final SimpleDoubleProperty amoun;
	private final SimpleStringProperty custom;
	public SaleTrack(String id,String name, String date,String item, String typ,int qty, double pric, double amoun, String custom) {
		super();
		this.name = new SimpleStringProperty(name);
		this.item = new SimpleStringProperty(item);
		this.id = new SimpleStringProperty(id);
		this.date =new SimpleStringProperty(date);
		this.typ =new SimpleStringProperty(typ);
		this.qty =new SimpleIntegerProperty(qty);
		this.pric =new SimpleDoubleProperty(pric);
		this.amoun =new SimpleDoubleProperty(amoun);
		this.custom =new SimpleStringProperty(custom);
		
			}
	
	public String getId() {
		return id.get();
	}
	public String getName() {
		return name.get();
	}
	public String getItem() {
		return item.get();
	}
	public String getDate() {
		return date.get();
	}
	public int getQty() {
		return qty.get();
	}
	public double getPrice() {
		return pric.get();
	}
	public double getAmount() {
		return amoun.get();
	}
	public String getCustom() {
		return custom.get();
	}
	public String getType() {
		return typ.get();
	}

}
