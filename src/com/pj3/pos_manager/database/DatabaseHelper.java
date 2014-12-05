/**
 * 
 */
package com.pj3.pos_manager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author L�C�ng
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	//Logcat tag 
	public static final String LOG = "DatabaseHelper";
	
	//Database name
	public static final String DATABASE_NAME = "POS";
	
	//Database version
	public static final int DATABASE_VERSION = 1;
	
	//Tables name
	public static final String TABLE_EMPLOYEE = "EMPLOYEE";
	public static final String TABLE_POSITION = "POSITION";
	public static final String TABLE_FOODSTATISTIC = "FOODSTATTISTIC";
	public static final String TABLE_BILL = "BILL";
	public static final String TABLE_MENU = "MENU";
	
	//Employee table - column name
	public static final String COLUMN_E_ID = "e_id";
	public static final String COLUMN_E_NAME = "e_name";
	public static final String COLUMN_E_EMAIL = "e_email";
	public static final String COLUMN_E_PASS = "e_pass";
	public static final String COLUMN_E_IMAGE = "e_image";
	public static final String COLUMN_E_PHONE = "e_phone_number";
	public static final String COLUMN_E_POSITION = "e_POSITION_p_id";
	
	//Position table - column names
	public static final String COLUMN_P_ID = "p_id";
	public static final String COLUMN_P_NAME = "p_name";
	public static final String COLUMN_P_SALARY = "p_salary";
	
	//FoodStistic - column names
	public static final String COLUMN_F_ID = "f_id";
	public static final String COLUMN_F_COUNT = "f_count";
	public static final String COLUMN_FBU = "fpu";
	public static final String COLUMN_F_B_ID = "f_b_id";
	public static final String COLUMN_F_M_ID = "f_m_id";
	
	//Bill - column names
	public static final String COLUMN_B_ID = "b_id";
	public static final String COLUMN_B_COUNT = "b_count";
	public static final String COLUMN_B_TIME_STAMP = "b_time_stamp";
	
	//Menu - column names
	public static final String COLUMN_M_ID = "m_food_id";
	public static final String COLUMN_M_NAME = "m_name";
	public static final String COLUMN_M_PRICE = "m_price";
	public static final String COLUMN_M_IMAGE = "m_image";
	public static final String COLUMN_M_STATUS = "m_status";
	public static final String COLUMN_M_OPTION = "m_option";
	
	// Table Create Statements
	//Employee table create statement
	public static final String DATABASE_E_CREATE = "create table "
			+ TABLE_EMPLOYEE
			+ " ( "
			+ COLUMN_E_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_E_NAME
			+ " VARCHAR(45) NOT NULL, "
			+ COLUMN_E_EMAIL
			+ " VARCHAR(20) NOT NULL, "
			+ COLUMN_E_PASS
			+ " VARCHAR(45) NOT NULL, "
			+ COLUMN_E_IMAGE
			+ " VARCHAR(45) NOT NULL, "
			+ COLUMN_E_PHONE
			+ " INT NOT NULL, "
			+ COLUMN_E_POSITION
			+ " INT NOT NULL ,"
			+ " CONSTRAINT e_POSITION_p_id FOREIGN KEY (e_POSITION_p_id) REFERENCES POSITION(p_id)"
			+ " ) ";
	
	//POSITION table create statement
	public static final String DATABASE_P_CREATE = "create table "
			+ TABLE_POSITION + " ( " 
			+ COLUMN_P_ID + " INT NOT NULL, "
			+ COLUMN_P_NAME + " VARCHAR(45) NOT NULL, "
			+ COLUMN_P_SALARY + " INT NOT NULL, "
			+ "PRIMARY KEY (p_id)"
			+ ")";
	
	//FoodStatis table create statement
	public static final String DATABASE_F_CREATE = "create table "
			+ TABLE_FOODSTATISTIC + " ( "
			+ COLUMN_F_ID + " INTEGER AUTOINCREAMENT, "
			+ COLUMN_F_COUNT + " INT NOT NULL, "
			+ COLUMN_F_B_ID + " INT NOT NULL, "
			+ COLUMN_F_M_ID + " INT NOT NULL,"
			+ COLUMN_FBU + " INT NOT NULL,"
			+ "PRIMARY KEY (f_id), "
			+ "CONSTRAINT f_b_id FOREIGN KEY (f_b_id) REFERENCES BILL(b_id), "
			+ "CONSTRAINT f_m_id FOREIGN KEY (f_m_id) REFERENCES MENU(m_id)"
			+ ")";
	
	// Bill table create statement
	public static final String DATABASE_B_CREATE = "create table "
			+ TABLE_BILL + " ( "
			+ COLUMN_B_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_B_COUNT + " INT NOT NULL, "
			+ COLUMN_B_TIME_STAMP + " VARCHAR(30) NOT NULL"
			+ ")";
	
	//MENU table create statement
	public static final String DATABASE_M_CREATE = "create table "
			+ TABLE_MENU + "("
			+ COLUMN_M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_M_NAME + " VARCHAR(45) NOT NULL,"
			+ COLUMN_M_PRICE + " INT NOT NULL, "
			+ COLUMN_M_IMAGE + " VARCHAR(45) NOT NULL,"
			+ COLUMN_M_STATUS + " BOOL NOT NULL,"
			+ COLUMN_M_OPTION + " VARCHAR(100)"
			+ ")";
	
	//Construct
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(DATABASE_E_CREATE);
		db.execSQL(DATABASE_P_CREATE);
		db.execSQL(DATABASE_F_CREATE);
		db.execSQL(DATABASE_B_CREATE);
		db.execSQL(DATABASE_M_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODSTATISTIC);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
		
		// create new tables
		onCreate(db);
	}

}
