package com.raidrin.shoppinglist;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Shop extends ListActivity {

    private int shoppingListId;
	private TextView shoppingListNameTextView;
	private Controller controller;
	private Button doneButton;

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
    	// TODO Auto-generated method stub
    	super.onResume();
    	shoppingListId = getIntent().getIntExtra(ShoppingListApp.SHOPPING_LIST_ID, -1);
    	if(shoppingListId < 0)
    		throw new Error("No shopping list item selected.");
    	
    	shoppingListNameTextView.setText(controller.getShoppingNameById(shoppingListId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop, menu);
        return true;
    }
}
