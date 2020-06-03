/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodCalories;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	int porzioni;
    	try {
    		porzioni=Integer.parseInt(this.txtPorzioni.getText());
    		this.boxFood.getItems().clear();
    		this.boxFood.getItems().addAll(model.getCibiSelezionati(porzioni));
    		
    	} catch(NumberFormatException e) {
    		this.txtResult.appendText("Inserire una porzione!\n");
    		return;
    	}
    	
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	
    	Food f=this.boxFood.getValue();
    	if(f == null) {
    		this.txtResult.appendText("ERRORE non hai selezionato un cibo!");
    		return;
    	}
    	
    	List<FoodCalories> lista =model.elencoCibiConnessi(f);
    	
    	for(int i=0; i<5 && i<lista.size(); i++)
    		this.txtResult.appendText(String.format("%s %f\n", lista.get(i).getFood().getDisplay_name(), lista.get(i).getCalorie()));
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	Food cibo=this.boxFood.getValue();
    	if(cibo == null) {
    		this.txtResult.appendText("ERRORE non hai selezionato un cibo!");
    		return;
    	}
    	try {
    	int k=Integer.parseInt(this.txtK.getText());
    	String messaggio=model.simula(cibo, k);
    	this.txtResult.appendText(messaggio);
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("ERRORE il k deve essere un numero intero!");
    		return;
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
