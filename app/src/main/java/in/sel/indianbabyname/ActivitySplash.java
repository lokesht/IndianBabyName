package in.sel.indianbabyname;

import in.sel.dbhelper.DBHelper;
import in.sel.dbhelper.TableContract;
import in.sel.logging.AppLogger;
import in.sel.model.M_Name;
import in.sel.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class ActivitySplash extends Activity {
	String TAG = "ActivitySplash";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		new AsyncTask<Void, Integer, String>() {

			protected void onPreExecute() {

			};

			@Override
			protected String doInBackground(Void... params) {

				/** Copy full database from asset Folder to database Folder */
				copyDatabaseFromAsset();
				//insertValue();
				return "";
			}

			protected void onPostExecute(String result) {
				// writeDataBase();
				/**/
				Intent in = new Intent(ActivitySplash.this, ActivityAlphabetMain.class);
				startActivity(in);
				finish();
			};
		}.execute();
	}

	/**
	 * Copy Database from asset Folder to data directory
	 */
	public void copyDatabaseFromAsset() {

		/** Just to calculate time How much it will take to copy database */
		Utility t = new Utility();

		/* Insert Database */
		DBHelper db = new DBHelper(this);
		try {
			boolean dbExist = db.isDataBaseAvailable();

			if (!dbExist)
				db.copyDataBaseFromAsset();

		} catch (Exception e) {
			AppLogger.writeLog("state " + TAG + " -- " + e.toString());
			Log.e("", e.toString());
		}
		System.out.println(t.getTime(t));
		// }
	}
	
	/** Inser Value from text file to database*/
	public void insertValue() {
		// boolean isDrop = this.deleteDatabase(DatabaseHelper.DATABASE_NAME);
		Utility t = new Utility();
		
		/* Insert Database */
		DBHelper db = new DBHelper(this);
		// /db.executeStatement(dropDB);

		/* State Entry */
		int count = db.getTableRowCount(TableContract.Name.TABLE_NAME, null);
		if (count == 0) {
			try {
				System.out.println("Started");
			//	db.createDataBase();
				InputStream im = getAssets().open("BabyName");
				BufferedReader br = new BufferedReader(new InputStreamReader(im, "UTF-8"));
				String line = br.readLine();
				List<M_Name> lst = new ArrayList<M_Name>();
				do {
					String temp[] = line.split(",");
					String gender_cast = "";
					if(temp.length == 4)
						gender_cast = temp[3];
							
					M_Name s1 = new M_Name( temp[1],temp[0], Integer.parseInt(temp[2]),gender_cast);
					lst.add(s1);
					
					count++;
					if(count%5000==0)
					{
					   Log.i("Count", count+" - "+t.getTime(t));
					   }
				} while ((line = br.readLine()) != null);
				//db.insertName(lst);
				
				System.out.println(t.getTime(t));
				db.insertNameInsertHelperLock(lst);
			} catch (IOException e) {
				AppLogger.writeLog("state " + TAG + " -- " + e.toString());
				Log.e("", e.toString());
			}
			System.out.println(t.getTime(t));
		}
	}

}
