package com.raidrin.shoppinglist;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * FileName: Controller.java
 * 
 * @author Aldrin Jerome Almacin
 *         <p>
 *         <b>Date: </b>November 11, 2012
 *         </p>
 *         <p>
 *         <b>Description: </b>Controller is used to make a connection from the
 *         app to the database in a way that no database call is made from the
 *         Activities. All the database manipulation and query is processed
 *         here. What the Activities do is just call the right methods to do
 *         CRUD operations.
 *         </p>
 * 
 */
public class Controller {
	public static final int DATABASE_VERSION = 1; // The version of the SQLite
													// Database
	public static final String DATABASE_NAME = "Assignment3"; // The name of the
																// Database

	public static final String SHOPPING_LIST_TABLE = "shoppingList"; // The name
																		// of
																		// the
																		// shopping
																		// list
																		// table
	public static final String SHOPPING_LIST_ID = "_id"; // The id that
															// identifies each
															// shopping list.
	public static final String SHOPPING_LIST_NAME = "listname"; // The name of
																// the shopping
																// list.

	public static final String ITEMS_TABLE = "items"; // The name of the items
														// table.
	public static final String ITEM_ID = "_id"; // The id that identifies each
												// item
	public static final String ITEM_NAME = "listname"; // The name of the item.
	public static final String ITEM_QUANTITY = "quantity"; // The quantity of
															// the item.
	// The reference to which shopping list the item belongs to.
	public static final String ITEM_SHOPPING_LIST_ID = "shoppinglistid";

	// The SQLite queries that will be used to create the shopping list and
	// items table
	public static final String SHOPPING_LIST_TABLE_QUERY = "CREATE TABLE "
			+ SHOPPING_LIST_TABLE + " (" + SHOPPING_LIST_ID
			+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
			+ SHOPPING_LIST_NAME + " TEXT NOT NULL);";
	public static final String ITEMS_TABLE_CREATE_QUERY = "CREATE TABLE "
			+ ITEMS_TABLE + " (" + ITEM_ID
			+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + ITEM_NAME
			+ " TEXT NOT NULL," + ITEM_QUANTITY + " INTEGER NOT NULL, "
			+ ITEM_SHOPPING_LIST_ID + " INTEGER NOT NULL,FOREIGN KEY("
			+ ITEM_SHOPPING_LIST_ID + ") REFERENCES " + SHOPPING_LIST_TABLE
			+ "(" + SHOPPING_LIST_ID + "));";

	private Context context; // A copy of the Activity context that is passed as
								// a constructor

	/**
	 * The constructor of the controller class.
	 * 
	 * @param context
	 *            The Activity context which is used to know which activity is
	 *            being modified.
	 */
	public Controller(Context context) {
		this.context = context; // Save a copy of the passed context
	} // End of Constructor

	/**
	 * Creates the shopping list and all its items then save those data into the
	 * database. If the mode is on modify, then a new shopping list wont be
	 * made. Also, if the shopping list being created already has the same name
	 * as an existing one, then just add the values given in this table to that
	 * existing table.
	 * 
	 * @param shoppingListName
	 *            The name of the shopping list to be created.
	 * @param maxItems
	 *            the maximum number of items the shoppingListTableLayout has.
	 * @param shoppingListTableLayout
	 *            The table layout that holds each item value.
	 * @param create
	 *            states whether the mode is on create
	 * @return The result of the creation/modification. True if successful,
	 *         false if not.
	 */
	public boolean createOrModifyShoppingList(String shoppingListName,
			int maxItems, TableLayout shoppingListTableLayout, boolean create) {
		// Stores all the values that will be checked from each item in this
		// table.
		ArrayList<ArrayList<String>> allValues = new ArrayList<ArrayList<String>>();
		// Used to count how many rows given are valid. Set the number of
		// correct rows to none.
		int numOfCorrectRowsCounter = 0;

		// Iterate through each item in the shoppingListTableLayout and check
		// whether the ShoppingListItem and quantity given by the user is
		// acceptable.
		for (int i = 0; i < maxItems; i++) {
			// Save the current Item into a variable.
			ShoppingListItem tempItem = (ShoppingListItem) shoppingListTableLayout
					.getChildAt(i);
			// Check if the item's name and quantity are both valid.
			if (tempItem.getShoppingListItemName() != ""
					&& tempItem.getQuantity() > 0) {
				// Create a temporary Array of String
				ArrayList<String> tempArrayList = new ArrayList<String>();
				// Add the values given by the user.
				tempArrayList.add(shoppingListName);
				tempArrayList.add(tempItem.getShoppingListItemName());
				tempArrayList.add(Integer.toString(tempItem.getQuantity()));
				// Add the arrayList to the allValues ArrayList
				allValues.add(tempArrayList);
				// Increment the number of correct rows.
				numOfCorrectRowsCounter++;
			} // End of Check item name and quantity if.
		} // End of shoppingListTable Layout For

		// If the correct number of valid rows is greater than one, then add the
		// items in the database.
		// Otherwise, skip this process then the method will return false.
		if (numOfCorrectRowsCounter > 0) {
			// If this is on create mode and the shoppingListItem is unique,
			// then create the new table.
			// If not, then the new rows will be added to an existing table with
			// the added value.
			if (create && shoppingListIsUnique(shoppingListName))
				addShoppingList(shoppingListName);
			// Create an iterator that iterates through each value in the
			// allValues ArrayList.
			Iterator<ArrayList<String>> it = allValues.iterator();
			while (it.hasNext()) {
				// Save the current ArrayList from the allValues ArrayList
				ArrayList<String> tempItem = it.next();
				// Add a shopping list item to the database with the values
				// specified by the user.
				addItem(tempItem.get(0), tempItem.get(1),
						Integer.parseInt(tempItem.get(2)));
			} // End of allValues while

			showAllItems(); // Show all database items in the console.
			return true; // Return a positive result of the creation.
		}
		return false; // Return a negative result of the creation.
	}

