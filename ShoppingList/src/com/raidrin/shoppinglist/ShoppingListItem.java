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

public class ShoppingListItem extends TableRow {
	protected static final int MAX_QUANTITY = 100;
	private static final int LEFT_MARGIN = 20;
	private int quantity;
	private String shoppingListName;
	private EditText shoppingListEditText;
	private TextView quantityTextView;
	private QuantityChanger decreaseImageButton;
	private QuantityChanger increaseImageButton;
	
	public ShoppingListItem(Context context) {
		super(context);
		quantity = 0;
		shoppingListName = "";
		
    	shoppingListEditText = new EditText(context);
    	shoppingListEditText.setWidth((int)context.getResources().getDimension(R.dimen.itemSize));
    	shoppingListEditText.setHint(context.getText(R.string.enter_shopping_list_name));
    	shoppingListEditText.setImeOptions(EditorInfo.IME_ACTION_NONE);
    	shoppingListEditText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {}
			
			public void afterTextChanged(Editable arg0) {
				shoppingListName = arg0.toString();
			}
		});


    	decreaseImageButton = new QuantityChanger(context,QuantityChanger.DECREASER);
    	decreaseImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.left));
    	decreaseImageButton.setOnClickListener(quantityArrowsClickListener);
    	TableRow.LayoutParams marginer = new TableRow.LayoutParams();
    	marginer.leftMargin = LEFT_MARGIN;
    	decreaseImageButton.setLayoutParams(marginer);
    	
    	increaseImageButton = new QuantityChanger(context,QuantityChanger.INCREASER);
    	increaseImageButton.setOnClickListener(quantityArrowsClickListener);
    	increaseImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.right));
    	
    	quantityTextView = new TextView(context);
		quantityTextView.setText(Integer.toString(quantity));
    	
    	this.addView(shoppingListEditText);
    	this.addView(decreaseImageButton);
    	this.addView(quantityTextView);
    	this.addView(increaseImageButton);
	}
	public String getShoppingListItemName() {
		return shoppingListName;
	}
	public void setShoppingListName(String shoppingList) {
		this.shoppingListName = shoppingList;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	private OnClickListener quantityArrowsClickListener = new OnClickListener() {
			public void onClick(View v) 
			{
				switch(((QuantityChanger)v).getType())
				{
					case QuantityChanger.INCREASER:
						quantity = (quantity < MAX_QUANTITY)?++quantity:quantity;
						break;
					case QuantityChanger.DECREASER:
						quantity = (quantity > 0)?--quantity:quantity;
						break;
				}
				quantityTextView.setText(Integer.toString(quantity));
			}
		};
	private class QuantityChanger extends ImageButton
	{
		public static final int INCREASER = 1;
		public static final int DECREASER = 2;
		private int type;
		
		public QuantityChanger(Context cont,int type) {
			super(cont);
			this.setType(type);
			if(type != INCREASER && type != DECREASER)
			{
				throw new Error("The type of quantity changer is not valid. Make sure to user either Quantity.INCREASER or Quantity.Decreaser");
			}
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
	}
}
