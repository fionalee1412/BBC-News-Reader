package com.digitallizard.bbcnewsreader.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {
	
	/** constants **/
	public static final String AUTHORITY = "com.digitallizard.bbcnewsreader";
	public static final Uri CONTENT_URI = Uri.parse("content://com.digitallizard.bbcnewsreader");
	public static final Uri CONTENT_URI_CATEGORIES = Uri.parse("content://com.digitallizard.bbcnewsreader/categories");
	public static final Uri CONTENT_URI_ITEMS = Uri.parse("content://com.digitallizard.bbcnewsreader/items");
	
	//uri matcher helpers
	private static final int CATEGORIES = 1;
	private static final int ENABLED_CATEGORIES = 2;
	private static final int ITEMS = 4;
	private static final int ITEM_BY_ID = 5;
	private static final int ITEMS_BY_CATEGORY = 3;
	private static final int UNDOWNLOADED_ITEMS = 6;
	
	//uri matcher
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		uriMatcher.addURI(AUTHORITY, "categories", CATEGORIES);
		uriMatcher.addURI(AUTHORITY, "categories/enabled", ENABLED_CATEGORIES);
		uriMatcher.addURI(AUTHORITY, "items/", ITEMS);
		uriMatcher.addURI(AUTHORITY, "items/#", ITEM_BY_ID);
		uriMatcher.addURI(AUTHORITY, "items/category/*", ITEMS_BY_CATEGORY);
		uriMatcher.addURI(AUTHORITY, "items/undownloaded/*", UNDOWNLOADED_ITEMS);
	}
	
	
	/** variables **/
	DatabaseHelper database;
	
	
	private Cursor getCategories(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return database.query(DatabaseHelper.CATEGORY_TABLE, projection, selection, selectionArgs, sortOrder);
	}
	
	private Cursor getEnabledCategories(String[] projection, String sortOrder) {
		//define a selection to only retrieve enabled categories
		String selection = "enabled='1'";
		//ask for categories by this selection
		return getCategories(projection, selection, null, sortOrder);
	}
	
	private Cursor getItems(String[] projection, String selection, String[] selectionArgs, String sortOrder){
		//get items
		return database.query(DatabaseHelper.ITEM_TABLE, projection, selection, selectionArgs, sortOrder);
	}
	
	private Cursor getItem(String[] projection, int id) {
		String selection = DatabaseHelper.COLUMN_ITEM_ID;
		String[] selectionArgs = new String[] {Integer.toString(id)};
		return database.query(DatabaseHelper.ITEM_TABLE, projection, selection, selectionArgs, null);
	}
	
	private Cursor getItems(String[] projection, String category, String sortOrder){
		//get items in this category
		return null;
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		//try and match the queried url
		switch(uriMatcher.match(uri)){
		case CATEGORIES:
			//query the database for all the categories
			if(selection == null){
				throw new IllegalArgumentException("Uri requires selection: " + uri.toString());
			}
			return getCategories(projection, selection, selectionArgs, sortOrder);
		case ENABLED_CATEGORIES:
			//query the database for enabled categories
			if(selection == null){
				throw new IllegalArgumentException("Uri requires selection: " + uri.toString());
			}
			return getEnabledCategories(projection, sortOrder);
		case ITEMS:
			//query the database for items
			return this.getItems(projection, selection, selectionArgs, sortOrder);
		case ITEM_BY_ID:
			//query the database for this specific item
			int id = Integer.parseInt(uri.getLastPathSegment());
			return getItem(projection, id);
		case ITEMS_BY_CATEGORY:
			//query the database for items in this category
			String category = uri.getLastPathSegment();
			return getItems(projection, category, sortOrder);
		case UNDOWNLOADED_ITEMS:
			//query the database for undownloaded items of this type
			
		default:
			throw new IllegalArgumentException("Unknown uri: " + uri.toString());
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean onCreate() {
		//initialise the database
		database = new DatabaseHelper(this.getContext());
		
		return false;
	}
	
	public DatabaseProvider() {
		// TODO Auto-generated constructor stub
	}

}
