package it.polito.tdp.babs.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulator {

	private Model model;

	private enum BikeEvent {
		PICK, DROP
	};

	private class SimEvent implements Comparable<SimEvent> {
		private BikeEvent type;
		private LocalDateTime time;
		private Station station;
		private Trip trip;

		@Override
		public int compareTo(SimEvent other) {
			return this.time.compareTo(other.time);
		}

		public SimEvent(BikeEvent type, LocalDateTime time, Station station,
				Trip trip) {
			super();
			this.type = type;
			this.time = time;
			this.station = station;
			this.trip = trip;
		}

		public BikeEvent getType() {
			return type;
		}

		public LocalDateTime getTime() {
			return time;
		}

		public Station getStation() {
			return station;
		}

		public Trip getTrip() {
			return this.trip;
		}
	}

	private PriorityQueue<SimEvent> queue = new PriorityQueue<Simulator.SimEvent>();

	private double K; // percentuale di riempimento iniziale

	private Map<Station, Integer> occupancy;

	private int failedPicks;
	private int failedDrops;

	public Simulator(double K, Model model) {
		this.K = K;
		this.model = model ;

		// Calcola l'occupazione iniziale (K% dei posti disponibili)
		occupancy = new HashMap<>();
		for (Station s : model.getStations()) {
			int bikes = (int) (s.getDockCount() * K);
			occupancy.put(s, bikes);
		}

		// Pre-carica tutte le partenze
		for (Trip trip : model.getTripsOfTheDay()) {
			queue.add(new SimEvent(BikeEvent.PICK, trip.getStartDate(), model
					.getStationByID(trip.getStartStationID()), trip));
		}

		failedDrops = 0;
		failedPicks = 0;
	}

	public int run() {
		
		int events = 0 ;

		while (!queue.isEmpty()) {
			
			events++ ;
			
			SimEvent ev = queue.poll();
			Station s = ev.getStation();
			Trip t = ev.getTrip();

			switch (ev.getType()) {
			case PICK:
				if (occupancy.get(s) == 0) {
					// no more bikes
					failedPicks++;
				} else {
					// schedule return
					occupancy.put(s, occupancy.get(s) - 1);
					queue.add(new SimEvent(BikeEvent.DROP, t.getEndDate(),
							model.getStationByID(t.getEndStationID()), t));
				}

				break;
			case DROP:
				if (occupancy.get(s) == s.getDockCount()) {
					// station full
					failedDrops++ ;
				} else {
					occupancy.put(s, occupancy.get(s) + 1);
				}
				break;
			}
		}
		return events ; // number of simulated events
	}

	public int getFailedPicks() {
		return failedPicks;
	}

	public int getFailedDrops() {
		return failedDrops;
	}
}
