package com.alstom.lean.all.spreadsheet;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;

import com.alstom.lean.all.activities.LoginActivity;
import com.alstom.lean.all.dropbox.DownloadFileFromDropbox;
import com.alstom.lean.all.dropbox.UploadFileToDropbox;
import com.alstom.lean.all.models.DatabaseHelper;
import com.alstom.lean.all.models.UserGroup.User;
import com.alstom.lean.all.spreadsheet.Worksheet.Table;
import com.alstom.lean.all.tools.Tools;
import com.google.api.services.spreadsheet.client.SpreadsheetClient;

/**
 * AsyncTask to synchronize the local DB with the spreadsheet
 * 
 * @author guillaumedavo
 * 
 */
public class SynchronizationTask extends AsyncTask<String, Integer, Integer> {

	private static final String TAG = "SynchronizationTask";
	private Activity mMainActivity;

	private static final int NO_ACCOUNT_ERROR = -1;
	private static final int IO_EXCEPTION = -2;

	public String errorMessage;

	private static final String KEY_LAST_SYNC = "last_sync";
	private static Time lastSyncTime;

	// columns indices (store them for performance reasons)
	private Integer mUpdatedIndex;
	private Integer mIdIndex;
	private ProgressDialog dialog;
	private List<ContentValues> allValues;

	// same as in settings.xml
	// Change this key for a different source spreadsheet
	private String spreadsheetKey;
	private SpreadsheetClient client;
	private DatabaseHelper dataHelper;
	private boolean isSend;
	private User user;

	public SynchronizationTask(Activity mainActivity,
			DatabaseHelper dataHelper, User user) {
		super();
		mMainActivity = mainActivity;
		this.dataHelper = dataHelper;
		SharedPreferences settings = mainActivity.getSharedPreferences(TAG, 0);
		long millis = settings.getLong(KEY_LAST_SYNC, 0);
		lastSyncTime = new Time();
		lastSyncTime.set(millis);
		this.user = user;
	}

	@Override
	protected void onPreExecute() {

		dialog = ProgressDialog.show(mMainActivity, "",
				"Please wait for data download ...");

	}

	@Override
	protected Integer doInBackground(String... params) {

		// create the SpreadSheetDb
		// SharedPreferences settings = mMainActivity.getSharedPreferences(TAG,
		// Activity.MODE_PRIVATE);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(mMainActivity);
		SpreadsheetAndroidRequestInitializer requestInitializer;
		try {
			requestInitializer = new SpreadsheetAndroidRequestInitializer(
					settings, mMainActivity);

			if (params[0].equals("getAll")) {
				for (Table table : Table.values())

					Tools.getAll(dataHelper, requestInitializer, table, user);
				isSend = false;
			} else if (params[0].equals("sendAll")) {
				for (Table table : Table.values())
					Tools.sendAll(dataHelper, requestInitializer, table, user);
				isSend = true;

			}
		} catch (IllegalArgumentException e) {
			errorMessage = e.getMessage();
			Log.e(TAG, errorMessage, e);
			return NO_ACCOUNT_ERROR;
		}

		return null;
	}

	

	@Override
	protected void onPostExecute(Integer result) {

		dialog.dismiss();

		if (isSend) {
			new UploadFileToDropbox(mMainActivity,
					LoginActivity.getDropboxApi()).execute();
		} else {
			new DownloadFileFromDropbox(mMainActivity, LoginActivity.getDropboxApi(), "/Alstom").execute();
		}
	}
}
