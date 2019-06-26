package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PowerOutagesController {
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private ComboBox<Nerc> cmbNerc;

    @FXML
    private TextField txtYears;

    @FXML
    private TextField txtHours;

    @FXML
    void doRun(ActionEvent event) {
    	this.txtResult.clear();
    	Nerc nerc = this.cmbNerc.getValue();
    	if(nerc == null) {
    		this.txtResult.setText("Selezionare un NERC dal menu'!");
    		return;
    	}
    	Integer anni, ore;
    	try {
			anni = Integer.parseInt(this.txtYears.getText().trim());
			ore = Integer.parseInt(this.txtHours.getText().trim());
		} catch (NumberFormatException e) {
			this.txtResult.setText("Inserire correttamente i dati numerici!");
    		return;
		}
    	List<PowerOutage> worstCase = this.model.computeWorstCase(nerc, anni, ore);
    	this.txtResult.appendText("Tot people affected: " +model.sumAffectedPeople(worstCase)+ "\n");
    	this.txtResult.appendText("Tot hours of outage: " +model.sumOutageHours(worstCase)+ "\n");
    	for(PowerOutage po : worstCase)
    		this.txtResult.appendText(po.toString()+ "\n");
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        txtResult.setStyle("-fx-font-family: monospace");
    }
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbNerc.getItems().addAll(this.model.getNercList());
    }
}