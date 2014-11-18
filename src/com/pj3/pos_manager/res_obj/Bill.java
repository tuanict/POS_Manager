/**
 * 
 */
package com.pj3.pos_manager.res_obj;

import java.util.Date;

/**
 * @author LÍCÙng
 *
 */
public class Bill {
	private int b_id;
	private int b_count;
	private Date b_time_stamp;
	
	public Bill(){
		
	}
	
	public Bill(int b_id, int b_count, Date b_time_stamp) {
		super();
		this.b_id = b_id;
		this.b_count = b_count;
		this.b_time_stamp = b_time_stamp;
	}
	
	public Bill(int b_count, Date b_time_stamp) {
		super();
		this.b_count = b_count;
		this.b_time_stamp = b_time_stamp;
	}

	public int getB_id() {
		return b_id;
	}
	public void setB_id(int b_id) {
		this.b_id = b_id;
	}
	public int getB_count() {
		return b_count;
	}
	public void setB_count(int b_count) {
		this.b_count = b_count;
	}
	public Date getB_time_stamp() {
		return b_time_stamp;
	}
	public void setB_time_stamp(Date b_time_stamp) {
		this.b_time_stamp = b_time_stamp;
	}
	
}
