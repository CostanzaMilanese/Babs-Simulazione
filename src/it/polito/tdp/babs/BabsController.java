package it.polito.tdp.babs;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.babs.model.Model;
import it.polito.tdp.babs.model.Station;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class BabsController {
	
	private Model model ;

    @FXML
    private Slider sliderK;

    @FXML
    private TextArea txtResult;

    @FXML
    private DatePicker pickData;

    @FXML
    void doContaTrip(ActionEvent event) {
    	txtResult.clear();
    	
    	LocalDate day = pickData.getValue() ;
    	if(day==null) {
    		txtResult.appendText("Inserire una data\n");
    		return ;
    	}
    	
    	int totTrips = model.loadTripsOfTheDay(day);

    	if(totTrips==0) {
    		txtResult.appendText("Non ci sono viaggi nella data specificata\n");
    		return ;
    	}
    	
    	List<Station> stationsSorted = new LinkedList<>(model.getStations()) ;
    	Collections.sort(stationsSorted, new Comparator<Station>(){
			@Override
			public int compare(Station arg0, Station arg1) {
				return -((Double)arg0.getLat()).compareTo(arg1.getLat()) ;
			}});
    	
    	txtResult.appendText(String.format("Percorsi del %s:\n", day.toString()));
    	for(Station s : stationsSorted ) {
    		int dep = model.countDepartingTrips(s) ;
    		int arr = model.countArrivingTrips(s) ;
    		txtResult.appendText(String.format("%s: %d %d\n", s.getName(), dep, arr)) ;
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {

    }
    
    public void setModel(Model m) {
    	this.model = m ;
    }

}
