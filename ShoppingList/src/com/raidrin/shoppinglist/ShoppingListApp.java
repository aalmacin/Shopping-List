package com.raidrin.shoppinglist;


import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.Context;

public class ShoppingListApp extends Activity {

    private ImageButton addShoppingListImageButton;
	private Context context = this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	private OnClickListener addShoppingListListener = new OnClickListener(){
		public void onClick(android.view.View v) {
			
		};
	};
	
	
}
