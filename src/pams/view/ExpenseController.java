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
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Header;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ExpenseController implements Initializable {
	   @FXML
	    private JFXButton savebtn,btnPrint;

	    @FXML
	    private JFXTextField amount,comment;

	    @FXML
	    private JFXTextField other;
	    @FXML
	    private Text others;

	    @FXML
	    private Text wag;
	   

	    @FXML
	    private ToggleGroup mode;

	    @FXML
	    private JFXRadioButton week;

	    @FXML
	    private JFXRadioButton month;

	    @FXML
	    private JFXTextField numday;

	    @FXML
	    private Text lblmode;

	   

	    @FXML
	    private AnchorPane pane;
	    @FXML
	    private JFXButton othexp;

	    @FXML
	    private JFXDatePicker date;
	   /* @FXML
	    private JFXSnackbar bar;*/

	    @FXML
	    private JFXButton whatbtn;
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs,rs1;
		    private Statement st,st1;
		    private PreparedStatement prep,prep1,prep2;
		    private WorkIndicatorDialog wd=null;
		    int wagE=0,oTher=0,s = 0,workday=0;;
		    String toMode = null,reasn,comm=null;
		    int wage = 0,oth = 0,workd=0;
		    LocalDate arriv = null;
	    	  Date dateArriv = null;
		  static  String selectmode;
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		comment.setOpacity(0);
		 week.setUserData("wages"); month.setUserData("others");
		 week.setToggleGroup(mode); month.setToggleGroup(mode);
		mode.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				  if (mode.getSelectedToggle() != null && !mode.getSelectedToggle().equals("mode") ) {
					  	selectmode = mode.getSelectedToggle().getUserData().toString();
					  	if(selectmode.equals("wages")){
					  		comment.setOpacity(0);
					  	}
					  	if(selectmode.equals("others")){
					  		comment.setOpacity(1);
					  	}
			          		  }
				
			}
			
		});
		
		wd = new WorkIndicatorDialog(null, "");
	 	   wd.addTaskEndNotification(result -> {
	 		  String outres = result.toString();
	          // System.out.println("nomaa "+outres);
	           if(outres.equals("1")){
	        	   wag.setText(""+wagE);
	        	  others.setText(""+oTher);
	        	
	        	
	           }
	           wd=null;
	 	   });
	 		 wd.exec("fetch", inputParam -> {
		           // Thinks to do...
		           // NO ACCESS TO UI ELEMENTS!
	 			
	 		  	try{
	 				
	 				con= database.dbconnect();
	 				   st= con.createStatement();
	 				   String query = "SELECT SUM(expense) AS 'expense',reason FROM expenseother GROUP BY reason";
	 				 
	 				  rs=st.executeQuery(query);
	 				 
	 				   while(rs.next()){
	 				
	 					 
	 					   reasn = rs.getString("reason");
	 					   if(reasn.equals("wages")){
	 						  wagE = rs.getInt("expense");
	 					   }
	 					   if(reasn.equals("others")){
	 						  oTher = rs.getInt("expense");
	 					   }
	 					//   moDe = rs.getString("mode");
	 					  // workday = rs.getInt("days");
	 				  s = 1;
	 				   }
	 				
	 				   rs.close();
	 				   st.close();
	 				   con.close();
	 				   
	 			}catch(SQLException err){
	 				err.printStackTrace();
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
    @SuppressWarnings("unchecked")
	@FXML
    void fill(ActionEvent event) {
    	//String toMode = null;
    	
    	try{
    		if(selectmode.equals("wages")){
    			wage = Integer.parseInt(amount.getText());
        		//oth = Integer.parseInt(other.getText());
        		arriv = date.getValue();
       		 dateArriv = Date.valueOf(arriv);
       		 comm = "N/A";
        		toMode = selectmode;
    		}else if(selectmode.equals("others")){
    			wage = Integer.parseInt(amount.getText());
        		comm = comment.getText();
        		arriv = date.getValue();
       		 dateArriv = Date.valueOf(arriv);
        		toMode = selectmode;
    		}
    		
    	}catch(Exception e){
    		TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("insert only number");
		       tray.setMessage("choose spend type and input only numbers");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(3000));
    	}
    	
    		wd = new WorkIndicatorDialog(null, "");
 	 	   wd.addTaskEndNotification(result -> {
 	 		  String outres = result.toString();
 	          // System.out.println("nomaa "+outres);
 	           if(outres.equals("1")){
 	        	 //  wag.setText(""+wage);
 	        	//  others.setText(""+oth);
 	        	 
 	        	
 	        	 amount.setText(null);
	        	 
	        	 
 	        	 JFXSnackbar bar = new JFXSnackbar(pane);
 	        	bar.show("Data recorded ",3000);
 	           }if(outres.equals("-1")){
 	        	  JFXSnackbar bar = new JFXSnackbar(pane);
 	 	        	bar.show("Please choose spending reason",5000);  
 	           }
 	           wd=null;
 	 	   });
 	 		 wd.exec("fetch", inputParam -> {
 		           // Thinks to do...
 		           // NO ACCESS TO UI ELEMENTS!
 	 			
 	 	if(!(comm.length() == 0)){
 		  	try{
	 				
	 				con= database.dbconnect();
	 				prep=(PreparedStatement) con.prepareStatement("INSERT INTO expenseother (expense,date,reason,comment) VALUES (?,?,?,?)");
				   prep.setInt(1, wage);
	    	  prep.setDate(2,dateArriv);
	    	  prep.setString(3, toMode);
	    	  prep.setString(4, comm);
	    	 int out = prep.executeUpdate();
	    	  prep.close();
	 				   if(out >0){
	 				
	 				  s = 1;
	 				   }else{
	 					   s = 0;
	 				   }
	 				 
	 				  
	 				   con.close();
	 				   
	 			}catch(SQLException err){
	 				err.printStackTrace();
	 			}
 	 	}else{
 	 		s= -1;
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
void close(){
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
    void print(ActionEvent event) {

		  wd = new WorkIndicatorDialog(null, "fetching data");
	 	   wd.addTaskEndNotification(result -> {
	 		  String outres = result.toString();
	          // System.out.println("nomaa "+outres);
	           if(outres.equals("1")){
	        	   TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.INFORMATION);
			       tray.setTitle("all data are written in the sheet");
			       tray.setMessage("data is in normal excel format");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
			       close();
			       openFile();
			   
	           }
	           wd=null;
	 	   });
	 		 wd.exec("fetch", inputParam -> {
	 			try{
					HSSFWorkbook workbook = new HSSFWorkbook(); 
				      HSSFSheet spreadsheet = workbook
				      .createSheet("Expense");
				      HSSFRow row=spreadsheet.createRow(0);
				      HSSFCellStyle RotateStyle = workbook.createCellStyle();
				      HSSFCellStyle BoldStyle = workbook.createCellStyle();
				      RotateStyle.setRotation((short)90);
				      HSSFFont my_font = workbook.createFont();
				      Header header = spreadsheet.getHeader();
				      header.setCenter(HSSFHeader.font("Times New Roman", "Bold")+HSSFHeader.fontSize((short)9)+"SHITI YA MATUMIZI\n");

				      my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				      spreadsheet.setColumnWidth(0, 6000);
				      spreadsheet.setColumnWidth(1, 6000);
				      spreadsheet.setColumnWidth(2, 6000);
				      spreadsheet.setColumnWidth(3, 6000);
				    //  spreadsheet.setColumnWidth(4, 2000);
				      BoldStyle.setFont(my_font);
				      RotateStyle.setFont(my_font);
				      HSSFCell cell;
				      cell=row.createCell(0);
				      cell.setCellValue("SPEND TYPE");
				      cell.setCellStyle(BoldStyle);
				      cell=row.createCell(1);
				      cell.setCellValue("DESCRIPTION");
				      cell.setCellStyle(BoldStyle);
				      cell=row.createCell(2);
				      cell.setCellValue("AMOUNT");		
				      cell.setCellStyle(BoldStyle);
				      cell=row.createCell(3);
				      cell.setCellValue("DATE");
				      cell.setCellStyle(BoldStyle);
				   
				      int i=1;
				      int J=0;
				     con = database.dbconnect();
				     st = con.createStatement();
				     
				      String str = "SELECT reason,comment,expense,date FROM expenseother";
			 rs = st.executeQuery(str);
			 int tot=0;
			     while(rs.next())
			     {
			    	 
			    	 String res = rs.getString("reason").toUpperCase();
			    	 String com = rs.getString("comment").toUpperCase();
			    	 int exp = rs.getInt("expense");
			    	String date = rs.getString("date");
			    	tot+=exp;
			        row=spreadsheet.createRow(i);
			        cell=row.createCell(0);
			        cell.setCellValue(res);
			        
			            cell=row.createCell(1);
			            cell.setCellValue(com); 
			        
			        cell=row.createCell(2); 
			        cell.setCellValue(exp);
			    
			        cell=row.createCell(3);
			        cell.setCellValue(date);
			        
			        
			        i++;
			        J = i+1;
			     }
			     row=spreadsheet.createRow(J);
			     cell=row.createCell(1); 
			        cell.setCellValue("TOTAL");
			        cell.setCellStyle(BoldStyle);
			     cell=row.createCell(2); 
			        cell.setCellValue(tot);
			        cell.setCellStyle(BoldStyle);
			        close();
			     FileOutputStream out = new FileOutputStream(
			     new File("Expense.xls"));
			     workbook.write(out);
			     out.close();
			    
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, e);
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
	private void openFile() {

    	wd = new WorkIndicatorDialog(null, "opening...");
 	 	   wd.addTaskEndNotification(result -> {
 	 		  String outres = result.toString();
 	          // System.out.println("nomaa "+outres);
 	           if(outres.equals("1")){
 	        	 
 	           }else if(outres.equals("2")){
 	        	  TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("failed to open file");
			       tray.setMessage("close any excel file and try again");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
 	           }else if(outres.equals("-1")){
 	        	  TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("file doesn't exist");
			       tray.setMessage("please retry to make new file");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
 	           }
 	           wd=null;
 	 	   });
 	 	 wd.exec("fetch", inputParam -> {
 	 		 int mes=0;
 	 		try {

					if ((new File("Expense.xls")).exists()) {

						Process p = Runtime
						   .getRuntime()
						   .exec("rundll32 url.dll,FileProtocolHandler Expense.xls");
						p.waitFor();
						mes=1;	
					} else {
						mes=-1;
					}

			  	  } catch (Exception ex) {
	 	  	  		  mes = 2;
	 	  	  
	 	  		  }
 	 		 try {
                  Thread.sleep(1000);
               }	
               catch (InterruptedException er) {
                  er.printStackTrace();
               }
             
           return new Integer(mes);
 	 	 });
		
	}
	@FXML
    void showhint(ActionEvent event) {
    	 JFXSnackbar bar = new JFXSnackbar(pane);
      	bar.show("Data in this section are essential in calculating your profit. \n"
      			+ " We recommend you to always feed correct data in this section. \n"
      			+ "Choose between expenditure type and enter respective amount. "
      			+ "",7000);
    }
}
