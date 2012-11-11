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
	private ScrollView androidListScrollView;
	private ListView mainAndroidListView;
	private Controller controller;	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        androidListScrollView = (ScrollView)findViewById(R.id.androidListScrollView);
        
        addShoppingListImageButton = (ImageButton)findViewById(R.id.addShoppingListImageButton);
        addShoppingListImageButton.setOnClickListener(addShoppingListListener);
        
        context = this;
        allShoppingLists = new HashMap<String,String>();
        
		mainAndroidListView = (ListView)androidListScrollView.getChildAt(0);
        controller = new Controller(context);
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
//		allShoppingLists = controller.takeAllShoppingList();
		CursorAdapter cursorAdapter = new CursorAdapter(context, controller.takeShoppingListCursor()) {
			
			@Override
			public View newView(Context context, Cursor cursor, ViewGroup parent) {
				return new TextView(context);
			}
			
			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				view.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Controller.LIST_ID))));
			}
		};
		setListAdapter(cursorAdapter);
//		ArrayList<String> names = new ArrayList<String>();
//		for(int i=0;i<allShoppingLists.size();i++){
//			int setID = (int)'t'+'e'+'m'+'p'+'T'+'e'+'x'+'t'+'V'+'i'+'e'+'w'+i;
////				names.add(allShoppingLists.get(Controller.LIST_ID));
//			TextView tempTextView = new TextView(context);
//			tempTextView.setText(allShoppingLists.get(Controller.LIST_ID));
//			tempTextView.setId(setID);
//			mainAndroidListView.addView(tempTextView);
////				setListAdapter(new ArrayAdapter<String>(this,setID,new String[]{allShoppingLists.get(Controller.LIST_ID)}));
//		}
//			((ArrayAdapter<Object>) mainAndroidListView.getAdapter()).notifyDataSetChanged();
//			setContentView(R.layout.main);
//			ListAdapter simpleAdapter = new SimpleAdapter(this, allShoppingLists, R.layout.main, new String[]{Controller.LIST_NAME}, new int[]{mainAndroidListView.getId()});
//			setListAdapter();
	}
}
