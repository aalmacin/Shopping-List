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
	public static final String DATABASE_NAME = "Assignment3";
    public static final int DATABASE_VERSION = 1;
    public static final String SHOPPING_LIST_TABLE = "shoppingList";
	public static final String LIST_ID = "_id";
	public static final String LIST_NAME = "listname";
	
    public static final String ITEMS_TABLE = "items";
	public static final String ITEM_ID = "_id";
	public static final String ITEM_NAME = "listname";
	public static final String ITEM_QUANTITY = "quantity";
	public static final String ITEM_SHOPPING_LIST_ID = "shoppinglistid";
	public static final String ITEM_BOUGHT = "checked";

	public static final String SHOPPING_LIST_TABLE_QUERY = "CREATE TABLE "+SHOPPING_LIST_TABLE+" ("+LIST_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+LIST_NAME+" VARCHAR NOT NULL,"+ITEM_QUANTITY+" INTEGER NOT NULL,"+ITEM_BOUGHT+" INTEGER NOT NULL);";
	public static final String ITEMS_TABLE_CREATE_QUERY = "CREATE TABLE "+ITEMS_TABLE+" ("+ITEM_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+ITEM_NAME+" VARCHAR NOT NULL, " +
				""+ITEM_SHOPPING_LIST_ID+" INTEGER NOT NULL,FOREIGN KEY("+ITEM_SHOPPING_LIST_ID+") REFERENCES "+SHOPPING_LIST_TABLE+"("+LIST_ID+"));";
	private static final String TAG_NAME = "Debug";
	private SQLiteDatabase db;
	private Context context;
	public Controller(Context context) {
		this.context = context;
	}
	
	public void addItem(String name,int quantity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ITEM_NAME, name);
		contentValues.put(ITEM_QUANTITY, quantity);
        openToWrite().db.insert(ITEMS_TABLE, null, contentValues);
	}

	public ArrayList<HashMap<String,String>> takeAllShoppingList() {
		ArrayList<HashMap<String,String>> allNames = new ArrayList<HashMap<String,String>>();
		Controller controller = new Controller(context);
		Cursor tempCursor = controller.openToRead().db.query(SHOPPING_LIST_TABLE, null, 
				null, null, null, null, null);
		tempCursor.moveToFirst();
		while(!tempCursor.isAfterLast())
		{
			HashMap<String,String> nameAndId = new HashMap<String,String>();
			nameAndId.put(tempCursor.getString(tempCursor.getColumnIndex(LIST_ID)),tempCursor.getString(tempCursor.getColumnIndex(LIST_NAME)));
			allNames.add(nameAndId);
			tempCursor.moveToNext();
		}
		tempCursor.close();
		controller.close();
		return allNames;
	}
	
	public void close()
	{
		db.close();
	}
	
	public Controller openToRead()
	{
		ShoppingListSQLHelper shoppingListSQLHelper = new ShoppingListSQLHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = shoppingListSQLHelper.getReadableDatabase();
		return this;
	}
	
	public Controller openToWrite()
	{
        ShoppingListSQLHelper shoppingListSQLHelper = new ShoppingListSQLHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = shoppingListSQLHelper.getWritableDatabase();
		return this;
	}	
	
	 public boolean doesTblExist(String tblName){ 
	        Cursor rs = null; 
	        try{ 
	            rs = openToRead().db.rawQuery("SELECT * FROM " + SHOPPING_LIST_TABLE + " WHERE 1=0", null ); 
				Log.e(TAG_NAME, "It is existing");
	            return true; 
	        }catch(Exception ex){ 
				Log.e(TAG_NAME, "It is not in existence");
	            return false; 
	        }finally{ 
	            if (rs != null) rs.close(); 
	        } 
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
