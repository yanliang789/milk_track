package yanliang.milk_track.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper dbHelper = null;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "MILK_TRACKER";
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + DBCode.data.TABLE_MILK + " ( "
            + DBCode.data._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + DBCode.data.LEFT_START_TIME + " INTEGER NOT NULL, "
            + DBCode.data.LEFT_END_TIME + " INTEGER NOT NULL, "
            + DBCode.data.RIGHT_START_TIME + " INTEGER NOT NULL, "
            + DBCode.data.RIGHT_END_TIME + " INTEGER NOT NULL, "
            + DBCode.data.SPLIT_DATA + " TEXT);";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public synchronized static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected void createTables(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

}
