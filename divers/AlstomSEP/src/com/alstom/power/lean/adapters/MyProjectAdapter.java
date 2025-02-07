package com.alstom.power.lean.adapters;

import java.util.List;

import com.alstom.power.lean.models.Project;
import com.alstom.power.lean.views.MyProjectCellView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyProjectAdapter extends BaseAdapter{
	private Context context;
	private List<Project> listProjects;
	
	public MyProjectAdapter(Context context, List<Project> listProjects){
		
		this.context = context;
		this.listProjects = listProjects;
		
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listProjects.size();
	}

	public Project getItem(int position) {
		// TODO Auto-generated method stub
		
		return listProjects.get(position);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		MyProjectCellView view = (MyProjectCellView) convertView;
		if (view == null) {
			view = new MyProjectCellView(context);
		}

		Project project = getItem(position);
		view.setData(project);

		return view;
	}

}
