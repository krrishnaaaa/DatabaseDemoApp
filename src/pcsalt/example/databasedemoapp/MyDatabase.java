package pcsalt.example.databasedemoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabase implements DBConsts {
	
	private final String TAG = getClass().getSimpleName().toString();
	private final String DB_NAME = "dbname.db";		// .db is not compulsory
	private final int DB_VERSION = 1; 				// database version must be greater than or equal to 1
													// Increment DB_VERSION whenever database table is updated.
	
	private SQLiteDatabase mSQLiteDatabase;
	private MySQLiteOpenHelper mOpenHelper;
	
	public MyDatabase(Context context) {
		mOpenHelper = new MySQLiteOpenHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	public MyDatabase open() {
		mSQLiteDatabase = mOpenHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		mOpenHelper.close();
	}
	
	/*
	 * Always pass table name and values as arguments. 
	 * It would be useful when there is more than one table in application.
	 * 
	 * insert function returns the id of inserted row and -1 if it was not successful.
	 * */
	public long insert(String table, ContentValues values) {
		long id = mSQLiteDatabase.insert(table, null, values);
		return id;
	}
	
	
	/*
	 * Create different versions of select function : overload select() to match your requirement
	 * */
	public Cursor select(String table, String[] columns, String selection, String[] selectionArgs) {
		return select(table, columns, selection, selectionArgs, null, null, null);
	}
	
	public Cursor select(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		Cursor cur = mSQLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		return cur;
	}
	
	/*
	 * Update() returns the number of updated rows.
	 * */
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		int updated_rows = mSQLiteDatabase.update(table, values, whereClause, whereArgs);
		return updated_rows;
	}
	
	public int delete(String table, String whereClause, String[] whereArgs) {
		int deleted_rows = mSQLiteDatabase.delete(table, whereClause, whereArgs);
		return deleted_rows;
	}
	
	private class MySQLiteOpenHelper extends SQLiteOpenHelper {

		public MySQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// Create tables here
			String query = "CREATE TABLE " + IF_NOT_EXISTS + TBL_CONTACT + " ("
							+ ID + " INTEGER PRIMARY KEY,"
							+ NAME + " VARCHAR(30), "
							+ CONTACT + " VARCHAR(30))";
			try {
				db.execSQL(query);
			} catch(Exception e) {
				// in case of any exception : application must not crash
				Log.e(TAG, "Exception: " + e.getMessage());
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// When database is upgraded this method is called
			// call onCreate explicitly to modify the database with updated table(s).
			onCreate(db);
		}
		
	}

}
