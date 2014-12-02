package com.pj3.pos_manager;


//general dependencies
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pj3.pos_manager.router.BillRouter;
import com.pj3.pos_manager.router.FoodStatusRouter;
import com.pj3.pos_manager.router.MenuRouter;
import com.pj3.pos_manager.router.OrderRouter;
//import com.pj3.pos_manager.router.RESTResource;
import com.pj3.pos_manager.router.UserRouter;
//local dependencies
import com.pj3.pos_manager.MainActivity;
import com.pj3.pos_manager.R;
import com.pj3.pos_manager.database.DatabaseSource;
import com.pj3.pos_manager.res_obj.Employee;
import com.pj3.pos_manager.res_obj.Food;
import com.pj3.pos_manager.res_obj.FoodTemprary;
import com.pj3.pos_manager.res_obj.Order;
import com.pj3.pos_manager.res_obj.Position;




//android dependencies
import android.media.Image;
import android.net.Uri;

import com.pj3.pos_manager.R;
import com.pj3.pos_manager.res_obj.Employee;

import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;



//restlet dependencies
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.routing.VirtualHost;
import org.restlet.Component;


public class Manager extends Activity{
	TabHost tabHost;
	List<Employee> employees;
	Spinner spPosition;
	GridLayout gridEmployees;
	PopupWindow popupWindow;
	Map<LinearLayout, Employee> itemEmployee;
	public static DatabaseSource db ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		db = new DatabaseSource(this);
		
		
		Component serverComponent = new Component();
		serverComponent.getServers().add(Protocol.HTTP, 8182);  
		final Router router = new Router(serverComponent.getContext().createChildContext());
		
		//test router
		//RESTResource r = new RESTResource();
		//router.attach("/",r.getClass());
		
		router.attach("/api/users", UserRouter.class);
		router.attach("/api/menus", MenuRouter.class);
		router.attach("/api/bills", BillRouter.class);
		router.attach("/api/orders", OrderRouter.class);
		router.attach("/api/foodstatus", FoodStatusRouter.class);
		
