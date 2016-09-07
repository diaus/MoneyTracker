package com.andrew.moneytracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

/**
 * Created by andrew on 07.09.2016.
 */
public class MyDatabaseOpenHelper extends DaoMaster.OpenHelper {
	private boolean isJustCreated;

	public MyDatabaseOpenHelper(Context context, String name) {
		super(context, name);
	}

	public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
		super(context, name, factory);
	}

	@Override
	public void onCreate(Database db) {
		super.onCreate(db);
		isJustCreated = true;
	}

	@Override
	public void onUpgrade(Database db, int oldVersion, int newVersion) {
		if (newVersion == oldVersion) return;
	}

	public boolean isJustCreated() {
		return isJustCreated;
	}
}
