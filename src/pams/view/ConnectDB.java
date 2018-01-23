package pams.view;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ConnectDB {
	static String mode;
	File file = new File("me.txt");
	String sCurrentLine;
	Connection con=null;
	String []conf;
	public static String getMode(){
		return mode;
	}
	public Connection dbconnect(){
		if(!file.exists()){
			TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.WARNING);
		       tray.setTitle("Your App does not point to your server");
		       tray.setMessage("please insert server details.");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
		}else{
			try{
			BufferedReader		br = new BufferedReader(new FileReader("me.txt"));

			if ((sCurrentLine = br.readLine()) != null) {
					conf = sCurrentLine.split("-");
					if(conf[0].equals("offline")){
						String ip = conf[1];
						mode = "offline";
						String url="jdbc:mysql://"+ip+":3306/";
						String db="pams";
						String username="root";
						String password="mkkilnya";
						Class.forName("com.mysql.jdbc.Driver");
						
						//showIp = sCurrentLine;
						 con=DriverManager.getConnection(url+db,username,password);
						 
					//	 JOptionPane.showMessageDialog(null, ip);
					
						 return con;
					}else if(conf[0].equals("online")){
						String mydb = conf[1];
						String mypas = conf[2];
						mode = "online";
						//String uname = conf[1];
						String url="jdbc:mysql://sql33.main-hosting.eu:3306/";// 31.220.20.207
						String db="u717724482_"+mydb;
						String username="u717724482_"+mydb;
						String password=mypas;
						Class.forName("com.mysql.jdbc.Driver");
						
						//showIp = sCurrentLine;
						 con=DriverManager.getConnection(url+db,username,password);
						 
					//	 JOptionPane.showMessageDialog(null, ip);
					
						 return con;
					}
			
			
			}else{
				
				
			}
			}catch(Exception e){
				e.printStackTrace();
				
				JOptionPane.showMessageDialog(null,"NETWORK ERROR \n Make sure you have working connection \n"
						+ ", try to configure server, your network/internet. \n"
						+ "IF THE PROBLEM PERSIST, \n"
						+ "please seek help from SERVER/DATABASE administrator");
			}
/*		String url="jdbc:mysql://localhost:3306/";	// sql6.freemysqlhosting.net // sql11.freesqldatabase.com
		String db="apsDB";		// sql6162550	// sql11163756
		String username="root";	// sql6162550	// sql11163756
		String password="mkkilnya";	// ewRqIqpkst		//  HJSRvXJbGt
		try{
		Class.forName("com.mysql.jdbc.Driver");
		
		 * Host: sql11.freesqldatabase.com
	Database name: sql11163756
	Database user: sql11163756
	Database password: HJSRvXJbGt
	Port number: 3306	
		 
		
		
		 con=DriverManager.getConnection(url+db,username,password);
		
		return con;
			
		
			
			
		}catch(Exception e){
			System.out.println("ERROR"+e);
			JOptionPane.showMessageDialog(null,"NETWORK ERROR \n Make sure you have internet connection \n"
					+ "IF THE PROBLEM PERSIST \n"
					+ "please seek help from SERVER/DATABASE administrator");
			TrayNotification tray = new TrayNotification();
		       tray.setNotificationType(NotificationType.ERROR);
		       tray.setTitle("NETWORK ERROR");
		       tray.setMessage("Make  sure you are connected to the internet");
		       tray.setAnimationType(AnimationType.SLIDE);
		       tray.showAndDismiss(Duration.millis(4000));
		}*/
		}
		
		return null;
		
	}
	
}
