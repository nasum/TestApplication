package com.example.masaya.testapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.masaya.testapplication.db.DataBaseHelper;
import com.example.masaya.testapplication.db.Todo;

import java.util.ArrayList;

/**
 * Created by masaya on 15/10/20.
 */
public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final DataBaseHelper dbHelper = new DataBaseHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final ListView lv = (ListView) findViewById(R.id.list_view);
        Button addButton = (Button) findViewById(R.id.button_add);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        Todo todo = new Todo();
        ArrayList<Todo> todoList = todo.list(db, null, null, null);
        db.close();

        for(Todo todoItem: todoList){
            arrayAdapter.add(todoItem.todo);
        }

        lv.setAdapter(arrayAdapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                ArrayAdapter adapter = (ArrayAdapter) lv.getAdapter();
                adapter.remove(adapter.getItem(position));

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                Todo todo = new Todo();
//                TODO IDの指定とかどうするかな
//                todo.todo = task;
//                todo.delete(db);
                db.close();

                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.edit_dialog, (ViewGroup) findViewById(R.id.dialog_layout));

                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle("タスクの編集");
                builder.setView(layout);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText eText = (EditText) layout.findViewById(R.id.task_text);

                        String task = eText.getText().toString();

                        ArrayAdapter adapter = (ArrayAdapter) lv.getAdapter();

                        adapter.remove(adapter.getItem(position));
                        adapter.insert(task, position);

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        Todo todo = new Todo();
                        todo.id = position;
                        todo.todo = task;
                        todo.put(db);

                        db.close();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                ListView listView = (ListView) parent;
                String item = (String) listView.getItemAtPosition(position);
                AlertDialog dialog = builder.create();
                EditText eText = (EditText) layout.findViewById(R.id.task_text);

                eText.setText(item, TextView.BufferType.EDITABLE);

                dialog.show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editor = (EditText) findViewById(R.id.editor);
                String text = editor.getText().toString();

                if (text.length() > 0) {
                    editor.getText().clear();
                    arrayAdapter.add(text);

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Todo todo = new Todo();
                    todo.todo = text;
                    todo.put(db);

                    db.close();
                }
            }
        });
    }
}
