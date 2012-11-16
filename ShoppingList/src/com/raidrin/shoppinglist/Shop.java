package com.raidrin.shoppinglist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
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
 *         If the user didn't checked specific items, then show an AlertDialog
 *         with all the items still needed.
 *         </p>
 * 
 */
public class Shop extends ListActivity {

	private int shoppingListId; // The id of the shopping list
	private Button doneButton; // The button that the user clicks when the
								// shopping is done.
	private TextView shoppingListNameTextView; // The TextView that shows the
												// name of the shopping list
	private ItemViewAdapter listViewAdapter; // The adapter that is used to draw
												// the views with the db values.
	private Controller controller; // An instance of the controller that is used
									// to grab and add data to the database.
	private ArrayList<ShoppingItem> shoppingItems; // All the ShoppingItem are
													// stored in an ArrayList
	private Context context; // The context of this activity
	private ListView listView;

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
		setContentView(R.layout.shop); // Set the content view to
										// shop. File:
										// shop.xml
		controller = new Controller(this); // The controller used for accessing
											// data from the database.
		shoppingItems = new ArrayList<ShoppingItem>(); // The arraylist that
														// stores all the items
		shoppingListNameTextView = (TextView) findViewById(R.id.shoppingListNameTextView); // The
																							// name
																							// of
																							// the
																							// current
																							// shopping
																							// list
		listView = (ListView) findViewById(android.R.id.list); // The android
																// list that is
																// used by this
																// ListActivity.

