package com.pj3.pos_manager.router;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.*;
import org.restlet.ext.json.JsonRepresentation;
import android.content.Context;
import java.util.*;
import com.pj3.*;
import com.pj3.pos_manager.res_obj.*;
import com.pj3.pos_manager.database.*;
import com.pj3.pos_manager.Manager;

public class FoodStatusRouter extends ServerResource {
	
	@Get
	public Representation doGet (Representation entity){
		DatabaseSource db = Manager.db;
		String uidString = getQuery().getValues("q");
		JSONObject jo1 = new JSONObject();
		JSONArray ja1 = new JSONArray();
		
/*		
		List<Order> orderList = db.getOrderList();
		try{
	
			for( Order z : orderList){
				List<FoodTemprary> t = z.getFoodTemp();
				for (FoodTemprary q: t){
<<<<<<< Updated upstream
					
					if((q.getStatus() == FoodTemprary.FOOD_STATUS_WAIT && uidString.equalsIgnoreCase("cook")) ||
						(q.getStatus() == FoodTemprary.FOOD_STATUS_WAIT && uidString.equalsIgnoreCase("waiter"))	){
						JSONObject jo2 = new JSONObject();
						jo2.put("f_id", Integer.toString(q.getFoodId()));
						jo2.put("f_count", Integer.toString(q.getCount()));
						jo2.put("f_note", q.getNote());						
						ja1.put(jo2);
=======
					for(int i=0; i<q.getCount(); i ++){
						if((q.getStatus() == FoodTemprary.FOOD_STATUS_WAIT && uidString.equalsIgnoreCase("cook")) ||
							(q.getStatus() == FoodTemprary.FOOD_STATUS_SERVE && uidString.equalsIgnoreCase("waiter"))	){
							JSONObject jo2 = new JSONObject();
							jo2.put("f_id", Integer.toString(q.getFoodId()));
							Food f = db.getFood(q.getFoodId());
							String f_name = f.getM_name();
							jo2.put("f_name", f_name);
							jo2.put("o_id", Integer.toString(z.getOrderId()));
							
							
							jo2.put("f_note", q.getNote());
							ja1.put(jo2);
						}
						
						
>>>>>>> Stashed changes
					}
					
				}
			} 
			jo1.put("f_array", ja1);
		}catch(JSONException e){
				e.printStackTrace();
				return new JsonRepresentation("{\"message\":\"error\"}");
		}*/	
		return new JsonRepresentation(jo1);
			
	
	}
	
	
	
	@Post
	public Representation doPost (Representation entity){
		int orderId=0;
		int fid = 0;
		int status =0;
		DatabaseSource db = Manager.db;
		try{
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			orderId = Integer.parseInt(jsonObj.getString("o_id"));
			fid = Integer.parseInt(jsonObj.getString("f_id"));
			status = Integer.parseInt(jsonObj.getString("status"));
			Order order = db.getBillTemp(orderId);
			List<FoodTemprary> foodList = order.getFoodTemp();
			for( FoodTemprary t : foodList){
				if(t.getFoodId() == fid){
					t.setStatus(status);
				}
			}
			order.setFoodTemp(foodList);
			db.updateBillTemp(order);
			return new JsonRepresentation("{\"message\":\"done\"}");
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		
	}
	
}
//1f62bd5222c034466d58121e6e089e55