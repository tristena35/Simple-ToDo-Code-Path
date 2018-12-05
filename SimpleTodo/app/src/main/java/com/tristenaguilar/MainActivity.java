package com.tristenaguilar.simpletodo;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //List
    ArrayList<String> itemsArrayList;

    //List Adapter
    ArrayAdapter<String> itemsArrayAdapter;

    //List View
    ListView itemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readToFile();

        //Set the Adapter to the list
        itemsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsArrayList);

        //ListView
        itemListView = (ListView) findViewById(R.id.listView_items);

        //Set ListViewAdapter
        itemListView.setAdapter(itemsArrayAdapter);

        viewListener();
    }

    public void addListItem(View v){

        //EditText for User Input
        EditText todoText = (EditText) findViewById(R.id.editText_enter_item);
        todoText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        //Get Users Item
        String item = todoText.getText().toString();

        if(item.isEmpty()){
            Toast.makeText(MainActivity.this,"Make sure you enter something TODO!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Add the item to the ListView
        itemsArrayAdapter.add(item);

        //Reset text box to null.
        todoText.setText("");

        writeToFile();

        Toast.makeText(getApplicationContext(), "Item saved", Toast.LENGTH_SHORT);
    }

    private File getDataFile(){
        return new File(getFilesDir(), "toDo.txt");
    }

    private void readToFile(){
        //Populate ArrayList with saved files
        try {
            itemsArrayList = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch(IOException e){
            Log.i("MainActivity", "Error reading files.", e);
            itemsArrayList = new ArrayList<>();
        }
    }

    private void writeToFile(){
        //Save item
        try {
            FileUtils.writeLines(getDataFile(), itemsArrayList);
        }catch(IOException e){
            Log.i("MainActivity", "Error writing to non-existing files.", e);
        }
    }

    public void viewListener() {

        //Deleting an item from the list
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                //Gets current item user is touching
                final int itemToDelete = pos;

                //Remove item
                itemsArrayList.remove(itemToDelete);
                itemsArrayAdapter.notifyDataSetChanged();

                writeToFile();
                return true;
            }
        });
    }

}
