package com.raidrin.shoppinglist;

import java.util.ArrayList;
import java.util.Currency;

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
import android.app.ListActivity;
import android.content.Context;
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
	private ImageButton addShoppingListImageButton;
	private Intent addModifyIntent;
	private Context context;
	private Controller controller;
	private int selectedItem;
	private Cursor allShoppingListsCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		addShoppingListImageButton = (ImageButton) findViewById(R.id.addShoppingListImageButton);
		addShoppingListImageButton.setOnClickListener(addShoppingListListener);

		context = this;

		controller = new Controller(context);
		addModifyIntent = new Intent(context, AddModify.class);
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
		allShoppingListsCursor = controller.takeShoppingListCursor();
		SimpleCursorAdapterExtension simpleAdapter = new SimpleCursorAdapterExtension(this,
				R.layout.shoppinglists_row,
				allShoppingListsCursor ,
				new String[] { Controller.LIST_NAME },
				new int[] { R.id.textView1 });
		setListAdapter(simpleAdapter);
	}
	
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
		Log.d(TAG_NAME, "databaseID: "+v.getTag());	
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SHOP_ID:
			Log.d(TAG_NAME, "Shop onContextItemSelected.");
			return true;
		case MODIFY_ID:
			addModifyIntent.putExtra(CREATE_MODIFY, MODIFY_REQUEST);
	        startActivity(addModifyIntent);
			onPause();
			Log.d(TAG_NAME, "Modify onContextItemSelected.");
			return true;
		case DELETE_ID:
			controller.delete(Controller.SHOPPING_LIST_TABLE,
					Controller.LIST_ID, selectedItem);
			onResume();
			return true;
		}
		return super.onContextItemSelected(item);
	}

}
