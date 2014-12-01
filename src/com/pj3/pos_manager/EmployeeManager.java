package com.pj3.pos_manager;

import com.pj3.pos_manager.R;
import com.pj3.pos_manager.R.layout;
import com.pj3.pos_manager.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EmployeeManager extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_manager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.employee_manager, menu);
		return true;
	}

}
