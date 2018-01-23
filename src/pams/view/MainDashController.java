package pams.view;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Duration;
import pams.Main;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class MainDashController implements Initializable {
	private Main main;
	@FXML
	JFXListView<String> listview;
	   @FXML
	    private StackPane stackbase;
    @FXML
    private BorderPane borderpane;

    @FXML
    private JFXHamburger ham;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXButton btnsale,btnManage,btnStore,btnpatient,btnmed,btnTrack,btnProft,btnSale;
    @FXML
    private JFXButton btnprod;

    @FXML
    private JFXButton btncustomer;

    @FXML
    private JFXButton btnsupp;

    @FXML
    private JFXButton btnhelp;

    @FXML
    private JFXButton btnlogout;
    @FXML
    private JFXButton setting;
    @FXML
    private JFXButton btnpharma;

    @FXML
    private JFXButton btnreport;
    
    @FXML
    private JFXPopup popup;
    @FXML
    private JFXButton btnChat;
    /*
     * NAVIGATION
     */
    @FXML
    private JFXButton drawSale;

    @FXML
    private JFXButton drawProd;

    @FXML
    private JFXButton drawCust;

    @FXML
    private JFXButton drawSupp;

    @FXML
    private JFXButton drawHelp;
    @FXML
    private ImageView logOffPi;
    @FXML
    private StackPane stackDash;

    @FXML
    private Label unam,fulstat,fulphone,fulmail,fulnam;
    @FXML
    private Label logOffLab;
    static String user = null;
    @FXML
    private JFXButton btnInventory;
    @FXML
    private JFXButton btnProf,btnabout;
    @FXML
    private JFXButton btnPurchase;
    @FXML
    private JFXButton btnwage,btnEd;
    @FXML
    private JFXButton btnunforessen;

    @FXML
    private BarChart<?, ?> barchart;
    @FXML
    private Label gdata,labsale,labpurch,labstock,labprod,labToS,labToPu,labMode,labWatch;
    @FXML
    private ProgressBar purchP;

    @FXML
    private ProgressBar saleP;
    @FXML
    private Label labP,labS,labD,labSta;
    @FXML
    private FontAwesomeIconView fntU;

    @FXML
    private FontAwesomeIconView fntD;
    @FXML
    private PieChart piechart;
    private WorkIndicatorDialog wd=null;
    LoginController lg = new LoginController();
    ObservableList<String>items = FXCollections.observableArrayList();
    ObservableList<Integer>analy = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> pieChartData;
    ConnectDB database = new ConnectDB();
    private Connection con;
    private ResultSet rs,rs1,rs2;
    private Statement st,st1,st2;
    private PreparedStatement prep;
    int totsal=0,totpurch=0,counters=0,counterp=0,prod=0;
    String fnam = null,lnam = null,usnam = null,phon = null,mail = null,statu=null;
    String mauzo,maununu,aina,leo;
    /*
     * 
     */
    JPopupMenu menu;
    ContextMenu context;
    
	@Override
	public void initialize(URL url, ResourceBundle rb){
		user = lg.getuser();
		unam.setText(lg.getStats()+":"+user);
		//ustat.setText(lg.getStats());
		drawer.isHidden();
		fillGraph();
		java.util.Date nw = new java.util.Date();
		java.sql.Date nw1 = new java.sql.Date(nw.getTime());
		labD.setText(nw1+"");
		
		 try {
	            VBox box = FXMLLoader.load(getClass().getResource("DrawerContent.fxml"));
	            drawer.setSidePane(box);
	            
	            for(Node node : box.getChildren()){
	            	if(node.getAccessibleText() != null){
	            		node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->{
	            			switch(node.getAccessibleText()){
	            			case "Material_one":
	            		//	System.out.println("You CLICKED1");
	            				goSale();
	            			break;
	            			case "Material_two":
		            			try {
									goProducts();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
		            			break;
	            			case "Material_three":
		            			try {
									goInvents();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
		            			break;
	            			case "Material_four":
		            			try {
									goStore();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
		            		
		            			break;
	            			case "Material_five":
	            				try {
									goSupp();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
		            			
		            			break;
	            			case "Material_six":
	            				try {
									logOut();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
		            			
		            			break;
	            			}
	            		});
	            	}
	            }
	            
	      
		 
		HamburgerSlideCloseTransition trans = new HamburgerSlideCloseTransition(ham);
		trans.setRate(-1);
		ham.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->{
			trans.setRate(trans.getRate() * -1);
			trans.play();
			
			if(drawer.isShown())
            {
                drawer.close();
            }else
                drawer.open();
		
		});
		//initPopup();
		//Navigate through Dashboard drawer item
		  } catch (IOException ex) {
	          ex.printStackTrace();
			  // Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
	        }
	}
	  @FXML
	    void goMeds(ActionEvent event) {
		  TrayNotification tray = new TrayNotification();
	       tray.setNotificationType(NotificationType.NOTICE);
	       tray.setTitle("Upgrade your application to access this feature");
	       tray.setMessage("This feature is available in Ultimate package ..");
	       tray.setAnimationType(AnimationType.POPUP);
	       tray.showAndDismiss(Duration.millis(5000));
	    }
	   @SuppressWarnings("unchecked")
		private void reFill() {
	    	wd = new WorkIndicatorDialog(null, "...");
		 	   wd.addTaskEndNotification(result -> {
		 		  String outres = result.toString();
		          // System.out.println("nomaa "+outres);
		           if(outres.equals("1")){
		        	   fulnam.setText(fnam+" "+lnam);
	 				  
	 				   fulmail.setText(mail);
	 				   fulphone.setText(phon);
	 				   fulstat.setText(statu);
		           }
		           wd=null;
		 	   });
		 		 wd.exec("fetch", inputParam -> {
			           // Thinks to do...
			           // NO ACCESS TO UI ELEMENTS!
		 			
		 		  	try{
		 				
		 				con= database.dbconnect();
		 				   st= con.createStatement();
		 				   String query = "SELECT * FROM user WHERE Id ='"+lg.getUserID()+"'";
		 				   rs=st.executeQuery(query);
		 				  System.out.println("USER:"+lg.getUserID());
		 				   if(rs.next()){
		 					  
		 					  
		 					   fnam = rs.getString("fname");
		 					   lnam = rs.getString("lname");
		 					   usnam = rs.getString("username");
		 					   phon = rs.getString("phone");
		 					   mail = rs.getString("email");
		 					  statu = rs.getString("status");
		 					   
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
			             
			           return new Integer(1);
			           
			           
			        });
	  		
			
		}
	static String getMe(){
		return user;
	}
	private void logOut() throws IOException {
		// TODO Auto-generated method stub
		main.showLogOut();
	}
	private void goProducts() throws IOException {
		// TODO Auto-generated method stub
		main.showProductScene();
	}
	private void goInvents() throws IOException {
		// TODO Auto-generated method stub
		main.showInventScene();
	}
	private void goStore() throws IOException {
		// TODO Auto-generated method stub
		main.showStoreScene();
	}	private void goSupp() throws IOException {
		// TODO Auto-generated method stub
		main.showSuppScene();
	}
	@FXML
	  private void goSale() {
		try {
			main.showSaleScene();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@SuppressWarnings("unchecked")
	private void Tip(){
		 wd = new WorkIndicatorDialog(null, "");
		   wd.addTaskEndNotification(result -> {
	          
	          String outres = result.toString();
	      if(outres.equals("1")){
	        	  // chart
				  summ();
				   double full1 = totsal+totpurch;  double full2 = totsal+totpurch;
				   double   salcent = (totsal/full1)*1;
				   double purcent = (totpurch/full2)*1;
				   double sal =  Math.round(salcent*100.0)/100.0;
				   double pu=  Math.round(purcent*100.0)/100.0;
				 //  System.out.println("sale:"+sal+" purch:"+pu); 
				   saleP.setProgress(salcent);
				   purchP.setProgress(pu);
				   double sl= Math.round((sal*100)*1000.0)/1000.0; double ps=Math.round((pu*100)*1000.0)/1000.0;
				   labS.setText(sl+"%"); labP.setText(ps+"%");
				   if(sl < ps){
					   fntD.setOpacity(1);
					   fntU.setOpacity(0);
					   labSta.setText("NOT GOOD!, purchases rate exceeds sales");
				   }else{
					   fntD.setOpacity(0);
					   fntU.setOpacity(1);
					   labSta.setText("GOOD!, Sales rate exceeds Purchases");
				   }
	      }
	           wd=null; // don't keep the object, cleanup
	        });
		  	 wd.exec("fetch", inputParam -> {
		  	
				  java.util.Date now = new java.util.Date();
				  java.sql.Date sqldate = null,sqldatepast = null;
				  SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
				  Calendar cal = Calendar.getInstance();
				  cal.setTime(now);
				  cal.add(Calendar.DATE, -7);
				  java.util.Date dateBefore7Days = cal.getTime();
				try {
					  sqldate = new java.sql.Date(now.getTime());  
					  sqldatepast = new java.sql.Date(dateBefore7Days.getTime());  
					// System.out.println("TODAY:"+sqldate+"\n PAST:"+sqldatepast);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//*****
					try{
						con= database.dbconnect();
						   st= con.createStatement();
				
						   String sql1="SELECT SUM(amount) AS amount,count(*) AS total FROM sales_order WHERE date BETWEEN '"+sqldatepast+"' AND '"+sqldate+"'";
						   rs=st.executeQuery(sql1);
						  totsal=0;
						   if(rs.next()){
							totsal=rs.getInt("amount");
							 counters = rs.getInt("total");
							 
						   }
						   st2= con.createStatement();
							  
						   String sql2="SELECT SUM(gen_cost) AS amount,count(*) AS total FROM purchased_items WHERE arrival BETWEEN '"+sqldatepast+"' AND '"+sqldate+"'";
						   rs2=st2.executeQuery(sql2);
						  
						
						   if(rs2.next()){
							totpurch = rs2.getInt("amount");
							 counterp = rs2.getInt("total");
							 System.out.println("aaaaa "+totpurch+" bbb "+counterp);
						   }
						   //graph getdata here
						   rs.close();
						st.close();
						
							 rs2.close();
								st2.close();
						con.close();
						
					}catch(Exception ex){
						
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
	private void fillGraph() {
		// barchart.getData().clear();
		  wd = new WorkIndicatorDialog(null, "Refreshing data(S)...");
		   wd.addTaskEndNotification(result -> {
	          
	          String outres = result.toString();
	         // System.out.println("nomaa "+outres);
	          if(outres.equals("1")){
	        	  labToS.setText(mauzo+"/=");
	        	  labToPu.setText(maununu+"/=");
	        	  labMode.setText(ConnectDB.getMode());
	        	  java.util.Date now = new java.util.Date();
	        	  labWatch.setText(now+"");
	        	Tip(); 
	        	// chart
				 /* 
				   int full = totsal+totpurch;
				   int salcent = (totsal/full)*100;
				   int purcent = (totpurch/full)*100;
				   piechart.setTitle("Last 7 Days:\n Sales="+salcent+"%, Purchases="+purcent+"%");
				   pieChartData =  FXCollections.observableArrayList(
					         new PieChart.Data("Sales("+salcent+"%)", totsal), 
					       	 new PieChart.Data("Purchases("+purcent+"%)", totpurch));
					// System.out.println("sale:"+totsal+" purch:"+totpurch);
				   piechart.getData().addAll(pieChartData); */
			  summ();
	          }	
	           wd=null; // don't keep the object, cleanup
	        });
		  	 wd.exec("fetch", inputParam -> {
		  	double SUM=0;
				  java.util.Date now = new java.util.Date();
				  java.sql.Date sqldate = null,sqldatepast = null;
				  SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
				  Calendar cal = Calendar.getInstance();
				  cal.setTime(now);
				  cal.add(Calendar.DATE, -7);
				  java.util.Date dateBefore7Days = cal.getTime();
			
				  try {
					  sqldate = new java.sql.Date(now.getTime());  
					  sqldatepast = new java.sql.Date(dateBefore7Days.getTime());  
					// System.out.println("TODAY:"+sqldate+"\n PAST:"+sqldatepast);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//  Date todate=new java.sql.Date(today);
				//*****
					try{
						con= database.dbconnect();
						   st= con.createStatement();
						  
						   String query = "SELECT SUM( amount) AS 'amount'"
							   		+ " FROM sales_order "
							   		+ " WHERE date= '"+sqldate+"'";
						   rs=st.executeQuery(query);
						  
						
						   if(rs.next()){
							double toto = rs.getDouble("amount");
						toto = Math.round(toto * 100.00) / 100.00;
							mauzo = toto+"";
							
						   }
						   st.close();
						   st= con.createStatement();
							  
						   String queryi = "SELECT SUM(gen_cost) AS 'amount'"
							   		+ " FROM purchased_items"
							   		+ " WHERE arrival= '"+sqldate+"'";
						   rs=st.executeQuery(queryi);
						  
						   if(rs.next()){
							double toto = rs.getDouble("amount");
							toto = Math.round(toto * 100.00) / 100.00;
							SUM = toto;
						//	maununu = toto+"";
							   }
						   st.close();
						   st= con.createStatement();
							  
						   String querye = "SELECT SUM(expense) AS 'amount'"
							   		+ " FROM expenseother"
							   		+ " WHERE date= '"+sqldate+"'";
						   rs=st.executeQuery(querye);
						  
						   if(rs.next()){
							double toto = rs.getDouble("amount");
							toto = Math.round(toto * 100.00) / 100.00;
							SUM+=toto;
							maununu = SUM+"";
							   }
						   	st.close();
						    st1= con.createStatement();
							  
						   String sql1="SELECT SUM(amount) AS amount,count(*) AS total FROM sales_order WHERE date BETWEEN '"+sqldatepast+"' AND '"+sqldate+"'";
						   rs1=st1.executeQuery(sql1);
						  totsal=0;
						   if(rs1.next()){
							totsal=rs1.getInt("amount");
							 counters = rs2.getInt("total");
							 
						   }
						   st2= con.createStatement();
							  
						   String sql2="SELECT SUM(gen_cost) AS amount,count(*) AS total FROM purchased_items WHERE date BETWEEN '"+sqldatepast+"' AND '"+sqldate+"'";
						   rs2=st2.executeQuery(sql2);
						  
						
						   if(rs2.next()){
							totpurch = rs2.getInt("amount");
							 counterp = rs2.getInt("total");
						   }
						   //graph getdata here
						   rs.close();
						st.close();
						 rs1.close();
							st1.close();
							 rs2.close();
								st2.close();
						con.close();
						
					}catch(Exception ex){
						
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
	private void summ(){
		// barchart.getData().clear();
		  wd = new WorkIndicatorDialog(null, "just a moment...");
		   wd.addTaskEndNotification(result -> {
	          
	          String outres = result.toString();
	         // System.out.println("nomaa "+outres);
	          if(outres.equals("1")){
	       
			  labsale.setText(counters+"/-");
			  labpurch.setText(counterp+"/-");
			  labprod.setText(prod+"");
			  reFill();
	          }
	           wd=null; // don't keep the object, cleanup
	        });
		  	 wd.exec("fetch", inputParam -> {
		  		 counters=0;  counterp=0;
				
				  java.util.Date now = new java.util.Date();
				  java.sql.Date sqldate = null,sqldatepast = null;
				  SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
				 // java.util.Date recdate;
				  Calendar cal = Calendar.getInstance();
				  cal.setTime(now);
				  cal.add(Calendar.DATE, -7);
				  java.util.Date dateBefore7Days = cal.getTime();
				try {
				//	recdate = sdf1.parse(today);
					  sqldate = new java.sql.Date(now.getTime());  
					  sqldatepast = new java.sql.Date(dateBefore7Days.getTime());  
					// System.out.println("TODAY:"+sqldate+"\n PAST:"+sqldatepast);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//  Date todate=new java.sql.Date(today);
				//*****
					try{
						con= database.dbconnect();
						   st= con.createStatement();
						  
						   String query="SELECT count(*) AS 'total' FROM purchased_items WHERE arrival='"+sqldate+"'";
						   rs=st.executeQuery(query);
						 
						   if(rs.next()){
							   counterp = rs.getInt("total");
							//   counterp++;
								
					 
						   }
						   
						   st1= con.createStatement();
							  
						   String sql1="SELECT count(*) AS 'amount' FROM sales_order WHERE date='"+sqldate+"'";
						   rs1=st1.executeQuery(sql1);
						  
						   if(rs1.next()){
							counters=rs1.getInt("amount");
							 //  counters++;
							
						   }
							st.close();
						   st= con.createStatement();
						 //  rs.close();
						
						   String query1="SELECT count(*) AS 'total' FROM products WHERE comment='ACTIVE'";
						   rs=st.executeQuery(query1);
						 
						   while(rs.next()){
							   prod = rs.getInt("total");
							  // prod++;
								
					 
						   }
						   //graph getdata here
						   rs.close();
						st.close();
						 rs1.close();
							st1.close();
							 
						con.close();
						
					}catch(Exception ex){
						
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
	    void goChat(ActionEvent event) {
		  try {
				main.showChatScene();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	  @FXML
	    void goInvent(ActionEvent event) {
		  try {
			main.showInventScene();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }

	 @FXML
	    void goProduct(ActionEvent event) {
		 try {
			main.showProductScene();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	 @FXML
	    void goStore(ActionEvent event) {
		 try {
			main.showStoreScene();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	    @FXML
	    void goUser(ActionEvent event) {
	    	 try {
	 			main.showUserScene();
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	    }

	    @FXML
	    void goPurchase(ActionEvent event) {
	   	 try {
	 			main.showPurchScene();
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	    }

	    @FXML
	    void goSupplier(ActionEvent event) {
	    	 try {
	 			main.showSuppScene();
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	    }
	    @FXML
	    void goProfile(ActionEvent event) {
	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("Profile.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text("My profile"));
		    	content.setBody(infopane);
		    	content.setStyle("-fx-background-color:  #2b89e0");
		    	
		    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("Done");
		    	bt.setStyle("-fx-background-color:  #ffffff");
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
			    
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		    
		    	content.setActions(bt);
		    	dialog.show();
		    
			/*
			 * 
			 */
	    	/* 	 try {
	 			main.showProfScene();
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}*/
	    }
	    @FXML
	    void goAbout(ActionEvent event) {
	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("About.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text("About P a m i s"));
		    	content.setBody(infopane);
		    	content.setStyle("-fx-background-color: #2b89e0");
		    	
		    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("Done");
		    	bt.setStyle("-fx-background-color:  #ffffff");
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
			    
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		  
		    	content.setActions(bt);
		    	dialog.show();
		    
			/*
			 * 
			 */
	    	/* 	 try {
	 			main.showProfScene();
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}*/
	    }
	    @FXML
	    void goExpense(ActionEvent event) {

	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("Expenses.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text(""));
		    	content.setBody(infopane);
		    	content.setStyle("-fx-background-color:  #2b89e0");
		    	
		    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("Done");
		    	bt.setStyle("-fx-background-color:  #ffffff");
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
			    
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		  
		    	content.setActions(bt);
		    	dialog.show();
		    
			/*
			 * 
			 */
	    	/* 	 try {
	 			main.showProfScene();
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}*/
	    
	    }
	    @FXML
	    void goManagement(ActionEvent event) {
	    	 try {
		 			main.showManageScene();
		 		} catch (IOException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 		}
	    }
		

		    @FXML
		    void goFaq(ActionEvent event) {

		    	AnchorPane infopane = null;
		    	try{
		    		FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("Faq.fxml"));
					 infopane = loader.load();
		    	}catch(Exception e){ 
		    		e.printStackTrace();
		    	}

				/*
				 * 
				 */	JFXDialogLayout content = new JFXDialogLayout();
			    	content.setHeading(new Text("Frequently Asked Questions -Online"));
			    	content.setBody(infopane);
			    	content.setStyle("-fx-background-color: #2b89e0");
			    	
			    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
			    	JFXButton bt = new JFXButton("Done");
			    	bt.setStyle("-fx-background-color:  #ffffff");
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
				    
				    	dialog.close();
				    	
				    	
						}
			    		
			    	});
			    
			    	content.setActions(bt);
			    	dialog.show();
			    
				/*
				 * 
				 */
			
		    	
		    
		    
		    
		    }
		    @FXML
		    void goProf(ActionEvent event) {
		    	AnchorPane infopane = null;
		    	try{
		    		FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("Profit.fxml"));
					 infopane = loader.load();
		    	}catch(Exception e){ 
		    		e.printStackTrace();
		    	}

				/*
				 * 
				 */	JFXDialogLayout content = new JFXDialogLayout();
			    	content.setHeading(new Text(""));
			    	content.setBody(infopane);
			    	content.setStyle("-fx-background-color: #2b89e0");
			    	
			    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
			    	JFXButton bt = new JFXButton("Done");
			    	JFXButton bt1 = new JFXButton("Cancel");
			    	bt.setOnAction(new EventHandler<ActionEvent>(){
			    		String prodName=null;
						@Override
						public void handle(ActionEvent arg0) {
				
				    	dialog.close();
				    	
				    	
						}
			    		
			    	});
			    	bt1.setOnAction(new EventHandler<ActionEvent>(){

						@Override
						public void handle(ActionEvent arg0) {
							dialog.close();
						}
			    		
			    	});
			    	content.setActions(bt,bt1);
			    	dialog.show();

		    
		    
		    }
		    @FXML
		    void goTrack(ActionEvent event) {
		    	 try {
			 			main.showSaletTrackScene();
			 		} catch (IOException e) {
			 			// TODO Auto-generated catch block
			 			e.printStackTrace();
			 		}
		    }
		    @FXML
		    void goSaleR(ActionEvent event) {
		    	 try {
			 			main.showReportScene();
			 		} catch (IOException e) {
			 			// TODO Auto-generated catch block
			 			e.printStackTrace();
			 		}
		    }
		    @FXML
		    void goHelp(ActionEvent event) {

		    	AnchorPane infopane = null;
		    	try{
		    		FXMLLoader loader = new FXMLLoader();
		    		loader.setLocation(getClass().getResource("Help.fxml"));
		    		 infopane = loader.load();
		    	}catch(Exception e){ 
		    		e.printStackTrace();
		    	}

		    	/*
		    	 * 
		    	 */	JFXDialogLayout content = new JFXDialogLayout();
		        	content.setHeading(new Text("Msaada"));
		        	content.setBody(infopane);
		        	content.setStyle("-fx-background-color: #2b89e0");
		        	
		        	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
		        	JFXButton bt = new JFXButton("Toka");
		        	bt.setStyle("-fx-background-color:  #ffffff");
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
		    	    
		    	    	dialog.close();
		    	    	
		    	    	
		    			}
		        		
		        	});
		      
		        	content.setActions(bt);
		        	dialog.show();

}

	    @FXML
	    void goProfit(ActionEvent event) {
	    	AnchorPane infopane = null;
	    	try{
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("profit.fxml"));
				 infopane = loader.load();
	    	}catch(Exception e){ 
	    		e.printStackTrace();
	    	}

			/*
			 * 
			 */	JFXDialogLayout content = new JFXDialogLayout();
		    	content.setHeading(new Text(""));
		    	content.setBody(infopane);
		    	content.setStyle("-fx-background-color: #19aecc");
		    	
		    	JFXDialog dialog = new JFXDialog(stackbase,content,JFXDialog.DialogTransition.TOP);
		    	JFXButton bt = new JFXButton("Done");
		    	bt.setStyle("-fx-background-color:  #ffffff");
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
			    
			    	dialog.close();
			    	
			    	
					}
		    		
		    	});
		   
		    	content.setActions(bt);
		    	dialog.show();
		    
			/*
			 * 
			 */
	    	/* 	 try {
	 			main.showProfScene();
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}*/
	    }
	    @FXML
	    void goReport(ActionEvent event) {
	    	try {
				main.showReportScene();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    @FXML
	    void logout(ActionEvent event) throws IOException {
	    	main.showLogOut();
	    }
	    @FXML
	    void showPop(MouseEvent event) {
	    	//popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, event.getX(), event.getY());
	    }

	private void initPopup() {
		JFXButton b1 = new JFXButton("task1");
		JFXButton b2 = new JFXButton("task2");
		JFXButton b3 = new JFXButton("task3");
		b1.setPadding(new Insets(10));
		b2.setPadding(new Insets(10));
		b3.setPadding(new Insets(10));
		VBox box = new VBox(b1,b2,b3);
		popup.setContent(box);
		popup.setSource(setting);
	}
	  

}
