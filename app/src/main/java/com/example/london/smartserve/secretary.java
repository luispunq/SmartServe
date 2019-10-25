package com.example.london.smartserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class secretary extends AppCompatActivity {
    private CardView mPesa,mCash,mPesareports,makepay,bankingrecords;
    private DatabaseReference mD2,mD4,mD5;
    private FirebaseAuth mAuth;
    private String unow;
    private TextView outball,amoutin,amoutout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretarycomp);
        mPesa=(CardView) findViewById(R.id.dailyre);
        mCash=(CardView) findViewById(R.id.monthlyre);
        makepay=findViewById(R.id.officepayment);
        mPesareports=findViewById(R.id.repot);
        mAuth=FirebaseAuth.getInstance();
        outball=findViewById(R.id.outbal);
        amoutin=findViewById(R.id.amountgvn);
        amoutout=findViewById(R.id.amountrtn);
        bankingrecords=findViewById(R.id.secaccount);
        unow=mAuth.getCurrentUser().getUid();
        mD2= FirebaseDatabase.getInstance().getReference().child("Employees").child(unow);
        mD5= FirebaseDatabase.getInstance().getReference().child("Accounts").child(unow);
        mD5.keepSynced(true);
        FirebaseDatabase.getInstance().getReference().child("Accounts").keepSynced(true);

        mD5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("Amountin").exists()){
                String bal=dataSnapshot.child("Balance").getValue().toString();
                String amin=dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("Amountin").getValue().toString();
                String amout=dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("Amountout").getValue().toString();

                outball.setText("Kshs. "+bal);
                amoutin.setText("Kshs. "+amin);
                amoutout.setText("Kshs. "+amout);
                }else {
                    String bal=dataSnapshot.child("Balance").getValue().toString();
                    String amin="0";
                    String amout="0";

                    outball.setText("Kshs. "+bal);
                    amoutin.setText("Kshs. "+amin);
                    amoutout.setText("Kshs. "+amout);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mPesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PrintOrder printer = new PrintOrder();
                //printer.Print(secretary.this,"USB");
                Intent dailyre=new Intent(secretary.this,mpesapayment.class);
                startActivity(dailyre);

            }
        });
        mCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monthlyre=new Intent(secretary.this,employees.class);
                startActivity(monthlyre);
                //finish();
            }
        });
        mPesareports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent montlyre=new Intent(secretary.this,dateselector3.class);
                startActivity(montlyre);
            }
        });

        makepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent montlyre=new Intent(secretary.this,groupslistsec.class);
                startActivity(montlyre);
            }
        });

        bankingrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent montlyre=new Intent(secretary.this,dateselector7.class);
                startActivity(montlyre);
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exiting App")
                .setMessage("Are you sure you want to exit app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
