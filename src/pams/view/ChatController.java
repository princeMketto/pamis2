package pams.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ChatController implements Initializable {
LoginController lg = new LoginController();
Main main;
    @FXML
    private JFXButton btnBack;

    @FXML
    private TableView<Chat> tableview;

    @FXML
    private TableColumn<Chat, String> messcol;
    @FXML
    private TableColumn<Chat, String> sendcol;

    @FXML
    private Text textv;

    @FXML
    private JFXTextArea notes;

    @FXML
    private JFXCheckBox adm;

    @FXML
    private JFXCheckBox stor;

    @FXML
    private JFXCheckBox cash;

    @FXML
    private JFXButton btnsend;
    ConnectDB database = new ConnectDB();
    private Connection con;
    private ResultSet rs;
    private Statement st;
    private PreparedStatement prep;
    String mess,sender;
    String choice;
    boolean admi,store,cashier = false;
	String admin,stores,cashiers;
	 private WorkIndicatorDialog wd=null;
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    //	LoginController lg = new LoginController();
		fetchData();
    	Image bac = new Image(getClass().getResourceAsStream("images/back.png"));
    	btnBack.setGraphic(new ImageView(bac));
    	
    	messcol.setCellValueFactory(new PropertyValueFactory<>("message"));
    	sendcol.setCellValueFactory(new PropertyValueFactory<>("sender"));
    	
    	tableview.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				mess = tableview.getSelectionModel().getSelectedItem().getMessage();
				sender = tableview.getSelectionModel().getSelectedItem().getSender();
				textv.setText(mess+"\n \t FROM:"+sender);	
			}
			});
    	adm.setOnAction(e -> handleCheckBoxAction(e));
    	stor.setOnAction(e -> handleCheckBoxAction(e));
    	cash.setOnAction(e -> handleCheckBoxAction(e));
	}
    @SuppressWarnings("unchecked")
	private void fetchData() {
	for(int i=0; i<tableview.getItems().size(); i++){
    		
		tableview.getItems().clear();
    	}
    	wd = new WorkIndicatorDialog(null, "Fetching Message(S)...");
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
			   String query = "SELECT * FROM note WHERE NOT sender='"+lg.getStats()+"' AND  receiver='"+lg.getStats()+"'";
			
			   rs=st.executeQuery(query);
			   while(rs.next()){
				   String mess,receiv,sende;
				  
				  
				   mess = rs.getString("message");
				   sende = rs.getString("sender");
				   receiv = rs.getString("receiver");
				  
				   tableview.getItems().add(new Chat(mess,sende));
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
	private void handleCheckBoxAction(ActionEvent e) {
    	
		if(adm.isSelected()){
			admin = adm.getText();
			admi = true;
		}
		if(stor.isSelected()){
			stores = stor.getText();
			store = true;
		}
		if(cash.isSelected()){
			cashiers = cash.getText();
			cashier = true;
		}
		
	}
	@FXML
    void sendNote(ActionEvent event) {
		String note="";
		note = notes.getText();
    
		if(!note.equals("")){
			if(admi){
    	//System.out.println(note+"\n \t SEND TO:"+admin);
    	try{
    		 con= database.dbconnect();
			 	
    		 prep = (PreparedStatement) con.prepareStatement("INSERT INTO note(message,sender,receiver) VALUES("
  		+ "?,?,?)");
    		 prep.setString(1, note);
	    	  prep.setString(2, lg.getStats());
	    	  prep.setString(3, adm.getText());
	    	  int out=prep.executeUpdate();
	    	  if(out>0){
	    		  notes.setText(null);
	    		  TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.SUCCESS);
			       tray.setTitle("NOTE SENT");
			       tray.setMessage("user will receive your note");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
	    	  }else{
	    		  System.out.println("FAIL TO SEND NOTE");
	    	  }
	    	  prep.close();
	    	  con.close();
    	}catch(SQLException es){
    		
    	}
			admi = false;
			}
			if(store){
				try{
		    		 con= database.dbconnect();
					 	
		    		 prep = (PreparedStatement) con.prepareStatement("INSERT INTO note(message,sender,receiver) VALUES("
		  		+ "?,?,?)");
		    		 prep.setString(1, note);
			    	  prep.setString(2, lg.getStats());
			    	  prep.setString(3, stor.getText());
			    	  int out=prep.executeUpdate();
			    	  if(out>0){
			    		  notes.setText(null);
			    		  TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.SUCCESS);
					       tray.setTitle("NOTE SENT");
					       tray.setMessage("user will receive your note");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(4000));
			    	  }else{
			    		//  System.out.println("FAIL TO SEND NOTE");
			    	  }
			    	  prep.close();
			    	  con.close();
		    	}catch(SQLException es){
		    		
		    	}
    //	System.out.println(note+"\n \t SEND TO:"+stores);
			store=false;
			}
			if(cashier){
				try{
		    		 con= database.dbconnect();
					 	
		    		 prep = (PreparedStatement) con.prepareStatement("INSERT INTO note(message,sender,receiver) VALUES("
		  		+ "?,?,?)");
		    		 prep.setString(1, note);
			    	  prep.setString(2, lg.getStats());
			    	  prep.setString(3, cash.getText());
			    	  int out=prep.executeUpdate();
			    	  if(out>0){
			    		  notes.setText(null);
			    		  TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.SUCCESS);
					       tray.setTitle("NOTE SENT");
					       tray.setMessage("user will receive your note");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(4000));
			    	  }else{
			    	//	  System.out.println("FAIL TO SEND NOTE");
			    	  }
			    	  prep.close();
			    	  con.close();
		    	}catch(SQLException es){
		    		
		    	}
    	//System.out.println(note+"\n \t SEND TO:"+cashiers);
			cashier = false;
			}
			}else{
				TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("PLEASE WRITE NOTE");
			       tray.setMessage("You cant send note with empty text");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
			}
    	
    }
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

}
