package com.raidrin.shoppinglist;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class ShoppingListApp extends ListActivity {


	private static final String TAG_NAME = "Debug";
	private ImageButton addShoppingListImageButton;
	private Intent addModifyIntent;
	private Context context;
	private HashMap<String,String> allShoppingLists;
	private Controller controller;	
	private ArrayList<Item> items;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        addShoppingListImageButton = (ImageButton)findViewById(R.id.addShoppingListImageButton);
        addShoppingListImageButton.setOnClickListener(addShoppingListListener);
        
        context = this;
        allShoppingLists = new HashMap<String,String>();
        
        controller = new Controller(context);
		items = new ArrayList<Item>();
    }

	private OnClickListener addShoppingListListener = new OnClickListener(){
		public void onClick(android.view.View v) {
	        addModifyIntent = new Intent(context,AddModify.class);
			startActivity(addModifyIntent);
			onPause();
		};
	};


	protected void onResume() {
		super.onResume();
		SimpleCursorAdapter simpAdapter = new SimpleCursorAdapterExtension(context, 0, null, null, null);
		SimpleCursorAdapter simpleAdapter = new SimpleCursorAdapter(this, R.layout.shoppinglists_row, controller.takeShoppingListCursor(), new String[]{Controller.LIST_NAME}, new int[]{R.id.textView1});
		items = controller.takeAllShoppingList();
		setListAdapter(simpleAdapter);
	}

	private class SimpleCursorAdapterExtension extends SimpleCursorAdapter 
	{
		private SimpleCursorAdapterExtension(Context context, int layout,Cursor c, String[] from, int[] to) {
			super(context, layout, c, from, to);
			
		}@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			super.bindView(view, context, cursor);
		}
	}
}