		VirtualHost server = serverComponent.getDefaultHost();
		server.attach(router); 
		
		
		try {
			serverComponent.start();
		} catch (Exception e) {e.printStackTrace();};
		main_employee();
		menu_view();
		payment_main();
		main_statistic();
		db.clearFileTemp();
	}
	
	
	//Phan nay cua Employee------------------------------------------------------
	public void main_employee(){
		employees = new ArrayList<Employee>();
		itemEmployee = new HashMap<LinearLayout, Employee>();
		gridEmployees = (GridLayout) findViewById(R.id.gridEmployee);
		spPosition = (Spinner) findViewById(R.id.spPosition);
		addEmployee();
		loadTab();
		loadSpPosition();
		loadGridEmployees(loadEmploy());
		handlerSpinerPos();
	}
	
	public void initTable_Position(){
		Position position = new Position();
		position.setP_id(1);
		position.setP_name("Quản lí");
		position.setP_salary(5000000);

		db.createPosition(position);
		position.setP_id(2);
		position.setP_name("Bồi bàn");
		position.setP_salary(3000000);

		db.createPosition(position);
		position.setP_id(3);
		position.setP_name("Đầu bếp");
		position.setP_salary(3500000);

		db.createPosition(position);
	}
	
	public List<Employee> loadEmploy(){
		List<Employee> employeess = new ArrayList<Employee>();
		int select = spPosition.getSelectedItemPosition();
		if(select == 0){
			employeess = db.getAllUsers();
		}else if(select == 1){
			employeess = db.getUserByPosition(1);
		}else if(select == 2){
			employeess = db.getUserByPosition(2);
		}else if(select == 3){
			employeess = db.getUserByPosition(3);
		}
		return employeess;
	}
	
	public void loadSpPosition(){
//		List<Position> positions = db.getPositions();
		List<String> sList = new ArrayList<String>();
		sList.add("Tất cả");
		sList.add("Quản lí");
		sList.add("Bồi bàn");
		sList.add("Đầu bếp");
//		int size = positions.size();
//		for(int i = 0; i < size; i++){
//			sList.add(positions.get(i).getP_name());
//		}
		
		ArrayAdapter<String> adapterPostion = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, sList);
		
		spPosition.setAdapter(adapterPostion);
		
	}
	
	@SuppressLint("NewApi")
	public void handlerSpinerPos(){
		spPosition.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				resetGridview();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
	}
	
	public void addEmployee(){
		ImageView addPicture = (ImageView) findViewById(R.id.addUserAction);
		addPicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				active_Add_Employee();
			}
		});
	}
	
	public void active_Add_Employee(){
		final Dialog dialogAdd = new Dialog(Manager.this);
    	dialogAdd.setContentView(R.layout.e_info_action);
    	dialogAdd.setTitle("Thêm nhân viên");
    	Button cancelButton = (Button)dialogAdd.findViewById(R.id.m_e_cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogAdd.dismiss();
			}
		});
    	Button doneButton = (Button)dialogAdd.findViewById(R.id.m_e_done_button);
		doneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText e_name = (EditText) dialogAdd.findViewById(R.id.e_name_tb);
				RadioGroup radio_position = (RadioGroup) dialogAdd.findViewById(R.id.radio_position);
				RadioButton r_manager = (RadioButton) dialogAdd.findViewById(R.id.radio_manager);
        		RadioButton r_waiter = (RadioButton) dialogAdd.findViewById(R.id.radio_waiter);
        		RadioButton r_chef = (RadioButton) dialogAdd.findViewById(R.id.radio_chef);
				int position = 0;
				if(r_manager.isChecked()){
					position = 1;
				}else if(r_waiter.isChecked()){
					position = 2;
				}else if(r_chef.isChecked()){
					position = 3;
				}
				
				EditText e_email = (EditText) dialogAdd.findViewById(R.id.e_email_tb);
				EditText e_phone = (EditText) dialogAdd.findViewById(R.id.e_phoneNumber_tb);
				EditText e_pass = (EditText) dialogAdd.findViewById(R.id.e_pass_tb);
				String name = e_name.getText().toString();
				String email = e_email.getText().toString();
				String pass = e_pass.getText().toString();
				String phone = e_phone.getText().toString();
				if (!checkNull(name) && !checkNull(email)
						&& !checkNull(pass) && position != 0
						&& !checkNull(phone) && email.contains("@")) {
					try{
						Employee e = new Employee(name, email, pass, "Anh",
								Integer.valueOf(phone), position);
						db.createUser(e);
						resetGridview();
						dialogAdd.setTitle("Thêm nhân viên");
						Toast.makeText(getApplicationContext(),
								"Thêm nhân viên thành công!",
								Toast.LENGTH_SHORT).show();
						dialogAdd.dismiss();
					}catch(Exception e){
						Toast.makeText(getApplicationContext(),
								"Số điện thoại phải là số!",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					dialogAdd.setTitle("Thêm nhân viên. Chưa điền đầy đủ thông tin");
					Toast.makeText(getApplicationContext(),
							"Hãy chắc chắn đã điền đầy đủ thông tin!",
							Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
		
		ImageView profile = (ImageView)dialogAdd.findViewById(R.id.picture_profile);
		profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Chưa xử lí chọn ảnh", Toast.LENGTH_SHORT).show();
			}
		});
    	
    	dialogAdd.show();
    	
	}
	
	public boolean checkNull(String input){
		return input.equals(" ");
	}
	
	@SuppressLint("NewApi")
	public void resetGridview(){
		gridEmployees.removeAllViews();
		loadGridEmployees(loadEmploy());
	}
	@SuppressLint("NewApi")
	public void loadGridEmployees(final List<Employee> employees){
		int numberEmployee_current = employees.size();
		for(int i = 0; i < numberEmployee_current; i++){
			LinearLayout item = new LinearLayout(Manager.this);
			item.setOrientation(LinearLayout.VERTICAL);
			item.setBackgroundResource(R.drawable.frame_item_in_grid);
			item.setLayoutParams(new LayoutParams(300, 350));
			item.setPadding(50, 50, 50, 50);
			item.setGravity(1);
			
			item.setWeightSum(3);
			final int k = i;
			itemEmployee.put(item, employees.get(i));
			item.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					final Employee e_item = employees.get(k);
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(Manager.this);
					alertDialog.setIcon(R.drawable.ic_launcher);
	                alertDialog.setTitle("Quản lí nhân viên: "+ e_item.getE_name());
	 
	                alertDialog.setPositiveButton("Xem", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	viewEmployee(e_item);
	                    }
	                });
	 
	                // Setting Negative "Edit" Button
	                alertDialog.setNegativeButton("Chỉnh sửa", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	editEmployee(e_item);
	                    }
	                });
	 
	                // Setting Netural "delete" Button
	                alertDialog.setNeutralButton("Xóa", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	AlertDialog dialogConfirm = new AlertDialog.Builder(Manager.this)
	                    	.setMessage("Bạn chắc chắn muốn xóa " + e_item.getE_name() + " ra khỏi danh sách?")
	                    	.setIcon(R.drawable.ic_launcher)
	                    	.setTitle("Xác nhận")
	                    	.setPositiveButton("Có", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									boolean ok = db.deleteUser(e_item.getE_id());
				                    if(ok){
				                    	resetGridview();
				                    	Toast.makeText(getApplicationContext(), "Xóa thành công!",
				                                        Toast.LENGTH_SHORT).show();
				                    	}else Toast.makeText(getApplicationContext(), "Xóa thất bại!",
		                                        Toast.LENGTH_SHORT).show();
								}
							})
	                    	.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							})
	                    	.create();
	                    	dialogConfirm.show();
		                    }
	                });
	 
	                // Showing Alert Message
	                alertDialog.show();
					return false;
				}
			});
			
			ImageView image = new ImageView(Manager.this);
			image.setBackgroundResource(R.drawable.man_brown);
			
			TextView name = new TextView(Manager.this);
			name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			name.setText(employees.get(i).getE_name());
			
			TextView phoneNumber = new TextView(Manager.this);
			phoneNumber.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			phoneNumber.setText(""+employees.get(i).getE_phone_number());
			
			item.addView(image);
			item.addView(name);
			item.addView(phoneNumber);
			
			gridEmployees.addView(item);
		}
		
	}
	public void viewEmployee(final Employee e_item){

    	final Dialog dialogView = new Dialog(Manager.this);
		dialogView.setContentView(R.layout.e_info_action);
		dialogView.setTitle("Xem nhân viên");
		
		EditText e_name = (EditText) dialogView.findViewById(R.id.e_name_tb);
		e_name.setText(e_item.getE_name());
		e_name.setFocusable(false);
		
		RadioGroup radio_position = (RadioGroup) dialogView.findViewById(R.id.radio_position);
		RadioButton r_manager = (RadioButton) dialogView.findViewById(R.id.radio_manager);
		RadioButton r_waiter = (RadioButton) dialogView.findViewById(R.id.radio_waiter);
		RadioButton r_chef = (RadioButton) dialogView.findViewById(R.id.radio_chef);
		if(e_item.getPOSITION_p_id() == 1){
			radio_position.check(r_manager.getId());
		}else if(e_item.getPOSITION_p_id() == 2){
			radio_position.check(r_waiter.getId());
		}else if(e_item.getPOSITION_p_id() == 3){
			radio_position.check(r_chef.getId());
		}
		
		r_manager.setEnabled(false);
		r_waiter.setEnabled(false);
		r_chef.setEnabled(false);
		
		EditText e_email = (EditText) dialogView.findViewById(R.id.e_email_tb);
		e_email.setText(e_item.getE_email());
		e_email.setFocusable(false);
		
		EditText e_phone = (EditText) dialogView.findViewById(R.id.e_phoneNumber_tb);
		e_phone.setText(""+e_item.getE_phone_number());
		e_phone.setFocusable(false);
		
		EditText e_pass = (EditText) dialogView.findViewById(R.id.e_pass_tb);
		e_pass.setText(""+e_item.getE_pass());
		e_pass.setFocusable(false);
		
		Button cancelButton = (Button)dialogView.findViewById(R.id.m_e_cancel_button);
		cancelButton.setText("Xong");
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogView.dismiss();
			}
		});
		Button doneButton = (Button)dialogView.findViewById(R.id.m_e_done_button);
		doneButton.setText("Chỉnh sửa");
		doneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogView.dismiss();
				editEmployee(e_item);
			}
		});
		
		ImageView profile = (ImageView)dialogView.findViewById(R.id.picture_profile);
		profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Chưa xử lí chọn ảnh", Toast.LENGTH_SHORT).show();
			}
		});
		dialogView.show();
	}
	
	public void editEmployee(final Employee e_item){

    	final Dialog dialogEdit = new Dialog(Manager.this);
    	dialogEdit.setContentView(R.layout.e_info_action);
    	dialogEdit.setTitle("Chỉnh sửa");
		
		final EditText e_name = (EditText) dialogEdit.findViewById(R.id.e_name_tb);
		e_name.setText(e_item.getE_name());
		
		final RadioGroup radio_position = (RadioGroup) dialogEdit.findViewById(R.id.radio_position);
		final RadioButton r_manager = (RadioButton) dialogEdit.findViewById(R.id.radio_manager);
		final RadioButton r_waiter = (RadioButton) dialogEdit.findViewById(R.id.radio_waiter);
		final RadioButton r_chef = (RadioButton) dialogEdit.findViewById(R.id.radio_chef);
		if(e_item.getPOSITION_p_id() == 1){
			radio_position.check(r_manager.getId());
		}else if(e_item.getPOSITION_p_id() == 2){
			radio_position.check(r_waiter.getId());
		}else if(e_item.getPOSITION_p_id() == 3){
			radio_position.check(r_chef.getId());
		}
		
		final EditText e_email = (EditText) dialogEdit.findViewById(R.id.e_email_tb);
		e_email.setText(e_item.getE_email());
		
		final EditText e_phone = (EditText) dialogEdit.findViewById(R.id.e_phoneNumber_tb);
		e_phone.setText(""+e_item.getE_phone_number());
		
		final EditText e_pass = (EditText) dialogEdit.findViewById(R.id.e_pass_tb);
		e_pass.setText(""+e_item.getE_pass());
		
		Button cancelButton = (Button)dialogEdit.findViewById(R.id.m_e_cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogEdit.dismiss();
			}
		});
		Button doneButton = (Button)dialogEdit.findViewById(R.id.m_e_done_button);
		doneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = 1;
				if(r_manager.isChecked()){
					position = 1;
				}else if(r_waiter.isChecked()){
					position = 2;
				}else if(r_chef.isChecked()){
					position = 3;
				}
				String name = e_name.getText().toString();
				String email = e_email.getText().toString();
				String pass = e_pass.getText().toString();
				String phone = e_phone.getText().toString();
				if (!checkNull(name) && !checkNull(email)
						&& !checkNull(pass) && position != 0
						&& !checkNull(phone) && email.contains("@")) {
					e_item.setE_name(name);
					e_item.setPOSITION_p_id(position);
					e_item.setE_email(email);
					
					e_item.setE_pass(pass);
					try{
						e_item.setE_phone_number(Integer.valueOf(phone));
						boolean ok = db.updateUser(e_item);
						if(ok){
							dialogEdit.setTitle("Chỉnh sửa");
							resetGridview();
							Toast.makeText(getApplicationContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
							dialogEdit.dismiss();
						}
						resetGridview();
					}catch(Exception e){
						dialogEdit.setTitle("Chỉnh sửa. Số điện thoại phải là số!");
					}

					
				} else {
					dialogEdit.setTitle("Chỉnh sửa. Chưa điền đầy đủ thông tin hoặc Email sai!");
					Toast.makeText(getApplicationContext(),
							"Hãy chắc chắn đã điền đầy đủ thông tin!",
							Toast.LENGTH_SHORT).show();
				}
				
				
				
				
			}
		});
		
		final ImageView profile = (ImageView)dialogEdit.findViewById(R.id.picture_profile);
		profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseImage();
				Toast.makeText(getApplicationContext(), "Chưa xử lí chọn ảnh", Toast.LENGTH_SHORT).show();
			}
		});
		dialogEdit.show();
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.manager_employee_context_button, menu);
	}

	public void loadTab(){
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		TabSpec spec;
		
		spec = tabHost.newTabSpec("tab1");
		spec.setIndicator("Thanh Toán");
		spec.setContent(R.id.payment);
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("tab2");
		spec.setIndicator("Menu");
		spec.setContent(R.id.menu);
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("tab3");
		spec.setIndicator("Nhân viên");
		spec.setContent(R.id.employee);
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("tab4");
		spec.setIndicator("Thống kê");
		spec.setContent(R.id.statistic);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}
	
	private static final int PICK_IMAGE = 1;
	private static String url_image = "";
	public void chooseImage(){
		
		Intent intent_image= new Intent();
		intent_image.setType("image/*");
		intent_image.setAction(Intent.ACTION_GET_CONTENT);
		Bundle bundle = new Bundle();
		startActivityForResult(Intent.createChooser(intent_image, "Select picture"), PICK_IMAGE);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == PICK_IMAGE && data != null && data.getData() != null) {
			if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	            Cursor cursor = getContentResolver().query(selectedImage,
	                    filePathColumn, null, null, null);
	            cursor.moveToFirst();
	 
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);
	            cursor.close();
	            url_image = picturePath;
			}
	        }
		super.onActivityResult(requestCode, resultCode, data);
	}

	//----------------Hết phần employee----------------------------------------
	
	//-------------phan nay cua menu---------------------
	GridLayout gridMenu;
	Spinner spHideOrDisplay;
	Spinner spinerSort;
	Map<LinearLayout, Food> map_item_food;
	public void menu_view(){
		gridMenu = (GridLayout) findViewById(R.id.gridMenu);
		spHideOrDisplay = (Spinner) findViewById(R.id.spHideOrDisplay);
		spinerSort = (Spinner) findViewById(R.id.spSort);
		map_item_food = new HashMap<LinearLayout, Food>();

		resetGridview();
		loadSpSort();
		loadSpHideOrDisplay();
		pictureAddHandler();
	}
	public void loadSpSort(){
		Spinner spSort = (Spinner) findViewById(R.id.spSort);
		String[] arr = {"Tăng dần", "Giảm dần"};
		ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arr);
		spSort.setAdapter(adapterSort);
		spSort.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				resetGridMenu();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void loadSpHideOrDisplay(){
		String[] arr = {"Tất cả", "Hiện","Ẩn"};
		ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arr);
		
		spHideOrDisplay.setAdapter(adapterSort);
		
		spHideOrDisplay.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				resetGridMenu();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
	}
	
	
	public void pictureAddHandler(){
		ImageView pictutreAdd = (ImageView) findViewById(R.id.picture_add_food);
		String[] statusList = {"Hiện","Ẩn"};
		final ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, statusList);
		pictutreAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(Manager.this);
				dialog.setTitle("Thêm món ăn");
				dialog.setContentView(R.layout.activity_menu_info_action);
				Button doneButton = (Button) dialog.findViewById(R.id.m_action1);
				Button cancelButton = (Button) dialog.findViewById(R.id.m_action2);
				final Spinner spStatus = (Spinner) dialog.findViewById(R.id.m_status_food);
				spStatus.setAdapter(adapterStatus);
				final EditText edNameFood = (EditText) dialog.findViewById(R.id.m_name_food);
				final EditText edPprice = (EditText) dialog.findViewById(R.id.m_price_food);
				
				doneButton.setText("Hoàn thành");
				cancelButton.setText("Hủy");
				
				doneButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String name = edNameFood.getText().toString();
						String price = edPprice.getText().toString();
						if(!checkNull(name) && !checkNull(price)){
							Food food = new Food();
							try{
							int iPrice = Integer.valueOf(price);
							int selectStatus = spStatus.getSelectedItemPosition();
							if(selectStatus == 0){
								food.setM_status(true);
							}else food.setM_status(false);
							food.setM_name(name);
							food.setM_price(iPrice);
							food.setM_image("Anh");
							
							db.createFood(food);
							Toast.makeText(getApplicationContext(),
									"Thêm món ăn thành công!",
									Toast.LENGTH_SHORT).show();
							resetGridMenu();
							}catch(Exception e){
								Toast.makeText(getApplicationContext(),
										"Chưa điền giá món ăn!",
										Toast.LENGTH_SHORT).show();
							}
						}else Toast.makeText(getApplicationContext(),
								"Hãy chắc chắn đã điền đầy đủ thông tin!",
								Toast.LENGTH_SHORT).show();
						
					}
				});
				cancelButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.hide();
					}
				});
				dialog.show();
			}
		});
	}
	
	public void resetGridMenu(){
		gridMenu.removeAllViews();
		loadGridFood(getFoods());
	}
	
	public List<Food> getFoods(){
		int selectHide = spHideOrDisplay.getSelectedItemPosition();
		int selectSort = spinerSort.getSelectedItemPosition();
		if(selectHide == 0){
			if(selectSort == 0)
				return sort(db.getAllFood(), true);
			else return sort(db.getAllFood(), false);
		}else if(selectHide == 1){
			if(selectSort == 0)
				return sort(db.getFoodsByStatus(true), true);
			else return sort(db.getFoodsByStatus(true), false);
		}else {
			if(selectSort == 0)
				return sort(db.getFoodsByStatus(false), true);
			else return sort(db.getFoodsByStatus(false), false);
		}
		
	}
	
	/**
	 * Sort list foods
	 * @param foods
	 * @param type true when A-> Z or false when Z->A
	 * @return
	 */
	public List<Food> sort(List<Food> foods, boolean type){
		int size =  foods.size();
		if(type){
			for(int i = 0; i < size - 1; i++){
				for(int j = i+1; j < size; j++){
					if(foods.get(i).getM_price() > foods.get(j).getM_price()){
						swapFood(foods.get(i), foods.get(j));
					}
				}
			}
		}else{
			for(int i = 0; i < size - 1; i++){
				for(int j = i+1; j < size; j++){
					if(foods.get(i).getM_price() < foods.get(j).getM_price()){
						swapFood(foods.get(i), foods.get(j));
					}
				}
			}
		}
		return foods;
	}
	
	public void swapFood(Food f1, Food f2){
		Food fTmp = new Food();
		fTmp.setM_food_id(f1.getM_food_id());
		fTmp.setM_image(f1.getM_image());
		fTmp.setM_name(f1.getM_name());
		fTmp.setM_option(f1.getM_option());
		fTmp.setM_price(f1.getM_price());
		fTmp.setM_status(f1.getM_status());
		
		f1.setM_food_id(f2.getM_food_id());
		f1.setM_image(f2.getM_image());
		f1.setM_name(f2.getM_name());
		f1.setM_option(f2.getM_option());
		f1.setM_price(f2.getM_price());
		f1.setM_status(f2.getM_status());
		
		f2.setM_food_id(fTmp.getM_food_id());
		f2.setM_image(fTmp.getM_image());
		f2.setM_name(fTmp.getM_name());
		f2.setM_option(fTmp.getM_option());
		f2.setM_price(fTmp.getM_price());
		f2.setM_status(fTmp.getM_status());
	}
	
	public void searchFood(){
		AutoCompleteTextView autoSearch = (AutoCompleteTextView) findViewById(R.id.auto_text_quick_search);
		List<Food> foods = db.getAllFood();
		List<String> list_name_food = new ArrayList<String>();
		int size = foods.size();
		for(int i = 0; i < size; i++){
			list_name_food.add(foods.get(i).getM_name());
		}
		
		ArrayAdapter adapter = new ArrayAdapter
				   (this,android.R.layout.simple_list_item_1,list_name_food);
		autoSearch.setAdapter(adapter);
		
		autoSearch.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void loadGridFood(List<Food> foods){
		int size = foods.size();
		for(int i = 0; i < size; i++){
			LinearLayout itemMenu = new LinearLayout(Manager.this);
			itemMenu.setOrientation(LinearLayout.VERTICAL);
			ImageView profileFood = new ImageView(Manager.this);
			profileFood.setBackgroundResource(R.drawable.add_food);
			
			TextView name = new TextView(Manager.this);
			name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			name.setText(foods.get(i).getM_name());
			
			TextView price = new TextView(Manager.this);
			price.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			price.setText(foods.get(i).getM_price()+" ");
			price.setText(foods.get(i).getM_price()+"");
			
			itemMenu.setWeightSum(3);
			itemMenu.addView(profileFood);
			itemMenu.addView(name);
			itemMenu.addView(price);
			itemMenu.setBackgroundResource(R.drawable.frame_item_in_grid);
			itemMenu.setLayoutParams(new LayoutParams(250, 300));
			itemMenu.setPadding(50, 50, 50, 50);
			itemMenu.setGravity(1);
			gridMenu.addView(itemMenu);
			
			map_item_food.put(itemMenu, foods.get(i));
		}
	}
	
	
	public void option(){
//		ImageView option_pic = (ImageView) findViewById(R.id.option_menu);
//		option_pic.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//			}
//		});
	}
	
	//--------------Hết phần menu----------------------------
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		for(Entry<LinearLayout, Food> entry : map_item_food.entrySet()){
			LinearLayout item = entry.getKey();
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					menu.clear();
					getMenuInflater().inflate(R.menu.option_manager_menu, menu);
				}
			});
			
		}
		return super.onPrepareOptionsMenu(menu);
	}
	// --------------Payment----------------------------------
	//@SuppressWarnings("null")
	public void payment_main() {
		ListView lv;
		Food newfood = new Food();
		newfood.setM_name("Cocacola");
		newfood.setM_price(5000);
		newfood.setM_image("anh");
		newfood.setM_status(true);
		//db.createFood(newfood);
		
		/*
		List<Order> allOrder = db.getOrderList();
		if (allOrder.size() > 0) {
			final bill[] bills = null;
			int i = 0;
			for (Order aOrder : allOrder) {
				String table = "Bàn " + String.valueOf(aOrder.getTableId())
						+ "." + String.valueOf(aOrder.getCount());
				food[] foods = null;
				int j = 0;
				for (FoodTemprary aFood : aOrder.getFoodTemp()) {
					Food tempFood = db.getFood(aFood.getFoodId());
					String nameFood = tempFood.getM_name();
					int numberofFood = aFood.getCount();
					int priceFood = tempFood.getM_price();
					food afood = new food(nameFood, numberofFood, priceFood);
					foods[j] = afood;
					j++;
				}
				bill abill = new bill(table, foods);
				bills[i] = abill;
			} */

			
			  food[] foods = new food[] { new food("Cafe", 2, 20000), new
			 food("Pepsi", 3, 7000), };
			 
			  food[] food1s = new food[] { new food("Cafe", 2, 20000), new
			  food("Sting", 3, 7000), new food("Pepsi", 3, 7000), };
			  
			  final bill[] bills = new bill[] { new bill("Bàn 1.1", foods), new
			  bill("Bàn 2.3", food1s), };
			 
			food[] atbfoods = bills[0].getFoods();

			List<String> bill_arr = new ArrayList<String>();
			for (bill abill : bills) {
				bill_arr.add(abill.getTable());
			}

			lv = (ListView) findViewById(R.id.lvbill);
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, bill_arr);
			lv.setAdapter(arrayAdapter);
			final Activity a = this;
			ListView lvfoods = (ListView) findViewById(R.id.tbfoods);
			ArrayList<food> food_arr = new ArrayList<food>();
			int totalpayment = 0;
			for (food afood : atbfoods) {
				totalpayment = totalpayment + afood.getNumberOf()
						* afood.getPrice();
				food_arr.add(afood);
			}
			ItemPaymentAdapter adapter = null;
			adapter = new ItemPaymentAdapter(this,
					R.layout.payment_item_layout, food_arr);
			lvfoods.setAdapter(adapter);
			TextView tvtpayment = (TextView) findViewById(R.id.tvtotal);
			tvtpayment.setTextSize(20);
			tvtpayment.setText(String.valueOf(totalpayment) + " VNĐ");

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					food[] atbfoods = bills[position].getFoods();
					ListView lvfoods = (ListView) findViewById(R.id.tbfoods);
					ArrayList<food> food_arr = new ArrayList<food>();
					int totalpayment = 0;
					for (food afood : atbfoods) {
						totalpayment = totalpayment + afood.getNumberOf()
								* afood.getPrice();
						food_arr.add(afood);
					}
					ItemPaymentAdapter adapter = null;
					adapter = new ItemPaymentAdapter(a,
							R.layout.payment_item_layout, food_arr);
					lvfoods.setAdapter(adapter);
					TextView tvtpayment = (TextView) findViewById(R.id.tvtotal);
					tvtpayment.setTextSize(20);
					tvtpayment.setText(String.valueOf(totalpayment) + " VNĐ");

				}
			});
		}

	//}

	// -----------------------------------Statistic---------------------------------
	public void main_statistic() {
		set_spin_type();
		set_spin_year();
		set_spin_month();
		set_lv_foodstt();
		Spinner spSort = (Spinner) findViewById(R.id.sptype);
		spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				change_type(pos);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}

	public void set_spin_type() {
		Spinner spSort = (Spinner) findViewById(R.id.sptype);
		String[] arr = { "Theo Quý", "Theo Tháng" };
		ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arr);
		spSort.setAdapter(adapterSort);
	}

	public void change_type(int type) {
		TextView tvmonths = (TextView) findViewById(R.id.tvmonth);
		if (type == 0) {
			tvmonths.setText("Quý: ");
			set_spin_month();
		}
		if (type == 1) {
			tvmonths.setText("Tháng : ");
			set_spin_month2();
		}
	}

	public void set_spin_year() {
		Spinner spSort = (Spinner) findViewById(R.id.spyear);
		String[] arr = { "2014", "2013" };
		ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arr);
		spSort.setAdapter(adapterSort);
	}

	public void set_spin_month() {
		Spinner spSort = (Spinner) findViewById(R.id.spmonth);
		String[] arr = { "1", "2", "3", "4" };
		ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arr);
		spSort.setAdapter(adapterSort);
	}

	public void set_spin_month2() {
		Spinner spSort = (Spinner) findViewById(R.id.spmonth);
		String[] arr = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
				"11", "12" };
		ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arr);
		spSort.setAdapter(adapterSort);
	}

	public void set_lv_foodstt() {
		food[] foods = new food[] { new food("Cafe", 2, 20000),
				new food("Pepsi", 3, 7000), new food("Coca", 3, 7000), };
		ArrayList<food> food_arr = new ArrayList<food>();
		int totalpayment = 0;
		for (food afood : foods) {
			totalpayment = totalpayment + afood.getNumberOf()
					* afood.getPrice();
			food_arr.add(afood);
		}
		ItemStatisticAdapter adapter = null;
		adapter = new ItemStatisticAdapter(this,
				R.layout.statistic_item_layout, food_arr);
		ListView lvfoodstt = (ListView) findViewById(R.id.lvfoodstt);
		lvfoodstt.setAdapter(adapter);
		TextView tvtpayment = (TextView) findViewById(R.id.tvbtotal);
		tvtpayment.setTextSize(20);
		tvtpayment.setText(String.valueOf(totalpayment) + " VNĐ");
	}

}
