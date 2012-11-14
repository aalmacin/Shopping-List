package com.raidrin.shoppinglist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * FileName: Shop.java
 * 
 * @author Aldrin Jerome Almacin
 *         <p>
 *         <b>Date: </b>November 13, 2012
 *         </p>
 *         <p>
 *         <b>Description: </b>Shop is a ListActivity that shows all the Items
 *         in a shopping list and is to be checked while the user is shopping.
 *         </p>
 * 
 */
public class Shop extends ListActivity {

	private int shoppingListId;	// The id of the shopping list
	private Button doneButton; // The button that the user clicks when the shopping is done.	
	private TextView shoppingListNameTextView; // The TextView that shows the name of the shopping list	
	private ItemViewAdapter listViewAdapter; // The adapter that is used to draw the views with the db values.	
	private Controller controller; // An instance of the controller that is used to grab and add data to the database.	
	private ArrayList<ShoppingItem> shoppingItems; // All the ShoppingItem are stored in an ArrayList
	private Context context;

	/**
	 * When the application starts/created, onCreate method is executed.
	 * Therefore, all initializations are done in this method.
	 * 
	 * @param savedInstanceState
	 *            The saved state of the application
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// In order for this override to be valid. A call to the super method
		// must be done.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);  // Set the content view to
										// shop. File:
										// shop.xml
		controller = new Controller(this);
		shoppingItems = new ArrayList<ShoppingItem>();
		shoppingListNameTextView = (TextView) findViewById(R.id.shoppingListNameTextView);
		doneButton = (Button) findViewById(R.id.done_button);
		doneButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String msg = getUncheckedListsMessage();
				if (msg != null) {
					showAlertDialog("You still need:", msg, "Continue",
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							});
				} else
					finish();
			}
		});
		
		context = this;
		
		shoppingListId = getIntent().getIntExtra(
				ShoppingListApp.SHOPPING_LIST_ID, -1);
		if (shoppingListId < 0)
			throw new Error("No shopping list item selected.");

		shoppingListNameTextView.setText(controller
				.getShoppingListNameById(shoppingListId));
		Cursor shoppingListCursor = controller
				.getAllNameAndQuantityCursor(shoppingListId);
		listViewAdapter = new ItemViewAdapter(context, R.layout.shop, R.id.shoppingListEditText, controller.getAllNameAndQuantityValues(shoppingListId));
//		listViewAdapter = new ItemViewAdapter(this, R.layout.shop,
//				shoppingListCursor, new String[] {}, new int[] {});
		setListAdapter(listViewAdapter);
	}

	private String getUncheckedListsMessage() {
		String itemsInMessage = "";
		boolean anItemIsNeeded = false;
		Iterator<ShoppingItem> it = shoppingItems.iterator();
		while (it.hasNext()) {
			ShoppingItem currentItem = it.next();
			if (!currentItem.isBought()) {
				itemsInMessage += "\n" + currentItem.getName()
						+ getTab(currentItem.getName())
						+ currentItem.getQuantity();
				anItemIsNeeded = true;
			}
		}
		String message = "List Item\t\t\t\t\tQuantity";
		return (anItemIsNeeded) ? message + itemsInMessage : null;
	}

	private String getTab(String str) {
		if (str.length() < 2)
			return "\t\t\t\t\t\t\t\t\t\t\t\t";
		else if (str.length() < 3)
			return "\t\t\t\t\t\t\t\t\t\t\t";
		else if (str.length() < 4)
			return "\t\t\t\t\t\t\t\t\t\t";
		else if (str.length() < 5)
			return "\t\t\t\t\t\t\t\t\t\t";
		else if (str.length() < 6)
			return "\t\t\t\t\t\t\t\t\t";
		else if (str.length() < 7)
			return "\t\t\t\t\t\t\t\t";
		else if (str.length() < 8)
			return "\t\t\t\t\t\t\t\t";
		else if (str.length() < 9)
			return "\t\t\t\t\t\t";
		else if (str.length() < 10)
			return "\t\t\t\t\t";
		else if (str.length() < 11)
			return "\t\t\t";
		else if (str.length() < 12)
			return "\t\t\t";
		else
			return "\t\t";
	}

	/**
	 * The AlertDialog that will be shown on the screen is created and shown.
	 * 
	 * @param title
	 *            The title of the AlertDialog box
	 * @param message
	 *            The message in the AlertDialog box
	 * @param buttonText
	 *            The text inside the OK button
	 * @param buttonOkListener
	 *            the listener for the OK button
	 */
	private void showAlertDialog(String title, String message,
			String buttonText, AlertDialog.OnClickListener buttonOkListener) {
		// Get the AlertDialog Builder class and save it in a variable.
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// Set the title, message, OK button, and the cancel listener of the
		// AlertDialog
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(message);

		// Positive button is using the buttonOKListener which is passed as a
		// parameter
		// as its event listener.
		alertDialogBuilder.setPositiveButton(buttonText, buttonOkListener);
		String cancelButtonText = getString(R.string.cancel);
		alertDialogBuilder.setNegativeButton(cancelButtonText,
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});

