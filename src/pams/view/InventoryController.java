package pams.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class InventoryController implements Initializable {
	Main main;
	private WorkIndicatorDialog wd=null;
	 @FXML
	    private BorderPane border;
	  @FXML
	    private Label labelprint;

	    @FXML
	    private JFXButton btnPrint;

	    @FXML
	    private Label laelTotal;

	    @FXML
	    private Label labelProfit;
	   
	    @FXML
	    private TableView<Inventory> tableview;

	    @FXML
	    private JFXButton btnBack;

	    @FXML
	    private TableColumn<Inventory, String> invoiceNo;

	    @FXML
	    private TableColumn<Inventory, String> date;

	    @FXML
	    private TableColumn<Inventory, String> prodname;

	    @FXML
	    private TableColumn<Inventory, String> categ;

	    @FXML
	    private TableColumn<Inventory, String> price;

	    @FXML
	    private TableColumn<Inventory, String> qty;

	    @FXML
	    private TableColumn<Inventory, String> amount;

	    @FXML
	    private TableColumn<Inventory, String> profit;

	    @FXML
	    private TableColumn<Inventory, String> action;
	    @FXML
	    private StackPane pane;
	   	String inv=null;
	    @FXML
	    private JFXTextField search;

	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs;
		    private Statement st;
		    int toAmount = 0,toProfit=0;
		    static  String pronam = null;
		    private PreparedStatement prep,prep1;
	    ObservableList<Items>list = FXCollections.observableArrayList();
	    ObservableList<Inventory> searchdata;
	    ObservableList<Inventory> datlist;
		 List listofdat = new ArrayList();
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1) {
	    
	    	
	    	fetchInventory();
	    	invoiceNo.setCellValueFactory(new PropertyValueFactory<>("inventID"));
	    	date.setCellValueFactory(new PropertyValueFactory<>("date"));
	    	prodname.setCellValueFactory(new PropertyValueFactory<>("name"));
	    	categ.setCellValueFactory(new PropertyValueFactory<>("cats"));
	    	price.setCellValueFactory(new PropertyValueFactory<>("price"));
	    	qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
	    	amount.setCellValueFactory(new PropertyValueFactory<>("totalamount"));
	    	profit.setCellValueFactory(new PropertyValueFactory<>("totalprofit"));
	    	action.setCellValueFactory(new PropertyValueFactory<>("action"));
	    	tableview.setOnMouseClicked(new EventHandler<MouseEvent>(){

				@Override
				public void handle(MouseEvent arg0) {
						//System.out.println("You click at cell "+ tableview.getSelectionModel().getSelectedCells().get(0).getColumn());
					//proCode = tableProd.getSelectionModel().getSelectedItem().
				//	pronam = tableview.getSelectionModel().getSelectedItem().getInventID();
					int cellNum =  tableview.getSelectionModel().getSelectedCells().get(0).getColumn();
			 if(cellNum == 8){
			
			JFXSnackbar bar = new JFXSnackbar(border);
	        	bar.show("To delete, Highlight the row you want to delete \n then press the remove button down the table",3000);
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
	 	        	  
		 	        	 laelTotal.setText("Tsh:"+toAmount);
						   labelProfit.setText("Tsh:"+toProfit);
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
	 					   String query = "SELECT saltab.*, prodtab.code,prodtab.name,prodtab.description FROM sales_order saltab, products prodtab"
	 					   		+ " WHERE saltab.product_code=prodtab.code";
	 					   rs=st.executeQuery(query);
	 					  listofdat.clear();
	 					   while(rs.next()){
	 						
	 						 double totamo = 0,totpro = 0;
	 						   String inv,date,name,cat;
	 						   int pric,qty,totalprof,totalam;
	 						   inv = rs.getString("invoice_no");
	 						   date = rs.getString("date");
	 						   name = rs.getString("name");
	 						   cat = rs.getString("description");
	 						   pric = rs.getInt("price");
	 						   qty = rs.getInt("qty");
	 						   totalprof = rs.getInt("profit");
	 						   totalam = rs.getInt("amount");
	 						   totamo = totamo+totalam;
	 						   totpro = totpro+totalprof;
	 						//  listofdat.add(new Inventory(inv, date, name, cat, pric, qty,totalam , totalprof,"delete"));
		 					  toAmount+=totamo;
		 					  toProfit+=totpro;
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
		   			   rowH4 = spreadsheet.createRow(0);
		   			      cellH = rowH4.createCell(4);
		   			      cellH.setCellValue(busweb);
		   			      cellH.setCellStyle(BoldStyle);
		   			      
		   			   row = spreadsheet.createRow(6);
		   			      cell=row.createCell(0);
		   			      cell.setCellValue("INVOICE NUMBER");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(1);
		   			      cell.setCellValue("ISSUED DATE");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(2);
		   			      cell.setCellValue("MEDICINE NAME");
					      cell.setCellStyle(BoldStyle);
					      cell=row.createCell(3);
		   			      cell.setCellValue("CATEGORY");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(4);
		   			      cell.setCellValue("SELLING PRICE");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(5);
		   			      cell.setCellValue("QUANTITY");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(6);
		   			      cell.setCellValue("AMOUNT");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(7);
		   			      cell.setCellValue("PROFIT OBTAINED");
		   			      cell.setCellStyle(BoldStyle);
		   			    
		   			    
		   			      int i=7; int tota=0; int totp=0;
		   			      for(int j=0;j<tableview.getItems().size(); j++){
		   		           	 
		   		           	  
		   		      /*    int prof=tableview.getItems().get(j).getTotalprofit();
		   		          String inv=tableview.getItems().get(j).getInventID();
		   		          int qt=tableview.getItems().get(j).getQty();
		   		          String dat=tableview.getItems().get(j).getDate();
		   		          String nam=tableview.getItems().get(j).getName();
		   		          int amo=tableview.getItems().get(j).getTotalamount();
		   		          int pri=tableview.getItems().get(j).getPrice();
		   		          String cat=tableview.getItems().get(j).getCats();*/
		   		/*       tota+=amo; totp+=prof;
		   		           		 row=spreadsheet.createRow(i);
		   		    	         cell=row.createCell(0);
		   		    	         cell.setCellValue(inv);
		   		    	         cell=row.createCell(1); 
		   		    	        
		   		    	         cell.setCellValue(dat);
		   		    	         cell=row.createCell(2);
		   		    	         cell.setCellValue(nam);
		   		    	         cell.setCellStyle(BoldStyle);
		   		    	         cell=row.createCell(3);
		   		    	         cell.setCellValue(cat);
		   		    	         cell=row.createCell(4);
				    	         cell.setCellValue(pri);
				    	         cell=row.createCell(5);
		   		    	         cell.setCellValue(qt);
		   		    	         cell=row.createCell(6);
				    	         cell.setCellValue(amo);
				    	         cell=row.createCell(7);
		   		    	         cell.setCellValue(prof);
		   		    	  	 */
				    	
		   		    	     
		   		           	 i++;	
		   							
		   							}
		   			   row = spreadsheet.createRow(i+2);
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
		   			      cell.setCellStyle(BoldStyle);
		   				
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
		    	main.showMainDash();
		    }
		
}
