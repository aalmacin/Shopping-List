package com.raidrin.shoppinglist;

import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.app.Activity;
import android.database.Cursor;

public class ShoppingListApp extends Activity {

    private ImageButton addShoppingListImageButton;
	private ShoppingListSQLHelper shoppingListSQLHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        shoppingListSQLHelper = new ShoppingListSQLHelper(this, SQLLiteInfo.DATABASE_NAME, null, SQLLiteInfo.DATABASE_VERSION);
        addShoppingListImageButton = (ImageButton)findViewById(R.id.addShoppingListImageButton);
        addShoppingListImageButton.setOnClickListener(addShoppingListListener);
    }
	
	private OnClickListener addShoppingListListener = new OnClickListener(){
		public void onClick(android.view.View v) {
			Log.e("Debug", "Before create");
			shoppingListSQLHelper.createNewList("CREATE TABLE sample (id INT NOT NULL PRIMARY KEY AUTOINCREMENT);");
			Log.e("Debug", "After create");
//			String[] columns = new String[]{"id"};
//			Cursor cursor = shoppingListSQLHelper.getWritableDatabase().query("sample", columns, null, null, null, null, null);
		};
	};
	
	
}
