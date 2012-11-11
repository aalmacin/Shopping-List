package com.raidrin.shoppinglist;

public class SQLiteInfo {
	public static final String DATABASE_NAME = "Assignment3";
    public static final int DATABASE_VERSION = 1;
    
    public static final String SHOPPING_LIST_TABLE = "shoppingList";
	public static final String LIST_ID = "_id";
	public static final String LIST_NAME = "listname";
	
    public static final String ITEMS_TABLE = "items";
	public static final String ITEM_ID = "_id";
	public static final String ITEM_NAME = "listname";
	public static final String ITEM_SHOPPING_LIST_ID = "shoppinglistid";
	private static final String ITEM_BOUGHT = "checked";

	public static final String ITEMS_TABLE_CREATE_QUERY = "create table "+SQLiteInfo.ITEMS_TABLE+" ("+SQLiteInfo.ITEM_ID+" integer not null primary key autoincrement,"+SQLiteInfo.ITEM_NAME+" text, " +
				""+SQLiteInfo.ITEM_SHOPPING_LIST_ID+" integer,foreign key("+SQLiteInfo.ITEM_SHOPPING_LIST_ID+") references "+SQLiteInfo.SHOPPING_LIST_TABLE+"("+SQLiteInfo.LIST_ID+"));";

	public static final String SHOPPING_LIST_TABLE_QUERY = "create table "+SQLiteInfo.SHOPPING_LIST_TABLE+" ("+SQLiteInfo.LIST_ID+" integer not null primary key autoincrement,"+SQLiteInfo.LIST_NAME+" text,"+SQLiteInfo.ITEM_BOUGHT+" integer);";
	
	

	
}
