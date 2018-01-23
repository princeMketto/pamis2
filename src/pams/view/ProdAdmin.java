package pams.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProdAdmin {
	private final SimpleIntegerProperty batchs;
	private final SimpleStringProperty id;
	private final SimpleStringProperty prodname;
	
	private final SimpleStringProperty genericname;
	private final SimpleStringProperty descr;
	private final SimpleStringProperty suppl;
	private final SimpleStringProperty received;
	private final SimpleStringProperty expire;
	private final SimpleDoubleProperty origprice;
	private final SimpleDoubleProperty cellprice;
	private final SimpleDoubleProperty cell1price;
	private final SimpleIntegerProperty qty;
	private final SimpleIntegerProperty qtyleft;
	private final SimpleDoubleProperty profit;
	private final SimpleDoubleProperty total;

	public ProdAdmin(Integer batchs,String id, String prodname,String genericname, String descr, String suppl,
			String received, String expire, double origprice,
			double cellprice,double cell1price, Integer qty, Integer qtyleft,double profit,
			double total) {
		super();
		this.batchs = new SimpleIntegerProperty(batchs);
		this.id = new SimpleStringProperty(id);
		this.prodname = new SimpleStringProperty(prodname);
		this.genericname = new SimpleStringProperty(genericname);
		this.descr = new SimpleStringProperty(descr);
		this.suppl = new SimpleStringProperty(suppl);
		this.received =new SimpleStringProperty( received);
		this.expire = new SimpleStringProperty(expire);
		this.origprice = new SimpleDoubleProperty(origprice);
		this.cellprice = new SimpleDoubleProperty(cellprice);
		this.cell1price = new SimpleDoubleProperty(cell1price);
		this.qty = new SimpleIntegerProperty(qty);
		this.qtyleft = new SimpleIntegerProperty(qtyleft);
		this.profit = new SimpleDoubleProperty(profit);
		this.total = new SimpleDoubleProperty(total);

	}
	public double getCell1price() {
		return cell1price.doubleValue();
	}
	public String getId() {
		return id.get();
	}
	public int getBatchs() {
		return batchs.get();
	}
	public double getProfit() {
		return profit.get();
	}
	public String getProdname() {
		return prodname.get();
	}
	public String getGenericname() {
		return genericname.get();
	}
	public String getDescr() {
		return descr.get();
	}
	public String getSuppl() {
		return suppl.get();
	}
	public String getReceived() {
		return received.get();
	}
	public String getExpire() {
		return expire.get();
	}
	public double getOrigprice() {
		return origprice.get();
	}
	public double getCellprice() {
		return cellprice.get();
	}
	public int getQty() {
		return qty.get();
	}
	public int getQtyleft() {
		return qtyleft.get();
	}
	public double getTotal() {
		return total.get();
	}
	
	
	

}
