package com.pj3.pos_manager;

public class food {
	private String name;
	private int numberOf;
	private int price;
	
	public food(String name, int numberof, int price){
		this.name = name;
		this.numberOf = numberof;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOf() {
		return numberOf;
	}

	public void setNumberOf(int numberOf) {
		this.numberOf = numberOf;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	
	
}
