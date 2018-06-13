package com.nasirbashak007.canteenui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    LinearLayout RootLayout;
    Animation topToBottom, bottomToTop;
    Intent UserPage;

    ValueEventListener listener;
    ValueEventListener single;

    static String email = "";
    static String password = "";

    static FirebaseDatabase database;
    static DatabaseReference databaseReference;
    static FirebaseApp firebaseApp;
    private FirebaseAuth auth;
    static Intent callingIntent;
    static String DB_NAME;

    static boolean deduct;

    List<FirebaseObject> onDisplay;

    Bitmap tempPic;

    ProgressDialog pd;

    final int USER_ADD_ACTIVITY = 2;

    static OnProfilePictureUploadedListener pul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        pd = new ProgressDialog(this);
        pd.setTitle("Fetching Data");
        pd.setMessage("Please Wait...");
        pd.show();

        RootLayout = findViewById(R.id.root_layout);

        onDisplay = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference=database.getReference();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                new DataFetchTask().execute(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        pul = new OnProfilePictureUploadedListener() {
            @Override
            public void onUploaded(FirebaseObject object, Bitmap image) {
                addNewAnimatedCard(object,image);
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

    public void AddTheAmount(FirebaseObject o, TextView tv) {
        AddDialog addDialog = new AddDialog(MainActivity.this, tv, o,deduct);
        addDialog.show(getSupportFragmentManager(), "Add Dialog");
    }

    public void addNewAnimatedCard(final FirebaseObject object, @Nullable Bitmap img){
        final FirebaseObject fo = object;
        View cardRoot = getLayoutInflater().inflate(R.layout.data_view, null);

        LinearLayout L1 = cardRoot.findViewById(R.id.first_layout);
        LinearLayout L2 = cardRoot.findViewById(R.id.second_layout);

        TextView userNameTv = cardRoot.findViewById(R.id.user_name_textview);
        final TextView userAmountTv = cardRoot.findViewById(R.id.user_amount_textview);
        Button addButton = cardRoot.findViewById(R.id.add_button);
        Button deductButton = cardRoot.findViewById(R.id.deduct_button);
        final CircleImageView profilePic = cardRoot.findViewById(R.id.user_image);

        userNameTv.setText(object.getName());
        userAmountTv.setText(object.getAmount());
        addButton.setContentDescription(object.getUsn());
        deductButton.setContentDescription(object.getUsn());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deduct=false;
                AddTheAmount(object,userAmountTv);
                Toast.makeText(getApplicationContext(), "Adding for USN: "+fo.getUsn(), Toast.LENGTH_LONG).show();
            }
        });
        userAmountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TransactionViewActivity.class);
                Toast.makeText(MainActivity.this,object.getTransactions().get(object.getTransactions().keySet().toArray()[0]),Toast.LENGTH_LONG).show();
                i.putExtra("values",object.getTransactions());
                i.putExtra("total",object.getAmount());
                startActivity(i);
            }
        });
        deductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deduct=true;
                AddTheAmount(object,userAmountTv);
                Toast.makeText(getApplicationContext(), "Deducting for USN: "+fo.getUsn(), Toast.LENGTH_LONG).show();
            }
        });

        if (img == null) {
            try {
                final File temp = File.createTempFile("image", ".jpg");
                FirebaseStorage.getInstance().getReference().child(fo.getUsn() + ".jpg").getFile(temp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(temp.getAbsolutePath());
                        profilePic.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("Image Download Error", "Cannot download profile picture");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            profilePic.setImageBitmap(img);
        }

        topToBottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_left);
        L1.setAnimation(topToBottom);

        bottomToTop = AnimationUtils.loadAnimation(this, R.anim.botton_to_top);
        L2.setAnimation(bottomToTop);

        RootLayout.addView(cardRoot);
    }

    private class DataFetchTask extends AsyncTask<DataSnapshot,FirebaseObject,Void>{

        @Override
        protected void onProgressUpdate(FirebaseObject... values) {
            if(pd.isShowing())
                pd.dismiss();
            addNewAnimatedCard(values[0],null);
        }

        @Override
        protected Void doInBackground(DataSnapshot... dataSnapshots) {
            long databaseChildCount = dataSnapshots[0].getChildrenCount();
            if (databaseChildCount == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Iterable<DataSnapshot> mdata = dataSnapshots[0].getChildren();
                for (DataSnapshot ds : mdata) {
                    try {
                        FirebaseObject temp = new FirebaseObject((HashMap) ds.getValue());
                        publishProgress(temp);
                    } catch (NullPointerException e) {

                    }
                }
            }
            database.getReference().removeEventListener(listener);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
        }
    }
}