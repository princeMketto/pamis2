package pams.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import pams.Main;

public class DrawerController implements Initializable {
	private Main main;
	
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}



}
