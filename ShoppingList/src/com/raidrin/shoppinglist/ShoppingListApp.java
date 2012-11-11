package com.raidrin.shoppinglist;


import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ShoppingListApp extends Activity {

    private ImageButton addShoppingListImageButton;
	private Intent addModifyIntent;
	private Context context;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addShoppingListImageButton = (ImageButton)findViewById(R.id.addShoppingListImageButton);
        context = this;
        addShoppingListImageButton.setOnClickListener(addShoppingListListener);
    }

	private OnClickListener addShoppingListListener = new OnClickListener(){
		public void onClick(android.view.View v) {
	        addModifyIntent = new Intent(context,AddModify.class);
			startActivity(addModifyIntent);
		};
	};
	
	
}
