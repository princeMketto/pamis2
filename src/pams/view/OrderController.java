package pams.view;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class OrderController implements Initializable{
	 
	 @FXML
	    private TableView<Order> tableview;

	    @FXML
	    private TableColumn<Order, String> idCol;

	    @FXML
	    private TableColumn<Order, String> nameCol;
	    @FXML
	    private TableColumn<Order, Double> genCol;

	    @FXML
	    private TableColumn<Order, String> descCol;
	    @FXML
	    private TableColumn<Order, Integer> qty;

	    @FXML
	    private TableColumn<Order, String> vendorCol;
	    @FXML
	    private JFXTextField search;
//
	    @FXML
	    private StackPane pane;

	    @FXML
	    private JFXButton btnCheck,btnSend;
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs,rs1,rsT,rsC,rs4;
		    private Statement st,st1,stC,stT,st4;
		    private PreparedStatement prep;
			private WorkIndicatorDialog wd=null;
		    ObservableList<Order> searchdata;
		    ObservableList<Order> datlist;
			 List listofdat = new ArrayList();
			 static String sendEm=null,pass=null;
			 static String vend = null;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		fetchAll();
		btnCheck.setTooltip(new Tooltip("Order and print order"));
	//	btNDelete.setTooltip(new Tooltip("You cant delete this order"));
		btnSend.setTooltip(new Tooltip("Click to send order"));
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    	nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    	genCol.setCellValueFactory(new PropertyValueFactory<>("gen"));
    	descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
    	qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    	vendorCol.setCellValueFactory(new PropertyValueFactory<>("vendor"));
	search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			
			if (oldValue != null && (newValue.length() < oldValue.length())) {
		            	tableview.setItems(searchdata);
		            }
		            String value = newValue.toLowerCase();
		            ObservableList<Order> subentries = FXCollections.observableArrayList();

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
	tableview.setOnMouseClicked(new EventHandler<MouseEvent>(){

		@Override
		public void handle(MouseEvent arg0) {
				//System.out.println("You click at cell "+ tableview.getSelectionModel().getSelectedCells().get(0).getColumn());
			//proCode = tableProd.getSelectionModel().getSelectedItem().
			vend = tableview.getSelectionModel().getSelectedItem().getVendor();

		
		}
		
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
	 					   String query = "SELECT * FROM products WHERE qty_remain <= minQty AND comment='ACTIVE'";
	 					   		
	 					   rs=st.executeQuery(query);
	 					  listofdat.clear();
	 					   while(rs.next()){
	 						   double amou;
	 						   String id,name,gen,des,ven; int qt;
	 						   id = rs.getString("code");
	 						   name = rs.getString("name");
	 						   gen =  rs.getString("generic_name");
	 						   des =  rs.getString("description");
	 						   ven =  rs.getString("supplier");
	 							qt = rs.getInt("nextQty");
	 						  
	 						  listofdat.add(new Order(id,name,gen,des,qt,ven));
		 				
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
	  private void sendFile(){
		  
	  }
	  static String getMail(){
		  return sendEm;
	  }
	  static String getVendor(){
		  return vend;
	  }
	  static String getPass(){
		  return pass;
	  }
	
		@SuppressWarnings("unchecked")
		@FXML
		    void sendOrder(ActionEvent event) {
			/*JFXSnackbar bar = new JFXSnackbar(pane);
        	bar.show("Upgrade your application \n to unlock this awesome feature...",4000);
			*/
			close();	
		   	 wd = new WorkIndicatorDialog(null, "");
		   	   wd.addTaskEndNotification(result -> {
		   		  String outres = result.toString();
		         	
		            if(outres.equals("1")){
		            	if(sendEm.equals(null)){
		            		JFXSnackbar bar = new JFXSnackbar(pane);
		    	        	bar.show("You need to set your Email to enable this action. \n"
		    	        			+ "Go to management and edit business detail \n"
		    	        			+ "",6000);
		            	}else{
		            		Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+"); // [A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}
		            		Matcher m = pattern.matcher(sendEm);
		            		if(m.find() && m.group().equals(sendEm)){
		            			sendEm = sendEm;
			            		AnchorPane infopane = null;
				    	    	try{
				    	    		FXMLLoader loader = new FXMLLoader();
				    				loader.setLocation(getClass().getResource("Email.fxml"));
				    				 infopane = loader.load();
				    	    	}catch(Exception e){ 
				    	    		e.printStackTrace();
				    	    	}

				    			
				    			 
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
				    					
				    					
				    						 
				    						//fetchInventory();
				    			    	dialog.close();
				    			    	
				    			    	
				    					}
				    		    		
				    		    	});
				    		   
				    		    	content.setActions(bt);
				    		    	dialog.show();
			            	
		            			
		            		}else{
		            			JFXSnackbar bar = new JFXSnackbar(pane);
			    	        	bar.show("You need to set VALID Email to enable this action. \n"
			    	        			+ "Go to management and edit business detail \n"
			    	        			+ "",6000);
		            		}
		            		
		            	}
		            	
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
		   			      .createSheet("item_order");
		   			      HSSFRow row,rowH,rowH1,rowH2,rowH3,rowH4;//=spreadsheet.createRow(0);
		   			      HSSFCellStyle RotateStyle = workbook.createCellStyle();
		   			      HSSFCellStyle BoldStyle = workbook.createCellStyle();
		   			      RotateStyle.setRotation((short)90);
		   			      HSSFFont my_font = workbook.createFont();
		   			      con = database.dbconnect();
		   			     st = con.createStatement();
		   			     String sql = "SELECT * FROM business_details";
		   			     rs = st.executeQuery(sql);
		   			     String busNam=null; String busAd=null; String busEm=null;
		   			     String busweb=null; String busph=null;
		   			     if(rs.next()){
		   			    	 busNam = rs.getString("name");
		   			    	 busAd =  rs.getString("address");
		   			    	 busEm = rs.getString("email");
		   			    	 sendEm=busEm;
		   			    	 pass=rs.getString("password");
		   			    	 busweb = rs.getString("website");
		   			    	 busph = rs.getString("phone");
		   			    	 
		   			     }
		   			     rs.close(); st.close(); con.close();
		   			      Header header = spreadsheet.getHeader();
		   			      header.setCenter(HSSFHeader.font("Times New Roman", "Bold")+HSSFHeader.fontSize((short)14)+"ORDER LIST");
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
		   			      cell.setCellValue("GENERIC NAME");
					      cell.setCellStyle(BoldStyle);
					      cell=row.createCell(3);
		   			      cell.setCellValue("DESCRIPTION");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(4);
		   			      cell.setCellValue("ORDER QUANTITY");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(5);
		   			      cell.setCellValue("VENDOR NAME");
		   			      cell.setCellStyle(BoldStyle);
		   		
		   			   
		   			      int i=7; int tota=0; int totp=0;
		   			      for(int j=0;j<tableview.getItems().size(); j++){	  
		   		          String gen=tableview.getItems().get(j).getGen();
		   		        String des=tableview.getItems().get(j).getDesc();
		   		     String ven=tableview.getItems().get(j).getVendor();
		   		     vend = ven;
		   		          String id=tableview.getItems().get(j).getId();
		   		      int qnt = tableview.getItems().get(j).getQty();
		   		          String nam=tableview.getItems().get(j).getName();
		   		         
		   		     		   	 row=spreadsheet.createRow(i);
		   		    	         cell=row.createCell(0);
		   		    	         cell.setCellValue(id);
		   		    	         cell=row.createCell(1); 
		   		    	        
		   		    	         cell.setCellValue(nam);
		   		    	         cell=row.createCell(2);
		   		    	         cell.setCellValue(gen);
		   		    	         cell.setCellStyle(BoldStyle);
		   		    	         cell=row.createCell(3);
		   		    	         cell.setCellValue(des);
		   		    	         cell.setCellStyle(BoldStyle); 
		   		    	         cell=row.createCell(4);
		   		    	         cell.setCellValue(qnt);
		   		    	         cell.setCellStyle(BoldStyle); 
		   		    	      cell=row.createCell(5);
		   		    	         cell.setCellValue(ven);
		   		    	         cell.setCellStyle(BoldStyle); 
		   		           	 i++;	
		   							
		   							}
		 
		   				 FileOutputStream out = new FileOutputStream(
		   					      new File("item_order.xls"));
		   					      workbook.write(out);
		   					      out.close();
		   					      k=1;
		   					      
		   			}catch(Exception e){
		   				e.printStackTrace();
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
	@FXML
	    void printOrder(ActionEvent event) {


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
		   			      .createSheet("item_order");
		   			      HSSFRow row,rowH,rowH1,rowH2,rowH3,rowH4;//=spreadsheet.createRow(0);
		   			      HSSFCellStyle RotateStyle = workbook.createCellStyle();
		   			      HSSFCellStyle BoldStyle = workbook.createCellStyle();
		   			      RotateStyle.setRotation((short)90);
		   			      HSSFFont my_font = workbook.createFont();
		   			      con = database.dbconnect();
		   			     st = con.createStatement();
		   			     String sql = "SELECT * FROM business_details";
		   			     rs = st.executeQuery(sql);
		   			     String busNam=null; String busAd=null; String busEm=null;
		   			     String busweb=null; String busph=null;
		   			     if(rs.next()){
		   			    	 busNam = rs.getString("name");
		   			    	 busAd =  rs.getString("address");
		   			    	 busEm = rs.getString("email");
		   			    	 sendEm=busEm;
		   			    	 busweb = rs.getString("website");
		   			    	 busph = rs.getString("phone");
		   			    	 
		   			     }
		   			     rs.close(); st.close(); con.close();
		   			      Header header = spreadsheet.getHeader();
		   			      header.setCenter(HSSFHeader.font("Times New Roman", "Bold")+HSSFHeader.fontSize((short)14)+"ORDER LIST");
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
		   			      cell.setCellValue("GENERIC NAME");
					      cell.setCellStyle(BoldStyle);
					      cell=row.createCell(3);
		   			      cell.setCellValue("DESCRIPTION");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(4);
		   			      cell.setCellValue("ORDER QUANTITY");
		   			      cell.setCellStyle(BoldStyle);
		   			      cell=row.createCell(5);
		   			      cell.setCellValue("VENDOR NAME");
		   			      cell.setCellStyle(BoldStyle);
		   		
		   			   
		   			      int i=7; int tota=0; int totp=0;
		   			      for(int j=0;j<tableview.getItems().size(); j++){	  
		   		          String gen=tableview.getItems().get(j).getGen();
		   		        String des=tableview.getItems().get(j).getDesc();
		   		     String ven=tableview.getItems().get(j).getVendor();
		   		          String id=tableview.getItems().get(j).getId();
		   		      int qnt = tableview.getItems().get(j).getQty();
		   		          String nam=tableview.getItems().get(j).getName();
		   		         
		   		     		   	 row=spreadsheet.createRow(i);
		   		    	         cell=row.createCell(0);
		   		    	         cell.setCellValue(id);
		   		    	         cell=row.createCell(1); 
		   		    	        
		   		    	         cell.setCellValue(nam);
		   		    	         cell=row.createCell(2);
		   		    	         cell.setCellValue(gen);
		   		    	         cell.setCellStyle(BoldStyle);
		   		    	         cell=row.createCell(3);
		   		    	         cell.setCellValue(des);
		   		    	         cell.setCellStyle(BoldStyle); 
		   		    	         cell=row.createCell(4);
		   		    	         cell.setCellValue(qnt);
		   		    	         cell.setCellStyle(BoldStyle); 
		   		    	      cell=row.createCell(5);
		   		    	         cell.setCellValue(ven);
		   		    	         cell.setCellStyle(BoldStyle); 
		   		           	 i++;	
		   							
		   							}
		 
		   				 FileOutputStream out = new FileOutputStream(
		   					      new File("item_order.xls"));
		   					      workbook.write(out);
		   					      out.close();
		   					      k=1;
		   					      
		   			}catch(Exception e){
		   				e.printStackTrace();
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
			
			 wd = new WorkIndicatorDialog(null, "Opening your order...");
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

						if ((new File("item_order.xls")).exists()) {

							Process p = Runtime
							   .getRuntime()
							   .exec("rundll32 url.dll,FileProtocolHandler item_order.xls");
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
}
