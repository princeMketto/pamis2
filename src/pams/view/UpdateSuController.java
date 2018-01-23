package pams.view;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class UpdateSuController implements Initializable{
	Main main;
	@FXML
	private JFXTextField userID;

	@FXML
	private JFXTextField name;

	@FXML
	private JFXTextField address,email;

	@FXML
	private JFXTextField contact;

	@FXML
	private JFXTextField note;

	@FXML
	private JFXButton savebtn;


	@FXML
	private JFXButton cancelbtn;
	ConnectDB database = new ConnectDB();
	private Connection con;
	   private ResultSet rs;
	   private Statement st;
	   private PreparedStatement prep;
	   private WorkIndicatorDialog wd=null;
	   SupplierController supi = new SupplierController();
	   boolean point;
	   String Id = null,namE = null,addR = null,conTacs = null,noTe = null,em=null;
	   @Override
	   public void initialize(URL arg0, ResourceBundle arg1) {
		  fillData();
	   	
	   }
	   @SuppressWarnings("unchecked")
	private void fillData() {
		   wd = new WorkIndicatorDialog(null, "");
		   	 wd.addTaskEndNotification(result -> {
		   		  String outres = result.toString();
		   	   if(outres.equals("1")){
		   		userID.setText(Id);
			   	name.setText(namE);
			   	address.setText(addR);
			   	email.setText(em);
			   	contact.setText(conTacs);
			   	note.setText(noTe);
		   	   }
		   	 if(outres.equals("0")){
		   		TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("fail to fetch data");
			       tray.setMessage("please retry");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
			   	  }
			  
		   	
		   	    wd=null;
		   	 });
		   		 wd.exec("fetch", inputParam -> {
					int z = 0;
					
					   	
					   try{
						   con= database.dbconnect();
						   st= con.createStatement();
						   String query = "SELECT * FROM suppliers WHERE id='"+supi.getSupID()+"'";
						   rs=st.executeQuery(query);
						   	if(rs.next()){
						   		 Id = rs.getString("id");
						   		 namE = rs.getString("name");
						   		 addR = rs.getString("address");
						   		em = rs.getString("email");
						   		conTacs = rs.getString("contact");
						   		noTe = rs.getString("note");
						   		 z=1;
						   	}else{
						   		z=0;
						   	}
						   
					   }catch(SQLException e){
						   e.printStackTrace();
					   }
		   	            try {
		   	               Thread.sleep(1000);
		   	            }	
		   	            catch (InterruptedException er) {
		   	               er.printStackTrace();
		   	            }
		   	       
		   	          
		   	        return new Integer(z);
		   	        
		   	        
		   	     });
		
	}
	@FXML
	   public void handleCloseButtonAction(ActionEvent event) {
	       Stage stage = (Stage) cancelbtn.getScene().getWindow();
	       stage.close();
	   }
	   @SuppressWarnings("unchecked")
	@FXML
	   void saveSupp(ActionEvent event) {

			RequiredFieldValidator validator = new RequiredFieldValidator();
		    validator.setMessage("Input Required");
		   
		    userID.getValidators().add(validator);
		    userID.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
		        if (!newVal)
		        	userID.validate();
		    });
		    name.getValidators().add(validator);
		    name.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
		        if (!newVal)
		        	name.validate();
		    });
		    address.getValidators().add(validator);
		    address.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
		        if (!newVal)
		        	address.validate();
		    });
		    contact.getValidators().add(validator);
		    contact.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
		        if (!newVal)
		        	contact.validate();
		    });
		    note.getValidators().add(validator);
		    note.focusedProperty().addListener(( o,  oldVal,  newVal) -> {
		        if (!newVal)
		        	note.validate();
		    });
		    
		    wd = new WorkIndicatorDialog(null, "");
		   	 wd.addTaskEndNotification(result -> {
		   		  String outres = result.toString();
		   	   if(outres.equals("0")){
		   		 TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.ERROR);
			       tray.setTitle("FAILURE UPDATING SUPPLIER");
			       tray.setMessage("Make sure to complete the form .");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
		   	   }if(outres.equals("-1")){
		   		TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.ERROR);
			       tray.setTitle("THE ID IS IN USE BY OTHER SUPPLIER");
			       tray.setMessage("Make sure to provide unique ID.");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
		   	   }if(outres.equals("1")){
		   		   	if(point){

				   		TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.INFORMATION);
					       tray.setTitle("detail saved with no valid email");
					       tray.setMessage("you can edit this before using email system");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(4000));
					       /*
							 * closing the window after insertion
							 */
					      
		   		   	}else{

				   		TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.SUCCESS);
					       tray.setTitle("SUPPLIER UPDATED SUCCESSFULLY ");
					       tray.setMessage("You can now find this supplier in the list..");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(4000));
					       /*
							 * closing the window after insertion
							 */
					   
		   		   	}
				 
		   	   }if(outres.equals("2")){
		   		TrayNotification tray = new TrayNotification();
				//   Notification notification = NotificationType.SUCCESS;
			       tray.setNotificationType(NotificationType.ERROR);
			       tray.setTitle("FAILURE UPDATING SUPPLIER");
			       tray.setMessage("Please Fill the form completely/correctly");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
		   	   }if(outres.equals("3")){
		   		TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.ERROR);
			       tray.setTitle("Use valid Email");
			       tray.setMessage("eg: example@gmail.com");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000)); 
		   	   }
			  
		   	    wd=null;
		   	 });
		   		 wd.exec("fetch", inputParam -> {
					int z = 0; point = false;
				    if(!(userID.getText().isEmpty() && name.getText().isEmpty() && address.getText().isEmpty() 
				    		&& contact.getText().isEmpty() && note.getText().isEmpty())){
				    	int mobile = 0;
				    	String id,nam,addr,not,cont,ema=null,emi=null;
				    	cont = contact.getText();
				    	id = userID.getText().toUpperCase(); nam =  name.getText().toUpperCase(); addr = address.getText().toUpperCase();
				    	not = note.getText().toUpperCase(); 
				    	//emi = email.getText();
				    	try{
				    		if(!(email.getText().isEmpty())){
					    		Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+"); // [A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}
					    		Matcher m = pattern.matcher(email.getText());
					    		if(m.find() && m.group().equals(email.getText())){
					    			ema = email.getText();
					    		}else{
					    			//invalid email
					    			z=3;
					    		}
					    	}else{
					    		ema = null;
					    	}
				    	}catch(NullPointerException nl){
				    		ema = null;
				    		point=true;
						}
				    
				    	try{
				    		 con= database.dbconnect();
							  prep = (PreparedStatement) con.prepareStatement("UPDATE suppliers SET name = ?,address = ?,email = ?,contact = ?,note = ?,comment = ?"
							  		+ " WHERE id =?");

							  
					    	  prep.setString(1, nam);
					    	  prep.setString(2, addr);
					    	  prep.setString(3, ema);
					    	  prep.setString(4, cont);
					    	  prep.setString(5, not);
					    	  prep.setString(6, "ACTIVE");
					    	  prep.setString(7, id);
					    	  int out=prep.executeUpdate();
								
								if(out >0){
									z=1;
								}else{
									z=0;
								}
				    	}catch(SQLException err){
				    		System.out.println(err);
				    		z=-1;
				    	}
				    	
				    }else{
				    	z=2;
				    }
		   	            try {
		   	               Thread.sleep(1000);
		   	            }	
		   	            catch (InterruptedException er) {
		   	               er.printStackTrace();
		   	            }
		   	       
		   	          
		   	        return new Integer(z);
		   	        
		   	        
		   	     });
			

	   }
}
