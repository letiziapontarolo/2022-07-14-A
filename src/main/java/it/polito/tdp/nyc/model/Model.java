package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private Graph<String, DefaultWeightedEdge> grafo;
	private NYCDao dao;
	private List<String> NTACode;
	private List<Adiacenza> adiacenze;
	
	public Model() {
		dao = new NYCDao();
		
		
		}
	
	public List<String> listaBorghi() {
		List<String> listaBorghi = this.dao.listaBorghi();
		return listaBorghi;
	}
	
	public void creaGrafo(String borgo) {
		
		adiacenze = new ArrayList<>();
		NTACode = new ArrayList<>();
		grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.dao.creaGrafo(this.NTACode, borgo);
		Graphs.addAllVertices(this.grafo, this.NTACode);
		for (Adiacenza a : this.dao.getAdiacenze(this.NTACode, borgo)) {
			adiacenze.add(a);
			Graphs.addEdgeWithVertices(this.grafo, a.getNTA1(),
			a.getNTA2(), a.getPeso());
			}

	}
	
	public String pesoArchi() {
		
		List<Adiacenza> result = new ArrayList<>();
		int contatore = 0;
		double peso = 0;
		for (Adiacenza a : this.adiacenze) {
			peso = peso + a.getPeso();
			contatore = contatore + 1;
		}
		
		double pesoMedioArchi = peso/contatore;
		
		for (Adiacenza a2 : adiacenze) {
			if (a2.getPeso() > pesoMedioArchi) {
				result.add(a2);
			}
		}
		
		int n = 0;
		String str = "";
		for (Adiacenza a3 : result) {
			str = str + a3.getNTA1() + ", " + a3.getNTA2() + ", " + a3.getPeso() + "\n";
			n = n + 1;
		}
		
		
		return "Peso medio: " + Double.toString(pesoMedioArchi) + "\n" 
		+ "Archi con peso maggiore del peso medio: " + Integer.toString(n) + "\n" + str;
		
	}
	
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
		}
	
		 public int numeroArchi() {
		return this.grafo.edgeSet().size();
		}
	
}
