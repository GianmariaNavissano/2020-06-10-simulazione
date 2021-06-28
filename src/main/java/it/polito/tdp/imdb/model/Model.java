package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> idMap;
	private Simulator sim;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.sim = new Simulator();
	}
	
	public List<String> getGeneri() {
		List<String> generi = this.dao.getGeneri();
		Collections.sort(generi);
		return generi;
	}
	
	public void creaGrafo(String genre) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		this.idMap = this.dao.getVertex(genre);
		Graphs.addAllVertices(grafo, this.idMap.values());
		
		//archi
		for(Adiacenza a : this.dao.getArchi(genre, idMap)) {
			Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNumEdges() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Actor> getAttori(){
		List<Actor> attori = new LinkedList<Actor>(this.idMap.values());
		Collections.sort(attori);
		return attori;
	}
	
	public String getSimili(Actor a) {
		String result = "";
		List<Actor> simili = this.getRaggiungibili(a);
		Collections.sort(simili);
		if(simili.isEmpty())
			return "Non sono stati trovati attori simili a "+a+"\n";
		for(Actor actor : simili) {
			result += actor+"\n";
		}
		return result;
	}
	
	public List<Actor> getRaggiungibili(Actor a) {
		BreadthFirstIterator<Actor, DefaultWeightedEdge> bfi = new BreadthFirstIterator<>(grafo, a);
		
		bfi.addTraversalListener(new TraversalListener<Actor, DefaultWeightedEdge>() {

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Actor> e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Actor> e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		List<Actor> result = new ArrayList<>();
		while(bfi.hasNext()) {
			Actor actor = bfi.next();
			result.add(actor);
		}
		return result;
	}
	
	public String doSimulazione(int giorni) {
		this.sim.init(giorni, grafo);
		this.sim.run();
		List<Actor> intervistati = this.sim.getIntervistati();
		int nPause = this.sim.getnPause();
		
		String result = "Sono stati intervistati i seguenti giocatori:\n";
		
		for(Actor a : intervistati) {
			result+= a+"\n";
		}
		
		result += "Sono stati fatti "+nPause+" giorni di pausa\n";
		return result;
	}

}
