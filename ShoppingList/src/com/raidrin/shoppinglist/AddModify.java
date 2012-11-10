package com.raidrin.shoppinglist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddModify extends Activity {

    private Button cancelButton;
	private Button saveButton;

	private Intent mainIntent;
	private Context context;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_modify);
        runtimeCreateRows();
        context = this;
        mainIntent = new Intent(context,ShoppingListApp.class);
        
        cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(mainIntent);				
			}
		});
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {				
				startActivity(mainIntent);
			}
		});
    }

    private void runtimeCreateRows() {
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_modify, menu);
        return true;
    }
}
