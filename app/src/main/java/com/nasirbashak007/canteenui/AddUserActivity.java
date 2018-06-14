package com.nasirbashak007.canteenui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUserActivity extends AppCompatActivity {

    LinearLayout L1, L2;
    Animation leftToRight, rightToLeft, bottomToTop;
    EditText name, usn, phone, email;
    Button saveButton;
    CircleImageView profilePic;
    boolean profilePicChanged;
    boolean profilePicCaptured;

    final int CAPTURE_REQUEST_CODE = 1;
    final int CAMERA_PERMISSION_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        L1 = findViewById(R.id.name_layout);
        L2 = findViewById(R.id.usn_layout);

        profilePicChanged = true;

        name = findViewById(R.id.name_edittext);
        usn = findViewById(R.id.usn_edittext);
        phone = findViewById(R.id.phone_edittext);
        email = findViewById(R.id.email_edittext);
        profilePic = findViewById(R.id.profile_imageview);
        saveButton = findViewById(R.id.save_button);

        animate();
    }

    public void animate() {
        leftToRight = AnimationUtils.loadAnimation(this, R.anim.left_to_right_linear);
        L1.setAnimation(leftToRight);

        rightToLeft = AnimationUtils.loadAnimation(this, R.anim.right_to_left_linear);
        L2.setAnimation(rightToLeft);

        findViewById(R.id.phone_layout).setAnimation(leftToRight);
        findViewById(R.id.mail_layout).setAnimation(rightToLeft);

        bottomToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_linear);

        profilePic.setAnimation(bottomToTop);
        saveButton.setAnimation(bottomToTop);
    }

    public void openCamera(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoIntent.putExtra("outputX", 150);
                photoIntent.putExtra("outputY", 150);
                photoIntent.putExtra("aspectX", 1);
                photoIntent.putExtra("aspectY", 1);
                photoIntent.putExtra("scale", true);
                startActivityForResult(new Intent(photoIntent), CAPTURE_REQUEST_CODE);
            } else {
                String[] perms = {Manifest.permission.CAMERA};
                requestPermissions(perms, CAMERA_PERMISSION_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    profilePic.setImageBitmap((Bitmap) data.getExtras().get("data"));
                    profilePicCaptured = true;
                }
                break;
        }
    }

    public void saveTheUserDetails(View view) {
        if (usn.getText().toString().isEmpty() || name.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "USN and Name fields can't be empty!", Toast.LENGTH_LONG).show();
            return;
        } else if (!profilePicCaptured) {
            Toast.makeText(getApplicationContext(), "Please capture the Picture", Toast.LENGTH_LONG).show();
            return;
        }
        final FirebaseObject toStore = new FirebaseObject(name.getText().toString(), usn.getText().toString(), phone.getText().toString(), email.getText().toString());

        MainActivity.database.getReference().child(usn.getText().toString()).setValue(toStore).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                final Bitmap bitmap = Bitmap.createBitmap(profilePic.getLayoutParams().width, profilePic.getLayoutParams().height, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bitmap);
                profilePic.layout(0, 0, profilePic.getLayoutParams().width, profilePic.getLayoutParams().height);
                profilePic.draw(c);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                FirebaseStorage.getInstance().getReference().child(usn.getText().toString() + ".jpg").putBytes(outputStream.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Details Saved", Toast.LENGTH_SHORT).show();
                        MainActivity.pul.onUploaded(toStore, bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to upload to database", Toast.LENGTH_LONG).show();
            }
        });

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST:
                if (grantResults[requestCode] == PackageManager.PERMISSION_GRANTED)
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAPTURE_REQUEST_CODE);
                else
                    Toast.makeText(getApplicationContext(), "You need to allow camera access", Toast.LENGTH_LONG).show();

        }
    }
}