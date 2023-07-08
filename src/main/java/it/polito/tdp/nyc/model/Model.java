package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private List<String> boroughs ;
	private List<NTA> NTAs ;
	private Graph<NTA, DefaultWeightedEdge> grafo ;
	
	public List<String> getBoroughs() {
		if(this.boroughs==null) {
			NYCDao dao = new NYCDao() ;
			this.boroughs = dao.getAllBoroughs() ;
		}
		
		return this.boroughs ;
	}
	
	public void creaGrafo(String borough) {
		NYCDao dao = new NYCDao() ;
		this.NTAs = dao.getNTAbyBorough(borough) ;
//		System.out.println(this.NTAs) ;
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class) ;
		Graphs.addAllVertices(this.grafo, this.NTAs) ;
		
		for(NTA n1: this.NTAs) {
			for(NTA n2: this.NTAs) {
				if(n1.getNTACode().compareTo(n2.getNTACode())<0) { // !n1.equals(n2)
					Set<String> unione = new HashSet<>() ;
					unione.addAll(n1.getSSIDs()) ;
					unione.addAll(n2.getSSIDs()) ;
					Graphs.addEdge(this.grafo, n1, n2, unione.size()) ;
				}
			}
		}
	}
	
	public List<Arco> analisiArchi() {
		double media = 0.0 ;
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			media = media + this.grafo.getEdgeWeight(e) ;
		}
		media = media / this.grafo.edgeSet().size() ;
		
		List<Arco> result = new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>media) {
				result.add(new Arco(
						this.grafo.getEdgeSource(e).getNTACode(),
						this.grafo.getEdgeTarget(e).getNTACode(),
						(int)this.grafo.getEdgeWeight(e)
						)) ;
			}
		}
		
		Collections.sort(result);
		return result ;
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
		}
		 public int numeroArchi() {
		return this.grafo.edgeSet().size();
		}
	
	public Map<NTA, Integer> simula(double probShare, int durationShare) {
		Simulator sim = new Simulator(this.grafo, probShare, durationShare) ;
		sim.init();
		sim.run();
		return sim.getNumTotShare() ;
	}
}