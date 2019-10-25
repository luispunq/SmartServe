package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class addmember extends AppCompatActivity {
    private ImageView mGroupImage;
    private TextView mGroupName;
    private ImageView mMemberImage;
    private EditText mName,mID,mGroupname;
    private Spinner mSex,mType;
    private Button mSubmit;
    private DatabaseReference mDatabasemembers1,mDatabase2,accounting;
    private StorageReference mProfileImageStore;
    private static final int GALLERY_REQUEST=1;
    private Uri mImageuri=null;
    private FirebaseAuth mAuth;
    private String user;
    private ProgressDialog mProgressdialog;
    private String newnum=null,unow=null;
    private String psex=null,grp=null,type=null,ptype;
    private String gname=null;
    private DatabaseReference mDatabasefees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        Bundle extras = getIntent().getExtras();
        grp = extras.getString("key");
        mGroupImage=findViewById(R.id.addmemberimageView);
        mGroupName=findViewById(R.id.addmembergroupname);
        mMemberImage=findViewById(R.id.imageView5);
        mName=findViewById(R.id.newname);
        mID=findViewById(R.id.idnumber);
        mType=findViewById(R.id.mbrclrnce);
        mGroupname=findViewById(R.id.group);
        mSubmit=findViewById(R.id.button);
        mSex= findViewById(R.id.sex);
        mDatabasemembers1= FirebaseDatabase.getInstance().getReference().child("details").child(grp).child("groupdetails");
        mDatabase2=FirebaseDatabase.getInstance().getReference().child("members");
        mProfileImageStore= FirebaseStorage.getInstance().getReference().child("profileimages");
        accounting=FirebaseDatabase.getInstance().getReference().child("Accounting");
        mDatabasefees=FirebaseDatabase.getInstance().getReference().child("fees");

        mDatabasemembers1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gname=(String)dataSnapshot.child("groupName").getValue();
                mGroupName.setText(dataSnapshot.child("groupName").getValue().toString());
                mGroupname.setText(gname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("details").child(grp).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type=dataSnapshot.child("type").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSex.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.memberopt,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(adapter2);


        mProgressdialog=new ProgressDialog(this);



        mSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                psex=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ptype=parent.getItemAtPosition(position).toString();
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
        final String GroupName=mGroupname.getText().toString().trim();
        final String user_id=mAuth.getCurrentUser().getUid();
        final String userlevel=ptype;
        String ima=mImageuri.getLastPathSegment();

        if(!ima.isEmpty()&&!TextUtils.isEmpty(Username)&&!TextUtils.isEmpty(ID)&&!TextUtils.isEmpty(Gender)&&!TextUtils.isEmpty(userlevel)){
            mProgressdialog.setMessage("Setting Up Account..");
            mProgressdialog.setMessage("Registering: "+Username);
            mProgressdialog.setCanceledOnTouchOutside(false);
            mProgressdialog.show();
            StorageReference filepath = mProfileImageStore.child(user);
            filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl=taskSnapshot.getDownloadUrl().toString();

                    mDatabase2.child(user).child("details").child("name").setValue(Username);
                    mDatabase2.child(user).child("details").child("flag").setValue("new");
                    mDatabase2.child(user).child("details").child("id").setValue(ID);
                    mDatabase2.child(user).child("details").child("clearance").setValue(userlevel);
                    mDatabase2.child(user).child("details").child("profileImage").setValue(downloadUrl);
                    mDatabase2.child(user).child("details").child("gender").setValue(Gender);
                    mDatabase2.child(user).child("details").child("group").setValue(gname);
                    mDatabase2.child(user).child("details").child("groupid").setValue(grp);

                    DatabaseReference newmember=mDatabase2.child("allmembers").child(grp).push();
                    newmember.child("name").setValue(Username);
                    newmember.child("profileImage").setValue(downloadUrl);
                    newmember.child("gender").setValue(Gender);
                    newmember.child("group").setValue(GroupName);
                    newmember.child("id").setValue(ID);
                    newmember.child("uid").setValue(user);


                    DatabaseReference template=mDatabase2.child(user);
                    template.child("savings").child("totalsavings").setValue("0");
                    template.child("loans").child("totalloan").setValue("0");
                    template.child("loans").child("installment").setValue("0");
                    template.child("advances").child("currpenalty").setValue("0");
                    template.child("advances").child("currentadvance").setValue("0");

                    DatabaseReference templatep=mDatabase2.child(user).child("project");
                    templatep.child("savings").child("totalsavings").setValue("0");
                    templatep.child("loans").child("totalloan").setValue("0");
                    templatep.child("loans").child("installment").setValue("0");
                    templatep.child("advances").child("currpenalty").setValue("0");
                    templatep.child("advances").child("currentadvance").setValue("0");

                    mDatabasemembers1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String totalnumbers= (String) dataSnapshot.child("numbers").getValue();
                            if (totalnumbers==null){
                                totalnumbers="0";
                            }
                            int oldnums=Integer.parseInt(totalnumbers);
                            int newnums=oldnums+1;
                            newnum=String.valueOf(newnums);
                            DatabaseReference updatenums=mDatabasemembers1.child("numbers");
                            updatenums.setValue(newnum);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mProgressdialog.dismiss();


                    final EditText misccash=new EditText(addmember.this);
                    misccash.setInputType(InputType.TYPE_CLASS_NUMBER);
                    misccash.setHint("Enter Amount");
                    final AlertDialog.Builder builders = new AlertDialog.Builder(addmember.this);
                    builders.setTitle("Registration Fees")
                            .setMessage("Enter Amount paid and Payment Type")
                            .setCancelable(false)
                            .setView(misccash)
                            .setPositiveButton("Cash", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseReference newfee=mDatabasefees.child(grp).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("memberregistration").push();
                                    switch (type) {
                                        case "Individual Loan Acccount":
                                            newfee.child("amount").setValue(misccash.getText().toString());
                                            newfee.child("groupid").setValue(grp);
                                            break;
                                        case "Individual Member Investment":
                                            newfee.child("amount").setValue(misccash.getText().toString());
                                            newfee.child("groupid").setValue(grp);
                                            break;
                                        case "Normal Groups":
                                            newfee.child("amount").setValue(misccash.getText().toString());
                                            newfee.child("groupid").setValue(grp);
                                            break;
                                        case "Group Investment Account":
                                            newfee.child("amount").setValue(misccash.getText().toString());
                                            newfee.child("groupid").setValue(grp);
                                            break;
                                    }


                                    DatabaseReference fees1=accounting.child("Feesfines").child(grp).child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                    fees1.child("name").setValue("Fees");
                                    fees1.child("amount").setValue(misccash.getText().toString());
                                    fees1.child("type").setValue("blue");
                                    fees1.child("meet").setValue("-");
                                    fees1.child("group").setValue(grp);
                                    fees1.child("debitac").setValue("BankCash");
                                    fees1.child("creditac").setValue("-");
                                    fees1.child("description").setValue("Registration Fees for "+Username);
                                    fees1.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference fees2=accounting.child("BankCash").child(grp).child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                    fees2.child("name").setValue("Fees");
                                    fees2.child("amount").setValue(misccash.getText().toString());
                                    fees2.child("meet").setValue("-");
                                    fees2.child("group").setValue(grp);
                                    fees2.child("type").setValue("blue");
                                    fees2.child("debitac").setValue("Feesfines");
                                    fees2.child("creditac").setValue("-");
                                    fees2.child("description").setValue("Registration Fees for"+Username);
                                    fees2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    dialog.dismiss();

                                    Toast.makeText(addmember.this, "Account Setup Finished!", Toast.LENGTH_LONG).show();
                                    freeMemory();


                                    Intent backtosignin =new Intent(addmember.this,Main.class);
                                    mAuth.signOut();
                                    backtosignin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(backtosignin);
                                    finish();
                                }
                            })
                            .setNegativeButton("Mpesa", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseReference newfee=mDatabasefees.child(grp).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("memberregistration").push();
                                    switch (type) {
                                        case "Individual Loan Acccount":
                                            newfee.child("amount").setValue(misccash.getText().toString());
                                            newfee.child("groupid").setValue("mpesa");
                                            break;
                                        case "Individual Member Investment":
                                            newfee.child("amount").setValue(misccash.getText().toString());
                                            newfee.child("groupid").setValue("mpesa");
                                            break;
                                        case "Normal Groups":
                                            newfee.child("amount").setValue(misccash.getText().toString());
                                            newfee.child("groupid").setValue("mpesa");
                                            break;
                                        case "Group Investment Account":
                                            newfee.child("amount").setValue(misccash.getText().toString());
                                            newfee.child("groupid").setValue("mpesa");
                                            break;
                                    }
                                    DatabaseReference fees1=accounting.child("Feesfines").child(grp).child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                    fees1.child("name").setValue("Fees");
                                    fees1.child("amount").setValue(misccash.getText().toString());
                                    fees1.child("type").setValue("blue");
                                    fees1.child("meet").setValue("-");
                                    fees1.child("group").setValue(grp);
                                    fees1.child("debitac").setValue("BankCash");
                                    fees1.child("creditac").setValue("-");
                                    fees1.child("description").setValue("Registration Fees for "+Username);
                                    fees1.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference fees2=accounting.child("BankCash").child(grp).child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                    fees2.child("name").setValue("Fees");
                                    fees2.child("amount").setValue(misccash.getText().toString());
                                    fees2.child("meet").setValue("-");
                                    fees2.child("group").setValue(grp);
                                    fees2.child("type").setValue("blue");
                                    fees2.child("debitac").setValue("Feesfines");
                                    fees2.child("creditac").setValue("-");
                                    fees2.child("description").setValue("Registration Fees for"+Username);
                                    fees2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    dialog.dismiss();
                                    Toast.makeText(addmember.this, "Account Setup Finished!", Toast.LENGTH_LONG).show();
                                    freeMemory();

                                    DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("NewMember").push();
                                    endmeetnot.child("name").setValue(Username);
                                    endmeetnot.child("group").setValue(gname);



                                    Intent backtosignin =new Intent(addmember.this,Main.class);
                                    mAuth.signOut();
                                    backtosignin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(backtosignin);
                                    finish();
                                }
                            });
                    AlertDialog alert121 = builders.create();
                    alert121.show();
                }
            });
        }else {
            Toast.makeText(addmember.this, "Please Enter all details", Toast.LENGTH_LONG).show();
        }

    }


    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
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
