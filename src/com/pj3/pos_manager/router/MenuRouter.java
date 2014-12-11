package com.pj3.pos_manager.router;


import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.*;
import org.restlet.ext.json.JsonRepresentation;
import android.content.Context;

import com.pj3.*;
import com.pj3.pos_manager.res_obj.*;
import com.pj3.pos_manager.database.*;
import com.pj3.pos_manager.Manager;
public class MenuRouter extends ServerResource {
	
	@Get
	public Representation doGet (Representation entity){
		DatabaseSource db = Manager.db;
		String uidString = getQuery().getValues("q");
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		if(uidString.equals("all")){
			List<Food> listOfFood = db.getAllFood();
			JSONObject ret = new JSONObject();
			JSONArray ja = new JSONArray();
			JSONObject element = new JSONObject();
			if (listOfFood == null){
				return new JsonRepresentation("{\"message\":\"error2\"}");
			}
			try{
				for(Food f: listOfFood){
					element = new JSONObject();
					element.put("f_id", f.getM_food_id());
					element.put("f_name", f.getM_name());
					element.put("f_price", f.getM_price());
					element.put("f_image", f.getM_image());
					element.put("f_status", f.getM_status());
					element.put("f_options", f.getM_option());
					ja.put(element);
					
					
				}
				ret.put("m_array", ja);
				return new JsonRepresentation(ret);
				
			} catch(Exception e){
				e.printStackTrace();
				return new JsonRepresentation("{\"message\":\"error2\"}");
			}
			
		}
		else{
			Food ret  = db.getFood(Integer.parseInt(uidString));
			JSONObject jo = new JSONObject();
			try{	
				jo.put("f_id", ret.getM_food_id());
				jo.put("f_name", ret.getM_name());
				jo.put("f_price", ret.getM_price());
				jo.put("f_image", ret.getM_image());
				jo.put("f_status", ret.getM_status());
				jo.put("f_options", ret.getM_option());
			} catch(Exception e){
				e.printStackTrace();
				return new JsonRepresentation("{\"message\":\"error\"}");
			}
			return new JsonRepresentation(jo);
		}
	}
	
	@Post("json")
	public Representation doPost (Representation entity){
		DatabaseSource db = Manager.db;
		String foodname = "";
		String price  	= "";
		String image	= "";
		String status	= "";
		String options = "";
		String fid = "";
		
		try{
		JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
		JSONObject 			jsonObj  = jsonRep.getJsonObject();
		foodname  	= jsonObj.getString("f_name");
		price 		= jsonObj.getString("f_price");
		//image 		= jsonObj.getString("image");
		status  	= jsonObj.getString("f_status");
		options  =	jsonObj.getString("f_options");
		Food tf = new Food(foodname,Integer.parseInt(price),image,Boolean.parseBoolean(status));
		tf.setM_option(options);
		int menuid = db.createFood(tf);
		fid = Integer.toString(menuid);
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"internal error\"}");
		}
		return new JsonRepresentation("{\"message\":\"done\","+"\"f_id\":\""+fid+"\"}");
	}
	
	@Put("json")
	public Representation doPut (Representation entity){
		DatabaseSource db = Manager.db;
		String foodname = "";
		String price  	= "";
		String image	= "";
		String status	= "";
		String id		= "";
		String options = "";
		String fid = "";
		String uidString = getQuery().getValues("q");
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		try{
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			foodname  	= jsonObj.getString("f_name");
			price 		= jsonObj.getString("f_price");
			//image 		= jsonObj.getString("image");
			status  	= jsonObj.getString("f_status");
			
			options  =	jsonObj.getString("f_options");
			Food tf = new Food(foodname,Integer.parseInt(price),image,Boolean.parseBoolean(status));
			tf.setM_option(options);
			db.updateMenu(tf);
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"internal erorr\"}");
		}
		return new JsonRepresentation("{\"message\":\"done\"}");
	}
	
	@Delete
	public Representation doDelete(Representation entity){
		String uidString = getQuery().getValues("q");
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		DatabaseSource db = Manager.db;
		db.changeStatusFood(Integer.parseInt(uidString), false);
		return new JsonRepresentation("{\"message\":\"done\"}");
	}
}
//1f62bd5222c034466d58121e6e089e55
//ZG9pbmcgc2hpdG91dA==