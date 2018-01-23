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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class SaleTrackControl implements Initializable {
	Main main;
	private WorkIndicatorDialog wd=null;
	 @FXML
	    private BorderPane border;
	  @FXML
	    private Label labelprint;

	    @FXML
	    private JFXButton btnPrint;

	  /*  @FXML
	    private Label laelTotal;

	    @FXML
	    private Label labelProfit;*/
	    @FXML
	    private JFXDatePicker startDate;

	    @FXML
	    private JFXDatePicker endDate;
	   
	    @FXML
	    private TableView<SaleTrack> tableview;

	    @FXML
	    private JFXButton btnBack,btnsearch;

	    @FXML
	    private TableColumn<SaleTrack, String> idcol;

	    @FXML
	    private TableColumn<SaleTrack, String> namecol;
	    @FXML
	    private TableColumn<SaleTrack, String> item;

	    @FXML
	    private TableColumn<SaleTrack, String> datecol;

	    @FXML
	    private TableColumn<SaleTrack, String> qtycol;

	    @FXML
	    private TableColumn<SaleTrack, String> qtyUsedCol;

	    @FXML
	    private TableColumn<SaleTrack, Double> qtyRemcol;

	    @FXML
	    private TableColumn<SaleTrack, String> typcol;
	    	
	    @FXML
	    private TableColumn<SaleTrack, Double> remakcol;
	    @FXML
	    private TableColumn<SaleTrack, String> statcol;
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
	    ObservableList<SaleTrack> searchdata;
	    ObservableList<SaleTrack> datlist;
		 List listofdat = new ArrayList();
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1) {
	    	btnPrint.setTooltip(new Tooltip("Print/save this report"));
	    	
	    	fetchInventory();
	    	idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
	    	namecol.setCellValueFactory(new PropertyValueFactory<>("name"));
	    	datecol.setCellValueFactory(new PropertyValueFactory<>("date"));
	    	item.setCellValueFactory(new PropertyValueFactory<>("item"));
	    	typcol.setCellValueFactory(new PropertyValueFactory<>("typ"));
	    	qtyUsedCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
	    	qtyRemcol.setCellValueFactory(new PropertyValueFactory<>("pric"));
	    	remakcol.setCellValueFactory(new PropertyValueFactory<>("amoun"));
	    	statcol.setCellValueFactory(new PropertyValueFactory<>("custom"));
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
				public void handle(MouseEvent arg0) {/*
						//System.out.println("You click at cell "+ tableview.getSelectionModel().getSelectedCells().get(0).getColumn());
					//proCode = tableProd.getSelectionModel().getSelectedItem().
					proID = tableview.getSelectionModel().getSelectedItem().getId();
					pronam = tableview.getSelectionModel().getSelectedItem().getName();
					qtyCur = tableview.getSelectionModel().getSelectedItem().getQty();
					qtyL = tableview.getSelectionModel().getSelectedItem().getQtyremain();
					qtyUse = tableview.getSelectionModel().getSelectedItem().getQtyused();
					int cellNum =  tableview.getSelectionModel().getSelectedCells().get(0).getColumn();
			 if(cellNum == 6){
			//showAdjust();
			JFXSnackbar bar = new JFXSnackbar(border);
	        	bar.show("Loss& Adjustment loading up...",3000);
			 }else{
				 
			 }
				
				*/}
				
			});
	search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
				
				if (oldValue != null && (newValue.length() < oldValue.length())) {
			            	tableview.setItems(searchdata);
			            }
			            String value = newValue.toLowerCase();
			            ObservableList<SaleTrack> subentries = FXCollections.observableArrayList();

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
	   
	    
	  
	    private void colorMap(){
	  	   tableview.setRowFactory(row -> new TableRow<SaleTrack>(){
	 			 @Override
	 			    public void updateItem(SaleTrack item, boolean empty){
	 				 super.updateItem(item, empty);
	 				 if (item == null || empty) {
	 			            setStyle("");
	 			        } else{
	 			        	Date dat; LocalDate nowD =  LocalDate.now(); 
	 			        	long daysbtn;
	 			        	 String stat = item.getType();
	 			      
	 							// System.out.println("ITEM:"+item.getId()+" | DAYS:"+exD+" AND "+nowD+" .DAYS BTN:"+daysbtn);
	 			        	 	if(stat.equals("CASH")){
	 			        	 		 if(getTableView().getSelectionModel().getSelectedItems().contains("CASH")){
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
	 			        	 	}else if(stat.equals("PROFORMA")){
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
	    		
	    		wd = new WorkIndicatorDialog(null, "");
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
	 					   String query = "SELECT sal.invoice_no AS 'receipt', pro.name AS 'item',sal.cashier AS 'name',sal.date,sal.type,"
	 					   		+ "salOrd.qty,salOrd.price,salOrd.amount,sal.customer_name FROM sales sal,products pro INNER JOIN "
	 					   		+ "sales_order salOrd WHERE sal.invoice_no = salOrd.invoice_no AND pro.code=salOrd.product_code ORDER BY sal.date ASC LIMIT 0,50;";
	 					   rs=st.executeQuery(query);
	 					  listofdat.clear();
	 					   while(rs.next()){
		 						
		 						 double pris = 0,amo = 0;
		 						   String id,itm,date,name,dat,typ,cname;
		 						   int qtyIs,qty;
		 						   id = rs.getString("receipt");
		 						   date = rs.getString("date");
		 						  itm = rs.getString("item");
		 						   name = rs.getString("name");
		 						   typ = rs.getString("type");
		 						   pris = rs.getDouble("price");
		 						   qty = rs.getInt("qty");
		 						  amo = rs.getDouble("amount");
		 						 cname = rs.getString("customer_name");
		 						//System.out.println(id+" "+itm+" "+name+" "+date+" "+typ+" "+pris+" "+amo);
		 						  listofdat.add(new SaleTrack(id,itm,name,date,typ,qty,pris,amo,cname));
		 						
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
	 					   String query = "SELECT sal.invoice_no AS 'receipt',pro.name AS 'item',sal.cashier AS 'name',sal.date,sal.type,"
	 					   		+ "salOrd.qty,salOrd.price,salOrd.amount,sal.customer_name FROM sales sal,products pro INNER JOIN "
	 					   		+ "sales_order salOrd WHERE sal.date BETWEEN '"+start+"' AND '"+end+"' AND "
	 					   				+ " sal.invoice_no = salOrd.invoice_no AND pro.code=salOrd.product_code ORDER BY sal.date ASC;";
	 					   		
	 					   rs=st.executeQuery(query);
	 					  listofdat.clear();
	 					   while(rs.next()){
		 						
		 						 double pris = 0,amo = 0;
		 						   String id,itm,date,name,dat,typ,cname;
		 						   int qtyIs,qty;
		 						   id = rs.getString("receipt");
		 						   date = rs.getString("date");
		 						  itm = rs.getString("item");
		 						   name = rs.getString("name");
		 						   typ = rs.getString("type");
		 						   pris = rs.getDouble("price");
		 						   qty = rs.getInt("qty");
		 						  amo = rs.getDouble("amount");
		 						 cname = rs.getString("customer_name");
		 						
		 						  listofdat.add(new SaleTrack(id,itm,name,date,typ,qty,pris,amo,cname));
		 						
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
		   			      .createSheet("SaleReport");
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
		   			      header.setCenter(HSSFHeader.font("Times New Roman", "Bold")+HSSFHeader.fontSize((short)14)+"SALE REPORT");
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
		   			      cell.setCellValue("RECEIPT No.");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(1);
		   			      cell.setCellValue("ITEM NAME");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(2);
		   			      cell.setCellValue("ITEM SOLD BY");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(3);
		   			      cell.setCellValue("DATE");
					      cell.setCellStyle(BoldStyle);
					      cell=row.createCell(4);
		   			      cell.setCellValue("TYPE OF SALE");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(5);
		   			      cell.setCellValue("QUANTITY ISSUED");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(6);
		   			      cell.setCellValue("PRICE CHARGED");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(7);
		   			      cell.setCellValue("AMOUNT");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(8);
		   			      cell.setCellValue("SOLD TO");
		   			      cell.setCellStyle(BoldStyle);
		   		
		   			   
		   			      int i=8; int tota=0; int totp=0;
		   			      for(int j=0;j<tableview.getItems().size(); j++){
		   		           	 
		   		           	  
		   		          int qt=tableview.getItems().get(j).getQty();
		   		       double am=tableview.getItems().get(j).getAmount();
		   		    double pri=tableview.getItems().get(j).getPrice();
		   		          String id=tableview.getItems().get(j).getId();
		   		       String itm=tableview.getItems().get(j).getItem();
		   		          String dat=tableview.getItems().get(j).getDate();
		   		          String nam=tableview.getItems().get(j).getName();
		   		       String cnam=tableview.getItems().get(j).getCustom();
		   		    String ty=tableview.getItems().get(j).getType();
		   		         
		   		     		   	 row=spreadsheet.createRow(i);
		   		    	         cell=row.createCell(0);
		   		    	         cell.setCellValue(id);
		   		    	         cell=row.createCell(1); 
		   		    	        
		   		    	         cell.setCellValue(itm);
		   		    	         cell=row.createCell(2);
		   		    	         cell.setCellValue(nam);
		   		    	         cell.setCellStyle(BoldStyle);
		   		    	         cell=row.createCell(3);
		   		    	         cell.setCellValue(dat);
		   		    	         cell=row.createCell(4);
				    	         cell.setCellValue(ty);
				    	         cell=row.createCell(5);
		   		    	         cell.setCellValue(qt);
		   		    	         cell=row.createCell(6);
		   		    	         cell.setCellValue(pri);
		   		    	         cell=row.createCell(7);
		   		    	         cell.setCellValue(am);
		   		    	      cell=row.createCell(8);
		   		    	         cell.setCellValue(cnam);
		   		    	    
		   		           	 i++;	
		   							
		   							}
		   			
		   				 FileOutputStream out = new FileOutputStream(
		   					      new File("SaleReport.xls"));
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

						if ((new File("SaleReport.xls")).exists()) {

							Process p = Runtime
							   .getRuntime()
							   .exec("rundll32 url.dll,FileProtocolHandler SaleReport.xls");
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
