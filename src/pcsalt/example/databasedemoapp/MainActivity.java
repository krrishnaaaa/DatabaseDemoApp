package pcsalt.example.databasedemoapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, DBConsts {

	EditText etName, etContact;
	TextView tvOutput;
	Button btnInsert, btnUpdate, btnDelete, btnSelect;
	
	final String TAG = getClass().getSimpleName().toString();
	final Context context = MainActivity.this;
	// Create database object
	MyDatabase mDB = new MyDatabase(context);

	ContentValues values = new ContentValues();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		etName		= (EditText) findViewById(R.id.etName);
		etContact	= (EditText) findViewById(R.id.etContact);
		
		tvOutput	= (TextView) findViewById(R.id.tvOutput);
		
		btnInsert	= (Button)	 findViewById(R.id.btnInsert);
		btnUpdate	= (Button)	 findViewById(R.id.btnUpdate);
		btnDelete	= (Button)	 findViewById(R.id.btnDelete);
		btnSelect	= (Button)	 findViewById(R.id.btnSelect);
		
		// Add listeners to button
		btnInsert.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnSelect.setOnClickListener(this);
		
		// Open database connection in onCreate()
		mDB.open();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Close database connection when application is destroyed
		mDB.close();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnInsert:
			insert();
			break;
		case R.id.btnUpdate:
			update();
			break;
		case R.id.btnDelete:
			delete();
			break;
		case R.id.btnSelect:
			select();
			break;
		}
	}
	
	public void insert() {
		// Insert statement uses ContentValues type to store column values
		// in ContentValues: key => column name & value => column data
		values.put(NAME, getName());
		values.put(CONTACT, getContact());
		
		long id = mDB.insert(TBL_CONTACT, values);
		output("inserted row id: " + id);
		iLog("inserted row id: " + id);
	}
	
	public void update() {
		// the ? will be replaced by the whereArgs element
		// So always keep matching string for whereClause and whereArgs
		String whereClause = NAME + "=?";
		String[] whereArgs = new String[] { getName() };
		
		values.put(CONTACT, getContact());
		
		int updated_rows = mDB.update(TBL_CONTACT, values, whereClause, whereArgs);
		output("Number of updated rows: " + updated_rows);
		iLog("Number of updated rows: " + updated_rows);
	}
	
	public void delete() {
		String whereClause = NAME + "=?" + AND + CONTACT + "=?";
		String[] whereArgs = new String[] {
				getName(),
				getContact()
		};
		int deleted_rows = mDB.delete(TBL_CONTACT, whereClause, whereArgs);
		output("Number of deleted rows: " + deleted_rows);
		iLog("Number of deleted rows: " + deleted_rows);
	}
	
	public void select() {
		StringBuilder result = new StringBuilder();
		int id;
		String name, contact;
		
		String[] columns = new String[] {
				ID,
				NAME,
				CONTACT
		};
		
		String selection = null; 	// no selection
		String[] selectionArgs = null;	// not needed
		
		Cursor cur = mDB.select(TBL_CONTACT, columns, selection, selectionArgs);
		//check if cursor returned from select statement is not null
		if(cur != null) {
			while(cur.moveToNext()) {
				id = cur.getInt(cur.getColumnIndex(ID));
				name = cur.getString(cur.getColumnIndex(NAME));
				contact = cur.getString(cur.getColumnIndex(CONTACT));
				result.append("[id: " + id + "]\t[name: " + name + "]\t[contact: " + contact + "]\n");
			}
		} else {
			result.append("No result found");
		}
		output(result);
		iLog(result);
	}
	
	public String getName() {
		return etName.getText().toString();
	}
	
	public String getContact() {
		return etContact.getText().toString();
	}
	
	// used for LogCat output
	public void iLog(Object message) {
		Log.i(TAG, message.toString());
	}
	
	public void output(Object message) {
		tvOutput.setText(message.toString());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
