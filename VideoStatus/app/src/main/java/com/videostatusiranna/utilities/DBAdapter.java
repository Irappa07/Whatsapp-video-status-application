package com.videostatusiranna.utilities;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBAdapter extends SQLiteOpenHelper {
	private static String DB_PATH;
	private static final String DB_NAME = "downloads.sqlite";

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	public DBAdapter(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	public void createDataBase() throws IOException{

		boolean dbExist = checkDataBase();

		if (dbExist) {

			Log.d("Database", "Exists");

		} else {
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {
				Log.d("Error", "Copy Database");
				throw new Error("Error copying database" + e);

			}
		}
	}

	private boolean checkDataBase() {
		DB_PATH = "/data/data/"+myContext.getPackageName()+"/databases/";
		Log.d("DB_PATH ", "" + DB_PATH);
		File dbFile = new File(DB_PATH + DB_NAME);
		Log.d("db", "" + dbFile);
		return dbFile.exists();
	}


	private void copyDataBase() throws IOException {
		DB_PATH = "/data/data/"+myContext.getPackageName()+"/databases/";
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		DB_PATH = "/data/data/"+myContext.getPackageName()+"/databases/";
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}






}
