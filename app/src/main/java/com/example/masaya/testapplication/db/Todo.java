package com.example.masaya.testapplication.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by masaya on 15/11/12.
 */
public class Todo extends AbstractModel {

    public static final String TABLE_NAME = "todo";

    public String todo;

    public static Todo createUsingCursor(Cursor c){
        Todo todo = new Todo();

        todo.id = c.getLong(c.getColumnIndex("_id"));
        todo.todo = c.getString(c.getColumnIndex("todo"));

        return todo;
    }

    static class DBAccessor extends AbstractDBAccessor<Todo> {
        public DBAccessor(SQLiteDatabase db){
            super(db, TABLE_NAME);
        }
        @Override
        protected Todo createModel(Cursor c) {
            return createUsingCursor(c);
        }

        @Override
        protected ContentValues createContentValue(Todo instance) {
            ContentValues values = new ContentValues();
            if (instance.id!=UNDEFINED) values.put("_id", instance.id);
            values.put("todo",instance.todo);
            return values;
        }
    }

    public static ArrayList<Todo> list(SQLiteDatabase db, String selection, String[] selectionArgs, String orderBy) {
        return new DBAccessor(db).list(selection, selectionArgs, orderBy);
    }

    public static Todo get(SQLiteDatabase db, long id) {
        return new DBAccessor(db).get(id);
    }

    public void put(SQLiteDatabase db) {
        new DBAccessor(db).put(this);
    }

    public static void delete(SQLiteDatabase db, String selection, String[] selectionArgs) {
        new DBAccessor(db).delete(selection, selectionArgs);
    }

    public void delete(SQLiteDatabase db) {
        new DBAccessor(db).delete(this);
    }

    public static void updateTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        int updateVersion = oldVersion;

        if (updateVersion < 1) {
            StringBuilder s = new StringBuilder();
            s.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME)
                    .append("(_id INTEGER PRIMARY KEY")
                    .append(",todo TEXT")
                    .append(");");
            db.execSQL(s.toString());

            updateVersion = 1;
        }

    }

    public static void dropTable(SQLiteDatabase db) {
        new DBAccessor(db).dropTable();
    }
}
