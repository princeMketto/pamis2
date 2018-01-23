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

public class AddSuppController implements Initializable {
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
@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	// TODO Auto-generated method stub
	
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
	       tray.setTitle("Failure adding supplier");
	       tray.setMessage("Make sure to complete the form .");
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
		       tray.setTitle("SUPPLIER ADDED SUCCESSFULLY ");
		       tray.setMessage("You can now find this supplier in the list..");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
		       /*
				 * closing the window after insertion
				 */
		     
		   	}
  		   }if(outres.equals("-1")){
  		 TrayNotification tray = new TrayNotification();
	       tray.setNotificationType(NotificationType.ERROR);
	       tray.setTitle("THE ID IS IN USE BY OTHER SUPPLIER");
	       tray.setMessage("Make sure to provide unique ID.");
	       tray.setAnimationType(AnimationType.SLIDE);
	       tray.showAndDismiss(Duration.millis(4000));
  	   }if(outres.equals("3")){
  		 TrayNotification tray = new TrayNotification();
			//   Notification notification = NotificationType.SUCCESS;
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("Failure adding supplier");
		       tray.setMessage("Please Fill the form completely/correctly");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
  	   }if(outres.equals("-3")){
  		 TrayNotification tray = new TrayNotification();
			//   Notification notification = NotificationType.SUCCESS;
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("use valid email format");
		       tray.setMessage("eg: example@gmail.com");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
  	   }if(outres.equals("5")){
  		   
  	   }
  	    wd=null;
  	 });
  		 wd.exec("fetch", inputParam -> {
			int z = 0; point = false;
			  if(!(userID.getText().isEmpty() && name.getText().isEmpty() && address.getText().isEmpty() 
			    		&& contact.getText().isEmpty() && note.getText().isEmpty())){
			    	int mobile = 0;
			    	String id,nam,addr,not,cont; String mail=null;
			    	cont = contact.getText();
			    	id = userID.getText().toUpperCase(); nam =  name.getText().toUpperCase(); addr = address.getText().toUpperCase();
			    	not = note.getText().toUpperCase();
			    	try{
			    	if(!(email.getText().equals(null))){
			    		Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+"); // [A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}
			    		Matcher m = pattern.matcher(email.getText());
			    		if(m.find() && m.group().equals(email.getText())){
			    			mail = email.getText();
			    		}else{
			    			//invalid email
			    			z=-3;
			    		}
			    	}else{
			    		mail = null;
			    	}
			    	}catch(NullPointerException nl){
			    		mail = null;
			    		point=true;
					}
			    	try{
			    		 con= database.dbconnect();
						  prep = (PreparedStatement) con.prepareStatement("INSERT INTO suppliers(id,name,address,email,contact,note,comment) VALUES("
					    	  		+ "?,?,?,?,?,?,?)");
						  prep.setString(1, id);
				    	  prep.setString(2, nam);
				    	  prep.setString(3, addr);
				    	  prep.setString(4, mail);
				    	  prep.setString(5, cont);
				    	  prep.setString(6, not);
				    	  prep.setString(7, "ACTIVE");
				    	  int out=prep.executeUpdate();
							
							if(out >0){
							z=1;
							}else{
								z=0;
							}
			    	}catch(SQLException err){
			    		err.printStackTrace();
			    		z=-1;
			    	}
			    	
			    }else{
			    	z=3;
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
