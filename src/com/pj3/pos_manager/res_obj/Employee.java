/**
 * 
 */
package com.pj3.pos_manager.res_obj;

/**
 * @author LÍCÙng
 *
 */
public class Employee {
	private int e_id;
	private String e_name;
	private String e_email;
	private String e_pass;
	private String e_image;
	private int e_phone_number;
	private int POSITION_p_id;
	
	public Employee(){
		
	}
	
	public Employee(int e_id, String e_name, String e_email, String e_pass,
			String e_image, int e_phone_number, int pOSITION_p_id) {
		super();
		this.e_id = e_id;
		this.e_name = e_name;
		this.e_email = e_email;
		this.e_pass = e_pass;
		this.e_image = e_image;
		this.e_phone_number = e_phone_number;
		POSITION_p_id = pOSITION_p_id;
	}
	
	
	public Employee(String e_name, String e_email, String e_pass,
			String e_image, int e_phone_number, int pOSITION_p_id) {
		super();
		this.e_name = e_name;
		this.e_email = e_email;
		this.e_pass = e_pass;
		this.e_image = e_image;
		this.e_phone_number = e_phone_number;
		POSITION_p_id = pOSITION_p_id;
	}


	public int getE_id() {
		return e_id;
	}
	public void setE_id(int e_id) {
		this.e_id = e_id;
	}
	public String getE_name() {
		return e_name;
	}
	public void setE_name(String e_name) {
		this.e_name = e_name;
	}
	public String getE_email() {
		return e_email;
	}
	public void setE_email(String e_email) {
		this.e_email = e_email;
	}
	public String getE_pass() {
		return e_pass;
	}
	public void setE_pass(String e_pass) {
		this.e_pass = e_pass;
	}
	public String getE_image() {
		return e_image;
	}
	public void setE_image(String e_image) {
		this.e_image = e_image;
	}
	public int getE_phone_number() {
		return e_phone_number;
	}
	public void setE_phone_number(int e_phone_number) {
		this.e_phone_number = e_phone_number;
	}
	public int getPOSITION_p_id() {
		return POSITION_p_id;
	}
	public void setPOSITION_p_id(int pOSITION_p_id) {
		POSITION_p_id = pOSITION_p_id;
	}
	
	
}
