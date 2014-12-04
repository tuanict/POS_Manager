package com.pj3.pos_manager;

import java.util.List;

public class bill {
	private String table;
	private List<food> foods;

	public bill(String table, List<food> foods) {
		this.foods = foods;
		this.table = table;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public List<food> getFoods() {
		return foods;
	}

	public void setFoods(List<food> foods) {
		this.foods = foods;
	}
}
