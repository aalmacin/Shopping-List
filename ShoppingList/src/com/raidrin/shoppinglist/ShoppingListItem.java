package com.raidrin.shoppinglist;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
/**
* FileName: ShoppingListItem.java
*
* @author Aldrin Jerome Almacin
* <p>
* <b>Date: </b>November 15, 2012
* </p>
* <p>
* <b>Description: </b>ShoppingListItem is an object that is used to create/modify a new shopping list item.
* </p>
 */
public class ShoppingListItem extends TableRow {
	
	// The maximum quantity of an item
	protected static final int MAX_QUANTITY = 100;
	
	// The left margin of the decreaseImageButton
	private static final int LEFT_MARGIN = 20;
	
	// The item quantity
	private int quantity;
	
	// The name of the shoppingList item
	private String shoppingListName;
	
	// The EditText that takes the name of the shopping list
	private EditText shoppingListEditText;
	// The textview that shows the item quantity.
	private TextView quantityTextView;
	
	// Instances of the quantity changer objects. One decreaser and one increaser
	private QuantityChanger decreaseImageButton;
	private QuantityChanger increaseImageButton;
	
	/** 
	 * @param context The context passed to the ShoppingListItem
	 */
	public ShoppingListItem(Context context) {
		super(context);
		// Set the default name and quantity
		quantity = 0;
		shoppingListName = "";
		
	// Initialize the editText and set its values
    	shoppingListEditText = new EditText(context);
    	shoppingListEditText.setWidth((int)context.getResources().getDimension(R.dimen.itemSize));
    	shoppingListEditText.setHint(context.getText(R.string.enter_shopping_list_name));
    	shoppingListEditText.setImeOptions(EditorInfo.IME_ACTION_NONE);
    	
    	// Add a text changed listener that watches for the items entered into the editText
    	// The item inputted will be the name of the shopping list item
    	shoppingListEditText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {}
			
			public void afterTextChanged(Editable arg0) {
				shoppingListName = arg0.toString();
			} // End of afterTextChanged Method
		} // End of TextWatcher Anonymous inner class
		);

 	// Initialize the decreaseImageButton which is a QuantityChanger and send its value as DECREASER.
    	decreaseImageButton = new QuantityChanger(context,QuantityChanger.DECREASER);
    	// Set the appropriate values for decreaseImageButton QuantityChanger
    	decreaseImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.left));
    	decreaseImageButton.setOnClickListener(quantityArrowsClickListener);
    	TableRow.LayoutParams marginer = new TableRow.LayoutParams();
    	marginer.leftMargin = LEFT_MARGIN;
    	decreaseImageButton.setLayoutParams(marginer);
    	
 	// Initialize the decreaseImageButton which is a QuantityChanger and send its value as INCREASER.
    	increaseImageButton = new QuantityChanger(context,QuantityChanger.INCREASER);
    	// Set the appropriate values for increaseImageButton QuantityChanger
    	increaseImageButton.setOnClickListener(quantityArrowsClickListener);
    	increaseImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.right));
    	
    	// Set the quantityTextView's text to the appropriate one.
    	quantityTextView = new TextView(context);
		quantityTextView.setText(Integer.toString(quantity));
    	
    	// Add the views in this View.
    	this.addView(shoppingListEditText);
    	this.addView(decreaseImageButton);
    	this.addView(quantityTextView);
    	this.addView(increaseImageButton);
	} // End of Constructor
	
	/**
	 * Getter of the shoppingListName
	 */
	public String getShoppingListItemName() {
		return shoppingListName;
	} // End of Getter
	
	/**
	 * Setter of shoppingListName
	 */
	public void setShoppingListName(String shoppingList) {
		this.shoppingListName = shoppingList;
	} // End of Setter
	
	/**
	 * Getter or quantity
	 */
	public int getQuantity() {
		return quantity;
	} // End of Getter
	
	/**
	 * Setter of quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	} // End of setter
	
	// An anonymous inner class quantityArrowsClickListener is created.
	private OnClickListener quantityArrowsClickListener = new OnClickListener() {
			public void onClick(View v) 
			{
				// Check the type of QuantityChanger and if its INCREASER, increase the quantity
				// If its DECREASER, decrease the quantity
				switch(((QuantityChanger)v).getType())
				{
					case QuantityChanger.INCREASER:
						// If the quantity didn't reached max yet, add the quantity.
						quantity = (quantity < MAX_QUANTITY)?++quantity:quantity;
						break;
					case QuantityChanger.DECREASER:
						// If the quantity didn't reached 0 yet, reduce the quantity.
						quantity = (quantity > 0)?--quantity:quantity;
						break;
				} // End of Switch
				// Set the text of the quantity to the new value.
				quantityTextView.setText(Integer.toString(quantity));
			} // End of onClick method
		}; // End of quantityArrowsClickListener Anonymous inner class
		
	/**
	* <p>
	* <b>Date: </b>November 15, 2012
	* </p>
	* <p>
	* <b>Description: </b>QuantityChanger is the imageButton that allows you to increase/decrease the item's quantity
	* value depending on the QuantityChanger type.
	* </p>
	 */
	private class QuantityChanger extends ImageButton
	{
		// Types of Quantity Changer
		public static final int INCREASER = 1;
		public static final int DECREASER = 2;
		// The type of QuantityChanger
		private int type;
		
		/**
		 * @param cont The context that uses this object
		 * @param type The type of QuantityChanger. INCREASER/DECREASER
		 */
		public QuantityChanger(Context cont,int type) {
			super(cont);
			// Set the type to the one passed.
			this.setType(type);
			// if the type of Quantity changer is neither INCREASER nor DECREASER,
			// throw an Error
			if(type != INCREASER && type != DECREASER)
			{
				throw new Error("The type of quantity changer is not valid. Make sure to user either Quantity.INCREASER or Quantity.Decreaser");
			} // End of if
		} // End of Constructor

		/**
		 * Getter of type
		 */
		public int getType() {
			return type;
		} // End of Getter

		/**
		 * Setter of type
		 */
		public void setType(int type) {
			this.type = type;
		} // End of Setter
	} // End of Quantity Changer class
} // End of ShoppingListItem class
