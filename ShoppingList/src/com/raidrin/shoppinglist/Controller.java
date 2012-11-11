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
import android.widget.TableLayout;

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
	
	public void addItem(String name,String shoppingListName,int quantity) {
		int id = 5000;
		id = getShoppingIdByValue(shoppingListName);
		ContentValues contentValues = new ContentValues();
		contentValues.put(ITEM_NAME, name);
		contentValues.put(ITEM_QUANTITY, quantity);
		contentValues.put(ITEM_SHOPPING_LIST_ID, id);
//		contentValues.put(ITEM_BOUGHT, NOT_SELECTED);
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

	public int getShoppingIdByValue(String name) {
		int id = -1;
		DBHelper dbHelper = new DBHelper();
		Cursor tempCursor = dbHelper.openToRead().db.query(SHOPPING_LIST_TABLE, new String[]{LIST_ID}, LIST_NAME+"='"+name+"'", null, null, null, null);
		if(tempCursor.moveToFirst())
		{
			while(!tempCursor.isAfterLast())
			{
//				Log.d(TAG_NAME, "List id: "+Integer.parseInt(tempCursor.getString(tempCursor.getColumnIndex(LIST_ID)))+" List name: "+tempCursor.getString(tempCursor.getColumnIndex(LIST_NAME)));
				id = Integer.parseInt(tempCursor.getString(tempCursor.getColumnIndex(LIST_ID)));
				tempCursor.moveToNext();
			}
		}
		dbHelper.close();
//		Log.d(TAG_NAME, "The id is changed to: "+id);
		return id;
	}

//	public ArrayList<Item> takeAllShoppingList() {
//		ArrayList<Item> nameAndId = new ArrayList<Item>();
//		DBHelper dbHelper = new DBHelper();
//		Cursor tempCursor = dbHelper.openToRead().db.query(SHOPPING_LIST_TABLE, null, null, null, null, null, null);
//		tempCursor.moveToFirst();
//		while(!tempCursor.isAfterLast())
//		{
//			new Item(Integer.parseInt(tempCursor.getString(tempCursor.getColumnIndex(LIST_ID))),tempCursor.getString(tempCursor.getColumnIndex(LIST_NAME)));
//			tempCursor.moveToNext();
//		}
//		tempCursor.close();
//		dbHelper.close();
//		return nameAndId;
//	}

	public boolean shoppingListIsUnique(String name) {
		Log.d(TAG_NAME, "The shopping list is "+((getShoppingIdByValue(name)<0)?"Unique":"Not unique"));
		return (getShoppingIdByValue(name)<0)?true:false;
	}
	
	public Cursor takeShoppingListCursor() {
		DBHelper dbHelper = new DBHelper();
		return dbHelper.openToRead().db.query(SHOPPING_LIST_TABLE, null, 
				null, null, null, null, null);
	}

	public void delete(String table,String keyId, int id) {
		DBHelper dbHelper = new DBHelper();
		Log.d(TAG_NAME, keyId + "=" + id);
        dbHelper.openToWrite().db.delete(table, keyId + "=" + id, null);
        dbHelper.close();
	}

	public void createOrAddItemsInShoppingList(String shoppingListName,	int maxItems, TableLayout shoppingListTableLayout) {

		boolean uniqueTable = shoppingListIsUnique(shoppingListName);
		
		if(uniqueTable)
		{
	        addShoppingList(shoppingListName);
		}
		
		int numOfCorrectRowsCounter = 0;
		for(int i=0;i<maxItems;i++)
		{
			ShoppingListItem tempItem = (ShoppingListItem) shoppingListTableLayout.getChildAt(i);
			if(tempItem.getShoppingListItemName() != "" && tempItem.getQuantity()>0)
			{
		        addItem(tempItem.getShoppingListItemName(),shoppingListName,tempItem.getQuantity());
		        numOfCorrectRowsCounter++;
	        }
		}
		if(numOfCorrectRowsCounter == 0 && uniqueTable )
		{
			delete(Controller.SHOPPING_LIST_TABLE, Controller.LIST_ID, getShoppingIdByValue(shoppingListName));
		}
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
