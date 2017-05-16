package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import adapters.Views;

/**
 * Created by user1 on 9/22/2016.
 */

public class DatabaseHandler {
    public static final String TABLE_ID = "id";
    public static final String VIEW_ID = "view_id";
    public static final String VIEW_COUNT = "view_count";


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "DealsWebDb.db";
    private static final int DATABASE_VERSION = 1;

    private static final String VIEW_TABLE = "ViewTable";

    private static final String CREATE_EMPLOYEES_TABLE = "create table "
            + VIEW_TABLE + " (" + TABLE_ID
            + " integer primary key autoincrement, "  + VIEW_ID + " text, "
            + VIEW_COUNT + " integer );";

    private final Context mCtx;




    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_EMPLOYEES_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + VIEW_TABLE);
            onCreate(db);
        }
    }
    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public DatabaseHandler(Context ctx) {
        mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }
    public DatabaseHandler open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void insertEmpDetails(Views viewData) {
        ContentValues cv = new ContentValues();
        Log.d("insert data db","--->"+viewData.getViewCount()+"..."+viewData.getViewId());

        cv.put(VIEW_ID, viewData.getViewId());
        cv.put(VIEW_COUNT, viewData.getViewCount());
        mDb.insert(VIEW_TABLE, null, cv);
    }


    public int retriveEmpDetails() throws SQLException
    {

        String selectQuery = "SELECT  * FROM " + VIEW_TABLE;
        Cursor cur = mDb.rawQuery(selectQuery, null);

        return cur.getCount();
    }
    public int retriveViewDetails1(String view_id)
    {

        Cursor cur = mDb.query(VIEW_TABLE, new String[]{VIEW_ID}, VIEW_ID+"=?", new String[] { view_id }, null, null, null);
       /* Log.d("get database move","--->"+cur.getCount());*/

         return cur.getCount();

    }

    //update record
    public void updateRecord(int viewId,int viewCount)
    {
        ContentValues cv= new ContentValues();
        cv.put(VIEW_ID,viewId);
        cv.put(VIEW_COUNT,viewCount);
        mDb.update(VIEW_TABLE, cv, VIEW_ID+"="+viewId, null);

    }
    //delete record
    public boolean deleteRecord() {
        return mDb.delete(VIEW_TABLE, null,null)>0;
    }
}
