package it.polito.tdp.babs.model;

import it.polito.tdp.babs.db.BabsDAO;

import java.time.LocalDate;
import java.util.List;

public class Model {
	
	private List<Station> stations ;
	private List<Trip> tripsOfTheDay ;

	/**
	 * The list of all loaded {@link Station}s.
	 * @return all the {@link Station}s
	 */
	public List<Station> getStations() {
		return stations;
	}

	public List<Trip> getTripsOfTheDay() {
		return tripsOfTheDay;
	}

	/**
	 * Load the list of all available {@link Station}s from the database. Must be called before the other methods.
	 */
	public void loadStations() {
		BabsDAO dao = new BabsDAO() ;
		
		stations = dao.getAllStations() ; 
	}

	public int loadTripsOfTheDay(LocalDate day) {
		BabsDAO dao = new BabsDAO() ;
		tripsOfTheDay = dao.getTripsOfDay(day) ;
		return tripsOfTheDay.size() ;
	}
	
	public int countDepartingTrips(Station depart) {
		int count=0 ;
		for(Trip t : tripsOfTheDay) {
			if( depart.getStationID() == t.getStartStationID())
				count++ ;
		}
		
		return count ;
	}

	public int countArrivingTrips(Station arrival) {
		int count=0 ;
		for(Trip t : tripsOfTheDay) {
			if( arrival.getStationID() == t.getEndStationID())
				count++ ;
		}
		
		return count ;
	}

}