	/**
	 * Adds a shopping list item into the database with the values specified by
	 * the user.
	 * 
	 * @param shoppingListName
	 *            The name of the shopping list that the item is being added on.
	 * @param name
	 *            The name of the item being added
	 * @param quantity
	 *            The number of items to be bought by the user.
	 */
	public void addItem(String shoppingListName, String name, int quantity) {
		// Get the shopping id by the name of the shopping list.
		int id = getShoppingIdByValue(shoppingListName);
		// Create a ContentValues object that is used to insert the items in the
		// database. A key-value pair is used where the key will be the column
		// names.
		ContentValues contentValues = new ContentValues();
		contentValues.put(ITEM_NAME, name);
		contentValues.put(ITEM_QUANTITY, quantity);
		contentValues.put(ITEM_SHOPPING_LIST_ID, id);

		// Open the database.
		DBHelper dbHelper = new DBHelper();
		// Tell the database what kind of database open will be used then insert
		// the values in the items table.
		dbHelper.openToWrite().db.insert(ITEMS_TABLE, null, contentValues);
		dbHelper.close(); // Close the Database.
	} // End of addItem

	/**
	 * Adds a shopping list to the database with the name specified by the user.
	 * 
	 * @param shoppingListName
	 *            The shopping name specified by the user.
	 */
	public void addShoppingList(String shoppingListName) {
		// Open the database.
		DBHelper dbHelper = new DBHelper();
		// Create a ContentValues object that is used to insert the items in the
		// database. A key-value pair is used where the key will be the column
		// names.
		ContentValues contentValues = new ContentValues();
		contentValues.put(SHOPPING_LIST_NAME, shoppingListName);
		// Tell the database what kind of database open will be used then insert
		// the value in the shopping list table.
		dbHelper.openToWrite().db.insert(SHOPPING_LIST_TABLE, null,
				contentValues);
		dbHelper.close(); // Close the database
	} // End of addShoppingList

	/**
	 * Get the shopping list's id from the database by that shopping list's
	 * value. Each shopping list has a unique value so this is right.
	 * 
	 * @param value
	 *            The value of the shopping list
	 * @return The id of the shopping list. -1 if the Shopping List doesn't
	 *         exist yet.
	 */
	public int getShoppingIdByValue(String value) {
		int id = -1;
		// Open the database.
		DBHelper dbHelper = new DBHelper();
		// The result of the database query is saved in the tempCursor variable.
		// The cursor has all the values that is returned by the query and is
		// used to access each data.
		Cursor tempCursor = dbHelper.openToRead().db.query(SHOPPING_LIST_TABLE,
				new String[] { SHOPPING_LIST_ID }, SHOPPING_LIST_NAME + "='"
						+ value + "'", null, null, null, null);
		// Move the cursor to first and if the position is not the first, then
		// don't do anything.
		if (tempCursor.moveToFirst())
			// While the cursor haven't reached the last item, access the
			// current cursor and save its value to the id variable.
			while (!tempCursor.isAfterLast()) {
				id = Integer.parseInt(tempCursor.getString(tempCursor
						.getColumnIndex(SHOPPING_LIST_ID)));
				tempCursor.moveToNext(); // Move to the next cursor.
			} // End of !tempCursor.isAfterLast() While
		dbHelper.close(); // Close the database
		return id; // Return the value taken. -1 if no such Shopping list
					// exists.
	} // End getShoppingIdByValue method

