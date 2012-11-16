package com.raidrin.shoppinglist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * FileName: ShoppingListApp.java
 * 
 * @author Aldrin Jerome Almacin
 *         <p>
 *         <b>Date: </b>November 15, 2012
 *         </p>
 *         <p>
 *         <b>Description: </b>ShoppingListApp is the main activity that will be
 *         launched when the application is started. All the shopping list's are
 *         shown in the screen.
 * 
 *         ShoppingListApp pauses when: ShoppingListApp pauses when the user
 *         needs to shop the items in the shopping list. ShoppingListApp pauses
 *         and run another activity when a new Shopping list is to be added.
 *         ShoppingListApp pauses when called by other activity when a Shopping
 *         List is accessed through modify.
 *         </p>
 * 
 */
public class ShoppingListApp extends ListActivity {

	private static final String TAG_NAME = "Debug"; // Tag name used by the
													// Log(Debugging purposes).
	// The ids which is used to identify which menu is selected.
	private static final int SHOP_ID = 0;
	private static final int MODIFY_ID = 1;
	private static final int DELETE_ID = 2;

	// The request ids of the request to be sent to the AddModify Activity
	public static final int MODIFY_REQUEST = 1;
	public static final int CREATE_REQUEST = 2;

	// The name/type of the items to be passed in the intents.
	public static final String CREATE_MODIFY = "TheCreateModiFyRequest";
	public static final String SHOPPING_LIST_ID = "TheShoppingListName";

	// The button that is clicked when the user want to add a new shopping list
	private ImageButton addShoppingListImageButton;

	// The intents used to start the other activities
	private Intent addModifyIntent;
	private Intent shopIntent;

	// The context of this activity
	private Context context;

	// The controller that connects to the database and changes data on it.
	private Controller controller;

	// The current selectedItem id of the database shopping item.
	private int selectedItem;

	// the textview that tells the user to add a new shopping list
	private TextView addShoppingListTextView;

	// The item adapter than shows the views into the list
	private ItemViewAdapter listViewAdapter;

	// The ListView that shows the textviews that contains the name of the
	// shopping list.
	private ListView listView;

	// All the TextViews are saved in an ArrayList.
	private ArrayList<TextView> allTextViews;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		addShoppingListImageButton = (ImageButton) findViewById(R.id.addShoppingListImageButton);
		listView = (ListView) findViewById(android.R.id.list);
		addShoppingListImageButton.setOnClickListener(addShoppingListListener);
		addShoppingListTextView = (TextView) findViewById(R.id.addShoppingListTextView);

		// Initialize the context to this activity
		context = this;

		controller = new Controller(context);

