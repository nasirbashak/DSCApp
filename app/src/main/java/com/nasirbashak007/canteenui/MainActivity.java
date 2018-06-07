package com.nasirbashak007.canteenui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout L1, L2;
    Animation topToBottom, bottomToTop;
    Intent UserPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L1 = (LinearLayout) findViewById(R.id.first_layout);
        L2 = (LinearLayout) findViewById(R.id.second_layout);

        topToBottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_left);
        L1.setAnimation(topToBottom);

        bottomToTop = AnimationUtils.loadAnimation(this, R.anim.botton_to_top);
        L2.setAnimation(bottomToTop);


    }


    public void addUser(View view) {
        startActivity(new Intent(this, AddUserActivity.class));
    }


    public void userPage(View view) {
        UserPage = new Intent(this, UserDetails.class);
        startActivity(UserPage);

    }

    public void AddTheAmount(View view) {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "Add Dialog");


    }
}
