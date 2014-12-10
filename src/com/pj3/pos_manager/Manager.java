package com.pj3.pos_manager;

//general dependencies
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.pj3.pos_manager.router.BillRouter;
import com.pj3.pos_manager.router.FoodStatusRouter;
import com.pj3.pos_manager.router.LoginRouter;
import com.pj3.pos_manager.router.MenuRouter;
import com.pj3.pos_manager.router.OrderRouter;
//import com.pj3.pos_manager.router.RESTResource;
import com.pj3.pos_manager.router.UserRouter;
//local dependencies
import com.pj3.pos_manager.MainActivity;
import com.pj3.pos_manager.R;
import com.pj3.pos_manager.database.DatabaseSource;
import com.pj3.pos_manager.res_obj.Bill;
import com.pj3.pos_manager.res_obj.Employee;
import com.pj3.pos_manager.res_obj.Food;
import com.pj3.pos_manager.res_obj.FoodStatistic;
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
import android.view.LayoutInflater;
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
import android.widget.ScrollView;
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

public class Manager extends Activity {
	TabHost tabHost;
	List<Employee> employees;
	Spinner spPosition;
	GridLayout gridEmployees;
	PopupWindow popupWindow;
	Map<LinearLayout, Employee> itemEmployee;
	
	public static String imagePath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
//		db = new DatabaseSource(this);

		Component serverComponent = new Component();
		serverComponent.getServers().add(Protocol.HTTP, 8182);
		final Router router = new Router(serverComponent.getContext()
				.createChildContext());

		// test router
		// RESTResource r = new RESTResource();
		// router.attach("/",r.getClass());

		router.attach("/api/users", UserRouter.class);
		router.attach("/api/menus", MenuRouter.class);
		router.attach("/api/bills", BillRouter.class);
		router.attach("/api/orders", OrderRouter.class);
		router.attach("/api/foodstatus", FoodStatusRouter.class);
		 router.attach("/login",LoginRouter.class);
		VirtualHost server = serverComponent.getDefaultHost();
		server.attach(router);

		try {
			serverComponent.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		main_employee();
		menu_view();
		payment_main();
		main_statistic();
	}

	// Phan nay cua
	// Employee------------------------------------------------------
	public void main_employee() {
		employees = new ArrayList<Employee>();
		itemEmployee = new HashMap<LinearLayout, Employee>();
		gridEmployees = (GridLayout) findViewById(R.id.gridEmployee);
		spPosition = (Spinner) findViewById(R.id.spPosition);
		addEmployee();
		loadTab();
		loadSpPosition();
		loadGridEmployees(loadEmploy());
		handlerSpinerPos();
		initTable_Position();
	}

	public void initTable_Position() {
		List<Position> pList = POS_M.db.getPositions();
		if (pList.size() != 0) {
			Position position = new Position();
			position.setP_id(1);
			position.setP_name("Quản lí");
			position.setP_salary(5000000);

			POS_M.db.createPosition(position);
			position.setP_id(2);
			position.setP_name("Bồi bàn");
			position.setP_salary(3000000);

			POS_M.db.createPosition(position);
			position.setP_id(3);
			position.setP_name("Đầu bếp");
			position.setP_salary(3500000);

			POS_M.db.createPosition(position);
		}
	}

	public List<Employee> loadEmploy() {
		List<Employee> employeess = new ArrayList<Employee>();
		int select = spPosition.getSelectedItemPosition();
		if (select == 0) {
			employeess = POS_M.db.getAllUsers();
		} else if (select == 1) {
			employeess = POS_M.db.getUserByPosition(1);
		} else if (select == 2) {
			employeess = POS_M.db.getUserByPosition(2);
		} else if (select == 3) {
			employeess = POS_M.db.getUserByPosition(3);
		}
		return employeess;
	}

	public void loadSpPosition() {
		// List<Position> positions = db.getPositions();
		List<String> sList = new ArrayList<String>();
		sList.add("Tất cả");
		sList.add("Quản lí");
		sList.add("Bồi bàn");
		sList.add("Đầu bếp");
		// int size = positions.size();
		// for(int i = 0; i < size; i++){
		// sList.add(positions.get(i).getP_name());
		// }

		ArrayAdapter<String> adapterPostion = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, sList);

