package com.pj3.pos_manager.router;


import java.io.IOException;

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
		
		Food ret  = db.getFood(Integer.parseInt(uidString));
		JSONObject jo = new JSONObject();
		try{	
			jo.put("food_id", ret.getM_food_id());
			jo.put("food_name", ret.getM_name());
			jo.put("food_price", ret.getM_price());
			jo.put("food_image", ret.getM_price());
			jo.put("food_status", ret.getM_status());
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		return new JsonRepresentation(jo);
	}
	
	@Post("json")
	public Representation doPost (Representation entity){
		DatabaseSource db = Manager.db;
		String foodname = "";
		String price  	= "";
		String image	= "";
		String status	= "";
		
		try{
		JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
		JSONObject 			jsonObj  = jsonRep.getJsonObject();
		foodname  	= jsonObj.getString("food_name");
		price 		= jsonObj.getString("food_price");
		//image 		= jsonObj.getString("image");
		status  	= jsonObj.getString("food_status");
		db.createFood(new Food(foodname,Integer.parseInt(price),image,Boolean.parseBoolean(status)));
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		return new JsonRepresentation("{\"message\":\"done\"}");
	}
	
	@Put("json")
	public Representation doPut (Representation entity){
		DatabaseSource db = Manager.db;
		String foodname = "";
		String price  	= "";
		String image	= "";
		String status	= "";
		String id		= "";
		
		String uidString = getQuery().getValues("q");
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		try{
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			foodname  	= jsonObj.getString("food_name");
			price 		= jsonObj.getString("food_price");
			//image 		= jsonObj.getString("image");
			status  	= jsonObj.getString("food_status");
			db.updateMenu(new Food(Integer.parseInt(uidString),foodname,Integer.parseInt(price),image,Boolean.parseBoolean(status)));
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
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