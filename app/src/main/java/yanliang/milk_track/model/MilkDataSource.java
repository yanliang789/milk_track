package yanliang.milk_track.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import yanliang.milk_track.database.DBCode;
import yanliang.milk_track.database.DBHelper;

public class MilkDataSource {
    private DBHelper mDBHelper;
    private SQLiteDatabase database;
    private String[] allColumns = { DBCode.data._ID,
            DBCode.data.LEFT_START_TIME,
            DBCode.data.LEFT_END_TIME,
            DBCode.data.RIGHT_START_TIME,
            DBCode.data.RIGHT_END_TIME,
            DBCode.data.SPLIT_DATA};

    public MilkDataSource(Context context) {
        mDBHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = mDBHelper.getWritableDatabase();
    }

    public void close() {
        mDBHelper.close();
    }

    public MilkRecord createRecord(long leftStartTime, long leftEndTime, long rightStartTime, long rightEndTime, String splitData) throws JSONException{
        ContentValues values = new ContentValues();
        values.put(DBCode.data.LEFT_START_TIME, leftStartTime);
        values.put(DBCode.data.LEFT_END_TIME, leftEndTime);
        values.put(DBCode.data.RIGHT_START_TIME, rightStartTime);
        values.put(DBCode.data.RIGHT_END_TIME, rightEndTime);
        values.put(DBCode.data.SPLIT_DATA, splitData);

        long insertId = database.insert(DBCode.data.TABLE_MILK, null,
                values);

        Cursor cursor = database.query(DBCode.data.TABLE_MILK,
                allColumns, DBCode.data._ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        MilkRecord newRecord = cursorToRecord(cursor);
        cursor.close();
        return newRecord;
    }

    public void delete(MilkRecord milkRecord) {
        long id = milkRecord.getId();
        System.out.println("MilkRecord deleted with id: " + id);
        database.delete(DBCode.data.TABLE_MILK, DBCode.data._ID
                + " = " + id, null);
    }

    public List<MilkRecord> getAllRecords() throws JSONException{
        List<MilkRecord> records = new ArrayList<MilkRecord>();

        Cursor cursor = database.query(DBCode.data.TABLE_MILK,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MilkRecord comment = cursorToRecord(cursor);
            records.add(comment);
            cursor.moveToNext();
        }
        cursor.close();
        return records;
    }


    private MilkRecord cursorToRecord(Cursor cursor) {
        MilkRecord record = new MilkRecord();
        record.setId(cursor.getLong(0));
        record.setLeftStartTime(cursor.getLong(1));
//        record.setLeftEndTime(cursor.getLong(2));
        record.setRightStartTime(cursor.getLong(3));
//        record.setRightEndTime(cursor.getLong(4));
        try {
            record.setSplitRecords(cursor.getString(5));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return record;
    }
}
