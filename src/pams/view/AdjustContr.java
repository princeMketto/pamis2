package pams.view;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class AdjustContr  implements Initializable {
	  @FXML
	    private Label lbName;

	    @FXML
	    private JFXComboBox<String> choice;

	    @FXML
	    private JFXTextField reason;

	    @FXML
	    private JFXTextField qty;

	    @FXML
	    private JFXButton btnAdd;
	    MyInvContr inv = new MyInvContr();
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs,rs1,rsT,rsC;
		    private Statement st,st1,stC,stT;
		    private PreparedStatement prep;
			private WorkIndicatorDialog wd=null;
			 ObservableList<String>list = FXCollections.observableArrayList();
			 String mode=null,reas=null;
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1) {
	    	lbName.setText(inv.getNam());
	    	reason.setOpacity(0.25);
	    	reason.setEditable(false);
	    	list.clear();
	    	list.add("specify adjustment");
	    	list.add("damaged");
	    	list.add("expired");
	    	list.add("others");
	    	choice.setItems(list);
	    	choice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

				@Override
				public void changed(ObservableValue<? extends String> arg0, String old, String newv) {
					if(!newv.equals("specify adjustment")){
						if(!newv.equals("others")){
							reason.setOpacity(0.25);
					    	reason.setEditable(false);
					    	mode = newv;
						}else{
							reason.setOpacity(1);
					    	reason.setEditable(true);
					    	mode = newv;
					    	//reas = reason.getText();
						}
					}else {
						mode = "error";
					}
					
				}
				
			});
		}
	    @SuppressWarnings("unchecked")
		@FXML
	    void submit(ActionEvent event) {
	    	wd = new WorkIndicatorDialog(null, "Adjusting Inventory...");
	      	 wd.addTaskEndNotification(result -> {
	      		  String outres = result.toString();
	      	   if(outres.equals("1")){
	      		 TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.INFORMATION);
			       tray.setTitle("Details submitted");
			       tray.setMessage("current inventory is been altered");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(5000));
	      	   }
	      	 if(outres.equals("0")){
	      		   
	      	   }
	      	 if(outres.equals("-1")){
	      		TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("Specify Adjustment");
			       tray.setMessage("you need to select cause for the adjustment");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(5000));
	      	   }if(outres.equals("-2")){
		      		TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.WARNING);
				       tray.setTitle("insert Quantity");
				       tray.setMessage("to adjust inventory insert quantity");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(5000));
		      	   }if(outres.equals("-3")){
			      		TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.ERROR);
					       tray.setTitle("numerical alert");
					       tray.setMessage("the quantity u specified exceeds available stock");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(5000));
			      	   }
	      	
	      	    wd=null;
	      	 });
	      		 wd.exec("fetch", inputParam -> {
	   			int z = 0;
	   			try{
	   				int qnt = Integer.parseInt(qty.getText());
	   				if(qnt > inv.getQtL()){
	   					z=-3;
	   				}else{
	   				String sababu=null;
	   				if(mode.equals("error")){
	   					z=-1;
	   				}else{
	   					if(mode.equals("others")){
	   						sababu = reason.getText();
	   						
	   					}else{
	   						sababu = mode;
	   					}
	   					java.util.Date nw = new java.util.Date();
	   					java.sql.Date sqlnw = new java.sql.Date(nw.getTime());
	   			String user = LoginController.getuser();
	   			// id,name,date,remak,qty,user
	   		//	System.out.println("VIEW:"+inv.getID()+" ,"+inv.getNam()+", "+sqlnw+" ,"+sababu+" ,"+inv.getQtL()+" ,"+qnt+","+user);
	   			try{
	   			 con= database.dbconnect();
	   			 prep = (PreparedStatement) con.prepareStatement("INSERT INTO adjustment(id,name,date,remarks,qty,issued_by) VALUES("
			    	  		+ "?,?,?,?,?,?)");
				
				  prep.setString(1, inv.getID());
				//  prep.setInt(2, batch);
		    	  prep.setString(2, inv.getNam());
		    	  prep.setDate(3, sqlnw);
		    	  prep.setString(4, sababu);
		    	  prep.setInt(5, qnt);
		    	  prep.setString(6, user);
		    	int  out=prep.executeUpdate();
					prep.close();
					if(out >0){
						 prep = (PreparedStatement) con.prepareStatement("UPDATE  products SET qty_remain = ?"
							  		+ " WHERE code = ?");
					    	  prep.setInt(1, inv.getQtL() - qnt);
					    	  prep.setString(2, inv.getID());
					    	 
							 int ou= prep.executeUpdate();
							
								prep.close();
								if(ou>0){
									z=1;
								}
						//************
					}else{
						z=0;
					}
					con.close();
	   			}catch(SQLException sq){
	   				sq.printStackTrace();
	   			}
	   				}
	   			}
	   			}catch(Exception e){
	   				e.printStackTrace();
	   				z=-2;
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
