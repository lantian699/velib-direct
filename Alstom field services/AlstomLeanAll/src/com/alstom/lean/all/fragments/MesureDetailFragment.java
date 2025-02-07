package com.alstom.lean.all.fragments;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alstom.lean.all.R;
import com.alstom.lean.all.activities.ImageDisplayActivity;
import com.alstom.lean.all.activities.MyProjectModeTabletActivity;
import com.alstom.lean.all.adapters.AssistanceRequestAdapter;
import com.alstom.lean.all.adapters.MesureDetailAdapter;
import com.alstom.lean.all.adapters.SectionedAdapter;
import com.alstom.lean.all.managers.ChangeObserver;
import com.alstom.lean.all.managers.TaskListManager;
import com.alstom.lean.all.models.DatabaseHelper;
import com.alstom.lean.all.models.Mesurement;
import com.alstom.lean.all.models.ModelObject;
import com.alstom.lean.all.models.Task;
import com.alstom.lean.all.pdfviewer.PdfViewerActivity;
import com.alstom.lean.all.views.CustomDialog;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MesureDetailFragment extends Fragment implements OnClickListener{
	
	private List<Mesurement> listMesure;
	private ListView listViewMesure;
	private MesureDetailAdapter adapter;
	private Task task;
	private TextView title;
	private TaskListManager manager;
	private DatabaseHelper dataHelper;
	private Button btn_terminate;
	private Button btn_cancel;
	private TaskListManager taskListManager;
	private ArrayAdapter<String> adapterString;
	private List<String> listFileName;
	private Button btn_add_sub_mesure;
	private Dao<Mesurement, ?> mesureDao;

	public MesureDetailFragment(List<Mesurement> listMesure, Task task, TaskListManager manager, DatabaseHelper hepler){
		
		this.listMesure = listMesure;
		this.task = task;
		this.manager = manager;
		this.dataHelper = hepler;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
	
    	
    }
 
 
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		  View view = inflater.inflate(R.layout.fragment_mesure_detail, container, false);
		  
		title = (TextView) view.findViewById(R.id.title_md_mesure);
		title.setText(task.getName());

		listViewMesure = (ListView) view.findViewById(R.id.list_mesure);
		adapter = new MesureDetailAdapter(getActivity(), listMesure, manager, dataHelper, task);
		listViewMesure.setAdapter(adapter);

		btn_terminate = (Button) view.findViewById(R.id.cancelsavesendbar_send);
		btn_cancel = (Button)view.findViewById(R.id.cancelsavesendbar_cancel);
		btn_terminate.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		taskListManager = MyProjectModeTabletActivity.getTaskListManager();
		
		taskListManager.registerAddMesureChangeObserver(new ChangeObserver() {
			
			

			@Override
			public void onChange(String res, ModelObject model) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChange(String res) {
				// TODO Auto-generated method stub
				try {
					Dao<Mesurement, ?> mesureDao = dataHelper.getDao(Mesurement.class);
					QueryBuilder<Mesurement, ?> queryBuilder = mesureDao.queryBuilder();
					queryBuilder.where().eq(Mesurement.TABLE_MESURE_COLUMN_PARENT, task.getName());
					PreparedQuery<Mesurement> preparedQuery = queryBuilder.prepare();
					listMesure = mesureDao.query(preparedQuery);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				adapter = new MesureDetailAdapter(getActivity(), listMesure, manager, dataHelper, task);
				listViewMesure.setAdapter(adapter);
			}
			
			@Override
			public void onChange() {
				// TODO Auto-generated method stub

				
			}
		});
		
		btn_add_sub_mesure = (Button) view.findViewById(R.id.add_sub_mesure);
		btn_add_sub_mesure.setOnClickListener(this);
		
		
		  
		 return view;
	 }
	
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cancelsavesendbar_send:
					
			
			getActivity().finish();
			
			break;
			
		case R.id.cancelsavesendbar_cancel:
			
			getActivity().finish();
			
			break;
			
		case R.id.add_sub_mesure:
			CustomDialog dialog = new CustomDialog(getActivity(), dataHelper, task,taskListManager);
			dialog.show();
			break;

		default:
			break;
		}
		
	}


}
