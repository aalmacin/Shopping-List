package com.raidrin.shoppinglist;


import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

public class Shop extends ListActivity {

	private int shoppingListId;
	private TextView shoppingListNameTextView;
	private Controller controller;
	private Button doneButton;
	private SimpleCursorAdapter listViewAdapter;
	private Cursor shoppingListCursor;
	private static final String TAG_NAME = "Debug";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);
		controller = new Controller(this);
		shoppingListNameTextView = (TextView) findViewById(R.id.shoppingListNameTextView);
		doneButton = (Button) findViewById(R.id.done_button);
		doneButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		shoppingListId = getIntent().getIntExtra(
				ShoppingListApp.SHOPPING_LIST_ID, -1);
		if (shoppingListId < 0)
			throw new Error("No shopping list item selected.");

		shoppingListNameTextView.setText(controller
				.getShoppingNameById(shoppingListId));
		shoppingListCursor = controller
				.getAllNameAndQuantityCursor(shoppingListId);
		listViewAdapter = new ItemViewAdapter(this, R.layout.shop, shoppingListCursor, new String[] {}, new int[] {});
		setListAdapter(listViewAdapter);
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		shoppingListCursor.close();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shop, menu);
		return true;
	}
	
	private class ShoppingItem extends LinearLayout
	{
		
		private boolean bought;

		public ShoppingItem(Context context, String name, int quantity) 
		{
			super(context);
			setBought(false);
			this.setOrientation(HORIZONTAL);
			TextView itemNameTextView = new TextView(context);
			itemNameTextView.setWidth((int)context.getResources().getDimension(R.dimen.itemSize));
			itemNameTextView.setTextSize(((int)context.getResources().getDimension(R.dimen.show_text_size)));
			itemNameTextView.setPadding(((int)context.getResources().getDimension(R.dimen.default_padding)), 0, 0, 0);
			itemNameTextView.setText(name);
			
			TextView itemQuantityTextView = new TextView(context);
			itemQuantityTextView.setText(Integer.toString(quantity));
			itemQuantityTextView.setTextSize(((int)context.getResources().getDimension(R.dimen.show_text_size)));
			itemQuantityTextView.setWidth((int)context.getResources().getDimension(R.dimen.quantity_width));
			
			CheckBox checkBox = new CheckBox(context);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					setBought((isChecked)?true:false);
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
		
		
	}
	
	private class ItemViewAdapter extends SimpleCursorAdapter{
		public ItemViewAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
		}
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			String name = cursor.getString(cursor.getColumnIndex(Controller.ITEM_NAME));
			int quantity = cursor.getInt(cursor.getColumnIndex(Controller.ITEM_QUANTITY));
			ShoppingItem item = new ShoppingItem(context, name, quantity);
			item.setTag(cursor.getString(cursor.getColumnIndex(Controller.ITEM_ID)));
			return item;
		}
	}
}
