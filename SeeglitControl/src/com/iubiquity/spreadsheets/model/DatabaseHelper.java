package com.iubiquity.spreadsheets.model;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "seeglit.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;
	
	private static DatabaseHelper mInstance = null;

//	// the DAO object we use to access the SimpleData table
	private Dao<Nat, Integer> natDao = null;
	private Dao<User, Integer> userDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	public static DatabaseHelper getInstance(final Context context) {
		if (mInstance == null) {
			mInstance = new DatabaseHelper(context);

		}
		return mInstance;
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Nat.class);
			TableUtils.createTable(connectionSource, User.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Nat.class, true);
			TableUtils.dropTable(connectionSource, User.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Nat, Integer> getNatDao() throws SQLException {
		if (natDao == null) {
			natDao = getDao(Nat.class);
		}
		return natDao;
	}


	
	public Dao<Nat, Integer> getUserDao() throws SQLException {
		if (userDao == null) {
			userDao = getDao(User.class);
		}
		return natDao;
	}
	
	public void addNat(String cameraId, String Protocole, String type, String externalPort, String destIp, String destPort,
			String nicVendor, String deviceType , String sid) throws SQLException{
	Nat natHost = new Nat();
	natHost.setCameraId(cameraId);
	natHost.setProtocole(Protocole);
	natHost.setType(type);
	natHost.setExternalPort(externalPort);
	natHost.setDestIP(destIp);
	natHost.setDestPort(destPort);
	natHost.setNicVendor(nicVendor);
	natHost.setDeviceType(deviceType);
	natHost.setSid(sid);
	
	this.getDao(Nat.class).create(natHost);
	
	}
	public void addUser(String username, String email, String uid, String createat ) throws SQLException{
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setUid(uid);
		user.setCreateat(createat);
		this.getDao(User.class).create(user);
	}
	
	public int getRowCount() throws SQLException{
		
		List<User> listUser = this.getDao(User.class).queryForAll();
		
		return listUser.size();
	}
	
	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		
	}
}