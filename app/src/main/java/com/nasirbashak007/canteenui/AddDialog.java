package com.nasirbashak007.canteenui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.susmit.mailsender.MailSender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.nasirbashak007.canteenui.MainActivity.EmailPassword;
import static com.nasirbashak007.canteenui.MainActivity.EmailSenderID;

@SuppressLint("ValidFragment")
public class AddDialog extends AppCompatDialogFragment {

    private EditText editTextAmount;
    private TextView toChange;
    private FirebaseObject object;
    private Context context;
    private boolean deduct;

    static String EmailRecvID;
    static String EmailSubject;
    static String EmailMessage;
    static String userName;

    public AddDialog(Context c, TextView tv, FirebaseObject person, boolean deduct) {
        object = person;
        context = c;
        toChange = tv;
        this.deduct = deduct;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_dialog_layout, null);

        if (!deduct) {
            builder.setView(view).setTitle("Adding Credentials").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getContext(), "Credentials Cancelled", Toast.LENGTH_SHORT).show();
                }
            }).setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    MainActivity.database.getReference().child(object.getUsn()).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // Toast.makeText(context, "Name from FB " + snapshot.getValue(), Toast.LENGTH_LONG).show();
                            userName = (String) snapshot.getValue();
                            // System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });


                    EmailRecvID = object.getEmail();

                    EmailSubject = "GK Canteen Statement";


                    final String amount = editTextAmount.getText().toString().trim();

                    int testAmount = Integer.parseInt(amount);
                    if (testAmount == 0) {
                        Toast.makeText(context, "Amount cant be zero", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Get date and time
                    final DateFormat df = DateFormat.getDateInstance();
                    final DateFormat tf = DateFormat.getTimeInstance();

                    //Update transactions list
                    MainActivity.database.getReference().child(object.getUsn()).child("transactions").child(df.format(new Date()) + " " + tf.format(new Date())).setValue("Added " + amount, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            final String newAmount = String.valueOf(Integer.parseInt(object.getAmount()) + Integer.parseInt(amount));
                            MainActivity.database.getReference().child(object.getUsn()).child("amount").setValue(newAmount, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    toChange.setText(newAmount);
                                    HashMap<String, String> m = object.getTransactions();
                                    m.put(df.format(new Date()) + " " + tf.format(new Date()), "Added " + amount);
                                    object.setTransactions(m);
                                    object.setAmount(newAmount);

                                    EmailMessage = "Dear " + userName + " .\nThank you for vitsiting GK shop.\nYou just credited with the amount " + amount + " .\n" +
                                            "Your Total Net Credit amount is " + newAmount;

                                    new MailSender(EmailSenderID, EmailPassword).sendMailAsync(EmailRecvID, EmailSubject, EmailMessage);
                                    Toast.makeText(context, "Transaction completed successfully!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });
        } else {
            builder.setView(view).setTitle("Deducting Credentials").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getContext(), "Credentials Cancelled", Toast.LENGTH_SHORT).show();
                }
            }).setPositiveButton("Deduct", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    EmailRecvID = object.getEmail();

                    EmailSubject = "GK Canteen Statement";

                    final String amount = editTextAmount.getText().toString().trim();

                    int testAmount = Integer.parseInt(amount);
                    if (testAmount == 0) {
                        Toast.makeText(context, "Amount cant be zero", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Get date and time
                    final DateFormat df = DateFormat.getDateInstance();
                    final DateFormat tf = DateFormat.getTimeInstance();

                    final String newAmount = String.valueOf(Integer.parseInt(object.getAmount()) - Integer.parseInt(amount));
                    if (Integer.parseInt(newAmount) < 0) {
                        Toast.makeText(getContext(), "Insufficient amount", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //Update transactions list
                    MainActivity.database.getReference().child(object.getUsn()).child("transactions").child(df.format(new Date()) + " " + tf.format(new Date())).setValue("Deducted " + amount, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            MainActivity.database.getReference().child(object.getUsn()).child("amount").setValue(newAmount, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    toChange.setText(newAmount);
                                    HashMap<String, String> m = object.getTransactions();
                                    m.put(df.format(new Date()) + " " + tf.format(new Date()), "Deducted " + amount);
                                    object.setTransactions(m);
                                    object.setAmount(newAmount);

                                    EmailMessage = "Dear " + userName + " .\nThank you for visiting GK shop.\nYou just debited with the amount " + amount + " .\n" +
                                            "Your Total Net Credit amount is " + newAmount;

                                    new MailSender(EmailSenderID, EmailPassword).sendMailAsync(EmailRecvID, EmailSubject, EmailMessage);

                                    Toast.makeText(context, "Transaction completed successfully!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });
        }
        editTextAmount = (EditText) view.findViewById(R.id.editTextAmount);

        return builder.create();
    }
}
