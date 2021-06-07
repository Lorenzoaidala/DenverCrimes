package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {

	private Graph<String,DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<String> percorsoMigliore;

	public Model() {
		dao = new EventsDao();
	}

	public void creaGrafo(String categoria, int mese) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		//Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));

		for(Adiacenza a : dao.getArchi(categoria, mese)) {
			grafo.addVertex(a.getType_1());
			grafo.addVertex(a.getType_2());
			if(this.grafo.getEdge(a.getType_1(), a.getType_2())==null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getType_1(), a.getType_2(),a.getPeso());
			}
		}

		System.out.println("Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.");
	}
	
	public List<Adiacenza> getArchiDatoPesoMedio(){
		List<Adiacenza> result = new LinkedList<Adiacenza>();
		double pesoMedio = 0.0;
		for(DefaultWeightedEdge a :this.grafo.edgeSet()) {
			pesoMedio+=this.grafo.getEdgeWeight(a);
		}
		pesoMedio = pesoMedio/this.grafo.edgeSet().size();
		
		for(DefaultWeightedEdge a :this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(a)>pesoMedio) {
				Adiacenza temp = new Adiacenza(this.grafo.getEdgeSource(a),this.grafo.getEdgeTarget(a),this.grafo.getEdgeWeight(a));
				result.add(temp);
			}
		}
		return result;
		
	}
	
	public List<String> trovaPercorso(String partenza, String destinazione){
		this.percorsoMigliore = new ArrayList<String>();
		List<String> parziale = new ArrayList<String>();
		parziale.add(partenza);
		cerca(destinazione,parziale);
		return percorsoMigliore;
	}

	private void cerca(String destinazione, List<String> parziale) {
	
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(parziale.size()>percorsoMigliore.size())
				percorsoMigliore = new ArrayList<String>(parziale);
			return;
		}
		
		for(String s : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				cerca(destinazione,parziale);
				parziale.remove(parziale.size()-1);
			}
				
		}
	}
	public List<String> getCategorie(){
		return dao.getAllCategorie();
	}
	
	
	
}
