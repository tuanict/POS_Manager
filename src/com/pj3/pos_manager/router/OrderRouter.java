package com.pj3.pos_manager.router;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.*;
import org.restlet.ext.json.JsonRepresentation;
import android.content.Context;

import com.pj3.*;
import com.pj3.pos_manager.res_obj.*;
import com.pj3.pos_manager.database.*;
import com.pj3.pos_manager.Manager;

public class OrderRouter extends ServerResource {
	
	
	@Get
	public Representation doGet (Representation entity) {
		DatabaseSource db = Manager.db;
		String uidString = getQuery().getValues("q");
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		else if (uidString.equals("all")){
			try {
			//get all order
				List<Order> lo = db.getOrderList();
				if(lo == null ) return new JsonRepresentation("{\"message\":\"no order\"}"); 
				JSONObject jo1 = new JSONObject();
				JSONArray orderArray = new JSONArray();
				JSONObject jo2 = new JSONObject();
				for(Order ret : lo){
					
						
						jo1.put("o_id", ret.getOrderId());
						jo1.put("t_id", ret.getTableId());
						jo1.put("t_count",ret.getCount());
						JSONArray foodArray = new JSONArray();
						for (FoodTemprary t : ret.getFoodTemp()){
							jo2 = new JSONObject();
							jo2.put("f_id", t.getFoodId());
							jo2.put("f_count", t.getCount());
							jo2.put("f_note", t.getNote());
							
							foodArray.put(jo2.toString());
							
						}
						jo1.put("f_array", foodArray);
					
					orderArray.put(jo1);
				}
				return new JsonRepresentation(new JSONObject().put("o_array", orderArray));
			} catch(Exception e){
				e.printStackTrace();
				String temp  = "{ \"o_array\": [{\"o_id\": \"0001\", \"t_id\": \"002\", \"t_count\": \"001\",\"f_array\":[{\"f_id\": \"002\",\"f_count\":\"3\",\"f_note\": \"error happen, this is test response\"}]}]}";
				return new JsonRepresentation(temp);
			}
		}
		else{
			//get order with specific id
			Order ret = db.getBillTemp(Integer.parseInt(uidString));
			JSONObject jo1 = new JSONObject();
			JSONObject jo2 = new JSONObject();
			
			try {
				
				jo1.put("o_id", ret.getOrderId());
				jo1.put("t_id", ret.getTableId());
				jo1.put("t_count",ret.getCount());
				JSONArray foodArray = new JSONArray();
				for (FoodTemprary t : ret.getFoodTemp()){
					jo2 = new JSONObject();
					
					jo2.put("f_id", t.getFoodId());
					jo2.put("f_count", t.getCount());
					jo2.put("f_note", t.getNote());
					foodArray.put(jo2.toString());
					
				}
				jo1.put("f_array", foodArray);
			} catch(Exception e){
				e.printStackTrace();
				String temp  = "{ \"o_array\": [{\"o_id\": \"0001\", \"t_id\": \"002\", \"t_count\": \"001\",\"f_array\":[{\"f_id\": \"002\",\"f_count\":\"3\",\"f_note\": \"this is sparta\"}]}]}";
				return new JsonRepresentation(temp);
			}
			return new JsonRepresentation(jo1);
		}
	}
	
	
	@Post("json")
	public Representation doPost (Representation entity){
		DatabaseSource db = Manager.db;
		try {
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			String tableId = jsonObj.getString("t_id");
			JSONArray foodArray = jsonObj.getJSONArray("f_array");
			
			Order order = new Order();
			order.setTableId(Integer.parseInt(tableId));
			FoodTemprary x = new FoodTemprary();
			List<FoodTemprary> listOfFood = new ArrayList<FoodTemprary>();
			for (int i = 0 ; i < foodArray.length(); i ++){
				x = new FoodTemprary();
				String fid = foodArray.getJSONObject(i).getString("f_id");
				x.setFoodId(Integer.parseInt(fid));
				String count = foodArray.getJSONObject(i).getString("f_count");
				x.setCount(Integer.parseInt(count));
				String note = foodArray.getJSONObject(i).getString("f_note");
				x.setNote(note);	
				listOfFood.add(x);
			}
			order.setFoodTemp(listOfFood);
			db.createBillTemp(order);
		}
		catch(Exception e){
			e.printStackTrace();
			//String temp  = "{ \"o_array\": [{\"o_id\": \"0001\", \"t_id\": \"002\", \"t_count\": \"001\",\"f_array\":[{\"f_id\": \"002\",\"f_count\":\"3\",\"f_note\": \"this is sparta\"}]}]}";
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		return new JsonRepresentation("{\"message\":\"done\"}");
	}
	
	
	
	@Put("json")
	public Representation doPut (Representation entity){
		DatabaseSource db = Manager.db;
		String uidString = getQuery().getValues("q");
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		Order ret1 = db.getBillTemp(Integer.parseInt(uidString));
		try {
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			JSONArray			ja	     = jsonObj.getJSONArray("f_array");
			for( int i =0; i < ja.length(); i ++){
				JSONObject jo1 = ja.getJSONObject(i);
				FoodTemprary ft = new FoodTemprary();
				ft.setFoodId(Integer.parseInt(jo1.getString("f_id")));
				ft.setCount(Integer.parseInt(jo1.getString("f_count")));
				ft.setNote(jo1.getString("f_note"));
				List<FoodTemprary> listFoodTemp = new ArrayList<FoodTemprary>(ret1.getFoodTemp());
				listFoodTemp.add(ft);
				ret1.setFoodTemp(listFoodTemp);
				
			}
			
			db.updateBillTemp(ret1);
			return new JsonRepresentation("{\"message\":\"done\"}");
		}
		catch (IOException e1 ){
			e1.printStackTrace();
			return new JsonRepresentation("\"message\": \"not found\"}");
		}
		catch(JSONException e2){
			e2.printStackTrace();
		}
		return null;
	}
	@Delete
	public Representation doDelete(Representation entity){
		return null;
	}
}
//1f62bd5222c034466d58121e6e089e55