package pams.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class MyInvContr implements Initializable {
	Main main;
	private WorkIndicatorDialog wd=null;
	 @FXML
	    private BorderPane border;
	  @FXML
	    private Label labelprint;

	    @FXML
	    private JFXButton btnPrint,btnAdjust,btnPrice,btnProfoma;

	  /*  @FXML
	    private Label laelTotal;

	    @FXML
	    private Label labelProfit;*/
	    @FXML
	    private JFXDatePicker startDate;

	    @FXML
	    private JFXDatePicker endDate;
	   
	    @FXML
	    private TableView<Inventory> tableview;

	    @FXML
	    private JFXButton btnBack,btnPayed,btnAuto,btnOrder;

	    @FXML
	    private TableColumn<Inventory, String> idcol;

	    @FXML
	    private TableColumn<Inventory, String> namecol;

	    @FXML
	    private TableColumn<Inventory, String> datecol;

	    @FXML
	    private TableColumn<Inventory, String> qtycol;

	    @FXML
	    private TableColumn<Inventory, String> qtyUsedCol;

	    @FXML
	    private TableColumn<Inventory, String> qtyRemcol;

	    @FXML
	    private TableColumn<Inventory, Boolean> remakcol;

	    @FXML
	    private TableColumn<Inventory, String> statcol;
	    LocalDate startD = null,endD = null,tempD;
	    Date start = null,end = null;
	    @FXML
	    private StackPane pane;
	   	String inv=null;
	    @FXML
	    private JFXTextField search;
	    LoginController lg = new LoginController();
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs;
		    private Statement st;
		    int toAmount = 0,toProfit=0;
		    static  String pronam = null,proID=null;
		    static int qtyCur=0,qtyL=0,qtyUse=0;
		    private PreparedStatement prep,prep1;
	    ObservableList<Items>list = FXCollections.observableArrayList();
	    ObservableList<Inventory> searchdata;
	    ObservableList<Inventory> datlist;
		 List listofdat = new ArrayList();
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1) {
	    	btnPayed.setTooltip(new Tooltip("Payed Proforma list"));
	    	
	    	fetchInventory();
	    	idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
	    	namecol.setCellValueFactory(new PropertyValueFactory<>("name"));
	    	datecol.setCellValueFactory(new PropertyValueFactory<>("date"));
	    	qtycol.setCellValueFactory(new PropertyValueFactory<>("qty"));
	    	qtyUsedCol.setCellValueFactory(new PropertyValueFactory<>("qtyused"));
	    	qtyRemcol.setCellValueFactory(new PropertyValueFactory<>("qtyremain"));
	    	remakcol.setCellValueFactory(new PropertyValueFactory<>("remak"));
	    	statcol.setCellValueFactory(new PropertyValueFactory<>("status"));
	    	//action.setCellValueFactory(new PropertyValueFactory<>("action"));
	 /*   	remakcol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Inventory,Boolean>, 
	    			ObservableValue<Boolean>>(){

						@Override
						public ObservableValue<Boolean> call(CellDataFeatures<Inventory, Boolean> arg0) {
							 return new SimpleBooleanProperty(arg0.getValue() != null);
						}
	    		
	    	});
	    	remakcol.setCellFactory(
	                new Callback<TableColumn<Inventory, Boolean>, TableCell<Inventory, Boolean>>() {
	                	 
	                    @Override
	                    public TableCell<Inventory, Boolean> call(TableColumn<Inventory, Boolean> p) {
	                        return new ButtonCell();
	                    }
	                 
	                });*/
	    	tableview.setOnMouseClicked(new EventHandler<MouseEvent>(){

				@Override
				public void handle(MouseEvent arg0) {
						//System.out.println("You click at cell "+ tableview.getSelectionModel().getSelectedCells().get(0).getColumn());
					//proCode = tableProd.getSelectionModel().getSelectedItem().
					proID = tableview.getSelectionModel().getSelectedItem().getId();
					pronam = tableview.getSelectionModel().getSelectedItem().getName();
					qtyCur = tableview.getSelectionModel().getSelectedItem().getQty();
					qtyL = tableview.getSelectionModel().getSelectedItem().getQtyremain();
					qtyUse = tableview.getSelectionModel().getSelectedItem().getQtyused();
					int cellNum =  tableview.getSelectionModel().getSelectedCells().get(0).getColumn();
			 if(cellNum == 6){
			showAdjust();
			JFXSnackbar bar = new JFXSnackbar(border);
	        	bar.show("Loss& Adjustment loading up...",3000);
			 }else{
				 
			 }
				
				}
				
			});
	search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
				
				if (oldValue != null && (newValue.length() < oldValue.length())) {
			            	tableview.setItems(searchdata);
			            }
			            String value = newValue.toLowerCase();
			            ObservableList<Inventory> subentries = FXCollections.observableArrayList();

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
/*	    private class ButtonCell extends TableCell<Inventory, Boolean>{
	    	final JFXButton cellButton = new JFXButton("rem");
	    	
	    	ButtonCell(){
	    		cellButton.setOnAction(new EventHandler<ActionEvent>(){

					@Override
					public void handle(ActionEvent event) {
						
				//	String cl =	tableview.getSelectionModel().getSelectedItem().getName();
					//System.out.println("YOU CLIKED: "+cl);
						}
					});
	    		
	    		}
	    	 //Display button if the row is not empty
	        @Override
	        protected void updateItem(Boolean t, boolean empty) {
	            super.updateItem(t, empty);
	            if(!empty){
	                setGraphic(cellButton);
	            }
	        }
	    }*/
	    @FXML  
	    void showAuto(ActionEvent event) {
	    	
	    	JFXSnackbar bar = new JFXSnackbar(border);
        	bar.show("Upgrade your application \n to unlock this awesome feature...",4000);
        	/*
        	 * Main aim here o is to make sure  i can initiate automatic ordering system to send order
        	 * to the specified vendor. in this case we will need
        	 * 1. vendor Name
        	 * 2. vendor phone for  SMS notfication
        	 * 3. vendor email for actual attachment.
        	 * If the status point to YES then we can always make sure the system sends Notifications wen 
        	 */
        	
	    }
	    @FXML  
	    void showOrder(ActionEvent event) {

	    	JFXSnackbar bar = new JFXSnackbar(border);
        	bar.show("Upgrade your application \n to unlock this awesome feature...",5000);
	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("Order.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text(""));
		    	content.setBody(infopane);
		    	content.setMaxWidth(250);
		    	content.setMaxHeight(255);
		    	content.setStyle("-fx-background-color: #2b89e0");
		    	
		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("DONE");
		    	bt.setStyle("-fx-background-color: #ffffff");
		    	//JFXButton bt1 = new JFXButton("Cancel");
		    	bt.setOnAction(new EventHandler<ActionEvent>(){
		    		String prodName=null;
					@Override
					public void handle(ActionEvent arg0) {
					/*
					 * 
					 */

						/*
						 * 
						 */
						fetchInventory();
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		   
		    	content.setActions(bt);
		    	dialog.show();
		    
			/*
			 * 
			 */
		
	    	
	    
	    
	    }
	    static String getID(){
	    	return proID;
	    }
	    static String getNam(){
	    	return pronam;
	    }
	    static int getQt(){
	    	return qtyCur;
	    }
	    static int getQtL(){
	    	return qtyL;
	    }
	    static int getQtU(){
	    	return qtyUse;
	    }
	    private void showAdjust(){

	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("Adjust.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	
	    		JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text(""));
		    	content.setBody(infopane);
		    	content.setMaxWidth(250);
		    	content.setMaxHeight(255);
		    	content.setStyle("-fx-background-color: #2b89e0");
		    	
		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("DONE");
		    	bt.setStyle("-fx-background-color: #ffffff");
		    	//JFXButton bt1 = new JFXButton("Cancel");
		    	bt.setOnAction(new EventHandler<ActionEvent>(){
		    		String prodName=null;
					@Override
					public void handle(ActionEvent arg0) {
					/*
					 * 
					 */

						/*
						 * 
						 */
						fetchInventory();
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		   
		    	content.setActions(bt);
		    	dialog.show();
		    
			/*
			 * 
			 */
		
	    	
	    
	    }
	    @FXML
	    void openAdjust(ActionEvent event) {
	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("LossAdjust.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text(""));
		    	content.setBody(infopane);
		    	
		    	content.setStyle("-fx-background-color: #2b89e0");
		    	
		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("DONE");
		    	bt.setStyle("-fx-background-color: #ffffff");
		    //	JFXButton bt1 = new JFXButton("Cancel");
		    	bt.setOnAction(new EventHandler<ActionEvent>(){
		    		String prodName=null;
					@Override
					public void handle(ActionEvent arg0) {
					/*
					 * 
					 */

						/*
						 * 
						 */
						fetchInventory();
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		    
		    	content.setActions(bt);
		    	dialog.show();
		
	    }
	    @FXML
	    void openPrice(ActionEvent event) {

	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("PriceList.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text(""));
		    	content.setBody(infopane);
		    	
		    	content.setStyle("-fx-background-color: #2b89e0");
		    	
		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("DONE");
		    	bt.setStyle("-fx-background-color: #ffffff");
		    //	JFXButton bt1 = new JFXButton("Cancel");
		    	bt.setOnAction(new EventHandler<ActionEvent>(){
		    		String prodName=null;
					@Override
					public void handle(ActionEvent arg0) {
					/*
					 * 
					 */

						/*
						 * 
						 */
						fetchInventory();
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		    
		    	content.setActions(bt);
		    	dialog.show();
		
	    
	    }
	    @FXML
	    void openPayedProfoma(ActionEvent event) {
	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("Payedproforma.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text(""));
		    	content.setBody(infopane);
		    	
		    	content.setStyle("-fx-background-color: #2b89e0");
		    	
		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("DONE");
		    	bt.setStyle("-fx-background-color: #ffffff");
		    //	JFXButton bt1 = new JFXButton("Cancel");
		    	bt.setOnAction(new EventHandler<ActionEvent>(){
		    		String prodName=null;
					@Override
					public void handle(ActionEvent arg0) {
					/*
					 * 
					 */

						/*
						 * 
						 */
						fetchInventory();
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		    
		    	content.setActions(bt);
		    	dialog.show();
		
	    
	    
	    }
	    @FXML
	    void openProfoma(ActionEvent event) {


	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("ProformaList.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text(""));
		    	content.setBody(infopane);
		    	
		    	content.setStyle("-fx-background-color: #2b89e0");
		    	
		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("DONE");
		    	bt.setStyle("-fx-background-color: #ffffff");
		    //	JFXButton bt1 = new JFXButton("Cancel");
		    	bt.setOnAction(new EventHandler<ActionEvent>(){
		    		String prodName=null;
					@Override
					public void handle(ActionEvent arg0) {
					/*
					 * 
					 */

						/*
						 * 
						 */
						fetchInventory();
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		    
		    	content.setActions(bt);
		    	dialog.show();
		
	    
	    
	    }
	    private void colorMap(){
	  	   tableview.setRowFactory(row -> new TableRow<Inventory>(){
	 			 @Override
	 			    public void updateItem(Inventory item, boolean empty){
	 				 super.updateItem(item, empty);
	 				 if (item == null || empty) {
	 			            setStyle("");
	 			        } else{
	 			        	Date dat; LocalDate nowD =  LocalDate.now(); 
	 			        	long daysbtn;
	 			        	 String stat = item.getStatus();
	 			      
	 							// System.out.println("ITEM:"+item.getId()+" | DAYS:"+exD+" AND "+nowD+" .DAYS BTN:"+daysbtn);
	 			        	 	if(stat.equals("Balanced")){
	 			        	 		 if(getTableView().getSelectionModel().getSelectedItems().contains("Balanced")){
		 		      	                    for(int i=0; i<getChildren().size();i++){
		 		      	                        ((Labeled) getChildren().get(i)).setTextFill(Color.BLUE);
		 		      	                      ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #4dd955");
		 		      	                    }
		 		      	                }
	 			        	 		 else{
		 		      	                    for(int i=0; i<getChildren().size();i++){
		 		      	                        ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);
		 		      	                      ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #c4d69f");
		 		      	                    }
		 		      	                }
	 			        	 	}else if(stat.equals("Low stock")){
	 			        	 		 for(int i=0; i<getChildren().size();i++){
	 	 			                    ((Labeled) getChildren().get(i)).setTextFill(Color.BROWN);
	 	 			                    ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #d5f631");
	 	 			                } 
	 			        	 	}else if(stat.equals("Out of stock")){
	 			        	 	  for(int i=0; i<getChildren().size();i++){
	 		 		                    ((Labeled) getChildren().get(i)).setTextFill(Color.WHITE);
	 		 		                    ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #f6313d");
	 		 		                }    
	 			        	 	}
	 			        	 	else{/*
	 			       
	 	                if(getTableView().getSelectionModel().getSelectedItems().contains(item)){
	 	                    for(int i=0; i<getChildren().size();i++){
	 	                        ((Labeled) getChildren().get(i)).setTextFill(Color.WHITE);
	 	                        ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #99f19d");
	 	                    }
	 	                }
	 	                else{
	 	                    for(int i=0; i<getChildren().size();i++){
	 	                        ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);
	 	                        ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #ffffff");
	 	                    }
	 	                }
	 		            */}
	 			      }
	 			 }
	 		});
	     }
	    @SuppressWarnings("unchecked")
		private void fetchInventory() {
	    	
	    	for(int i=0; i<tableview.getItems().size(); i++){
	    		
	    		tableview.getItems().clear();
	    	}
	    		
	    		wd = new WorkIndicatorDialog(null, "Loading Inventory(S)...");
	 	 	   wd.addTaskEndNotification(result -> {
	 	 		  String outres = result.toString();
	 	          // System.out.println("nomaa "+outres);
	 	           if(outres.equals("1")){
	 	        	  
						   datlist = FXCollections.observableList(listofdat);
			        	   tableview.setItems(datlist);
			        	   searchdata =  tableview.getItems();
			        	   
			        	   colorMap();
	 	           }
	 	           wd=null;
	 	 	   });
	 	 		 wd.exec("fetch", inputParam -> {
	 		           // Thinks to do...
	 		           // NO ACCESS TO UI ELEMENTS!
	 	 		 	try{
	 					con= database.dbconnect();
	 					   st= con.createStatement();
	 					   String query = "SELECT * FROM products";
	 					   rs=st.executeQuery(query);
	 					  listofdat.clear();
	 					   while(rs.next()){
	 						
	 						 double totamo = 0,totpro = 0;
	 						   String id,date,name,dat;
	 						   int qtyL,qtyIs,qty,totalam,balance;
	 						   id = rs.getString("code");
	 						   date = rs.getString("arrival");
	 						   name = rs.getString("name");
	 						   balance = rs.getInt("minQty");
	 						   qtyL = rs.getInt("qty_remain");
	 						   qty = rs.getInt("qty");
	 						  if(qtyL <= balance && qtyL >=1){
	 						  listofdat.add(new Inventory(id,name,date,qty,qty-qtyL,qtyL,"edit","Low stock",""));
	 						  }else if(qtyL <= balance && qtyL <=0){
		 						  listofdat.add(new Inventory(id,name,date,qty,qty-qtyL,qtyL,"edit","Out of stock",""));
		 						  }else if(qtyL > balance ){
			 						  listofdat.add(new Inventory(id,name,date,qty,qty-qtyL,qtyL,"edit","Balanced",""));
		 						  }
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
	    @SuppressWarnings("unchecked")
		@FXML
	    void showInv(ActionEvent event) {

	    	
	    	for(int i=0; i<tableview.getItems().size(); i++){
	    		
	    		tableview.getItems().clear();
	    	}
	    		
	    		wd = new WorkIndicatorDialog(null, "Loading Inventory(S)...");
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
	 	 	  startD = null;endD = null;tempD=null;
	 	   	  start = null;end = null;
	 	   	  try{
	 	   		 startD = startDate.getValue();
	 	   		 endD = endDate.getValue();
	 	   		 tempD = LocalDate.from(startD);
	 	   		 
	 	   		 start = Date.valueOf(startD);
	 	   		 end = Date.valueOf(endD);
	 	   		 long spanyear = tempD.until(endD, ChronoUnit.YEARS);
	 	   		 	tempD = tempD.plusYears(spanyear);
	 	   		 long spanmonth = tempD.until(endD, ChronoUnit.MONTHS);
	 	   		 tempD = tempD.plusMonths(spanmonth);
	 	   		 long spandays = tempD.until(endD, ChronoUnit.DAYS);
	 	   		 tempD = tempD.plusDays(spandays);
	 	   		}catch(Exception e){
	 	   		e.printStackTrace();
	 	   	TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.WARNING);
		       tray.setTitle("CHOOSE START & END DATE");
		       tray.setMessage("this will specify the range you provide");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(5000));
	 	   	  	}
	 	 		 wd.exec("fetch", inputParam -> {
	 		           // Thinks to do...
	 		           // NO ACCESS TO UI ELEMENTS!
	 	 			// System.out.println(startD +"  "+endD);
	 	 		 	try{
	 					con= database.dbconnect();
	 					   st= con.createStatement();
	 					   String query = "SELECT * FROM products WHERE arrival BETWEEN '"+start+"' AND '"+end+"'";
	 					   rs=st.executeQuery(query);
	 					  listofdat.clear();
	 					   while(rs.next()){
	 						
	 						 double totamo = 0,totpro = 0;
	 						   String id,date,name,dat;
	 						   int qtyL,qtyIs,qty,totalam;
	 						   id = rs.getString("code");
	 						   date = rs.getString("arrival");
	 						   name = rs.getString("name");
	 						   
	 						   qtyL = rs.getInt("qty_remain");
	 						   qty = rs.getInt("qty");
	 						  
	 						  listofdat.add(new Inventory(id,name,date,qty,qty-qtyL,qtyL,"!","-",""));
		 				
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
		@SuppressWarnings("unchecked")
		@FXML
	    void PrintInv(ActionEvent event) {

			close();
	    	
		   	 wd = new WorkIndicatorDialog(null, "Creating list...");
		   	   wd.addTaskEndNotification(result -> {
		   		  String outres = result.toString();
		         	
		            if(outres.equals("1")){
		         
		            	open();
		            }else{
		         	   
		            }
		            wd=null;
		   	   });
		   	  wd.exec("fetch", inputParam -> {
		            int k=0;
		   		  boolean done =false;
		   			try{
		   				 HSSFWorkbook workbook = new HSSFWorkbook(); 
		   			      HSSFSheet spreadsheet = workbook
		   			      .createSheet("inventory");
		   			      HSSFRow row,rowH,rowH1,rowH2,rowH3,rowH4;//=spreadsheet.createRow(0);
		   			      HSSFCellStyle RotateStyle = workbook.createCellStyle();
		   			      HSSFCellStyle BoldStyle = workbook.createCellStyle();
		   			      RotateStyle.setRotation((short)90);
		   			      HSSFFont my_font = workbook.createFont();
		   			     st = con.createStatement();
		   			     String sql = "SELECT * FROM business_details";
		   			     rs = st.executeQuery(sql);
		   			     String busNam=null; String busAd=null; String busEm=null;
		   			     String busweb=null; String busph=null;
		   			     if(rs.next()){
		   			    	 busNam = rs.getString("name");
		   			    	 busAd =  rs.getString("address");
		   			    	 busEm = rs.getString("email");
		   			    	 busweb = rs.getString("website");
		   			    	 busph = rs.getString("phone");
		   			    	 
		   			     }
		   			     rs.close(); st.close(); con.close();
		   			      Header header = spreadsheet.getHeader();
		   			      header.setCenter(HSSFHeader.font("Times New Roman", "Bold")+HSSFHeader.fontSize((short)14)+"INVENTORY LIST");
		   			      Footer footer = spreadsheet.getFooter();
		   			      footer.setCenter(HSSFFooter.font("Times New Roman", "Bold")+HSSFFooter.fontSize((short)9)+"");
		   			      spreadsheet.setColumnWidth(0, 3000);
		   			      spreadsheet.setColumnWidth(1, 4000);
		   			      spreadsheet.setColumnWidth(2, 4000);
		   			      spreadsheet.setColumnWidth(3, 2000);
		   			      spreadsheet.setColumnWidth(4, 4000);
		   			      spreadsheet.setColumnWidth(5, 4000);
		   			      spreadsheet.setColumnWidth(6, 4000);
		   			      spreadsheet.setColumnWidth(7, 4000);
		   			      spreadsheet.setColumnWidth(8, 4000);
		   			      spreadsheet.setColumnWidth(9, 4000);
		   			       spreadsheet.setColumnWidth(10, 4000);
		   			      /*
		   			       * 
		   			       */

		   			  /*  //FileInputStream obtains input bytes from the image file
		   			    InputStream inputStream = new FileInputStream("images/Pamis.png");
		   			    //Get the contents of an InputStream as a byte[].
		   			    byte[] bytes = IOUtils.toByteArray(inputStream);
		   			    //Adds a picture to the workbook
		   			    int pictureIdx = workbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_PNG);
		   			    //close the input stream
		   			    inputStream.close();

		   			    //Returns an object that handles instantiating concrete classes
		   			    CreationHelper helper = workbook.getCreationHelper();

		   			    //Creates the top-level drawing patriarch.
		   			    Drawing drawing = spreadsheet.createDrawingPatriarch();

		   			    //Create an anchor that is attached to the worksheet
		   			    ClientAnchor anchor = helper.createClientAnchor();
		   			    //set top-left corner for the image
		   			    anchor.setCol1(1);
		   			    anchor.setRow1(2);

		   			    //Creates a picture
		   			    Picture pict = drawing.createPicture(anchor, pictureIdx);
		   			    //Reset the image to the original size
		   			    pict.resize();
		   			       
		   			        * 
		   			        
		   			       */
		   			       
		   			      
		   			      my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   			      BoldStyle.setFont(my_font);
		   			      RotateStyle.setFont(my_font);
		   			      HSSFCell cell,cellH;
		   			      
		   			      rowH = spreadsheet.createRow(0);
		   			      cellH = rowH.createCell(4);
		   			      cellH.setCellValue(busNam);
		   			      cellH.setCellStyle(BoldStyle);
		   			   rowH1 = spreadsheet.createRow(1);
		   			      cellH = rowH1.createCell(4);
		   			      cellH.setCellValue(busAd);
		   			      cellH.setCellStyle(BoldStyle);
		   			   rowH2 = spreadsheet.createRow(2);
		   			      cellH = rowH2.createCell(4);
		   			      cellH.setCellValue(busEm);
		   			      cellH.setCellStyle(BoldStyle);
		   			   rowH3 = spreadsheet.createRow(3);
		   			      cellH = rowH3.createCell(4);
		   			      cellH.setCellValue(busph);
		   			      cellH.setCellStyle(BoldStyle);
		   			   rowH4 = spreadsheet.createRow(4);
		   			      cellH = rowH4.createCell(4);
		   			      cellH.setCellValue(busweb);
		   			      cellH.setCellStyle(BoldStyle);
		   			      
		   			   row = spreadsheet.createRow(6);
		   			      cell=row.createCell(0);
		   			      cell.setCellValue("ITEM ID");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(1);
		   			      cell.setCellValue("ITEM NAME");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(2);
		   			      cell.setCellValue("ARRIVAL DATE");
					      cell.setCellStyle(BoldStyle);
					      cell=row.createCell(3);
		   			      cell.setCellValue("INIT. QUANTITY");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(4);
		   			      cell.setCellValue("QUANTITY ISSUED");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(5);
		   			      cell.setCellValue("QUANTITY REMAIN");
		   			      cell.setCellStyle(BoldStyle);
		   		
		   			   
		   			      int i=7; int tota=0; int totp=0;
		   			      for(int j=0;j<tableview.getItems().size(); j++){
		   		           	 
		   		           	  
		   		          int qt=tableview.getItems().get(j).getQty();
		   		       int qtL=tableview.getItems().get(j).getQtyremain();
		   		          String id=tableview.getItems().get(j).getId();
		   		          int qtIs=tableview.getItems().get(j).getQtyused();
		   		          String dat=tableview.getItems().get(j).getDate();
		   		          String nam=tableview.getItems().get(j).getName();
		   		         
		   		     		   	 row=spreadsheet.createRow(i);
		   		    	         cell=row.createCell(0);
		   		    	         cell.setCellValue(id);
		   		    	         cell=row.createCell(1); 
		   		    	        
		   		    	         cell.setCellValue(nam);
		   		    	         cell=row.createCell(2);
		   		    	         cell.setCellValue(dat);
		   		    	         cell.setCellStyle(BoldStyle);
		   		    	         cell=row.createCell(3);
		   		    	         cell.setCellValue(qt);
		   		    	         cell=row.createCell(4);
				    	         cell.setCellValue(qtIs);
				    	         cell=row.createCell(5);
		   		    	         cell.setCellValue(qtL);
		   		    	    
		   		           	 i++;	
		   							
		   							}
		   			/*   row = spreadsheet.createRow(i+2);
		   			      cell=row.createCell(6);
		   			      cell.setCellValue("TOTAL AMOUNTS");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(7);
		   			      cell.setCellValue("TOTAL PROFIT");
		   			      cell.setCellStyle(BoldStyle);
		   			   row = spreadsheet.createRow(i+3);
		   			      cell=row.createCell(6);
		   			      cell.setCellValue(tota);
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(7);
		   			      cell.setCellValue(totp);
		   			      cell.setCellStyle(BoldStyle);*/
		   				
		   				 FileOutputStream out = new FileOutputStream(
		   					      new File("inventory.xls"));
		   					      workbook.write(out);
		   					      out.close();
		   					      k=1;
		   					      
		   			}catch(Exception e){
		   				
		   				}
		   	
		                try {
		                   Thread.sleep(1000);
		                }	
		                catch (InterruptedException er) {
		                   er.printStackTrace();
		                }
		              
		            return new Integer(k);
		            
		            
		         });
		
	    }
		@SuppressWarnings("unchecked")
		public void open(){
			
			 wd = new WorkIndicatorDialog(null, "Opening up list...");
		 	   wd.addTaskEndNotification(result -> {
		 		  String outres = result.toString();
		          // System.out.println("nomaa "+outres);
		           if(outres.equals("1")){
		        	  
		           }else{
		        	  	 
		           }
		           wd=null;
		 	   });
		 	  wd.exec("fetch", inputParam -> {
		          
				int	j=0;
		 		 try {

						if ((new File("inventory.xls")).exists()) {

							Process p = Runtime
							   .getRuntime()
							   .exec("rundll32 url.dll,FileProtocolHandler inventory.xls");
							p.waitFor();
								j=1;
						} else {

							JOptionPane.showMessageDialog(null, "File does not exist");

						}

						

				  	  } catch (Exception ex) {
						ex.printStackTrace();
					  }
					
		               try {
		                  Thread.sleep(1000);
		               }	
		               catch (InterruptedException er) {
		                  er.printStackTrace();
		               }
		             
		           return new Integer(j);
		           
		           
		        });
			 
		}
	   public void close(){

			  try {

					

						Process pk = Runtime
						   .getRuntime()
						   .exec("cmd /c taskkill /f /im excel.exe");
						pk.waitFor();
							
						 Thread.sleep(100);
					

			  	  } catch (Exception ex) {
					ex.printStackTrace();
				  }
		}
		  @FXML
		    void removInv(ActionEvent event) {


		    	try{
		    		inv = pronam;
		    		if(inv.isEmpty() || inv == null){
						TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.WARNING);
					       tray.setTitle("PLEASE SELECT DATA FIRST");
					       tray.setMessage("select Product data on the table above");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(4000));
					
					
					}else{
						/*
						 * 
						 */	JFXDialogLayout content = new JFXDialogLayout();
					    	content.setHeading(new Text("Confirmation"));
					    	content.setBody(new Text("This action will remove selected\n"
					    			+ "Product data in the inventory data\n"
					    			+ "Press COMMIT to commit this action\n"
					    			+ "OR CANCEL to quit\n"));
					    	
					    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.CENTER);
					    	JFXButton bt = new JFXButton("COMMIT");
					    	JFXButton bt1 = new JFXButton("CANCEL");
					    	bt.setOnAction(new EventHandler<ActionEvent>(){
					    		String prodName=null;
								@SuppressWarnings("unchecked")
								@Override
								public void handle(ActionEvent arg0) {
									
									wd = new WorkIndicatorDialog(null, "Removing Item...");
								   	 wd.addTaskEndNotification(result -> {
								   		  String outres = result.toString();
								   		 if(outres.equals("1")){
								   			TrayNotification tray = new TrayNotification();
										       tray.setNotificationType(NotificationType.SUCCESS);
										       tray.setTitle("PRODUCT DATA REMOVED SUCCESSFULLY");
										       tray.setMessage("Removed data wont be available in the inventory list");
										       tray.setAnimationType(AnimationType.SLIDE);
										       tray.showAndDismiss(Duration.millis(4000));
										       fetchInventory();
								      	   }if(outres.equals("0")){
								      		 TrayNotification tray = new TrayNotification();
										       tray.setNotificationType(NotificationType.WARNING);
										       tray.setTitle("PRODUCT COULD'NT GET REMOVED");
										       tray.setMessage("something went wrong. please retry");
										       tray.setAnimationType(AnimationType.SLIDE);
										       tray.showAndDismiss(Duration.millis(4000));
								      	   }if(outres.equals("-1")){
								      		   
								      	   }
								      	
								   	    wd=null;
								   	 });
								   		 wd.exec("fetch", inputParam -> {
											int z = 0;
											/*
											 * 
											 */

									  
									    	try{
									    		prodName = pronam;
												con= database.dbconnect();
												st= con.createStatement();
												String query = "DELETE  FROM sales_order  WHERE invoice_no='"+inv+"'";
												
											int out =	st.executeUpdate(query);
													st.close();
											
												if(out > 0){	
													z=1;
												}else{
													z=0;
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
								   	       
								   	          
								   	        return new Integer(z);
								   	        
								   	        
								   	     });
									
						
									/*
									 * 
									 */
						    	fetchInventory();
						    	dialog.close();
						    	
						    	
								}
					    		
					    	});
					    	bt1.setOnAction(new EventHandler<ActionEvent>(){

								@Override
								public void handle(ActionEvent arg0) {
									dialog.close();
								}
					    		
					    	});
					    	content.setActions(bt,bt1);
					    	dialog.show();
					    	
						/*
						 * 
						 */
					}
		    	}catch(NullPointerException nul){
		    		TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.WARNING);
				       tray.setTitle("PLEASE SELECT PRODUCT FIRST");
				       tray.setMessage("select product on the product table above");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(4000));
		    	}
		    
		    
		  }
		 @FXML
		    void goBack(ActionEvent event) throws IOException{
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
