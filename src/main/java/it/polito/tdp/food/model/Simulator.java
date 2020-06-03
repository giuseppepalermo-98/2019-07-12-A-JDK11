package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Evento.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;


public class Simulator {

	//Modello del mondo 
	private List<Stazione> stazioni;
	private List<Food> cibi;
	
	private Graph<Food, DefaultWeightedEdge> grafo;
	 private Model model;
	 
	//Parametri di simulazione
	private int K = 5; //Numero di stazioni disponibili
	
	//Risultati calcolati
	private Double tempoPrepazione;
	private int cibiPreparati;
	
	//Coda degli eventi
	private PriorityQueue <Evento> queue;
	
	
	public Simulator (Graph<Food, DefaultWeightedEdge> grafo, Model model) {
		this.grafo= grafo;
		this.model=model; //In questo modo non creo un nuovo modello ma ho un riferimento a quello su cui lavoravo già
	}
	
	public int getCibiPreparati() {
		return cibiPreparati;
	}



	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public Double getTempoPrepazione() {
		return tempoPrepazione;
	}



	public void init(Food partenza) { //All'inizio è quello scelto dall'utente
		 this.cibi = new ArrayList<>(this.grafo.vertexSet());
	
		 for(Food f:cibi) {
			 f.setPreparazione(StatoPreparazione.DA_PREPARARE);
		 }
		 
		 this.stazioni= new ArrayList<>();
		 
		 
		 for(int i=0; i<K; i++) {
			 this.stazioni.add(new Stazione(true, null));
		 }
		 
		 this.tempoPrepazione = 0.0;
		 this.cibiPreparati=0;
		 
		 this.queue= new PriorityQueue<>();
		 List<FoodCalories> vicini= model.elencoCibiConnessi(partenza);
		 
		 for(int i=0; i<K && i<vicini.size(); i++) {
			 this.stazioni.get(i).setLibera(false);
			 this.stazioni.get(i).setFood(vicini.get(i).getFood());
			 
			 Evento e=new Evento(EventType.FINE_PREPARAZIONE,vicini.get(i).getCalorie(),this.stazioni.get(i), vicini.get(i).getFood());
			 this.queue.add(e);
		 }
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Evento e= queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Evento e) {
		switch(e.getEvento()) {
		case INIZIO_PREPARAZIONE:
			List<FoodCalories>vicini=model.elencoCibiConnessi(e.getFood());
			FoodCalories prossimo = null; //Il prossimo da cucinare
			
			for(FoodCalories f: vicini) {
				if(f.getFood().getPreparazione()== StatoPreparazione.DA_PREPARARE) {
					prossimo=f;
					break; //Se è vero l'if fermo la ricerca
				}
			}
			
			if(prossimo != null) {
				prossimo.getFood().setPreparazione(StatoPreparazione.IN_CORSO);
				e.getStazione().setLibera(false);
				e.getStazione().setFood(prossimo.getFood());
				
				
				Evento enew= new Evento(EventType.FINE_PREPARAZIONE, 
									 e.getTime() + prossimo.getCalorie(),
									 e.getStazione(),
									 prossimo.getFood());
				this.queue.add(enew);
			}
			
			break;
		
		
		
		
		case FINE_PREPARAZIONE:
			//AGGIORNO MODELLO DEL MONDO
			this.cibiPreparati++;
			this.tempoPrepazione = e.getTime();
			e.getStazione().setLibera(true);
			e.getFood().setPreparazione(StatoPreparazione.PREPARATO);
			
			Evento enew= new Evento(EventType.INIZIO_PREPARAZIONE, e.getTime(), e.getStazione(), e.getFood());
			this.queue.add(enew);
			break;
		}
	}
}