	/**
	 * Get the shopping list name with the same id as the one passed.
	 * 
	 * @param id
	 *            The shopping list id used to find the shopping list name.
	 * @return The shopping list name that is identified by the passed id.
	 */
	public String getShoppingListNameById(int id) {
		String value = null; // Set the value to null initially.
		// Open the database and take the shopping list name from the shopping
		// list table where the shopping list id is the same as the id passed.
		DBHelper dbHelper = new DBHelper();
		Cursor tempCursor = dbHelper.openToRead().db.query(SHOPPING_LIST_TABLE,
				new String[] { SHOPPING_LIST_NAME }, SHOPPING_LIST_ID + "='"
						+ id + "'", null, null, null, null);
		// While the cursor haven't reached the last item, access the current
		// cursor and save its value to the id variable.
		if (tempCursor.moveToFirst())
			while (!tempCursor.isAfterLast()) {
				value = tempCursor.getString(tempCursor
						.getColumnIndex(SHOPPING_LIST_NAME));
				tempCursor.moveToNext(); // Move to the next cursor.
			} // End of !tempCursor.isAfterLast() While
		dbHelper.close(); // Close the database
		return value; // Return the shopping list name that has the same as the
						// id passed.
	} // End of getShoppingListNameById method

	/**
	 * Checks if the shopping list is unique and no shopping list with the same name exists.
	 * 
	 * @param name The name of the shopping list to be checked
	 * @return The result of the check. True if a shopping list with the given value doesn't exist. False if a shopping list with the name passed already exists.
	 */
	public boolean shoppingListIsUnique(String name) {
		return (getShoppingIdByValue(name) < 0) ? true : false;
	} // End of shoppingListIsUnique method

	/**
	 * Take the cursor that is used to grab all the shopping lists.
	 * @return The cursor returned by the select query.
	 */
	public Cursor takeShoppingListCursor() {
		return new DBHelper().openToRead().db.query(SHOPPING_LIST_TABLE, null, null,
				null, null, null, null);
	} // End of takeShoppingListCursor

	/**
	 * Delete all the shopping list items where shoppingid matches the shopping list.
	 * @param id The id that is used to check which shopping list items and which shopping list should be deleted.
	 */
	public void deleteAllShoppingListItemsByShoppingListId(int id) {
		// Open the database and delete the items with the given id.
		DBHelper dbHelper = new DBHelper();
		dbHelper.openToWrite().db.delete(ITEMS_TABLE, ITEM_SHOPPING_LIST_ID + "= " + id, null);
		dbHelper.openToWrite().db.delete(SHOPPING_LIST_TABLE, SHOPPING_LIST_ID + "=" + id, null);
		dbHelper.close(); // Close the database.
		
		showAllItems(); // Show all database items in the console.
	} // End of deleteAllShoppingListItemsByShoppingListId

	/**
	 * Shows all the items the database currently have. This method is  used for debugging purposes.
	 */
	private void showAllItems() {
		// Open the database and show each and every shopping list.
		DBHelper dbHelper = new DBHelper();
		// Output each and every row from the shopping list table.
		Cursor curs = dbHelper.openToRead().db.query(SHOPPING_LIST_TABLE, null,
				null, null, null, null, null);
		System.out
				.println("----------------- SHOPPING LISTS -----------------");
		curs.moveToFirst();
		while (!curs.isAfterLast()) {
			System.out.println("List id : "
					+ curs.getString(curs.getColumnIndex(SHOPPING_LIST_ID)));
			System.out.println("List name : "
					+ curs.getString(curs.getColumnIndex(SHOPPING_LIST_NAME)));
			curs.moveToNext();
		}
		curs.close(); // Close the cursor

		// Output each and every row from the items table.
		Cursor curs2 = dbHelper.openToRead().db.query(ITEMS_TABLE, null, null,
				null, null, null, null);
		System.out
				.println("----------------- SHOPPING LISTS ITEMS -----------------");
		curs2.moveToFirst();
		while (!curs2.isAfterLast()) {
			System.out.println("Item id : "
					+ curs2.getString(curs2.getColumnIndex(ITEM_ID)));
			System.out.println("Item name : "
					+ curs2.getString(curs2.getColumnIndex(ITEM_NAME)));
			System.out.println("Item quantity : "
					+ curs2.getString(curs2.getColumnIndex(ITEM_QUANTITY)));
			System.out.println("Item shopping list id : "
					+ curs2.getString(curs2
							.getColumnIndex(ITEM_SHOPPING_LIST_ID)));
			curs2.moveToNext();
		}
		curs2.close(); // Close the second cursor
		dbHelper.close(); // Close the database
	} // End of showAllItems method

