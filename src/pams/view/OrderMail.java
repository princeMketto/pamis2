package pams.view;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class OrderMail implements Initializable{
	  @FXML
	    private AnchorPane pane;

	    @FXML
	    private JFXTextField from;

	    @FXML
	    private JFXButton btnSend;

	    @FXML
	    private JFXTextField to,phone;

	    @FXML
	    private JFXTextField subject;

	    @FXML
	    private Label labfile,status;

	    @FXML
	    private RadioButton yes;

	    @FXML
	    private ToggleGroup sms;

	    @FXML
	    private RadioButton no;

	    @FXML
	    private JFXTextArea text;

	    @FXML
	    private RadioButton single;

	    @FXML
	    private ToggleGroup mode;

	    @FXML
	    private RadioButton multiple;

	    @FXML
	    private RadioButton all;

	    @FXML
	    private JFXComboBox<String> vendor;
	    ObservableList<String>suplist = FXCollections.observableArrayList();
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs;
		    private Statement st;
		    private WorkIndicatorDialog wd=null;
		    static String selectmode=null,mess=null;
		    List<String> listofmail = new ArrayList();
		    String pass=null,messO=null,iPhon = null, content = null, kwa = null;
		    String regexStr = "^[0-9]*$";
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		suplist.clear();
		suplist.add("vendor list");
		vendor.setValue("vendor list");
		fillVendor();
		from.setText(OrderController.getMail());
		pass=OrderController.getPass();
		String filename = "item_order.xls";	
		File fl = new File(filename);
		labfile.setText(fl.getName());
		 single.setUserData("single"); multiple.setUserData("multiple"); all.setUserData("all");
		 single.setToggleGroup(mode); multiple.setToggleGroup(mode); all.setToggleGroup(mode);
		 yes.setUserData("yes"); no.setUserData("no"); 
		 yes.setToggleGroup(sms); yes.setToggleGroup(sms);
		 no.setSelected(true);
		 if(no.isSelected()){
			 phone.setOpacity(0);
			 text.setOpacity(0);
		 }else{
			 phone.setOpacity(1);
			 text.setOpacity(1);
		 }
		 mode.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
					  if (mode.getSelectedToggle() != null && !mode.getSelectedToggle().equals("mode") ) {
						  	selectmode = mode.getSelectedToggle().getUserData().toString();
						  			if(selectmode.equals("single")){
						  				vendor.setOpacity(1);
						  				to.setText(null);
						  				
						  			}if(selectmode.equals("multiple")){
						  				vendor.setOpacity(1);
						  				to.setText(null);
						  				
						  			}if(selectmode.equals("all")){
						  				to.setText(null);
						  				vendor.setOpacity(0);
						  				
						  				for(int i=0; i<listofmail.size(); i++){
						  					try{
						  					if(to.getText().length() !=0){
						  						String oldmail = to.getText();
						  							to.setText(oldmail+","+listofmail.get(i));
						  						}else{
						  							to.setText(listofmail.get(i));
						  						}
						  					}catch(NullPointerException ex){
						  						to.setText(listofmail.get(i));
							  				}
						  				}
						  				
						  			}
				          		  }
					
				}
				
			});
		 sms.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
					  if (sms.getSelectedToggle() != null && !sms.getSelectedToggle().equals("mode") ) {
						  	mess = sms.getSelectedToggle().getUserData().toString();
						  		if(mess.equals("yes")){
						  			 phone.setOpacity(1);
									 text.setOpacity(1);
						  		}if(mess.equals("no")){
						  			 phone.setOpacity(0);
									 text.setOpacity(0);
						  		}
				          		  }
					
				}
				
			});
		 vendor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldval, String newval) {
				//System.out.println(newval);
				//newval = "mama,baba,dada";
			if(selectmode.equals("single")){
				String []mail = newval.split(",");
				
				String venmail = mail[1];
					to.setText(venmail);
			}else if(selectmode.equals("multiple")){
				String []mail = newval.split(",");
				
				String venmail = mail[1];
				try{
				
					if(to.getText().length() !=0){
					String oldmail = to.getText();
						to.setText(oldmail+","+venmail);
					}else{
						to.setText(venmail);
					}
				}catch(NullPointerException nl){
					to.setText(venmail);
				}
			
			}else{
				JFXSnackbar bar = new JFXSnackbar(pane);
	        	bar.show("Choose between SEND TO mode",3000);
			}
			}
			 
		 });
	}
	  @SuppressWarnings("unchecked")
	private void fillVendor() {
		  wd = new WorkIndicatorDialog(null, "");
		   	 wd.addTaskEndNotification(result -> {
		   		  String outres = result.toString();
		   	   if(outres.equals("1")){
		   		   vendor.setItems(suplist);
		   		   subject.setText("Item Order");
		   	   }
			  
		   	
		   	    wd=null;
		   	 });
		   		 wd.exec("fetch", inputParam -> {
					int z = 0;
					try{
						con= database.dbconnect();
		 				   st= con.createStatement();
		 				   String query = "SELECT * FROM suppliers";
		 				   rs=st.executeQuery(query);
		 				   suplist.clear();
		 				  while(rs.next()){
		 					   String nam,mal;
		 					  mal = rs.getString("email");
		 					   nam = rs.getString("name");
		 					   suplist.add(nam+","+mal);
		 					  listofmail.add(mal);
		 					   z=1;
		 				  }
		 				  
		 					con.close();   
					}catch(SQLException sq){
						sq.printStackTrace();
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
	@SuppressWarnings("unchecked")
	@FXML
	    void sendOrder(ActionEvent event) {
		wd = new WorkIndicatorDialog(null, "");
	   	 wd.addTaskEndNotification(result -> {
	   		  String outres = result.toString();
	   	   if(outres.equals("1")){
	   		
	 	   			JFXSnackbar bar = new JFXSnackbar(pane);
	 	        	bar.show("EMAIL SENT",3000);
	 	        	from.setText(null);
	 	        	to.setText(null);
	 	        	subject.setText(null);
	 	        	labfile.setText(null);
	 	       	status.setText(messO);
	 	        	/*SmsClass sms = new SmsClass();
					String smsR;
					try {
						smsR = sms.send_sms(iPhon, content+". Attachment FILE is being sent to:"+kwa);
						System.out.println("REPORT:"+smsR);
						messO = smsR;
						status.setText(messO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
	   		   
	   	   }if(outres.equals("-2")){
	   		JFXSnackbar bar = new JFXSnackbar(pane);
        	bar.show("INVALID phone number",3000);
	   	   }if(outres.equals("-1")){
	   		JFXSnackbar bar = new JFXSnackbar(pane);
        	bar.show("INVALID receiver email",3000);
	   	   }if(outres.equals("-3")){
	   		JFXSnackbar bar = new JFXSnackbar(pane);
        	bar.show("TIMEOUT. email not sent, retry",3000);
	   	   }
		  
	   	
	   	    wd=null;
	   	 });
	   		 wd.exec("fetch", inputParam -> {
				int z = 0;
				MailApp mai = new MailApp();
					try{
						String frm;
						String tom;
						Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+"); // [A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}
						Matcher m = pattern.matcher(from.getText());
						if(m.find() && m.group().equals(from.getText())){
							frm =  from.getText();
							
								tom = to.getText();
								String []allmail;
								allmail = tom.split(",");
								String subj = subject.getText();
								if(mess.equals("yes")){
								//	System.out.println("YES SMS");
									
									if(phone.getText().matches(regexStr)){
										String phoneN =phone.getText(); 
										String message = text.getText();
										String filename = "item_order.xls";	
										boolean sent = false;
										if(allmail.length >0){
											iPhon = phoneN; content = message; kwa = tom;
										}
										for(int i=0; i<allmail.length; i++){
											Pattern pattern1 = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+"); // [A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}
											Matcher m1 = pattern1.matcher(allmail[i]);
											if(m1.find() && m1.group().equals(allmail[i])){
												mai.Init();
												mai.InSession(frm, pass);
												mai.sendMessage(frm, allmail[i], subject.getText(), message, filename);
												sent = true;
												z=1;
											}else{
												//invalid sendor name
												System.out.println("Invalid sendor mail "+allmail[i]);
												z=-1;
											}
										}
										
										//send message here
										if(sent){
											SmsClass sms = new SmsClass();
											String smsR = sms.send_sms(phoneN, message+". Attachment FILE is being sent to:"+tom);
											System.out.println("REPORT:"+smsR);
											messO = smsR;
										}else{
											messO=null;
										}
									
									}else{
										//INVALID phone #
										System.out.println("Invalid phone number");
										z=-2;
									}
								}else{
									//send only EMAIL
										String message = text.getText();
										String filename = "item_order.xls";	
										for(int i=0; i<allmail.length; i++){
											Pattern pattern1 = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+"); // [A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}
											Matcher m1 = pattern1.matcher(allmail[i]);
											if(m1.find() && m1.group().equals(allmail[i])){
												mai.Init();
												mai.InSession(frm, pass);
												mai.sendMessage(frm, allmail[i], subject.getText(), message, filename);
												z=1;
											}else{
												//invalid sendor name
												System.out.println("Invalid sendor mail");
												z=-1;
											}
										}
									}
							
						}else{
							//invalid sender email
							z=-1;
						}
						
					}catch(Exception er){
						er.printStackTrace();
						z=-3;
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
