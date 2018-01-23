package pams.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class StoreControl implements Initializable{
	Main main;
	LoginController lg = new LoginController();
    @FXML
    private JFXButton btnBack,btn0,btn00;
    @FXML
    private Label labfile;
    @FXML
    private Text tx,tx1,tx11,tx111;
    @FXML
    private JFXTextField search;

    @FXML
    private JFXButton addProdBtn,btnpush,btnOrder,btnScale,btnUsed,btnClean,btnSuspend;

    @FXML
    private TableView<ProdAdmin> tableProd;
/*
    @FXML
    private TableColumn<ProdAdmin, Integer> batchCol;*/
    @FXML
    private TableColumn<ProdAdmin, String> nameCol,idCol;

    @FXML
    private TableColumn<ProdAdmin, String> descrCol;

    @FXML
    private TableColumn<ProdAdmin, String> suppCol;

    @FXML
    private TableColumn<ProdAdmin, String> recCol;

    @FXML
    private TableColumn<ProdAdmin, String> exCol;

    @FXML
    private TableColumn<ProdAdmin, Integer> orCol;

    @FXML
    private TableColumn<ProdAdmin, Integer> cellCol,cell1Col;

    @FXML
    private TableColumn<ProdAdmin, Integer> qtyCol;

    @FXML
    private TableColumn<ProdAdmin, Integer> qtyLeftCol;

    @FXML
    private TableColumn<ProdAdmin, Integer> totalCol;
    @FXML
    private TableColumn<ProdAdmin, Integer> prof;

    @FXML
    private TableColumn<ProdAdmin, String> genname;
    @FXML
    private JFXButton refresh;
    @FXML
    private JFXButton updateBtn;
    @FXML
    private JFXButton ModBtn,btnpull,btnOpen;

    @FXML
    private JFXButton removBtn;

    @FXML
    private StackPane stackPop;
    @FXML
    private StackPane pane;
    static String proCode=null,proID=null;
    private WorkIndicatorDialog wd=null;
    ObservableList<Items>list = FXCollections.observableArrayList();
    ObservableList<Items>filteredlist = FXCollections.observableArrayList();
    ObservableList<ProdAdmin>searchdata;
    ObservableList<ProdAdmin> datlist;
	 List listofdat = new ArrayList();
    ConnectDB database = new ConnectDB();
    FileInputStream input,input1;
	InputStream is = null;
	FileChooser flc,flc1;
	List<String> yumo,succes,empty ;
    File selectedFile,selectedFile1 ;
    private Connection con;
    private ResultSet rs;
    private Statement st;
    private PreparedStatement prep;
  static  String pronam = null;
  boolean em,su = false;
  int s = 0;
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    		//	batchCol.setCellValueFactory(new PropertyValueFactory<>("batchs"));
    	btnSuspend.setTooltip(new Tooltip("all deleted/cleaned item are listed here"));
    	btnClean.setTooltip(new Tooltip("this will unlist expired items"));
    	btnOpen.setTooltip(new Tooltip("browse my computer"));
    	btnpull.setTooltip(new Tooltip("download item template"));
    	btnpush.setTooltip(new Tooltip("import file"));
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("prodname"));
		genname.setCellValueFactory(new PropertyValueFactory<>("genericname"));
		descrCol.setCellValueFactory(new PropertyValueFactory<>("descr"));
		suppCol.setCellValueFactory(new PropertyValueFactory<>("suppl"));
		recCol.setCellValueFactory(new PropertyValueFactory<>("received"));
		exCol.setCellValueFactory(new PropertyValueFactory<>("expire"));
		orCol.setCellValueFactory(new PropertyValueFactory<>("origprice"));
		cellCol.setCellValueFactory(new PropertyValueFactory<>("cellprice"));
		cell1Col.setCellValueFactory(new PropertyValueFactory<>("cell1price"));
		prof.setCellValueFactory(new PropertyValueFactory<>("profit"));
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
		qtyLeftCol.setCellValueFactory(new PropertyValueFactory<>("qtyleft"));
		totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
		
		fetchProduct();
		tableProd.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
					//System.out.println("You click at cell "+ tableview.getSelectionModel().getSelectedCells().get(0).getColumn());
				//proCode = tableProd.getSelectionModel().getSelectedItem().
				pronam = tableProd.getSelectionModel().getSelectedItem().getProdname();
				proID = tableProd.getSelectionModel().getSelectedItem().getId();
				int cellNum =  tableProd.getSelectionModel().getSelectedCells().get(0).getColumn();
		 if(cellNum == 10){
		//System.out.println("clik");
			
		 }else{
			 
		 }
			
			}
			
		});
