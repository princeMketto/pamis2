package pams.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class EditOfficeInfoContr implements Initializable {
	 @FXML
	    private JFXButton btnBack;

	    @FXML
	    private AnchorPane anchor;
	  @FXML
	    private JFXTextField name;

	    @FXML
	    private JFXTextField address;

	    @FXML
	    private JFXTextField web;

	    @FXML
	    private JFXTextField phone;
	    @FXML
	    private JFXPasswordField pass;

	    @FXML
	    private JFXTextField mail,tin;

	    @FXML
	    private JFXButton btnsave;

	    @FXML
	    private ImageView logo;

	    @FXML
	    private JFXButton btnlogo;
	    private WorkIndicatorDialog wd=null;
	   		 ConnectDB database = new ConnectDB();
		    private Connection con;
		    private ResultSet rs;
		    private Statement st,st1,st2;
		    private PreparedStatement prep;
		    FileInputStream input;
			InputStream is = null;
			Image image=null;
			FileChooser flc;
		  File selectedFile ;
		  String nam,addr,email,webs,phon,tins,passw;

	    @FXML
	    void attachlogo(ActionEvent event) {
	    	flc  = new FileChooser();
	    	selectedFile = flc.showOpenDialog(null);
	    	if(selectedFile != null){
	    	
	    		try {
	    			input = new FileInputStream(selectedFile);
					image= new Image(input);

					logo.setImage(image);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
	    	}
	    
	    
	    }

	    @SuppressWarnings("unchecked")
		@FXML
	    void submit(ActionEvent event) {
	    	try{
	    		nam	= name.getText().toUpperCase();
	    		addr = address.getText().toUpperCase();
	    		tins = tin.getText();
	    		Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+"); // [A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}
				Matcher m = pattern.matcher( mail.getText());
				if(m.find() && m.group().equals( mail.getText())){
					//return true;
					email = mail.getText();
					passw = pass.getText();
				}else{
					//
					TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.WARNING);
				       tray.setTitle("USE VALID EMAIL ADDRESS");
				       tray.setMessage("email format: name@provider.category");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(4000));
				}
			//	Pattern pattern1 = Pattern.compile("[a-z][a-z][a-z].[a-zA-Z0-9]+([.][a-zA-Z]+)+"); // [A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}
				try{
					if(!(web.getText().equals(null)|| web.getText().isEmpty())){
					//	Matcher m1 = pattern1.matcher( web.getText());
					//	if(m1.find() && m1.group().equals( web.getText())){
							//return true;
							webs = web.getText();
					/*	}else{
							TrayNotification tray = new TrayNotification();
						       tray.setNotificationType(NotificationType.WARNING);
						       tray.setTitle("USE VALID WEB ADDRESS");
						       tray.setMessage("WEB format: wwww.yourdomain.category");
						       tray.setAnimationType(AnimationType.SLIDE);
						       tray.showAndDismiss(Duration.millis(4000));
						}*/
						}else{
							webs = null;
						}
				}catch(NullPointerException nl){
					webs = null;
				}
			
				phon = phone.getText();
				if(!(nam.length() == 0 && addr.length() == 0 && email.length()==0 &&
						 phon.length()==0 && pass.toString().length()==0)){
					wd = new WorkIndicatorDialog(null, "Saving details ...");
					   wd.addTaskEndNotification(result -> {
					 		  String outres = result.toString();
					          // System.out.println("nomaa "+outres);
					           if(outres.equals("1")){
					        	   JFXSnackbar bar = new JFXSnackbar(anchor);
					 	        	bar.show("Details successfully saved.",3000);
					 	        	name.setText(null);
						    		address.setText(null);
						    		mail.setText(null);
						    		pass.setText(null);
						    		web.setText(null);
						    		tin.setText(null);
						    		phone.setText(null);  
						    		try{
					 	        		Main.showManageScene();
					 	        	}catch(Exception e){
					 	        		
					 	        	}
					           }else if(outres.equals("0")){
					        	  
					 				JFXSnackbar bar = new JFXSnackbar(anchor);
					 	        	bar.show("Details are not saved. retry",3000);
					 	        	
					           }if(outres.equals("2")){
						        	  
						 				JFXSnackbar bar = new JFXSnackbar(anchor);
						 	        	bar.show("Details NOT saved \n "
						 	        			+ "1.make sure you filled all details \n"
						 	        			+ "2.make sure to have working network . \n Then retry",4000);
						           }
					           wd=null;
					 	   });
						 wd.exec("fetch", inputParam -> {
				 			 boolean contain = false;
				 			 int s=0;
				 			 try{
							    	if(selectedFile != null){
							    input = new FileInputStream(selectedFile); 
							    	}else{
							    		
							    		selectedFile =new File("logo.png");
							    		input = new FileInputStream(selectedFile);
							    	}
							    } catch (NullPointerException  ex1) {
									// TODO Auto-generated catch block
									ex1.printStackTrace();
									//JOptionPane.showMessageDialog(null, "No attachment selected");
									 

								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									//JOptionPane.showMessageDialog(null, "File not found. retry");
								}
				 			 try{
				 				con= database.dbconnect();
				 				   st= con.createStatement();
				 			 String select = "SELECT * FROM business_details";
							    rs=st.executeQuery(select);
							    
							    if(rs.next()){
							    String bname = rs.getString("name");
							    String baddr = rs.getString("address");
							    if(nam.equals(bname) && addr.equals(baddr)){
							    	contain = true;
							    }
							    }
							    st.close();
							    con.close();
				 			 }catch(SQLException e){
				 				 e.printStackTrace();
				 				 s = 2;
				 			 }
				 			 if(contain){
				 				 try{
				 					con= database.dbconnect();
					 				  
				 					 prep = (PreparedStatement) con.prepareStatement("UPDATE business_details SET "
				 						 		+ "id=?,name = ?,tin = ?,address = ?,email = ?,password=?,website = ?,phone=?,logo=? WHERE id = ? ");
						 				// prep.setString(1,snum);
										 prep.setInt(1,1);
										 prep.setString(2,nam);
										 prep.setString(3,tins);
										 prep.setString(4,addr);
										 prep.setString(5,email);
										 prep.setString(6,passw);
										 prep.setString(7,webs);
										 prep.setString(8,phon);
										 prep.setBinaryStream(9, (InputStream)input,(int)selectedFile.length());
										 prep.setInt(10,1);
										   int out=prep.executeUpdate();
										 if(out >0){
											 s = 1;
										 }else{
											 s = 0;
										 }
										 prep.close();
										 con.close();
				 				 }catch(SQLException sq){
				 					 sq.printStackTrace();
				 					 s=2;
				 				 }
				 			 }else{
				 				 try{
				 					con= database.dbconnect();
					 				   st1= con.createStatement();
					 				//  st2= con.createStatement();
					 				// st= con.createStatement();
				 				String sql2 = "DELETE FROM business_details WHERE 1";
						    	st1.executeUpdate(sql2);
						    	 prep = (PreparedStatement) con.prepareStatement("INSERT INTO business_details "
			 						 		+ "(id,name,tin,address,email,password,website,phone,logo) VALUES (?,?,?,?,?,?,?,?,?) ");
					 				 prep.setInt(1,1);
									 prep.setString(2,nam);
									 prep.setString(3,tins);
									 prep.setString(4,addr);
									 prep.setString(5,email);
									 prep.setString(6,passw);
									 prep.setString(7,webs);
									 prep.setString(8,phon);
									 prep.setBinaryStream(9, (InputStream)input,(int)selectedFile.length());
									   int out=prep.executeUpdate();
									 if(out >0){
										 s = 1;
									 }else{
										 s =2;
									 }
									 prep.close();
						    	
						    	
						    	prep.close();
						    	con.close();
				 				 }catch(SQLException x){
				 					 s=2;
				 					 x.printStackTrace();
				 				 }
				 				
				 				 }
				 		  
					       
					               try {
					                  Thread.sleep(1000);
					               }	
					               catch (InterruptedException er) {
					                  er.printStackTrace();
					               }
					             
					           return new Integer(s);
					           
					           
					        });   
				}else{
					
				}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("PLEASE FILL ALL THE DETAILS BEFORE PERFORMING THIS ACTION");
			       tray.setMessage("You cannot submit empty form");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
	    	}
	    }
	   
	   
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { 	
    	wd = new WorkIndicatorDialog(null, "");
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
	 					tins = rs.getString("tin");
	 					phon = rs.getString("phone");
	 					
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


}
