package com.raidrin.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.view.View.OnClickListener;

public class ShoppingListSQLHelper extends SQLiteOpenHelper {

	private SQLiteDatabase db;

	public ShoppingListSQLHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("Debug", "DB is initialized");
		this.db = db;
	}

	public void createNewList(String sql)
	{
		if(db == null)
		{
			Log.e("Debug", "DB is null");
		}
//		db.execSQL(sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
