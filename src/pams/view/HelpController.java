package pams.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXProgressBar;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class HelpController implements Initializable {
	  @FXML
	    private WebView webview;
	  @FXML
	    private JFXProgressBar prog;

	    @FXML
	    private Label labcont;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		 WebEngine webEngine = webview.getEngine();
		 ScrollPane scrollPane = new ScrollPane();
		 scrollPane.setContent(webview);
		 webEngine.load("https://docs.google.com/forms/u/0/d/e/1FAIpQLSe4xBc3JoN6FP9gmFED2X5TSNmFFwBXa8nhHS0J4o2BfohyNg/viewform?usp=sf_link");
		 webEngine.setJavaScriptEnabled( true );
		 prog.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
		 webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>(){
			 public void changed(ObservableValue<? extends State> ov, State oldState, State newState){
				 if (newState == State.SUCCEEDED) {
					 labcont.setText("connected to the internet");
				 }else{
					 labcont.setText("searching...");
				 }
			 }
		 });
	}
}
