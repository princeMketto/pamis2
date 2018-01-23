package pams.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ReportTableController implements Initializable{
	Main main;
	ReportController reps =new ReportController();
	private WorkIndicatorDialog wd=null;
	  @FXML
	    private JFXButton btnback;

	    @FXML
	    private JFXButton btnPrint;

	    @FXML
	    private TableView<SaleTable> tabledata;

	    @FXML
	    private TableColumn<SaleTable, String> transID;

	    @FXML
	    private TableColumn<SaleTable, String> date;

	    @FXML
	    private TableColumn<SaleTable, String> product;

	    @FXML
	    private TableColumn<SaleTable, String> customer;

	    @FXML
	    private TableColumn<SaleTable, String> invoice;

	    @FXML
	    private TableColumn<SaleTable, Integer> amount;
	    @FXML
	    private Label totamount;

	    @FXML
	    private Label totprofit;
	    private List<Double>pesa = new ArrayList<Double>();
	    private List<Double>faida = new ArrayList<Double>();

	    @FXML
	    private TableColumn<SaleTable, Integer> profit;
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs;
		    private Statement st;
		    private PreparedStatement prep,prep1;
	    ObservableList<SaleTable>list = FXCollections.observableArrayList();
	  static  String start = null,end = null;
		  double toAmount = 0;
		  double toProfit=0;
		  int totamo = 0,totpro = 0;
		  boolean su = false;
		  int s=0;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		transID.setCellValueFactory(new PropertyValueFactory<>("tranID"));
    	date.setCellValueFactory(new PropertyValueFactory<>("tranDate"));
    	product.setCellValueFactory(new PropertyValueFactory<>("product"));
    	customer.setCellValueFactory(new PropertyValueFactory<>("cusname"));
    	invoice.setCellValueFactory(new PropertyValueFactory<>("invoname"));
    	amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    	profit.setCellValueFactory(new PropertyValueFactory<>("profit"));
    	
    	fillTable();
    
	}
	  @SuppressWarnings("unchecked")
	private void fillTable() {
		
	    	for(int i=0; i<tabledata.getItems().size(); i++){
	    		
	    		tabledata.getItems().clear();
	    	}
	    	wd = new WorkIndicatorDialog(null, "Generating table...");
	    	 wd.addTaskEndNotification(result -> {
	    		  String outres = result.toString();
	    	   // System.out.println("nomaa "+outres);
	    	    if(outres.equals("1")){
	    	    	
				       totamount.setText(""+toAmount);
					   totprofit.setText(""+toProfit);
	    	    } if(outres.equals("0")){
	    	    	TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.WARNING);
				       tray.setTitle("SORRY, REPORT CAN'T BE GENERATED");
				       tray.setMessage("You must choose start and end dates first ..");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(4000));
	    	    }
	    	    wd=null;
	    	 });
	    		 wd.exec("fetch", inputParam -> {
	    	        // Thinks to do...
	    	        // NO ACCESS TO UI ELEMENTS!
	    			 int z=0;
	    		    	try{
	    		    		start = reps.getStart();
	    		    		end = reps.getEnder();
	    		    		System.out.println(start+" DATES: "+end);
	    		    		tabledata.getItems().clear();
	    		    		try{
	    						con= database.dbconnect();
	    						   st= con.createStatement();
	    						   String query = "SELECT saltab.*, prodtab.code,prodtab.name,sal.invoice_no,sal.customer_name FROM sales_order saltab, products prodtab, sales sal"
	    						   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code AND saltab.invoice_no=sal.invoice_no";
	    						   rs=st.executeQuery(query);
	    						   toAmount=0;
	    						   toProfit=0;
	    						//   int EMB=0;
	    						   boolean exite=false;
	    						   while(rs.next()){
	    							
	    							   String inv,date,name,custom,trans;
	    							   int qty,totalprof,totalam;
	    							   inv = rs.getString("invoice_no");
	    							   date = rs.getString("date");
	    							   name = rs.getString("name");
	    							   custom = rs.getString("customer_name");
	    							   trans = rs.getString("transactionID");
	    							   qty = rs.getInt("qty");
	    							   totalprof = rs.getInt("profit");
	    							   totalam = rs.getInt("amount");
	    							   pesa.add((double) totalprof);
	    							   faida.add((double) totalam);
	    							  totamo = totamo+totalam;
	    							   totpro = totpro+totalprof;
	    							   tabledata.getItems().add(new SaleTable(trans,date,name, custom,inv,totalam,totalprof));
	    						//  toAmount+=totamo;
	    						//  toProfit+=totpro;
	    						  su = true;
	    						  exite=true;
	    						   }
	    						//   System.out.println(start+" DATES: "+end +" ITM:"+tabledata.getItems().size()+" itm2:"+EMB);
	    						/*   double pesan=0; double faidan=0;
	    						   for(int i=0; i<tabledata.getItems().size(); i++){
	    							   pesan += amount.getCellData(i);
	    							   faidan += profit.getCellData(i);
	    							  
	    						   }*/
	    						   st.close();
	    						   if(exite){
	    							   st= con.createStatement();
		    						   String sql ="SELECT SUM(amount) AS 'totalAm',SUM(profit) AS 'totalProf' FROM sales_order"
		    						   		+ " WHERE date BETWEEN '"+start+"' AND '"+end+"'";
		    						   rs=st.executeQuery(sql);
		    						   if(rs.next()){
		    							   double amn,profn;
		    							   double amn0 = rs.getDouble("totalAm");
		    							   double profn0 = rs.getDouble("totalProf");
		    							  amn = Math.round(amn0 * 100.00) / 100.00;
		    							  profn = Math.round(profn0 * 100.00) / 100.00;
		    							  toAmount = amn;
			    						   toProfit	= profn; 
		    						   }
	    						   }
	    						   
	    					}catch(SQLException err){
	    						err.printStackTrace();
	    					}
	    		    	}catch(Exception er){
	    		    		z = 0;
	    		    	}
	    			 
	    	            try {
	    	               Thread.sleep(1000);
	    	            }	
	    	            catch (InterruptedException er) {
	    	               er.printStackTrace();
	    	            }
	    	          if(su){
	    	        	  s = 1;
	    	          }
	    	          
	    	        return new Integer(s);
	    	        
	    	        
	    	     });
	    	
	}
	@FXML
	    void goBack(ActionEvent event) throws IOException {
		main.showReportScene();
	    }

	    @FXML
	    void printTable(ActionEvent event) {

	    }
}
