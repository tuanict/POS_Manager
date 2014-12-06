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
import com.pj3.pos_manager.POS_M;
import com.pj3.pos_manager.Manager;

public class LoginRouter  extends ServerResource{
	@Post
	public Representation doLogin(Representation entity){
		DatabaseSource db = POS_M.db;
		String email = "";
		String pass  = "";
		JSONObject ret  = new JSONObject();
		try{
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			email = jsonObj.getString("email");
			pass  = jsonObj.getString("password");
			Employee t = db.checkUser(email, pass);
			if(t != null){
				ret.put("message","success login");
				ret.put("email",t.getE_email());
				ret.put("username", t.getE_name());
				ret.put("id",t.getE_id());
				ret.put("image", t.getE_image());
				
			}
			else{
				ret.put("message", "login fail");
			}
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"internal erorr\"}");
		}
		return new JsonRepresentation(ret);
	}
}
