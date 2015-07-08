package br.ufu.weightThrowing;

public class node {

	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public char getId() {
		return id;
	}
	public void setId(char id) {
		this.id = id;
	}
	double weight;
	char id;
	boolean initiator;
	public boolean isInitiator() {
		return initiator;
	}
	public void setInitiator(boolean initiator) {
		this.initiator = initiator;
	}
	
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	boolean active;
	
}
