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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ProformaListContr implements Initializable{
	  @FXML
	    private TableView<Profoma> tableview;

	    @FXML
	    private TableColumn<Profoma, String> idCol;

	    @FXML
	    private TableColumn<Profoma, String> dateCol;
	    @FXML
	    private TableColumn<Profoma, Double> amountCol;

	    @FXML
	    private TableColumn<Profoma, String> nameCol;

	    @FXML
	    private TableColumn<Profoma, String> addressCol;

	    @FXML
	    private TableColumn<Profoma, String> phoneCol;

	    @FXML
	    private JFXTextField search,amounti;

	    @FXML
	    private JFXButton btnprintPurchase,btnCheck,btNDelete;
	    @FXML
	    private StackPane pane;
	    static List<Double>amounts = new ArrayList<Double>();
		 static List<Double>prices = new ArrayList<Double>();
		 static List<Double>profits = new ArrayList<Double>();
		 static List<Integer>qties = new ArrayList<Integer>();
		 static List<String>prodnames = new ArrayList<String>();
		 static List<String>prID = new ArrayList<String>();
		 static List<String>invID = new ArrayList<String>();
		String userAmoun=null;
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs,rs1,rsT,rsC,rs4;
		    private Statement st,st1,stC,stT,st4;
		    private PreparedStatement prep;
			private WorkIndicatorDialog wd=null;
		    ObservableList<Profoma> searchdata;
		    ObservableList<Profoma> datlist;
			 List listofdat = new ArrayList();
	  static String id=null,nam,addr,phon;
	  String invoId;
	  double TempTot;
	  String cuName,cuAddr,cuPhon;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		prodnames.clear();
		qties.clear();
		amounts.clear();
		profits.clear();
		prices.clear();
		prID.clear();
		invID.clear();
		amounti.setOpacity(0);
		btNDelete.setTooltip(new Tooltip("delete proforma"));
		fetchAll();
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    	nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    	amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
    	dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    	addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
    	phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
     	tableview.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
					//System.out.println("You click at cell "+ tableview.getSelectionModel().getSelectedCells().get(0).getColumn());
				//proCode = tableProd.getSelectionModel().getSelectedItem().
				id = tableview.getSelectionModel().getSelectedItem().getId();
				nam = tableview.getSelectionModel().getSelectedItem().getName();
				addr = tableview.getSelectionModel().getSelectedItem().getAddress();
				phon = tableview.getSelectionModel().getSelectedItem().getPhone();
				
				int cellNum =  tableview.getSelectionModel().getSelectedCells().get(0).getColumn();
		 if(cellNum == 6){
		//showAdjust();
		JFXSnackbar bar = new JFXSnackbar(pane);
        	bar.show("Converting into sale...",3000);
		 }else{
			 
		 }
			
			}
			
		});
	search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			
			if (oldValue != null && (newValue.length() < oldValue.length())) {
		            	tableview.setItems(searchdata);
		            }
		            String value = newValue.toLowerCase();
		            ObservableList<Profoma> subentries = FXCollections.observableArrayList();

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
    static List<Double> getAmountArray(){
    	return amounts;
    }
    static List<Double> getProfitArray(){
    	return profits;
    }
    static List<Integer> getQtyArray(){
    	return qties;
    }
    static List<Double> getPriceArray(){
    	return prices;
    }
    static List<String> getProdArray(){
    	return prodnames;
    }
    static List<String> getProdCod(){
    	return prID;
    }
    static List<String> getInvo(){
    	return invID;
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
	 					   String query = "SELECT invoice,SUM(amount) AS 'amount',date,customer,address,phone FROM proforma_list"
	 					   		+ " WHERE NOT status='PAYED' "
	 					   		+ "GROUP BY invoice";
	 					   rs=st.executeQuery(query);
	 					  listofdat.clear();
	 					   while(rs.next()){
	 						   double amou;
	 						   String id,name,inv,dat,cust,addr,phn;
	 						//   id = rs.getString("i");
	 						   name = rs.getString("customer");
	 						   inv =  rs.getString("invoice");
	 						   dat =  rs.getString("date");
	 						   amou = rs.getDouble("amount");
	 						   addr =  rs.getString("address");
	 							phn =  rs.getString("phone");
	 						  
	 						  
	 						  listofdat.add(new Profoma(inv,dat,amou,name,addr,phn));
		 				
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

	    @FXML
	    void showAmount(ActionEvent event) {
	    	amounti.setOpacity(1);
	    	
	    }
	    @SuppressWarnings("unchecked")
		@FXML
	    void deleteList(ActionEvent event) {
	    	try{
	    	if(!id.equals(null)){

	    	wd = new WorkIndicatorDialog(null, "");
	      	 wd.addTaskEndNotification(result -> {
	      		  String outres = result.toString();
	      		  if(outres.equals("1")){
	      			JFXSnackbar bar = new JFXSnackbar(pane);
	      	      	bar.show("You deleted 1 proforma in The list...",3000);
	      	      	fetchAll();
	      		  }if(outres.equals("0")){
	      			JFXSnackbar bar = new JFXSnackbar(pane);
	      	      	bar.show("Couldnt Delete Data, retry...",3000);
	      		  }
	   	  
	      	
	      	    wd=null;
	      	 });
	      		 wd.exec("fetch", inputParam -> {
	   			int z = 0;
	   			String inv = id;
	   						try{
	   							con= database.dbconnect();
	   						 prep = (PreparedStatement) con.prepareStatement("DELETE FROM proforma_list WHERE invoice= ?"
						    	  		+ "");
							    prep.setString(1, inv);
							    int out = prep.executeUpdate();
							    if(out > 0){
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
	   	
	    	}else{
	    		JFXSnackbar bar = new JFXSnackbar(pane);
	        	bar.show("Please chooce proforma in the list below.",4000);
	    	}
	    }catch(NullPointerException nl){
	    	JFXSnackbar bar = new JFXSnackbar(pane);
        	bar.show("Please chooce proforma in the list below.",4000);
	    }
	    }
	  @SuppressWarnings("unchecked")
	@FXML
	    void showCheck(ActionEvent event) {
		  try{
		  if(!(id.length() == 0)){
			  if(!(amounti.getText().length()==0)){
				  userAmoun = amounti.getText();
				  
				  wd = new WorkIndicatorDialog(null, "");
				   	 wd.addTaskEndNotification(result -> {
				   		  String outres = result.toString();
				   		  if(outres.equals("1")){
				   			fetchAll();
				   		//	System.out.println("NIMEFIKA");
				   			  showInvoice();
				   		  }if(outres.equals("0")){
				   			System.out.println("ZEROOO");
				   		  }if(outres.equals("-1")){
				   			JFXSnackbar bar = new JFXSnackbar(pane);
				        	bar.show("Some Items have low quantity left \n PLEASE ADJUST.",4000);
				   		  }if(outres.equals("-2")){
					   			JFXSnackbar bar = new JFXSnackbar(pane);
					        	bar.show("Transaction declined, \n Make sure you have working internet.",4000);
					   		  }
					  
				   	
				   	    wd=null;
				   	 });
				   		 wd.exec("fetch", inputParam -> {
							int z = 0;
							String invo = null,dat,nm = null,adr = null,ph = null,itmId,itmNam;
							int qtyL,qt; double cos,amou;
							boolean fin = false; boolean success = false;
							java.util.Date nw = new java.util.Date();
							java.sql.Date today = new java.sql.Date(nw.getTime());
								try{
									con = database.dbconnect();
									 st= con.createStatement();
									// System.out.println("before qry");
									   String query = "SELECT proforma_list.*,products.name,products.qty_remain,products.cost FROM proforma_list,products WHERE "
									   		+ " proforma_list.invoice='"+id+"' AND proforma_list.item_id=products.code";
									   rs=st.executeQuery(query);
										while(rs.next()){
											invo = rs.getString("invoice");
											dat = rs.getString("date");
											nm = rs.getString("customer");
											adr = rs.getString("address");
											ph = rs.getString("phone");
											itmId = rs.getString("item_id");
											itmNam = rs.getString("name");
											amou = rs.getDouble("amount");
											cos = rs.getDouble("cost");
											qtyL =  rs.getInt("qty_remain");
											qt = rs.getInt("qty");
										//	System.out.println("LOOP: "+invo);
											if(qt > qtyL){
												z=-1;
											}else{
												TempTot+=amou;
												invID.add(invo); prID.add(itmId);
												prodnames.add(itmNam); prices.add((amou/qt));
												qties.add(qt);  amounts.add(amou);
												double prof = ((amou/qt) - cos)*qt;
												profits.add(prof);
												fin = true;
											}
											
										}
										
										if(fin){
											cuName = nm; cuAddr = adr; cuPhon = ph;
											for(int i=0; i<getInvo().size(); i++){
												  prep = (PreparedStatement) con.prepareStatement("INSERT INTO sales_order(invoice_no,product_code,qty,price,amount,profit,date) VALUES("
											    	  		+ "?,?,?,?,?,?,?)");
												    prep.setString(1, getInvo().get(i));
											    	  prep.setString(2, getProdCod().get(i));
											    	  prep.setInt(3, getQtyArray().get(i));
											    	  prep.setDouble(4, getPriceArray().get(i));
											    	  prep.setDouble(5, getAmountArray().get(i));
											    	  prep.setDouble(6, getProfitArray().get(i));
											    	  prep.setDate(7, today);
											    	  int out=prep.executeUpdate();
														
														if(out >0){
																success = true;
																// close statement
																prep.close();
															
														}else{
															z= -2;
														}
														prep= (PreparedStatement) con.prepareStatement("INSERT INTO sales(invoice_no,cashier,date,type,amount,customer_name) VALUES("
												    	  		+ "?,?,?,?,?,?)");
														 prep.setString(1,invo);
												    	  prep.setString(2, LoginController.getuserName());
												    	  prep.setDate(3, today);
												    	  prep.setString(4, "PROFORMA");
												    	  prep.setDouble(5,getAmountArray().get(i) );
												    	  prep.setString(6, nm);
												    	  int out2 = prep.executeUpdate();
												    	  if(out2 > 0){
												    		// System.out.println("TO SALE DONE");
												    	  }else{
												    		//  System.out.println("FAIL  TO SALE "); 
												    	  }
														prep.close();
														if(success){
															z=1;
														}
														//open new ststement for updating task
													    st= con.createStatement();
													    String sql = "UPDATE products SET qty_remain= qty_remain-"+getQtyArray().get(i)+" WHERE code='"+getProdCod().get(i)+"'";
														   st.executeUpdate(sql);
														   
														   st= con.createStatement();
														    String sql1 = "UPDATE proforma_list SET status='PAYED'  WHERE invoice='"+invo+"'";
															   st.executeUpdate(sql1);
														  // st.close();
											}
										}
										st.close();
										con.close();
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
					
			  
			  }else{
				  JFXSnackbar bar = new JFXSnackbar(pane);
		        	bar.show("Please Enter Amount received.",3000);
			  }
		  }else{
			  JFXSnackbar bar = new JFXSnackbar(pane);
	        	bar.show("Please Highlight proforma in the list.",3000);
		  }
	  }catch(NullPointerException nl){
		  JFXSnackbar bar = new JFXSnackbar(pane);
      	bar.show("Please Highlight proforma in the list.",3000);
		  nl.printStackTrace();
	  				}
	    }
	   @SuppressWarnings("unchecked")
			private void showInvoice(){
				wd = new WorkIndicatorDialog(null, "...");
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
					  //	closePDF();
						openPDFInv();
				  }
			   	
			   	    wd=null;
			   	 });
			   		 wd.exec("fetch", inputParam -> {
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
						   
						   
							file = new FileOutputStream(new File("invoice.pdf"));
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
			 				double userCash = Double.parseDouble(userAmoun);
			 	 			Double	changem = (double) (userCash - TempTot);
			 	 				con=database.dbconnect();	
			 	 				String nameco = null,adres = null,mail = null,web = null,phne = null;
			 	 				String str ="SELECT * FROM business_details";
			 	 				st4=con.createStatement();
			 	 				rs4=st4.executeQuery(str);
			 	 				while(rs4.next()){
			 	 					 nameco=rs4.getString("name");
			 	 					 adres=rs4.getString("address");
			 	 			         mail=rs4.getString("email");
			 	 					 web=rs4.getString("website");
			 	 					 phne=rs4.getString("phone");
			 	 					 
			 	 					 java.sql.Blob imageBlob = rs4.getBlob("logo");
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
			 				    
			 				    String receipt ="TS-"+year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
			 				    invoId = year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
			 				    
			 	 				  Phrase janalacompun = new Phrase(nameco, SCNAME);
			 	 			      PdfContentByte coname = writer.getDirectContent();
			 	 				  ColumnText.showTextAligned(coname, Element.ALIGN_CENTER, janalacompun, 300, 810, 0);
			 	 				
			 	 				 Phrase pobox = new Phrase(adres+" -TANZANIA", boldFontmkuu);
			 	 				 PdfContentByte box = writer.getDirectContent();
			 	 				 ColumnText.showTextAligned(box, Element.ALIGN_CENTER, pobox, 300, 800, 0);
			 	 				 
			 	 				 Phrase simu = new Phrase("Contacts: " +phne+" , "+web, phonenumba);
			 	 				 PdfContentByte mobile = writer.getDirectContent();
			 	 				 ColumnText.showTextAligned(mobile, Element.ALIGN_CENTER, simu, 300, 790, 0);
			 	 				 
			 	 				Phrase tiN = new Phrase("TIN: "+"",phonenumba);
			 	 				 PdfContentByte tin = writer.getDirectContent();
			 	 				 ColumnText.showTextAligned(tin, Element.ALIGN_CENTER, tiN, 300, 780, 0);
			 	 				 
			 	 				Phrase tit = new Phrase("INVOICE",phonenumba);
			 	 				 PdfContentByte titl = writer.getDirectContent();
			 	 				 ColumnText.showTextAligned(titl, Element.ALIGN_CENTER, tit, 300, 770, 0);
			 	 				 
			 	 				 Phrase receipno = new Phrase("Invoice No:  "+invoId, boldFontle); //RECEIPT VARIABLE HERE
			 				      PdfContentByte receip = writer.getDirectContent();
			 					  ColumnText.showTextAligned(receip, Element.ALIGN_LEFT, receipno, 110, 760, 0);
			 					
			 					  Phrase dat = new Phrase("Date:        "+tarehee, boldFontle);
			 					  PdfContentByte tar = writer.getDirectContent();
			 					  ColumnText.showTextAligned(tar, Element.ALIGN_LEFT, dat, 110, 750, 0);
			 						 
			 					 Phrase costname = new Phrase("Customer Name:  "+cuName.toUpperCase()+",  ADDRESS:_______________ ,  PHONE:_________________", boldFontle);
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
			 	 					int Rows = getProdArray().size()*50;
			 	 					int newRow =  690- Rows;
			 	 					double tota=0;
			 	 				for(int k=0; k<getProdArray().size(); k++){ //740
			 	 					
			 	 					tabler.setWidthPercentage(100);
			 	 					int ord = k+1;
			 	 				  double amou = getAmountArray().get(k);
					   		         double pri = getPriceArray().get(k);
					   		         tota+=amou;
			 	 					num = new PdfPCell(new Phrase(ord+"", maagizo));
			 	 					name = new PdfPCell(new Phrase(getProdArray().get(k), maagizo));
			 	 					qnty = new PdfPCell(new Phrase(""+getQtyArray().get(k), maagizo));
			 	 					bei = new PdfPCell(new Phrase(""+getPriceArray().get(k), maagizo));
			 	 					Amount = new PdfPCell(new Phrase(""+getAmountArray().get(k), maagizo));
			 	 					
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
			 	 				  bname= new PdfPCell(new Phrase("SALES TOTAL", boldFontle));
			 	 				  Cash= new PdfPCell(new Phrase(""+tota, boldFontle));
			 	 				  emp.setBorder(Rectangle.NO_BORDER);
			 						     tabler2.addCell(emp);
			 						     tabler2.addCell(bname);
			 							 tabler2.addCell(Cash);
			 							 
			 					 
			 			 		 imelip= new PdfPCell(new Phrase("VAT", boldFontle));
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
			 							tabler4.addCell(Cash3);						 
			 						     
			 							
			 							
			 							
			 	 				document.add(tabler2);
			 	 				document.add(tabler3);
			 	 				document.add(tabler4);
			 	 				
			 	 				 Phrase foot = new Phrase("Prepared By: "+LoginController.getuserName().toUpperCase(), boldFontle);
			 					 PdfContentByte fot = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot, Element.ALIGN_LEFT, foot, 110, newRow, 0);
			 					 
			 					Phrase foot1 = new Phrase("Goods Removed By:  "+"_______________", boldFontle);
			 					 PdfContentByte fot1 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot1, Element.ALIGN_LEFT, foot1, 110, newRow-10, 0);
			 					 
			 					Phrase foot2 = new Phrase("Goods Packed By:   "+"_______________", boldFontle);
			 					 PdfContentByte fot2 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot2, Element.ALIGN_LEFT, foot2, 110, newRow-20, 0);
			 					 
			 					Phrase foot3 = new Phrase("Goods Delivered By:"+"_______________", boldFontle);
			 					 PdfContentByte fot3 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot3, Element.ALIGN_LEFT, foot3, 110, newRow-30, 0);
			 					 
			 					Phrase foot4 = new Phrase("Received Above goods in good order and condition", boldFontle);
			 					 PdfContentByte fot4 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot4, Element.ALIGN_LEFT, foot4, 410, newRow-40, 0);
			 					Phrase foot5 = new Phrase("Received By: "+"_______________", boldFontle);
			 					 PdfContentByte fot5 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot5, Element.ALIGN_LEFT, foot5, 410, newRow-50, 0);
			 					Phrase foot6 = new Phrase("Sign:        "+"_______________", boldFontle);
			 					 PdfContentByte fot6 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot6, Element.ALIGN_LEFT, foot6, 410, newRow-60, 0);
			 	 				
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
			private void getDelivery(){
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
					//  saveTransact();
					  //	closePDF();
						openPDFD();
				  }
			   	
			   	    wd=null;
			   	 });
			   		 wd.exec("fetch", inputParam -> {
			   		//	closePDF();
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
							file = new FileOutputStream(new File("delivery.pdf"));
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
			 				double userCash = Double.parseDouble(amounti.getText());
			 	 			Double	changem = (double) (userCash - TempTot);
			 	 				con=database.dbconnect();
			 	 				String nameco = null,adres = null,mail = null,web = null,phne = null;
			 	 				String str ="SELECT * FROM business_details";
			 	 				st4=con.createStatement();
			 	 				rs4=st4.executeQuery(str);
			 	 				while(rs4.next()){
			 	 					 nameco=rs4.getString("name");
			 	 					 adres=rs4.getString("address");
			 	 			         mail=rs4.getString("email");
			 	 					 web=rs4.getString("website");
			 	 					 phne=rs4.getString("phone");
			 	 					 
			 	 					 java.sql.Blob imageBlob = rs4.getBlob("logo");
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
			 				    
			 				    String receipt ="TS-"+year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
			 				  //  invoId = year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
			 				    
			 	 				  Phrase janalacompun = new Phrase(nameco, SCNAME);
			 	 			      PdfContentByte coname = writer.getDirectContent();
			 	 				  ColumnText.showTextAligned(coname, Element.ALIGN_CENTER, janalacompun, 300, 810, 0);
			 	 				
			 	 				 Phrase pobox = new Phrase(adres+" -TANZANIA", boldFontmkuu);
			 	 				 PdfContentByte box = writer.getDirectContent();
			 	 				 ColumnText.showTextAligned(box, Element.ALIGN_CENTER, pobox, 300, 800, 0);
			 	 				 
			 	 				 Phrase simu = new Phrase("Contacts: " +phne+" , "+web, phonenumba);
			 	 				 PdfContentByte mobile = writer.getDirectContent();
			 	 				 ColumnText.showTextAligned(mobile, Element.ALIGN_CENTER, simu, 300, 790, 0);
			 	 				 
			 	 				Phrase tiN = new Phrase("TIN: "+"",phonenumba);
			 	 				 PdfContentByte tin = writer.getDirectContent();
			 	 				 ColumnText.showTextAligned(tin, Element.ALIGN_CENTER, tiN, 300, 780, 0);
			 	 				 
			 	 				Phrase tit = new Phrase("DELIVERY NOTE",phonenumba);
			 	 				 PdfContentByte titl = writer.getDirectContent();
			 	 				 ColumnText.showTextAligned(titl, Element.ALIGN_CENTER, tit, 300, 770, 0);
			 	 				 
			 	 				 Phrase receipno = new Phrase("Invoice No:  "+invoId, boldFontle); //RECEIPT VARIABLE HERE
			 				      PdfContentByte receip = writer.getDirectContent();
			 					  ColumnText.showTextAligned(receip, Element.ALIGN_LEFT, receipno, 110, 760, 0);
			 					
			 					  Phrase dat = new Phrase("Date:        "+tarehee, boldFontle);
			 					  PdfContentByte tar = writer.getDirectContent();
			 					  ColumnText.showTextAligned(tar, Element.ALIGN_LEFT, dat, 110, 750, 0);
			 						 
			 					 Phrase costname = new Phrase("Customer Name:  "+cuName.toUpperCase()+",  ADDRESS:_______________ ,  PHONE:_________________", boldFontle);
			 					 PdfContentByte cname = writer.getDirectContent();
			 					 ColumnText.showTextAligned(cname, Element.ALIGN_LEFT, costname, 110, 740, 0);
			 					 
			 					 Phrase empt = new Phrase(" ", boldFontle);
			 					 PdfContentByte empti = writer.getDirectContent();
			 					 ColumnText.showTextAligned(empti, Element.ALIGN_LEFT, empt, 110, 730, 0);
			 	 				 
			 	 				PdfPTable 	tabler = new PdfPTable(new float[] { 0, 2,2,2});
			 	 				tabler.setWidthPercentage(75);
			 	 				/*PdfPTable 	tabler2 = new PdfPTable(new float[] {6,2,2});
			 	 				tabler2.setWidthPercentage(75);
			 	 				PdfPTable 	tabler3 = new PdfPTable(new float[] {6,2,2});
			 	 				tabler3.setWidthPercentage(75);
			 	 				PdfPTable 	tabler4 = new PdfPTable(new float[] {6,2,2});
			 	 				tabler4.setWidthPercentage(75);*/
			 	 				PdfPCell name=null,qnty=null,bonus=null,num=null,Amount=null;
			 	 				PdfPCell emp=null,bname=null,Cash=null,imelip=null,Cash2=null,imebak=null,Cash3=null;
			 	 				 
			 	 					num = new PdfPCell(new Phrase("", boldFontle));
			 	 					name = new PdfPCell(new Phrase("ITEMS", boldFontle));
			 						qnty = new PdfPCell(new Phrase("QTY", boldFontle));
			 						bonus = new PdfPCell(new Phrase("BONUS", boldFontle));
			 						
			 						tabler.addCell(num);
			 	 					tabler.addCell(name);
			 	 					tabler.addCell(qnty);
			 	 					tabler.addCell(bonus);
			 	 					
			 	 					document.add(tabler);
			 	 					int Rows = getProdArray().size()*40;
			 	 					int newRow = 700 - Rows;
			 	 				for(int k=0; k<getProdArray().size(); k++){ //740
			 	 					
			 	 					tabler.setWidthPercentage(100);
			 	 					int ord = k+1;
			 	 					num = new PdfPCell(new Phrase(ord+"", maagizo));
			 	 					name = new PdfPCell(new Phrase(getProdArray().get(k), maagizo));
			 	 					qnty = new PdfPCell(new Phrase(""+getQtyArray().get(k), maagizo));
			 	 					bonus = new PdfPCell(new Phrase("+ "+0.0, maagizo));
			 	 					
			 	 				    tabler = new PdfPTable(new float[] { 0, 2,2,2});
			 	 				   tabler.setWidthPercentage(75);
			 	 					tabler.addCell(num);
			 	 					tabler.addCell(name);
			 	 					tabler.addCell(qnty);
			 	 					tabler.addCell(bonus);
			 	 					document.add(tabler);
			 	 					fin = true;
			 	 				}
			 	 				
			 	 				 Phrase foot = new Phrase("Prepared By: "+LoginController.getuserName().toUpperCase(), boldFontle);
			 					 PdfContentByte fot = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot, Element.ALIGN_LEFT, foot, 110, newRow, 0);
			 					 
			 					Phrase foot1 = new Phrase("Goods Removed By:  "+"_______________", boldFontle);
			 					 PdfContentByte fot1 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot1, Element.ALIGN_LEFT, foot1, 110, newRow-10, 0);
			 					 
			 					Phrase foot2 = new Phrase("Goods Packed By:   "+"_______________", boldFontle);
			 					 PdfContentByte fot2 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot2, Element.ALIGN_LEFT, foot2, 110, newRow-20, 0);
			 					 
			 					Phrase foot3 = new Phrase("Goods Delivered By:"+"_______________", boldFontle);
			 					 PdfContentByte fot3 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot3, Element.ALIGN_LEFT, foot3, 110, newRow-30, 0);
			 					 
			 					Phrase foot4 = new Phrase("Received Above goods in good order and condition", boldFontle);
			 					 PdfContentByte fot4 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot4, Element.ALIGN_LEFT, foot4, 410, newRow-40, 0);
			 					Phrase foot5 = new Phrase("Received By: "+"_______________", boldFontle);
			 					 PdfContentByte fot5 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot5, Element.ALIGN_LEFT, foot5, 410, newRow-50, 0);
			 					Phrase foot6 = new Phrase("Sign:        "+"_______________", boldFontle);
			 					 PdfContentByte fot6 = writer.getDirectContent();
			 					 ColumnText.showTextAligned(fot6, Element.ALIGN_LEFT, foot6, 410, newRow-60, 0);
			 	 				
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
			public void openPDFD(){
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

							if ((new File("delivery.pdf")).exists()) {

								Process p = Runtime
								   .getRuntime()
								   .exec("rundll32 url.dll,FileProtocolHandler delivery.pdf");
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

			@SuppressWarnings("unchecked")
			public void openPDFInv(){
				wd = new WorkIndicatorDialog(null, "");
			 	   wd.addTaskEndNotification(result -> {
			 		  String outres = result.toString();
			          // System.out.println("nomaa "+outres);
			           if(outres.equals("1")){
			        	   getDelivery();
			           }else{
			        	  	 
			           }
			           wd=null;
			 	   });
			 	  wd.exec("fetch", inputParam -> {
			 		  int s=0;
			 		 try {

							if ((new File("invoice.pdf")).exists()) {

								Process p = Runtime
								   .getRuntime()
								   .exec("rundll32 url.dll,FileProtocolHandler invoice.pdf");
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
	   			      .createSheet("proforma_list");
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
	   			      cell.setCellValue("PROFORMA NO.");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(1);
	   			      cell.setCellValue("DATE");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(2);
	   			      cell.setCellValue("AMOUNT");
				      cell.setCellStyle(BoldStyle);
				      cell=row.createCell(3);
	   			      cell.setCellValue("CUSTOMER NAME");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(4);
	   			      cell.setCellValue("CUSTOMER ADDRESS");
	   			      cell.setCellStyle(BoldStyle);
	   			      cell=row.createCell(5);
	   			      cell.setCellValue("CUSTOMER PHONE");
	   			      cell.setCellStyle(BoldStyle);
	   		
	   			   
	   			      int i=7; int tota=0; int totp=0;
	   			      for(int j=0;j<tableview.getItems().size(); j++){
	   		            
	   		          String id=tableview.getItems().get(j).getId();
	   		          String dat=tableview.getItems().get(j).getDate();
	   		          String nam=tableview.getItems().get(j).getName();
	   		          double amo = tableview.getItems().get(j).getAmount();
	   		          String comm = tableview.getItems().get(j).getAddress();
	   		       String phon = tableview.getItems().get(j).getPhone();
	   		         
	   		     		   	 row=spreadsheet.createRow(i);
	   		    	         cell=row.createCell(0);
	   		    	         cell.setCellValue(id);
	   		    	         cell=row.createCell(1); 
	   		    	        
	   		    	         cell.setCellValue(dat);
	   		    	         cell=row.createCell(2);
	   		    	         cell.setCellValue(amo);
	   		    	         cell.setCellStyle(BoldStyle);
	   		    	         cell=row.createCell(3);
	   		    	         cell.setCellValue(nam);
	   		    	         cell=row.createCell(4);
			    	         cell.setCellValue(comm);
			    	         cell=row.createCell(5);
	   		    	         cell.setCellValue(phon);
	   		    	    
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
	   					      new File("proforma_list.xls"));
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

					if ((new File("proforma_list.xls")).exists()) {

						Process p = Runtime
						   .getRuntime()
						   .exec("rundll32 url.dll,FileProtocolHandler proforma_list.xls");
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
