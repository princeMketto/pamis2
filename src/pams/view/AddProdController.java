package pams.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class AddProdController implements Initializable{
	Main main;
	StoreControl pr = new StoreControl();
	   @FXML
	    private JFXTextField batchNum;

	 @FXML
	    private JFXTextField prodcode;

	    @FXML
	    private JFXTextField prodname,prodname1;

	    @FXML
	    private JFXTextField descrp;

	    @FXML
	    private JFXDatePicker arrival;

	    @FXML
	    private JFXDatePicker expiry;

	    @FXML
	    private JFXTextField cost;

	    @FXML
	    private JFXTextField price,priceGen;

	    @FXML
	    private JFXTextField search;


	    @FXML
	    private ChoiceBox supplier;

	    @FXML
	    private JFXTextField qty,minQty,nextQty;
	    private WorkIndicatorDialog wd=null;
	    @FXML
	    private JFXButton saveBtn;
	    ObservableList<String>suplist = FXCollections.observableArrayList();
	    MainDashController todash = new MainDashController();
	    @FXML
	    private JFXButton cancelBtn;
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs,rs1;
		    private Statement st,st1;
		    private PreparedStatement prep,prep1,prep2,prep0;
		    ProdAdminController pro = new ProdAdminController();
		    String code=null,name=null,genname=null,suppli=null,desc=null;
    	    LocalDate arriv = null,exp = null;
    	    double cos = 0,gencos=0,pri=0,prof=0,priGen=0;
    	    int qt=0,minQt=0,nxQt=0;
    	    Date dateArriv = null,dateExp = null;
		    int toBatch = 0;
			@Override
			public void initialize(URL arg0, ResourceBundle arg1) {
				fillSupp();
				
				
			}
	
			@SuppressWarnings("unchecked")
			private void fillSupp() {

				wd = new WorkIndicatorDialog(null, "..");
			   	 wd.addTaskEndNotification(result -> {
			   		  String outres = result.toString();
			   		  if(outres.equals("1")){
			   			supplier.setItems(suplist);
			   		
			   		  }
				  
			   	
			   	    wd=null;
			   	 });
			   		 wd.exec("fetch", inputParam -> {
						int z = 0;
						suplist.clear();
						suplist.add("Choose Supplier Here");
						supplier.setValue("Choose Supplier Here");
						try{
							con= database.dbconnect();
							   st= con.createStatement();
							   String query = "SELECT name FROM suppliers ";
							   rs=st.executeQuery(query);
							   	while(rs.next()){
							   		String nam = rs.getString("name");
							   		suplist.add(nam);
							   		
							  z=1;
							   	}
							   
							   	con.close();
						}catch(Exception err){
							err.printStackTrace();
						}
			   	            try {
			   	               Thread.sleep(1000);
			   	            }	
			   	            catch (InterruptedException er) {
			   	               er.printStackTrace();
			   	            }
			   	       
			   	          
			   	        return new Integer(z);
			   	        
			   	        
			   	     });
				
		  
			
		//	prodQuant();	
			
				
			}
		
			@FXML
			public void handleCloseButtonAction(ActionEvent event) {
			    Stage stage = (Stage) cancelBtn.getScene().getWindow();
			    stage.close();
			}
			   @SuppressWarnings("unchecked")
			@FXML
			   void saveProduct(ActionEvent event) {

					RequiredFieldValidator validator = new RequiredFieldValidator();
				    validator.setMessage("Input Required");
				   
				    prodcode.getValidators().add(validator);
				    prodcode.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
				        if (!newVal)
				        	prodcode.validate();
				    });
				    prodname.getValidators().add(validator);
				    prodname.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
				        if (!newVal)
				        	prodname.validate();
				    });
				    descrp.getValidators().add(validator);
				    descrp.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
				        if (!newVal)
				        	descrp.validate();
				    });
				    cost.getValidators().add(validator);
				    cost.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
				        if (!newVal)
				        	cost.validate();
				    });
				    price.getValidators().add(validator);
				    price.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
				        if (!newVal)
				        	price.validate();
				    });
				    qty.getValidators().add(validator);
		    	    qty.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
		    	        if (!newVal)
		    	        	qty.validate();
		    	    });
				    if(!(prodname.getText().isEmpty() && descrp.getText().isEmpty() && cost.getText().isEmpty() 
				    		&& price.getText().isEmpty() && qty.getText().isEmpty())){
				    	
			    	    try{
			    	    code = prodcode.getText().toUpperCase();
						   name = prodname.getText().toUpperCase();
						   genname = prodname1.getText().toUpperCase();
						   desc = descrp.getText();
						   arriv = arrival.getValue();
						   exp =  expiry.getValue();
						   gencos = Double.parseDouble(cost.getText());
						   pri = Double.parseDouble(price.getText());
						   priGen = Double.parseDouble(priceGen.getText());
						   suppli = (String) supplier.getSelectionModel().getSelectedItem();
						   qt = Integer.parseInt(qty.getText());
						   minQt= Integer.parseInt(minQty.getText());
						   nxQt= Integer.parseInt(nextQty.getText());
						   cos = (gencos/qt);
						   cos = Math.round(cos * 100.00) / 100.00;
						   
						   prof = pri - cos;
						   prof = Math.round(prof * 100.00) / 100.00;
						   try{
							   dateArriv = Date.valueOf(arriv);
							   dateExp = Date.valueOf(exp); 
						   
						   
						   
						    
							   if(dateArriv.equals(null) || dateExp.equals(null) ){
									  TrayNotification tray = new TrayNotification();
										//   Notification notification = NotificationType.SUCCESS;
									       tray.setNotificationType(NotificationType.WARNING);
									       tray.setTitle("Please provide Dates for the product");
									       tray.setMessage("Dear User. Arrival Date and Expire date are important.");
									       tray.setAnimationType(AnimationType.SLIDE);
									       tray.showAndDismiss(Duration.millis(4000));
								  }else{
									  if(!suppli.equals("Choose Supplier Here")){
										  wd = new WorkIndicatorDialog(null, "Registering...");
									      	 wd.addTaskEndNotification(result -> {
									      		  String outres = result.toString();
									      	   if(outres.equals("1")){
									      		 TrayNotification tray = new TrayNotification();
											       tray.setNotificationType(NotificationType.SUCCESS);
											       tray.setTitle("ITEM ADDED SUCCESSFULLY ");
											       tray.setMessage("You can now find this utem in the list..");
											       tray.setAnimationType(AnimationType.SLIDE);
											       tray.showAndDismiss(Duration.millis(4000));
											       try {
														Main.showStoreScene();
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
									      	   }if(outres.equals("0")){
									      		 TrayNotification tray = new TrayNotification();
											       tray.setNotificationType(NotificationType.ERROR);
											       tray.setTitle("FAILURE ADDING ITEM");
											       tray.setMessage("Make sure to complete the form .");
											       tray.setAnimationType(AnimationType.SLIDE);
											       tray.showAndDismiss(Duration.millis(4000));
									      	   }if(outres.equals("-1")){
									      		 TrayNotification tray = new TrayNotification();
											       tray.setNotificationType(NotificationType.ERROR);
											       tray.setTitle("ERROR ADDING ITEM");
											       tray.setMessage("Make sure to complete the form.");
											       tray.setAnimationType(AnimationType.SLIDE);
											       tray.showAndDismiss(Duration.millis(4000));
										      	   }
									   	  
									      	
									      	    wd=null;
									      	 });
									      		 wd.exec("fetch", inputParam -> {
									   			int z = 0;
												  GregorianCalendar date = new GregorianCalendar();
													int mill = date.get(Calendar.MILLISECOND);
													int sec = date.get(Calendar.SECOND);
													int mins = date.get(Calendar.MINUTE);
													int hour = date.get(Calendar.HOUR); 
												    int     day = date.get(Calendar.DAY_OF_MONTH);
												    int  month = date.get(Calendar.MONTH)+1;
												    int  year = date.get(Calendar.YEAR);
												    String invoicID =""+year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
												    String todate =  day+"/"+month+"/"+year;
										    	try{
										    		 con= database.dbconnect();
										 	
										    		 prep = (PreparedStatement) con.prepareStatement("INSERT INTO products(code,name,generic_name,description,arrival,expiry,gen_cost,cost"
														  		+ ",price,priceGen,wholeProfit,profit,supplier,qty,qty_remain,minQty,nextQty,total,comment) VALUES("
														  		+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

														  
												    	  prep.setString(1, code);
												    	  prep.setString(2, name);
												    	  prep.setString(3, genname);
												    	  prep.setString(4, desc);
												    	  prep.setDate(5, dateArriv);
												    	  prep.setDate(6, dateExp);
												    	  prep.setDouble(7, gencos);
												    	  prep.setDouble(8, cos);
												    	  prep.setDouble(9, pri);
												    	  prep.setDouble(10, priGen);
												    	  prep.setDouble(11, (priGen-gencos));
												    	  prep.setDouble(12, prof);
												    	  prep.setString(13, suppli);
												    	  prep.setInt(14, qt); 
												    	  prep.setInt(15, qt); 
												    	  prep.setInt(16, minQt);
												    	  prep.setInt(17, nxQt); 
														prep.setDouble(18, (pri*qt));
														prep.setString(19, "ACTIVE");
							
										    		 int out=prep.executeUpdate();
														
														if(out >0){
															z=1;
														       /*
																 * insert into other table i.e purchased_items & purchases
																 */
															 prep0 = (PreparedStatement) con.prepareStatement("INSERT INTO purchased_items(product_code,qty,arrival,expiry,gen_cost,cost,price,priceGen,supplier,invoice) VALUES("
														    	  		+ "?,?,?,?,?,?,?,?,?,?)");
																
															    	  prep0.setString(1, code);
															    	  prep0.setInt(2, Integer.parseInt(qty.getText()));
															    	  prep0.setDate(3, dateArriv);
															    	  prep0.setDate(4, dateExp);
															    	  prep0.setDouble(5, gencos);
															    	  prep0.setDouble(6, cos);
															    	  prep0.setDouble(7, pri);
															    	  prep0.setDouble(8, priGen);
															    	  prep0.setString(9, suppli);
															    	  prep0.setString(10, invoicID);
															    	  prep0.executeUpdate();
															    	  prep0.close();
															
															/*
															 *    on sucess update insert values to other  new two tables. 
															 */
															    prep1 = (PreparedStatement) con.prepareStatement("INSERT INTO purchases(invoice_no,date,supplier,cashier) VALUES("
														    	  		+ "?,?,?,?)");
															  prep1.setString(1,invoicID);
													    	  prep1.setDate(2,dateArriv);
													    	  prep1.setString(3, suppli);
													    	  prep1.setString(4, todash.getMe());
													    	 
													    	  prep1.executeUpdate();
													    	  prep1.close(); 
													    
														}else{
															z=0;
														}
														
														
														
										    	}catch(SQLException err){
										    		err.printStackTrace();
										    		//System.out.println(err);
										    		z=-1;
										    	}
									      	            try {
									      	               Thread.sleep(1000);
									      	            }	
									      	            catch (InterruptedException er) {
									      	               er.printStackTrace();
									      	            }
									      	       
									      	          
									      	        return new Integer(z);
									      	        
									      	        
									      	     });
										  
							
									  }else{
										  TrayNotification tray = new TrayNotification();
									       tray.setNotificationType(NotificationType.ERROR);
									       tray.setTitle("FAILURE ADDING ITEM");
									       tray.setMessage("choose Supplier and try again.");
									       tray.setAnimationType(AnimationType.SLIDE);
									       tray.showAndDismiss(Duration.millis(4000));
									  }
								  }
							   //inserting into new two tables.
							   
						   }catch(Exception ex){
							   TrayNotification tray = new TrayNotification();
								//   Notification notification = NotificationType.SUCCESS;
							       tray.setNotificationType(NotificationType.WARNING);
							       tray.setTitle("Please provide Dates for the item");
							       tray.setMessage("Dear User. Arrival Date and Expire date are important.");
							       tray.setAnimationType(AnimationType.SLIDE);
							       tray.showAndDismiss(Duration.millis(4000)); 
						   }
			    	    }catch(Exception er){
			    	    	TrayNotification tray = new TrayNotification();
							//   Notification notification = NotificationType.SUCCESS;
						       tray.setNotificationType(NotificationType.WARNING);
						       tray.setTitle("FILL THE FORM COMPLETELY/CORRECTLY");
						       tray.setMessage("i.e Supplier, QTY in numbers etc.");
						       tray.setAnimationType(AnimationType.SLIDE);
						       tray.showAndDismiss(Duration.millis(4000));
			    	    }
				    	
				    }else{
				    	TrayNotification tray = new TrayNotification();
						//   Notification notification = NotificationType.SUCCESS;
					       tray.setNotificationType(NotificationType.ERROR);
					       tray.setTitle("FAILURE UPDATING SUPPLIER");
					       tray.setMessage("Please Fill the form completely/correctly");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(4000));
				    }

			   }
}
