package com.example.masaya.testapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by masaya on 15/11/04.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;


    public DataBaseHelper(Context context){
        super(context, "todo", null, DB_VERSION);
    }

    public interface DBCallback<T> {
        T process(SQLiteDatabase db);
    }

    public static <T> T read(Context context, DBCallback<T> process) {
        T ret = null;

        SQLiteDatabase db = new DataBaseHelper(context).getReadableDatabase();
        try {
            ret = process.process(db);
        }
        finally {
            db.close();
        }
        return ret;
    }

    public static <T> T write(Context context, DBCallback<T> process) {
        T ret = null;

        SQLiteDatabase db = new DataBaseHelper(context).getWritableDatabase();
        try {
            ret = process.process(db);
        }
        finally {
            db.close();
        }
        return ret;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Todo.updateTable(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Todo.dropTable(db);
        onUpgrade(db, 0, newVersion);
    }
}