		spPosition.setAdapter(adapterPostion);

	}

	@SuppressLint("NewApi")
	public void handlerSpinerPos() {
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

	public void addEmployee() {
		ImageView addPicture = (ImageView) findViewById(R.id.addUserAction);
		addPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				active_Add_Employee();
			}
		});
	}

	public void active_Add_Employee() {
		final Dialog dialogAdd = new Dialog(Manager.this);
		dialogAdd.setContentView(R.layout.e_info_action);
		dialogAdd.setTitle("Thêm nhân viên");
		Button cancelButton = (Button) dialogAdd
				.findViewById(R.id.m_e_cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogAdd.dismiss();
			}
		});
		Button doneButton = (Button) dialogAdd
				.findViewById(R.id.m_e_done_button);
		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText e_name = (EditText) dialogAdd
						.findViewById(R.id.e_name_tb);
				RadioGroup radio_position = (RadioGroup) dialogAdd
						.findViewById(R.id.radio_position);
				RadioButton r_manager = (RadioButton) dialogAdd
						.findViewById(R.id.radio_manager);
				RadioButton r_waiter = (RadioButton) dialogAdd
						.findViewById(R.id.radio_waiter);
				RadioButton r_chef = (RadioButton) dialogAdd
						.findViewById(R.id.radio_chef);
				int position = 0;
				if (r_manager.isChecked()) {
					position = 1;
				} else if (r_waiter.isChecked()) {
					position = 2;
				} else if (r_chef.isChecked()) {
					position = 3;
				}

				EditText e_email = (EditText) dialogAdd
						.findViewById(R.id.e_email_tb);
				EditText e_phone = (EditText) dialogAdd
						.findViewById(R.id.e_phoneNumber_tb);
				EditText e_pass = (EditText) dialogAdd
						.findViewById(R.id.e_pass_tb);
				String name = e_name.getText().toString();
				String email = e_email.getText().toString();
				String pass = e_pass.getText().toString();
				String phone = e_phone.getText().toString();
				if (!checkNull(name) && !checkNull(email) && !checkNull(pass)
						&& position != 0 && !checkNull(phone)
						&& email.contains("@") && !checkNull(imagePath)) {
					try {
						Employee e = new Employee(name, email, pass, imagePath,
								Integer.valueOf(phone), position);
						POS_M.db.createUser(e);
						resetGridview();
						dialogAdd.setTitle("Thêm nhân viên");
						Toast.makeText(getApplicationContext(),
								"Thêm nhân viên thành công!",
								Toast.LENGTH_SHORT).show();
						dialogAdd.dismiss();
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"Số điện thoại phải là số!", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					dialogAdd
							.setTitle("Thêm nhân viên. Chưa điền đầy đủ thông tin");
					Toast.makeText(getApplicationContext(),
							"Hãy chắc chắn đã điền đầy đủ thông tin!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		ImageView profile = (ImageView) dialogAdd
				.findViewById(R.id.picture_profile);
		profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				chooseImage();
				Toast.makeText(getApplicationContext(), "Chưa xử lí chọn ảnh",
						Toast.LENGTH_SHORT).show();
			}
		});

		dialogAdd.show();

	}

	public boolean checkNull(String input) {
		return input.equals("");
	}

	@SuppressLint("NewApi")
	public void resetGridview() {
		gridEmployees.removeAllViews();
		loadGridEmployees(loadEmploy());
	}

	@SuppressLint("NewApi")
	public void loadGridEmployees(final List<Employee> employees) {
		int numberEmployee_current = employees.size();
		for (int i = 0; i < numberEmployee_current; i++) {
			LinearLayout item = new LinearLayout(Manager.this);
			item.setOrientation(LinearLayout.VERTICAL);
			item.setBackgroundResource(R.drawable.frame_item_in_grid);
			item.setLayoutParams(new LayoutParams(300, 350));
			item.setPadding(10, 10, 10, 10);
			item.setGravity(1);

			item.setWeightSum(3);
			final int k = i;
			itemEmployee.put(item, employees.get(i));
			item.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					final Employee e_item = employees.get(k);
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							Manager.this);
					alertDialog.setIcon(R.drawable.ic_launcher);
					alertDialog.setTitle("Quản lí nhân viên: "
							+ e_item.getE_name());

					alertDialog.setPositiveButton("Xem",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									viewEmployee(e_item);
								}
							});

					// Setting Negative "Edit" Button
					alertDialog.setNegativeButton("Chỉnh sửa",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									editEmployee(e_item);
								}
							});

					// Setting Netural "delete" Button
					alertDialog.setNeutralButton("Xóa",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									AlertDialog dialogConfirm = new AlertDialog.Builder(
											Manager.this)
											.setMessage(
													"Bạn chắc chắn muốn xóa "
															+ e_item.getE_name()
															+ " ra khỏi danh sách?")
											.setIcon(R.drawable.ic_launcher)
											.setTitle("Xác nhận")
											.setPositiveButton(
													"Có",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															boolean ok = POS_M.db
																	.deleteUser(e_item
																			.getE_id());
															if (ok) {
																resetGridview();
																Toast.makeText(
																		getApplicationContext(),
																		"Xóa thành công!",
																		Toast.LENGTH_SHORT)
																		.show();
															} else
																Toast.makeText(
																		getApplicationContext(),
																		"Xóa thất bại!",
																		Toast.LENGTH_SHORT)
																		.show();
														}
													})
											.setNegativeButton(
													"Hủy",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
														}
													}).create();
									dialogConfirm.show();
								}
							});

					// Showing Alert Message
					alertDialog.show();
					return false;
				}
			});

			ImageView image = new ImageView(Manager.this);
			image.setImageBitmap(BitmapFactory.decodeFile(employees.get(i).getE_image()));
			image.setLayoutParams(new LayoutParams(250, 250));

			TextView name = new TextView(Manager.this);
			name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			name.setText(employees.get(i).getE_name());

			TextView phoneNumber = new TextView(Manager.this);
			phoneNumber.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			phoneNumber.setText("" + employees.get(i).getE_phone_number());

			item.addView(image);
			item.addView(name);
			item.addView(phoneNumber);

			gridEmployees.addView(item);
		}

	}

	public void viewEmployee(final Employee e_item) {

		final Dialog dialogView = new Dialog(Manager.this);
		dialogView.setContentView(R.layout.e_info_action);
		dialogView.setTitle("Xem nhân viên");

		EditText e_name = (EditText) dialogView.findViewById(R.id.e_name_tb);
		e_name.setText(e_item.getE_name());
		e_name.setFocusable(false);

		RadioGroup radio_position = (RadioGroup) dialogView
				.findViewById(R.id.radio_position);
		RadioButton r_manager = (RadioButton) dialogView
				.findViewById(R.id.radio_manager);
		RadioButton r_waiter = (RadioButton) dialogView
				.findViewById(R.id.radio_waiter);
		RadioButton r_chef = (RadioButton) dialogView
				.findViewById(R.id.radio_chef);
		if (e_item.getPOSITION_p_id() == 1) {
			radio_position.check(r_manager.getId());
		} else if (e_item.getPOSITION_p_id() == 2) {
			radio_position.check(r_waiter.getId());
		} else if (e_item.getPOSITION_p_id() == 3) {
			radio_position.check(r_chef.getId());
		}

		r_manager.setEnabled(false);
		r_waiter.setEnabled(false);
		r_chef.setEnabled(false);

		EditText e_email = (EditText) dialogView.findViewById(R.id.e_email_tb);
		e_email.setText(e_item.getE_email());
		e_email.setFocusable(false);

		EditText e_phone = (EditText) dialogView
				.findViewById(R.id.e_phoneNumber_tb);
		e_phone.setText("" + e_item.getE_phone_number());
		e_phone.setFocusable(false);

		EditText e_pass = (EditText) dialogView.findViewById(R.id.e_pass_tb);
		e_pass.setText("" + e_item.getE_pass());
		e_pass.setFocusable(false);

		Button cancelButton = (Button) dialogView
				.findViewById(R.id.m_e_cancel_button);
		cancelButton.setText("Xong");
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogView.dismiss();
			}
		});
		Button doneButton = (Button) dialogView
				.findViewById(R.id.m_e_done_button);
		doneButton.setText("Chỉnh sửa");
		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogView.dismiss();
				editEmployee(e_item);
			}
		});

		ImageView profile = (ImageView) dialogView
				.findViewById(R.id.picture_profile);
		profile.setImageBitmap(BitmapFactory.decodeFile(e_item.getE_image()));
		dialogView.show();
	}

	public void editEmployee(final Employee e_item) {

		final Dialog dialogEdit = new Dialog(Manager.this);
		dialogEdit.setContentView(R.layout.e_info_action);
		dialogEdit.setTitle("Chỉnh sửa");

		final EditText e_name = (EditText) dialogEdit
				.findViewById(R.id.e_name_tb);
		e_name.setText(e_item.getE_name());

		final RadioGroup radio_position = (RadioGroup) dialogEdit
				.findViewById(R.id.radio_position);
		final RadioButton r_manager = (RadioButton) dialogEdit
				.findViewById(R.id.radio_manager);
		final RadioButton r_waiter = (RadioButton) dialogEdit
				.findViewById(R.id.radio_waiter);
		final RadioButton r_chef = (RadioButton) dialogEdit
				.findViewById(R.id.radio_chef);
		if (e_item.getPOSITION_p_id() == 1) {
			radio_position.check(r_manager.getId());
		} else if (e_item.getPOSITION_p_id() == 2) {
			radio_position.check(r_waiter.getId());
		} else if (e_item.getPOSITION_p_id() == 3) {
			radio_position.check(r_chef.getId());
		}

		final EditText e_email = (EditText) dialogEdit
				.findViewById(R.id.e_email_tb);
		e_email.setText(e_item.getE_email());

		final EditText e_phone = (EditText) dialogEdit
				.findViewById(R.id.e_phoneNumber_tb);
		e_phone.setText("" + e_item.getE_phone_number());

		final EditText e_pass = (EditText) dialogEdit
				.findViewById(R.id.e_pass_tb);
		e_pass.setText("" + e_item.getE_pass());

		Button cancelButton = (Button) dialogEdit
				.findViewById(R.id.m_e_cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogEdit.dismiss();
			}
		});
		Button doneButton = (Button) dialogEdit
				.findViewById(R.id.m_e_done_button);
		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = 1;
				if (r_manager.isChecked()) {
					position = 1;
				} else if (r_waiter.isChecked()) {
					position = 2;
				} else if (r_chef.isChecked()) {
					position = 3;
				}
				String name = e_name.getText().toString();
				String email = e_email.getText().toString();
				String pass = e_pass.getText().toString();
				String phone = e_phone.getText().toString();
				if (!checkNull(name) && !checkNull(email) && !checkNull(pass)
						&& position != 0 && !checkNull(phone)
						&& email.contains("@")) {
					e_item.setE_name(name);
					e_item.setPOSITION_p_id(position);
					e_item.setE_email(email);

					e_item.setE_pass(pass);
					if(!imagePath.equals("")){
						e_item.setE_image(imagePath);
					}else{
						
					}
					
					try {
						e_item.setE_phone_number(Integer.valueOf(phone));
						boolean ok = POS_M.db.updateUser(e_item);
						if (ok) {
							dialogEdit.setTitle("Chỉnh sửa");
							resetGridview();
							Toast.makeText(getApplicationContext(),
									"Cập nhật thành công!", Toast.LENGTH_SHORT)
									.show();
							dialogEdit.dismiss();
						}
						resetGridview();
					} catch (Exception e) {
						dialogEdit
								.setTitle("Chỉnh sửa. Số điện thoại phải là số!");
					}

				} else {
					dialogEdit
							.setTitle("Chỉnh sửa. Chưa điền đầy đủ thông tin hoặc Email sai!");
					Toast.makeText(getApplicationContext(),
							"Hãy chắc chắn đã điền đầy đủ thông tin!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		final ImageView profile = (ImageView) dialogEdit
				.findViewById(R.id.picture_profile);
		profile.setImageBitmap(BitmapFactory.decodeFile(e_item.getE_image()));
		profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				chooseImage();
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

	public void loadTab() {
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

	

	// ----------------Hết phần employee----------------------------------------
	
	//-----------------Handler pick image -----------------------------
	public void chooseImage() {
		imagePath = "";
		Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         
        startActivityForResult(i, PICK_IMAGE);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            imagePath = picturePath;
            Toast.makeText(getApplicationContext(),
					"Ảnh đã được chọn: "+ imagePath,
					Toast.LENGTH_SHORT).show();
            cursor.close();
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	//-----------------End handler pick image -------------------------

	// -------------phan nay cua menu---------------------
	GridLayout gridMenu;
	Spinner spHideOrDisplay;
	Spinner spinerSort;

	public void menu_view() {
		gridMenu = (GridLayout) findViewById(R.id.gridMenu);
		spHideOrDisplay = (Spinner) findViewById(R.id.spHideOrDisplay);
		spinerSort = (Spinner) findViewById(R.id.spSort);

		resetGridview();
		loadSpSort();
		loadSpHideOrDisplay();
		pictureAddHandler();
	}

	public void loadSpSort() {
		Spinner spSort = (Spinner) findViewById(R.id.spSort);
		String[] arr = { "Tăng dần", "Giảm dần" };
		ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arr);
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

	public void loadSpHideOrDisplay() {
		String[] arr = { "Tất cả", "Hiện", "Ẩn" };
		ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arr);

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

	public void pictureAddHandler() {
		imagePath = "";
		ImageView pictutreAdd = (ImageView) findViewById(R.id.picture_add_food);
		String[] statusList = { "Hiện", "Ẩn" };
		final ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, statusList);
		pictutreAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(Manager.this);
				dialog.setTitle("Thêm món ăn");
				dialog.setContentView(R.layout.activity_menu_info_action);
				Button doneButton = (Button) dialog
						.findViewById(R.id.m_action1);
				Button cancelButton = (Button) dialog
						.findViewById(R.id.m_action2);
				final Spinner spStatus = (Spinner) dialog
						.findViewById(R.id.m_status_food);
				spStatus.setAdapter(adapterStatus);
				final EditText edNameFood = (EditText) dialog
						.findViewById(R.id.m_name_food);
				final EditText edPprice = (EditText) dialog
						.findViewById(R.id.m_price_food);
				final EditText edOption = (EditText) dialog
						.findViewById(R.id.m_option_food);

				doneButton.setText("Hoàn thành");
				cancelButton.setText("Hủy");

				doneButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String name = edNameFood.getText().toString();
						String price = edPprice.getText().toString();
						String option = edOption.getText().toString();
						if (!checkNull(name) && !checkNull(price) && !checkNull(imagePath)) {
							Food food = new Food();
							try {
								int iPrice = Integer.valueOf(price);
								int selectStatus = spStatus
										.getSelectedItemPosition();
								if (selectStatus == 0) {
									food.setM_status(true);
								} else
									food.setM_status(false);
								food.setM_name(name);
								food.setM_price(iPrice);
								food.setM_image("Anh");
								food.setM_option(option);
								if(!imagePath.equals(""))
									food.setM_image(imagePath);

								POS_M.db.createFood(food);
								Toast.makeText(getApplicationContext(),
										"Thêm món ăn thành công!",
										Toast.LENGTH_SHORT).show();
								dialog.dismiss();
								resetGridMenu();
							} catch (Exception e) {
								Toast.makeText(getApplicationContext(),
										"Chưa điền giá món ăn!",
										Toast.LENGTH_SHORT).show();
							}
						} else
							Toast.makeText(getApplicationContext(),
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
				
				ImageView imageFood = (ImageView) dialog.findViewById(R.id.m_menu_dialog_pictureFood);
				imageFood.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						chooseImage();
					}
				});
				
				dialog.show();
			}
		});
	}

	public void resetGridMenu() {
		gridMenu.removeAllViews();
		loadGridFood(getFoods());
	}

	public List<Food> getFoods() {
		int selectHide = spHideOrDisplay.getSelectedItemPosition();
		int selectSort = spinerSort.getSelectedItemPosition();
		if (selectHide == 0) {
			if (selectSort == 0)
				return sort(POS_M.db.getAllFood(), true);
			else
				return sort(POS_M.db.getAllFood(), false);
		} else if (selectHide == 1) {
			if (selectSort == 0)
				return sort(POS_M.db.getFoodsByStatus(true), true);
			else
				return sort(POS_M.db.getFoodsByStatus(true), false);
		} else {
			if (selectSort == 0)
				return sort(POS_M.db.getFoodsByStatus(false), true);
			else
				return sort(POS_M.db.getFoodsByStatus(false), false);
		}

	}

	/**
	 * Sort list foods
	 * 
	 * @param foods
	 * @param type
	 *            true when A-> Z or false when Z->A
	 * @return
	 */
	public List<Food> sort(List<Food> foods, boolean type) {
		int size = foods.size();
		if (type) {
			for (int i = 0; i < size - 1; i++) {
				for (int j = i + 1; j < size; j++) {
					if (foods.get(i).getM_price() > foods.get(j).getM_price()) {
						swapFood(foods.get(i), foods.get(j));
					}
				}
			}
		} else {
			for (int i = 0; i < size - 1; i++) {
				for (int j = i + 1; j < size; j++) {
					if (foods.get(i).getM_price() < foods.get(j).getM_price()) {
						swapFood(foods.get(i), foods.get(j));
					}
				}
			}
		}
		return foods;
	}

	public void swapFood(Food f1, Food f2) {
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


	public void loadGridFood(List<Food> foods) {
		int size = foods.size();
		for (int i = 0; i < size; i++) {
			Food food_i = foods.get(i);
			LinearLayout itemMenu = new LinearLayout(Manager.this);
			itemMenu.setOrientation(LinearLayout.VERTICAL);
			ImageView profileFood = new ImageView(Manager.this);
			try{
				profileFood.setImageBitmap(BitmapFactory.decodeFile(food_i.getM_image()));
			}catch(Exception e){
				profileFood.setBackgroundResource(R.drawable.man_brown);
			}
			profileFood.setLayoutParams(new LayoutParams(250, 200));

			TextView name = new TextView(Manager.this);
			name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			name.setText(food_i.getM_name());

			TextView price = new TextView(Manager.this);
			price.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			price.setText(food_i.getM_price() + "");

			itemMenu.setWeightSum(3);
			itemMenu.addView(profileFood);
			itemMenu.addView(name);
			itemMenu.addView(price);
			itemMenu.setBackgroundResource(R.drawable.frame_item_in_grid);
			itemMenu.setLayoutParams(new LayoutParams(250, 300));
			itemMenu.setPadding(10, 10,10, 10);
			itemMenu.setGravity(1);

			final Food food = foods.get(i);
			String[] statusList = { "Hiện", "Ẩn" };
			final ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(
					this, android.R.layout.simple_list_item_1, statusList);

			itemMenu.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					AlertDialog.Builder dialogMenu = new AlertDialog.Builder(
							Manager.this);
					dialogMenu.setIcon(R.drawable.ic_launcher);
					dialogMenu.setTitle("" + food.getM_name());
					LayoutInflater inflater = getLayoutInflater();

					// final View dialogView = inflater.inflate(R.layout., root)
					dialogMenu.setPositiveButton("Chỉnh sửa",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									final Dialog editDialog = new Dialog(
											Manager.this);
									editDialog
											.setContentView(R.layout.activity_menu_info_action);
									Button doneButton = (Button) editDialog
											.findViewById(R.id.m_action1);
									Button cancel = (Button) editDialog
											.findViewById(R.id.m_action2);
									doneButton.setText("Hoàn tất");
									cancel.setText("Hủy");

									final Spinner spStatus = (Spinner) editDialog
											.findViewById(R.id.m_status_food);
									final EditText edNameFood = (EditText) editDialog
											.findViewById(R.id.m_name_food);
									final EditText edPprice = (EditText) editDialog
											.findViewById(R.id.m_price_food);
									final EditText edNote = (EditText) editDialog
											.findViewById(R.id.m_option_food);
									final ImageView pictureFood = (ImageView)editDialog.findViewById(R.id.m_menu_dialog_pictureFood);
									
									spStatus.setAdapter(adapterStatus);

									boolean status = food.getM_status();
									if (status) {
										spStatus.setSelection(0);
									} else
										spStatus.setSelection(1);

									edNameFood.setText(food.getM_name());
									edPprice.setText("" + food.getM_price());
									edNote.setText(food.getM_option());
									pictureFood.setImageBitmap(BitmapFactory.decodeFile(food.getM_image()));

									cancel.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											editDialog.dismiss();
										}
									});

									doneButton
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													String name = edNameFood
															.getText()
															.toString();
													String price = edPprice
															.getText()
															.toString();
													String option = edNote
															.getText()
															.toString();
													if (!checkNull(name)
															&& !checkNull(price)) {
														try {
															int iPrice = Integer
																	.valueOf(price);
															int selectStatus = spStatus
																	.getSelectedItemPosition();
															if (selectStatus == 0) {
																food.setM_status(true);
															} else
																food.setM_status(false);
															food.setM_name(name);
															food.setM_price(iPrice);
															food.setM_image(imagePath);
															food.setM_option(option);

															POS_M.db.updateMenu(food);
															Toast.makeText(
																	getApplicationContext(),
																	"Cập nhật món ăn thành công!",
																	Toast.LENGTH_SHORT)
																	.show();
															editDialog
																	.dismiss();
															resetGridMenu();
														} catch (Exception e) {
															Toast.makeText(
																	getApplicationContext(),
																	"Chưa điền giá món ăn!",
																	Toast.LENGTH_SHORT)
																	.show();
														}
													} else
														Toast.makeText(
																getApplicationContext(),
																"Hãy chắc chắn đã điền đầy đủ thông tin!",
																Toast.LENGTH_SHORT)
																.show();
												}
											});
									pictureFood.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											chooseImage();
										}
									});
									editDialog.show();
								}
							});

					dialogMenu.setNegativeButton("Ẩn/Hiện",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									boolean status = food.getM_status();
									String flag = "";
									if (status)
										flag += "ẩn";
									else
										flag += "hiện";

									POS_M.db.changeStatusFood(food.getM_food_id(),
											!status);
									Toast.makeText(
											getApplicationContext(),
											"Đã " + flag + " "
													+ food.getM_name(),
											Toast.LENGTH_SHORT).show();
									resetGridMenu();
								}
							});
					dialogMenu.show();
					return false;
				}
			});

			gridMenu.addView(itemMenu);

		}
	}

	public void option() {
		// ImageView option_pic = (ImageView) findViewById(R.id.option_menu);
		// option_pic.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// }
		// });
	}

	// --------------Hết phần menu----------------------------

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager, menu);
		return true;
	}

	// --------------Payment----------------------------------
	// @SuppressWarnings("null")
	public void payment_main() {
		Button btrf = (Button) findViewById(R.id.button2);
		btrf.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				payment_main();
			}
		});
		ListView lv;
