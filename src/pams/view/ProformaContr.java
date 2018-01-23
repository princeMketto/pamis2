package pams.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ProformaContr implements Initializable{

    @FXML
    private JFXTextField Cname;

    @FXML
    private JFXTextField Caddress;

    @FXML
    private JFXTextField Cphone;

    @FXML
    private JFXButton whatbtn;
    ConnectDB database = new ConnectDB();
	 private Connection con;
	    private ResultSet rs,rs1,rsT,rsC;
	    private Statement st,st1,stC,stT;
	    private PreparedStatement prep;
		private WorkIndicatorDialog wd=null;
  String mteja;
String invoicId;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	  @SuppressWarnings("unchecked")
	@FXML
	    void showProform(ActionEvent event) {
		  wd = new WorkIndicatorDialog(null, "");
		   	 wd.addTaskEndNotification(result -> {
		   		  String outres = result.toString();
		   	   if(outres.equals("1")){
		   		   openProforma();
		   		   Cname.setText(null); Caddress.setText(null); Cphone.setText(null);
		   	   }if(outres.equals("0")){
		   		TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("Please complete the form above");
			       tray.setMessage("You cant generate proforma with fault");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
		   	   }if(outres.equals("-1")){
		   		TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.ERROR);
			       tray.setTitle("Coudnt generate proforma");
			       tray.setMessage("this is unlikely to happen, check connection");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
		   	   }
			  
		   	
		   	    wd=null;
		   	 });
		   		 wd.exec("fetch", inputParam -> {
		   			 SaleController sal = new SaleController();
					int z = 0; boolean done = false;
					java.util.Date nw = new java.util.Date();
					java.sql.Date sqlnw = new java.sql.Date(nw.getTime());
					 GregorianCalendar date = new GregorianCalendar();
	 					int mill = date.get(Calendar.MILLISECOND);
	 					int sec = date.get(Calendar.SECOND);
	 					int mins = date.get(Calendar.MINUTE);
	 					int hour = date.get(Calendar.HOUR); 
	 				    int     day = date.get(Calendar.DAY_OF_MONTH);
	 				    int  month = date.get(Calendar.MONTH)+1;
	 				    int  year = date.get(Calendar.YEAR);
	 				    
	 				    String receipt =""+year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
	 				    invoicId = year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
					if(!(Cname.getText().length()==0 &&  Caddress.getText().length() ==0 && Cphone.getText().length()==0)){
				 String cuname = Cname.getText().toUpperCase();
				 mteja = cuname;
				 String cuaddress = Caddress.getText();
				 String cuphone = Cphone.getText();
				 try{
					 con = database.dbconnect();
					 for(int i=0; i<sal.getProdArray().size(); i++){
						 prep = (PreparedStatement) con.prepareStatement("INSERT INTO proforma_list(invoice,item_id,qty,amount,date,customer,address,phone,status) VALUES("
					    	  		+ "?,?,?,?,?,?,?,?,?)");
						 	prep.setString(1, invoicId);
						    prep.setString(2, sal.getProdCod().get(i));
					    	  prep.setInt(3, sal.getQtyArray().get(i));
					    	  prep.setDouble(4,sal.getAmountArray().get(i));
					    	  prep.setDate(5,sqlnw);
					    	  prep.setString(6,cuname);
					    	  prep.setString(7, cuaddress);
					    	  prep.setString(8,cuphone);
					    	  prep.setString(9, "active");
					    	  int out=prep.executeUpdate();
								
								if(out >0){
								//success;
									done = true;
									 
									// close statement
									prep.close();
								}else{
									z= -1;
								}
					    	  
					 }
					 if(done){
						 z=1;
					 }
				 }catch(SQLException sq){
					 sq.printStackTrace();
				 }
					}else{
						z=0;
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
	private void openProforma() {

		wd = new WorkIndicatorDialog(null, "");
	   	 wd.addTaskEndNotification(result -> {
	   		  String outres = result.toString();
	   		if(outres.equals("-1")){ 
	      		   TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("fail to generate delivery note");
		       tray.setMessage("this problem is unlikely to happen. consult Admin.");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
		       }
		  if(outres.equals("1")){
			//  System.out.println("DONE SUCEES");
			 
				openPDF();
		  }
	   	
	   	    wd=null;
	   	 });
	   		 wd.exec("fetch", inputParam -> {
	   			 SaleController sal = new SaleController();
	   			closePDF();
				int z = 0;
				boolean fin = false;
				//88888888888888888
				java.util.Date d=new java.util.Date();
				 java.sql.Date   tarehee=new java.sql.Date(d.getTime());
				Document document=null;
			    OutputStream file=null;
			   PdfWriter writer = null;
			   Font boldFontle = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
			   Font boldFontmkuu = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
			   Font phonenumba = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
			   Font maagizo = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
			  
			   Font SCNAME = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
			   try {
				   
				   
					file = new FileOutputStream(new File("proforma.pdf"));
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
			   document = new Document(PageSize.A4, 50, 50, 105, 50);
	 			 try {
					writer= PdfWriter.getInstance(document, file);
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
	 			 document.open();
	 			try {
	 	 		//	Double	changem = (double) (userCash - TempToatal);
	 	 				con=database.dbconnect();
	 	 				String nameco = null,adres = null,mail = null,web = null,phne = null,tins=null;
	 	 				String str ="SELECT * FROM business_details";
	 	 				st=con.createStatement();
	 	 				rs=st.executeQuery(str);
	 	 				while(rs.next()){
	 	 					 nameco=rs.getString("name");
	 	 					 adres=rs.getString("address");
	 	 			         mail=rs.getString("email");
	 	 			         tins = rs.getString("tin");
	 	 					 web=rs.getString("website");
	 	 					 phne=rs.getString("phone");
	 	 					 
	 	 					 java.sql.Blob imageBlob = rs.getBlob("logo");
	 	 			    	   byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
	 	 			    	   Image image = Image.getInstance(imageBytes);
	 	 			    	   image.scaleAbsolute(70f, 70f); // IMAGESIZE
	 	 			           image.setAbsolutePosition(110, 770);
	 	 			          document.add(image);
	 	 			     }
	 	 				 GregorianCalendar date = new GregorianCalendar();
	 					int mill = date.get(Calendar.MILLISECOND);
	 					int sec = date.get(Calendar.SECOND);
	 					int mins = date.get(Calendar.MINUTE);
	 					int hour = date.get(Calendar.HOUR); 
	 				    int     day = date.get(Calendar.DAY_OF_MONTH);
	 				    int  month = date.get(Calendar.MONTH)+1;
	 				    int  year = date.get(Calendar.YEAR);
	 				    
	 				    String receipt =""+year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
	 				    invoicId = year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
	 				    
	 	 				  Phrase janalacompun = new Phrase(nameco, SCNAME);
	 	 			      PdfContentByte coname = writer.getDirectContent();
	 	 				  ColumnText.showTextAligned(coname, Element.ALIGN_CENTER, janalacompun, 300, 810, 0);
	 	 				
	 	 				 Phrase pobox = new Phrase(adres+" -TANZANIA", boldFontmkuu);
	 	 				 PdfContentByte box = writer.getDirectContent();
	 	 				 ColumnText.showTextAligned(box, Element.ALIGN_CENTER, pobox, 300, 800, 0);
	 	 				 
	 	 				 Phrase simu = new Phrase("Contacts: " +phne+" , "+web, phonenumba);
	 	 				 PdfContentByte mobile = writer.getDirectContent();
	 	 				 ColumnText.showTextAligned(mobile, Element.ALIGN_CENTER, simu, 300, 790, 0);
	 	 				 
	 	 				Phrase tiN = new Phrase("TIN: "+tins,phonenumba);
	 	 				 PdfContentByte tin = writer.getDirectContent();
	 	 				 ColumnText.showTextAligned(tin, Element.ALIGN_CENTER, tiN, 300, 780, 0);
	 	 				 
	 	 				Phrase tit = new Phrase("PROFORMA-INVOICE",phonenumba);
	 	 				 PdfContentByte titl = writer.getDirectContent();
	 	 				 ColumnText.showTextAligned(titl, Element.ALIGN_CENTER, tit, 300, 770, 0);
	 	 				 
	 	 				 Phrase receipno = new Phrase("Proforma No:  "+invoicId, boldFontle); //RECEIPT VARIABLE HERE
	 				      PdfContentByte receip = writer.getDirectContent();
	 					  ColumnText.showTextAligned(receip, Element.ALIGN_LEFT, receipno, 110, 760, 0);
	 					
	 					  Phrase dat = new Phrase("Date:        "+tarehee, boldFontle);
	 					  PdfContentByte tar = writer.getDirectContent();
	 					  ColumnText.showTextAligned(tar, Element.ALIGN_LEFT, dat, 110, 750, 0);
	 						 
	 					 Phrase costname = new Phrase("Customer Name:  "+mteja.toUpperCase()+",  ADDRESS:_______________ ,  PHONE:_________________", boldFontle);
	 					 PdfContentByte cname = writer.getDirectContent();
	 					 ColumnText.showTextAligned(cname, Element.ALIGN_LEFT, costname, 110, 740, 0);
	 					 Phrase empt = new Phrase(" ", boldFontle);
	 					 PdfContentByte empti = writer.getDirectContent();
	 					 ColumnText.showTextAligned(empti, Element.ALIGN_LEFT, empt, 110, 730, 0);
	 					 
	 	 				 
	 	 				PdfPTable 	tabler = new PdfPTable(new float[] { 1, 2,2,2,2});
	 	 				tabler.setWidthPercentage(75);
	 	 				PdfPTable 	tabler2 = new PdfPTable(new float[] {5,2,2});
	 	 				tabler2.setWidthPercentage(75);
	 	 				PdfPTable 	tabler3 = new PdfPTable(new float[] {5,2,2});
	 	 				tabler3.setWidthPercentage(75);
	 	 				PdfPTable 	tabler4 = new PdfPTable(new float[] {5,2,2});
	 	 				tabler4.setWidthPercentage(75);
	 	 				PdfPCell name=null,qnty=null,bei=null,num=null,Amount=null;
	 	 				PdfPCell emp=null,bname=null,Cash=null,imelip=null,Cash2=null,imebak=null,Cash3=null;
	 	 				 
	 	 					num = new PdfPCell(new Phrase("ITEM No", boldFontle));
	 	 					name = new PdfPCell(new Phrase("ITEM DESCRIPTION", boldFontle));
	 						qnty = new PdfPCell(new Phrase("QUANTITY", boldFontle));
	 						bei = new PdfPCell(new Phrase("UNIT PRICE", boldFontle));
	 						Amount = new PdfPCell(new Phrase("AMOUNT", boldFontle));
	 						
	 						tabler.addCell(num);
	 	 					tabler.addCell(name);
	 	 					tabler.addCell(qnty);
	 	 					tabler.addCell(bei);
	 	 					tabler.addCell(Amount);
	 	 					
	 	 					
	 	 					document.add(tabler);
	 	 					int Rows = sal.getProdArray().size()*50;
	 	 					int newRow =  690- Rows;
	 	 					double tota=0;
	 	 				for(int k=0; k<sal.getProdArray().size(); k++){ //740
	 	 					
	 	 					tabler.setWidthPercentage(100);
	 	 					int ord = k+1;
	 	 				  double amou = sal.getAmountArray().get(k);
			   		         double pri = sal.getPriceArray().get(k);
			   		         tota+=amou;
	 	 					num = new PdfPCell(new Phrase(ord+"", maagizo));
	 	 					name = new PdfPCell(new Phrase(sal.getProdArray().get(k), maagizo));
	 	 					qnty = new PdfPCell(new Phrase(""+sal.getQtyArray().get(k), maagizo));
	 	 					bei = new PdfPCell(new Phrase(""+sal.getPriceArray().get(k), maagizo));
	 	 					Amount = new PdfPCell(new Phrase(""+sal.getAmountArray().get(k), maagizo));
	 	 					
	 	 				    tabler = new PdfPTable(new float[] { 1, 2,2,2,2});
	 	 				   tabler.setWidthPercentage(75);
	 	 					tabler.addCell(num);
	 	 					tabler.addCell(name);
	 	 					tabler.addCell(qnty);
	 	 					tabler.addCell(bei);
	 	 					tabler.addCell(Amount);
	 	 					document.add(tabler);
	 	 					fin = true;
	 	 				}
	 	 				emp= new PdfPCell(new Phrase("", maagizo));
	 	 				  bname= new PdfPCell(new Phrase("TOTAL", boldFontle));
	 	 				  Cash= new PdfPCell(new Phrase(""+tota, boldFontle));
	 	 				  emp.setBorder(Rectangle.NO_BORDER);
	 						     tabler2.addCell(emp);
	 						     tabler2.addCell(bname);
	 							 tabler2.addCell(Cash);
	 							 
	 					 
	 			 		/* imelip= new PdfPCell(new Phrase("VAT", boldFontle));
	 			 		 Cash2= new PdfPCell(new Phrase("", boldFontle));
	 			 		 emp.setBorder(Rectangle.NO_BORDER);
	 							 tabler3.addCell(emp);
	 							 tabler3.addCell(imelip);
	 							 tabler3.addCell(Cash2);
	 					imebak= new PdfPCell(new Phrase("GRAND TOTAL", boldFontle));
	 					 Cash3= new PdfPCell(new Phrase(""+tota, boldFontle));
	 					 emp.setBorder(Rectangle.NO_BORDER);
	 							tabler4.addCell(emp);
	 							tabler4.addCell(imebak);
	 							tabler4.addCell(Cash3);			*/			 
	 						     
	 							
	 							
	 							
	 	 				document.add(tabler2);
	 	 				//document.add(tabler3);
	 	 				//document.add(tabler4);
	 	 				
	 	 				 Phrase foot = new Phrase("Prepared By: "+LoginController.getuserName().toUpperCase(), boldFontle);
	 					 PdfContentByte fot = writer.getDirectContent();
	 					 ColumnText.showTextAligned(fot, Element.ALIGN_LEFT, foot, 110, newRow, 0);
	 					
	 	 				
	 	 				/* emp= new PdfPCell(new Phrase("", maagizo));
	 	 				  bname= new PdfPCell(new Phrase("SUM TOTAL", boldFontle));
	 	 				  Cash= new PdfPCell(new Phrase(""+TempToatal, boldFontle));
	 	 				  emp.setBorder(Rectangle.NO_BORDER);
	 						     tabler2.addCell(emp);
	 						     tabler2.addCell(bname);
	 							 tabler2.addCell(Cash);
	 							 
	 					 
	 			 		 imelip= new PdfPCell(new Phrase("CASH TENDERED", boldFontle));
	 			 		 Cash2= new PdfPCell(new Phrase(""+userCash, boldFontle));
	 			 		 emp.setBorder(Rectangle.NO_BORDER);
	 							 tabler3.addCell(emp);
	 							 tabler3.addCell(imelip);
	 							 tabler3.addCell(Cash2);
	 					imebak= new PdfPCell(new Phrase("CHANGE", boldFontle));
	 					 Cash3= new PdfPCell(new Phrase(""+changem, boldFontle));
	 					 emp.setBorder(Rectangle.NO_BORDER);
	 							tabler4.addCell(emp);
	 							tabler4.addCell(imebak);
	 							tabler4.addCell(Cash3);						 
	 						     
	 							
	 							
	 							
	 	 				document.add(tabler2);
	 	 				document.add(tabler3);
	 	 				document.add(tabler4);
	 					*/
	 	 				
	 	 				
	 				} catch (Exception e1) {
	 					
	 					e1.printStackTrace();
	 				}
	 			 document.close();
	 			 
					try {
					file.close();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				
				//88888888888
				if(fin){
					z=1;
				}else{	
					z=-1;
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
	public void openPDF(){
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

					if ((new File("proforma.pdf")).exists()) {

						Process p = Runtime
						   .getRuntime()
						   .exec("rundll32 url.dll,FileProtocolHandler proforma.pdf");
						p.waitFor();
						s=1;
							
					} else {
						System.out.println("File not exist");
						//JOptionPane.showMessageDialog(null, "File does not exist");

					}

					

			  	  } catch (Exception ex) {
			  		
					
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
	public void closePDF(){

		  try {
				
				

					Process pr = Runtime
					   .getRuntime()
					   .exec("taskkill /F /IM acroRd32.exe");
					pr.waitFor();
						
					 Thread.sleep(1200);   
				

		  	  } catch (Exception ex) {
		  		
				
			  }
	}
}
