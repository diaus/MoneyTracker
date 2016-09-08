package com.andrew.moneytracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import java.text.Normalizer;

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

		// fix case-insensitive by adding COLLATE NOCASE to field and index
//		ProductDao.dropTable(db, true);
//		db.execSQL("CREATE TABLE \"PRODUCT\" (" + //
//				  "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
//				  "\"NAME\" TEXT NOT NULL," + // 1: name
//				  "\"PARENT_ID\" INTEGER);"); // 2: parentId

		//Normalizer.normalize("asd", Normalizer.Form.NFKD)
//		// Add Indexes
//		db.execSQL("CREATE INDEX IDX_PRODUCT_NAME ON PRODUCT" +
//				  " (\"NAME\" COLLATE NOCASE ASC);");

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
