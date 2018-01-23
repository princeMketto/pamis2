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
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class SupplierController implements Initializable {
	Main main;
	LoginController lg = new LoginController();
	  @FXML
	    private JFXButton backbtn;

	    @FXML
	    private JFXTextField searchUser,search;

	    @FXML
	    private JFXButton addSupplier;

	    @FXML
	     TableView<Supplier> tableuser;
	    private WorkIndicatorDialog wd=null;
	    @FXML
	    private TableColumn<Supplier, String> userId;

	    @FXML
	    private TableColumn<Supplier, String> name;

	    @FXML
	    private TableColumn<Supplier, String> address,email;

	    @FXML
	    private TableColumn<Supplier, String> contact;

	   
	    @FXML
	    private JFXButton refresh;
	    @FXML
	    private JFXButton updatebtn;

	    @FXML
	    private JFXButton removbtn;
	    @FXML
	    private StackPane pane;

	    @FXML
	    private TableColumn<Supplier, String> note;
	    ObservableList<Supplier>list = FXCollections.observableArrayList();
	    ObservableList<Supplier>filteredlist = FXCollections.observableArrayList();
	    ObservableList<Supplier> searchdata;
	    ObservableList<Supplier> datlist;
		 List listofdat = new ArrayList();
	    ConnectDB database = new ConnectDB();
	    private Connection con;
	    private ResultSet rs;
	    private Statement st;
	    private PreparedStatement prep;
	 static   String supID=null;
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1) {
	    
	    	
	    	fetchSupplier();
	    	userId.setCellValueFactory(new PropertyValueFactory<>("Sid"));
	    	name.setCellValueFactory(new PropertyValueFactory<>("Sname"));
	    	address.setCellValueFactory(new PropertyValueFactory<>("Saddress"));
	    	email.setCellValueFactory(new PropertyValueFactory<>("Semail"));
	    	contact.setCellValueFactory(new PropertyValueFactory<>("Scontact"));
			note.setCellValueFactory(new PropertyValueFactory<>("Snote"));
			tableuser.setOnMouseClicked(new EventHandler<MouseEvent>(){

				@Override
				public void handle(MouseEvent arg0) {
					supID = tableuser.getSelectionModel().getSelectedItem().getSid();
				//	System.out.print(supID);
				}
				
			});
			search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
				
				if (oldValue != null && (newValue.length() < oldValue.length())) {
					tableuser.setItems(searchdata);
			            }
			            String value = newValue.toLowerCase();
			            ObservableList<Supplier> subentries = FXCollections.observableArrayList();

			            long count = tableuser.getColumns().stream().count();
			            for (int i = 0; i < tableuser.getItems().size(); i++) {
			                for (int j = 0; j < count; j++) {
			                    String entry = "" + tableuser.getColumns().get(j).getCellData(i);
			                    if (entry.toLowerCase().contains(value)) {
			                    	
			                        subentries.add(tableuser.getItems().get(i));
			                        break;
			                    }
			                }
			            }
			            tableuser.setItems(subentries);
			        });
			
		}
	    static String getSupID(){
	    	return supID;
	    }
	    @FXML
	    void removeSuppl(ActionEvent event) {
	    	String supId=null;
	    	try{
    			supId = supID;
    			if(supId.isEmpty() || supId == null){
    				TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.WARNING);
				       tray.setTitle("PLEASE SELECT SUPPLIER FIRST");
				       tray.setMessage("select supplier on the supplier table above");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(4000));
    			
    			
    			}else{
    				/*
    				 * 
    				 */	JFXDialogLayout content = new JFXDialogLayout();
    			    	content.setHeading(new Text("Confirmation"));
    			    	content.setBody(new Text("This action will remove selected\n"
    			    			+ "supplier in the list of suppliers\n"
    			    			+ "Press COMMIT to commit this action\n"
    			    			+ "OR CANCEL to quit\n"));
    			    	
    			    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.CENTER);
    			    	JFXButton bt = new JFXButton("COMMIT");
    			    	JFXButton bt1 = new JFXButton("CANCEL");
    			    	bt.setOnAction(new EventHandler<ActionEvent>(){
    			    		String supId=null;
    						@Override
    						public void handle(ActionEvent arg0) {
    						/*
    						 * 
    						 */

    				    	/*	try{
    				    			supId = supID;
    				    			if(supId.isEmpty() || supId == null){
    				    				TrayNotification tray = new TrayNotification();
    								       tray.setNotificationType(NotificationType.WARNING);
    								       tray.setTitle("PLEASE SELECT SUPPLIER FIRST");
    								       tray.setMessage("select supplier on the supplier table above");
    								       tray.setAnimationType(AnimationType.SLIDE);
    								       tray.showAndDismiss(Duration.millis(4000));
    				    			}
    				    		}catch(NullPointerException nul){
    				    			TrayNotification tray = new TrayNotification();
    							       tray.setNotificationType(NotificationType.WARNING);
    							       tray.setTitle("PLEASE SELECT SUPPLIER FIRST");
    							       tray.setMessage("select supplier on the supplier table above");
    							       tray.setAnimationType(AnimationType.SLIDE);
    							       tray.showAndDismiss(Duration.millis(4000));
    				    		}*/
    				    	try{
    				    		supId = supID;
    							con= database.dbconnect();
    							prep = (PreparedStatement) con.prepareStatement("UPDATE suppliers SET comment = ? WHERE id = ?");
    							System.out.println("ID  "+supId);
    							prep.setString(1,"INACTIVE");
   							 prep.setString(2,supId);
   						int out =	prep.executeUpdate();
   								prep.close();
    						
    							if(out > 0){	
    								TrayNotification tray = new TrayNotification();
 							       tray.setNotificationType(NotificationType.SUCCESS);
 							       tray.setTitle("SUPPLIER REMOVED SUCCESSFULLY");
 							       tray.setMessage("Removed supplier wont be available in the list");
 							       tray.setAnimationType(AnimationType.SLIDE);
 							       tray.showAndDismiss(Duration.millis(4000));
    							}else{
    								TrayNotification tray = new TrayNotification();
 							       tray.setNotificationType(NotificationType.WARNING);
 							       tray.setTitle("SUPPLIER COULD HAVE NOT BEEN REMOVED");
 							       tray.setMessage("something went wrong. please retry");
 							       tray.setAnimationType(AnimationType.SLIDE);
 							       tray.showAndDismiss(Duration.millis(4000));
    							}
    							
    						}catch(SQLException err){
    							err.printStackTrace();
    						}
    							/*
    							 * 
    							 */
    				    	fetchSupplier();
    				    	dialog.close();
    				    	
    				    	
    						}
    			    		
    			    	});
    			    	bt1.setOnAction(new EventHandler<ActionEvent>(){

    						@Override
    						public void handle(ActionEvent arg0) {
    							dialog.close();
    						}
    			    		
    			    	});
    			    	content.setActions(bt,bt1);
    			    	dialog.show();
    			    	fetchSupplier();
    				/*
    				 * 
    				 */
    			}
    		}catch(NullPointerException nul){
    			TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("PLEASE SELECT SUPPLIER FIRST");
			       tray.setMessage("select supplier on the supplier table above");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
    		}
	    
	    	
	    }
	    @SuppressWarnings("unchecked")
		@FXML
	     void fetchSupplier() {
	    	for(int i=0; i<tableuser.getItems().size(); i++){
	    		
	    		tableuser.getItems().clear();
	    	}
	    	wd = new WorkIndicatorDialog(null, "Loading Supplier(S)...");
		 	   wd.addTaskEndNotification(result -> {
		 		  String outres = result.toString();
		          // System.out.println("nomaa "+outres);
		           if(outres.equals("1")){
		        	   datlist = FXCollections.observableList(listofdat);
		        	   tableuser.setItems(datlist);
		        	   searchdata =  tableuser.getItems();
		           }
		           wd=null;
		 	   });
		 		 wd.exec("fetch", inputParam -> {
			           // Thinks to do...
			           // NO ACCESS TO UI ELEMENTS!
		 			try{
						con= database.dbconnect();
						   st= con.createStatement();
						   String query = "SELECT * FROM suppliers WHERE NOT comment='INACTIVE'";
						   rs=st.executeQuery(query);
						   listofdat.clear();
						   while(rs.next()){
							   String nam,id,addr,cont,not,mail;
							 //  int cos,pric,prof,qt,qtyslef,total;
							   id = rs.getString("id");
							   nam = rs.getString("name");
							   addr = rs.getString("address");
							   mail = rs.getString("email");
							   cont = rs.getString("contact");
							   not =  rs.getString("note");
							  
							   listofdat.add(new Supplier(id, nam, addr,mail, cont, not,"Edit"));
						   }
					}catch(SQLException err){
						err.printStackTrace();
					}
			       
			               try {
			                  Thread.sleep(1000);
			               }	
			               catch (InterruptedException er) {
			                  er.printStackTrace();
			               }
			             
			           return new Integer(1);
			           
			           
			        });
	    
		}

		@FXML
	    void addSupplier(ActionEvent event) throws IOException {
			AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("AddSupplier.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text("Add vendor(supplier)"));
		    	content.setBody(infopane);
		    	content.setStyle("-fx-background-color:  #2b89e0");
		    	//content.
		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
		    	dialog.setMaxHeight(555);
		    	dialog.setMaxWidth(220);
		    	dialog.setPrefWidth(220);
		    	dialog.setPrefHeight(555);
		    	JFXButton bt = new JFXButton("Done");
		    	bt.setStyle("-fx-background-color:  #ffffff");
		    	
		    	bt.setOnAction(new EventHandler<ActionEvent>(){
		    		String prodName=null;
					@Override
					public void handle(ActionEvent arg0) {
					/*
					 * 
					 */
						
						fetchSupplier();
						/*
						 * 
						 */
			    
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		    
		    	content.setActions(bt);
		    	dialog.show();
		}

	    @SuppressWarnings("static-access")
		@FXML
	    void goBack(ActionEvent event) throws IOException {
	     	if(lg.getStats().equals("Admin")){
	    		main.showMainDash();
	    	}
	    	if(lg.getStats().equals("cashier")){
	    		main.showCashierDash();
	    	}
	    	if(lg.getStats().equals("storekeeper")){
	    		main.showStoreDash();
	    	}
	    }
	    @FXML
	    void updateSupp(ActionEvent event) throws IOException {
	    	String ToId=null;
	    	try{
	    		ToId = tableuser.getSelectionModel().getSelectedItem().getSid();
	    		if(!ToId.isEmpty() && !ToId.equals(null)){	AnchorPane infopane = null;
    	    	try{
    	    		FXMLLoader loader = new FXMLLoader();
    				loader.setLocation(getClass().getResource("UpdateSupp.fxml"));
    				 infopane = loader.load();
    	    	}catch(Exception e){ 
    	    		e.printStackTrace();
    	    	}

    			/*
    			 * 
    			 */	JFXDialogLayout content = new JFXDialogLayout();
    		    	content.setHeading(new Text("Edit information"));
    		    	content.setBody(infopane);
    		    	content.setStyle("-fx-background-color:  #2b89e0");
    		    	//content.
    		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
    		    	dialog.setMaxHeight(555);
    		    	dialog.setMaxWidth(220);
    		    	dialog.setPrefWidth(220);
    		    	dialog.setPrefHeight(555);
    		    	JFXButton bt = new JFXButton("Done");
    		    	bt.setStyle("-fx-background-color:  #ffffff");
    		    	bt.setOnAction(new EventHandler<ActionEvent>(){
    		    		String prodName=null;
    					@Override
    					public void handle(ActionEvent arg0) {
    					/*
    					 * 
    					 */
    						
    						fetchSupplier();
    						/*
    						 * 
    						 */
    			    
    			    	dialog.close();
    			    	
    			    	
    					}
    		    		
    		    	});
    		    
    		    	content.setActions(bt);
    		    	dialog.show();}else{
	    			TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.WARNING);
				       tray.setTitle("PLEASE SELECT SUPPLIER FIRST");
				       tray.setMessage("select supplier on the supplier table above");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(4000));
	    		}
	    	}catch(Exception er){
	    		TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("PLEASE SELECT SUPPLIER FIRST");
			       tray.setMessage("select supplier on the supplier table above");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
	    	}
	    	
	    	
	    }
		
}
