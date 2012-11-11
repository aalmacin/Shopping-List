package com.raidrin.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ShoppingListSQLHelper extends SQLiteOpenHelper {
	public ShoppingListSQLHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQLiteInfo.SHOPPING_LIST_TABLE_QUERY);
		db.execSQL(SQLiteInfo.ITEMS_TABLE_CREATE_QUERY);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
