package pams.view;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class PayedControll  implements Initializable {

    @FXML
    private TableView<Profoma> tableview;

    @FXML
    private TableColumn<Profoma, String> idCol;

    @FXML
    private TableColumn<Profoma, String> dateCol;

    @FXML
    private TableColumn<Profoma, String> amountCol;

    @FXML
    private TableColumn<Profoma,String> nameCol;

    @FXML
    private TableColumn<Profoma, String> addressCol;

    @FXML
    private TableColumn<Profoma, String> phoneCol;

    @FXML
    private JFXTextField search;

    @FXML
    private JFXButton btnprintPurchase;

    @FXML
    private JFXButton btnCheck;

    @FXML
    private StackPane pane;

    @FXML
    private JFXTextField amounti;
    ConnectDB database = new ConnectDB();
	 private Connection con;
	    private ResultSet rs,rs1,rsT,rsC,rs4;
	    private Statement st,st1,stC,stT,st4;
	    private PreparedStatement prep;
		private WorkIndicatorDialog wd=null;
	    ObservableList<Profoma> searchdata;
	    ObservableList<Profoma> datlist;
		 List listofdat = new ArrayList();
 static String id,nam,addr,phon;
 String invoId;
 double TempTot;
 String cuName,cuAddr,cuPhon;
    @FXML
    void printPurchase(ActionEvent event) {

    }

    @SuppressWarnings("unchecked")
	@FXML
    void showAmount(ActionEvent event) {

    	wd = new WorkIndicatorDialog(null, "");
      	 wd.addTaskEndNotification(result -> {
      		  String outres = result.toString();
      		  if(outres.equals("1")){
      			JFXSnackbar bar = new JFXSnackbar(pane);
      	      	bar.show("You deleted all payed proforma...",3000);
      		  }if(outres.equals("0")){
      			JFXSnackbar bar = new JFXSnackbar(pane);
      	      	bar.show("Couldnt Delete Data, retry...",3000);
      		  }
   	  
      	
      	    wd=null;
      	 });
      		 wd.exec("fetch", inputParam -> {
   			int z = 0;
   						try{
   							con= database.dbconnect();
   						 prep = (PreparedStatement) con.prepareStatement("DELETE FROM proforma_list WHERE status= ?"
					    	  		+ "");
						    prep.setString(1, "PAYED");
						    boolean out = prep.execute();
						    if(out){
						    	z=1;
						    }else{
						    	z=0;
						    }
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

    @FXML
    void showCheck(ActionEvent event) {

    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		fetchAll();
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
  	nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
  	amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
  	dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
  	addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
  	phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
   	tableview.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
					//System.out.println("You click at cell "+ tableview.getSelectionModel().getSelectedCells().get(0).getColumn());
				//proCode = tableProd.getSelectionModel().getSelectedItem().
				id = tableview.getSelectionModel().getSelectedItem().getId();
				nam = tableview.getSelectionModel().getSelectedItem().getName();
				addr = tableview.getSelectionModel().getSelectedItem().getAddress();
				phon = tableview.getSelectionModel().getSelectedItem().getPhone();
				
				int cellNum =  tableview.getSelectionModel().getSelectedCells().get(0).getColumn();
		 if(cellNum == 6){
		//showAdjust();
		JFXSnackbar bar = new JFXSnackbar(pane);
      	bar.show("Converting into sale...",3000);
		 }else{
			 
		 }
			
			}
			
		});
	search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			
			if (oldValue != null && (newValue.length() < oldValue.length())) {
		            	tableview.setItems(searchdata);
		            }
		            String value = newValue.toLowerCase();
		            ObservableList<Profoma> subentries = FXCollections.observableArrayList();

		            long count = tableview.getColumns().stream().count();
		            for (int i = 0; i < tableview.getItems().size(); i++) {
		                for (int j = 0; j < count; j++) {
		                    String entry = "" + tableview.getColumns().get(j).getCellData(i);
		                    if (entry.toLowerCase().contains(value)) {
		                    	
		                        subentries.add(tableview.getItems().get(i));
		                        break;
		                    }
		                }
		            }
		            tableview.setItems(subentries);
		        });
	}
	  @SuppressWarnings("unchecked")
	private void fetchAll() {
	    	for(int i=0; i<tableview.getItems().size(); i++){
	    		
	    		tableview.getItems().clear();
	    	}
	    		
	    		wd = new WorkIndicatorDialog(null, "");
	 	 	   wd.addTaskEndNotification(result -> {
	 	 		  String outres = result.toString();
	 	          // System.out.println("nomaa "+outres);
	 	           if(outres.equals("1")){
	 	        	  
						   datlist = FXCollections.observableList(listofdat);
			        	   tableview.setItems(datlist);
			        	   searchdata =  tableview.getItems();
	 	           }
	 	           wd=null;
	 	 	   });
	 	 		 wd.exec("fetch", inputParam -> {
	 		           // Thinks to do...
	 		           // NO ACCESS TO UI ELEMENTS!
	 	 		 	try{
	 					con= database.dbconnect();
	 					   st= con.createStatement();
	 					   String query = "SELECT invoice,SUM(amount) AS 'amount',date,customer,address,phone FROM proforma_list"
	 					   		+ " WHERE status='PAYED' "
	 					   		+ "GROUP BY invoice";
	 					   rs=st.executeQuery(query);
	 					  listofdat.clear();
	 					   while(rs.next()){
	 						   double amou;
	 						   String id,name,inv,dat,cust,addr,phn;
	 						//   id = rs.getString("i");
	 						   name = rs.getString("customer");
	 						   inv =  rs.getString("invoice");
	 						   dat =  rs.getString("date");
	 						   amou = rs.getDouble("amount");
	 						   addr =  rs.getString("address");
	 							phn =  rs.getString("phone");
	 						  
	 						  
	 						  listofdat.add(new Profoma(inv,dat,amou,name,addr,phn));
		 				
	 					   }
	 					  st.close(); con.close();
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
