package com.raidrin.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ShoppingList {

	private String createSQL;
	private String name;
	private ShoppingListSQLHelper shoppingListSQLHelper;

	public ShoppingList(String name, Context context) {
		this.setName(name);
		createSQL = "CREATE TABLE "+name+" (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, item VARCHAR(15), quantity INTEGER);";
		shoppingListSQLHelper = new ShoppingListSQLHelper(context, SQLLiteInfo.DATABASE_NAME, null, SQLLiteInfo.DATABASE_VERSION, createSQL);
	}
	
	public SQLiteDatabase openToRead() {
		return shoppingListSQLHelper.getReadableDatabase();	
	}
	
	public SQLiteDatabase openToWrite() {
		return shoppingListSQLHelper.getWritableDatabase();	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String[] getItems()
	{
		return new String[]{};
	}
}
