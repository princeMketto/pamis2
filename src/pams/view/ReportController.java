package pams.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ReportController implements Initializable {
Main main;
LoginController lg = new LoginController();
boolean exist = false;
NumberAxis  xAxis;
NumberAxis yAxis;
@FXML
private BorderPane reportPane;
@FXML
private JFXDatePicker startDate;

@FXML
private JFXDatePicker endDate;
@FXML
private CategoryAxis x;
@FXML
private CategoryAxis y;
@FXML
private BarChart<?, ?> graph;
@FXML
private JFXButton btnprintgraph;
@FXML
private ChoiceBox<String> length;
ObservableList<String>prodlist = FXCollections.observableArrayList();
@FXML
private JFXButton btnBack;
@FXML
private JFXButton btnsaletable;
ConnectDB database = new ConnectDB();
private Connection con;
   private ResultSet rs;
   private Statement st;
   private PreparedStatement prep,prep1;
   static String starter=null,ender=null;
   ArrayList<String> objdate = new ArrayList<String>();
   ArrayList<String> objname = new ArrayList<String>();
   ArrayList<Integer> objamount = new ArrayList<Integer>();
   ArrayList<Integer> objprofit = new ArrayList<Integer>();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	//	XYChart.Series series = new XYChart.Series();
	/*	series.getData().add(new XYChart.Data("1",23));
		series.getData().add(new XYChart.Data("2",13));
		series.getData().add(new XYChart.Data("3",68));
		series.getData().add(new XYChart.Data("4",25));
		series.getData().add(new XYChart.Data("5",12));
		series.getData().add(new XYChart.Data("6",8));
		series.getData().add(new XYChart.Data("7",45));
		series.getData().add(new XYChart.Data("8",32));
		series.getData().add(new XYChart.Data("9",39));*/
		
/*		XYChart.Series series2 = new XYChart.Series();
		series2.getData().add(new XYChart.Data("1",99));
		series2.getData().add(new XYChart.Data("2",78));
		series2.getData().add(new XYChart.Data("3",58));
		series2.getData().add(new XYChart.Data("4",25));
		series2.getData().add(new XYChart.Data("5",68));
		series2.getData().add(new XYChart.Data("6",88));
		series2.getData().add(new XYChart.Data("7",86));
		series2.getData().add(new XYChart.Data("8",13));
		series2.getData().add(new XYChart.Data("9",46));*/
		
	//	graph.getData().addAll(series,series2);
//	fillGraph();
		prodlist.clear();
		prodlist.add("Time length");
		prodlist.add("Day Report");
		prodlist.add("Week Report");
		prodlist.add("Month Report");
		prodlist.add("Year Report");
		length.setValue("Time length");
		length.setItems(prodlist);
	}
	private void fillGraph() {
		String timing,date1,dateTemp = null,name1,nameTemp = null,nextname=null;
		int amount1,amountTemp=0,amount2;
		 GregorianCalendar date = new GregorianCalendar();
		  int     day = date.get(Calendar.DAY_OF_MONTH);
		    int  month = date.get(Calendar.MONTH)+1;
		    int  year = date.get(Calendar.YEAR);
		  String  today = year+"/"+month+"/"+day;
		  int nextmonth=0;
		  nextmonth+=month;
		try{
			con= database.dbconnect();
			   st= con.createStatement();
			  
			   String query = "SELECT saltab.date, saltab.product_code, prodtab.name, SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount'"
			   		+ " FROM sales_order saltab, products prodtab"
			   		+ " WHERE saltab.date BETWEEN "+today+"' AND '"+today+"' AND saltab.product_code=prodtab.code"
			   		+ " GROUP BY saltab.date, saltab.product_code";
			   rs=st.executeQuery(query);
			   int coun = 0;
			 //  XYChart.Series series1 = new XYChart.Series();
			   while(rs.next()){
				   date1 = rs.getString("date");
					  name1 = rs.getString("name");
					  amount1 = rs.getInt("amount");
					  XYChart.Series series = new XYChart.Series();
					  series.setName(name1);
				   series.getData().add(new XYChart.Data(date1,amount1));
				   graph.getData().addAll(series);
					
		
			   }
			   //graph getdata here
			   rs.close();
			st.close();
			con.close();
			
		}catch(Exception ex){
			
		}
		
	}
    @FXML
    void fetchGraph(ActionEvent event) {
    	
    	// graph.getData().removeAll();
    	 graph.getData().clear();
    	
    	 LocalDate startD = null,endD = null,tempD;
    	 Date start = null,end = null;
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
    		 // filling bar start here
    		 String date1,dateTemp = null,name1,nameTemp = null,nextname=null;
    			int amount1,amountTemp=0,amount2;
    			
    		//	System.out.println("START:"+start+"\t END:"+end);
    			String timing = length.getSelectionModel().getSelectedItem();
    			try{
    				con= database.dbconnect();
    				   st= con.createStatement();
    				   String query = null;
    				   
    				   if(!timing.equals("Time length")){
    					   if(timing.equals("Day Report")){
    						 //  System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
    						    query = "SELECT EXTRACT(DAY FROM saltab.date) AS 'date', saltab.product_code, prodtab.name, SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount'"
    		    				   		+ " FROM sales_order saltab, products prodtab"
    		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
    		    				   		+ " GROUP BY EXTRACT(DAY FROM saltab.date), saltab.product_code";
    						    starter = start.toString();
    						    ender = end.toString();
    					   }else if(timing.equals("Week Report")){
    						  // System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
    						    query = "SELECT EXTRACT(DAY FROM saltab.date) AS 'date', saltab.product_code, prodtab.name, SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount'"
    		    				   		+ " FROM sales_order saltab, products prodtab"
    		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
    		    				   		+ " GROUP BY EXTRACT(DAY FROM saltab.date), saltab.product_code";
    						    starter = start.toString();
    						    ender = end.toString();
    					   }else if(timing.equals("Month Report")){
    						 //  System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
    						    query = "SELECT EXTRACT(MONTH FROM saltab.date) AS 'date', saltab.product_code, prodtab.name, SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount'"
    		    				   		+ " FROM sales_order saltab, products prodtab"
    		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
    		    				   		+ " GROUP BY EXTRACT(MONTH FROM saltab.date), saltab.product_code";
    						    starter = start.toString();
    						    ender = end.toString();
    					   }else if(timing.equals("Year Report")){
    						 //  System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
    						    query = "SELECT EXTRACT(YEAR FROM saltab.date) AS 'date', saltab.product_code, prodtab.name, SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount'"
    		    				   		+ " FROM sales_order saltab, products prodtab"
    		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
    		    				   		+ " GROUP BY EXTRACT(YEAR FROM saltab.date), saltab.product_code";
    						    starter = start.toString();
    						    ender = end.toString();
    					   }
    				   }else{
    					   TrayNotification tray = new TrayNotification();
    				       tray.setNotificationType(NotificationType.ERROR);
    				       tray.setTitle("PLEASE CHOOSE TIME LENGTH AND DATES");
    				       tray.setMessage("System cant generate report \n with empty time");
    				       tray.setAnimationType(AnimationType.SLIDE);
    				       tray.showAndDismiss(Duration.millis(4000));    
    				   }
    				  
    				   rs=st.executeQuery(query);
    				   exist = rs.next();
    				   int coun = 0;
    				//   XYChart.Series series1 = new XYChart.Series();
    				   	graph.setTitle("SALES GRAPH FROM "+start+" TO "+end+"\n                   "+timing);
    				   	
    				   while(rs.next()){
    					   date1 = rs.getString("date");
    						  name1 = rs.getString("name");
    						  amount1 = rs.getInt("amount");
    						 
    						  XYChart.Series series1 = new XYChart.Series();
    						  series1.setName(name1);
    					   series1.getData().add(new XYChart.Data(date1,amount1));
    					   graph.getData().addAll(series1);
    						
    			
    				   }
    				   //graph getdata here
    				if(!exist){
    					TrayNotification tray = new TrayNotification();
    				       tray.setNotificationType(NotificationType.ERROR);
    				       tray.setTitle("NO DATA FOR SELECTED DATES");
    				       tray.setMessage("System cant generate report \n with empty time");
    				       tray.setAnimationType(AnimationType.SLIDE);
    				       tray.showAndDismiss(Duration.millis(4000)); 
    				}
    			}catch(Exception ex){
    				
    			}
    		
    	 }catch(Exception er){
    		 TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("PLEASE CHOOSE START, END DATE");
		       tray.setMessage("System cant generate report \n with empty time");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));    	 
		       }
    	
    	
    }
    @FXML
    void printGraph(ActionEvent event) {

    }
    static String getStart(){
    	return starter;
    }
    static String getEnder(){
    	return ender;
    }
    @FXML
    void fetchTable(ActionEvent event) throws IOException {
  
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/ReportTable.fxml"));
		BorderPane salepane = loader.load();
		reportPane.setCenter(salepane);
  
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
