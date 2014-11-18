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
//handle user router
public class UserRouter extends ServerResource {
	//?
	
	@Get
	public Representation doGet (Representation entity) throws Exception{
		DatabaseSource db = Manager.db;
		String uidString = getQuery().getValues("q");
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		Employee emp = db.getUser(Integer.parseInt(uidString));
		JSONObject ret  = new JSONObject();
		ret.put("e_id", emp.getE_id());
		ret.put("e_name", emp.getE_name());
		ret.put("e_email", emp.getE_image());
		ret.put("e_phone",emp.getE_phone_number());
		ret.put("e_position",emp.getPOSITION_p_id());
		
		return new JsonRepresentation(ret);
												
	}
	//create new user
	@Post("json")
	public Representation doPost (Representation entity)  {
		DatabaseSource db = Manager.db;
		try {
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			String username = jsonObj.getString("e_name");
			String email	= jsonObj.getString("e_email");
			String password	= jsonObj.getString("e_password");
			String phone_num = jsonObj.getString("e_phone");
			String position	= jsonObj.getString("e_position");
			db.createUser(new Employee(username,email,password,"",Integer.parseInt(phone_num),Integer.parseInt(position)));
			return new JsonRepresentation("{\"message\":\"done\"}");
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		
	}
	//edit user info
	@Put("json")
	public Representation doPut (Representation entity){
		DatabaseSource db = Manager.db;
		String uidString = getQuery().getValues("q");
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		
		try {
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			String username = jsonObj.getString("e_name");
			String email	= jsonObj.getString("e_email");
			String password	= jsonObj.getString("e_password");
			String phone_num = jsonObj.getString("e_phone_number");
			String position	= jsonObj.getString("e_position");
			db.updateUser(new Employee(Integer.parseInt(uidString),username,email,password,"",Integer.parseInt(phone_num),Integer.parseInt(position)));
			return new JsonRepresentation("{\"message\":\"done\"}");
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
	
	}
	//delete an user
	@Delete
	public Representation doDelete(Representation entity){
		DatabaseSource db = Manager.db;
		String uidString = getQuery().getValues("q");
		if(uidString == null ) return new JsonRepresentation("{\"message\":\"error\"}");
		db.deleteUser(Integer.parseInt(uidString));
		return new JsonRepresentation("{\"message\":\"done\"}");
	}
}




//1f62bd5222c034466d58121e6e089e55
//ZG9pbmcgc2hpdG91dA==