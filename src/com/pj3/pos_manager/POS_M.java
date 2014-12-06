package com.pj3.pos_manager;

import java.util.List;

import com.pj3.pos_manager.database.DatabaseSource;
import com.pj3.pos_manager.res_obj.Employee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class POS_M extends Activity {

	EditText email, password;
	Button submit;
	public static DatabaseSource db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		db = new DatabaseSource(this);
		
		List<Employee> employees = db.getAllUsers();
		int size = employees.size();
		if(size == 0){
			db.createUser(new Employee("admin", "admin@gmail.com", "admin", "admin.png", 12345, 1));
		}
		
		email = (EditText) findViewById(R.id.login_email);
		password = (EditText) findViewById(R.id.login_pass);
		submit = (Button) findViewById(R.id.login_submit);
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String e_email = email.getText().toString();
				String pass = password.getText().toString(); 
				boolean ok = false;
				Employee user = null;
				if(!e_email.equals("") && !pass.equals(""))
					ok = true;
				if(ok)
					user = db.checkUser(e_email, pass);
				
				if(user != null){
					if(user.getPOSITION_p_id() == 1){
						Intent mainItent = new Intent(POS_M.this, Manager.class);
						startActivity(mainItent);
					}
					else 
						Toast.makeText(
								getApplicationContext(),
								"Bạn không phải là quản lí. Vui lòng đăng nhập vào thiết bị phù hợp",
								Toast.LENGTH_SHORT).show();
				}else
					Toast.makeText(
							getApplicationContext(),
							"Email hoặc mật khẩu không đúng",
							Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
