package com.aung.simpletodo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // These java objects are base on the xml objects
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();
        //  items = new ArrayList<>();
        //  items.add("Buy milk");
        //  items.add("Go to gym");
        //  items.add("Have fun");

        ItemsAdapter.OnLongClickListener  onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed.", Toast.LENGTH_SHORT).show();

                // save updated list
                saveItems();
            }
        };

        // ItemAdapter is a RecyclerView.Adapter
        // needs a list of items
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);

        // RecyclerView needs an adapter and a layout
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));


        // Add listener to btnAdd
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();

                // Add item to the model
                items.add(todoItem);

                // Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);

                // Clear
                etItem.setText("");

                // toast the success of adding an item
                Toast.makeText(getApplicationContext(), "Item was added.", Toast.LENGTH_SHORT).show();

                // save to file system
                saveItems();
            }
        });
    }

    private File getDataFile(){
        return  new File(getFilesDir(), "data.txt");
    }
    private void loadItems(){
        // FileUtils of commons, not android.io
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items.", e);
            items = new ArrayList<>();  // just an empty
        }
    }
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items.", e);
        }
    }
}