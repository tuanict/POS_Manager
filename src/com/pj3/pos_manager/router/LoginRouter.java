package com.pj3.pos_manager.router;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.*;
import org.restlet.ext.json.JsonRepresentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pj3.*;
import com.pj3.pos_manager.res_obj.*;
import com.pj3.pos_manager.database.*;
import com.pj3.pos_manager.Manager;
import android.util.*;
public class LoginRouter  extends ServerResource{
	public static Context context;
	@Post
	public Representation doLogin(Representation entity){
		DatabaseSource db = Manager.db;
		context = Manager.context;
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
				ret.put("position", t.getPOSITION_p_id());
				String[] parts = t.getE_image().split("/");
				ret.put("image_name", parts[parts.length-1]);
				
				
				//File x = new File(context.getFilesDir(),t.getE_image());
				Bitmap bm = BitmapFactory.decodeFile(t.getE_image());
				ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] b = baos.toByteArray(); 
				String encodeImage = Base64.encodeToString(b, Base64.DEFAULT);
				ret.put("imageBase64", encodeImage);
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
