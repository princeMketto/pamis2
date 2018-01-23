package pams.view;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class AddUserController implements Initializable {
	@FXML
    private JFXTextField userID;

    @FXML
    private JFXTextField firstname;

    @FXML
    private JFXTextField lastname;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXRadioButton male;

    @FXML
    private ToggleGroup gender;

    @FXML
    private JFXRadioButton female;

    @FXML
    private ChoiceBox status;

    @FXML
    private JFXTextField pass;

    @FXML
    private JFXTextField phon;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXButton savebtn;

    @FXML
    private JFXButton cancelbtn;
    ConnectDB database = new ConnectDB();
    private WorkIndicatorDialog wd=null;
    private Connection con;
       private ResultSet rs;
       private Statement st;
       private PreparedStatement prep;
       String gend;
       ObservableList<String>statlist = FXCollections.observableArrayList();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		male.setUserData("Male");
		female.setUserData("Female");
		
		male.setToggleGroup(gender);
		female.setToggleGroup(gender);
		
		gender.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				  if (gender.getSelectedToggle() != null && !gender.getSelectedToggle().equals("Gender") ) {
					  	gend = gender.getSelectedToggle().getUserData().toString();
			            // System.out.println(gender.getSelectedToggle().getUserData().toString());
			             // Do something here with the userData of newly selected radioButton

			         }
				
			}
			
		});
		
		statlist.clear();
		statlist.add("Status");
		statlist.add("Admin");
		statlist.add("cashier");
		statlist.add("storekeeper");
		status.setValue("Status");
		status.setItems(statlist);
	}
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
	    Stage stage = (Stage) cancelbtn.getScene().getWindow();
	    stage.close();
	}
	@SuppressWarnings("unchecked")
	@FXML
	void saveUser(ActionEvent event) {
		if(!(userID.getText().isEmpty() && firstname.getText().isEmpty() && phon.getText().isEmpty() &&
				lastname.getText().isEmpty() && username.getText().isEmpty() 
			&& status.getSelectionModel().getSelectedItem().equals("Status") && email.getText().isEmpty() )){
			wd = new WorkIndicatorDialog(null, "Registering user...");
	      	 wd.addTaskEndNotification(result -> {
	      		  String outres = result.toString();
	      	   if(outres.equals("1")){
	      		 TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.SUCCESS);
			       tray.setTitle("USER ADDED SUCCESSFULLY");
			       tray.setMessage("This user can now use the system..");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
			       /*
					 * closing the window after insertion
					 */
			   
				try {
					Main.showManageScene();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	      	   }if(outres.equals("0")){
	      		 TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.ERROR);
			       tray.setTitle("FAILURE ADDING USER");
			       tray.setMessage("Make sure to complete the form .");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
	      	   }if(outres.equals("-1")){
	      		 TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.ERROR);
				       tray.setTitle("THIS ID IS IN USE BY OTHER USER");
				       tray.setMessage("Make sure to provide unique ID.");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(4000));
	    	   }if(outres.equals("-2")){
	      	 TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.WARNING);
		       tray.setTitle("INVALID EMAIL ADDRESS");
		       tray.setMessage("please enter valid email..");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
	      	 }
	   	  
	      	
	      	    wd=null;
	      	 });
	      	 wd.exec("fetch", inputParam -> {
	 			int z = 0;
	 			try{
					String id,fnam,lnam,unam,stat,mail = null,pas,phone;
					id = userID.getText(); fnam = firstname.getText(); lnam = lastname.getText(); unam = username.getText();
					stat = (String) status.getSelectionModel().getSelectedItem();
						try{
					if( email.getText().contains("@")){
						mail =  email.getText();	
					}else{
						System.out.println("WRONG EMAIL");
					}
						}catch(Exception er){
							z=-2;
						}
					pas = pass.getText();
					phone = phon.getText();
					try{
			    		 con= database.dbconnect();
						  prep = (PreparedStatement) con.prepareStatement("INSERT INTO user(Id,fname,lname,username,status,password,phone,email,sex,comment) VALUES("
					    	  		+ "?,?,?,?,?,?,?,?,?,?)");
						  prep.setString(1, id);
				    	  prep.setString(2, fnam);
				    	  prep.setString(3, lnam);
				    	  prep.setString(4, unam);
				    	  prep.setString(5, stat);
				    	  prep.setString(6, pas);
				    	  prep.setString(7, phone);
				    	  prep.setString(8, mail);
				    	  prep.setString(9, gend );
				    	  prep.setString(10, "ACTIVE" );
				    	  int out=prep.executeUpdate();
							
							if(out >0){
								z=1;
							}else{
								z=0;
							}
			    	}catch(SQLException err){
			    		z=-1;
			    	}
				}catch(Exception ex){
					ex.printStackTrace();
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
		       tray.setTitle("FAILURE ADDING USER");
		       tray.setMessage("Please Fill the form completely/correctly");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
		}
	}
}
