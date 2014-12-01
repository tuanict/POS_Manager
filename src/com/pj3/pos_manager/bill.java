package com.pj3.pos_manager;

public class bill {
	private String table;
	private food[] foods;
	
	public bill(String table, food[] foods ){
		this.foods = foods;
		this.table = table;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public food[] getFoods() {
		return foods;
	}

	public void setFoods(food[] foods) {
		this.foods = foods;
	}	
}
