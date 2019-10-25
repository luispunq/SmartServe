package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class   setupgroup extends AppCompatActivity {
    private ImageView imageButton;
    private EditText gname,glocation,mgnum;
    private Spinner gtype;
    private StorageReference mGimagestore;
    private DatabaseReference mDatabase,mDatabasenewgrp,mNums,mDatabasefees,mFinance;
    private Button mBtn;
    private Uri mImageuri=null;
    private static final int GALLERY_REQUEST=1;
    private String grpkey,user,grptype=null;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setupgroup);
        mProgress=new ProgressDialog(this);
        mProgress.setTitle("New Group");
        mProgress.setMessage("Setting Up Group..");
        mProgress.setCanceledOnTouchOutside(false);
        imageButton=findViewById(R.id.gimagebtn);
        gname=findViewById(R.id.profilename);
        mgnum=findViewById(R.id.profilename4);
        glocation=findViewById(R.id.profilename2);
        gtype=findViewById(R.id.profilename3);
        mBtn=findViewById(R.id.groupbutton);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        mDatabasenewgrp=FirebaseDatabase.getInstance().getReference().child("Groups");
        mGimagestore= FirebaseStorage.getInstance().getReference().child("groupphotos");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("details");
        mDatabasefees=FirebaseDatabase.getInstance().getReference().child("fees");
        mFinance=FirebaseDatabase.getInstance().getReference().child("finances");
        mNums=FirebaseDatabase.getInstance().getReference().child("groupnums");

        mNums.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newnum=dataSnapshot.child("current").getValue().toString();
                mgnum.setText(newnum);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Group_Type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gtype.setAdapter(adapter);

        gtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                grptype=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newgroup();
            }
        });
    }

    private void newgroup() {
        final String name=gname.getText().toString().toUpperCase();
        final String location=glocation.getText().toString();
        final String type=grptype;
        final String nug=mgnum.getText().toString();
        mProgress.show();

        DatabaseReference newGrp=mDatabasenewgrp.push();
        newGrp.child("name").setValue(name);
        newGrp.child("num").setValue(nug);
        grpkey=newGrp.getKey();

        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(location)){

            StorageReference filepath = mGimagestore.child(grpkey);
            filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl=taskSnapshot.getDownloadUrl().toString();

                    mDatabase.child(grpkey).child("groupdetails").child("groupName").setValue(name);
                    mDatabase.child(grpkey).child("groupdetails").child("location").setValue(location);
                    mDatabase.child(grpkey).child("groupdetails").child("profileImage").setValue(downloadUrl);
                    mDatabase.child(grpkey).child("groupdetails").child("key").setValue(grpkey);
                    mDatabase.child(grpkey).child("groupdetails").child("num").setValue(nug);
                    mDatabase.child(grpkey).child("groupdetails").child("numbers").setValue("0");
                    mDatabase.child(grpkey).child("groupdetails").child("nextdate").setValue("Date Not Set");
                    mDatabase.child(grpkey).child("groupdetails").child("nextvenue").setValue("Group location");
                    mDatabase.child(grpkey).child("groupdetails").child("type").setValue(type);

                    DatabaseReference template=mFinance.child(grpkey);
                    template.child("savings").child("totalsavings").setValue("0");
                    template.child("loans").child("currentloan").setValue("0");
                    template.child("advances").child("currentadvance").setValue("0");

                    DatabaseReference newfee=mDatabasefees.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("groupregistration").push();
                    if (type.equals("Individual Loan Acccount")){
                        newfee.child("amount").setValue("1000");
                        newfee.child("groupid").setValue(grpkey);
                    }else{
                        newfee.child("amount").setValue("1000");
                        newfee.child("groupid").setValue(grpkey);
                    }

                    int oldnum=Integer.parseInt(nug);
                    int newnum=1+oldnum;

                    String neewnum=String.valueOf(newnum);
                    DatabaseReference updatenums=mNums;
                    updatenums.child("current").setValue(neewnum);

                    DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("NewGroup").push();
                    endmeetnot.child("name").setValue(name);
                    endmeetnot.child("groupid").setValue(grpkey);

                    mProgress.dismiss();
                    Toast.makeText(setupgroup.this, "Account Setup Finished!", Toast.LENGTH_LONG).show();

                    Intent backtosignin =new Intent(setupgroup.this,Main.class);
                    backtosignin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    backtosignin.putExtra("user",user);
                    startActivity(backtosignin);
                    finish();
                }
            });
        }

    }
    private String setdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, 42); // Adding 30 days
        String output = sdf.format(c.getTime());

        return output;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            mImageuri=data.getData();
            imageButton.setImageURI(mImageuri);
        }
    }
}
