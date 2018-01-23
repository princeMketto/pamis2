package pams.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.sql.DriverManager;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ServerControl implements Initializable {
	  @FXML
	    private JFXButton btnsave;
	   @FXML
	    private JFXTextField dbname;

	    @FXML
	    private JFXPasswordField dbpass;
	  @FXML
	    private Label labelIp;
	  @FXML
	    private Label error;

	    @FXML
	    private JFXToggleButton dbmode;
	    String line;
	    String []conf;
	   

	    @FXML
	    private JFXTextField ip;
	    File file = new File("me.txt");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	//	dbmode.a
		loadMode();
	}
	 private void loadMode() {
		 if(!file.exists()){
			 dbmode.selectedProperty().set(false);
			 dbname.setOpacity(0);
			 dbpass.setOpacity(0);
			 ip.setOpacity(1);
			}else{
				try{
				BufferedReader br = new BufferedReader(new FileReader("me.txt"));

				if ((line = br.readLine()) != null) {
					conf = line.split("-");
					String mode = conf[0];
					
					if(mode.equals("online")){
						 dbname.setOpacity(1);
						 dbpass.setOpacity(1);
						 ip.setOpacity(0);
						 dbmode.setSelected(true);
					}else if(mode.equals("offline")){
						  dbname.setOpacity(0);
						 dbpass.setOpacity(0);
						 ip.setOpacity(1);
						 dbmode.setSelected(false);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					}
				}		

		/* if(dbmode.isSelected()){
			 labelIp.setText("IP ON:");
		 }else{
			 labelIp.setText("IP OFF:"); 
		 }*/
	}
	@FXML
	    void Mode(ActionEvent event) {
		 if(dbmode.isSelected()){
			 labelIp.setText("online mode selected");
			// dbmode.selectedProperty().set(true);
			 dbname.setOpacity(1);
			 dbpass.setOpacity(1);
			 ip.setOpacity(0);
			 
		 }else{
			 labelIp.setText("offline mode selected"); 
			// dbmode.selectedProperty().set(false);
			 dbname.setOpacity(0);
			 dbpass.setOpacity(0);
			 ip.setOpacity(1);
		 }
	    }
	@FXML
    void goSave(ActionEvent event) {
		if(dbmode.isSelected()){
			if(dbname.getText().length() !=0 && dbpass.getText().length() !=0){
				error.setText(null);
				String nam = dbname.getText();
				String pas = dbpass.getText();
				String myip = "online"+"-"+nam+"-"+pas;
				try{
				file.createNewFile();
				//help.show();
				
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(myip);
				
				bw.close();
				dbname.setText(null);
				dbpass.setText(null);
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					String line;
					BufferedReader		br = new BufferedReader(new FileReader("me.txt"));

					if ((line = br.readLine()) != null) {
						labelIp.setText("SAVED:"+line);
					}else{
						
						
					}
					}catch(Exception e){
						
					}
		    
				
			}else{
				error.setText("please fill all details");
			}
			
		}else{
			if(ip.getText().length() !=0){
				String ips = ip.getText();
				String myip = "offline"+"-"+ips;
				try{
				file.createNewFile();
				//help.show();
				
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(myip);
				
				bw.close();
				ip.setText(null);
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					String line;
					BufferedReader		br = new BufferedReader(new FileReader("me.txt"));

					if ((line = br.readLine()) != null) {
						labelIp.setText("SAVED:"+line);
					}else{
						
						
					}
					}catch(Exception e){
						
					}
			}else{
				error.setText("please fill server IP");
			}
		
	    
		}
	}
}
