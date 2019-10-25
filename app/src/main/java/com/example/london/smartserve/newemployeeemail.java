package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class newemployeeemail extends AppCompatActivity {
    private EditText mEmailField,mPasswordField;
    private Button mregisterbtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newemployeeemail);
        mProgress =new ProgressDialog(this);
        //grpkey=getIntent().getExtras().getString("key");
        mAuth=FirebaseAuth.getInstance();
        mEmailField=(EditText)findViewById(R.id.emailField);
        mPasswordField=(EditText)findViewById(R.id.passwordField);
        mregisterbtn=(Button)findViewById(R.id.registerbtn);

        mregisterbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

    private void startRegister() {
        String email=mEmailField.getText().toString().trim();
        String password=mPasswordField.getText().toString().trim();


        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password) ){
            mProgress.setTitle("Please wait..");
            mProgress.setMessage("Signing Up.. ");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        mProgress.dismiss();
                        Toast.makeText(newemployeeemail.this, "Please finish account setup", Toast.LENGTH_LONG).show();
                        Intent finishsetup =new Intent(newemployeeemail.this,addemployee.class);
                        startActivity(finishsetup);
                        finish();
                    }
                }
            });
        }
        else{
            Toast.makeText(newemployeeemail.this, "Enter all details", Toast.LENGTH_LONG).show();
        }
    }
}
