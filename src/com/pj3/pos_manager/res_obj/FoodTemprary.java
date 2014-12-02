/**
 * 
 */
package com.pj3.pos_manager.res_obj;

/**
 * @author L�C�ng
 *
 */
public class FoodTemprary {
	private int foodId;
	private int count;
	private String note;
	private int status; 
	
	public static int FOOD_STATUS_WAIT	= 1;
	public static int FOOD_STATUS_DONE	= 2;
	public static int FOOD_STATUS_SERVE	= 3;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getFoodId() {
		return foodId;
	}
	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
