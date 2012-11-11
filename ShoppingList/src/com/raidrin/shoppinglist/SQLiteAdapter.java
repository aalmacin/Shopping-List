package com.raidrin.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteAdapter {
	

	//create table MY_DATABASE (ID integer primary key, Content text not null);
	private static final String SCRIPT_CREATE_DATABASE =
		"CREATE TABLE " + Controller.SHOPPING_LIST_TABLE + " ("
		+ Controller.LIST_ID + " INTEGER NOT NULL primary key autoincrement, "
		+ Controller.LIST_NAME + " VARCHAR NOT NULL);";



	private static final String TAG_NAME = "Debug";
	

	
	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;

	private Context context;
	
	public SQLiteAdapter(Context c){
		context = c;
		doesTblExist(Controller.SHOPPING_LIST_TABLE);
	}
	
	 public boolean doesTblExist(String tblName){ 
	        Cursor rs = null; 
	        try{ 
	            rs = openToRead().sqLiteDatabase.rawQuery("SELECT * FROM " + Controller.SHOPPING_LIST_TABLE + " WHERE 1=0", null ); 
				Log.e(TAG_NAME, "It is existing");
	            return true; 
	        }catch(Exception ex){ 
				Log.e(TAG_NAME, "It is not in existence");
	            return false; 
	        }finally{ 
	            if (rs != null) rs.close(); 
	        } 
	    } 
	
	public SQLiteAdapter openToRead() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, Controller.SHOPPING_LIST_TABLE, null, Controller.DATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;	
	}
	
	public SQLiteAdapter openToWrite() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, Controller.SHOPPING_LIST_TABLE, null, Controller.DATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;	
	}
	
	public void close(){
		sqLiteHelper.close();
	}
	
	public long insert(String content, String quantity){
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(Controller.LIST_NAME, content);
		return sqLiteDatabase.insert(Controller.SHOPPING_LIST_TABLE, null, contentValues);
	}
	
	public int deleteAll(){
		return sqLiteDatabase.delete(Controller.SHOPPING_LIST_TABLE, null, null);
	}
	
	public Cursor queueAll(){
		String[] columns = new String[]{Controller.LIST_ID, Controller.LIST_NAME};
		Cursor cursor = sqLiteDatabase.query(Controller.SHOPPING_LIST_TABLE, columns, 
				null, null, null, null, null);
		
		return cursor;
	}
	
	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(SCRIPT_CREATE_DATABASE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

}
