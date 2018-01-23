package pams;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import pams.view.ConnectDB;
import pams.view.WorkIndicatorDialog;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


public class Main extends Application {

	
	    private PreparedStatement prep;
	private static Stage primaryStage;
	private static BorderPane mainLayout;
	public static BorderPane mainItems;
	int s=0,f=0,e=0;
	boolean su,al=false;
	private WorkIndicatorDialog wd=null;
	 String head,content;
	 ConnectDB database = new ConnectDB();
	 private Connection con;
	    private ResultSet rs;
	    private Statement st;
	  
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();

		primaryStage.setX(bounds.getMinX());
		primaryStage.setY(bounds.getMinY());
		primaryStage.setWidth(bounds.getWidth());
		primaryStage.setHeight(bounds.getHeight());
		Image icon = new Image(getClass().getResourceAsStream("view/images/pamis0.png"));
		primaryStage.getIcons().add(icon);
		Main.primaryStage=primaryStage;
		Main.primaryStage.setTitle("P a m i s");
		showMainView();
		checkAlert();
		//showMainItems();
	}
	 public static void setNode(Node node) {
	        /* AudioClip notifyMe = new AudioClip((getClass().getResource("Notify.wav")).toString());
	        notifyMe.play();
	         */

		// mainItems.getChildren().clear();
		 
		 // anchorPane.getChildren().add((Node) node);
		 mainItems.setCenter((Node) node);;

	        FadeTransition ft = new FadeTransition(Duration.millis(2000));
	        ft.setNode(node);
	        ft.setFromValue(0.1);
	        ft.setToValue(1);
	        ft.setCycleCount(1);
	        ft.setAutoReverse(false);
	        ft.play();

	    }
	private void showMainView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Mainview.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void showMainDash() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/MainDash.fxml"));
		 mainItems = loader.load();
		//mainLayout.setCenter(mainItems);
		 
		Scene scene = new Scene(mainItems,primaryStage.getWidth(),primaryStage.getHeight());
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	public static void showCashierDash() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/DashCashier.fxml"));
		 mainItems = loader.load();
		//mainLayout.setCenter(mainItems);
		Scene scene = new Scene(mainItems,primaryStage.getWidth(),primaryStage.getHeight());
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	public static void showStoreDash() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/DashStore.fxml"));
		 mainItems = loader.load();
		//mainLayout.setCenter(mainItems);
		Scene scene = new Scene(mainItems,primaryStage.getWidth(),primaryStage.getHeight());
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	public static void showChatScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/ChatStage.fxml"));
		BorderPane chatscene = loader.load();
	//	mainItems.setCenter(chatscene);
		setNode(chatscene);
	}
	public static void showLogOut() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Mainview.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout,primaryStage.getWidth(),primaryStage.getHeight());
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public static void showReceipt() throws IOException{ 
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Receipt.fxml"));
		BorderPane receiptpane = loader.load();
		//mainItems.setCenter(receiptpane);
		 setNode(receiptpane);
	}
	public static void showSaleScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Sale.fxml"));
		BorderPane salepane = loader.load();
		//mainItems.setCenter(salepane);
		 setNode(salepane);
	}
	public static void showInventScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/MyInventory.fxml"));
		BorderPane inventorypane = loader.load();
		mainItems.setCenter(inventorypane);
		 setNode(inventorypane);
	}
	public static void showReportScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Report.fxml"));
		BorderPane reportpane = loader.load();
		mainItems.setCenter(reportpane);
	}
	public static void showSaletTrackScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/SaleTrack.fxml"));
		BorderPane reportpane = loader.load();
		mainItems.setCenter(reportpane);
	}

	public static void showProductScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/ProdAdmin.fxml"));
		BorderPane prodpane = loader.load();
		 setNode(prodpane);
	//	mainItems.setCenter(prodpane);
	}
	public static void showStoreScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Store.fxml"));
		BorderPane prodpane = loader.load();
		 setNode(prodpane);
	//	mainItems.setCenter(prodpane);
	}
	public static void showUserScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/PharmacistAdm.fxml"));
		BorderPane userpane = loader.load();
		//mainItems.setCenter(userpane);
		 setNode(userpane);
	}
	public static void showPurchScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Purchase.fxml"));
		BorderPane userpane = loader.load();
	//	mainItems.setCenter(userpane);
		setNode(userpane);
	}
	public static void showSuppScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/SupplierAdmin.fxml"));
		BorderPane suppl = loader.load();
	//	mainItems.setCenter(userpane);
		setNode(suppl);
	}
	public static void showManageScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/ManageScene.fxml"));
		BorderPane suppl = loader.load();
	//	mainItems.setCenter(userpane);
		setNode(suppl);
	}
	
	public static void showProfScene() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Profile.fxml"));
		BorderPane userpane = loader.load();
		mainItems.setCenter(userpane);
		setNode(userpane);
	}
	public static void showAddProduct() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/AddProduct.fxml"));
		BorderPane addnew = loader.load();
		Stage dialogstage = new Stage();
		dialogstage.setTitle("ADD PRODUCT");
		dialogstage.initModality(Modality.WINDOW_MODAL);
		dialogstage.initOwner(primaryStage);
		Scene scene = new Scene(addnew);
		dialogstage.setScene(scene);
		dialogstage.showAndWait();
	}
	public static void showAddUser() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/AddUser.fxml"));
		BorderPane addnew = loader.load();
		Stage dialogstage = new Stage();
		dialogstage.setTitle("ADD USER");
		dialogstage.initModality(Modality.WINDOW_MODAL);
		dialogstage.initOwner(primaryStage);
		Scene scene = new Scene(addnew);
		dialogstage.setScene(scene);
		dialogstage.showAndWait();
	}
	public static void showAddSupplier() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/AddSupplier.fxml"));
		BorderPane addnew = loader.load();
		Stage dialogstage = new Stage();
		dialogstage.setTitle("ADD SUPPLIER");
		dialogstage.initModality(Modality.WINDOW_MODAL);
		dialogstage.initOwner(primaryStage);
		Scene scene = new Scene(addnew);
		dialogstage.setScene(scene);
		dialogstage.showAndWait();
	}
	public static void showAUpdSupplier() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/UpdateSupp.fxml"));
		BorderPane addnew = loader.load();
		Stage dialogstage = new Stage();
		dialogstage.setTitle("UPDATE SUPPLIER");
		dialogstage.initModality(Modality.WINDOW_MODAL);
		dialogstage.initOwner(primaryStage);
		Scene scene = new Scene(addnew);
		dialogstage.setScene(scene);
		dialogstage.showAndWait();
	}
	public static void showAUpdProduct() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/UpdateProd.fxml"));
		BorderPane addnew = loader.load();
		Stage dialogstage = new Stage();
		dialogstage.setTitle("EDIT INFORMATION");
		dialogstage.initModality(Modality.WINDOW_MODAL);
		dialogstage.initOwner(primaryStage);
		Scene scene = new Scene(addnew);
		dialogstage.setScene(scene);
		dialogstage.showAndWait();
	}
	public static void showModProduct() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/ModProd.fxml"));
		BorderPane addnew = loader.load();
		Stage dialogstage = new Stage();
		dialogstage.setTitle("UPDATE PRODUCT");
		dialogstage.initModality(Modality.WINDOW_MODAL);
		dialogstage.initOwner(primaryStage);
		Scene scene = new Scene(addnew);
		dialogstage.setScene(scene);
		dialogstage.showAndWait();
	}
	@SuppressWarnings("unchecked")
	private  void checkAlert() {
		   wd = new WorkIndicatorDialog(null, "Contacting Server...");
		   wd.addTaskEndNotification(result -> {
	          
	          String outres = result.toString();
	         // System.out.println("nomaa "+outres);
	          if(outres.equals("1")){
	        	  TrayNotification tray = new TrayNotification();
	      	       tray.setNotificationType(NotificationType.NOTICE);
	      	       tray.setTitle(head);
	      	       tray.setMessage(content);
	      	       tray.setAnimationType(AnimationType.POPUP);
	      	       tray.showAndDismiss(Duration.millis(15000));
	          }
	           wd=null; // don't keep the object, cleanup
	        });
	  	 wd.exec("alert", inputParam -> {
	           // Thinks to do...
	           // NO ACCESS TO UI ELEMENTS!
	  		 try{
	    			con= database.dbconnect();
	    		   st= con.createStatement();
	    		   String query = "SELECT * FROM alert ";
	    		   rs=st.executeQuery(query);
	    		   	if(rs.next()){
	    		   		head=  rs.getString("head");
	    		   		content = rs.getString("content");
	    		   		
	    		   		
	    		   		al=true;
	    		  
	    		   	}else{
	    		   		al=false;
	    	
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
	              if(al){
	            	  e = 1;
	              }else{
	            	  e = 0;
	              }
	              
	           return new Integer(e);
	           
	           
	        });
	}
	public static void main(String[] args) {
	
		launch(args);
	}
}
