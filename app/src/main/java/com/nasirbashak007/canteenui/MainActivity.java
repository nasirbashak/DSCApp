package com.nasirbashak007.canteenui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    LinearLayout L1, L2;
    Animation topToBottom, bottomToTop;
    Intent UserPage;

    static String email = "";
    static String password = "";

    static FirebaseDatabase database;
    static DatabaseReference databaseReference;
    private FirebaseAuth auth;
    static Intent callingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L1 = (LinearLayout) findViewById(R.id.first_layout);
        L2 = (LinearLayout) findViewById(R.id.second_layout);

        final LinearLayout ll =(LinearLayout) getLayoutInflater().inflate(R.layout.temp_firebase_login_dialog,null);
        new AlertDialog.Builder(this).setView(ll).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                email = ((EditText)(ll.findViewById(R.id.mail))).getText().toString();
                password = ((EditText)(ll.findViewById(R.id.pass))).getText().toString();
                dialog.dismiss();
            }
        }).create().show();

        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            Log.e("Login error","Couldn't log in");
                        }
                    }
                });


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
