package com.raidrin.shoppinglist;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
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
	private Cursor allShoppingListsCursor;
	private TextView addShoppingListTextView;
	private SimpleCursorAdapterExtension listViewAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		addShoppingListImageButton = (ImageButton) findViewById(R.id.addShoppingListImageButton);
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
		if(controller.checkIfAListExists())
		{
			addShoppingListTextView.setVisibility(View.VISIBLE);
		}
		else
		{
			addShoppingListTextView.setVisibility(View.INVISIBLE);
		}
		allShoppingListsCursor = controller.takeShoppingListCursor();
		listViewAdapter = new SimpleCursorAdapterExtension(this,
				R.layout.shoppinglists_row,
				allShoppingListsCursor ,
				new String[] { Controller.LIST_NAME },
				new int[] { R.id.textView1 });
		setListAdapter(listViewAdapter);
	}
	
	@Override
	public void onBackPressed() {}
	
	@Override
	protected void onPause() {
		super.onPause();
		allShoppingListsCursor.close();
	}

	private class SimpleCursorAdapterExtension extends SimpleCursorAdapter {

		public SimpleCursorAdapterExtension(Context context, int layout,
				Cursor c, String[] from, int[] to) {
			super(context, layout, c, from, to);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			super.bindView(view, context, cursor);
			view.setTag(cursor.getString(cursor.getColumnIndex(Controller.LIST_ID)));
		}
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
			allShoppingListsCursor.close();
			return true;
		case MODIFY_ID:
			addModifyIntent.putExtra(CREATE_MODIFY, MODIFY_REQUEST);
			addModifyIntent.putExtra(SHOPPING_LIST_ID, selectedItem);
	        startActivity(addModifyIntent);
			allShoppingListsCursor.close();
			return true;
		case DELETE_ID:
			showAlertDialog(getString(R.string.delete), getString(R.string.delete_verify)+" "+controller.getShoppingNameById(selectedItem)+"?", getString(R.string.delete),
					new AlertDialog.OnClickListener()
					{						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							controller.deleteTable(Controller.SHOPPING_LIST_TABLE,
									Controller.LIST_ID, selectedItem);
							listViewAdapter.notifyDataSetChanged();
							allShoppingListsCursor.close();
							onResume();
						}
					});
			return true;
		}
		return super.onContextItemSelected(item);
	}

}
