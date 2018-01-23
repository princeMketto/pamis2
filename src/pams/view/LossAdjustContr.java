package pams.view;

import java.io.File;
import java.io.FileOutputStream;
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
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class LossAdjustContr  implements Initializable {

    @FXML
    private TableView<Adjust> tableview;

    @FXML
    private TableColumn<Adjust, String> idCol;

    @FXML
    private TableColumn<Adjust, String> dateCol;

    @FXML
    private TableColumn<Adjust, String> nameCol;

    @FXML
    private TableColumn<Adjust,String> remakCol;

    @FXML
    private TableColumn<Adjust, Integer> qtyCol;

    @FXML
    private TableColumn<Adjust, String> issueCol;
    @FXML
    private JFXDatePicker startDate,endDate;


    @FXML
    private JFXTextField search;

    @FXML
    private JFXButton btnprintPurchase,btnShow;

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
	    ObservableList<Adjust> searchdata;
	    ObservableList<Adjust> datlist;
		 List listofdat = new ArrayList();
		 LocalDate startD = null,endD = null,tempD;
		    Date start = null,end = null;
 static String id,nam,addr,phon;
 String invoId;
 double TempTot;
 String cuName,cuAddr,cuPhon;
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
	   			      .createSheet("adjustment_report");
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
	   			      header.setCenter(HSSFHeader.font("Times New Roman", "Bold")+HSSFHeader.fontSize((short)14)+"LOSS & ADJUSTMENT REPORT");
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
	   			      cell.setCellValue("ISSUE DATE");
				      cell.setCellStyle(BoldStyle);
				      cell=row.createCell(3);
	   			      cell.setCellValue("COMMENT");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(4);
	   			      cell.setCellValue("QUANTITY");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(5);
	   			      cell.setCellValue("ISSUED BY");
	   			      cell.setCellStyle(BoldStyle);
	   		
	   			   
	   			      int i=7; int tota=0; int totp=0;
	   			      for(int j=0;j<tableview.getItems().size(); j++){
	   		            
	   		          int qt=tableview.getItems().get(j).getQty();
	   		          String id=tableview.getItems().get(j).getId();
	   		          String dat=tableview.getItems().get(j).getDate();
	   		          String nam=tableview.getItems().get(j).getName();
	   		          String iss = tableview.getItems().get(j).getIssue();
	   		          String comm = tableview.getItems().get(j).getRemak();
	   		         
	   		     		   	 row=spreadsheet.createRow(i);
	   		    	         cell=row.createCell(0);
	   		    	         cell.setCellValue(id);
	   		    	         cell=row.createCell(1); 
	   		    	        
	   		    	         cell.setCellValue(nam);
	   		    	         cell=row.createCell(2);
	   		    	         cell.setCellValue(dat);
	   		    	         cell.setCellStyle(BoldStyle);
	   		    	         cell=row.createCell(3);
	   		    	         cell.setCellValue(comm);
	   		    	         cell=row.createCell(4);
			    	         cell.setCellValue(qt);
			    	         cell=row.createCell(5);
	   		    	         cell.setCellValue(iss);
	   		    	    
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
	   					      new File("adjustment_report.xls"));
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

					if ((new File("adjustment_report.xls")).exists()) {

						Process p = Runtime
						   .getRuntime()
						   .exec("rundll32 url.dll,FileProtocolHandler adjustment_report.xls");
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

    @SuppressWarnings("unchecked")
	@FXML
    void showCheck(ActionEvent event) {


    	
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
 					   String query = "SELECT * FROM adjustment WHERE date BETWEEN '"+start+"' AND '"+end+"'";
 					   rs=st.executeQuery(query);
 					  listofdat.clear();
 					   while(rs.next()){
 						   int qt;
 						   String id,name,rema,dat,iss;
 						   id = rs.getString("id");
 						   name = rs.getString("name");
 						   rema =  rs.getString("remarks");
 						   dat =  rs.getString("date");
 						   qt = rs.getInt("qty");
 						   iss =  rs.getString("issued_by");
 						  
 						  
 						  listofdat.add(new Adjust(id,name,dat,rema,qt,iss));
	 				
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
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		fetchAll();
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
  	nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
  	dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
  	remakCol.setCellValueFactory(new PropertyValueFactory<>("remak"));
  	qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
  	issueCol.setCellValueFactory(new PropertyValueFactory<>("issue"));

	search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			
			if (oldValue != null && (newValue.length() < oldValue.length())) {
		            	tableview.setItems(searchdata);
		            }
		            String value = newValue.toLowerCase();
		            ObservableList<Adjust> subentries = FXCollections.observableArrayList();

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
	 					   String query = "SELECT * FROM adjustment";
	 					   		
	 					   rs=st.executeQuery(query);
	 					  listofdat.clear();
	 					   while(rs.next()){
	 						   int qt;
	 						   String id,name,rema,dat,iss;
	 						   id = rs.getString("id");
	 						   name = rs.getString("name");
	 						   rema =  rs.getString("remarks");
	 						   dat =  rs.getString("date");
	 						   qt = rs.getInt("qty");
	 						   iss =  rs.getString("issued_by");
	 						  
	 						  
	 						  listofdat.add(new Adjust(id,name,dat,rema,qt,iss));
		 				
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
