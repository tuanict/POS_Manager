package com.pj3.pos_manager;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemPaymentAdapter extends ArrayAdapter<food> {

	Activity context = null;
	ArrayList<food> myArray = null;
	int layoutId;

	public ItemPaymentAdapter(Activity context, int layoutId,
			ArrayList<food> arr) {
		super(context, layoutId, arr);
		this.context = context;
		this.layoutId = layoutId;
		this.myArray = arr;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(layoutId, null);
		final food afd =myArray.get(position);
		final TextView txtname=(TextView)
				 convertView.findViewById(R.id.name);
		txtname.setText(afd.getName());
		final TextView txtprice=(TextView)
				 convertView.findViewById(R.id.price);
		txtprice.setText(String.valueOf(afd.getPrice()));
		final TextView txtnumberof=(TextView)
				 convertView.findViewById(R.id.numberof);
		txtnumberof.setText(String.valueOf(afd.getNumberOf()));
		final TextView txttotal=(TextView)
				 convertView.findViewById(R.id.total);
		txttotal.setText(String.valueOf(afd.getNumberOf()*afd.getPrice())+ " VNĐ");
		
		return convertView;
	}

}
