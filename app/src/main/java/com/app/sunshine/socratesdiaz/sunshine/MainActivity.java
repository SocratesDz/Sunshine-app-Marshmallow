package com.app.sunshine.socratesdiaz.sunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> fakeData = new ArrayList<String>();
        fakeData.add("Today - Sunny - 88 / 63");
        fakeData.add("Tomorrow - Foggy - 70 / 46");
        fakeData.add("Weds - Cloudy - 72 / 63");
        fakeData.add("Thurs - Rainy - 64 / 51");
        fakeData.add("Fri - Foggy - 70 / 46");
        fakeData.add("Sat - Sunny - 76 / 83");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, R.layout.list_item_forecast, R.id.list_item_forecast_textview, fakeData);

        ListView listView = (ListView) findViewById(R.id.listview_forecast);
        listView.setAdapter(arrayAdapter);

    }
}
