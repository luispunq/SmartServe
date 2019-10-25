package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class editemployee extends AppCompatActivity {
    private ImageView mProfpicbtn;
    private EditText mUsername,mPassword,mContact;
    private TextView mOldname,passchange,contactchange;
    private Uri mImageuri=null;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private static final int GALLERY_REQUEST=1;
    private StorageReference mStorageImage;
    private ProgressDialog mProgressdialog;
    private String user_id=null,pic=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editemployee);
        mOldname=findViewById(R.id.cuurname);
        passchange=findViewById(R.id.cuurname2);
        contactchange=findViewById(R.id.cuurname22);
        Button mFinish=(Button)findViewById(R.id.accountsetupbtnmod);
        final ImageView mUdate1=(ImageView) findViewById(R.id.updatebtn);
        final ImageView mUpdate2=(ImageView) findViewById(R.id.updatebtn1);
        mPassword=(EditText) findViewById(R.id.profilenamemod2);
        mProfpicbtn=(ImageView) findViewById(R.id.profileimagebtnmod);
        mUsername=(EditText)findViewById(R.id.profilenamemod);
        mContact=findViewById(R.id.profilenamemod22);
        mAuth=FirebaseAuth.getInstance();
        user_id =getIntent().getExtras().getString("key");
        mStorageImage= FirebaseStorage.getInstance().getReference().child("profileimages");
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Employees");
        mDatabaseUsers.keepSynced(true);
        mProgressdialog=new ProgressDialog(this);


        mDatabaseUsers.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String curname=dataSnapshot.child("name").getValue().toString();
                String im=dataSnapshot.child("image").getValue().toString();
                mContact.setText(dataSnapshot.child("phone").getValue().toString());
                Picasso.with(editemployee.this).load(im).into(mProfpicbtn);
                mOldname.setText(curname);
                //mUsername.setText(curname);
                pic=im;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUdate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsername.getText().toString().isEmpty()){
                    //mUdate1.setVisibility(View.GONE);
                    mOldname.setVisibility(View.GONE);
                    mUsername.setVisibility(View.VISIBLE);
                }else {
                    //mUdate1.setVisibility(View.GONE);
                    mOldname.setText(mUsername.getText().toString());
                    mOldname.setVisibility(View.VISIBLE);
                    mUsername.setVisibility(View.GONE);
                }
            }
        });


        contactchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactchange.setVisibility(View.GONE);
                mContact.setVisibility(View.VISIBLE);
            }
        });

        passchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passchange.setVisibility(View.GONE);
                mUpdate2.setVisibility(View.VISIBLE);
                mPassword.setVisibility(View.VISIBLE);
            }
        });

        mUpdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressdialog.setTitle("Changing Password");
                mProgressdialog.setCanceledOnTouchOutside(false);
                mProgressdialog.show();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String newPassword = mPassword.getText().toString();

                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mProgressdialog.dismiss();
                                    Toast.makeText(editemployee.this,"Password Changed!",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });


        mProfpicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSetup();
            }
        });
    }

    private void finishSetup() {
        final String Username;
        if (mUsername.getText().toString().isEmpty()){
            Username=mOldname.getText().toString().trim();
        }else {
            Username=mUsername.getText().toString();
        }
        //final String Usertpe=mUserttype;


        mProgressdialog.setMessage("Modifying Up Account..");
        mProgressdialog.setCanceledOnTouchOutside(false);
        mProgressdialog.show();

        mDatabaseUsers.child(user_id).child("name").setValue(Username);
        mDatabaseUsers.child(user_id).child("phone").setValue(mContact.getText().toString());

        mProgressdialog.dismiss();
        Toast.makeText(editemployee.this,"Done",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            mImageuri=data.getData();
            mProfpicbtn.setImageURI(mImageuri);
            mProgressdialog.setMessage("Changing your Profile Image..");
            mProgressdialog.setCanceledOnTouchOutside(false);
            mProgressdialog.show();

            StorageReference filepath = mStorageImage.child(mImageuri.getLastPathSegment());
            filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl=taskSnapshot.getDownloadUrl().toString();
                    mDatabaseUsers.child(user_id).child("image").setValue(downloadUrl);

                    mProgressdialog.dismiss();
                    Toast.makeText(editemployee.this, "Account Setup Finished!", Toast.LENGTH_LONG).show();
                    freeMemory();
                }
            });

        }
    }
    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
