package com.raidrin.shoppinglist;

public class Structure {
	public Structure() {
	}
	public static class shoppingList
	{
		public static int id;
		public static String name;
	}
	public static class items
	{
		public static int id;
		public static String name;
		public static int shoppingList_id;
		public static boolean selected;
	}
}
