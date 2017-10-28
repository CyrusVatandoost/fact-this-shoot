package com.teamenigma.factthisshoot;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HighScores extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> valueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        valueList = new ArrayList();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, valueList);

        listView = (ListView) findViewById(R.id.listHighScore);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //setItemView(expenseList.get(position));
            }
        });
        adapter.notifyDataSetChanged();

        valueList.add("Dogs: " + prefs.getInt("hs_dogs", 0));
        valueList.add("Planets: " + prefs.getInt("hs_planets", 0));
        valueList.add("Flowers: " + prefs.getInt("hs_flowers", 0));
        valueList.add("Sports: " + prefs.getInt("hs_sports", 0));

    }
}
