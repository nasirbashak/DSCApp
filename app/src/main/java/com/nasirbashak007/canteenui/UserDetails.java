package com.nasirbashak007.canteenui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetails extends AppCompatActivity {

    private String name;
    private String usn;
    private String phone;
    private String email;
    private String amount;


    TextView nameTv, usnTv, phoneTv, emailTv, amountTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        init();


        String name = getIntent().getExtras().get("name").toString();
        String usn = getIntent().getExtras().get("usn").toString();
        String phone = getIntent().getExtras().get("phone").toString();
        String email = getIntent().getExtras().get("email").toString();
        String amount = getIntent().getExtras().get("amount").toString();

        nameTv.setText(name);
        usnTv.setText(usn);
        phoneTv.setText(phone);
        emailTv.setText(email);
        amountTv.setText(amount);


    }

    private void init() {

        /*nameTv = (TextView) findViewById(R.id.tvName);
        usnTv = (TextView) findViewById(R.id.tvUsn);
        phoneTv = (TextView) findViewById(R.id.tvPhone);
        emailTv = (TextView) findViewById(R.id.tvEmail);
        amountTv = (TextView) findViewById(R.id.tvAmount);*/


    }


}
