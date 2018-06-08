package com.nasirbashak007.canteenui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.susmit.mailsender.MailSender;

public class AddDialog extends AppCompatDialogFragment {

    private EditText editTextAmount;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_dialog_layout, null);

        builder.setView(view)
                .setTitle("Adding Credentials")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Credentials Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        //TODO: Fill these
                        String senderID = "someone@gmail.com";
                        String password="********"; //TODO: Maybe set up an encrypted string, and decpypt on call
                        String recvID = "otherperson@somemail.com";
                        String subject = "Some Subject";
                        String message = "Some text";
                        //new MailSender(senderID,password).sendMailAsync(recvID,subject,message);

                        String amount = editTextAmount.getText().toString().trim();

                        PersonDetails personDetails = new PersonDetails(amount);
                        Toast.makeText(getContext(), amount , Toast.LENGTH_SHORT).show();

                        Toast.makeText(getContext(), "From The Class " + personDetails.getAmount(), Toast.LENGTH_SHORT).show();
                    }
                });
        editTextAmount = (EditText) view.findViewById(R.id.editTextAmount);



        return builder.create();
    }
}
