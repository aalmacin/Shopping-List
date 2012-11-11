package com.raidrin.shoppinglist;

public class Item {
	private static final int SHOPPING_LIST = 0;

	private static final int SHOPPING_ITEM = 1;
	
	private int type;
	private String name;
	private int id;
	private int quantity;
	private int shoppingListId;
	private boolean selected;

	public Item(int id, String name) {
		setType(SHOPPING_LIST);
		setId(id);
		setName(name);
	}
	
	public Item(int id, String name,int quantity, int shoppingListId, boolean selected) {
		setType(SHOPPING_ITEM);
		setId(id);
		setName(name);
		setQuantity(quantity);
		setShoppingListId(shoppingListId);
		setSelected(selected);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getShoppingListId() {
		return shoppingListId;
	}

	public void setShoppingListId(int shoppingListId) {
		this.shoppingListId = shoppingListId;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
