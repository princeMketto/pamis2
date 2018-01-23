package pams.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ReceiptController implements Initializable{
	Main main;
	SaleController sale = new SaleController();
    @FXML
    private Label orderNo;
    @FXML
    private TableView<Receipt> tableReceipt;
    @FXML
    private Label date;
    @FXML
    private Label customerName;

    @FXML
    private TableColumn<Receipt, String> recProdnameCol;

    @FXML
    private TableColumn<Receipt, Integer> recQtyCol;

    @FXML
    private TableColumn<Receipt, Integer> recPricCol;

    @FXML
    private TableColumn<Receipt, Integer> recDiscountCol;

    @FXML
    private TableColumn<Receipt, Integer> recAmountCol;

    @FXML
    private Label totalAmount;

    @FXML
    private Label cashTendered;

    @FXML
    private Label change;
    @FXML
    private Label printlabel;

    @FXML
    private JFXButton btnPrint;
    @FXML
    private JFXButton btnBackSale;
    @FXML
    private WebView webcontent;
    WebEngine webEngine;
    ConnectDB database = new ConnectDB();
	 private Connection con;
	    private ResultSet rs;
	    private Statement st;
	    private PreparedStatement prep,prep1;
	    MainDashController todash = new MainDashController();
	    String today,invoicId;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnPrint.setDisable(false);
		
		
		webEngine = webcontent.getEngine();
		String arrName ="";
		String arrNameo = null;
		String arrNames = null;
		for(int i=0; i<sale.getProdArray().size(); i++){
			 arrName = arrName+","+sale.getProdArray().get(i);	
		}
		arrNameo = arrName.substring(1);
		try{
		 final URL urlHtml = getClass().getResource("htmlReceipt.html");
		 webEngine.load(urlHtml.toExternalForm());
		 webEngine.getLoadWorker().stateProperty().addListener(
				    new ChangeListener<State>() {
				      
						@Override
						public void changed(ObservableValue<? extends State> arg0, State arg1, State arg2) {
							// TODO Auto-generated method stub
							 if (arg2 == State.SUCCEEDED) {
								 GregorianCalendar date = new GregorianCalendar();
									int mill = date.get(Calendar.MILLISECOND);
									int sec = date.get(Calendar.SECOND);
									int mins = date.get(Calendar.MINUTE);
									int hour = date.get(Calendar.HOUR); 
								    int     day = date.get(Calendar.DAY_OF_MONTH);
								    int  month = date.get(Calendar.MONTH)+1;
								    int  year = date.get(Calendar.YEAR);
								    String receipt ="TS-"+year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
								    invoicId = "INV-"+year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
								     today = year+"/"+month+"/"+day;
									//	System.out.println("OUT: "+nvoice);
								 
								 String proda = sale.getProdArray().toString().substring(sale.getProdArray().toString().indexOf("[")+1, sale.getProdArray().toString().indexOf("]"));
								 String proqt = sale.getQtyArray().toString().substring(sale.getQtyArray().toString().indexOf("[")+1, sale.getQtyArray().toString().indexOf("]"));
								 String propri = sale.getPriceArray().toString().substring(sale.getPriceArray().toString().indexOf("[")+1, sale.getPriceArray().toString().indexOf("]"));
								 String proamo = sale.getAmountArray().toString().substring(sale.getAmountArray().toString().indexOf("[")+1, sale.getAmountArray().toString().indexOf("]"));
								 String pharmName="MY PHARMACY NAME";
								 String pharmCont="P.O. BOX 1234, IRINGA-TZ";
								 String recNo=receipt;
								 String cusName=sale.getCustName();
								 double tota = sale.getTotal();
								 double tender = sale.getCash();
								 double chang = (sale.getCash() -sale.getTotal());
								 webEngine.executeScript(" myFunction('"+ proda +"','"+ proqt +"','"+ propri +"','"+ proamo +"',"
								 		+ "'"+ tota +"','"+ tender +"','"+ chang +"','"+ cusName +"','"+ recNo +"','"+today+"','"+ pharmName +"','"+ pharmCont +"')");

					            }
						}
				    });
		 webcontent.autosize();
		 		}catch(Exception er){
			er.printStackTrace();
		}
		ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(webcontent);
		
		
	//	arrNames = "["+arrNameo+"]".toString();
	//	System.out.println(sale.getProdArray());
		
 

		/*for(int i=0; i< sale.getProdArray().size(); i++){
			tableReceipt.getItems().add(new Receipt(sale.getProdArray().get(i),sale.getQtyArray().get(i),
					sale.getPriceArray().get(i),0,sale.getAmountArray().get(i)));
		}
		
		
		recProdnameCol.setCellValueFactory(new PropertyValueFactory<>("prodname"));
		recQtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
		recPricCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		recDiscountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
		recAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));*/
		/*
		customerName.setText("CUSTOMER NAME:"+sale.getCustName());
		totalAmount.setText(""+sale.getTotal()+"/-");
		cashTendered.setText(""+sale.getCash()+"/-");
		change.setText(""+(sale.getCash() -sale.getTotal())+"/-");*/
		
//	System.out.println(sale.getAmountArray());

	/*for(int i=0; i<sale.getAmountArray().size(); i++){
		System.out.println(" CLEAN "+sale.getAmountArray().get(i));
	}*/
	
	}
	   @FXML
	    void printReceipt(ActionEvent event) {
		   Stage stage = new Stage();
		   
		   printSetup(webcontent, stage);
	    }

	  private void printSetup(Node node, Stage owner) {
		// Create the PrinterJob       
		  	        PrinterJob job = PrinterJob.createPrinterJob();
		  	      if (job == null) {
		  	    	 return;
		  	      }
		  	 // Show the print setup dialog
		  	    boolean proceed = job.showPrintDialog(owner); //showPageSetupDialog OR showPrintDialog
		  	  if (proceed) {
		  		  print(job, node);
		  	  }
	}
	private void print(PrinterJob job, Node node) {
		printlabel.textProperty().bind(job.jobStatusProperty().asString());
		
		//prinring node
		 boolean printed = job.printPage(node);
		 if (printed) {
			 btnPrint.setDisable(true);
			 job.endJob();
			 commitTrans();
		 }
		
	}
	private void commitTrans() {
		boolean success = false;
		boolean tosale = false;
		for(int i=0; i< sale.getProdArray().size(); i++){
			/*tableReceipt.getItems().add(new Receipt(sale.getProdArray().get(i),sale.getQtyArray().get(i),
					sale.getPriceArray().get(i),0,sale.getAmountArray().get(i)));*/
		String myCode = null;
		int myCost = 0;
			try{
			con = database.dbconnect();
			 st= con.createStatement();
			   String query = "SELECT code,cost FROM products WHERE name='"+sale.getProdArray().get(i)+"'";
			   rs=st.executeQuery(query);
			   	if(rs.next()){
			   		myCode = rs.getString("code");
			   		myCost = rs.getInt("cost");
			   	}
			  
			   	st.close();
			   	//**
			    prep = (PreparedStatement) con.prepareStatement("INSERT INTO sales_order(invoice_no,product_code,qty,price,amount,profit,date) VALUES("
		    	  		+ "?,?,?,?,?,?,?)");
			    prep.setString(1, invoicId);
		    	  prep.setString(2, myCode);
		    	  prep.setInt(3, sale.getQtyArray().get(i));
		    	  prep.setDouble(4, sale.getPriceArray().get(i));
		    	  prep.setDouble(5, sale.getAmountArray().get(i));
		    	  prep.setDouble(6, (sale.getPriceArray().get(i) - myCost)*sale.getQtyArray().get(i));
		    	  prep.setString(7, today);
		    	  int out=prep.executeUpdate();
					
					if(out >0){
					//success;
						success = true;
						 
						// close statement
						prep.close();
					}else{
						TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.ERROR);
					       tray.setTitle("Failure Performing transaction");
					       tray.setMessage("Make sure-.\n Server is online. \n OR contact system Admin");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(4000));
					}
					prep1 = (PreparedStatement) con.prepareStatement("INSERT INTO sales(invoice_no,cashier,date,type,amount,customer_name) VALUES("
			    	  		+ "?,?,?,?,?,?)");
					 prep1.setString(1,invoicId);
			    	  prep1.setString(2, todash.getMe());
			    	  prep1.setString(3, today);
			    	  prep1.setString(4, "CASH");
			    	  prep1.setDouble(5,sale.getAmountArray().get(i) );
			    	  prep1.setString(6, sale.getCustName());
			    	  int out2 = prep1.executeUpdate();
			    	  if(out2 > 0){
			    		// System.out.println("TO SALE DONE");
			    	  }else{
			    		//  System.out.println("FAIL  TO SALE "); 
			    	  }
					prep1.close();
					if(success){
						TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.SUCCESS);
					       tray.setTitle("Transaction committed with success");
					       tray.setMessage("you cant undo this transaction");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(4000));
					}
			   	
			   	//open new ststement for updating task
			    st= con.createStatement();
			    String sql = "UPDATE products SET qty_remain= qty_remain-"+sale.getQtyArray().get(i)+" WHERE code='"+myCode+"'";
				   st.executeUpdate(sql);
				   st.close();
				   	con.close();
		}catch(SQLException sq){
			sq.printStackTrace();
		}
		}
		
	}
	@FXML
	    void goBack(ActionEvent event) throws IOException {
	    	main.showSaleScene();;
	   
	    }
}
