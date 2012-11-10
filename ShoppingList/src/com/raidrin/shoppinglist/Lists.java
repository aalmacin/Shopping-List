package com.raidrin.shoppinglist;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;

public class Lists extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lists, menu);
        return true;
    }
}
