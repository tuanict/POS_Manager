/**
 * 
 */
package com.pj3.pos_manager.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.pj3.pos_manager.res_obj.Bill;
import com.pj3.pos_manager.res_obj.Employee;
import com.pj3.pos_manager.res_obj.Food;
import com.pj3.pos_manager.res_obj.FoodStatistic;
import com.pj3.pos_manager.res_obj.FoodTemprary;
import com.pj3.pos_manager.res_obj.Order;
import com.pj3.pos_manager.res_obj.Position;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



/**
 * @author L�C�ng
 *
 */
public class DatabaseSource implements SqliteAPIs{
	DatabaseHelper dHelper;
	File billTem;
	public static int billTempId = 0;
	public static final String FILENAME = "BillTemp.json";
	private Context context;
	JSONParser parser;
	public DatabaseSource(Context context){
		
		//important
		//context.deleteDatabase("POS");
		dHelper = new DatabaseHelper(context);
		this.context = context;
		//Init json object
		parser = new JSONParser();
		
		//Create file tamporary
		String[] files = context.getFilesDir().list();
		boolean ok = true;
		for(String file : files){
			if(file.equals(FILENAME)){
				ok = false;
				break;
			}
		}
		if(ok){
			billTem = new File(context.getFilesDir(), FILENAME);
			try {
				billTem.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		Log.e("Create File", ""+context.getFilesDir().list()[0].toString());   
		
	}
	@Override
	public int createUser(Employee user) {
		//Check email exits
		List<Employee> employees = this.getAllUsers();
		int size = employees.size();
		int count = 0;
		while(count < size){
			if (employees.get(count).getE_email().equals(user.getE_email()))
				return -1;
			else count++;
		}
		
		SQLiteDatabase db = dHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(dHelper.COLUMN_E_NAME,user.getE_name());
		values.put(dHelper.COLUMN_E_EMAIL, user.getE_email());
		values.put(dHelper.COLUMN_E_PASS, user.getE_pass());
		values.put(dHelper.COLUMN_E_IMAGE, user.getE_image());
		values.put(dHelper.COLUMN_E_PHONE, user.getE_phone_number());
		values.put(dHelper.COLUMN_E_POSITION, user.getPOSITION_p_id());
		
		//insert row
		int userId = (int) db.insert(dHelper.TABLE_EMPLOYEE, null, values);
		db.close();
		return userId;
	}

	@Override
	public Employee getUser(int userId) {
		SQLiteDatabase db = dHelper.getReadableDatabase();
		String query = "SELECT * FROM " + dHelper.TABLE_EMPLOYEE
				+ " WHERE " + dHelper.COLUMN_E_ID + " = " + userId;
		
		Log.e(dHelper.LOG, query);
		Cursor c = db.rawQuery(query, null);
		
		if(c != null)
			c.moveToFirst();
		
		Employee user = new Employee();
		user.setE_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_E_ID)));
		user.setE_name(c.getString(c
				.getColumnIndex(dHelper.COLUMN_E_NAME)));
		user.setE_email(c.getString(c.getColumnIndex(dHelper.COLUMN_E_EMAIL)));
		user.setE_pass(c.getString(c.getColumnIndex(dHelper.COLUMN_E_PASS)));
		user.setE_image(c.getString(c.getColumnIndex(dHelper.COLUMN_E_IMAGE)));
		user.setE_phone_number(c.getInt(c.getColumnIndex(dHelper.COLUMN_E_PHONE)));
		user.setPOSITION_p_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_E_POSITION)));
		db.close();
		return user;
	}

	@Override
	public List<Employee> getAllUsers() {
		List<Employee> employees = new ArrayList<Employee>();
		String query = "SELECT * FROM " + dHelper.TABLE_EMPLOYEE;
		Log.e(dHelper.LOG, query);
		
		SQLiteDatabase db = dHelper.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		//looping through all rows and adding to list
		if(c.moveToFirst()){
			do{
				Employee user = new Employee();
				user.setE_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_E_ID)));
				user.setE_name(c.getString(c
						.getColumnIndex(dHelper.COLUMN_E_NAME)));
				user.setE_email(c.getString(c.getColumnIndex(dHelper.COLUMN_E_EMAIL)));
				user.setE_pass(c.getString(c.getColumnIndex(dHelper.COLUMN_E_PASS)));
				user.setE_image(c.getString(c.getColumnIndex(dHelper.COLUMN_E_IMAGE)));
				user.setE_phone_number(c.getInt(c.getColumnIndex(dHelper.COLUMN_E_PHONE)));
				user.setPOSITION_p_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_E_POSITION)));
				
				employees.add(user);
			}while(c.moveToNext());
		}
		db.close();
		return employees;
	}

	@Override
	public List<Employee> getUserByPosition(int postionId) {
		List<Employee> employees = new ArrayList<Employee>();
		String query = "SELECT * FROM " + dHelper.TABLE_EMPLOYEE 
				+ " WHERE " + dHelper.COLUMN_E_POSITION + " = " +postionId;
		Log.e(dHelper.LOG, query);
		
		SQLiteDatabase db = dHelper.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		//looping through all rows and adding to list
		if(c.moveToFirst()){
			do{
				Employee user = new Employee();
				user.setE_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_E_ID)));
				user.setE_name(c.getString(c
						.getColumnIndex(dHelper.COLUMN_E_NAME)));
				user.setE_email(c.getString(c.getColumnIndex(dHelper.COLUMN_E_EMAIL)));
				user.setE_pass(c.getString(c.getColumnIndex(dHelper.COLUMN_E_PASS)));
				user.setE_image(c.getString(c.getColumnIndex(dHelper.COLUMN_E_IMAGE)));
				user.setE_phone_number(c.getInt(c.getColumnIndex(dHelper.COLUMN_E_PHONE)));
				user.setPOSITION_p_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_E_POSITION)));
				
				employees.add(user);
			}while(c.moveToNext());
		}
		db.close();
		return employees;
	}

	@Override
	public boolean updateUser(Employee user) {
		SQLiteDatabase db = dHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		boolean ok = false;
		if(user.getE_name() != null)
			values.put(dHelper.COLUMN_E_NAME,user.getE_name());
		if(user.getE_email() != null)
			values.put(dHelper.COLUMN_E_EMAIL, user.getE_email());
		if(user.getE_pass() != null)
			values.put(dHelper.COLUMN_E_PASS, user.getE_pass());
		if(user.getE_image() != null)
			values.put(dHelper.COLUMN_E_IMAGE, user.getE_image());
		if(user.getE_phone_number() != 0)
			values.put(dHelper.COLUMN_E_PHONE, user.getE_phone_number());
		if(user.getPOSITION_p_id() != 0)
			values.put(dHelper.COLUMN_E_POSITION, user.getPOSITION_p_id());
		try{
			int result = db.update(dHelper.TABLE_EMPLOYEE, values,
					dHelper.COLUMN_E_ID + " = ?",
					new String[] { String.valueOf(user.getE_id()) });
			ok = true;
		}catch(Exception e){
			ok = false;
		}
		db.close();
		return ok;
	}

	@Override
	public boolean checkUser(String username, String password) {
		List<Employee> employees = this.getAllUsers();
		int size = employees.size();
		int count = 0;
		while(count < size){
			if (employees.get(count).getE_email().equals(username)
					&& employees.get(count).getE_pass().equals(password))
				return true;
			else count++;
		}
		return false;
	}

	@Override
	public boolean deleteUser(int userId) {
		SQLiteDatabase db = dHelper.getWritableDatabase();
		boolean ok = false;
		try{
			db.delete(dHelper.TABLE_EMPLOYEE, dHelper.COLUMN_E_ID + " = ?",
					new String[] { String.valueOf(userId) });
			
			ok = true;
		}catch(Exception e){
			ok = false;
		}
		db.close();
		return ok;
	}

	@Override
	public int createFood(Food menu) {
		List<Food> menus = this.getAllFood();
		int size = menus.size();
		int count = 0;
		while(count < size){
			if(menus.get(count).getM_name().equals(menu.getM_name()))
				return -1;
			else count ++;
		}
		
		SQLiteDatabase db = dHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(dHelper.COLUMN_M_IMAGE, menu.getM_image());
		values.put(dHelper.COLUMN_M_NAME, menu.getM_name());
		values.put(dHelper.COLUMN_M_PRICE, menu.getM_price());
		values.put(dHelper.COLUMN_M_STATUS, menu.getM_status());
		values.put(dHelper.COLUMN_M_OPTION, menu.getM_option());
		
		//insert row
		int menuId = (int) db.insert(dHelper.TABLE_MENU, null, values);
		db.close();
		return menuId;
	}

	@Override
	public boolean updateMenu(Food menu) {
		SQLiteDatabase db = dHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(dHelper.COLUMN_M_IMAGE, menu.getM_image());
		values.put(dHelper.COLUMN_M_NAME, menu.getM_name());
		values.put(dHelper.COLUMN_M_PRICE, menu.getM_price());
		values.put(dHelper.COLUMN_M_STATUS, menu.getM_status());
		values.put(dHelper.COLUMN_M_OPTION, menu.getM_option());
		
		try{
			db.update(dHelper.TABLE_MENU, values,
					dHelper.COLUMN_M_ID + " = ? ",
					new String[] { String.valueOf(menu.getM_food_id()) });
			db.close();
			return true;
		}catch(Exception e){
			db.close();
			return false;
		}
	}

	@Override
	public boolean changeStatusFood(int foodId, boolean status) {
		Food menu = getFood(foodId);
		if(menu == null)
			return false;
		menu.setM_status(status);
		return this.updateMenu(menu);
	}

	@Override
	public Food getFood(int foodId) {
		SQLiteDatabase db = dHelper.getReadableDatabase();
		String query = "SELECT * FROM " + dHelper.TABLE_MENU + " WHERE "
				+ dHelper.COLUMN_M_ID + " = " + foodId;
		Log.e(dHelper.LOG, query);
		
		Cursor c = db.rawQuery(query, null);
		if(c != null)
			c.moveToFirst();
		
		Food menu = new Food();
		menu.setM_food_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_M_ID)));
		menu.setM_image(c.getString(c.getColumnIndex(dHelper.COLUMN_M_IMAGE)));
		menu.setM_name(c.getString(c.getColumnIndex(dHelper.COLUMN_M_NAME)));
		menu.setM_price(c.getInt(c.getColumnIndex(dHelper.COLUMN_M_PRICE)));
		menu.setM_status(c.getInt(c.getColumnIndex(dHelper.COLUMN_M_STATUS)) == 1);
		menu.setM_option(c.getString(c.getColumnIndex(dHelper.COLUMN_M_OPTION)));
		db.close();
		return menu;
	}

	@Override
	public List<Food> getAllFood() {
		List<Food> menus = new ArrayList<Food>();
		String query = "SELECT * FROM " + dHelper.TABLE_MENU;
		Log.e(dHelper.LOG,query);
		
		SQLiteDatabase db = dHelper.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		// looping through all rows and adding to list
		if(c.moveToFirst()){
			do{
				Food menu = new Food();
				menu.setM_food_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_M_ID)));
				menu.setM_image(c.getString(c.getColumnIndex(dHelper.COLUMN_M_IMAGE)));
				menu.setM_name(c.getString(c.getColumnIndex(dHelper.COLUMN_M_NAME)));
				menu.setM_price(c.getInt(c.getColumnIndex(dHelper.COLUMN_M_PRICE)));
				menu.setM_status(c.getInt(c.getColumnIndex(dHelper.COLUMN_M_STATUS)) == 1);
				menu.setM_option(c.getString(c.getColumnIndex(dHelper.COLUMN_M_OPTION)));
				
				menus.add(menu);
			}while(c.moveToNext());
		}
		db.close();
		return menus;
	}
	
	public List<Food> getFoodsByStatus(boolean type){
		List<Food> allFood = getAllFood();
		List<Food> foodStatus = new ArrayList<Food>();
		int sie = allFood.size();
		for(int i = 0; i < sie; i++){
			if(allFood.get(i).getM_status() == type){
				foodStatus.add(allFood.get(i));
			}
		}
		return foodStatus;
		
	}
	
	private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}

	@Override
	public int createBill(Bill bill) {
		SQLiteDatabase db = dHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(dHelper.COLUMN_B_COUNT, bill.getB_count());
		values.put(dHelper.COLUMN_B_TIME_STAMP, this.getDateTime());
		
		int billId = (int) db.insert(dHelper.TABLE_BILL, null, values);
		db.close();
		return billId;
	}
	
	@SuppressWarnings("deprecation")
	private Date convertTime(String time){
		Date date = new Date();
		String[] splitTime = time.split(" ");
		String[] splitDate = splitTime[0].split("-");
		String[] splitHour  = splitTime[1].split(":");
		
		date.setYear(Integer.parseInt(splitDate[0]));
		date.setMonth(Integer.parseInt(splitDate[1]));
		date.setDate(Integer.parseInt(splitDate[2]));
		
		date.setHours(Integer.parseInt(splitHour[0]));
		date.setMinutes(Integer.parseInt(splitHour[1]));
		date.setSeconds(Integer.parseInt(splitHour[2]));
		
		return date;
	}

	@Override
	public Bill getBill(int billId) {
		Bill bill = new Bill();
		SQLiteDatabase db = dHelper.getReadableDatabase();
		String query = "SELECT * FROM " + dHelper.TABLE_BILL + " WHERE "
				+ dHelper.COLUMN_B_ID + " = " + billId;
		Log.e(dHelper.LOG, query);
		Cursor c = db.rawQuery(query, null);
		if(c != null)
			c.moveToFirst();
		bill.setB_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_B_ID)));
		bill.setB_count(c.getInt(c.getColumnIndex(dHelper.COLUMN_B_COUNT)));
		String time = c.getString(c.getColumnIndexOrThrow(dHelper.COLUMN_B_TIME_STAMP));
		bill.setB_time_stamp(this.convertTime(time));
		db.close();
		return bill;
	}
	
	@Override
	public List<Bill> getAllBill() {
		List<Bill> bills = new ArrayList<Bill>();
		
		SQLiteDatabase db = dHelper.getReadableDatabase();
		String query = "SELECT * FROM " + dHelper.TABLE_BILL;
		Log.e(dHelper.LOG, query);
		Cursor c = db.rawQuery(query, null);
		
		//looping through all rows and adding to list
		if(c.moveToFirst()){
			do{
				Bill bill = new Bill();
				bill.setB_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_B_ID)));
				bill.setB_count(c.getInt(c.getColumnIndex(dHelper.COLUMN_B_COUNT)));
				String time = c.getString(c.getColumnIndexOrThrow(dHelper.COLUMN_B_TIME_STAMP));
				bill.setB_time_stamp(this.convertTime(time));
				
				bills.add(bill);
			}while(c.moveToNext());
		}
		db.close();
		return bills;
	}
	
	@Override
	public boolean deleteBill(int billId) {
		SQLiteDatabase db = dHelper.getWritableDatabase();
		boolean ok = false;
		try{
			db.delete(dHelper.TABLE_BILL, dHelper.COLUMN_B_ID + " = ?",
					new String[] { String.valueOf(billId) });
			ok = true;
		}catch(Exception e){
			ok = false;
		}
		db.close();
		return ok;
	}

	@Override
	public int createBillTemp(Order order) {
		order.setOrderId(billTempId);
		String orderFileName = "o_"+ Integer.toString(billTempId) + ".json";
		File orderFile = new File(context.getFilesDir(), orderFileName);
		JSONObject root = new JSONObject();
		try {
			root = (JSONObject) parser.parse(new FileReader(billTem));
			JSONObject newBill = new JSONObject();
			newBill.put("orderId", order.getOrderId());
			newBill.put("tableId", order.getTableId());
			newBill.put("count", order.getCount());
			
			JSONArray foods = new JSONArray();
			int size = order.getFoodTemp().size();
			for(int i = 0; i < size; i++){
				JSONObject food = new JSONObject();
				food.put("foodId", order.getFoodTemp().get(i).getFoodId());
				food.put("foodCount", order.getFoodTemp().get(i).getCount());
				food.put("note", order.getFoodTemp().get(i).getNote());
				food.put("status",1);
				foods.add(food);
			}
			newBill.put("foods", foods);
			root.put(""+order.getOrderId(), newBill);
			
			FileWriter file = new FileWriter(billTem);
			file.write(root.toJSONString());
			file.flush();
			file.close();
			billTempId++;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return order.getOrderId();
	}

	@Override
	public boolean updateBillTemp(Order order) {
		JSONObject root = new JSONObject();
		try {
			root = (JSONObject) parser.parse(new FileReader(billTem));
			JSONObject update = (JSONObject) root.get(""+order.getOrderId());
			update.put("orderId", order.getOrderId());
			update.put("tableId", order.getTableId());
			update.put("count", order.getCount());
			JSONArray foods = new JSONArray();
			int size = order.getFoodTemp().size();
			for(int i = 0; i < size; i++){
				JSONObject food = new JSONObject();
				food.put("foodId", order.getFoodTemp().get(i).getFoodId());
				food.put("foodCount", order.getFoodTemp().get(i).getCount());
				food.put("note", order.getFoodTemp().get(i).getNote());
				food.put("status", order.getFoodTemp().get(i).getStatus());
				foods.add(food);
			}
			update.put("foods", foods);
			
			root.put(""+order.getOrderId(), update);
			FileWriter file = new FileWriter(billTem);
			file.write(root.toJSONString());
			file.flush();
			file.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteBillTemp(int billId) {
		JSONObject root = new JSONObject();
		try {
			root = (JSONObject) parser.parse(new FileReader(billTem));
			root.remove(""+billId);
			FileWriter file = new FileWriter(billTem);
			file.write(root.toJSONString());
			file.flush();
			file.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Order getBillTemp(int billId) {
		JSONObject root = new JSONObject();
		try {
			root = (JSONObject) parser.parse(new FileReader(billTem));
			Order order = new Order();
			JSONObject object = (JSONObject) root.get("" + billId);
			order.setOrderId(billId);
			order.setTableId((Integer) object.get("tableId"));
			order.setCount((Integer) object.get("count"));
			JSONArray foods = (JSONArray) object.get("foods");
			int size = foods.size();
			
			List<FoodTemprary> foList = new ArrayList<FoodTemprary>();
			for(int i = 0; i < size; i++){
				FoodTemprary fo = new FoodTemprary();
				JSONObject foodTemp = new JSONObject();
				foodTemp = (JSONObject) foods.get(i);
				fo.setFoodId((Integer) foodTemp.get("foodId"));
				fo.setCount((Integer) foodTemp.get("foodCount"));
				fo.setNote((String) foodTemp.get("note"));
				fo.setStatus((Integer) foodTemp.get("status"));
				foList.add(fo);
			}
			
			order.setFoodTemp(foList);
			return order;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Order> getOrderList(){
		return null;
	}
	@Override
	public int createFoodStatistic(FoodStatistic foodStatistic) {
		SQLiteDatabase db = dHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(dHelper.COLUMN_F_COUNT, foodStatistic.getF_count());
		values.put(dHelper.COLUMN_F_B_ID, foodStatistic.getF_b_id());
		values.put(dHelper.COLUMN_F_M_ID, foodStatistic.getF_m_id());
		values.put(dHelper.COLUMN_FBU, foodStatistic.getFpu());
		
		
		//insert row
		int fId = (int) db.insert(dHelper.TABLE_MENU, null, values);
		db.close();
		return fId;
	}
	@Override
	public List<FoodStatistic> getCookingOrder(int billId) {
		List<FoodStatistic> foods = new ArrayList<FoodStatistic>();
		String query = "SELECT * FROM " + dHelper.TABLE_FOODSTATISTIC + " WHERE " + dHelper.COLUMN_F_B_ID  + "= " + Integer.toString(billId);
		Log.e(dHelper.LOG,query);
		
		SQLiteDatabase db = dHelper.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		// looping through all rows and adding to list
		if(c.moveToFirst()){
			do{
				FoodStatistic foodStatistic = new FoodStatistic();
				foodStatistic.setF_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_F_ID)));
				foodStatistic.setF_count(c.getInt(c.getColumnIndex(dHelper.COLUMN_F_COUNT)));
				foodStatistic.setF_b_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_F_B_ID)));
				foodStatistic.setF_m_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_F_M_ID)));
				foodStatistic.setFpu(c.getInt(c.getColumnIndex(dHelper.COLUMN_FBU)));
				
				foods.add(foodStatistic);
			}while(c.moveToNext());
		}
		db.close();
		return foods;
	}
	@Override
	public boolean updateFoodStatus(FoodStatistic fStatistic) {
		SQLiteDatabase db = dHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(dHelper.COLUMN_F_COUNT, fStatistic.getF_count());
		values.put(dHelper.COLUMN_F_B_ID, fStatistic.getF_b_id());
		values.put(dHelper.COLUMN_F_M_ID, fStatistic.getF_m_id());
		values.put(dHelper.COLUMN_FBU, fStatistic.getFpu());
		
		try{
			db.update(dHelper.TABLE_FOODSTATISTIC, values,
					dHelper.COLUMN_F_ID + " = ? ",
					new String[] { String.valueOf(fStatistic.getF_id()) });
			db.close();
			return true;
		}catch(Exception e){
			db.close();
			return false;
		}
	}
	@Override
	public int createPosition(Position position) {
		SQLiteDatabase db = dHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(dHelper.COLUMN_P_ID, position.getP_id());
		values.put(dHelper.COLUMN_P_NAME, position.getP_name());
		values.put(dHelper.COLUMN_P_SALARY, position.getP_salary());
		
		db.insert(dHelper.TABLE_POSITION, null, values);
		db.close();
		return position.getP_id();
	}
	@Override
	public List<Position> getPositions() {
		List<Position> positions = new ArrayList<Position>();
		String query = "SELECT * FROM " + dHelper.TABLE_POSITION;
		Log.e(dHelper.LOG,query);
		
		SQLiteDatabase db = dHelper.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		// looping through all rows and adding to list
		if(c.moveToFirst()){
			do{
				Position position = new Position();
				position.setP_id(c.getInt(c.getColumnIndex(dHelper.COLUMN_P_ID)));
				position.setP_name(c.getString(c.getColumnIndex(dHelper.COLUMN_P_NAME)));
				position.setP_salary(c.getInt(c.getColumnIndex(dHelper.COLUMN_P_SALARY)));
				
				positions.add(position);
			}while(c.moveToNext());
		}
		db.close();
		return positions;
	}
}
