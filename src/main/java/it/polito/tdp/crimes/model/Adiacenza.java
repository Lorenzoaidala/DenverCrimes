package it.polito.tdp.crimes.model;

public class Adiacenza {
	private String type_1;
	private String type_2;
	private double peso;
	public Adiacenza(String type_1, String type_2, double peso) {
		super();
		this.type_1 = type_1;
		this.type_2 = type_2;
		this.peso = peso;
	}
	public String getType_1() {
		return type_1;
	}
	public void setType_1(String type_1) {
		this.type_1 = type_1;
	}
	public String getType_2() {
		return type_2;
	}
	public void setType_2(String type_2) {
		this.type_2 = type_2;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Adiacenza [type_1=" + type_1 + ", type_2=" + type_2 + ", peso=" + peso + "]";
	}

	
}
