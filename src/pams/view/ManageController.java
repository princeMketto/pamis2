package pams.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ManageController implements Initializable {
	 @FXML
	    private JFXButton btnBack,btnedit,btnReg,btnsaveCat,btnReq,btn0,btn00,btnRemov,btnProf,btnSale;
	 @FXML
	    private StackPane stackbase;
	    @FXML
	    private Label name;

	    @FXML
	    private Label address;

	    @FXML
	    private Label mail;

	    @FXML
	    private Label web,tin;

	    @FXML
	    private Label phone;

	    @FXML
	    private ImageView logo;
	    private WorkIndicatorDialog wd=null;
	    String nam,addr,email,webs,phon,tins;
	    ConnectDB database = new ConnectDB();
	    private Connection con;
	    private ResultSet rs;
	    private Statement st,st1,st2;
	    private PreparedStatement prep;
	    String useID=null;
	    FileInputStream input;
		InputStream is = null;
		Image image=null;
		FileChooser flc;
	  File selectedFile ;
	 Main main;
		LoginController lg = new LoginController();
		 @FXML
		    private TableView<AddUser> tableuser;

		    @FXML
		    private TableColumn<AddUser, String> userId;

		    @FXML
		    private TableColumn<AddUser, String> fname;

		    @FXML
		    private TableColumn<AddUser, String> lname;

		    @FXML
		    private TableColumn<AddUser, String> uname;

		    @FXML
		    private TableColumn<AddUser, String> sex;

		    @FXML
		    private TableColumn<AddUser, String> phone1;
		    @FXML
		    private TableColumn<AddUser, String> sectn;
		    static String usID;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		fetchData();
		user();
		
	}
	private void user(){
		userId.setCellValueFactory(new PropertyValueFactory<>("id"));
		fname.setCellValueFactory(new PropertyValueFactory<>("fname"));
		lname.setCellValueFactory(new PropertyValueFactory<>("lname"));
		uname.setCellValueFactory(new PropertyValueFactory<>("uname"));
		sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
		phone1.setCellValueFactory(new PropertyValueFactory<>("phone"));
		sectn.setCellValueFactory(new PropertyValueFactory<>("sectn"));
		tableuser.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				usID = tableuser.getSelectionModel().getSelectedItem().getId();
			//	System.out.print(supID);
			}
			
		});
		
	}
	static String getSupID(){
    	return usID;
    }
	@SuppressWarnings("unchecked")
	public void fetchUser() {
		for(int i=0; i<tableuser.getItems().size(); i++){
    		
    		tableuser.getItems().clear();
    	}
		wd = new WorkIndicatorDialog(null, "Loading User(S)...");
	 	   wd.addTaskEndNotification(result -> {
	 		  String outres = result.toString();
	          // System.out.println("nomaa "+outres);
	           if(outres.equals("1")){
	        	   
	           }
	           wd=null;
	 	   });
	 		 wd.exec("fetch", inputParam -> {
		           // Thinks to do...
		           // NO ACCESS TO UI ELEMENTS!
	 			try{
	 				con= database.dbconnect();
	 				   st= con.createStatement();
	 				   String query = "SELECT * FROM user WHERE NOT comment='INACTIVE'";
	 				   rs=st.executeQuery(query);
	 				   while(rs.next()){
	 					   String fnam,lnam,id,phon,stat,sex,unam;
	 					 //  int cos,pric,prof,qt,qtyslef,total;
	 					   id = rs.getString("Id");
	 					   fnam = rs.getString("fname");
	 					   lnam = rs.getString("lname");
	 					   unam = rs.getString("username");
	 					   sex = rs.getString("sex");
	 					   phon = rs.getString("phone");
	 					   stat =  rs.getString("status");
	 					  
	 					   tableuser.getItems().add(new AddUser(id, fnam, lnam, unam, sex,phon,stat));
	 					   
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
    void removUser(ActionEvent event) {
    
    	try{
    		useID = usID;
    		if(useID.isEmpty() || uname == null){
				TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("PLEASE SELECT USER FIRST");
			       tray.setMessage("select user on the user table above");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
			
			
			}else{
				/*
				 * 
				 */	JFXDialogLayout content = new JFXDialogLayout();
			    	content.setHeading(new Text("Confirmation"));
			    	content.setBody(new Text("This action will remove selected\n"
			    			+ "User in the list of user(s)\n"
			    			+ "Press COMMIT to commit this action\n"
			    			+ "OR CANCEL to quit\n"));
			    	
			    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.CENTER);
			    	JFXButton bt = new JFXButton("COMMIT");
			    	JFXButton bt1 = new JFXButton("CANCEL");
			    	bt.setOnAction(new EventHandler<ActionEvent>(){
			    		String useram=null;
						@SuppressWarnings("unchecked")
						@Override
						public void handle(ActionEvent arg0) {
						/*
						 * 
						 */
							wd = new WorkIndicatorDialog(null, "Refreshing data...");
						   	 wd.addTaskEndNotification(result -> {
						   		  String outres = result.toString();
						   		  if(outres.equals("1")){
						   			fetchUser();
						   			TrayNotification tray = new TrayNotification();
								       tray.setNotificationType(NotificationType.SUCCESS);
								       tray.setTitle("USER REMOVED SUCCESSFULLY");
								       tray.setMessage("Removed user wont be available in the list");
								       tray.setAnimationType(AnimationType.SLIDE);
								       tray.showAndDismiss(Duration.millis(4000));
						   		  }if(outres.equals("0")){
						   			TrayNotification tray = new TrayNotification();
								       tray.setNotificationType(NotificationType.WARNING);
								       tray.setTitle("USER COULD HAVE NOT BEEN REMOVED");
								       tray.setMessage("something went wrong. please retry");
								       tray.setAnimationType(AnimationType.SLIDE);
								       tray.showAndDismiss(Duration.millis(4000));
						   		  }
							  
						   	
						   	    wd=null;
						   	 });
						   		 wd.exec("fetch", inputParam -> {
									int z = 0;
									try{
								    	//	useram = uname;
											con= database.dbconnect();
											prep = (PreparedStatement) con.prepareStatement("UPDATE user SET comment = ? WHERE Id = ?");
										//	System.out.println("ID  "+supId);
											prep.setString(1,"INACTIVE");
											 prep.setString(2,useID);
										int out =	prep.executeUpdate();
												prep.close();
										
											if(out > 0){	
												z=1;
											}else{
												z=0;
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
						   	       
						   	          
						   	        return new Integer(z);
						   	        
						   	        
						   	     });
							
				  
				    
							/*
							 * 
							 */
				    	
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
			    	fetchUser();
				/*
				 * 
				 */
			}
    	}catch(NullPointerException nul){
    		TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.WARNING);
		       tray.setTitle("PLEASE SELECT PRODUCT FIRST");
		       tray.setMessage("select product on the product table above");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
    	}
    }
	   @SuppressWarnings("unchecked")
		private void fetchData() {
	    	
	    	wd = new WorkIndicatorDialog(null, "Loading Data...");
		 	   wd.addTaskEndNotification(result -> {
		 		  String outres = result.toString();
		          // System.out.println("nomaa "+outres);
		           if(outres.equals("1")){
		        	   name.setText(nam);
		 				 address.setText(addr);
		 				web.setText(webs);
		 				tin.setText(tins);
		 				mail.setText(email);
		 			phone.setText(phon);
		 			fetchUser();
		           }
		           wd=null;
		 	   });
		 		 wd.exec("fetch", inputParam -> {
			           // Thinks to do...
			           // NO ACCESS TO UI ELEMENTS!
		 			
		 		  	try{
		 				
		 				con= database.dbconnect();
		 				   st= con.createStatement();
		 				   String query = "SELECT * FROM business_details ";
		 				   rs=st.executeQuery(query);
		 				  
		 				   if(rs.next()){
		 					  
		 					  nam = rs.getString("name");
		 					 addr = rs.getString("address");
		 					email = rs.getString("email");
		 					webs = rs.getString("website");
		 					phon = rs.getString("phone");
		 					tins = rs.getString("tin");
		 					 Blob aBlob = rs.getBlob("logo");
		 					   is =  aBlob.getBinaryStream(1, aBlob.length());
		 					  image= new Image(is);
								logo.setImage(image);
		 				   }
		 				 
		 				   rs.close();
		 				   st.close();
		 				   con.close();
		 				   
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
	    void goProf(ActionEvent event) {
	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("Profit.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text(""));
		    	content.setBody(infopane);
		    	content.setStyle("-fx-background-color: #2b89e0");
		    	
		    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("Done");
		    	JFXButton bt1 = new JFXButton("Cancel");
		    	bt.setOnAction(new EventHandler<ActionEvent>(){
		    		String prodName=null;
					@Override
					public void handle(ActionEvent arg0) {
			
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

	    
	    
	    }
	    @FXML
	    void goSale(ActionEvent event) {
	    	 try {
		 			main.showReportScene();
		 		} catch (IOException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 		}
	    }
    @FXML
    void showReg(ActionEvent event) {

    	AnchorPane infopane = null;
    	try{
    		FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("AddUser.fxml"));
			 infopane = loader.load();
    	}catch(Exception e){ 
    		e.printStackTrace();
    	}

		/*
		 * 
		 */	JFXDialogLayout content = new JFXDialogLayout();
	    	content.setHeading(new Text(""));
	    	content.setBody(infopane);
	    	content.setStyle("-fx-background-color: #2b89e0");
	    	
	    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
	    	JFXButton bt = new JFXButton("Done");
	    	JFXButton bt1 = new JFXButton("Cancel");
	    	bt.setOnAction(new EventHandler<ActionEvent>(){
	    		String prodName=null;
				@Override
				public void handle(ActionEvent arg0) {
		
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

    
    }
    @FXML
    void ConfirmCat(ActionEvent event) {

    }
    @FXML
    void showRequest(ActionEvent event) {

    	AnchorPane infopane = null;
    	try{
    		FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("inner/ManageRequest.fxml"));
			 infopane = loader.load();
    	}catch(Exception e){ 
    		e.printStackTrace();
    	}

		/*
		 * 
		 */	JFXDialogLayout content = new JFXDialogLayout();
	    	content.setHeading(new Text(""));
	    	content.setBody(infopane);
	    	content.setStyle("-fx-background-color: #2b89e0");
	    	
	    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
	    	JFXButton bt = new JFXButton("Done");
	    	JFXButton bt1 = new JFXButton("Cancel");
	    	bt.setOnAction(new EventHandler<ActionEvent>(){
	    		String prodName=null;
				@Override
				public void handle(ActionEvent arg0) {
		
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

    
    }
    @FXML
    void showEdit(ActionEvent event) {

    	AnchorPane infopane = null;
    	try{
    		FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("EditBusiness.fxml"));
			 infopane = loader.load();
    	}catch(Exception e){ 
    		e.printStackTrace();
    	}

		/*
		 * 
		 */	JFXDialogLayout content = new JFXDialogLayout();
	    	content.setHeading(new Text(""));
	    	content.setBody(infopane);
	    	content.setStyle("-fx-background-color: #2b89e0");
	    	
	    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
	    	JFXButton bt = new JFXButton("Done");
	    	JFXButton bt1 = new JFXButton("Cancel");
	    	bt.setOnAction(new EventHandler<ActionEvent>(){
	    		String prodName=null;
				@Override
				public void handle(ActionEvent arg0) {
		
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

    
    }

    @SuppressWarnings("static-access")
   	@FXML
       void goBack(ActionEvent event) throws IOException{
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
}
