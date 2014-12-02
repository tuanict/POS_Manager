/**
 * 
 */
package com.pj3.pos_manager.res_obj;

import java.util.List;

/**
 * @author L�C�ng
 *
 */
public class Order {
	private int orderId;
	private int tableId;
	private List<FoodTemprary> foodTemp;
	private int count;
	
	public Order(){
		
	}
	
	public Order(int tableId, List<FoodTemprary> foodTemp, int count) {
		super();
		this.tableId = tableId;
		this.foodTemp = foodTemp;
		this.count = count;
	}
	public Order(int orderId, int tableId, List<FoodTemprary> foodTemp,
			int count) {
		super();
		this.orderId = orderId;
		this.tableId = tableId;
		this.foodTemp = foodTemp;
		this.count = count;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getTableId() {
		return tableId;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	public List<FoodTemprary> getFoodTemp() {
		return foodTemp;
	}
	public void setFoodTemp(List<FoodTemprary> foodTemp) {
		this.foodTemp = foodTemp;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