		// set the cancel listener of the AlertDialog
		// Cancel is when the user pressed the back key in his/her phone.
		// alertDialogBuilder.setOnCancelListener(new
		// DialogInterface.OnCancelListener()
		// {
		// // The onCancel method is called when the back key is pressed.
		// @Override
		// public void onCancel(DialogInterface dialog) {
		// } // End of onCancel method
		// }); // End of OnCancelListener
		// Create the AlertDialog and show it on the screen
		alertDialogBuilder.create().show();
	} // End of showAlertDialog

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		controller.closeCursor();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shop, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
	}

	private class ShoppingItem extends LinearLayout {

		private boolean bought;
		private int quantity;
		private String name;

		public ShoppingItem(Context context, String name, int quantity) {
			super(context);
			setBought(false);
			this.setName(name);
			this.setQuantity(quantity);
			this.setOrientation(HORIZONTAL);
			TextView itemNameTextView = new TextView(context);
			itemNameTextView.setWidth((int) context.getResources()
					.getDimension(R.dimen.itemSize));
			itemNameTextView.setTextSize(((int) context.getResources()
					.getDimension(R.dimen.show_text_size)));
			itemNameTextView.setPadding(((int) context.getResources()
					.getDimension(R.dimen.default_padding)), 0, 0, 0);
			itemNameTextView.setText(name);

			TextView itemQuantityTextView = new TextView(context);
			itemQuantityTextView.setText(Integer.toString(quantity));
			itemQuantityTextView.setTextSize(((int) context.getResources()
					.getDimension(R.dimen.show_text_size)));
			itemQuantityTextView.setWidth((int) context.getResources()
					.getDimension(R.dimen.quantity_width));

			CheckBox checkBox = new CheckBox(context);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					setBought((isChecked) ? true : false);
				}
			});
			this.addView(itemNameTextView);
			this.addView(itemQuantityTextView);
			this.addView(checkBox);
		}

		public boolean isBought() {
			return bought;
		}

		public void setBought(boolean bought) {
			this.bought = bought;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}


	private class ItemViewAdapter extends ArrayAdapter<ArrayList<String>> {


		private List<ArrayList<String>> allItems;

		public ItemViewAdapter(Context context, int resource,
				int textViewResourceId, List<ArrayList<String>> items) {
			super(context, resource, textViewResourceId, items);
			allItems = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ShoppingItem item = new ShoppingItem(context, allItems.get(position).get(0), Integer.parseInt(allItems.get(position).get(1)));
			return item;
		}
	}
	
//	private class ItemView2Adapter extends SimpleCursorAdapter {
//		public ItemViewAdapter(Context context, int layout, Cursor c,
//				String[] from, int[] to) {
//			super(context, layout, c, from, to);
//		}
//
//		@Override
//		public View newView(Context context, Cursor cursor, ViewGroup parent) {
//			String name = cursor.getString(cursor
//					.getColumnIndex(Controller.ITEM_NAME));
//			int quantity = cursor.getInt(cursor
//					.getColumnIndex(Controller.ITEM_QUANTITY));
//			ShoppingItem item = new ShoppingItem(context, name, quantity);
//			shoppingItems.add(item);
//			item.setTag(cursor.getString(cursor
//					.getColumnIndex(Controller.ITEM_ID)));
//			return item;
//		}
//	}
}
