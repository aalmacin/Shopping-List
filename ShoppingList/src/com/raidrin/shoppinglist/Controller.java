package com.raidrin.shoppinglist;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class Controller {
    public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Assignment3";
    public static final String SHOPPING_LIST_TABLE = "shoppingList";
	public static final String LIST_ID = "_id";
	public static final String LIST_NAME = "listname";
	
    public static final String ITEMS_TABLE = "items";
	public static final String ITEM_ID = "_id";
	public static final String ITEM_NAME = "listname";
	public static final String ITEM_QUANTITY = "quantity";
	public static final String ITEM_SHOPPING_LIST_ID = "shoppinglistid";
	public static final String ITEM_BOUGHT = "checked";

	public static final String SHOPPING_LIST_TABLE_QUERY = "CREATE TABLE "+SHOPPING_LIST_TABLE+" ("+LIST_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+LIST_NAME+" TEXT NOT NULL);";
	public static final String ITEMS_TABLE_CREATE_QUERY = "CREATE TABLE "+ITEMS_TABLE+" ("+ITEM_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+ITEM_NAME+" TEXT NOT NULL,"+ITEM_QUANTITY+" INTEGER NOT NULL, " + ITEM_SHOPPING_LIST_ID+" INTEGER NOT NULL,FOREIGN KEY("+ITEM_SHOPPING_LIST_ID+") REFERENCES "+SHOPPING_LIST_TABLE+"("+LIST_ID+"));";
	private static final String TAG_NAME = "Debug";
	protected static final int NOT_SELECTED = 0;
	protected static final int SELECTED = 1;
	
	private Context context;
	public Controller(Context context) {
		this.context = context;
	}
	
	public void addItem(String name,int quantity,int shoppingId,int selected) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ITEM_NAME, name);
		contentValues.put(ITEM_QUANTITY, quantity);
		DBHelper dbHelper = new DBHelper();
        dbHelper.openToWrite().db.insert(ITEMS_TABLE, null, contentValues);
        dbHelper.close();
	}
	
	public void addShoppingList(String shoppingListName) {
		DBHelper dbHelper = new DBHelper();
		ContentValues contentValues = new ContentValues();
		contentValues.put(LIST_NAME, shoppingListName);
		dbHelper.openToWrite().db.insert(SHOPPING_LIST_TABLE, null, contentValues);
		dbHelper.close();
	}

	public int getShoppingIdByValue(String string) {
		int id = 0;
		DBHelper dbHelper = new DBHelper();
		Cursor tempCursor = dbHelper.openToRead().db.query(SHOPPING_LIST_TABLE, new String[]{LIST_ID}, LIST_NAME+"=", new String[]{string}, null, null, null);
		while(!tempCursor.isAfterLast())
		{
			id = (tempCursor.getString(tempCursor.getColumnIndex(LIST_ID))==null)?-6:Integer.parseInt(tempCursor.getString(tempCursor.getColumnIndex(LIST_ID)));
			if(id == -6)
			{
				Log.e(TAG_NAME, "Its a null");
			}
			tempCursor.moveToNext();
		}
		dbHelper.close();
		return id;
	}

	public HashMap<String,String> takeAllShoppingList() {
		HashMap<String,String> nameAndId = new HashMap<String,String>();
		DBHelper dbHelper = new DBHelper();
		Cursor tempCursor = dbHelper.openToRead().db.query(SHOPPING_LIST_TABLE, null, 
				null, null, null, null, null);
		tempCursor.moveToFirst();
		while(!tempCursor.isAfterLast())
		{
			nameAndId.put(tempCursor.getString(tempCursor.getColumnIndex(LIST_ID)),tempCursor.getString(tempCursor.getColumnIndex(LIST_NAME)));
			tempCursor.moveToNext();
		}
		tempCursor.close();
		dbHelper.close();
		return nameAndId;
	}
	
	public Cursor takeShoppingListCursor() {
		DBHelper dbHelper = new DBHelper();
		Cursor tempCursor = dbHelper.openToRead().db.query(SHOPPING_LIST_TABLE, null, 
				null, null, null, null, null);
		dbHelper.close();
		tempCursor.close();
		return tempCursor;
	}

	private class DBHelper
	{
		private SQLiteDatabase db;
		public DBHelper() {
			
		}
		
		public void close()
		{
			db.close();
		}
		
		public DBHelper openToRead()
		{
			ShoppingListSQLHelper shoppingListSQLHelper = new ShoppingListSQLHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
			db = shoppingListSQLHelper.getReadableDatabase();
			return this;
		}
		
		public DBHelper openToWrite()
		{
	        ShoppingListSQLHelper shoppingListSQLHelper = new ShoppingListSQLHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
			db = shoppingListSQLHelper.getWritableDatabase();
			return this;
		}
		
		private class ShoppingListSQLHelper extends SQLiteOpenHelper {
			public ShoppingListSQLHelper(Context context, String name, CursorFactory factory,
					int version) {
				super(context, name, factory, version);
			}

			public void onCreate(SQLiteDatabase db) {
				db.execSQL(SHOPPING_LIST_TABLE_QUERY);
				db.execSQL(ITEMS_TABLE_CREATE_QUERY);
			}
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			}
		}
	}
}
