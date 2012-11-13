package com.raidrin.shoppinglist;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * FileName: AddModify.java
 * 
 * @author Aldrin Jerome Almacin
 * <p><b>Date: </b>November 11, 2012 </p>
 * <p><b>Description: </b>AddModify is a class that extends Activity which
 *           lets the user add an item, modify an existing one, give the list a
 *           name/modify an existing one, and save the given changes into the
 *           database.</p>
 * 
 */
public class AddModify extends Activity {

	private static final int MAX_ITEMS = 20; // The maximum number of items in a single shopping list.

	private Context context; // Used to save a copy of the context so that it can be used throughout the class.
	
	private TableLayout shoppingListTableLayout;
	
	private EditText shoppingListEditText;
	
	private Button cancelButton; // The button used to cancel the AddModify activity.
	private Button saveButton; // The button used to allow the saving of the shopping list items
	
	private Controller controller; // The controller which is used to get processed information from the database.

	private TextView modifyCreateTextView; // The TextView that shows
	private String shoppingListName;
	private boolean createMode;
	private int shoppingListId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_modify);
		createMode = true;
		context = this;
		controller = new Controller(context);

		modifyCreateTextView = (TextView) findViewById(R.id.modifyCreateTextView);
		shoppingListEditText = (EditText) findViewById(R.id.shoppingListEditText);
		shoppingListEditText.selectAll();
		shoppingListTableLayout = (TableLayout) findViewById(R.id.shoppingListTableLayout);

		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				shoppingListId = getIntent().getIntExtra(
						ShoppingListApp.SHOPPING_LIST_ID, -1);
				shoppingListName = shoppingListEditText.getText().toString();
				if (!createMode) {
					controller
							.updateTableName(shoppingListId, shoppingListName);
					controller
							.deleteAllShoppingListItemsByShoppingId(controller
									.getShoppingIdByValue(shoppingListName));
				}
				boolean itemsAddedSuccesfully = false;

				if (!shoppingListEditText.getText().toString().equals(""))
					itemsAddedSuccesfully = controller
							.createOrAddItemsInShoppingList(shoppingListName,
									MAX_ITEMS, shoppingListTableLayout,
									createMode);

				if (itemsAddedSuccesfully)
					finish();
				else
					showAlertDialog(getString(R.string.shopping_list_error),
							getString(R.string.you_need_at_least),
							getString(R.string.fix),
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
			}
		});
		runtimeCreateRows();

	}

	@Override
	public void onBackPressed() {
	}

	/**
	 * The AlertDialog that will be shown on the screen is created and shown.
	 * 
	 * @param title
	 *            The title of the AlertDialog box
	 * @param message
	 *            The message in the AlertDialog box
	 * @param buttonText
	 *            The text inside the OK button
	 * @param buttonOkListener
	 *            the listener for the OK button
	 */
	private void showAlertDialog(String title, String message,
			String buttonText, AlertDialog.OnClickListener buttonOkListener) {
		// Get the AlertDialog Builder class and save it in a variable.
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// Set the title, message, OK button, and the cancel listener of the
		// AlertDialog
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(message);

		// Positive button is using the buttonOKListener which is passed as a
		// parameter
		// as its event listener.
		alertDialogBuilder.setPositiveButton(buttonText, buttonOkListener);

		// set the cancel listener of the AlertDialog
		// Cancel is when the user pressed the back key in his/her phone.
		// alertDialogBuilder.setOnCancelListener(new
		// DialogInterface.OnCancelListener()
		// {
		// // The onCancel method is called when the back key is pressed.
		// @Override
		// public void onCancel(DialogInterface dialog) {
		// } // End of onCancel method
		// }); // End of OnCancelListener
		// Create the AlertDialog and show it on the screen
		alertDialogBuilder.create().show();
	} // End of showAlertDialog

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		switch (getIntent().getIntExtra(ShoppingListApp.CREATE_MODIFY, -1)) {
		case ShoppingListApp.CREATE_REQUEST:
			modifyCreateTextView.setText(getString(R.string.create_new_list));
			createMode = true;
			break;
		case ShoppingListApp.MODIFY_REQUEST:
			createMode = false;
			fillUpForms();
			modifyCreateTextView.setText(getString(R.string.modify_list));
			break;
		}
	}

	private void fillUpForms() {
		int shoppingListId = getIntent().getIntExtra(
				ShoppingListApp.SHOPPING_LIST_ID, -1);
		controller.fillUpShoppingItems(shoppingListId, shoppingListEditText,
				shoppingListTableLayout);
	}

	private void runtimeCreateRows() {
		for (int i = 0; i < MAX_ITEMS; i++) {
			shoppingListTableLayout.addView(new ShoppingListItem(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_modify, menu);
		return true;
	}
}
