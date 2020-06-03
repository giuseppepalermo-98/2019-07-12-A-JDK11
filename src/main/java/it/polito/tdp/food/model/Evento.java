package it.polito.tdp.food.model;

public class Evento implements Comparable <Evento>{

	public enum EventType {
		INIZIO_PREPARAZIONE, //assegno un cibo ad una stazione
		FINE_PREPARAZIONE,  //la stazione ha completato la preparazione
	}
	
	private EventType evento;
	private Double time; // tempo in minuti
	private Stazione stazione;
	private Food food;

	public Evento(EventType evento, double time, Stazione stazione, Food food) {
		super();
		this.evento = evento;
		this.time = time;
		this.stazione = stazione;
		this.food = food;
	}
	
	public EventType getEvento() {
		return evento;
	}
	public Double getTime() {
		return time;
	}
	public Stazione getStazione() {
		return stazione;
	}
	public Food getFood() {
		return food;
	}

	@Override
	public int compareTo(Evento other) {
		return this.time.compareTo(other.time);
	}
	

	
	
}
