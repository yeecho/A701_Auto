package com.greatwall.autotest.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greatwall.autotest.R;

public class TestAdapter extends BaseAdapter{
	
	private List<String> list;
	private List<String> status;
	private LayoutInflater mInflater;
	
	public List<String> getList() {
		return list;
	}
	
	public void setList(List<String> list) {
		this.list = list;
	}
	
	public List<String> getStatus() {
		return status;
	}
	
	public void setStatus(List<String> status) {
		this.status = status;
	}
	
	public TestAdapter(Context context) {
		// TODO Auto-generated constructor stub
		list = new ArrayList<String>();
		status = new ArrayList<String>();
		String [] items = context.getResources().getStringArray(R.array.adapter_item);
		for (int i = 0; i < items.length; i++) {
			list.add(items[i]);
			status.add("no test");
		}
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_test, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.item_test_title);
			holder.statu = (TextView) convertView.findViewById(R.id.item_test_statu);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(list.get(position)+"²âÊÔ");
		holder.statu.setText(status.get(position));
		if (status.get(position).equals("Failed")) {
			convertView.setBackgroundColor(Color.RED);
		}else if (status.get(position).equals("Pass")) {
			convertView.setBackgroundColor(Color.GREEN);
		}else {
			convertView.setBackgroundColor(Color.YELLOW);
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView title;
		TextView statu;
	}
	
	public void update(int count, String str){
		status.set(count, str);
		notifyDataSetChanged();
	}
	
}
