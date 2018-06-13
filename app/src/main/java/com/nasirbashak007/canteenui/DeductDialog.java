package com.nasirbashak007.canteenui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.Date;

@SuppressLint("ValidFragment")
public class DeductDialog extends AppCompatDialogFragment {
    private EditText editTextAmount;
    private TextView toChange;
    private FirebaseObject object;
    private Context context;

    @SuppressLint("ValidFragment")
    public DeductDialog(Context c, TextView tv, FirebaseObject person) {
        object = person;
        context = c;
        toChange = tv;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_dialog_layout, null);

        builder.setView(view).setTitle("Deducting Credentials")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Credentials Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("Deduct", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Name, USN, Phone, Mail
                //TODO: Fill these
                String senderID = "someone@gmail.com";
                String password = "********"; //TODO: Maybe set up an encrypted string, and decpypt on call
                String recvID = "otherperson@somemail.com";
                String subject = "Some Subject";
                String message = "Some text";
                //new MailSender(senderID,password).sendMailAsync(recvID,subject,message);

                final String amount = editTextAmount.getText().toString().trim();


                PersonDetails personDetails = new PersonDetails(amount);
                Toast.makeText(getContext(), amount, Toast.LENGTH_SHORT).show();

                Toast.makeText(getContext(), "From The Class " + personDetails.getAmount(), Toast.LENGTH_SHORT).show();

                //Get date and time
                DateFormat df = DateFormat.getDateInstance();
                DateFormat tf = DateFormat.getTimeInstance();

                //Update transactions list
                MainActivity.database.getReference().child(object.getUsn()).child("transactions").child(df.format(new Date()) + " " + tf.format(new Date())).setValue("Deducted " + amount, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                final int temp=Integer.parseInt(object.getAmount());
                        final String newAmount = String.valueOf(Integer.parseInt(object.getAmount()) - Integer.parseInt(amount));
                        MainActivity.database.getReference().child(object.getUsn()).child("amount").setValue(newAmount, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                toChange.setText("Net Amount:  " + newAmount);

                                Toast.makeText(context,"Previous amount from FB "+temp,Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Transaction completed successfully! Amount "+newAmount , Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
        editTextAmount = (EditText) view.findViewById(R.id.editTextAmount);

        return builder.create();
    }
}
