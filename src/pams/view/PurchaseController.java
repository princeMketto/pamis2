package pams.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.util.IOUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class PurchaseController implements Initializable {
	ConnectDB database = new ConnectDB();
	LoginController lg = new LoginController(); 
	private Connection con;
	   private ResultSet rs,rsH;
	   private Statement st,stH;
	   private PreparedStatement prep,prep1;
    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnprintPurchase;

    @FXML
    private JFXTextField search;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXDatePicker startDate;

    @FXML
    private JFXDatePicker endDate;

    @FXML
    private JFXButton btnShow;
    @FXML
    private TableView<Purchase> tableview;

   /* @FXML
    private TableColumn<Purchase, Integer> batchCol;
*/
    @FXML
    private TableColumn<Purchase, String> nameCol;

    @FXML
    private TableColumn<Purchase, Integer> qtyCol;

    @FXML
    private TableColumn<Purchase, String> arrivCol;

    @FXML
    private TableColumn<Purchase, String> expCol;

    @FXML
    private TableColumn<Purchase, Integer> costCol;

    @FXML
    private TableColumn<Purchase, Integer> priceCol;

    @FXML
    private TableColumn<Purchase, String> supplCol;

    @FXML
    private TableColumn<Purchase, String> invoiceCol;
    ObservableList<Purchase> searchdata;
    ObservableList<Purchase> datlist;
	 List listofdat = new ArrayList();
    LocalDate startD = null,endD = null,tempD;
    Date start = null,end = null;
Main main;
private WorkIndicatorDialog wd=null;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		fetchPurchase();
	//	batchCol.setCellValueFactory(new PropertyValueFactory<>("batchs"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("prodname"));
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
		arrivCol.setCellValueFactory(new PropertyValueFactory<>("received"));
		expCol.setCellValueFactory(new PropertyValueFactory<>("expire"));
		costCol.setCellValueFactory(new PropertyValueFactory<>("origprice"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("cellprice"));
		supplCol.setCellValueFactory(new PropertyValueFactory<>("suppl"));
		invoiceCol.setCellValueFactory(new PropertyValueFactory<>("invo"));
		/*Image searbtn = new Image(getClass().getResourceAsStream("images/seac.png"));
		btnSearch.setGraphic(new ImageView(searbtn));*/
		search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			
			if (oldValue != null && (newValue.length() < oldValue.length())) {
		            	tableview.setItems(searchdata);
		            }
		            String value = newValue.toLowerCase();
		            ObservableList<Purchase> subentries = FXCollections.observableArrayList();

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
	private void fetchPurchase() {
for(int i=0; i<tableview.getItems().size(); i++){
    		
	tableview.getItems().clear();
    	}
wd = new WorkIndicatorDialog(null, "Loading purchase data...");
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
				   String query = "SELECT purchased_items.*,products.name FROM purchased_items,products WHERE "
	   				   		+ "purchased_items.product_code=products.code";
	   				   rs=st.executeQuery(query);
	   				 listofdat.clear();
	   				   while(rs.next()){
	   					   String nam,sup,rec,ex,supl,inv;
	   					   double cos,pric,prof; int qt,total;
	   					  // batc = rs.getInt("batch");
	   					   nam = rs.getString("name");
	   					   qt = rs.getInt("qty");
	   					   rec = rs.getString("arrival");
	   					   ex =  rs.getString("expiry");
	   					   cos = rs.getDouble("cost");
	   					   pric = rs.getDouble("price");
	   					   sup = rs.getString("supplier");
	   					 	inv = rs.getString("invoice");
				
					  
	   					 listofdat.add(new Purchase(nam,qt,rec, ex, cos, pric,sup,inv));
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
    void showPurchase(ActionEvent event) {
    	wd = new WorkIndicatorDialog(null, "Fetching information...");
     	 wd.addTaskEndNotification(result -> {
     		  String outres = result.toString();
     	   if(outres.equals("0")){
     		 TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("PLEASE CHOOSE START, END DATE");
		       tray.setMessage("System cant generate info \n with empty time");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));    	
     	   }
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
   		
   			for(int i=0; i<tableview.getItems().size(); i++){
   	    		
   				tableview.getItems().clear();
   			    	}
   			wd.exec("fetch", inputParam -> {
   				int z = 0;
   			try{
   				con= database.dbconnect();
   				   st= con.createStatement();
   				   String query = "SELECT purchased_items.*,products.name FROM purchased_items,products WHERE "
   				   		+ "purchased_items.arrival BETWEEN '"+start+"' AND '"+end+"' AND purchased_items.product_code=products.code";
   				   rs=st.executeQuery(query);
   				 listofdat.clear();
   				   while(rs.next()){
   					   String nam,sup,rec,ex,supl,inv;
   					   double cos,pric,prof; int qt,total;
   					 //  batc = rs.getInt("batch");
   					   nam = rs.getString("name");
   					   qt = rs.getInt("qty");
   					   rec = rs.getString("arrival");
   					   ex =  rs.getString("expiry");
   					   cos = rs.getDouble("cost");
   					   pric = rs.getDouble("price");
   					   sup = rs.getString("supplier");
   					 	inv = rs.getString("invoice");
   				
   					  
   					 listofdat.add(new Purchase(nam,qt,rec, ex, cos, pric,sup,inv));
   					   z=1;
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
   	 }catch(Exception er){
   		// z=0; 
		       }
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
	   			      .createSheet("purchases");
	   			      HSSFRow row,rowH,rowH1,rowH2,rowH3,rowH4;//=spreadsheet.createRow(0);
	   			      HSSFCellStyle RotateStyle = workbook.createCellStyle();
	   			      HSSFCellStyle BoldStyle = workbook.createCellStyle();
	   			      RotateStyle.setRotation((short)90);
	   			      HSSFFont my_font = workbook.createFont();
	   			     stH = con.createStatement();
	   			     String sql = "SELECT * FROM business_details";
	   			     rsH = stH.executeQuery(sql);
	   			     String busNam=null; String busAd=null; String busEm=null;
	   			     String busweb=null; String busph=null;
	   			     if(rsH.next()){
	   			    	 busNam = rsH.getString("name");
	   			    	 busAd =  rsH.getString("address");
	   			    	 busEm = rsH.getString("email");
	   			    	 busweb = rsH.getString("website");
	   			    	 busph = rsH.getString("phone");
	   			    	 
	   			     }
	   			      Header header = spreadsheet.getHeader();
	   			      header.setCenter(HSSFHeader.font("Times New Roman", "Bold")+HSSFHeader.fontSize((short)14)+"PURCHASE LIST");
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
	   			      cell.setCellValue("BATCH");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(1);
	   			      cell.setCellValue("MEDICINE NAME");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(2);
	   			      cell.setCellValue("QUANTITY");
				      cell.setCellStyle(BoldStyle);
				      cell=row.createCell(3);
	   			      cell.setCellValue("ARRIVAL DATE");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(4);
	   			      cell.setCellValue("EXPIRE DATE");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(5);
	   			      cell.setCellValue("GENERAL COST");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(6);
	   			      cell.setCellValue("PRICE");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(7);
	   			      cell.setCellValue("SUPPLIER");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(8);
	   			      cell.setCellValue("INVOICE");
	   			      cell.setCellStyle(BoldStyle);
	   			    
	   			      int i=7;
	   			      for(int j=0;j<tableview.getItems().size(); j++){
	   		           	 
	   		           	  
	   		       //   int batc=tableview.getItems().get(j).getBatchs();
	   		          String nam=tableview.getItems().get(j).getProdname();
	   		          int qt=tableview.getItems().get(j).getQty();
	   		          String arriv=tableview.getItems().get(j).getReceived();
	   		          String exp=tableview.getItems().get(j).getExpire();
	   		          double cos=tableview.getItems().get(j).getOrigprice();
	   		          double pri=tableview.getItems().get(j).getCellprice();
	   		          String supp=tableview.getItems().get(j).getSuppl();
	   		       String inv=tableview.getItems().get(j).getInvo();
	   		          
	   		             
	   		           		 row=spreadsheet.createRow(i);
	   		    	         cell=row.createCell(0);
	   		    	         cell.setCellValue(j);
	   		    	         cell=row.createCell(1); 
	   		    	         cell.setCellStyle(BoldStyle);
	   		    	         cell.setCellValue(nam);
	   		    	         cell=row.createCell(2);
	   		    	         cell.setCellValue(qt);
	   		    	         cell=row.createCell(3);
	   		    	         cell.setCellValue(arriv);
	   		    	         cell=row.createCell(4);
			    	         cell.setCellValue(exp);
			    	         cell=row.createCell(5);
	   		    	         cell.setCellValue(cos);
	   		    	         cell=row.createCell(6);
			    	         cell.setCellValue(pri);
			    	         cell=row.createCell(7);
	   		    	         cell.setCellValue(supp);
	   		    	  	   cell=row.createCell(8);
			    	         cell.setCellValue(inv);
			    	
	   		    	     
	   		           	 i++;	
	   							
	   							}
	   		
	   				
	   				 FileOutputStream out = new FileOutputStream(
	   					      new File("purchases.xls"));
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

					if ((new File("purchases.xls")).exists()) {

						Process p = Runtime
						   .getRuntime()
						   .exec("rundll32 url.dll,FileProtocolHandler purchases.xls");
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