//		try {
//			Food newfood = new Food();
//			newfood.setM_name("Cocacalo");
//			newfood.setM_price(7000);
//			newfood.setM_image("anh");
//			newfood.setM_status(true);
//			db.createFood(newfood);
//
//			Food newfood1 = new Food();
//			newfood1.setM_name("Sting");
//			newfood1.setM_price(8000);
//			newfood1.setM_image("anh");
//			newfood1.setM_status(true);
//			db.createFood(newfood1);
//		} catch (Exception e) {
//		}
//		Random rand = new Random();
//		int randomNum1 = rand.nextInt(10) + 1;
//		int randomNum2 = rand.nextInt(10) + 1;
//		FoodTemprary newtempfood = new FoodTemprary();
//		newtempfood.setFoodId(1);
//		newtempfood.setCount(10);
//		List<FoodTemprary> newlisttempfood = new ArrayList<FoodTemprary>();
//		newlisttempfood.add(newtempfood);
//		Order neworder = new Order();
//		neworder.setTableId(randomNum1);
//		neworder.setCount(randomNum2);
//		neworder.setFoodTemp(newlisttempfood);
//		db.createBillTemp(neworder);
		//
		// FoodTemprary newtempfood1 = new FoodTemprary();
		// newtempfood1.setFoodId(2);
		// newtempfood1.setCount(10);
		// List<FoodTemprary> newlisttempfood1 = new ArrayList<FoodTemprary>();
		// newlisttempfood1.add(newtempfood1);
		// Order neworder1 = new Order();
		// neworder1.setTableId(2);
		// neworder1.setCount(1);
		// neworder1.setFoodTemp(newlisttempfood1);
		// db.createBillTemp(neworder1);

		final List<Order> allOrder = POS_M.db.getOrderList();
		final List<bill> bills = new ArrayList<bill>();
		if (allOrder != null) {
			for (Order aOrder : allOrder) {
				String table = "Bàn " + String.valueOf(aOrder.getTableId())
						+ "." + String.valueOf(aOrder.getCount());
				List<food> food_arr = new ArrayList<food>();
				if (aOrder.getFoodTemp() != null) {
					for (FoodTemprary aFood : aOrder.getFoodTemp()) {
						Food tempFood = POS_M.db.getFood(aFood.getFoodId());
						String nameFood = tempFood.getM_name();
						int numberofFood = aFood.getCount();
						int priceFood = tempFood.getM_price();
						food afood = new food(nameFood, numberofFood, priceFood);
						food_arr.add(afood);
					}
					bill abill = new bill(table, food_arr);
					bills.add(abill);
				}
			}

			// final List<bill> bills = new ArrayList<bill>();
			// List<food> food_arr1 = new ArrayList<food>();
			// food food1 = new food("Cafe", 2, 20000);
			// food_arr1.add(food1);
			// food food2 = new food("Pepsi", 3, 7000);
			// food_arr1.add(food2);
			// bill bill1 = new bill("Bàn 1.1", food_arr1);
			// bills.add(bill1);
			// List<food> food_arr2 = new ArrayList<food>();
			// food food3 = new food("Lemonate", 3, 40000);
			// food_arr2.add(food3);
			// food food4 = new food("Sting", 5, 8000);
			// food_arr2.add(food4);
			// food food5 = new food("Orange Juice", 1, 30000);
			// food_arr2.add(food5);
			// bill bill2 = new bill("Bàn 2.2", food_arr2);
			// bills.add(bill2);

			final List<String> bill_arr = new ArrayList<String>();
			for (bill abill : bills) {
				bill_arr.add(abill.getTable());
			}
			final Activity a = this;
			TextView tvtpayment = (TextView) findViewById(R.id.tvtotal);
			tvtpayment.setTextSize(20);
			tvtpayment.setText(" 0 VNĐ");
			TextView tv1 = (TextView) findViewById(R.id.textView1);
			tv1.setText(" ");
			ListView lvfoods = (ListView) findViewById(R.id.tbfoods);
			ItemPaymentAdapter adapter = null;
			ArrayList<food> food_arr = new ArrayList<food>();
			adapter = new ItemPaymentAdapter(a, R.layout.payment_item_layout,
					food_arr);
			lvfoods.setAdapter(adapter);
			lv = (ListView) findViewById(R.id.lvbill);
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, bill_arr);
			lv.setAdapter(arrayAdapter);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView tv1 = (TextView) findViewById(R.id.textView1);
					tv1.setText(bill_arr.get(position));
					final int billtemopos = position;
					ListView lvfoods = (ListView) findViewById(R.id.tbfoods);
					ArrayList<food> food_arr = new ArrayList<food>();
					int totalpayment = 0;
					for (food afood : bills.get(position).getFoods()) {
						totalpayment = totalpayment + afood.getNumberOf()
								* afood.getPrice();
						food_arr.add(afood);
					}
					final int ttpm = totalpayment;
					ItemPaymentAdapter adapter = null;
					adapter = new ItemPaymentAdapter(a,
							R.layout.payment_item_layout, food_arr);
					lvfoods.setAdapter(adapter);
					TextView tvtpayment = (TextView) findViewById(R.id.tvtotal);
					tvtpayment.setTextSize(20);
					tvtpayment.setText(String.valueOf(totalpayment) + " VNĐ");
					Button btn_payment = (Button) findViewById(R.id.button1);
					btn_payment.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Bill newBill = new Bill();
							newBill.setB_count(ttpm);
							int billid = POS_M.db.createBill(newBill);
							List<FoodTemprary> newlistFoodTempraries = allOrder
									.get(billtemopos).getFoodTemp();
							for (FoodTemprary aFood : newlistFoodTempraries) {
								FoodStatistic newFoodStatistic = new FoodStatistic();
								newFoodStatistic.setF_b_id(billid);
								Food tempfood = POS_M.db.getFood(aFood.getFoodId());

								newFoodStatistic.setF_count(aFood.getCount()
										* tempfood.getM_price());
								newFoodStatistic.setFpu(tempfood.getM_price());
								newFoodStatistic.setF_m_id(aFood.getFoodId());
								int c = POS_M.db
										.createFoodStatistic(newFoodStatistic);
							}
							POS_M.db.deleteBillTemp(allOrder.get(billtemopos)
									.getOrderId());
							payment_main();
						}
					});

				}
			});
		}

	}

	// -----------------------------------Statistic---------------------------------
	public void main_statistic() {
		set_spin_type();
		set_spin_year();
		set_spin_month();
		Spinner spSort = (Spinner) findViewById(R.id.sptype);
		spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				change_type(pos);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		Button bt_sttt = (Button) findViewById(R.id.btsttic);
		bt_sttt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					List<food> food_arr = new ArrayList<food>();
					for (Food aFood : POS_M.db.getAllFood()) {
						int count = 0;
						List<FoodStatistic> alistFoodStatistics = POS_M.db
								.getStatisticByFoodId(aFood.getM_food_id());
						if (alistFoodStatistics != null) {
							for (FoodStatistic aFoodStatistic : alistFoodStatistics) {
								Bill abill = POS_M.db.getBill(aFoodStatistic
										.getF_b_id());
								java.util.Date adate = abill.getB_time_stamp();
								int mini = adate.compareTo(get_minium_day());
								int maxi = adate.compareTo(get_maxium_day());
								if (mini >= 0 && maxi < 0) {
									count = count + aFoodStatistic.getF_count()
											/ aFoodStatistic.getFpu();
								}
							}
						}

						String name_food = aFood.getM_name();
						int price_food = aFood.getM_price();
						food newfood = new food(name_food, count, price_food);
						food_arr.add(newfood);

					}
					set_lv_foodstt(food_arr);
				} catch (Exception e) {
//					Bill abill = db.getBill(1);
//					int a = abill.getB_time_stamp().compareTo(get_maxium_day());
//					Toast.makeText(getApplicationContext(), "Đếu được",
//							Toast.LENGTH_SHORT).show();
				}

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
		String[] arr = { "2014", "2015", "2016", "2017" };
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

	public void set_lv_foodstt(List<food> foods) {
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

	public Date get_minium_day() {
		Spinner sptype = (Spinner) findViewById(R.id.sptype);
		int type = sptype.getSelectedItemPosition();
		Spinner spmonth = (Spinner) findViewById(R.id.spmonth);
		int month = spmonth.getSelectedItemPosition();
		Spinner spyear = (Spinner) findViewById(R.id.spyear);
		int year = spyear.getSelectedItemPosition() + 2014;
		if (type == 0) {
			month = month * 3 + 1;
		} else {
			month = month + 1;
		}
		if (month > 12) {
			month = month - 12;
			year = year + 1;
		}
		Date newdate = new Date(1, month, year);
		newdate.setYear(year);
		newdate.setMonth(month);
		newdate.setDate(1);
		return newdate;

	}

	public Date get_maxium_day() {
		Spinner sptype = (Spinner) findViewById(R.id.sptype);
		int type = sptype.getSelectedItemPosition();
		Spinner spmonth = (Spinner) findViewById(R.id.spmonth);
		int month = spmonth.getSelectedItemPosition();
		Spinner spyear = (Spinner) findViewById(R.id.spyear);
		int year = spyear.getSelectedItemPosition() + 2014;
		if (type == 0) {
			month = month * 3 + 4;
		} else {
			month = month + 2;
		}
		if (month > 12) {
			month = month - 12;
			year = year + 1;
		}
		Date newdate = new Date(1, month, year);
		newdate.setYear(year);
		newdate.setMonth(month);
		newdate.setDate(1);
		return newdate;

	}

}
