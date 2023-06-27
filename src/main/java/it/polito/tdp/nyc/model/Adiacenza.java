package it.polito.tdp.nyc.model;

public class Adiacenza {
	
	String NTA1;
	String NTA2;
	int peso;
	public Adiacenza(String nTA1, String nTA2, int peso) {
		NTA1 = nTA1;
		NTA2 = nTA2;
		this.peso = peso;
	}
	public String getNTA1() {
		return NTA1;
	}
	public void setNTA1(String nTA1) {
		NTA1 = nTA1;
	}
	public String getNTA2() {
		return NTA2;
	}
	public void setNTA2(String nTA2) {
		NTA2 = nTA2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	
	
	

}
