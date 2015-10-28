package com.example.masaya.testapplication;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by masaya on 15/10/20.
 */
public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final ListView lv = (ListView) findViewById(R.id.list_view);
        Button addButton = (Button) findViewById(R.id.button_add);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        lv.setAdapter(arrayAdapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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
                return false;
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
                }
            }
        });
    }
}