		// Initialize the intents into their respective Activities
		addModifyIntent = new Intent(context, AddModify.class);
		shopIntent = new Intent(this, Shop.class);
	} // End of onCreate

	// Create an anonymous inner class of type OnClickListener
	private OnClickListener addShoppingListListener = new OnClickListener() {
		public void onClick(android.view.View v) {
			// When this listener is dispatched, add the extra data in the
			// intent with the create request.
			addModifyIntent.putExtra(CREATE_MODIFY, CREATE_REQUEST);
			// Then start the activity.
			startActivity(addModifyIntent);
		}; // End of onClick method
	}; // End of addShoppingListListener anonymous inner class

	// Do some check when the activity is resumed.
	protected void onResume() {
		super.onResume();
		// If a shopping list doesn't exist, make the addShoppingListTextView
		// visible
		if (!controller.checkIfAShoppingListExists()) {
			addShoppingListTextView.setVisibility(View.VISIBLE);
		} else {
			addShoppingListTextView.setVisibility(View.INVISIBLE);
		}

		// Set the adapter to null. Getting rid of the current adapter that's
		// existing.
		listView.setAdapter(null);
		listViewAdapter = null;
		// Initialize the allTextViews field as a new ArrayList
		allTextViews = new ArrayList<TextView>();
		// Get all the shopping lists and save them in an allTables ArrayList
		ArrayList<ArrayList<String>> allTables = controller
				.getAllShoppingLists();
		// if there is at least one table, create a new adapter and set it as
		// the list's adapter
		if (allTables != null) {
			listViewAdapter = new ItemViewAdapter(context, -1, -1, allTables);
			setListAdapter(listViewAdapter);
		} // End of if
	} // End of onResume

	@Override
	public void onBackPressed() {
		// Close the app when the phone's back button is pressed.
		finish();
	} // End of onBackPressed

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// When a list item is clicked, save the tag of the clicked item as the
		// selectedItem.
		selectedItem = Integer.parseInt(v.getTag().toString());
		// Register the context menu, open it, and close it.
		registerForContextMenu(v);
		openContextMenu(v);
		unregisterForContextMenu(v);
	} // End of onListItemClick

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// When the Context menu is being creates, add shop,modify, and delete
		// with their respective ids.
		menu.add(Menu.NONE, SHOP_ID, Menu.NONE, R.string.shop);
		menu.add(Menu.NONE, MODIFY_ID, Menu.NONE, R.string.modify);
		menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.delete);
	} // End of onCreateContextMenu Method

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
		// Create the AlertDialog and show it on the screen
		alertDialogBuilder.create().show();
	} // End of showAlertDialog

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// check what is the id of the context menu selected.
		switch (item.getItemId()) {
		case SHOP_ID:
			// If shop, call the shop activity, and send the selectedItem along.
			shopIntent.putExtra(SHOPPING_LIST_ID, selectedItem);
			startActivity(shopIntent);
			return true;
		case MODIFY_ID:
			// If modify, start the AddModify activity, and send the request and
			// the selectedItem to it.
			addModifyIntent.putExtra(CREATE_MODIFY, MODIFY_REQUEST);
			addModifyIntent.putExtra(SHOPPING_LIST_ID, selectedItem);
			startActivity(addModifyIntent);
			return true;
		case DELETE_ID:
			// If delete, show an AlertDialog to the user for him/her to decide
			// whether to delete the selected item.
			showAlertDialog(
					getString(R.string.delete),
					getString(R.string.delete_verify) + " "
							+ controller.getShoppingListNameById(selectedItem)
							+ "?", getString(R.string.delete),
					new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Delete the view from the database.
							controller
									.deleteAllShoppingListAndItemsByShoppingListId(selectedItem);
							// Delete a view
							deleteAView(selectedItem);
							// Call onResume to refresh the adapter
							onResume();
						} // End of onClick Method
					}); // End of onClickListener
			return true;
		} // End of Switch
		return super.onContextItemSelected(item);
	} // End of onContextItemSelected Method

	/**
	 * Delete a reference of the view with the given tag
	 * 
	 * @param tag
	 *            The tag that is used to identify if the view given is the
	 *            current one.
	 */
	private void deleteAView(int tag) {
		Iterator<TextView> it = allTextViews.iterator();
		while (it.hasNext()) {
			TextView currTextView = it.next();
			if (Integer.toString(tag).equals(currTextView.getTag())) {
				// Remove the current item in the iterator.
				it.remove();
			} // End of If
		} // End of While
	} // End of deleteAView method

	/**
	 * <p>
	 * <b>Date: </b>November 15, 2012
	 * </p>
	 * <p>
	 * <b>Description: </b>ItemViewAdapter is an adapter that adds textViews in
	 * the listView.
	 * </p>
	 */
	private class ItemViewAdapter extends ArrayAdapter<ArrayList<String>> {

		// All the values of the items.
		private List<ArrayList<String>> allItems;

		public ItemViewAdapter(Context context, int resource,
				int textViewResourceId, List<ArrayList<String>> items) {
			super(context, resource, textViewResourceId, items);
			// In the constructor, just make a reference of the item values.
			allItems = items;
		} // End of Constructor

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// When getting the view, check if the convertView is a new one.
			TextView item = (TextView) convertView;
			if (convertView == null) {
				// If new, create a new TextView and set the text and tag with
				// the one from allItems.
				Log.d("Debug", "Convert view is null");
				item = new TextView(context);
				item.setTag(allItems.get(position).get(0));
				item.setText(allItems.get(position).get(1));
				// Then add a reference of each textview.
				allTextViews.add(item);
			}
			return item;
		} // End of getView Method
	} // End of ItemViewAdapter class
} // End of ShoppingListApp class
