package com.nasirbashak007.canteenui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddUserActivity extends AppCompatActivity {

    LinearLayout L1,L2,L3;
    Animation leftToRight,rightToLeft,bottomToTop;
    EditText name,usn,phone,email;
    Intent main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        L1=findViewById(R.id.name_layout);
        L2=findViewById(R.id.usn_layout);
        L3=findViewById(R.id.image_layout);

        name = findViewById(R.id.name_edittext);
        usn = findViewById(R.id.usn_edittext);
        phone = findViewById(R.id.phone_edittext);
        email = findViewById(R.id.email_edittext);

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
        main= new Intent(this,MainActivity.class);
        Toast.makeText(getApplicationContext(),"Details Saved",Toast.LENGTH_SHORT).show();
        MainActivity.database.getReference().child(usn.getText().toString()).setValue(new FirebaseObject(name.getText().toString(),usn.getText().toString(),phone.getText().toString(),email.getText().toString()));
        startActivity(main);
    }
}
