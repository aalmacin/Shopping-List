package com.raidrin.shoppinglist;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AddModify extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_modify);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_modify, menu);
        return true;
    }
}
