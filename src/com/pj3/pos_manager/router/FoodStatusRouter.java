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
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		JSONObject jo1 = new JSONObject();
		JSONArray ja1 = new JSONArray();
		int stt =0;
		
		List<Order> orderList = db.getOrderList();
		System.out.println("tadaa"+ orderList.size());
		if (orderList.size() == 0) return new JsonRepresentation("{\"message\":\"nothing to cook\"}");
		try{
	
			for( Order z : orderList){
				List<FoodTemprary> t = z.getFoodTemp();
				for (FoodTemprary q: t){
					for(int i=0; i<q.getCount(); i ++){
						if((q.getStatus() == FoodTemprary.FOOD_STATUS_WAIT && uidString.equalsIgnoreCase("cook")) ||
							(q.getStatus() == FoodTemprary.FOOD_STATUS_DONE && uidString.equalsIgnoreCase("waiter"))	){
							JSONObject jo2 = new JSONObject();
							jo2.put("f_id", Integer.toString(q.getFoodId()));
							Food f = db.getFood(q.getFoodId());
							String f_name = f.getM_name();
							jo2.put("f_name", f_name);
							jo2.put("o_id", Integer.toString(z.getOrderId()));
							jo2.put("status", q.getStatus());
							jo2.put("stt", stt ++);
							jo2.put("f_image", f.getM_image());
							jo2.put("f_note", q.getNote());
							ja1.put(jo2);
						}
						
						
					}
					
				}
			} 
			jo1.put("f_array", ja1);
		}catch(JSONException e){
				e.printStackTrace();
				return new JsonRepresentation("{\"message\":\"error\"}");
		}
		return new JsonRepresentation(jo1);
			
		
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Post("json")
	public Representation doPost (Representation entity){
		int orderId=0;
		int fid = 0;
		int status =0;
		DatabaseSource db = Manager.db;
		try{
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			JSONArray jsonArr = jsonObj.getJSONArray("f_array");
			for (int i=0; i< jsonArr.length(); i ++){
				JSONObject jizz = jsonArr.getJSONObject(i);
				orderId = Integer.parseInt(jizz.getString("o_id"));
				fid = Integer.parseInt(jizz.getString("f_id"));
				status = Integer.parseInt(jizz.getString("status"));
				Order order = db.getBillTemp(orderId);
				List<FoodTemprary> foodList = order.getFoodTemp();
				for( FoodTemprary t : foodList){
					if(t.getFoodId() == fid){
						t.setStatus(status);
						System.out.println("tadada" + t.getStatus());
						
						break;
					}
				}
				order.setFoodTemp(foodList);
				db.updateBillTemp(order);
			}
			
			return new JsonRepresentation("{\"message\":\"done\"}");
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		
	}
	
	@Put("json")
	public Representation doPut(Representation entity){
		DatabaseSource db = Manager.db;
		String uidString = getQuery().getValues("q");
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		Order order = db.getBillTemp(Integer.parseInt(uidString));
		if(order == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		try {
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			List<FoodTemprary> foodList = order.getFoodTemp();
			for (FoodTemprary t: foodList){
				if (t.getFoodId() == Integer.parseInt(jsonObj.getString("f_id"))){
					t.setStatus(Integer.parseInt((String)jsonObj.getString("status")));
					break;
				}
			}
			order.setFoodTemp(foodList);
			db.updateBillTemp(order);
			return new JsonRepresentation("{\"message\":\"done\"}");
		
		} catch (Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"internal error\"}");
		}
		
	}
	
}
//1f62bd5222c034466d58121e6e089e55