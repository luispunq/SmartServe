package com.example.london.smartserve;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.Settings.*;

public class Main extends AppCompatActivity {
    private EditText mloginemail, mloginpass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,mData;
    private ProgressDialog mProgress;
    private Boolean op=true;
    private long downsize;
    private String refreshedToken;
    private StorageReference storageRef;
    private long enqueue;
    private DownloadManager dm;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData=FirebaseDatabase.getInstance().getReference().child("Employees");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("UpdateManager");
        mloginemail = (EditText) findViewById(R.id.username);
        mloginpass = (EditText) findViewById(R.id.password);
        Button mloginbtn = (Button) findViewById(R.id.loginbutton);
        mProgress=new ProgressDialog(this);
        sp = getSharedPreferences("login",MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        storageRef= FirebaseStorage.getInstance().getReference();

        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        mloginemail.setHintTextColor(getResources().getColor(R.color.colorAccent));
        mloginpass.setHintTextColor(getResources().getColor(R.color.colorAccent));

        /*if(sp.getBoolean("logged",false)){
            userTypelogin2();
        }*/

        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignin();
                //sp.edit().putBoolean("logged",true).apply();
            }
        });

    }


    private void startSignin() {
        final String snemail = mloginemail.getText().toString().trim();
        String snpass = mloginpass.getText().toString().trim();
        if (!TextUtils.isEmpty(snemail) && !TextUtils.isEmpty(snpass)) {
            mProgress.setTitle("Please wait");
            mProgress.setMessage("Signing In.. ");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            mAuth.signInWithEmailAndPassword(snemail,snpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mProgress.dismiss();
                        userTypelogin2();
                    }
                    else{
                        Toast.makeText(Main.this,"Login Failed", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            Toast.makeText(Main.this, "Enter All Details", Toast.LENGTH_LONG).show();
        }
    }

    private void userTypelogin2() {
        final String user_id=mAuth.getCurrentUser().getUid();
        mProgress.setTitle("Please wait");
        mProgress.setMessage("Signing in: "+mAuth.getCurrentUser().getEmail());
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        final String android_id=FirebaseInstanceId.getInstance().getToken();
        mData.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switch (dataSnapshot.child("position").getValue().toString()) {
                    case "Admin":
                        mData.child(user_id).child("device_token").setValue(refreshedToken);
                        Intent doctorview = new Intent(Main.this, masterhome.class);
                        mProgress.dismiss();
                        Toast.makeText(Main.this, "Login Successful", Toast.LENGTH_LONG).show();
                        startActivity(doctorview);
                        finish();
                        break;
                    case "user": {
                        Intent addpatients = new Intent(Main.this, coordinator.class);
                        mData.child(user_id).child("device_token").setValue(refreshedToken);
                        addpatients.putExtra("user", user_id);
                        startActivity(addpatients);
                        mProgress.dismiss();
                        Toast.makeText(Main.this, "Login Successful", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    }
                    case "manager": {
                        Intent addpatients = new Intent(Main.this,managerhome.class);
                        mData.child(user_id).child("device_token").setValue(android_id);
                        mProgress.dismiss();
                        startActivity(addpatients);
                        Toast.makeText(Main.this, "Login Successful", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    }
                    case "superadmin": {
                        Intent addpatients = new Intent(Main.this, accountinghome.class);
                        mData.child(user_id).child("device_token").setValue(android_id);
                        mProgress.dismiss();
                        startActivity(addpatients);
                        Toast.makeText(Main.this, "Login Successful", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    }
                    default:
                    {
                        Intent addpatients = new Intent(Main.this, secretary.class);
                        mData.child(user_id).child("device_token").setValue(android_id);
                        addpatients.putExtra("user", user_id);
                        startActivity(addpatients);
                        mProgress.dismiss();
                        Toast.makeText(Main.this, "Login Successful", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean checkmember(final String user_id) {
        DatabaseReference mDatabases=FirebaseDatabase.getInstance().getReference().child("members");
        mDatabases.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user_id).child("details").child("clearance").getValue().equals("member")){
                    op=true;
                }else{
                    op=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return op;
    }


}
