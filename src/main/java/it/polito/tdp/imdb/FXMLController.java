/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private boolean grafoCreato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	this.txtResult.clear();
    	if(!this.grafoCreato) {
    		this.txtResult.appendText("Creare il grafo!\n");
    		return;
    	}
    	Actor a = this.boxAttore.getValue();
    	if(a==null) {
    		this.txtResult.appendText("Selezionare l'attore\n");
    		return;
    	}
    	this.txtResult.appendText(this.model.getSimili(a));

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String genre = this.boxGenere.getValue();
    	if(genre==null) {
    		this.txtResult.appendText("Selezionare un genere\n");
    		return;
    	}
    	this.model.creaGrafo(genre);
    	this.grafoCreato = true;
    	this.txtResult.appendText("Grafo creato!\n# vertici: "+this.model.getNumVertici()+"\n# archi: "+this.model.getNumEdges()+"\n");
    	this.boxAttore.getItems().clear();
    	this.boxAttore.getItems().addAll(this.model.getAttori());

    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	this.txtResult.clear();
    	if(!this.grafoCreato) {
    		this.txtResult.appendText("Creare il grafo!\n");
    		return;
    	}
    	if(this.txtGiorni.getText().equals("")) {
    		this.txtResult.appendText("Selezionare un numero di giorni\n");
    		return;
    	}
    	int giorni = 0;
    	try {
    		giorni = Integer.parseInt(this.txtGiorni.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Il numero di giorni deve essere un intero positivo\n");
    		return;
    	}
    	this.txtResult.appendText(this.model.doSimulazione(giorni));
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxGenere.getItems().addAll(this.model.getGeneri());
    }
}