	/**
	 * Updates a
	 * @param shoppingId
	 * @param newName
	 */
	public void updateShoppingListName(int shoppingId, String newName) {
		DBHelper dbHelper = new DBHelper();
		ContentValues cv = new ContentValues();
		cv.put(SHOPPING_LIST_NAME, newName);
		dbHelper.openToWrite().db.update(SHOPPING_LIST_TABLE, cv,
				SHOPPING_LIST_ID + "=" + shoppingId, null);
		dbHelper.close();
	}

	public void fillUpShoppingItems(int shoppingListId,
			EditText shoppingListEditText, TableLayout shoppingListTableLayout) {
		ArrayList<ArrayList<String>> allValues = getAllNameAndQuantityValues(shoppingListId);
		shoppingListEditText.setText(getShoppingListNameById(shoppingListId));
		Iterator<ArrayList<String>> it = allValues.iterator();
		int i = 0;
		while (it.hasNext()) {
			ArrayList<String> currentItem = it.next();
			ShoppingListItem tempItem = (ShoppingListItem) shoppingListTableLayout
					.getChildAt(i);
			EditText tempEditText = (EditText) tempItem.getChildAt(0);
			tempEditText.setText(currentItem.get(0));
			TextView tempTextView = (TextView) tempItem.getChildAt(2);
			tempTextView.setText(currentItem.get(1));
			tempItem.setQuantity(Integer.parseInt(currentItem.get(1)));
			i++;
		}
	}

	public void deleteAllShoppingListItemsByShoppingId(int shoppingListId) {
		DBHelper dbHelper = new DBHelper();
		dbHelper.openToWrite().db.delete(ITEMS_TABLE, "shoppinglistid = "
				+ shoppingListId, null);
		dbHelper.close();
	}

	public ArrayList<ArrayList<String>> getAllNameAndQuantityValues(
			int shoppingListId) {
		ArrayList<ArrayList<String>> allValues = new ArrayList<ArrayList<String>>();
		DBHelper dbHelper = new DBHelper();
		Cursor tempCursor = dbHelper.openToRead().db.query(ITEMS_TABLE,
				new String[] { ITEM_NAME, ITEM_QUANTITY },
				ITEM_SHOPPING_LIST_ID + " = " + shoppingListId, null, null,
				null, null);
		tempCursor.moveToFirst();
		while (!tempCursor.isAfterLast()) {
			ArrayList<String> tempArrayList = new ArrayList<String>();
			tempArrayList.add(tempCursor.getString(tempCursor
					.getColumnIndex(ITEM_NAME)));
			tempArrayList.add(tempCursor.getString(tempCursor
					.getColumnIndex(ITEM_QUANTITY)));
			allValues.add(tempArrayList);
			tempCursor.moveToNext();
		}
		tempCursor.close();
		dbHelper.close();
		return allValues;
	}

	public Cursor getAllNameAndQuantityCursor(int shoppingListId) {
		DBHelper dbHelper = new DBHelper();
		return dbHelper.openToRead().db.query(ITEMS_TABLE, new String[] {
				ITEM_ID, ITEM_NAME, ITEM_QUANTITY }, ITEM_SHOPPING_LIST_ID
				+ " = " + shoppingListId, null, null, null, null);
	}

	public boolean checkIfAListExists() {
		boolean notEmpty = false;
		DBHelper dbHelper = new DBHelper();
		Cursor tempCursor = dbHelper.openToRead().db.query(SHOPPING_LIST_TABLE,
				null, null, null, null, null, null);
		if (tempCursor.moveToFirst()) {
			notEmpty = true;
		}
		tempCursor.close();
		dbHelper.close();
		return notEmpty;
	}

	private class DBHelper {
		private SQLiteDatabase db;

		public DBHelper() {
		}

		public void close() {
			db.close();
		}

		public DBHelper openToRead() throws android.database.SQLException {
			ShoppingListSQLHelper shoppingListSQLHelper = new ShoppingListSQLHelper(
					context, DATABASE_NAME, null, DATABASE_VERSION);
			db = shoppingListSQLHelper.getReadableDatabase();
			return this;
		}

		public DBHelper openToWrite() throws android.database.SQLException {
			ShoppingListSQLHelper shoppingListSQLHelper = new ShoppingListSQLHelper(
					context, DATABASE_NAME, null, DATABASE_VERSION);
			db = shoppingListSQLHelper.getWritableDatabase();
			return this;
		}

		private class ShoppingListSQLHelper extends SQLiteOpenHelper {
			public ShoppingListSQLHelper(Context context, String name,
					CursorFactory factory, int version) {
				super(context, name, factory, version);
			}

			public void onCreate(SQLiteDatabase db) {
				db.execSQL(SHOPPING_LIST_TABLE_QUERY);
				db.execSQL(ITEMS_TABLE_CREATE_QUERY);
			}

			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
			}
		}
	}
}
