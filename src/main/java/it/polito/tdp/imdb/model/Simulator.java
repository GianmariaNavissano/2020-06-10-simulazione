package it.polito.tdp.imdb.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {
	
	//modello
	private List<Actor> daIntervistare;
	private boolean faiCasuale;
	
	//input
	private int giorni;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	//output
	private List<Actor> intervistati;
	private int nPause;
	
	
	public void init(int giorni, Graph<Actor, DefaultWeightedEdge> grafo) {
		
		this.giorni = giorni;
		this.grafo = grafo;
		this.faiCasuale = true;
		this.daIntervistare = new LinkedList<>(grafo.vertexSet());
		
		this.intervistati = new LinkedList<>();
		this.nPause = 0;
		
	}
	
	
	public void run() {
		
		Actor a;
		
		for(int i = 0; i<giorni; i++) {
			
			int p60e40 = (int)Math.random()*100;
			int pCasuale = (int)Math.random()*daIntervistare.size();
			int pPausa = (int)Math.random()*100;
			
			if(daIntervistare.isEmpty()) {
				//non ci sono più attori da intervistare
				return;
			}
			if(faiCasuale) {
				
				this.faiCasuale = false;
				
				//l'ultimo elemento era una pausa oppure siamo nel caso iniziale.
				//devo scegliere un elemento a caso tra quelli di daIntervistare
				
				a = this.daIntervistare.get(pCasuale);
				this.intervistati.add(a);
				this.daIntervistare.remove(pCasuale);
				
			}else {
				
				
				//controllo che gli ultimi 2 non siano dello stesso sesso
				if(this.intervistati.size()>1) {
					
					if(this.intervistati.get(intervistati.size()-1).getGender().equals(this.intervistati.get(intervistati.size()-2).getGender())) {
						
						if(pPausa<90) {
							this.nPause++;
							this.faiCasuale = true;
							
						}
						
					}
				}
				
				
				if(!faiCasuale) {
					
					if(p60e40<60) {
						
						
						//in questo caso faccio di nuovo il casuale
						a = this.daIntervistare.get(pCasuale);
						this.intervistati.add(a);
						this.daIntervistare.remove(pCasuale);
						
						
					}else {
						
						//in questo caso devo prendere il vicino di grado massimo
						//dell'ultimo attore intervistato
						a = this.getNext(this.intervistati.get(intervistati.size()-1));
						
						if(a==null) {
							
							//se non vi erano vicini faccio casuale
							a = this.daIntervistare.get(pCasuale);
							
						}
						this.intervistati.add(a);
						this.daIntervistare.remove(a);
						
					}
				}
					
			}
			
			
		}
	}
	
	public Actor getNext(Actor a) {
		
		List<Actor> vicini = Graphs.neighborListOf(grafo, a);
		int pesoMax = 0;
		Actor result = null;
		
		if(vicini.isEmpty()) {
			return null;
		}
		int peso;
		
		for(Actor actor : vicini) {
			
			//CASO ARCO A, ACTOR
			if(grafo.getEdge(a, actor)!=null) {
				
				peso = (int)grafo.getEdgeWeight(grafo.getEdge(a, actor));
				
				if(peso>=pesoMax) {
					pesoMax = peso;
					result = actor;
				}
				
				
			}else {
				
				
				//CASO ARCO ACTOR, A
				if(grafo.getEdge(actor, a)!=null) {
					
					peso = (int)grafo.getEdgeWeight(grafo.getEdge(actor, a));
					
					//caso peso>pesomax
					if(peso>=pesoMax) {
						pesoMax = peso;
						result = actor;
					}
				}
			}
		}
		
		return result;
	}


	public List<Actor> getIntervistati() {
		return intervistati;
	}


	public int getnPause() {
		return nPause;
	}
	
	
	

}
