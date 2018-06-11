package com.nasirbashak007.canteenui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransactionViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_view);

        ListView lv = findViewById(R.id.transactions);

        HashMap<String, String> vals = (HashMap<String,String>)getIntent().getSerializableExtra("values");
        List<String> displayValues = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,displayValues);
        lv.setAdapter(listAdapter);

        for(Map.Entry<String, String> e:vals.entrySet()){
            displayValues.add(e.getKey()+" : "+e.getValue());
        }
    }
}
