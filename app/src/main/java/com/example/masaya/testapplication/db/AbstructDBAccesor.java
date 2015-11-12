package com.example.masaya.testapplication.db;

/**
 * Created by masaya on 15/11/12.
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

abstract class AbstractDBAccessor<T extends AbstractModel> {
    private static final String TAG = AbstractDBAccessor.class.getName();

    protected SQLiteDatabase mDb;
    protected String mTableName;
    protected AbstractDBAccessor(SQLiteDatabase db, String tableName) {
        mDb = db;
        mTableName = tableName;
    }
    protected abstract T createModel(Cursor c);
    protected abstract ContentValues createContentValue(T instance);

    public ArrayList<T> list(String selection, String[] selectionArgs, String orderBy){
        final ArrayList<T> ret = new ArrayList<T>();
        Cursor c = mDb.query(mTableName,null,selection, selectionArgs,null,null,orderBy);
        if(c!=null) {
            while (c.moveToNext()) {
                ret.add(createModel(c));
            }
            c.close();
        }
        return ret;
    }

    public T get(long id){
        Cursor c = mDb.query(mTableName, null, "_id=?", new String[]{Long.toString(id)}, null, null, null);
        if(c!=null && c.moveToNext()) {
            T instance = (c.getCount() > 0) ? createModel(c) : null;
            c.close();
            return instance;
        }
        return null;
    }

    public void put(T instance){
        ContentValues values = createContentValue(instance);
        mDb.replace(mTableName, null, values);
    }

    public int delete(T instance){
        return mDb.delete(mTableName,"_id=?",new String[]{Long.toString(instance.id)});
    }

    public int delete(String selection, String[] selectionArgs){
        return mDb.delete(mTableName,selection,selectionArgs);
    }

    protected void dropTable(){
        mDb.execSQL("DROP TABLE IF EXISTS "+mTableName+";");
    }
}
