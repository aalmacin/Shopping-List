package com.raidrin.shoppinglist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
public class AddModify extends Activity {

    private static final int MAX_ITEMS = 20;
	private static final String TAG_NAME = "Debug";
	private Button cancelButton;
	private Button saveButton;

	private Intent mainIntent;
	private Context context;
	private TableLayout shoppingListTableLayout;
	private EditText shoppingListEditText;
	private Controller controller;
	private TextView modifyCreateTextView;
	private boolean modify;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_modify);
        context = this;
		controller = new Controller(context);

		modifyCreateTextView = (TextView) findViewById(R.id.modifyCreateTextView);
        shoppingListEditText = (EditText) findViewById(R.id.shoppingListEditText);
        shoppingListEditText.selectAll();
    	shoppingListTableLayout = (TableLayout)findViewById(R.id.shoppingListTableLayout);
    	
        mainIntent = new Intent(context,ShoppingListApp.class);
        
        cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(mainIntent);
				onDestroy();
			}
		});
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String shoppingListName = shoppingListEditText.getText().toString();
				controller.createOrAddItemsInShoppingList(shoppingListName, MAX_ITEMS, shoppingListTableLayout);
				startActivity(mainIntent);
			}
		});

        runtimeCreateRows();
        
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG_NAME, "this method is runned: "+getIntent().getIntExtra(ShoppingListApp.CREATE_MODIFY,-1));
		switch(getIntent().getIntExtra(ShoppingListApp.CREATE_MODIFY,-1))
		{
			case ShoppingListApp.CREATE_REQUEST:
				modifyCreateTextView.setText(getString(R.string.create_new_list));
				Log.d(TAG_NAME, "CREATE the list");
				break;
			case ShoppingListApp.MODIFY_REQUEST:
				Log.d(TAG_NAME, "MODIFY the list");
				modifyCreateTextView.setText(getString(R.string.modify_list));
				break;
		}
	}
	
    private void runtimeCreateRows() {
    	for(int i=0;i<MAX_ITEMS;i++)
    	{
    		shoppingListTableLayout.addView(new ShoppingListItem(this));
    	}
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_modify, menu);
        return true;
    }
}
