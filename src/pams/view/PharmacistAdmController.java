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
import com.jfoenix.controls.JFXTextField;

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
import pams.Main;

public class PharmacistAdmController implements Initializable {
Main main;
private WorkIndicatorDialog wd=null;
    @FXML
    private JFXButton backbtn;

    @FXML
    private JFXTextField searchUser;

    @FXML
    private JFXButton addUserbtn;

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
    private TableColumn<AddUser, String> phone;

    @FXML
    private TableColumn<AddUser, String> mail;
    static  String  usID;
    ConnectDB database = new ConnectDB();
    private Connection con;
       private ResultSet rs;
       private Statement st;
       private PreparedStatement prep;
    
    @FXML
    void addUser(ActionEvent event) throws IOException {
    	main.showAddUser();
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
    	main.showMainDash();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image ad = new Image(getClass().getResourceAsStream("images/add.png"));
		addUserbtn.setGraphic(new ImageView(ad));
		Image bac = new Image(getClass().getResourceAsStream("images/back.png"));
		backbtn.setGraphic(new ImageView(bac));	
		
		fetchUser();
			userId.setCellValueFactory(new PropertyValueFactory<>("id"));
			fname.setCellValueFactory(new PropertyValueFactory<>("fname"));
			lname.setCellValueFactory(new PropertyValueFactory<>("lname"));
			uname.setCellValueFactory(new PropertyValueFactory<>("uname"));
			sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
			phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
			mail.setCellValueFactory(new PropertyValueFactory<>("mail"));
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
	private void fetchUser() {
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
	 					   String fnam,lnam,id,phon,mail,sex,unam;
	 					 //  int cos,pric,prof,qt,qtyslef,total;
	 					   id = rs.getString("Id");
	 					   fnam = rs.getString("fname");
	 					   lnam = rs.getString("lname");
	 					   unam = rs.getString("username");
	 					   sex = rs.getString("sex");
	 					   phon = rs.getString("phone");
	 					   mail =  rs.getString("email");
	 					  
	 					   tableuser.getItems().add(new AddUser(id, fnam, lnam, unam, sex,phon,mail));
	 					   
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

	
}