		// The button that needs to be clicked when the user thinks that the
		// shopping is done.
		doneButton = (Button) findViewById(R.id.done_button);
		doneButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Get the message from the unchecked lists. If it returns null,
				// then all shopping items are bought
				// Then close this activity if that's the case.
				// Else, show an AlertDialog with the items that are not bought
				// and give the user a choice to continue or cancel.
				String msg = getUncheckedListsMessage();
				if (msg != null) {
					// call the showAlertDialog class to show the AlertDialog
					showAlertDialog("You still need:", msg, "Continue",
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish(); // Close this activity
								} // End of onClick method
							} // End of onClickListener method
					);
				} else
					finish(); // Close this activity
			} // End of onClick method
		} // End of onClickListener
				);

		// Initialize the context to this activitie's context.
		context = this;

		// get the shopping list id that is passed to this activity via intents.
		shoppingListId = getIntent().getIntExtra(
				ShoppingListApp.SHOPPING_LIST_ID, -1);

		// Throw an error when there is no shoppingListId as with the one
		// passed.
		if (shoppingListId < 0)
			throw new Error("No shopping list item selected.");

		// Set the text to the shoppingListName of the passed id.
		shoppingListNameTextView.setText(controller
				.getShoppingListNameById(shoppingListId));

		// Get all the name and quantity values of items from the database.
		ArrayList<ArrayList<String>> allItems = controller
				.getAllNameAndQuantityValues(shoppingListId);
		// Add all the values from all the name and quantity values and pass
		// them to the created
		// ShoppingList objects that will be saved on the shoppingItems
		// ArrayList.
		for (int i = 0; i < allItems.size(); i++) {
			shoppingItems.add(new ShoppingItem(context, allItems.get(i).get(0),
					Integer.parseInt(allItems.get(i).get(1))));
		} // End of for

		// Initialize the adapter and use all the items as the views to be shown
		// in the adapter.
		listViewAdapter = new ItemViewAdapter(context, R.layout.shop,
				R.id.shoppingListEditText, allItems);

		// Set the adapter.
		setListAdapter(listViewAdapter);
	} // End of onCreate

	/**
	 * Get the message with unchecked values.
	 * 
	 * @return the message that contains all the shopping list items that are
	 *         not yet bought.
	 */
	private String getUncheckedListsMessage() {
		String itemsInMessage = ""; // Initially, the item has a blank value.
		boolean anItemIsNeeded = false; // A flag that is use to identify
										// whether an item is not checked.

		// Go through each item in the shoppingItems arraylist
		Iterator<ShoppingItem> it = shoppingItems.iterator();
		while (it.hasNext()) {
			ShoppingItem currentItem = it.next();
			// If the currentItem in the shoppingList ArrayList is not bought,
			// then add it to the message.
			// And set the boolean variable to true.
			if (!currentItem.isBought()) {
				itemsInMessage += "\n" + currentItem.getName()
						+ getTab(currentItem.getName())
						+ currentItem.getQuantity();
				anItemIsNeeded = true;
			} // End of If
		} // End of while
			// Set the title of the message.
		String message = "List Item\t\t\t\t\tQuantity";
		// If an item is needed, return the title concatenated with the items in
		// message
		// Else, return a null.
		return (anItemIsNeeded) ? message + itemsInMessage : null;
	} // End of getUncheckedListsMessage Method

	/**
	 * Returns the amount of tab that the string needs.
	 * 
	 * @return The String with the amount of tab
	 */
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
			return "\t\t\t\t\t\t\t";
		else if (str.length() < 10)
			return "\t\t\t\t\t";
		else if (str.length() < 11)
			return "\t\t\t\t\t";
		else if (str.length() < 12)
			return "\t\t\t\t";
		else
			return "\t\t\t";
	} // End of getTab

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
		alertDialogBuilder
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					// The onCancel method is called when the back key is
					// pressed.
					@Override
					public void onCancel(DialogInterface dialog) {
					} // End of onCancel method
				}); // End of OnCancelListener
		// Create the AlertDialog and show it on the screen
		alertDialogBuilder.create().show();
	} // End of showAlertDialog

	@Override
	public void onBackPressed() {
		// The back button on the user's phone is overriden in order for the
		// user to use the done button.
	} // End of onBackPressed method

	/**
	 * <p>
	 * <b>Date: </b>November 13, 2012
	 * </p>
	 * <p>
	 * <b>Description: </b>ShoppingItem is a class that extends LinearLayout
	 * which consists of a TextView that stores the name of the shopping list
	 * item, another TextView that stores the quantity, and a checkbox which
	 * tells whether the item is bought/not bought.
	 * </p>
	 */
	private class ShoppingItem extends LinearLayout {

		private boolean bought; // The boolean that tells whether this item is
								// bought.
		private int quantity; // The amount of item in this shopping item.
		private String name; // The name of the shopping list item.

		public ShoppingItem(Context context, String name, int quantity) {
			super(context);
			// Set the bought to false initially
			setBought(false);
			// Set the name to the name passed.
			this.setName(name);
			// Set the quantity to the one passed.
			this.setQuantity(quantity);
			// Set the LinearLayout orientation to horizontal.
			this.setOrientation(HORIZONTAL);

			// Initialize the itemNameTextView values.
			TextView itemNameTextView = new TextView(context);
			itemNameTextView.setWidth((int) context.getResources()
					.getDimension(R.dimen.itemSize));
			itemNameTextView.setTextSize(((int) context.getResources()
					.getDimension(R.dimen.show_text_size)));
			itemNameTextView.setPadding(((int) context.getResources()
					.getDimension(R.dimen.default_padding)), 0, 0, 0);
			itemNameTextView.setText(name);

			// Initialize the itemQuantityTextView values.
			TextView itemQuantityTextView = new TextView(context);
			itemQuantityTextView.setTextSize(((int) context.getResources()
					.getDimension(R.dimen.show_text_size)));
			itemQuantityTextView.setWidth((int) context.getResources()
					.getDimension(R.dimen.quantity_width));
			itemQuantityTextView.setText(Integer.toString(quantity));

			// Create the check box and set its listener so that it changes the
			// value of bought.
			// Each time the value changes.
			CheckBox checkBox = new CheckBox(context);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					setBought((isChecked) ? true : false);
				} // End of onCheckedChanged method
			});// End of onCheckedChangeListener method.

			// Add the views to this object.
			this.addView(itemNameTextView);
			this.addView(itemQuantityTextView);
			this.addView(checkBox);
		} // End of Constructor

		/**
		 * Getter for bought
		 */
		public boolean isBought() {
			return bought;
		} // End of getter

		/**
		 * Setter of bought
		 */
		public void setBought(boolean bought) {
			this.bought = bought;
		} // End of setter

		/**
		 * Getter of quantity
		 */
		public int getQuantity() {
			return quantity;
		} // End of Getter

		/**
		 * Setter of quantity
		 */
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		} // End of setter

		/**
		 * Getter of name
		 */
		public String getName() {
			return name;
		} // End of getter

		/**
		 * Setter of name
		 */
		public void setName(String name) {
			this.name = name;
		} // End of setter

	} // End of ShoppingItem class

	/**
	 * <p>
	 * <b>Date: </b>November 13, 2012
	 * </p>
	 * <p>
	 * <b>Description: </b>ItemViewAdapter is the adapter that puts the
	 * ShoppingItem views into the ListView.
	 * </p>
	 */
	private class ItemViewAdapter extends ArrayAdapter<ArrayList<String>> {

		public ItemViewAdapter(Context context, int resource,
				int textViewResourceId, List<ArrayList<String>> items) {
			super(context, resource, textViewResourceId, items);
			// Empty constructor
		} // End of Constructor

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Override the getView and use the shoppingItems ArrayList as the
			// views to be shown on screen.
			return shoppingItems.get(position);
		}// End of getView
	} // End of ItemViewAdapter class
} // End of Shop class
