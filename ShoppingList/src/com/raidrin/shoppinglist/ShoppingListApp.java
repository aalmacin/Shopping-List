package com.raidrin.shoppinglist;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class ShoppingListApp extends ListActivity {

	private static final String TAG_NAME = "Debug";
	private ImageButton addShoppingListImageButton;
	private Intent addModifyIntent;
	private Context context;
	private Controller controller;
	private ArrayList<HashMap<String,String>> allShoppingLists;
	private ScrollView androidListScrollView;
	private ListView mainAndroidListView;	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        androidListScrollView = (ScrollView)findViewById(R.id.androidListScrollView);
        addShoppingListImageButton = (ImageButton)findViewById(R.id.addShoppingListImageButton);
        allShoppingLists = new ArrayList<HashMap<String,String>>();
        controller = new Controller(context);

		mainAndroidListView = (ListView)androidListScrollView.getChildAt(0);
        context = this;
        addShoppingListImageButton.setOnClickListener(addShoppingListListener);
    }

	private OnClickListener addShoppingListListener = new OnClickListener(){
		public void onClick(android.view.View v) {
	        addModifyIntent = new Intent(context,AddModify.class);
			startActivity(addModifyIntent);
		};
	};


	protected void onResume() {
			super.onResume();
//			Log.e(TAG_NAME,"Resumed");
			allShoppingLists = controller.takeAllShoppingList();
			if(allShoppingLists.size() > 0)
			{
				ListAdapter simpleAdapter = new SimpleAdapter(this, allShoppingLists, R.layout.main, new String[]{Controller.LIST_NAME}, new int[]{mainAndroidListView.getId()});	
			}
		}
	}
