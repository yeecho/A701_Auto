package com.greatwall.autotest.bean;

import java.util.ArrayList;

import android.content.Context;

import com.greatwall.autotest.R;

public class TestResult {
	
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<String> results = new ArrayList<String>();
	private ArrayList<String> reasons = new ArrayList<String>();
	private ArrayList<String> values = new ArrayList<String>();
	
	public TestResult(Context context){
		String [] items = context.getResources().getStringArray(R.array.adapter_item);
		for (int i = 0; i < items.length; i++) {
			titles.add(items[i]);
			results.add("no test");
			reasons.add("");
			values.add("");
		}
	}
	
	public ArrayList<String> getTitles() {
		return titles;
	}
	public void setTitles(ArrayList<String> titles) {
		this.titles = titles;
	}
	public ArrayList<String> getResults() {
		return results;
	}
	public void setResults(ArrayList<String> results) {
		this.results = results;
	}
	public ArrayList<String> getReasons() {
		return reasons;
	}
	public void setReasons(ArrayList<String> reasons) {
		this.reasons = reasons;
	}
	public ArrayList<String> getValues() {
		return values;
	}
	public void setValues(ArrayList<String> values) {
		this.values = values;
	}
	
	public void setResult(int position, String result, String reason, String value){
		results.set(position, result);
		reasons.set(position, reason);
		values.set(position, value);
	}

}
