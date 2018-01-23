package pams.view;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ForgotCont implements Initializable {
    @FXML
    private JFXButton btnsave;
    @FXML
    private JFXTextField word,id;
    @FXML
    private Label labelIp;
    ConnectDB database = new ConnectDB();
	 private Connection con;
	    private ResultSet rs,rs1,rsT,rsC;
	    private Statement st,st1,stC,stT;
	    private PreparedStatement prep;
		private WorkIndicatorDialog wd=null;
		String passw = null;
		int s=0;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
    @SuppressWarnings("unchecked")
	@FXML
    void goSave(ActionEvent event) {

		String myword,ID;
		try{
			myword = word.getText();
			ID = id.getText();
		if(myword.length()==0 && ID.length()==0){
			
		}else{
			wd = new WorkIndicatorDialog(null, "Retrieving password...");
		 	   wd.addTaskEndNotification(result -> {
		 		  String outres = result.toString();
		          // System.out.println("nomaa "+outres);
		           if(outres.equals("1")){
		        	   labelIp.setText("Your password is:"+passw);
		        	  
		           }else if(outres.equals("0")){
		        	   TrayNotification tray = new TrayNotification();
		  		     tray.setNotificationType(NotificationType.WARNING);
		  		     tray.setTitle("No user with such details");
		  		     tray.setMessage("if you cant remember anything seek help from \n database administrator");
		  		     tray.setAnimationType(AnimationType.SLIDE);
		  		     tray.showAndDismiss(Duration.millis(4000));  
		        	  
		           }
		           wd=null;
		 	  });
	           wd.exec("fetch", inputParam -> {
	        		String []parts;
	        		try{
	    				con= database.dbconnect();
	    				   st= con.createStatement();
	    				   String query = "SELECT password FROM user WHERE Id='"+ID+"' AND username='"+myword+"' ";
	    				   rs=st.executeQuery(query);
	    				   	if(rs.next()){
	    				   		passw = rs.getString("password");
	    				   s=1;
	    				   	}
	    				   	
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
			           
			           return new Integer(s); 
		           });
	    
		}
		}catch(Exception e){
			e.printStackTrace();
		}

    
    }
}
