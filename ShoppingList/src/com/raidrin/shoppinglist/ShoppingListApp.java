package com.raidrin.shoppinglist;

import java.util.ArrayList;
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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

public class ShoppingListApp extends ListActivity {

	private static final String TAG_NAME = "Debug";
	private static final int SHOP_ID = 0;
	private static final int MODIFY_ID = 1;
	private static final int DELETE_ID = 2;
	public static final int MODIFY_REQUEST = 1;
	public static final int CREATE_REQUEST = 2;
	public static final String CREATE_MODIFY = "TheCreateModiFyRequest";
	public static final String SHOPPING_LIST_ID = "TheShoppingListName";
	private ImageButton addShoppingListImageButton;
	private Intent addModifyIntent;
	private Intent shopIntent;
	private Context context;
	private Controller controller;
	private int selectedItem;
//	private Cursor allShoppingListsCursor;
	private TextView addShoppingListTextView;
	private ItemViewAdapter listViewAdapter;
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		addShoppingListImageButton = (ImageButton) findViewById(R.id.addShoppingListImageButton);
		listView = (ListView)findViewById(android.R.id.list);
		addShoppingListImageButton.setOnClickListener(addShoppingListListener);
		addShoppingListTextView = (TextView)findViewById(R.id.addShoppingListTextView);

		context = this;

		controller = new Controller(context);
		addModifyIntent = new Intent(context, AddModify.class);
		shopIntent = new Intent(this,Shop.class);
	}

	private OnClickListener addShoppingListListener = new OnClickListener() {
		public void onClick(android.view.View v) {
			addModifyIntent.putExtra(CREATE_MODIFY, CREATE_REQUEST);
	        startActivity(addModifyIntent);
			onPause();
		};
	};

	protected void onResume() {
		super.onResume();
		if(!controller.checkIfAShoppingListExists())
		{
			addShoppingListTextView.setVisibility(View.VISIBLE);
		}
		else
		{
			addShoppingListTextView.setVisibility(View.INVISIBLE);
		}

		ArrayList<ArrayList<String>> allTables = controller.getAllShoppingLists();
		if(allTables != null)
		{
			listViewAdapter = new ItemViewAdapter(context,-1,-1,allTables);
			setListAdapter(listViewAdapter);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		selectedItem = Integer.parseInt(v.getTag().toString());
		registerForContextMenu(v);
		openContextMenu(v);
		unregisterForContextMenu(v);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, SHOP_ID, Menu.NONE, R.string.shop);
		menu.add(Menu.NONE, MODIFY_ID, Menu.NONE, R.string.modify);
		menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.delete);
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
		alertDialogBuilder.setNegativeButton(cancelButtonText , new AlertDialog.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
			
		});
		// Create the AlertDialog and show it on the screen
		alertDialogBuilder.create().show();
	} // End of showAlertDialog

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SHOP_ID:
			shopIntent.putExtra(SHOPPING_LIST_ID, selectedItem);
			startActivity(shopIntent);
			return true;
		case MODIFY_ID:
			addModifyIntent.putExtra(CREATE_MODIFY, MODIFY_REQUEST);
			addModifyIntent.putExtra(SHOPPING_LIST_ID, selectedItem);
	        startActivity(addModifyIntent);
			return true;
		case DELETE_ID:
			showAlertDialog(getString(R.string.delete), getString(R.string.delete_verify)+" "+controller.getShoppingListNameById(selectedItem)+"?", getString(R.string.delete),
					new AlertDialog.OnClickListener()
					{						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							controller.deleteAllShoppingListAndItemsByShoppingListId(selectedItem);
						}
					});
			onResume();
			return true;
		}
		return super.onContextItemSelected(item);
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
			TextView item = new TextView(context);
			item.setTag(allItems.get(position).get(0));
			item.setText(allItems.get(position).get(1));
			return item;
		}
	}

}
