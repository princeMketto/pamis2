package pams.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class SaleController implements Initializable  {
	LoginController lg = new LoginController();
	private Main main;
	
	   @FXML
	    private JFXTextField quantity;
	@FXML
    private ChoiceBox<String> choiceBox1;

    @FXML
    private JFXTextField hint;
    @FXML
    private StackPane pane;
    @FXML
    private RadioButton recNo,recYes,retS,wholS,recInv,recProf;

    @FXML
    private ToggleGroup receipt,saletype;
 
	 @FXML
	    private JFXButton btnAdd;

	   @FXML
	    private JFXButton btnBack,btnProfom;
	 
	    @FXML
	    private TableView<Items> tableview;
	    @FXML
	    private TableView<Items> Itableview;
	    @FXML
	    private TableColumn<Items, String> itmID,itmNam,genNam;
	    @FXML
	    private TableColumn<Items, Double> itmPric;
	    @FXML
	    private TableColumn<Items, Integer> itmQty;

	    @FXML
	    private TableColumn<Items, String> prodnamecol,idCol;

	    @FXML
	    private TableColumn<Items, String> categorycol;

	    @FXML
	    private TableColumn<Items, Double> pricecol;

	    @FXML
	    private TableColumn<Items, Integer> qtycol;
	    

	    @FXML
	    private TableColumn<Items, Double> amountcol;
	    public static double TempToatal,userCash;
	    public static String custName;
	    @FXML
	    private TableColumn<Items, String> actioncol;
	    @FXML
	    public Label totallabel,lbNwS,lbCur,lbSubT;
	    @FXML
	    private JFXTextField prodName,amoun;
	    @FXML
	    private JFXButton btnsave;
	    @FXML
	    private JFXPopup popup;

	    @FXML
	    private JFXListView<String> listview;

	    @FXML
	    private JFXButton btnExpand;
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs,rs0,rs4;
		    private Statement st,st0,st4;
		    private PreparedStatement prep;
		    double newamount,saleamoun ;
	    Task load;
	    private WorkIndicatorDialog wd=null;
	    boolean up,su,suc = false;
	    int s,p,sc = 0; double change,priceIn,priceWhol;
		int qn =0,iQt=0;
	    String procode = null;
		static String selectmode=null,salemode=null;
		String prodselect=null;
		String today,invoicId;
		int qt=0,qt_remain=0;
	    int batch=0;
	 static List<Double>amounts = new ArrayList<Double>();
	 static List<Double>prices = new ArrayList<Double>();
	 static List<Integer>qties = new ArrayList<Integer>();
	 static List<String>prodnames = new ArrayList<String>();
	 static List<String>prID = new ArrayList<String>();
	 ObservableList<Items>output = FXCollections.observableArrayList();
	 ObservableList<String>prodlist = FXCollections.observableArrayList();
	 ObservableList<String>itemNolist = FXCollections.observableArrayList();
	    ObservableList<Items>list = FXCollections.observableArrayList();
	    ObservableList<Items>searchdata;
	    ObservableList<Items> datlist;
		 List listofdat = new ArrayList();
	    int newBatch = 0,qty=0; double cost=0,price=0,priceGen=0,gencost=0;
		String prodcode = null,prodname=null,generic=null,sup=null;
		Date arriv,exp;
		int oldBatch;
		String cat1=null;
    	double price1=0;
    	double amounts1 = 0;
    	String avstoc,newstoc,subtot,pri,mID;
    	boolean wholeinvo = false;
    	
		 MainDashController todash = new MainDashController();
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

	    @FXML
	    void addNew(ActionEvent event) {

	    }
	 
	    private void List(){
	    	
	    }
	    private void initPop(){
	    	JFXButton b = new JFXButton("option 1");
	    	JFXButton b1 = new JFXButton("option 2");
	    	JFXButton b2= new JFXButton("option 3");
	    	
	    	b.setPadding(new Insets(10));
	    	b1.setPadding(new Insets(10));
	    	b2.setPadding(new Insets(10));
	    	
	    	VBox vbox = new VBox(b,b1,b2);
	    	
	    	popup.setContent(vbox);
	    	popup.setSource(listview);
	    }
	
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			//dropDown = new ChoiceBox<String>(FXCollections.observableArrayList("1","2","3"));
			List();
		//	initPop();
			prodnames.clear();
			qties.clear();
			amounts.clear();
			prices.clear();
			prID.clear();
			fillProd();
			//prodQuant();
			 NumberValidator validator = new NumberValidator();
			    validator.setMessage("Qty must be a number");
			    quantity.getValidators().add(validator);
			/*
			 * get selected product to quantify
			 */
			  /*  choiceBox1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			        @Override
			        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
			        //  System.out.println(choiceBox1.getItems().get((Integer) number2));
			          quantity.setText("");
			        }
			      });*/
			    recYes.setUserData("receipt"); recNo.setUserData("noreceipt");
				 recYes.setToggleGroup(receipt); recNo.setToggleGroup(receipt);
				 recInv.setUserData("invoice"); recInv.setToggleGroup(receipt);
				 recProf.setUserData("proforma"); recProf.setToggleGroup(receipt);
				 receipt.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

					@Override
					public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
						  if (receipt.getSelectedToggle() != null && !receipt.getSelectedToggle().equals("mode") ) {
							  	selectmode = receipt.getSelectedToggle().getUserData().toString();
							  		
					          		  }
						
					}
					
				});
				   retS.setUserData("retail"); wholS.setUserData("wholesale");
				   retS.setToggleGroup(saletype); wholS.setToggleGroup(saletype);
					 saletype.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

						@Override
						public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
							  if (saletype.getSelectedToggle() != null && !saletype.getSelectedToggle().equals("mode") ) {
								  	salemode = saletype.getSelectedToggle().getUserData().toString();
								  	if(salemode.equals("retail")){
								  		double salpric = priceIn ;
								  		amoun.setText(""+salpric);
								  	}
								  	if(salemode.equals("wholesale")){
								  		double salpric = priceWhol;
								  		amoun.setText(""+salpric);
								  	}
						          		  }
							
						}
						
					});
					 itmID.setCellValueFactory(new PropertyValueFactory<>("id"));
						itmNam.setCellValueFactory(new PropertyValueFactory<>("prodname"));
						genNam.setCellValueFactory(new PropertyValueFactory<>("Category"));
						itmPric.setCellValueFactory(new PropertyValueFactory<>("Price"));
						itmQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
						Itableview.setOnMouseClicked(new EventHandler<MouseEvent>(){

							@Override
							public void handle(MouseEvent arg0) {
								quantity.setText("");
									//System.out.println("You click at cell "+ tableview.getSelectionModel().getSelectedCells().get(0).getColumn());
						 int cellNum =  Itableview.getSelectionModel().getSelectedCells().get(0).getColumn();
						 if(cellNum >=0){
							 mID = Itableview.getSelectionModel().getSelectedItem().getId();
							 pri = Itableview.getSelectionModel().getSelectedItem().getPrice()+"";
							 avstoc = Itableview.getSelectionModel().getSelectedItem().getQty()+"";
							lbCur.setText(avstoc);
						 }else{
							 
						 }
							
							}
							
						});
					 
			/*choiceBox.setValue("Choose Qty");
			choiceBox.setItems(itemNolist);*/
			idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
			prodnamecol.setCellValueFactory(new PropertyValueFactory<>("prodname"));
			categorycol.setCellValueFactory(new PropertyValueFactory<>("Category"));
			pricecol.setCellValueFactory(new PropertyValueFactory<>("Price"));
			qtycol.setCellValueFactory(new PropertyValueFactory<>("Qty"));
			amountcol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
			actioncol.setCellValueFactory(new PropertyValueFactory<>("Action"));
			
			tableview.setOnMouseClicked(new EventHandler<MouseEvent>(){

				@Override
				public void handle(MouseEvent arg0) {
						//System.out.println("You click at cell "+ tableview.getSelectionModel().getSelectedCells().get(0).getColumn());
			 int cellNum =  tableview.getSelectionModel().getSelectedCells().get(0).getColumn();
			 if(cellNum == 6){
				 //get value at AMOUNT before remove remove.
				double amounty = tableview.getSelectionModel().getSelectedItem().getAmount();
				double curamount = Double.parseDouble(totallabel.getText());
				double newamount = curamount - amounty;
				if(newamount <0){
					totallabel.setText(""+0);
				}else{
				totallabel.setText(""+newamount);
				}
				 tableview.getItems().remove(output.remove(tableview.getSelectionModel().getSelectedIndex()));
			 }else{
				 
			 }
				
				}
				
			});
			hint.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
				
				if (oldValue != null && (newValue.length() < oldValue.length())) {
					Itableview.setItems(searchdata);
			            }
			            String value = newValue.toLowerCase();
			            ObservableList<Items> subentries = FXCollections.observableArrayList();

			            long count = Itableview.getColumns().stream().count();
			            for (int i = 0; i < Itableview.getItems().size(); i++) {
			                for (int j = 0; j < count; j++) {
			                    String entry = "" + Itableview.getColumns().get(j).getCellData(i);
			                    if (entry.toLowerCase().contains(value)) {
			                    	
			                        subentries.add(Itableview.getItems().get(i));
			                        break;
			                    }
			                }
			            }
			            Itableview.setItems(subentries);
			        });
			//System.out.println("CELL COUNT "+tableview.getItems().size());
			int cellcount = tableview.getItems().size();
			// IMPORTANT
		/*	choiceBox1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){ 

				@Override
				public void changed(ObservableValue<? extends String> arg0, String old, String newv) {
					if(!newv.equals("Choose Product Here")){
					 pri = newv.substring(newv.indexOf("#")+1, newv.indexOf("|"));
					 avstoc = newv.substring(newv.indexOf("@")+1, newv.indexOf(")"));
					lbCur.setText(avstoc);
					//System.out.println("VAL:"+pri+" vs "+qtL);
					}else{
					
						lbCur.setText(null);
					}
				}
				
			});*/
			
		}
		@FXML
	      void issuePrice(ActionEvent event) {
			int mQ = Integer.parseInt(quantity.getText());
			double amo = Double.parseDouble(amoun.getText());
			double nwsubt = amo * mQ;
			lbSubT.setText(""+nwsubt);
		}
		@SuppressWarnings("unchecked")
		@FXML
	      void prodQuant(ActionEvent event) {
		
			 String selected = mID;
			 prodselect = selected;
				if( prodselect==null){
		    		TrayNotification tray = new TrayNotification();
					//   Notification notification = NotificationType.SUCCESS;
				       tray.setNotificationType(NotificationType.WARNING);
				       tray.setTitle("Please Choose Item First");
				       tray.setMessage("To obtain available qty choose Item in above \n table");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(5000));
		    	}else{
		try{
			if(!(quantity.getText().length()==0 || quantity.getText().equals("")|| quantity.getText().isEmpty())){
			 qn = Integer.parseInt(quantity.getText());
			 
			 int newbl = Integer.parseInt(avstoc) - qn;
			 lbNwS.setText(""+newbl);
			
			
			}else{
				TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("Qty must be in Numbers only");
			       tray.setMessage("Enter only Numeric value");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(5000));
			       lbNwS.setText(null);  lbSubT.setText(null);
			       quantity.setText("ERR");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
		wd = new WorkIndicatorDialog(null, "");
   	 wd.addTaskEndNotification(result -> {
   		  String outres = result.toString();
   	   // System.out.println("nomaa "+outres);
   	int qt_remain = Integer.parseInt(outres);
    if(qt_remain < qn){
		  if(qt_remain == 0){
			  TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.INFORMATION);
		       tray.setTitle("SELECTED ITEM IS OUT OF STOCK...");
		       tray.setMessage("Inform storekeeper for new Item named:"+prodselect);
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
		      // checkStore(batch,procode);
		      
		  }else{
		  TrayNotification tray = new TrayNotification();
	       tray.setNotificationType(NotificationType.WARNING);
	       tray.setTitle("LARGE QTY THAN AVAILABLE QTY SELECTED");
	       tray.setMessage("use remain QTY while Sytem loads new QTY");
	       tray.setAnimationType(AnimationType.SLIDE);
	       tray.showAndDismiss(Duration.millis(5000));
	       lbNwS.setText(null);  lbSubT.setText(null);
	       quantity.setText("ERR");
		  }
	  }else{
	   	int remainPerc=(qt_remain * 100)/qt;
	   	

	  	if(remainPerc <=20 && remainPerc >=11){
	   		TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.WARNING);
		       tray.setTitle("Product with ID "+prodselect+" BELOW 20%");
		       tray.setMessage("Inform storekeeper for new Item named:"+prodselect);
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(5000));
	   	}else if(remainPerc<=10 && remainPerc >5){
	   		TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.WARNING);
		       tray.setTitle("Product with ID "+prodselect+" BELOW 10%");
		       tray.setMessage("Inform storekeeper for new Item named:"+prodselect);
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(5000));
	   	}else if(remainPerc<=5 && remainPerc >=1){
	   		TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.WARNING);
		       tray.setTitle("Product with ID "+prodselect+" BELOW 5%");
		       tray.setMessage("Inform storekeeper for new Item named:"+prodselect);
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(8000));
	   	}else if(remainPerc<=0){
	   	 TrayNotification tray = new TrayNotification();
	       tray.setNotificationType(NotificationType.INFORMATION);
	       tray.setTitle("SELECTED ITEM IS OUT OF STOCK...");
	       tray.setMessage("Inform storekeeper for new Item named:"+prodselect);
	       tray.setAnimationType(AnimationType.SLIDE);
	       tray.showAndDismiss(Duration.millis(4000));
		     //  checkStore(batch,procode);
		
	   	}
	  }
   	
   	    wd=null;
   	 });
   		 wd.exec("fetch", inputParam -> {
   	        // Thinks to do...
   	        // NO ACCESS TO UI ELEMENTS!
   			try{
   				con = database.dbconnect();
   				 st= con.createStatement();
   				   String query = "SELECT code,price,priceGen,qty,qty_remain FROM products WHERE code='"+prodselect+"'";
   				   rs=st.executeQuery(query);
   				   	if(rs.next()){
   				   		
   				   		procode = rs.getString("code");
   				   		 qt = rs.getInt("qty");
   				   		 iQt = qt;
   				   		 qt_remain = rs.getInt("qty_remain");
   				   		 priceIn = rs.getDouble("price");
   				   		 priceWhol = rs.getDouble("priceGen");
   				   		
   				   	}
   				  	rs.close();
   				  	st.close();
   				   con.close();	
   			}catch(Exception err){
   				err.printStackTrace();
   			}
   			 
   	            try {
   	               Thread.sleep(1000);
   	            }	
   	            catch (InterruptedException er) {
   	               er.printStackTrace();
   	            }
   	       
   	          
   	        return new Integer(qt_remain);
   	        
   	        
   	     });
	
		  	}    
		
	    	
		}
		
		

	/*	@SuppressWarnings("unchecked")
		private void checkStore(int batch, String procode) {
			 oldBatch = batch;	
			batch++;
			int NBatch = oldBatch+1;
			
				wd = new WorkIndicatorDialog(null, "Updating Product...");
		    	 wd.addTaskEndNotification(result -> {
		    		  String outres = result.toString();
		    	   // System.out.println("nomaa "+outres);
		    	    if(outres.equals("1")){
		    	    	TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.SUCCESS);
					       tray.setTitle("NEW BATCH SUCCESSFULLY OPENED");
					       tray.setMessage("You can now find this product in the list..");
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(4000));
		    	    }else if(outres.equals("2")){
		    	    	TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.ERROR);
					       tray.setTitle("MEDICINE NOT AVAILABLE");
					       tray.setMessage("We cant find any remain Product with ID:"+prodname);
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(5000));
					       quantity.setText("ERR");
				   		System.out.println("No product available");
		    	    }else if(outres.equals("-1")){
		    	    	TrayNotification tray = new TrayNotification();
					       tray.setNotificationType(NotificationType.ERROR);
					       tray.setTitle("MEDICINE NOT AVAILABLE");
					       tray.setMessage("We cant find any remain Product with ID:"+prodname);
					       tray.setAnimationType(AnimationType.SLIDE);
					       tray.showAndDismiss(Duration.millis(5000));
					       quantity.setText("ERR");
				   		System.out.println("No product available");
		    	    }
		    	    wd=null;
		    	 });
		    		 wd.exec("fetch", inputParam -> {
		    	        // Thinks to do...
		    	        // NO ACCESS TO UI ELEMENTS!
		    			 int J=0;
		    				try{
		    					con = database.dbconnect();
		    					 st0= con.createStatement();
		    					 String sql = "SELECT MAX(batch) AS 'batch' FROM purchased_items WHERE product_code='"+procode+"'";
		    					   rs0=st0.executeQuery(sql);
		    						if(rs0.next()){
		    							int topB = rs0.getInt("batch");
		    							if(topB > oldBatch){
		    								 st= con.createStatement();
		    		    					 String query = "SELECT * FROM purchased_items WHERE batch='"+topB+"' AND product_code='"+procode+"'";
		    		    					   rs=st.executeQuery(query);
		    		    					  	if(rs.next()){
		    		    					   		newBatch = rs.getInt("batch");
		    		    					   		prodcode = rs.getString("product_code");
		    		    					   		prodname = rs.getString("product_name");
		    		    					   		generic =  rs.getString("generic_name");
		    		    					   		qty = rs.getInt("qty");
		    		    					   		arriv = rs.getDate("arrival");
		    		    					   		exp = rs.getDate("expiry");
		    		    					   		gencost = rs.getDouble("gen_cost");
		    		    					   		cost = rs.getDouble("cost");
		    		    					   		price = rs.getDouble("price");
		    		    					   		priceGen = rs.getDouble("priceGen");
		    		    					   		sup = rs.getString("supplier");
		    		    					   		
		    		    					   		
		    		    					   		 * try to update into products table
		    		    					   		 
		    		    					   	 prep = (PreparedStatement) con.prepareStatement("UPDATE products SET batch = ?,name = ?,generic_name = ?,arrival = ?,expiry = ?,gen_cost=?,cost = ?"
		    		    							  		+ ",price = ?,priceGen=?,wholeProfit=?,profit = ?,supplier = ?,qty = ?,qty_remain = ?,total = ? WHERE code =? AND batch = ?");

		    		    					   	 	  
		    		    					   	 		prep.setInt(1, newBatch);
		    		    					    	  prep.setString(2, prodname);
		    		    					    	  prep.setString(3, generic);
		    		    					    	  prep.setDate(4, arriv);
		    		    					    	  prep.setDate(5, exp);
		    		    					    	  prep.setDouble(6, gencost);
		    		    					    	  prep.setDouble(7, cost);
		    		    					    	  prep.setDouble(8, price);
		    		    					    	  prep.setDouble(9, priceGen);
		    		    					    	  prep.setDouble(10, (priceGen-gencost));
		    		    					    	  prep.setDouble(11, (price - cost));
		    		    					    	  prep.setString(12, sup);
		    		    					    	  prep.setInt(13, qty); 
		    		    					    	  prep.setInt(14, qty); 
		    		    							prep.setDouble(15, (price*qty));
		    		    							prep.setString(16, prodcode);
		    		    							prep.setInt(17, oldBatch);
		    		    					    	  int out=prep.executeUpdate();
		    		    					    	  if(out >0){
		    		    									suc = true;
		    		    					    	  }else{
		    		    					    		  System.out.println("SOMETHING WENT WRONG");
		    		    					    		  up = true;
		    		    					    	  }
		    		    					   	}else{
		    		    					   		up = true;
		    		    					   	} 
		    		    					  	rs.close();
		    		    					   	prep.close();
		    		    					   	st.close();
		    							}else{
		    								p=-1;
		    								System.out.println("NO new batch follows....");
		    							}
		    						}
		    					
		    					   	con.close();
		    				}catch(SQLException sqler){
		    					sqler.printStackTrace();
		    				}
		    	            try {
		    	               Thread.sleep(1000);
		    	            }	
		    	            catch (InterruptedException er) {
		    	               er.printStackTrace();
		    	            }
		    	          if(suc){
		    	        	  p = 1;
		    	          }
		    	          if(up){
		    	        	  p = 2;
		    	          }
		    	          
		    	        return new Integer(p);
		    	        
		    	        
		    	     });
			
				
				
		}*/

		@SuppressWarnings("unchecked")
		private void fillProd() {
	    	prodlist.clear();
	    	prodlist.add("Choose Product Here");
	     // 	choiceBox1.setValue("Choose Product Here");
	      	for(int i=0; i<Itableview.getItems().size(); i++){
	    		
	      		Itableview.getItems().clear();
	    	}
	      	wd = new WorkIndicatorDialog(null, "Loading Items...");
	      	 wd.addTaskEndNotification(result -> {
	      		  String outres = result.toString();
	      	   // System.out.println("nomaa "+outres);
	      	    if(outres.equals("1")){
	      	   // 	choiceBox1.setItems(prodlist);
	      	    	
	      	    	 datlist = FXCollections.observableList(listofdat);
	      	    	Itableview.setItems(datlist);
	          	   searchdata =  Itableview.getItems();
	      	    }
	      	    wd=null;
	      	 });
	      		 wd.exec("fetch", inputParam -> {
	      	        // Thinks to do...
	      	        // NO ACCESS TO UI ELEMENTS!
	      			try{
	      				con= database.dbconnect();
	      				   st= con.createStatement();
	      				   String query = "SELECT * FROM products ORDER BY name";
	      				   rs=st.executeQuery(query);
	      				 listofdat.clear();
	      				   	while(rs.next()){
	      				   		String cod = rs.getString("code");
	      				   		String medic = rs.getString("name");
	      				   		String genname= rs.getString("generic_name");
	      				   		String expireat = rs.getString("expiry");
	      				   		int left = rs.getInt("qty_remain");
	      				   		double salepr = rs.getDouble("price");
	      				   		String med = "`"+cod+"-"+medic+"-"+genname+"( price:#"+salepr+"| expires in:"+expireat+" remain:@"+left+")";
	      				   		prodlist.add(med);
	      				   		listofdat.add(new Items(cod,medic,genname,salepr,left,0,""));
	      				   	}
	      				   	su = true;
	      				   	con.close();
	      			}catch(Exception err){
	      				err.printStackTrace();
	      			}
	      	            try {
	      	               Thread.sleep(1000);
	      	            }	
	      	            catch (InterruptedException er) {
	      	               er.printStackTrace();
	      	            }
	      	          if(su){
	      	        	  s = 1;
	      	          }
	      	        return new Integer(s);
	      	        
	      	        
	      	     });
	      	
	
	//	prodQuant();	
		}

	    @SuppressWarnings("unchecked")
		@FXML
	    void goHint(ActionEvent  event) {
	    	String order = hint.getText().toUpperCase();

	    	prodlist.clear();
	    	prodlist.add("Choose Product Here");
	      	choiceBox1.setValue("Choose Product Here");
	    	
	      	wd = new WorkIndicatorDialog(null, "Loading Products...");
	      	 wd.addTaskEndNotification(result -> {
	      		  String outres = result.toString();
	      	   // System.out.println("nomaa "+outres);
	      	    if(outres.equals("1")){
	      	    	choiceBox1.setItems(prodlist);
	      	    }
	      	    wd=null;
	      	 });
	      		 wd.exec("fetch", inputParam -> {
	      	        // Thinks to do...
	      	        // NO ACCESS TO UI ELEMENTS!
	      			try{
	      				con= database.dbconnect();
	      				   st= con.createStatement();
	      				   String query = "SELECT * FROM products WHERE (name LIKE '%"+order+"%' OR generic_name LIKE '%"+order+"%') ORDER BY name";
	      				   rs=st.executeQuery(query);
	      				   	while(rs.next()){
	      				   	String cod = rs.getString("code");
      				   		String medic = rs.getString("name");
      				   		String genname= rs.getString("generic_name");
      				   		String expireat = rs.getString("expiry");
      				   		int left = rs.getInt("qty_remain");
      				   		double salepr = rs.getDouble("price");
      				   		String med = "`"+cod+"-"+medic+"-"+genname+"( price:#"+salepr+"| expires in:"+expireat+" remain:@"+left+")";
      				   		prodlist.add(med);
	      				   	}
	      				   	su = true;
	      				   	con.close();
	      			}catch(Exception err){
	      				err.printStackTrace();
	      			}
	      	            try {
	      	               Thread.sleep(1000);
	      	            }	
	      	            catch (InterruptedException er) {
	      	               er.printStackTrace();
	      	            }
	      	          if(su){
	      	        	  s = 1;
	      	          }
	      	        return new Integer(s);
	      	        
	      	        
	      	     });
	      	
	
	//	prodQuant();	
		
	    }
		@SuppressWarnings("unchecked")
		@FXML
	    void AddItem(ActionEvent event) {
	    	int sum=0;
	    	newamount=0;
	    	//String selected = mID;
	    	String newprod =mID;
	    	
	    if(quantity.getText().equals("ERR")){
	    	TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("ITEM IS NOT AVAILABLE");
		       tray.setMessage("We cant find the product ");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(5000));
	    }else if(quantity.getText().isEmpty()){
	    	TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.WARNING);
		       tray.setTitle("QUANTITY NOT SET");
		       tray.setMessage("please set quantity ");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(5000));
		     
	    }else{
	   
	    	String quant = quantity.getText();
	    
	      	String cat=null;
	    	double price=0;
	    	int qt = Integer.parseInt(quant);
	    	double amounts = 0;
	    	if(!newprod.isEmpty() && !newprod.equals("") && newprod!=null && !quant.isEmpty() && !quant.equals("Choose QTY") ){
	    		if(amoun.getText().isEmpty() || amoun.getText().length()==0 || amoun.getText().equals("") ){
	    			saleamoun = 0;
	    		}else{
	    			saleamoun = Double.parseDouble(amoun.getText());
	    		}
	    		wd = new WorkIndicatorDialog(null, "Adding to cart...");
	    	   	 wd.addTaskEndNotification(result -> {
	    	   		  String outres = result.toString();
	    	   	   if(outres.equals("1")){
	    	   		tableview.setItems(output);
	    	   		totallabel.setText(""+newamount);
	    	   		amoun.setText(null);
	    	   		lbCur.setText(null); lbNwS.setText(null);
	    	   		lbSubT.setText(null); quantity.setText(null);
	    	   		retS.setSelected(false); wholS.setSelected(false);
	    	   	   }if(outres.equals("-2")){
	    	   		TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.WARNING);
				       tray.setTitle("CHOOSE TYPE OF SALE PLEASE");
				       tray.setMessage("select btn RETAIL and WHOLESALE types");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(5000));
	    	   	   }
	    		  
	    	   	
	    	   	    wd=null;
	    	   	 });
	    	   		 wd.exec("fetch", inputParam -> {
	    	   			int z = 0;
	    	   			if(salemode != null){
	    	   				if(salemode.equals("retail")){
	    	   					wholeinvo = false;
	    	   					newamount=0;
	    	    	   			String coD=null;
	    	    			
	    	    				int qt1 = Integer.parseInt(quant);
	    	    				String nam=null;
	    	    				/*
	    	    	    		 * Fetch all info on the product
	    	    	    		 */
	    	    	    		try{
	    	    	    			con= database.dbconnect();
	    	    	    			   st= con.createStatement();
	    	    	    			  
	    	    	    			   String query = "SELECT * FROM products WHERE code='"+newprod+"'";
	    	    	    			   rs=st.executeQuery(query);
	    	    	    			   	if(rs.next()){
	    	    	    			   		nam = rs.getString("name");
	    	    	    			   		 cat1 = rs.getString("description");
	    	    	    			   		 price1 = rs.getDouble("price");
	    	    	    			   		 coD = rs.getString("code");
	    	    	    			   		 z=1;
	    	    	    			   		 System.out.println("ITEM :"+nam+","+cat1+","+price1);
	    	    	    			   	}
	    	    	    			   	double curamount = Double.parseDouble(totallabel.getText());
	    	    	    			   	if(saleamoun == 0){
	    	    	    				amounts1 = qt1*price1;
	    	    	    				output.add(new Items(coD,nam,cat1,price1,qt1,amounts1,"X"));
	    	    	    				 newamount = curamount + amounts1;
	    	    	    			   	}else{
	    	    	    			   		amounts1 = saleamoun * qt1;
	    	    	    			   		output.add(new Items(coD,nam,cat1,saleamoun,qt1,amounts1,"X"));
	    	    	    			   	 newamount = curamount + amounts1;
	    	    	    			   	}
	    	    			    		
	    	    	    			//   	choiceBox1.setItems(prodlist);
	    	    	    			//   	Itableview.setItems(datlist);
	    	    	    			   	con.close();
	    	    	    		}catch(Exception err){
	    	    	    			err.printStackTrace();
	    	    	    		}
	    	   				}
	    	   				if(salemode.equals("wholesale")){
	    	   					wholeinvo = true; 
	    	   					newamount=0;
	    	    	   			String coD=null;
	    	    			
	    	    				int qt1 = Integer.parseInt(quant);
	    	    				String nam=null;
	    	    				/*
	    	    	    		 * Fetch all info on the product
	    	    	    		 */
	    	    	    		try{
	    	    	    			con= database.dbconnect();
	    	    	    			   st= con.createStatement();
	    	    	    			   String query = "SELECT * FROM products WHERE code='"+newprod+"'";
	    	    	    			   rs=st.executeQuery(query);
	    	    	    			   	if(rs.next()){
	    	    	    			   		nam = rs.getString("name");
	    	    	    			   		 cat1 = rs.getString("description");
	    	    	    			   		 price1 = rs.getInt("priceGen");
	    	    	    			   		 coD = rs.getString("code");
	    	    	    			   		 z=1;
	    	    	    			   	}
	    	    	    			   	double curamount = Double.parseDouble(totallabel.getText());
	    	    	    				if(saleamoun == 0){
		    	    	    				amounts1 = qt1*price1;
		    	    	    				output.add(new Items(coD,nam,cat1,price1,qt1,amounts1,"X"));
		    	    	    				 newamount = curamount + amounts1;
		    	    	    			   	}else{
		    	    	    			   		amounts1 = saleamoun * qt1;
		    	    	    			   		output.add(new Items(coD,nam,cat1,saleamoun,qt1,amounts1,"X"));
		    	    	    			   	 newamount = curamount + amounts1;
		    	    	    			   	}
	    	    	    			
	    	    	    			  // 	choiceBox1.setItems(prodlist);
	    	    	    			//	Itableview.setItems(datlist);
	    	    	    			   	con.close();
	    	    	    		}catch(Exception err){
	    	    	    			err.printStackTrace();
	    	    	    		}
	    	   				
	    	   				}
	    	   			}else{
	    	   				z=-2;
	    	   			}
	    	   			 /*
	    	   			  * 
	    	   			  */
	    	   			 
	    	   		
	    	   	            try {
	    	   	               Thread.sleep(1000);
	    	   	            }	
	    	   	            catch (InterruptedException er) {
	    	   	               er.printStackTrace();
	    	   	            }
	    	   	       
	    	   	          
	    	   	        return new Integer(z);
	    	   	        
	    	   	        
	    	   	     });
	    	
	    	
	    	}else{
	    		TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.WARNING);
			       tray.setTitle("CHOOSE ITEM FIRST");
			       tray.setMessage("select Item before quantity");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(5000));
	    	}
	    }
	  
	    }
	    static double getTotal(){
	    	return TempToatal;
	    }
	    static double getCash(){
	    	return userCash;
	    }
	    static String getCustName(){
	    	return custName.toUpperCase();
	    }
	    static List<Double> getAmountArray(){
	    	return amounts;
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
	    @FXML
	    void showProform(ActionEvent event) {
	    	if(selectmode !=null){
    			if(selectmode.equals("proforma")){
    				amounts.clear(); prices.clear(); qties.clear(); prodnames.clear(); prID.clear();
	    	    	for(int i=0; i<tableview.getItems().size(); i++){
	    	    		
	    	    		amounts.add(amountcol.getCellData(i));
	    	    		prices.add(pricecol.getCellData(i));
	    	    		qties.add(qtycol.getCellData(i));
	    	    		prodnames.add(prodnamecol.getCellData(i));
	    	    		prID.add(idCol.getCellData(i));
	    	    	}
    				AnchorPane infopane = null;
    		    	try{
    		    		FXMLLoader loader = new FXMLLoader();
    					loader.setLocation(getClass().getResource("Proforma.fxml"));
    					 infopane = loader.load();
    		    	}catch(Exception e){ 
    		    		e.printStackTrace();
    		    	}

    				/*
    				 * 
    				 */	JFXDialogLayout content = new JFXDialogLayout();
    			    	content.setHeading(new Text(""));
    			    	content.setBody(infopane);
    			    	content.setMaxWidth(268);
    			    	content.setMaxHeight(255);
    			    	content.setStyle("-fx-background-color: #2b89e0");
    			    	
    			    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
    			    	JFXButton bt = new JFXButton("DONE");
    			    	bt.setStyle("-fx-background-color: #ffffff");
    			    //	JFXButton bt1 = new JFXButton("Cancel");
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
    							  output.clear();
    						       totallabel.setText("0");
    						       lbNwS.setText(null);lbCur.setText(null);lbSubT.setText(null);
    						       tableview.getItems().clear();
    				    	dialog.close();
    				    	
    				    	
    						}
    			    		
    			    	});
    			    
    			    	content.setActions(bt);
    			    	dialog.show();
    			
	    		}else{
	    			TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.WARNING);
				       tray.setTitle("Use this option for proforma invoice");
				       tray.setMessage("for other modes use transaction button");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(4000));
	    		}
    		}else{
    			TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.ERROR);
			       tray.setTitle("SELECT MODE(with receipt,no receipt,etc)");
			       tray.setMessage("one of the mode must be select to continue with transaction");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
    		}
	    }
	    @FXML
	    void showDialog(ActionEvent event) {
	    	if(!tableview.getItems().isEmpty()){
	    	TempToatal = Double.parseDouble(totallabel.getText());

	    	
	    	Dialog<Pair<String, Integer>> dialog = new Dialog<>();
	    	dialog.setTitle("Customer Information");
	    	dialog.setHeaderText("Enter Payment detail Here");
	    	dialog.setGraphic(new ImageView(this.getClass().getResource("images/customer.png").toString()));
	    	// Set the button types.
	    	ButtonType confirmtypbtn = new ButtonType("Confirm", ButtonData.OK_DONE);
	    	dialog.getDialogPane().getButtonTypes().addAll(confirmtypbtn, ButtonType.CANCEL);

	    	// Create the username and password labels and fields.
	    	GridPane grid = new GridPane();
	    	grid.setHgap(10);
	    	grid.setVgap(10);
	    	grid.setPadding(new Insets(20, 150, 10, 10));

	    	JFXTextField customername = new JFXTextField();
	    	customername.setPromptText("Customer Name");
	    	customername.setLabelFloat(true);
	    	customername.setFocusColor(Color.GREEN);
	    	customername.setFont(javafx.scene.text.Font.font("Century Gothic",FontWeight.BOLD,14));
	    	customername.setUnFocusColor(Color.BLUEVIOLET);
	    	JFXTextField cash = new JFXTextField();
	    	cash.setPromptText("Cash");
	    	cash.setFont(javafx.scene.text.Font.font("Century Gothic",FontWeight.BOLD,14));
	    	cash.setLabelFloat(true);
	    	cash.setFocusColor(Color.GREEN);
	    	cash.setUnFocusColor(Color.BLUEVIOLET);
	    	
	   
	    	grid.add(customername, 1, 0);
	    	
	    	grid.add(cash, 1, 1);
	    	// Enable/Disable login button depending on whether a username was entered.
	    	Node confirmbtn = dialog.getDialogPane().lookupButton(confirmtypbtn);
	    	confirmbtn.setDisable(true);

	    	// Do some validation (using the Java 8 lambda syntax).
	    	customername.textProperty().addListener((observable, oldValue, newValue) -> {
	    		confirmbtn.setDisable(newValue.trim().isEmpty());
	    	});

	    	dialog.getDialogPane().setContent(grid);

	    	// Request focus on the username field by default.
	    	Platform.runLater(() -> customername.requestFocus());

	    	// Convert the result to a username-password-pair when the login button is clicked.
	    	dialog.setResultConverter(dialogButton -> {
	    	    if (dialogButton == confirmtypbtn) {
	    	       // return new Pair<>(customername.getText(), Integer.parseInt(cash.getText()));
	    	    	custName = customername.getText();
	    	    	userCash = Integer.parseInt(cash.getText());
	    	    	//fetch data in a table and save them in an array
	    	    	amounts.clear(); prices.clear(); qties.clear(); prodnames.clear(); prID.clear();
	    	    	for(int i=0; i<tableview.getItems().size(); i++){
	    	    		
	    	    		amounts.add(amountcol.getCellData(i));
	    	    		prices.add(pricecol.getCellData(i));
	    	    		qties.add(qtycol.getCellData(i));
	    	    		prodnames.add(prodnamecol.getCellData(i));
	    	    		prID.add(idCol.getCellData(i));
	    	    	}
	    	    	// CHECK CASH HERE
	    	    	if(userCash < TempToatal){
	    	    		amounts.clear();
	    	    		prices.clear();
	    	    		qties.clear();
	    	    		prodnames.clear();
	    	    		TrayNotification tray = new TrayNotification();
	    			       tray.setNotificationType(NotificationType.ERROR);
	    			       tray.setTitle("LOW AMOUNT OF MONEY IS ENTERED");
	    			       tray.setMessage("Cash entered is lower than ordered sum");
	    			       tray.setAnimationType(AnimationType.SLIDE);
	    			       tray.showAndDismiss(Duration.millis(4000));
	    	    	}else{
	    	    		if(selectmode !=null){
	    	    			if(selectmode.equals("receipt")){
		    	    			showReceipt();
		    	    		}else
		    	    		if(selectmode.equals("noreceipt")){
		    	    			saveTransact();
		    	    		}else if(selectmode.equals("invoice")){
		    	    			showInvoice();
		    	    		}
	    	    		}else{
	    	    			TrayNotification tray = new TrayNotification();
		    			       tray.setNotificationType(NotificationType.ERROR);
		    			       tray.setTitle("SELECT MODE(with receipt,no receipt,etc)");
		    			       tray.setMessage("one of the mode must be select to continue with transaction");
		    			       tray.setAnimationType(AnimationType.SLIDE);
		    			       tray.showAndDismiss(Duration.millis(4000));
	    	    		}
	    	    		
	    	    	}
	    	    	
	    	    }
	    	    return null;
	    	});

	    	Optional<Pair<String, Integer>> result = dialog.showAndWait();

	    	result.ifPresent(usernamePassword -> {
	    	    System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
	    	});
	    	// Get the Stage.
	    //	Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
	    }else{
	    	TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("NOTHING IS SELECTED FOR SALE");
		       tray.setMessage("you cant commit empty transaction.");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
	    	}
	    	
	    }
	    @SuppressWarnings("unchecked")
		private void saveTransact(){
	    	wd = new WorkIndicatorDialog(null, "proccessing transaction...");
	      	 wd.addTaskEndNotification(result -> {
	      		  String outres = result.toString();
	      	   if(outres.equals("-1")){
	      		 TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.ERROR);
			       tray.setTitle("Failure Performing transaction");
			       tray.setMessage("Make sure-.\n you have working internet. \n OR contact system Admin");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
			      
	      	   }if(outres.equals("-2")){ 
	      		   TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("Transaction declined");
		       tray.setMessage("Number of products is lower than selected quantity.");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
		       }
	    	   if(outres.equals("1")){
	    		   TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.SUCCESS);
			       tray.setTitle("Transaction committed with success");
			       tray.setMessage("you cant undo this transaction");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
			       output.clear();
			       totallabel.setText("0");
			       lbNwS.setText(null);lbCur.setText(null);lbSubT.setText(null);
			       tableview.getItems().clear();
			       change = userCash - TempToatal;
			       Text tx = new Text("CASH TENDERED:"+userCash+"/- \n"
									+"AMOUNT DUE:    "+TempToatal+"/- \n"
									+"CHANGE:        "+change+"/-");
			      
			       tx.setStyle("-fx-font-weight:bold;");
			     //  tx.setStyle("-fx-text-fill:white ;");
			 
				      JFXDialogLayout content = new JFXDialogLayout();
				    	content.setHeading(new Text("Transaction Status:"));
				    	content.setBody(tx);
				    	content.setStyle("-fx-background-color: #19aecc");
				    	
				    	JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.TOP);
				    	JFXButton bt = new JFXButton("Done");
				    	JFXButton bt1 = new JFXButton("Cancel");
				    	bt.setOnAction(new EventHandler<ActionEvent>(){
				    		String prodName=null;
							@Override
							public void handle(ActionEvent arg0) {
							
						
								 
								fillProd();
								 lbNwS.setText(null);lbCur.setText(null);lbSubT.setText(null);
					    	dialog.close();
					    	
					    	
							}
				    		
				    	});
				    	bt1.setOnAction(new EventHandler<ActionEvent>(){

							@Override
							public void handle(ActionEvent arg0) {
								fillProd();
								 lbNwS.setText(null);lbCur.setText(null);lbSubT.setText(null);
								dialog.close();
							}
				    		
				    	});
				    	content.setActions(bt,bt1);
				    	dialog.show();
		      	   }
	   	  
	      	
	      	    wd=null;
	      	 });
	       wd.exec("fetch", inputParam -> {
	   			int z = 0;
	   		 GregorianCalendar date = new GregorianCalendar();
				int mill = date.get(Calendar.MILLISECOND);
				int sec = date.get(Calendar.SECOND);
				int mins = date.get(Calendar.MINUTE);
				int hour = date.get(Calendar.HOUR); 
			    int     day = date.get(Calendar.DAY_OF_MONTH);
			    int  month = date.get(Calendar.MONTH)+1;
			    int  year = date.get(Calendar.YEAR);
			    String receipt ="TS-"+year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
			    invoicId = "REC-"+year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
			     today = year+"/"+month+"/"+day;
			boolean success = false;
			boolean tosale = false;
			for(int i=0; i< getProdArray().size(); i++){
				/*tableReceipt.getItems().add(new Receipt(sale.getProdArray().get(i),sale.getQtyArray().get(i),
						sale.getPriceArray().get(i),0,sale.getAmountArray().get(i)));*/
			String myCode = null;
			int myCost = 0; int qtR;
				try{
				con = database.dbconnect();
				 st= con.createStatement();
				   String query = "SELECT code,cost,qty_remain FROM products WHERE code='"+getProdCod().get(i)+"'";
				   rs=st.executeQuery(query);
				   	if(rs.next()){
				   		myCode = rs.getString("code");
				   		myCost = rs.getInt("cost");
				   		qtR = rs.getInt("qty_remain");
				   		if(qtR >= getQtyArray().get(i)){
				   		    prep = (PreparedStatement) con.prepareStatement("INSERT INTO sales_order(invoice_no,product_code,qty,price,amount,profit,date) VALUES("
					    	  		+ "?,?,?,?,?,?,?)");
						    prep.setString(1, invoicId);
					    	  prep.setString(2, myCode);
					    	  prep.setInt(3, getQtyArray().get(i));
					    	  prep.setDouble(4, getPriceArray().get(i));
					    	  prep.setDouble(5, getAmountArray().get(i));
					    	  prep.setDouble(6, (getPriceArray().get(i) - myCost)*getQtyArray().get(i));
					    	  prep.setString(7, today);
					    	  int out=prep.executeUpdate();
								
								if(out >0){
								//success;
									success = true;
									 
									// close statement
									prep.close();
								}else{
									z= -1;
								}
								prep= (PreparedStatement) con.prepareStatement("INSERT INTO sales(invoice_no,cashier,date,type,amount,customer_name) VALUES("
						    	  		+ "?,?,?,?,?,?)");
								 prep.setString(1,invoicId);
						    	  prep.setString(2, todash.getMe());
						    	  prep.setString(3, today);
						    	  prep.setString(4, "CASH");
						    	  prep.setDouble(5,getAmountArray().get(i) );
						    	  prep.setString(6, getCustName());
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
						    String sql = "UPDATE products SET qty_remain= qty_remain-"+getQtyArray().get(i)+" WHERE code='"+myCode+"'";
							   st.executeUpdate(sql);
							  // st.close();
							  
				   			
				   		}else{
				   			z=-2;
				   		}
				   	}
				  
				   	st.close();
					con.close();
				   	//**
				
			}catch(SQLException sq){
				sq.printStackTrace();
			}
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
		private void showInvoice(){
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
				  saveTransact();
				  	closePDF();
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
		 	 			Double	changem = (double) (userCash - TempToatal);
		 	 				con=database.dbconnect();
		 	 				String nameco = null,adres = null,mail = null,web = null,phne = null,tins=null;
		 	 				String str ="SELECT * FROM business_details";
		 	 				st4=con.createStatement();
		 	 				rs4=st4.executeQuery(str);
		 	 				while(rs4.next()){
		 	 					 nameco=rs4.getString("name");
		 	 					 adres=rs4.getString("address");
		 	 			         mail=rs4.getString("email");
		 	 					 web=rs4.getString("website");
		 	 					 phne=rs4.getString("phone");
		 	 					 tins = rs4.getString("tin");
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
		 	 				 
		 	 				Phrase tit = new Phrase("INVOICE",phonenumba);
		 	 				 PdfContentByte titl = writer.getDirectContent();
		 	 				 ColumnText.showTextAligned(titl, Element.ALIGN_CENTER, tit, 300, 770, 0);
		 	 				 
		 	 				 Phrase receipno = new Phrase("Invoice No:  "+invoicId, boldFontle); //RECEIPT VARIABLE HERE
		 				      PdfContentByte receip = writer.getDirectContent();
		 					  ColumnText.showTextAligned(receip, Element.ALIGN_LEFT, receipno, 110, 760, 0);
		 					
		 					  Phrase dat = new Phrase("Date:        "+tarehee, boldFontle);
		 					  PdfContentByte tar = writer.getDirectContent();
		 					  ColumnText.showTextAligned(tar, Element.ALIGN_LEFT, dat, 110, 750, 0);
		 						 
		 					 Phrase costname = new Phrase("Customer Name:  "+custName.toUpperCase()+",  ADDRESS:_______________ ,  PHONE:_________________", boldFontle);
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
		 	 				
		 	 				 Phrase foot = new Phrase("Prepared By: "+lg.getuserName().toUpperCase(), boldFontle);
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
		 	 			Double	changem = (double) (userCash - TempToatal);
		 	 				con=database.dbconnect();
		 	 				String nameco = null,adres = null,mail = null,web = null,phne = null,tins=null;
		 	 				String str ="SELECT * FROM business_details";
		 	 				st4=con.createStatement();
		 	 				rs4=st4.executeQuery(str);
		 	 				while(rs4.next()){
		 	 					 nameco=rs4.getString("name");
		 	 					 adres=rs4.getString("address");
		 	 			         mail=rs4.getString("email");
		 	 					 web=rs4.getString("website");
		 	 					 phne=rs4.getString("phone");
		 	 					 tins = rs4.getString("tin");
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
		 	 				 
		 	 				Phrase tit = new Phrase("DELIVERY NOTE",phonenumba);
		 	 				 PdfContentByte titl = writer.getDirectContent();
		 	 				 ColumnText.showTextAligned(titl, Element.ALIGN_CENTER, tit, 300, 770, 0);
		 	 				 
		 	 				 Phrase receipno = new Phrase("Invoice No:  "+invoicId, boldFontle); //RECEIPT VARIABLE HERE
		 				      PdfContentByte receip = writer.getDirectContent();
		 					  ColumnText.showTextAligned(receip, Element.ALIGN_LEFT, receipno, 110, 760, 0);
		 					
		 					  Phrase dat = new Phrase("Date:        "+tarehee, boldFontle);
		 					  PdfContentByte tar = writer.getDirectContent();
		 					  ColumnText.showTextAligned(tar, Element.ALIGN_LEFT, dat, 110, 750, 0);
		 						 
		 					 Phrase costname = new Phrase("Customer Name:  "+custName.toUpperCase()+",  ADDRESS:_______________ ,  PHONE:_________________", boldFontle);
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
		 	 				
		 	 				 Phrase foot = new Phrase("Prepared By: "+lg.getuserName().toUpperCase(), boldFontle);
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
		private void showReceipt() {
			wd = new WorkIndicatorDialog(null, "Fetching information...");
		   	 wd.addTaskEndNotification(result -> {
		   		  String outres = result.toString();
		   		if(outres.equals("-1")){ 
		      		   TrayNotification tray = new TrayNotification();
			       tray.setNotificationType(NotificationType.ERROR);
			       tray.setTitle("Transaction declined");
			       tray.setMessage("Number of products is lower than selected quantity.");
			       tray.setAnimationType(AnimationType.SLIDE);
			       tray.showAndDismiss(Duration.millis(4000));
			       }
			  if(outres.equals("1")){
				//  System.out.println("DONE SUCEES");
				  saveTransact();
				  	closePDF();
					openPDF();
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
						file = new FileOutputStream(new File("receipt.pdf"));
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
		 	 			Double	changem = (double) (userCash - TempToatal);
		 	 				con=database.dbconnect();
		 	 				String nameco = null,adres = null,mail = null,web = null,phne = null,tins=null;
		 	 				String str ="SELECT * FROM business_details";
		 	 				st4=con.createStatement();
		 	 				rs4=st4.executeQuery(str);
		 	 				while(rs4.next()){
		 	 					 nameco=rs4.getString("name");
		 	 					 adres=rs4.getString("address");
		 	 			         mail=rs4.getString("email");
		 	 					 web=rs4.getString("website");
		 	 					 phne=rs4.getString("phone");
		 	 					 tins = rs4.getString("tin");
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
		 				    
		 				    String receipt =""+year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
		 				    invoicId = +year+""+month+""+day+""+hour+""+mins+""+sec+""+mill;
		 				    
		 	 				  Phrase janalacompun = new Phrase(nameco, SCNAME);
		 	 			      PdfContentByte coname = writer.getDirectContent();
		 	 				  ColumnText.showTextAligned(coname, Element.ALIGN_CENTER, janalacompun, 300, 810, 0);
		 	 				
		 	 				 Phrase pobox = new Phrase(adres+" -TANZANIA", boldFontmkuu);
		 	 				 PdfContentByte box = writer.getDirectContent();
		 	 				 ColumnText.showTextAligned(box, Element.ALIGN_CENTER, pobox, 300, 800, 0);
		 	 				 
		 	 				 Phrase simu = new Phrase("Contact1: " +phne+", Contact2: "+web, phonenumba);
		 	 				 PdfContentByte mobile = writer.getDirectContent();
		 	 				 ColumnText.showTextAligned(mobile, Element.ALIGN_CENTER, simu, 300, 790, 0);
		 	 				 
		 	 				 Phrase tin = new Phrase("Tin No: " +tins, phonenumba);
		 	 				 PdfContentByte tini = writer.getDirectContent();
		 	 				 ColumnText.showTextAligned(tini, Element.ALIGN_CENTER, tin, 300, 780, 0);
		 	 				 
		 	 				 Phrase receipno = new Phrase("Receipt No:  "+invoicId, boldFontle); //RECEIPT VARIABLE HERE
		 				      PdfContentByte receip = writer.getDirectContent();
		 					  ColumnText.showTextAligned(receip, Element.ALIGN_LEFT, receipno, 110, 770, 0);
		 					
		 					  Phrase dat = new Phrase("Date:        "+tarehee, boldFontle);
		 					  PdfContentByte tar = writer.getDirectContent();
		 					  ColumnText.showTextAligned(tar, Element.ALIGN_LEFT, dat, 110, 760, 0);
		 						 
		 					 Phrase costname = new Phrase("Customer Name:  "+custName, boldFontle);
		 					 PdfContentByte cname = writer.getDirectContent();
		 					 ColumnText.showTextAligned(cname, Element.ALIGN_LEFT, costname, 110, 750, 0);
		 					 
		 					 
		 	 				 
		 	 				PdfPTable 	tabler = new PdfPTable(new float[] { 2, 2,2,2,2});
		 	 				tabler.setWidthPercentage(75);
		 	 				PdfPTable 	tabler2 = new PdfPTable(new float[] {6,2,2});
		 	 				tabler2.setWidthPercentage(75);
		 	 				PdfPTable 	tabler3 = new PdfPTable(new float[] {6,2,2});
		 	 				tabler3.setWidthPercentage(75);
		 	 				PdfPTable 	tabler4 = new PdfPTable(new float[] {6,2,2});
		 	 				tabler4.setWidthPercentage(75);
		 	 				PdfPCell name=null,qnty=null,price=null,discont=null,Amount=null;
		 	 				PdfPCell emp=null,bname=null,Cash=null,imelip=null,Cash2=null,imebak=null,Cash3=null;
		 	 				 
		 	 				    name = new PdfPCell(new Phrase("ITEM NAME", boldFontle));
		 						qnty = new PdfPCell(new Phrase("QTY", boldFontle));
		 						price = new PdfPCell(new Phrase("UNIT PRICE", boldFontle));
		 						discont = new PdfPCell(new Phrase("DISCOUNT", boldFontle));
		 						Amount = new PdfPCell(new Phrase("AMOUNT", boldFontle));
		 						tabler.addCell(name);
		 	 					tabler.addCell(qnty);
		 	 					tabler.addCell(price);
		 	 					tabler.addCell(discont);
		 	 					tabler.addCell(Amount);
		 	 					document.add(tabler);
		 	 				for(int k=0; k<getProdArray().size(); k++){
		 	 					
		 	 					tabler.setWidthPercentage(100);
		 	 					name = new PdfPCell(new Phrase(getProdArray().get(k), maagizo));
		 	 					qnty = new PdfPCell(new Phrase(""+getQtyArray().get(k), maagizo));
		 	 					price = new PdfPCell(new Phrase(""+getPriceArray().get(k), maagizo));
		 	 					discont = new PdfPCell(new Phrase("0.0", maagizo));
		 	 					Amount = new PdfPCell(new Phrase(""+getAmountArray().get(k), maagizo));
		 	 				    tabler = new PdfPTable(new float[] { 2, 2,2,2,2});
		 	 				   tabler.setWidthPercentage(75);
		 	 					tabler.addCell(name);
		 	 					tabler.addCell(qnty);
		 	 					tabler.addCell(price);
		 	 					tabler.addCell(discont);
		 	 					tabler.addCell(Amount);
		 	 					document.add(tabler);
		 	 					fin = true;
		 	 				}
		 	 				   emp= new PdfPCell(new Phrase("", maagizo));
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
		public void openPDF(){
			wd = new WorkIndicatorDialog(null, "Opening...");
		 	   wd.addTaskEndNotification(result -> {
		 		  String outres = result.toString();
		          // System.out.println("nomaa "+outres);
		           if(outres.equals("1")){
		        	  
		           }else{
		        	  	 
		           }
		           wd=null;
		 	   });
		 	  wd.exec("fetch", inputParam -> {
		 		 try {

						if ((new File("receipt.pdf")).exists()) {

							Process p = Runtime
							   .getRuntime()
							   .exec("rundll32 url.dll,FileProtocolHandler receipt.pdf");
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

						if ((new File("invoice.xls")).exists()) {

							Process p = Runtime
							   .getRuntime()
							   .exec("rundll32 url.dll,FileProtocolHandler invoice.xls");
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
	    @SuppressWarnings("unchecked")
		public void openD(){
			
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

						if ((new File("delivery.xls")).exists()) {

							Process p = Runtime
							   .getRuntime()
							   .exec("rundll32 url.dll,FileProtocolHandler delivery.xls");
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
