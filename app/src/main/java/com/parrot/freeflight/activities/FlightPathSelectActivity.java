package com.parrot.freeflight.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parrot.freeflight.R;

import java.io.File;
import java.util.ArrayList;

public class FlightPathSelectActivity extends Activity {
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_path_select);

        arrayList = new ArrayList<String>();
        list = (ListView) findViewById(R.id.listViewFiles);

        // Read svg filenames in directory
        String path = Environment.getExternalStorageDirectory().toString()+"/FreeFlight";
        try
        {
            File f = new File(path);
            File file[] = f.listFiles();
            for (int i=0; i < file.length; i++)
            {
                if (file[i].getName().endsWith(".svg"))
                {
                    arrayList.add(file[i].getName());
                }
            }
        }
        catch (Exception e)
        {
            Log.d("Files", "Path not found");
        }
        this.
        // Update UI element
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(adapter);

        // Set onclick
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String fileName = list.getItemAtPosition(position).toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("filename", fileName);
                setResult(Activity.RESULT_OK, resultIntent);
                Log.d("Files", "clicked: " + fileName);
                finish();
            }
        });

    }
}
