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
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

public class PriceListContr  implements Initializable {
	  @FXML
	    private TableView<Price> tableview;

	    @FXML
	    private TableColumn<Price, String> idCol;

	    @FXML
	    private TableColumn<Price, String> nameCol;

	    @FXML
	    private TableColumn<Price, String> unitCol;

	    @FXML
	    private TableColumn<Price, String> wholeCol;

	    @FXML
	    private JFXTextField search;

	    @FXML
	    private JFXButton btnprintPurchase;

	    @FXML
	    private StackPane pane;
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs,rs1,rsT,rsC,rs4;
		    private Statement st,st1,stC,stT,st4;
		    private PreparedStatement prep;
			private WorkIndicatorDialog wd=null;
		    ObservableList<Price> searchdata;
		    ObservableList<Price> datlist;
			 List listofdat = new ArrayList();
	   
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fetchAll();
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		unitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		wholeCol.setCellValueFactory(new PropertyValueFactory<>("pricewhol"));
search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			
			if (oldValue != null && (newValue.length() < oldValue.length())) {
		            	tableview.setItems(searchdata);
		            }
		            String value = newValue.toLowerCase();
		            ObservableList<Price> subentries = FXCollections.observableArrayList();

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
	 					   String query = "SELECT * FROM products";
	 					   		
	 					   rs=st.executeQuery(query);
	 					  listofdat.clear();
	 					   while(rs.next()){
	 						   double uni,whol;
	 						   String id,name;
	 						   id = rs.getString("code");
	 						   name = rs.getString("name");
	 						   uni =  rs.getDouble("price");
	 						   whol =  rs.getDouble("priceGen");
	 						  
	 						  
	 						  
	 						  listofdat.add(new Price(id,name,uni,whol));
		 				
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
	@SuppressWarnings("unchecked")
	@FXML
	    void printPurchase(ActionEvent event) {

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
	   			      .createSheet("pricelist");
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
	   			      header.setCenter(HSSFHeader.font("Times New Roman", "Bold")+HSSFHeader.fontSize((short)14)+"PRICE LIST");
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
	   			      cell.setCellValue("UNIT PRICE@");
				      cell.setCellStyle(BoldStyle);
				      cell=row.createCell(3);
	   			      cell.setCellValue("WHOLESALE PRICE");
	   			      cell.setCellStyle(BoldStyle);
	   			     
	   		
	   			   
	   			      int i=7; int tota=0; int totp=0;
	   			      for(int j=0;j<tableview.getItems().size(); j++){
	   		            
	   		          double uni=tableview.getItems().get(j).getPrice();
	   		          String id=tableview.getItems().get(j).getId();
	   		          double whol=tableview.getItems().get(j).getPricewhol();
	   		          String nam=tableview.getItems().get(j).getName();
	   		         
	   		         
	   		     		   	 row=spreadsheet.createRow(i);
	   		    	         cell=row.createCell(0);
	   		    	         cell.setCellValue(id);
	   		    	         cell=row.createCell(1); 
	   		    	        
	   		    	         cell.setCellValue(nam);
	   		    	         cell=row.createCell(2);
	   		    	         cell.setCellValue(uni);
	   		    	         cell.setCellStyle(BoldStyle);
	   		    	         cell=row.createCell(3);
	   		    	         cell.setCellValue(whol);
	   		    	        
	   		           	 i++;	
	   							
	   							}
	   		
	   				
	   				 FileOutputStream out = new FileOutputStream(
	   					      new File("pricelist.xls"));
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
	          
			int	j=0;
	 		 try {

					if ((new File("pricelist.xls")).exists()) {

						Process p = Runtime
						   .getRuntime()
						   .exec("rundll32 url.dll,FileProtocolHandler pricelist.xls");
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