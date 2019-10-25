package com.example.london.smartserve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class banksactivity extends AppCompatActivity {
    private EditText mBname,mAcname,mAcnum,mInitial,mSigs;
    private DatabaseReference databaseReference;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banksactivity);
        button=findViewById(R.id.btnconfirm);
        mBname=findViewById(R.id.bnkname);
        mAcname=findViewById(R.id.accname);
        mAcnum=findViewById(R.id.accnum);
        mInitial=findViewById(R.id.initial);
        mSigs=findViewById(R.id.signatr);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Accounting");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mSigs.getText().toString())){
                DatabaseReference newbank=databaseReference;
                newbank.child("Bank").child("Accounts").child(mBname.getText().toString()).child("details").child("Bankname").setValue(mBname.getText().toString()+" Account");
                newbank.child("Bank").child("Accounts").child(mBname.getText().toString()).child("details").child("Accountname").setValue(mAcname.getText().toString());
                newbank.child("Bank").child("Accounts").child(mBname.getText().toString()).child("details").child("Accountnum").setValue(mAcnum.getText().toString());
                newbank.child("Bank").child("Accounts").child(mBname.getText().toString()).child("details").child("Accountsig").setValue(mSigs.getText().toString());



                    DatabaseReference reference3=databaseReference.child("Bank").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                    reference3.child("chequenumber").setValue("#09#");
                    reference3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    reference3.child("details").setValue("Cash to Office");
                    reference3.child("status").setValue("completed");
                    reference3.child("deposit").setValue(mInitial.getText().toString());
                    reference3.child("withdraw").setValue("0");
                    reference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                    DatabaseReference reference32=databaseReference.child("Bank").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                    reference32.child("chequenumber").setValue("#09#");
                    reference32.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    reference32.child("details").setValue("Cash to Office");
                    reference32.child("status").setValue("completed");
                    reference32.child("deposit").setValue(mInitial.getText().toString());
                    reference32.child("withdraw").setValue("0");
                    reference32.child("timestamp").setValue(ServerValue.TIMESTAMP);

                    DatabaseReference reference4=databaseReference.child("Bank").child("Trans").child("all").push();
                    reference4.child("chequenumber").setValue("#09#");
                    reference4.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    reference4.child("details").setValue("Cash to Office");
                    reference4.child("status").setValue("completed");
                    reference4.child("deposit").setValue(mInitial.getText().toString());
                    reference4.child("withdraw").setValue("0");
                    reference4.child("timestamp").setValue(ServerValue.TIMESTAMP);

                    DatabaseReference reference5=databaseReference.child("Bank").child("Accounts").child(mBname.getText().toString()).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                    reference5.child("chequenumber").setValue("#09#");
                    reference5.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    reference5.child("details").setValue("Cash to Office");
                    reference5.child("status").setValue("completed");
                    reference5.child("deposit").setValue(mInitial.getText().toString());
                    reference5.child("withdraw").setValue("0");
                    reference5.child("timestamp").setValue(ServerValue.TIMESTAMP);

                    DatabaseReference reference5m=databaseReference.child("Bank").child("Accounts").child(mBname.getText().toString()).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                    reference5m.child("chequenumber").setValue("#09#");
                    reference5m.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    reference5m.child("details").setValue("Cash to Office");
                    reference5m.child("status").setValue("completed");
                    reference5m.child("deposit").setValue(mInitial.getText().toString());
                    reference5m.child("withdraw").setValue("0");
                    reference5m.child("timestamp").setValue(ServerValue.TIMESTAMP);

                    DatabaseReference reference6=databaseReference.child("Bank").child("Accounts").child(mBname.getText().toString()).child("Trans").child("all").push();
                    reference6.child("chequenumber").setValue("#09#");
                    reference6.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    reference6.child("details").setValue("Cash to Office");
                    reference6.child("status").setValue("completed");
                    reference6.child("deposit").setValue(mInitial.getText().toString());
                    reference6.child("withdraw").setValue("0");
                    reference6.child("timestamp").setValue(ServerValue.TIMESTAMP);

                    DatabaseReference r2eferene3d=databaseReference.child("Bank").child("Accounts").child(mBname.getText().toString()).child("recents").child("depositstransaction");
                    r2eferene3d.child("amount").setValue(mInitial.getText().toString());
                    r2eferene3d.child("madeby").setValue("Super Admin");
                    r2eferene3d.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                }

                Intent next =new Intent(banksactivity.this,bankact.class);
                startActivity(next);
                finish();

            }
        });
    }
}
