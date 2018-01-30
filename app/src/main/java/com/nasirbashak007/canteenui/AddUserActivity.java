package com.nasirbashak007.canteenui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddUserActivity extends AppCompatActivity {

    LinearLayout L1,L2,L3;
    Animation leftToRight,rightToLeft,bottomToTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        L1=(LinearLayout)findViewById(R.id.name_layout);
        L2=(LinearLayout)findViewById(R.id.usn_layout);
        L3=(LinearLayout)findViewById(R.id.image_layout);

        animate();
    }

    public void animate(){
        leftToRight= AnimationUtils.loadAnimation(this,R.anim.left_to_right_linear);
        L1.setAnimation(leftToRight);

        rightToLeft= AnimationUtils.loadAnimation(this,R.anim.right_to_left_linear);
        L2.setAnimation(rightToLeft);

        bottomToTop= AnimationUtils.loadAnimation(this,R.anim.bottom_to_top_linear);
        L3.setAnimation(bottomToTop);

    }

    public void openCamera(View view) {
        Toast.makeText(getApplicationContext(),"Open Camera",Toast.LENGTH_SHORT).show();
    }

    public void saveTheUserDetails(View view) {
        Toast.makeText(getApplicationContext(),"Details Saved",Toast.LENGTH_SHORT).show();
    }
}
