package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class addemployee extends AppCompatActivity {
    private ImageView mGroupImage;
    private TextView mGroupName;
    private ImageView mMemberImage;
    private EditText mName,mID,mContact,mStatus,mStartDate,mSalary;
    private Spinner mSex,mPosition;
    private Button mSubmit;
    private DatabaseReference mDatabaseMembers,mD2;
    private StorageReference mProfileImageStore;
    private static final int GALLERY_REQUEST=1;
    private Uri mImageuri=null;
    private FirebaseAuth mAuth;
    private String user;
    private ProgressDialog mProgressdialog;
    private String newnum=null,unow,poss;
    private String grpkey=null,psex=null,grp=null,type=null;
    private String gname=null;
    private DatabaseReference mDatabasefees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addemployee);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        //=findViewById(R.id.addmemberimageView);
        mGroupName=findViewById(R.id.addmembergroupname);
        mMemberImage=findViewById(R.id.imageView5);
        mName=findViewById(R.id.newname);
        mID=findViewById(R.id.idnumber);
        mPosition=findViewById(R.id.mbrclrnce);
        mContact=findViewById(R.id.group);
        mStatus=findViewById(R.id.statuses);
        mStartDate=findViewById(R.id.startdate);
        mSalary=findViewById(R.id.salary);
        mSubmit=findViewById(R.id.button);
        mSex=(Spinner)findViewById(R.id.sex);
        mDatabaseMembers= FirebaseDatabase.getInstance().getReference().child("Employees");
        mProfileImageStore= FirebaseStorage.getInstance().getReference().child("employeeimages");
        mProgressdialog=new ProgressDialog(this);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSex.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.memberopts,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPosition.setAdapter(adapter2);

        mSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                psex=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                poss=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mMemberImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSetup();
            }
        });

    }

    private void finishSetup() {
        final String Username=mName.getText().toString().trim();
        final String ID=mID.getText().toString().trim();
        final String Gender=psex;
        final String Position=poss;
        final String Contact=mContact.getText().toString().trim();
        final String Status=mStatus.getText().toString().trim();
        final String StartDate=mStartDate.getText().toString().trim();
        final String Salary=mSalary.getText().toString().trim();
        final String user_id=mAuth.getCurrentUser().getUid();
        String ima=mImageuri.getLastPathSegment();

        if(!ima.isEmpty()&&!TextUtils.isEmpty(Username)&&!TextUtils.isEmpty(ID)&&!TextUtils.isEmpty(Gender)){
            mProgressdialog.setMessage("Setting Up Account..");
            mProgressdialog.setMessage("Registering: "+Username);
            mProgressdialog.setCanceledOnTouchOutside(false);
            mProgressdialog.show();
            StorageReference filepath = mProfileImageStore.child(mImageuri.getLastPathSegment());
            filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl=taskSnapshot.getDownloadUrl().toString();

                    mDatabaseMembers.child(user_id).child("name").setValue(Username);
                    mDatabaseMembers.child(user_id).child("id").setValue(ID);
                    mDatabaseMembers.child(user_id).child("phone").setValue(Contact);
                    mDatabaseMembers.child(user_id).child("position").setValue(Position);
                    mDatabaseMembers.child(user_id).child("sex").setValue(Gender);
                    mDatabaseMembers.child(user_id).child("fieldid").setValue(String.valueOf(ServerValue.TIMESTAMP));
                    mDatabaseMembers.child(user_id).child("status").setValue(Status);
                    mDatabaseMembers.child(user_id).child("Branch").setValue(StartDate);
                    mDatabaseMembers.child(user_id).child("salary").setValue(Salary);
                    mDatabaseMembers.child(user_id).child("image").setValue(downloadUrl);

                    DatabaseReference newloan=mDatabaseMembers.child(user_id).child("basicpays");
                    newloan.child("basicpay").setValue(Salary);
                    newloan.child("comms").setValue("0");
                    newloan.child("other").setValue("0");

                    DatabaseReference newloane=mDatabaseMembers.child(user_id).child("deductions");
                    newloane.child("nssf").setValue("0");
                    newloane.child("nhif").setValue("0");
                    newloane.child("paye").setValue("0");

                    DatabaseReference newloanl=mDatabaseMembers.child(user_id).child("loans");
                    newloanl.child("totalloan").setValue("0");

                    DatabaseReference newloana=mDatabaseMembers.child(user_id).child("advances");
                    newloana.child("currentadvance").setValue("0");



                    mProgressdialog.dismiss();
                    Toast.makeText(addemployee.this, "Account Setup Finished!", Toast.LENGTH_LONG).show();

                    Intent backtosignin =new Intent(addemployee.this,Main.class);
                    backtosignin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(backtosignin);
                    finish();
                }
            });
        }else {
            Toast.makeText(addemployee.this, "Please Enter all details", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            mImageuri=data.getData();
            mMemberImage.setImageURI(mImageuri);
        }
    }
}
