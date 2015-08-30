package in.sel.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import in.sel.exception.ValueNotInsertedException;
import in.sel.logging.AppLogger;
import in.sel.model.M_Name;
import in.sel.utility.AppConstants;
import in.sel.utility.Utility;

/**
 * @author lokesh.tiwari
 */

public class DBHelper extends SQLiteOpenHelper {

    /** */
    private String TAG = getClass().getName();

    /** */
    private SQLiteDatabase db;

    /**
     * If you change the database schema, you must increment the database version.
     */
    public static int DATABASE_VERSION = 1;
    public static final String DB_NAME = "BabyName.sqlite";

    public static final String DB_SUFFIX = "/databases/";
    private Context myContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;

        // DB_PATH = Environment.getExternalStorageDirectory()+"/Test/AndroidBabyName/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            this.db = db;
            // db.setVersion(2);
            db.execSQL(TableContract.FavourateName.SQL_CREATE);
            //   db.execSQL(TableContract.Name.SQL_CREATE);
            if (AppConstants.DEVELOER)
                Log.i(TAG, TableContract.FavourateName.SQL_CREATE + "Database created Successfully");
        } catch (Exception e) {
            if (AppConstants.DEBUG)
                Log.e(TAG, e.toString());
            // AppLogger.writeLog("Class Name --> DBHelper -- " + e.toString());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==2)
        db.execSQL(TableContract.FavourateName.SQL_CREATE);
    }


    /**
     * Identify Database is available or Not
     */
    public boolean isDataBaseAvailable() {
        try {
            String myPath = getDatabasePath();
            File f = new File(myPath);
            if (f.exists()) {
                return true;
            }

        } catch (SQLiteException e) {
            if (AppConstants.DEBUG)
                Log.e(TAG, e.toString() + "   database doesn't exists yet..");
        }
        return false;
    }

    /**
     * Give the Path of database
     */
    public String getDatabasePath() {
        return myContext.getApplicationInfo().dataDir + DB_SUFFIX + DB_NAME;
    }

    public void copyDataBaseFromAsset() throws Exception {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        OutputStream myOutput = null;
        try {

            String outFileName = getDatabasePath();

			/* if the path doesn't exist first, create it */
            File f = new File(myContext.getApplicationInfo().dataDir + DB_SUFFIX);
            if (!f.exists())
                f.mkdir();

			/* Open the empty db as the output stream */
            myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();

        } catch (Exception e) {
            throw e;
        } finally {
            myOutput.close();
            myInput.close();
        }

    }

    public boolean openDataBase() throws SQLException {
        String myPath = getDatabasePath();
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

        return db != null;
    }

    /**
     * Reset Table Identity column from 0
     */
    public void resetTable(String TableName) {
        try {
            db = getWritableDatabase();
            db.delete(TableName, null, null);
            String whereClause = "name" + " = " + "'" + TableName + "'";

            ContentValues cv = new ContentValues();
            cv.put("seq", 0);

            int rowId = db.update("sqlite_sequence", cv, whereClause, null);

            int s = (rowId < 0) ? Log.i(TAG, "Not reseted Table --> " + TableName) : Log.i(TAG, "Reseted Table --> "
                    + TableName);
        } catch (Exception e) {
            AppLogger.writeLog("Class Name --> DBHelper -- " + e.toString());
            if (AppConstants.DEBUG)
                Log.e("dbHelper", e.toString());
        } finally {
            db.close();
        }
    }

    /**
     * @param TableName
     * @param columns
     * @param where
     * @return Cursor
     */
    public Cursor getTableValue(String TableName, String[] columns, String where) {
        Cursor c = null;
        try {
            db = this.getReadableDatabase();
            c = db.query(TableName, columns, where, null, null, null, null, null);
        } catch (Exception e) {
            AppLogger.writeLog("Class Name --> DBHelper -- " + e.toString());
            if (AppConstants.DEBUG)
                Log.e(TAG, e.toString() + "--> getTableValue()");
        } finally {
            /** If database does not contain anything immediately close database */
            if (c == null)
                db.close();
        }
        return c;
    }

    /**
     * @param TableName
     * @param where
     * @return Cursor
     */
    public int deleteRow(String TableName, String where) {
        Cursor c = null;
        try {
            db = getWritableDatabase();
            int i = db.delete(TableName, where, null);
            return i;
        } catch (Exception e) {
            if (AppConstants.DEBUG)
                Log.e(TAG, e.toString() + "--> getTableValue()");
        } finally {
            /** If database does not contain anything immediately close database */
            if (c == null)
                db.close();
        }
        return 0;
    }

    /**
     * Get row count of Table
     *
     * @param tableName
     * @param where
     * @return
     */
    public int getTableRowCount(String tableName, String where) {
        int count = 0;
        try {
            db = getReadableDatabase();
            Cursor c = db.query(tableName, null, where, null, null, null, null);
            if (c != null) {
                count = c.getCount();
                c.close();
            }
        } catch (Exception e) {
            AppLogger.writeLog("Class Name --> " + TAG + " " + e.toString());
            if (AppConstants.DEBUG)
                Log.e(getClass().getName(), e.toString() + "-->");
        } finally {
            db.close();
        }

        if (AppConstants.DEBUG)
            Log.i(TAG, count + "-->");
        return count;
    }

    /**
     * Execute Row query
     *
     * @param sqlQuery
     */
    public void executeStatement(String sqlQuery) {
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {
            AppLogger.writeLog("Class Name --> " + TAG + " " + e.toString());
            if (AppConstants.DEBUG)
                Log.e(getClass().getName(), e.toString() + "-->");
        } finally {
            db.close();
        }
    }

    /**
     * Update Table
     *
     * @param table
     * @param cv
     * @param where
     * @return
     */
    public int updateTable(String table, ContentValues cv, String where) {
        db = getWritableDatabase();
        try {
            return db.update(table, cv, where, null);
        } catch (Exception e) {
            AppLogger.writeLog(TAG + e.toString());

            if (AppConstants.DEBUG)
                Log.e("updateTable", e.toString());

        } finally {
            db.close();
        }
        return -1;
    }

    /* Insert state */
    public long insertName(List<M_Name> lsData) {
        long rowid = 0;
        try {
            db = this.getWritableDatabase();

            for (M_Name obj : lsData) {
                ContentValues cv = new ContentValues();
                cv.put(TableContract.Name.NAME_EN, obj.getName_en());
                cv.put(TableContract.Name.NAME_MA, obj.getName_ma());
                cv.put(TableContract.Name.NAME_FRE, obj.getFrequency());

                rowid = db.insert(TableContract.Name.TABLE_NAME, TableContract.Name.NAME_EN, cv);

                if (rowid < 0) {
                    throw new ValueNotInsertedException();
                }
            }

        } catch (Exception e) {
            AppLogger.writeLog(TAG + e.toString());
            if (AppConstants.DEBUG)
                Log.e("insertName", e.toString());
        } finally {
            db.close();
        }
        return rowid;
    }

    /* Insert Saved Status of List */
    public long insertInTable(String tableName, String column, ContentValues cv) {
        long rowid = 0;
        try {
            db = this.getWritableDatabase();
            rowid = db.insert(tableName, column, cv);

            if (rowid < 0) {
                throw new ValueNotInsertedException();
            }

        } catch (Exception e) {
            if (AppConstants.DEBUG)
                Log.e("insertName", e.toString());
        } finally {
            db.close();
        }
        return rowid;
    }

    /**
     * Insert state
     */
    public long insertNameInsertHelperLock(List<M_Name> lsData) {
        long rowid = 0;
        db = getWritableDatabase();
        InsertHelper ih = new InsertHelper(getWritableDatabase(), TableContract.Name.TABLE_NAME);

        /** To measure Time for insertion */
        Utility t = new Utility();

        /** */
        final int nEn = ih.getColumnIndex(TableContract.Name.NAME_EN);
        final int nMa = ih.getColumnIndex(TableContract.Name.NAME_MA);
        final int nFre = ih.getColumnIndex(TableContract.Name.NAME_FRE);
        final int gender = ih.getColumnIndex(TableContract.Name.GENDER_CAST);
        try {
            // this.db.setLockingEnabled(false);
            db.beginTransaction();
            for (M_Name obj : lsData) {

                ih.prepareForInsert();
                ih.bind(nEn, obj.getName_en());
                ih.bind(nMa, obj.getName_ma());
                ih.bind(nFre, obj.getFrequency());
                ih.bind(gender, obj.getDescription());

                ih.execute();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            AppLogger.writeLog(TAG + e.toString());
            if (AppConstants.DEBUG)
                Log.e("insertName", e.toString());
        } finally {
            if (ih != null)
                ih.close();
            db.endTransaction();
            db.close();
            // this.db.setLockingEnabled(true);
        }
        if (AppConstants.DEBUG)
            Log.i("InsertTime", t.getTime(t));
        return rowid;
    }

}
