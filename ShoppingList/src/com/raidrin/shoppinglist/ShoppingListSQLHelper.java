package com.raidrin.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ShoppingListSQLHelper extends SQLiteOpenHelper {

	private String sql;
	public ShoppingListSQLHelper(Context context, String name, CursorFactory factory,
			int version, String sql) {
		super(context, name, factory, version);
		this.sql = sql;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
