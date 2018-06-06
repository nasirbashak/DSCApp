package com.nasirbashak007.canteenui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class UserDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        String details[]= {"+50 on 25/4/17 at 10:00 am","+70 on 25/4/17 at 5:00 pm"};
        ListAdapter UserAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,details);
        ListView PurchaseDetails= findViewById(R.id.details);
        PurchaseDetails.setAdapter(UserAdapter);
    }
}
