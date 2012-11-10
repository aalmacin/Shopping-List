package com.raidrin.shoppinglist;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

public class ShoppingListItem extends TableRow {
	protected static final int MAX_QUANTITY = 100;
	private int quantity;
	private String shoppingList;
	private Context context;
	private EditText shoppingListName;
	private TextView quantityTextView;
	private QuantityChanger decreaseImageButton;
	private QuantityChanger increaseImageButton;
	
	public ShoppingListItem(Context context) {
		super(context);
		this.context = context;
		quantity = 0;
		shoppingList = "";
		
    	shoppingListName = new EditText(context);

    	increaseImageButton = new QuantityChanger(context,QuantityChanger.INCREASER);
    	increaseImageButton.setOnClickListener(quantityArrowsClickListener);
    	increaseImageButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.right));

    	decreaseImageButton = new QuantityChanger(context,QuantityChanger.DECREASER);
    	decreaseImageButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.left));
    	decreaseImageButton.setOnClickListener(quantityArrowsClickListener);
    	
    	quantityTextView = new TextView(context);
    	
    	this.addView(shoppingListName);
    	this.addView(increaseImageButton);
    	this.addView(quantityTextView);
    	this.addView(decreaseImageButton);
	}
	public String getShoppingList() {
		return shoppingList;
	}
	public void setShoppingList(String shoppingList) {
		this.shoppingList = shoppingList;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	private OnClickListener quantityArrowsClickListener = new OnClickListener() {
		public void onClick(View v) {
			switch(v.getId())
			{
				case QuantityChanger.INCREASER:
					quantity = (quantity<= MAX_QUANTITY)?quantity++:quantity;
					break;
				case QuantityChanger.DECREASER:
					quantity--;
					break;
			}
				quantityTextView.setText(Integer.toString(quantity));
			}
		};
	private class QuantityChanger extends ImageButton
	{
		public static final int INCREASER = 1;
		public static final int DECREASER = 2;
		
		public QuantityChanger(Context cont,int type) {
			super(cont);
			if(type != INCREASER && type != DECREASER)
			{
				throw new Error("The type of quantity changer is not valid. Make sure to user either Quantity.INCREASER or Quantity.Decreaser");
			}
		}
	}
}
