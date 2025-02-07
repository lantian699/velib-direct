package com.alstom.lean.all.activities;


import java.sql.SQLException;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.Button;

import com.alstom.lean.all.R;
import com.alstom.lean.all.fragments.MesureDetailFragment;
import com.alstom.lean.all.fragments.MesureDocumentFragment;
import com.alstom.lean.all.fragments.TaskDetailFragment;
import com.alstom.lean.all.managers.TaskListManager;
import com.alstom.lean.all.models.DatabaseHelper;
import com.alstom.lean.all.models.Mesurement;
import com.alstom.lean.all.models.Task;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class MesureDetailActivity extends FragmentActivity{

	
	private static final int RESULT_CODE_TERMINATE = 2;
	private Task task;
	private DatabaseHelper dataHelper;
	private List<Mesurement> listMesure;
	private Button btn_terminate;
	private TaskListManager taskListManager;
	private static String PDF_GT26_PLAN_2D = "gt26_plan_2d.pdf";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_mesure_detail);
		
		task = (Task) getIntent().getSerializableExtra(TaskDetailFragment.TASK_NAME);
		dataHelper = LoginActivity.getDataHelper();
	
		 try {
			Dao<Mesurement, ?> mesureDao = dataHelper.getDao(Mesurement.class);
			QueryBuilder<Mesurement, ?> queryBuilder = mesureDao.queryBuilder();
			queryBuilder.where().eq(Mesurement.TABLE_MESURE_COLUMN_PARENT, task.getName());
			PreparedQuery<Mesurement> preparedQuery = queryBuilder.prepare();
			listMesure = mesureDao.query(preparedQuery);
					
					
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		taskListManager = MyProjectModeTabletActivity.getTaskListManager();
		
		Fragment fragment = new MesureDetailFragment(listMesure, task, taskListManager, dataHelper);
		getSupportFragmentManager().beginTransaction().replace(R.id.activity_mesure_detail_container_2, fragment).commit();
		
		/*Uri uri = Uri.parse("file://"+Environment.getExternalStorageDirectory());
		Fragment fragment_document = new MesureDocumentFragment(uri);
		getSupportFragmentManager().beginTransaction().replace(R.id.activity_mesure_detail_container_1, fragment_document).commit();*/
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			
			finish();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
