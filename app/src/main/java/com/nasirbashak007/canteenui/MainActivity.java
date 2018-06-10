package com.nasirbashak007.canteenui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout RootLayout;
    Animation topToBottom, bottomToTop;
    Intent UserPage;

    ValueEventListener listener;

    static String email = "";
    static String password = "";

    static FirebaseDatabase database;
    static DatabaseReference databaseReference;
    static FirebaseApp firebaseApp;
    private FirebaseAuth auth;
    static Intent callingIntent;
    static String DB_NAME;

    List<FirebaseObject> onDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        RootLayout = findViewById(R.id.root_layout);

        onDisplay = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference=database.getReference();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long databaseChildCount=dataSnapshot.getChildrenCount();
                if(databaseChildCount==0){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Iterable<DataSnapshot> mdata = dataSnapshot.getChildren();
                    for (DataSnapshot ds : mdata) {
                        FirebaseObject temp = ds.getValue(FirebaseObject.class);
                        addNewAnimatedCard(temp);
                    }
                    database.getReference().removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        database.getReference().addValueEventListener(listener);
    }


    public void addUser(View view) {
        startActivity(new Intent(this, AddUserActivity.class));
    }


    public void userPage(View view) {
        UserPage = new Intent(this, UserDetails.class);
        startActivity(UserPage);

    }

    public void AddTheAmount(FirebaseObject o) {
        AddDialog addDialog = new AddDialog(MainActivity.this,o);
        addDialog.show(getSupportFragmentManager(), "Add Dialog");
    }

    public void addNewAnimatedCard(final FirebaseObject object){
        final FirebaseObject fo = object;
        View cardRoot = getLayoutInflater().inflate(R.layout.data_view, null);

        LinearLayout L1 = cardRoot.findViewById(R.id.first_layout);
        LinearLayout L2 = cardRoot.findViewById(R.id.second_layout);

        TextView userNameTv = cardRoot.findViewById(R.id.user_name_textview);
        TextView userAmountTv = cardRoot.findViewById(R.id.user_amount_textview);
        Button addButton = cardRoot.findViewById(R.id.add_button);
        Button deductButton = cardRoot.findViewById(R.id.deduct_button);

        userNameTv.setText(object.getName());
        addButton.setContentDescription(object.getUsn());
        deductButton.setContentDescription(object.getUsn());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTheAmount(object);
                Toast.makeText(getApplicationContext(), "Adding for USN: "+fo.getUsn(), Toast.LENGTH_LONG).show();
            }
        });
        deductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Deducting for USN: "+fo.getUsn(), Toast.LENGTH_LONG).show();
            }
        });

        topToBottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_left);
        L1.setAnimation(topToBottom);

        bottomToTop = AnimationUtils.loadAnimation(this, R.anim.botton_to_top);
        L2.setAnimation(bottomToTop);

        RootLayout.addView(cardRoot);
    }
}
