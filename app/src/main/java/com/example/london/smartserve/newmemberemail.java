package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class newmemberemail extends AppCompatActivity {
    private EditText mEmailField,mPasswordField;
    private Button mregisterbtn;
    private FirebaseAuth mAuth;
    private String grpkey;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private String user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmemberemail);
        mProgress =new ProgressDialog(this);
        Bundle extras = getIntent().getExtras();
        grpkey= extras.getString("key");
        user= extras.getString("user");
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("members");
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
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (!task.isSuccessful())
                                    {
                                        try
                                        {
                                            throw task.getException();
                                        }
                                        // if user enters wrong email.
                                        catch (FirebaseAuthWeakPasswordException weakPassword)
                                        {


                                        }
                                        // if user enters wrong password.
                                        catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                        {


                                        }
                                        catch (FirebaseAuthUserCollisionException existEmail)
                                        {
                                            mProgress.dismiss();
                                            Toast.makeText(newmemberemail.this, "Use Another Email, the one you entered already exists.", Toast.LENGTH_LONG).show();

                                        }
                                        catch (Exception e)
                                        {

                                        }
                                    }else{
                                        if(task.isSuccessful()){
                                            mProgress.dismiss();
                                            Toast.makeText(newmemberemail.this, "Please finish account setup", Toast.LENGTH_LONG).show();
                                            Intent finishsetup =new Intent(newmemberemail.this,addmember.class);
                                            Bundle extras = new Bundle();
                                            extras.putString("key", grpkey);
                                            finishsetup.putExtras(extras);
                                            startActivity(finishsetup);
                                            finish();
                                        }
                                    }
                                }
                            }
                    );
        }
        else{
            Toast.makeText(newmemberemail.this, "Enter all details", Toast.LENGTH_LONG).show();
        }
    }
}
