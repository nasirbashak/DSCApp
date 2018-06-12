package com.nasirbashak007.canteenui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddUserActivity extends AppCompatActivity {

    LinearLayout L1,L2,L3;
    Animation leftToRight,rightToLeft,bottomToTop;
    EditText name,usn,phone,email;
    ImageView profilePic;
    Intent main;

    final int CAPTURE_REQUEST_CODE = 0;

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
        profilePic = findViewById(R.id.profile_imageview);

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
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),CAPTURE_REQUEST_CODE);
    }

    public void saveTheUserDetails(View view) {
        main= new Intent(this,MainActivity.class);
        MainActivity.database.getReference().child(usn.getText().toString()).setValue(new FirebaseObject(name.getText().toString(),usn.getText().toString(),phone.getText().toString(),email.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                profilePic.setDrawingCacheEnabled(true);
                profilePic.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                profilePic.layout(0, 0, profilePic.getMeasuredWidth(), profilePic.getMeasuredHeight());
                profilePic.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(profilePic.getDrawingCache());

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                FirebaseStorage.getInstance().getReference().child(usn.getText().toString()+".jpg").putBytes(outputStream.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Details Saved",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to upload image",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to upload to database",Toast.LENGTH_LONG).show();
            }
        });

        startActivity(main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAPTURE_REQUEST_CODE && resultCode==RESULT_OK)
            profilePic.setImageBitmap((Bitmap)data.getExtras().get("data"));
    }
}
