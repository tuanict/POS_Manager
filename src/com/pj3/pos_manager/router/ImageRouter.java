package com.pj3.pos_manager.router;

import java.io.ByteArrayOutputStream;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.pj3.*;
import com.pj3.pos_manager.res_obj.*;
import com.pj3.pos_manager.database.*;
import com.pj3.pos_manager.Manager;
public class ImageRouter extends ServerResource {
	@Post("json")
	public Representation doPost(Representation entity){
		JsonRepresentation jsonRep;
		try {
			jsonRep = new JsonRepresentation(entity);
			JSONObject jsonObj  = jsonRep.getJsonObject();
			String filename = jsonObj.getString("file");
			
			Bitmap bm = BitmapFactory.decodeFile(filename);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] b = baos.toByteArray(); 
			String encodeImage = Base64.encodeToString(b, Base64.DEFAULT);
			JSONObject ret = new JSONObject();
			ret.put("message", "done");
			ret.put("file", filename);
			ret.put("base64_data", encodeImage);
			
			return new JsonRepresentation(ret);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		
	}
}
