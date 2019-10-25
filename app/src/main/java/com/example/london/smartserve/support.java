package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class support extends AppCompatActivity {
    private EditText mHelpquerry;
    private Button mSend;
    private DatabaseReference mDatabasehelp;
    private FirebaseAuth mAuth;
    private String user;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        mDatabasehelp= FirebaseDatabase.getInstance().getReference().child("helpqueries");
        mDatabasehelp.keepSynced(true);
        mHelpquerry=findViewById(R.id.message);
        mSend=findViewById(R.id.supportbutton);
        mProgress=new ProgressDialog(this);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress.setTitle("Please wait");
                mProgress.setMessage("Sending Message");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                String message=mHelpquerry.getText().toString();
                DatabaseReference newquerry=mDatabasehelp.push();
                newquerry.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                newquerry.child("user").setValue(user);
                newquerry.child("message").setValue(message);
                newquerry.child("status").setValue("pending");
                mProgress.dismiss();
                Toast.makeText(support.this,"Message Sent!", Toast.LENGTH_LONG).show();

                Intent forw=new Intent(support.this,coordinator.class);
                forw.putExtra("user",user);
                startActivity(forw);
                finish();
            }
        });
    }
}
