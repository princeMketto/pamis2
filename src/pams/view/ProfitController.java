package pams.view;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ProfitController implements Initializable {
	

	    @FXML
	    private ChoiceBox<String> length;

	    @FXML
	    private JFXDatePicker startDate;
	    @FXML
	    private CategoryAxis x;
	    @FXML
	    private CategoryAxis y;
	    @FXML
	    private JFXDatePicker endDate;

	    @FXML
	    private AreaChart<String,Double> graph1;

	    @FXML
	    private AreaChart<?, ?> graph2;
	    @FXML
	    private BarChart<String, Double> graph;
	    @FXML
	    private Text netpro;

	    @FXML
	    private JFXButton goBtnhintbtn;

	    @FXML
	    private AnchorPane anchor;
	    @FXML
	    private Text profit;

	    @FXML
	    private Text loss;

	    @FXML
	    private JFXButton helpbtn;
	    ConnectDB database = new ConnectDB();
		 private Connection con;
		    private ResultSet rs,rs1;
		    private Statement st,st1;
		    private PreparedStatement prep,prep1,prep2;
		    private WorkIndicatorDialog wd=null;
	    ObservableList<String>prodlist = FXCollections.observableArrayList();
	    static String starter=null,ender=null;
	 boolean exist = false;
	 boolean netday = false;	 boolean netweek = false;	 boolean netmonth = false;	 boolean netyear = false;
	  
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		initGraph();
	}
	   private void initGraph() {
		   prodlist.clear();
			prodlist.add("Time length");
			prodlist.add("Day Report");
			prodlist.add("Week Report");
			prodlist.add("Month Report");
			prodlist.add("Year Report");
			length.setValue("Time length");
			length.setItems(prodlist);
	}
	@FXML
	    void fill(ActionEvent event) {
		   graph1.getData().clear();
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
	    			
	    			String timing = length.getSelectionModel().getSelectedItem();
	    			//System.out.println("START:"+start+"\t END:"+end+"\t TIME:"+timing);
	    			
	    			try{

	    				con= database.dbconnect();
	    				   st= con.createStatement();
	    				   String query = null;
	    				   String query1 = null;
	    				   if(!timing.equals("Time length")){
	    					   if(timing.equals("Day Report")){
	    						 //  System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
	    						    query = "SELECT EXTRACT(DAY FROM saltab.date) AS 'date', SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount',"
	    						    		+ " SUM( saltab.profit ) AS 'profit' "
	    		    				   		+ " FROM sales_order saltab, products prodtab"
	    		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
	    		    				   		+ " GROUP BY EXTRACT(DAY FROM saltab.date)";
	    						    starter = start.toString();
	    						    ender = end.toString();
	    						   
	    					   }else if(timing.equals("Week Report")){
	    						  // System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
	    						    query = "SELECT EXTRACT(DAY FROM saltab.date) AS 'date', SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount',"
	    						    		+ "SUM( saltab.profit ) AS 'profit' "
	    		    				   		+ " FROM sales_order saltab, products prodtab"
	    		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
	    		    				   		+ " GROUP BY EXTRACT(DAY FROM saltab.date)";
	    						    starter = start.toString();
	    						    ender = end.toString();
	    						   
	    					   }else if(timing.equals("Month Report")){
	    						 //  System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
	    						    query = "SELECT EXTRACT(MONTH FROM saltab.date) AS 'date', SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount',"
	    						    		+ "SUM( saltab.profit ) AS 'profit' "
	    		    				   		+ " FROM sales_order saltab, products prodtab"
	    		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
	    		    				   		+ " GROUP BY EXTRACT(MONTH FROM saltab.date)";
	    						    starter = start.toString();
	    						    ender = end.toString();
	    						    
	    					   }else if(timing.equals("Year Report")){
	    						 //  System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
	    						    query = "SELECT EXTRACT(YEAR FROM saltab.date) AS 'date', SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount',"
	    						    		+ "SUM( saltab.profit ) AS 'profit' "
	    		    				   		+ " FROM sales_order saltab, products prodtab"
	    		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
	    		    				   		+ " GROUP BY EXTRACT(YEAR FROM saltab.date)";
	    						    starter = start.toString();
	    						    ender = end.toString();
	    						    
	    					   }
	    				    
	    				    fillInd(start,end,starter,ender);
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
	    				   boolean nopass = true;
	    				//   XYChart.Series series1 = new XYChart.Series();
	    				   	graph1.setTitle("GENERAL PROFIT SINCE "+start+" TO "+end+" BASED ON: "+timing);
	    				    XYChart.Series<String,Double> series1 = new XYChart.Series<>();
	    				    int tot = 0;
	    				    double totP=0;
	    				   while(rs.next()){
	    					  nopass = false;
	    					   date1 = rs.getString("date");
	    						  name1 = rs.getString("profit");
	    						  amount1 = rs.getInt("amount");
	    						  tot+=amount1;
	    						 
	    						  double prof = Math.round((Double.parseDouble(name1)) * 100) / 100;
	    						// prof+=prof;
	    						  series1.setName("TOTAL AMOUNT:"+tot);
	    					   series1.getData().add(new XYChart.Data<>(date1,prof));
	    					  	    						
	    						 totP+=prof;
	    						
	    				   }
	    				
	    				   st1= con.createStatement();
	    				   String sql = "SELECT SUM(expense) AS 'expense',reason AS 'others'"
	    				   		+ " FROM expenseOther WHERE date BETWEEN '"+start+"' AND '"+end+"'";
	    				   rs1=st1.executeQuery(sql);
	    				   double mynet = 0;
	    				   
	    				   if(rs1.next()){
	    					   int expense = rs1.getInt("expense");
	    					   String reas = rs1.getString("others");
	    					  mynet = totP-expense;
	    					  if(mynet > 0){
	    						  profit.setText("Profit");
	    						  loss.setText(null);
	    					  }
	    					  if(mynet < 0){
	    						  profit.setText(null);
	    						  loss.setText("Loss");
	    					  }
	    					 netpro.setText(""+(totP-expense));
	    				   }else{
	    					   mynet = totP;
	    					   if(mynet > 0){
		    						  profit.setText("Profit");
		    						  loss.setText(null);
		    					  }
		    					  if(mynet < 0){
		    						  profit.setText(null);
		    						  loss.setText("Loss");
		    					  }
	    					   netpro.setText(""+(totP));
	    				   }
	    				
	    				  // netpro.setText(""+(tot-mynet));
	    				   graph1.getData().addAll(series1);
	    				   st1.close();
	    				   rs1.close();
	    				   //graph getdata here
	    				  

	    				if(!exist){
	    					TrayNotification tray = new TrayNotification();
	    				       tray.setNotificationType(NotificationType.ERROR);
	    				       tray.setTitle("NO DATA FOR SELECTED DATES");
	    				       tray.setMessage("System cant generate profit \n with empty time");
	    				       tray.setAnimationType(AnimationType.SLIDE);
	    				       tray.showAndDismiss(Duration.millis(4000)); 
	    				}
	    			
	    			}catch(SQLException sq){
	    				sq.printStackTrace();
	    				/*TrayNotification tray = new TrayNotification();
 				       tray.setNotificationType(NotificationType.ERROR);
 				       tray.setTitle("NO DATA FOR SELECTED DATES");
 				       tray.setMessage("System cant generate profit \n with empty time");
 				       tray.setAnimationType(AnimationType.SLIDE);
 				       tray.showAndDismiss(Duration.millis(4000)); */
	    			}
		   }catch(Exception e){
			   e.printStackTrace();
			   TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("PLEASE CHOOSE START, END DATE");
		       tray.setMessage("System cant calculate profit \n with empty time");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));    
		   }
	    }

	  private void fillInd(Date start, Date end, String starter2, String ender2) {
		  graph.getData().clear();
		  String date1,dateTemp = null,name1,name = null,nextname=null;
			int amount1,amountTemp=0,amount2;
		  String timing = length.getSelectionModel().getSelectedItem();
			//System.out.println("START:"+start+"\t END:"+end+"\t TIME:"+timing);
			
			try{

				con= database.dbconnect();
				   st= con.createStatement();
				   String query = null;
				 
				   if(!timing.equals("Time length")){
					   if(timing.equals("Day Report")){
						 //  System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
						    query = "SELECT EXTRACT(DAY FROM saltab.date) AS 'date', saltab.product_code, prodtab.name, SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount',"
						    		+ " SUM( saltab.profit ) AS 'profit' "
		    				   		+ " FROM sales_order saltab, products prodtab"
		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
		    				   		+ " GROUP BY EXTRACT(DAY FROM saltab.date), saltab.product_code";
						    starter = start.toString();
						    ender = end.toString();
					   }else if(timing.equals("Week Report")){
						  // System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
						    query = "SELECT EXTRACT(DAY FROM saltab.date) AS 'date', saltab.product_code, prodtab.name, SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount',"
						    		+ "SUM( saltab.profit ) AS 'profit' "
		    				   		+ " FROM sales_order saltab, products prodtab"
		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
		    				   		+ " GROUP BY EXTRACT(DAY FROM saltab.date), saltab.product_code";
						    starter = start.toString();
						    ender = end.toString();
					   }else if(timing.equals("Month Report")){
						 //  System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
						    query = "SELECT EXTRACT(MONTH FROM saltab.date) AS 'date', saltab.product_code, prodtab.name, SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount',"
						    		+ "SUM( saltab.profit ) AS 'profit' "
		    				   		+ " FROM sales_order saltab, products prodtab"
		    				   		+ " WHERE saltab.date BETWEEN '"+start+"' AND '"+end+"' AND saltab.product_code=prodtab.code"
		    				   		+ " GROUP BY EXTRACT(MONTH FROM saltab.date), saltab.product_code";
						    starter = start.toString();
						    ender = end.toString();
					   }else if(timing.equals("Year Report")){
						 //  System.out.println("DAYS:"+spandays+"\t MONTH:"+spanmonth+"\t YEAR:"+spanyear);
						    query = "SELECT EXTRACT(YEAR FROM saltab.date) AS 'date', saltab.product_code, prodtab.name, SUM( saltab.qty ) AS 'qty', SUM( saltab.amount ) AS 'amount',"
						    		+ "SUM( saltab.profit ) AS 'profit' "
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
				   	graph.setTitle("PARTICULAR PROFIT SINCE "+start+" TO "+end+" BASED ON: "+timing);
				   
				    int tot = 0;
				   while(rs.next()){
					  
					   date1 = rs.getString("date");
					   name = rs.getString("name");
						  name1 = rs.getString("profit");
						  amount1 = rs.getInt("amount");
						  tot+=amount1;
					double prof = Math.round((Double.parseDouble(name1)) * 100) / 100;
					 XYChart.Series<String,Double> series1 = new XYChart.Series<>();
						  series1.setName("NAME:"+name+"| AMOUNT:"+amount1);
					   series1.getData().add(new XYChart.Data<>(date1,prof));
					  	    						
					   graph.getData().addAll(series1); 
						
				   }
				   
				   //graph getdata here
				  

				if(!exist){
					TrayNotification tray = new TrayNotification();
				       tray.setNotificationType(NotificationType.ERROR);
				       tray.setTitle("NO DATA FOR SELECTED DATES");
				       tray.setMessage("System cant generate profit \n with empty time");
				       tray.setAnimationType(AnimationType.SLIDE);
				       tray.showAndDismiss(Duration.millis(4000)); 
				}
			
			}catch(SQLException sq){
				sq.printStackTrace();
				/*TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("NO DATA FOR SELECTED DATES");
		       tray.setMessage("System cant generate profit \n with empty time");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000)); */
			}		
	}
	@FXML
	    void showHint(ActionEvent event) {

	    }
}
