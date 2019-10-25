package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class groupsetting extends AppCompatActivity {
    private ImageView mGroupImage;
    private TextView mGroupName;
    private ImageView mMemberImage;
    private EditText mName,mID,mGroupname,mType;
    private EditText mSex;
    private Button mSubmit;
    private DatabaseReference mDatabasemembers1,mDatabase2,mNumbers;
    private StorageReference mProfileImageStore;
    private static final int GALLERY_REQUEST=1;
    private Uri mImageuri=null;
    private FirebaseAuth mAuth;
    private String user;
    private ProgressDialog mProgressdialog;
    private String newnum=null,unow=null;
    private String psex=null,grp=null,type=null;
    private String gname=null,url,id;
    private DatabaseReference mDatabasefees,mD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupsetting);
        mAuth=FirebaseAuth.getInstance();
        mProgressdialog=new ProgressDialog(this);
        user=mAuth.getCurrentUser().getUid();
        grp=getIntent().getExtras().getString("key");
        mGroupImage=findViewById(R.id.addmemberimageView);
        mGroupName=findViewById(R.id.addmembergroupname);
        mMemberImage=findViewById(R.id.imageView5);
        mName=findViewById(R.id.newname);
        mID=findViewById(R.id.idnumber);
        mSubmit=findViewById(R.id.button);
        mSex=findViewById(R.id.sex);
        mDatabasemembers1= FirebaseDatabase.getInstance().getReference().child("details").child(grp);
        mD=FirebaseDatabase.getInstance().getReference();
        mProfileImageStore= FirebaseStorage.getInstance().getReference().child("groupphotos");



        mDatabasemembers1.child("groupdetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String curname=dataSnapshot.child("groupName").getValue().toString();
                String im=dataSnapshot.child("profileImage").getValue().toString();
                String nums=dataSnapshot.child("num").getValue().toString();
                Picasso.with(groupsetting.this).load(im).into(mMemberImage);
                mGroupName.setText(curname);
                mID.setText(nums);
                mName.setText(curname);
                url=im;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                newgroup();
            }
        });
    }

    private void newgroup() {
        final String name=mName.getText().toString();
        final String num=mID.getText().toString();
        final String bran=mSex.getText().toString();

        mProgressdialog.setMessage("Changing Account..");
        mProgressdialog.setMessage("Modifying: "+mGroupName.getText().toString());
        mProgressdialog.setCanceledOnTouchOutside(false);
        mProgressdialog.show();

        DatabaseReference newGrp=mD.child("Groups").child(grp);
        //grpkey=newGrp.getKey();
        newGrp.child("name").setValue(name);
        newGrp.child("num").setValue(num);


        mDatabasemembers1.child("groupdetails").child("groupName").setValue(name);
        mDatabasemembers1.child("groupdetails").child("branch").setValue(bran);
        mDatabasemembers1.child("groupdetails").child("num").setValue(num);

            mProgressdialog.dismiss();
            Toast.makeText(groupsetting.this, "Group Info Updated!", Toast.LENGTH_LONG).show();

            Intent backtosignin =new Intent(groupsetting.this,groupstats.class);
            backtosignin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            backtosignin.putExtra("key",grp);
            startActivity(backtosignin);
            finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            mImageuri=data.getData();
            mMemberImage.setImageURI(mImageuri);
            mProgressdialog.setMessage("Changing Group Image..");
            mProgressdialog.setCanceledOnTouchOutside(false);
            mProgressdialog.show();

            StorageReference filepath = mProfileImageStore.child(grp+"update");
            filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl=taskSnapshot.getDownloadUrl().toString();
                    mDatabasemembers1.child("groupdetails").child("profileImage").setValue(downloadUrl);

                    mProgressdialog.dismiss();
                    Toast.makeText(groupsetting.this, "Account Picture Updated!", Toast.LENGTH_LONG).show();
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
