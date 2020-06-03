package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private List<Food> foodSelezionati;
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> foodidMap;
	private List<Adiacenza> listaAdiacenze;
	
	public Model() {
		dao= new FoodDao();
	}
	
	public List<Food> getCibiSelezionati(int porzioni){
		//Creo il grafo
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		foodidMap=new HashMap<>();
		
		foodSelezionati=dao.getFoodSelection(porzioni);
	
		
		for(Food f: foodSelezionati) {
			foodidMap.put(f.getFood_code(), f);
		}
		
		//Perchè devo farlo alla pressione del tasto 'ANALISI'
		this.creaGrafo();
		
		return this.foodSelezionati;
	}
	
	public void creaGrafo() {
		Graphs.addAllVertices(grafo, this.foodSelezionati);
		
		listaAdiacenze=new ArrayList<>(dao.getAdiacenze(foodidMap));
		
		for(Adiacenza a: listaAdiacenze) {
			Graphs.addEdge(this.grafo, a.getF1(), a.getF2(), a.getPeso());
		}
		System.out.println(this.grafo.edgeSet().size());
	}
	
	public List<FoodCalories> elencoCibiConnessi(Food f) {
		List<FoodCalories>result=new ArrayList<>();
		
		List<Food>vicini=Graphs.neighborListOf(this.grafo, f);
		
		for(Food v: vicini) {
			Double calorie=this.grafo.getEdgeWeight(this.grafo.getEdge(f, v));
			result.add(new FoodCalories(v, calorie));
		}
		Collections.sort(result);
		
		return result;
	}
	
	public String simula(Food cibo, int k) {
		Simulator simu= new Simulator(this.grafo, this);
		simu.setK(k);
		simu.init(cibo);
		simu.run();
		String messaggio=String.format("Preparati %d cibi in %f minuti", simu.getCibiPreparati(), simu.getTempoPrepazione());
		return messaggio;
	}
}