search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			
			if (oldValue != null && (newValue.length() < oldValue.length())) {
				tableProd.setItems(searchdata);
		            }
		            String value = newValue.toLowerCase();
		            ObservableList<ProdAdmin> subentries = FXCollections.observableArrayList();

		            long count = tableProd.getColumns().stream().count();
		            for (int i = 0; i < tableProd.getItems().size(); i++) {
		                for (int j = 0; j < count; j++) {
		                    String entry = "" + tableProd.getColumns().get(j).getCellData(i);
		                    if (entry.toLowerCase().contains(value)) {
		                    	
		                        subentries.add(tableProd.getItems().get(i));
		                        break;
		                    }
		                }
		            }
		            tableProd.setItems(subentries);
		        });
		
		
	}
    static String getProName(){
    	return pronam;
    }
    static String getProID(){
    	return proID;
    }
    private void colorMap(){
 	   tableProd.setRowFactory(row -> new TableRow<ProdAdmin>(){
			 @Override
			    public void updateItem(ProdAdmin item, boolean empty){
				 super.updateItem(item, empty);
				 if (item == null || empty) {
			            setStyle("");
			        } else{
			        	Date dat; LocalDate nowD =  LocalDate.now(); 
			        	long daysbtn;
			        	 String date = item.getExpire();
			        	LocalDate exD = LocalDate.parse(date);
			        	
							 daysbtn = ChronoUnit.DAYS.between(nowD,exD);
							// System.out.println("ITEM:"+item.getId()+" | DAYS:"+exD+" AND "+nowD+" .DAYS BTN:"+daysbtn);
						
							 if (daysbtn > 90 ) {  if(getTableView().getSelectionModel().getSelectedItems().contains(item)){
		      	                    for(int i=0; i<getChildren().size();i++){
		      	                        ((Labeled) getChildren().get(i)).setTextFill(Color.WHITE);
		      	                      ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #31f694");
		      	                    }
		      	                }
		      	                else{
		      	                    for(int i=0; i<getChildren().size();i++){
		      	                        ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);
		      	                      ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #c4d69f");
		      	                    }
		      	                }
							 }else
							 if (daysbtn <= 90 && daysbtn > 30) {
			                //We apply now the changes in all the cells of the row
			                for(int i=0; i<getChildren().size();i++){
			                    ((Labeled) getChildren().get(i)).setTextFill(Color.BROWN);
			                    ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #d5f631");
			                }                        
			            }else
			          if (daysbtn <= 30 && daysbtn > 1) {
			                //We apply now the changes in all the cells of the row
			                for(int i=0; i<getChildren().size();i++){
			                    ((Labeled) getChildren().get(i)).setTextFill(Color.BROWN);
			                    ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #e7f631");
			                }                        
			            }else
			        if (daysbtn <= 1) {
		                //We apply now the changes in all the cells of the row
		                for(int i=0; i<getChildren().size();i++){
		                    ((Labeled) getChildren().get(i)).setTextFill(Color.WHITE);
		                    ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #f6313d");
		                }    
		              //  setText("Expired");
		            }else{
			       
	                if(getTableView().getSelectionModel().getSelectedItems().contains(item)){
	                    for(int i=0; i<getChildren().size();i++){
	                        ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);
	                        ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #31f694");
	                    }
	                }
	                else{
	                    for(int i=0; i<getChildren().size();i++){
	                        ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);
	                        ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: #f5f6f3");
	                    }
	                }
		            }
			      }
			 }
		});
    }
    @SuppressWarnings("unchecked")
	@FXML
     void fetchProduct() {
    	//tableProd.getItems().add(new ProdAdmin("Panadol","Pain killer","MSD","2017-02-03","2018-02-03",500,1000,10,10,10000,"Edit"));
    		//tableProd.refresh();
    	for(int i=0; i<tableProd.getItems().size(); i++){
    		
    		tableProd.getItems().clear();
    	}
    	wd = new WorkIndicatorDialog(null, "Loading Medicine...");
 	   wd.addTaskEndNotification(result -> {
 		  String outres = result.toString();
          // System.out.println("nomaa "+outres);
           if(outres.equals("1")){
        	   datlist = FXCollections.observableList(listofdat);
        	   tableProd.setItems(datlist);
        	   searchdata =  tableProd.getItems();
        	   //
        	   colorMap();
        	   //
           }
           wd=null;
 	   });
 		 wd.exec("fetch", inputParam -> {
	           // Thinks to do...
	           // NO ACCESS TO UI ELEMENTS!
 			try{
 				con= database.dbconnect();
 				   st= con.createStatement();
 				   String query = "SELECT * FROM products WHERE NOT comment='INACTIVE'";
 				   rs=st.executeQuery(query);
 				  listofdat.clear();
 				   while(rs.next()){
 					   String nam,genname,des,sup,rec,ex,supl,cod;
 					   double cos,pric,prof,total,batc,pric1;
 					   int qt,qtyslef;
 					//   batc = rs.getInt("batch");
 					  cod = rs.getString("code");
 					   nam = rs.getString("name");
 					  genname = rs.getString("generic_name");
 					   des = rs.getString("description");
 					   sup = rs.getString("supplier");
 					   rec = rs.getString("arrival");
 					   ex =  rs.getString("expiry");
 					   cos = rs.getDouble("cost");
 					   pric = rs.getDouble("price");
 					  pric1 = rs.getDouble("priceGen");
 					   prof = rs.getDouble("profit");
 					   qt = rs.getInt("qty");
 					   qtyslef = rs.getInt("qty_remain");
 					   total = rs.getDouble("total");
 					  pric = Math.round(pric * 100.00) / 100.00;
 					 pric1 = Math.round(pric1 * 100.00) / 100.00;
 					 cos = Math.round(cos * 100.00) / 100.00;
 					  prof = Math.round(prof * 100.00) / 100.00;
 					 total = Math.round(total * 100.00) / 100.00;
 					  listofdat.add(new ProdAdmin(0,cod,nam,genname, des, sup, rec, ex, cos, pric,pric1, qt, qtyslef, prof,total));
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

	@FXML
    void addProduct(ActionEvent event)  {
		
        	AnchorPane infopane = null;
    	try{
    		FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("AddProduct.fxml"));
			 infopane = loader.load();
    	}catch(Exception e){ 
    		e.printStackTrace();
    	}

		/*
		 * 
		 */	JFXDialogLayout content = new JFXDialogLayout();
	    	content.setHeading(new Text("Register product"));
	    	content.setBody(infopane);
	    	content.setStyle("-fx-background-color:  #2b89e0");
	    	//content.
	    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
	    	dialog.setMaxHeight(555);
	    	dialog.setMaxWidth(220);
	    	dialog.setPrefWidth(220);
	    	dialog.setPrefHeight(555);
	    	JFXButton bt = new JFXButton("Done");
	    	bt.setStyle("-fx-background-color:  #ffffff");
	    	bt.setOnAction(new EventHandler<ActionEvent>(){
	    		String prodName=null;
				@Override
				public void handle(ActionEvent arg0) {
		
		    	dialog.close();
		    	
		    	
				}
	    		
	    	});
	    
	    	content.setActions(bt);
	    	dialog.show();
	    
	
    
    }

    @SuppressWarnings("static-access")
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
    @FXML
    void removProd(ActionEvent event) {
    	String prodName=null;
    	try{
    		prodName = pronam;
    		if(prodName.isEmpty() || prodName == null){
				TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("PLEASE SELECT PRODUCT FIRST");
			       tray.setMessage("select Product on the Product table above");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
			
			
			}else{
				/*
				 * 
				 */
				JFXDialogLayout content = new JFXDialogLayout();
				 tx1.setText("Verify Item Delete");
				
				 content.setHeading(tx1);
			    	tx.setText("You are about to delete an Item \n Press delete to delete \n or cancel to abort.");
			    	content.setBody(tx);
			    	content.setStyle("-fx-background-color:  #2b89e0");
			    	
			    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
			    //	JFXButton bt = new JFXButton("COMMIT");
			    //	JFXButton bt1 = new JFXButton("CANCEL");
			    	btn00.setOnAction(new EventHandler<ActionEvent>(){
			    		String prodName=null;
						@SuppressWarnings("unchecked")
						@Override
						public void handle(ActionEvent arg0) {
							/*
							 * 
							 */
								wd = new WorkIndicatorDialog(null, "Removing Item...");
							   	 wd.addTaskEndNotification(result -> {
							   		  String outres = result.toString();
							   	   if(outres.equals("1")){
							   		TrayNotification tray = new TrayNotification();
								       tray.setNotificationType(NotificationType.SUCCESS);
								       tray.setTitle("PRODUCT REMOVED SUCCESSFULLY");
								       tray.setMessage("Removed product wont be available in the list");
								       tray.setAnimationType(AnimationType.SLIDE);
								       tray.showAndDismiss(Duration.millis(4000));
							   		fetchProduct();
							   	   } if(outres.equals("0")){
							   		TrayNotification tray = new TrayNotification();
								       tray.setNotificationType(NotificationType.WARNING);
								       tray.setTitle("PRODUCT COULD HAVE NOT BEEN REMOVED");
								       tray.setMessage("something went wrong. please retry");
								       tray.setAnimationType(AnimationType.SLIDE);
								       tray.showAndDismiss(Duration.millis(4000));
							   	   }
								  
							   	
							   	    wd=null;
							   	 });
							   		 wd.exec("fetch", inputParam -> {
										int z = 0;
									 	try{
								    		prodName = pronam;
											con= database.dbconnect();
											prep = (PreparedStatement) con.prepareStatement("UPDATE products SET comment = ? WHERE name = ?");
										//	System.out.println("ID  "+supId);
											prep.setString(1,"INACTIVE");
											 prep.setString(2,prodName);
										int out =	prep.executeUpdate();
												prep.close();
										
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
							
					    	
					    	dialog.close();
					    	
					    	
							}
			    		
			    	});
			    	btn0.setOnAction(new EventHandler<ActionEvent>(){

						@Override
						public void handle(ActionEvent arg0) {
							dialog.close();
						}
			    		
			    	});
			    	content.setActions(btn00,btn0);
			    	dialog.show();
			    	fetchProduct();
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
    void showMost(ActionEvent event) {
    	
    }
    @FXML  
    void showOrder(ActionEvent event) {


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
					fetchProduct();
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
    void modifyProd(ActionEvent event) {
    	String ToName = null;
    	try{
    		ToName = tableProd.getSelectionModel().getSelectedItem().getProdname();
    		if(!ToName.isEmpty() && !ToName.equals(null)){
    		//	main.showModProduct();;

    			AnchorPane infopane = null;
    	    	try{
    	    		FXMLLoader loader = new FXMLLoader();
    				loader.setLocation(getClass().getResource("ModProd.fxml"));
    				 infopane = loader.load();
    	    	}catch(Exception e){ 
    	    		e.printStackTrace();
    	    	}

    			/*
    			 * 
    			 */	JFXDialogLayout content = new JFXDialogLayout();
    		    	content.setHeading(new Text("Add stock in"));
    		    	content.setBody(infopane);
    		    	content.setStyle("-fx-background-color:  #2b89e0");
    		    	//content.
    		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
    		    	dialog.setMaxHeight(555);
    		    	dialog.setMaxWidth(220);
    		    	dialog.setPrefWidth(220);
    		    	dialog.setPrefHeight(555);
    		    	JFXButton bt = new JFXButton("Done");
    		    	bt.setStyle("-fx-background-color:  #ffffff");
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
    			    
    			    	dialog.close();
    			    	
    			    	
    					}
    		    		
    		    	});
    		 
    		    	content.setActions(bt);
    		    	dialog.show();
    		
    		}else{
    			TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("PLEASE SELECT PRODUCT FIRST");
			       tray.setMessage("select product on the supplier table above");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
    		}
    	}catch(Exception er){
    		
    		TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.WARNING);
		       tray.setTitle("PLEASE SELECT PRODUCT FIRST");
		       tray.setMessage("select product on the supplier table above");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
    	}
    }
    @FXML
    void updateProd(ActionEvent event) {
    	String ToName = null;
    	try{
    		ToName = tableProd.getSelectionModel().getSelectedItem().getProdname();
    	//	System.out.println("NAME:"+ToName);
    		if(!ToName.isEmpty() && !ToName.equals(null)){
    			//main.showAUpdProduct();
    			AnchorPane infopane = null;
    	    	try{
    	    		FXMLLoader loader = new FXMLLoader();
    				loader.setLocation(getClass().getResource("UpdateProd.fxml"));
    				 infopane = loader.load();
    	    	}catch(Exception e){ 
    	    		e.printStackTrace();
    	    	}

    			/*
    			 * 
    			 */	JFXDialogLayout content = new JFXDialogLayout();
    		    	content.setHeading(new Text("Edit information"));
    		    	content.setBody(infopane);
    		    	content.setStyle("-fx-background-color:  #2b89e0");
    		    	//content.
    		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
    		    	dialog.setMaxHeight(555);
    		    	dialog.setMaxWidth(220);
    		    	dialog.setPrefWidth(220);
    		    	dialog.setPrefHeight(555);
    		    	JFXButton bt = new JFXButton("Done");
    		    	bt.setStyle("-fx-background-color:  #ffffff");
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
    			    
    			    	dialog.close();
    			    	
    			    	
    					}
    		    		
    		    	});
    		  
    		    	content.setActions(bt);
    		    	dialog.show();
    		}else{
    			TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("PLEASE SELECT PRODUCT FIRST");
			       tray.setMessage("select product on the supplier table above");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
    		}
    	}catch(Exception er){
    		er.printStackTrace();
    		TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.WARNING);
		       tray.setTitle("PLEASE SELECT PRODUCT FIRST");
		       tray.setMessage("select product on the supplier table above");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
    	}
    }
	  @FXML
	    void attachFile(ActionEvent event) {
	    	flc1  = new FileChooser();
	    	selectedFile1 = flc1.showOpenDialog(null);
	    	if(selectedFile1 != null){
	    	
	    		try {
	    			labfile.setText(selectedFile1.getName());
	    			input1 = new FileInputStream(selectedFile1);
	    			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	    private void showarn(Text text){
	    	AnchorPane infopane = null;
	    
		JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text("Form uploaded with fault."));
		    	content.setBody(text);
		    	content.setStyle("-fx-background-color: #84C7D2");
		    	
		    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("Done");
		    //	JFXButton bt1 = new JFXButton("Cancel");
		    	bt.setStyle("-fx-background-color: #ffffff");
		    	bt.setOnAction(new EventHandler<ActionEvent>(){
		    		String prodName=null;
					@Override
					public void handle(ActionEvent arg0) {
						fetchProduct();
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		    	
		    	content.setActions(bt);
		    	dialog.show();
		    
	    }
	    @SuppressWarnings("unchecked")
		@FXML
	    void pushData(ActionEvent event) {

	  		String head = "PHARMACY"; String head1 = " PAMIS EXTERNAL REGISTRATION FORM";
	  		String head2 = "2017";
	  		 wd = new WorkIndicatorDialog(null, "Generating form...");
	  		 wd.addTaskEndNotification(result -> {
	  			  String outres = result.toString();
	  	        // System.out.println("nomaa "+outres);
	  	         if(outres.equals("1")){
	  	        	
	  	      	   open();
	  	      	 TrayNotification tray = new TrayNotification();
	  		     tray.setNotificationType(NotificationType.SUCCESS);
	  		     tray.setTitle("EXTERNAL REGISTRATION FORM CREATED");
	  		     tray.setMessage("you can fill the form and upload it at any time");
	  		     tray.setAnimationType(AnimationType.SLIDE);
	  		     tray.showAndDismiss(Duration.millis(4000));
	  	         }else{
	  	      	   TrayNotification tray = new TrayNotification();
	  				     tray.setNotificationType(NotificationType.ERROR);
	  				     tray.setTitle("Could not generate form");
	  				     tray.setMessage("please, retry");
	  				     tray.setAnimationType(AnimationType.SLIDE);
	  				     tray.showAndDismiss(Duration.millis(4000));	 
	  	         }
	  	         wd=null;
	  		   });
	  		  wd.exec("fetch", inputParam -> {
	  		       
	  				  boolean done =false;
	  					try{
	  						 HSSFWorkbook workbook = new HSSFWorkbook(); 
	  					      HSSFSheet spreadsheet = workbook
	  					      .createSheet("Item list");
	  					      HSSFRow row0,row,row1,row2,row3,row4,row5,row6,row7,row8;
	  					      HSSFCellStyle RotateStyle = workbook.createCellStyle();
	  					      HSSFCellStyle BoldStyle = workbook.createCellStyle();
	  					      HSSFCellStyle BoldStyle1 = workbook.createCellStyle();
	  					      RotateStyle.setRotation((short)0);
	  					      HSSFFont my_font = workbook.createFont();
	  					      HSSFFont my_font1 = workbook.createFont();
	  					     
	  					      Header header = spreadsheet.getHeader();
	  					      header.setCenter(HSSFHeader.font("Times New Roman", "Bold")+HSSFHeader.fontSize((short)9)+head+"\n"
	  					      		+head2.toUpperCase()+ " \n"+head1+".");
	  					      Footer footer = spreadsheet.getFooter();
	  					      footer.setCenter(HSSFFooter.font("Times New Roman", "Bold")+HSSFFooter.fontSize((short)9)+"*NB: use valid FORMATS.");
	  					     
	  					      spreadsheet.setColumnWidth(0, 4000);
	  					      spreadsheet.setColumnWidth(1, 4000);
	  					      spreadsheet.setColumnWidth(2, 4000);
	  					      spreadsheet.setColumnWidth(3, 4000);
	  					      spreadsheet.setColumnWidth(4, 4000);
	  					      spreadsheet.setColumnWidth(5,4000);
	  					      spreadsheet.setColumnWidth(6, 4000);
	  					      spreadsheet.setColumnWidth(7, 4000);
	  					      spreadsheet.setColumnWidth(8, 4000);
	  					      spreadsheet.setColumnWidth(9, 4000);
	  					      spreadsheet.setColumnWidth(10, 4000);
	  					      spreadsheet.setColumnWidth(11, 4000);
	  					      spreadsheet.setColumnWidth(12, 4000);
	  					      spreadsheet.setColumnWidth(13, 4000);
	  					      spreadsheet.setColumnWidth(14, 4000);
	  					      spreadsheet.setColumnWidth(15, 4000);
	  					      spreadsheet.setColumnWidth(16, 4000);
	  					      spreadsheet.setColumnWidth(17, 4000);
	  					     
	  					      
	  					      my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	  					      my_font1.setItalic(true);
	  					      String str = "ITEM LIST";
	  					      BoldStyle.setFont(my_font);
	  					      BoldStyle1.setFont(my_font1);
	  					      RotateStyle.setFont(my_font);
	  					      HSSFCell cell,cell0,cell1,cell2,cell3;
	  					      
	  					      /*
	  					       * KEY
	  					       */
	  					      row0=spreadsheet.createRow(0);
	  					         cell0 = row0.createCell(0);
	  					         cell0.setCellValue("TO ENTER VALID DATA PLEASE USE THE FOLLOWING FORMAT");
	  					         cell0.setCellStyle(BoldStyle);
	  					         
	  					         row1=spreadsheet.createRow(1);
	  					         cell0 = row1.createCell(1);
	  					         cell0.setCellValue("ITEM CODE-use number Eg: 1,2,30");
	  					         cell0.setCellStyle(BoldStyle1);
	  					         
	  					         row2=spreadsheet.createRow(2);
	  					         cell0 = row2.createCell(1);
	  					         cell0.setCellValue("COST,PRICE-use number");
	  					         cell0.setCellStyle(BoldStyle1);
	  					         
	  					         row3=spreadsheet.createRow(3);
	  					         cell0 = row3.createCell(1);
	  					         cell0.setCellValue("DATES FORMAT: YYYY/MM/DD");
	  					         cell0.setCellStyle(BoldStyle1);
	  					         
	  					         row4=spreadsheet.createRow(4);
	  					         cell0 = row4.createCell(1);
	  					         cell0.setCellValue("NB*- Fill All Details");
	  					         cell0.setCellStyle(BoldStyle1);
	  					        
	  					        
	  					 
	  					       row=spreadsheet.createRow(5);
	  					      cell = row.createCell(0);
	  					      cell.setCellValue(str);
	  					      cell=row.createCell(0);
	  					      cell.setCellValue("ITEM CODE");
	  					      cell.setCellStyle(BoldStyle);
	  					      cell=row.createCell(1);
	  					      cell.setCellValue("BRAND NAME");
	  					      cell.setCellStyle(BoldStyle);
	  					      cell=row.createCell(2);
	  					      cell.setCellValue("GENERIC NAME");
	  					      cell.setCellStyle(RotateStyle);
	  					      cell=row.createCell(3);
	  					      cell.setCellValue("DESCRIPTION");
	  					      cell.setCellStyle(RotateStyle);
	  					      cell=row.createCell(4);
	  					      cell.setCellValue("ARRIVAL DATE");
	  					      cell.setCellStyle(RotateStyle);
	  					      cell=row.createCell(5);
	  					      cell.setCellValue("EXPIRE DATE");
	  					      cell.setCellStyle(RotateStyle);
	  					      cell=row.createCell(6);
	  					      cell.setCellValue("VENDOR COST");
	  					      cell.setCellStyle(RotateStyle);
	  					      cell=row.createCell(7);
	  					      cell.setCellValue("PRICE FOR WHOLESALE");
	  					      cell.setCellStyle(RotateStyle);
	  					      cell=row.createCell(8);
	  					      cell.setCellValue("UNIT PRICE");
	  					      cell.setCellStyle(RotateStyle);
	  					      cell=row.createCell(9);
	  					      cell.setCellValue("QUANTITY ARRIVED");
	  					      cell.setCellStyle(RotateStyle);
	  					      cell=row.createCell(10);
	  					      cell.setCellValue("VENDOR NAME");
	  					      cell.setCellStyle(RotateStyle);
	  					     
	  					   
	  					      closeform();
	  						 FileOutputStream out = new FileOutputStream(
	  							      new File("item_list.xls"));
	  							      workbook.write(out);
	  							      out.close();
	  							      
	  					}catch(Exception e){
	  						
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
	    void pullData(ActionEvent event) {
		  if(selectedFile1!=null){
			  wd = new WorkIndicatorDialog(null, "Registering Item(S)...");
		 	   wd.addTaskEndNotification(result -> {
		 		  String outres = result.toString();
		          System.out.println("nomaa "+empty.size());
		 		  if(outres.equals("0")){
		 			 TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.ERROR);
				       tray.setTitle("Some of the Items are not registered");
				       tray.setMessage("they are already registered or have fault.verify this");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(5000));
		 		  }
		           if(succes.size()!=0){
		        	   TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.SUCCESS);
				       tray.setTitle("Items Registered");
				       tray.setMessage("you have all items in he list now");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(4000));
				       fetchProduct();
		           }
		           if(succes.size()!=0&&yumo.size()!=0){
		        	   Text text1 = new Text(""
		        				+"1.Item with Code"+yumo+" Do not have unique ID\n"
		        				+ ",Either this Item(s) is already registered or use ID:\n"
		        				+ "That is assigned to another Item. \n"
		        				+ "These Items are not registered, if they are new in a system, please \n"
		        				+ "provide them with new CODE. and re upload the form.\n"
		        				+ " MADE CHANGES TO CORRECT INVALIDITY AND RE-UPLOAD THIS FORM.");
		        	   showarn(text1);
		        	   fetchProduct();
		        	   /* JFXSnackbar bar = new JFXSnackbar(anchor);
		        	  
				     	bar.show("students with id: "+yumo+" Does not have unique ID",16000);
				     	log.writter("Uploaded registration form");*/
				     
		           }
		           if(succes.size()==0&&yumo.size()!=0){
				     	  TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.ERROR);
					       tray.setTitle("Registration form contains already registered ITEMS");
					       tray.setMessage("for new items, please use NEW CODE,");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(6000));
		           }
		           if(empty.size()!=0){
		        	   Text text2 = new Text(""
		        				+"1.Item with ID:"+empty+" Have incomplete information\n"
		        				+ " MADE CHANGES TO CORRECT INVALIDITY AND RE-UPLOAD THIS FORM.");
		        	   showarn(text2);
		        	   fetchProduct();
		           }
		           wd=null;
		 	   });
		 	  wd.exec("fetch", inputParam -> {
		 		  try{
		 		  int s=0;
		 		 DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	 	 			DataFormatter formatter = new DataFormatter();
					con=database.dbconnect();
		            st= con.createStatement();
		            FileInputStream input = new FileInputStream(selectedFile1);
		            POIFSFileSystem fs = new POIFSFileSystem( input );
		            HSSFWorkbook wb = new HSSFWorkbook(fs);
		            HSSFSheet sheet = wb.getSheetAt(0);
		            HSSFRow row=null;
		            Boolean k=false;
		            yumo = new ArrayList<String>();
		            succes=new ArrayList<String>();
		            empty=new ArrayList<String>();
		            
		            for(int i=6; i<=sheet.getLastRowNum(); i++){
		            	 row = sheet.getRow(i);
			        	   String ID = null,itemNam,itemGen,desc,suppl;
			        	   double cost,priceW,price; int qty;
			        	   java.sql.Date arriv,exp;
			        	   if(row.getCell(0) !=null && row.getCell(1)!=null &&
					        		row.getCell(2)!=null){
			        		   
			        		 if(row.getCell(3)==null  || row.getCell(4)==null || row.getCell(5)==null  ||
						        		row.getCell(6)==null || row.getCell(7)==null ||
						        		row.getCell(8)==null || row.getCell(9)==null  ||
						        		row.getCell(10)==null ){
			        			 System.out.println("SKIPPED2");
			        			 continue;
			        		 
			        	   }   if(row.getCell(0) !=null && row.getCell(1)!=null &&
					        		row.getCell(2)!=null && row.getCell(3)!=null  &&
					        		row.getCell(4)!=null && row.getCell(5)!=null  &&
					        		row.getCell(6)!=null && row.getCell(7)!=null &&
					        		row.getCell(8)!=null && row.getCell(9)!=null  &&
					        		row.getCell(10)!=null ){
					        		
			        		   ID = row.getCell(0).getStringCellValue();
			        		   itemNam = row.getCell(1).getStringCellValue().toUpperCase();
			        		   itemGen = row.getCell(2).getStringCellValue().toUpperCase();
			        		   desc = row.getCell(3).getStringCellValue().toUpperCase();
			        		 
			        		   java.util.Date   ar =  row.getCell(4).getDateCellValue();
			        		   java.util.Date   ex =  row.getCell(5).getDateCellValue();
			        		   arriv = new java.sql.Date(ar.getTime());
			        		   exp = new java.sql.Date(ex.getTime()); 
			        		   cost = row.getCell(6).getNumericCellValue();
			        		   priceW = row.getCell(7).getNumericCellValue();
			        		   price = row.getCell(8).getNumericCellValue();
			        		   qty = (int) row.getCell(9).getNumericCellValue();
			        		   suppl = row.getCell(10).getStringCellValue().toUpperCase();
			        		   double cos = (cost/qty);
			        		   cos = Math.round(cos * 100.0) / 100.0;
			        		   System.out.println("THE ID-"+ID);
			        		   String str ="SELECT * FROM products WHERE code='"+ID+"' AND comment='INACTIVE'";
					        	 rs=st.executeQuery(str);
					        	  if(rs.next()){
					        		  yumo.add(ID);
					        		  System.out.println("SKIPPED3");
					        		  continue;
					        		  
					        	  }else{
					        		  prep = (PreparedStatement) con.prepareStatement("INSERT INTO products(code,name,generic_name,description,arrival,expiry,gen_cost,cost,price,priceGen,wholeProfit,profit,supplier,qty,qty_remain,total,comment) VALUES("
								    	  		+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
									//  batch = 1;
									  prep.setString(1, ID);
									//  prep.setInt(2, batch);
							    	  prep.setString(2, itemNam);
							    	  prep.setString(3, itemGen);
							    	  prep.setString(4, desc);
							    	  prep.setDate(5, arriv);
							    	  prep.setDate(6, exp);
							    	  prep.setDouble(7, cost);
							    	  prep.setDouble(8, cos);
							    	  prep.setDouble(9, price);
							    	  prep.setDouble(10, priceW);
							    	  prep.setDouble(11, ((priceW*qty)-cost));
							    	  prep.setDouble(12, price-cos);
							    	  prep.setString(13, suppl);
							    	  prep.setInt(14, qty);
							    	  prep.setInt(15, qty);
									prep.setDouble(16, (price*qty));
									 prep.setString(17, "ACTIVE");
									int ou=prep.executeUpdate();
									succes.add(ID);
									prep.close();
					        	  }
			        	   }else if(row.getCell(0)==null||row.getCell(1)==null||
					        		row.getCell(2)==null||row.getCell(3)==null||
					        		row.getCell(4)==null||row.getCell(5)==null ||
					        		row.getCell(6)==null||row.getCell(7)==null||
					        		row.getCell(8)==null||row.getCell(9)==null||
					        		row.getCell(10)==null){
			        		  
			        		        empty.add(ID);
			        		        System.out.println("SKIPPED4");
			        		        continue;
			        	   }
			        	  }else{
			        		  System.out.println("SKIPPED5");
			        		  continue;
			        		 
			        	  }
		            }
		 		  
		 		 }catch(SQLException e){
		 	 			e.printStackTrace();
		 	 			s=0;
		 	 		}catch(Exception e){
		 	 			e.printStackTrace();
		 	 		}
		 		 s=1;
		 		 try {
	                  Thread.sleep(1000);
	               }	
	               catch (InterruptedException er) {
	                  er.printStackTrace();
	               }
	             
	          return new Integer(s);
	        });
		  }else{
			  TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("FILE");
		       tray.setMessage("Attach file please!");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000)); 
		  }
	  }
	  public void closeform(){

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
	@SuppressWarnings("unchecked")
	public void open(){
		 wd = new WorkIndicatorDialog(null, "");
	 	   wd.addTaskEndNotification(result -> {
	 		  String outres = result.toString();
	          // System.out.println("nomaa "+outres);
	           if(outres.equals("1")){
	        	  
	           }else{
	        	  	 
	           }
	           wd=null;
	 	   });
	 	  wd.exec("fetch", inputParam -> {
	          int s=0;
				
	 		 try {

					if ((new File("item_list.xls")).exists()) {

						Process p = Runtime
						   .getRuntime()
						   .exec("rundll32 url.dll,FileProtocolHandler item_list.xls");
						p.waitFor();
							s=1;
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
	             
	           return new Integer(s);
	           
	           
	        });
		 
	}
}
