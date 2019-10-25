package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class editmember extends AppCompatActivity {
    private ImageView mGroupImage;
    private TextView mGroupName;
    private ImageView mMemberImage;
    private EditText mName,mID,mGroupname,mCode,mSex;
    private Spinner mSexx;
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
    private DatabaseReference mDatabasefees,mD,mD2;
    private Toolbar mBar;
    private EditText gnaame;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmember);
        mAuth=FirebaseAuth.getInstance();
        mBar=findViewById(R.id.bar55);
        setSupportActionBar(mBar);
        user=mAuth.getCurrentUser().getUid();
        //grp=getIntent().getExtras().getString("key");
        Bundle extras = getIntent().getExtras();
        grp = extras.getString("key");
        unow = extras.getString("user");
        mGroupImage=findViewById(R.id.addmemberimageView);
        mGroupName=findViewById(R.id.addmembergroupname);
        mMemberImage=findViewById(R.id.imageView5);
        mName=findViewById(R.id.newname);
        mID=findViewById(R.id.idnumber);
        mSubmit=findViewById(R.id.button);
        mSex=(EditText) findViewById(R.id.sex);
        mSexx=(Spinner) findViewById(R.id.gen);
        mCode=findViewById(R.id.code);
        mDatabasemembers1= FirebaseDatabase.getInstance().getReference().child("details").child(grp);
        mDatabase2=FirebaseDatabase.getInstance().getReference().child("members").child(unow);
        mProfileImageStore= FirebaseStorage.getInstance().getReference().child("profileimages");
        mDatabasefees=FirebaseDatabase.getInstance().getReference().child("fees");
        mD=FirebaseDatabase.getInstance().getReference();
        mNumbers=FirebaseDatabase.getInstance().getReference().child("details");
        mD2=FirebaseDatabase.getInstance().getReference().child("members");

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSexx.setAdapter(adapter);
        mProgressdialog=new ProgressDialog(this);

        mDatabase2.child("details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String curname=dataSnapshot.child("name").getValue().toString();
                String im=dataSnapshot.child("profileImage").getValue().toString();
                Picasso.with(editmember.this).load(im).into(mMemberImage);
                id=dataSnapshot.child("id").getValue().toString();
                mID.setText(id);
                mName.setText(curname);
                url=im;
                if (dataSnapshot.child("contact").exists()){
                    mSex.setText(dataSnapshot.child("contact").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasemembers1.child("groupdetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gname=(String)dataSnapshot.child("groupName").getValue();
                type=dataSnapshot.child("type").getValue().toString();
                mGroupName.setText(gname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSexx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                psex=parent.getItemAtPosition(position).toString();
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
        final String Gender=mSex.getText().toString();
        final String ccode=mCode.getText().toString().trim();



            mProgressdialog.setMessage("Changing Account..");
            mProgressdialog.setMessage("Modifying: "+Username);
            mProgressdialog.setCanceledOnTouchOutside(false);
            mProgressdialog.show();


        mDatabase2.child("details").child("name").setValue(Username);
        mDatabase2.child("details").child("id").setValue(ID);
        mDatabase2.child("details").child("clearance").setValue("member");
        mDatabase2.child("details").child("contact").setValue(Gender);
        mDatabase2.child("details").child("group").setValue(gname);
        mDatabase2.child("details").child("groupid").setValue(grp);
        mDatabase2.child("details").child("gender").setValue(psex);
        mDatabase2.child("details").child("code").setValue(ccode);


        mD2.child("allmembers").child(grp).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.child("uid").getValue().toString().equals(unow)){
                        mD2.child("allmembers").child(grp).child(snapshot.getKey()).child("name").setValue(Username);
                        mD2.child("allmembers").child(grp).child(snapshot.getKey()).child("contact").setValue(Gender);
                        mD2.child("allmembers").child(grp).child(snapshot.getKey()).child("group").setValue(gname);
                        mD2.child("allmembers").child(grp).child(snapshot.getKey()).child("id").setValue(ID);
                        mD2.child("allmembers").child(grp).child(snapshot.getKey()).child("uid").setValue(unow);
                        mD2.child("allmembers").child(grp).child(snapshot.getKey()).child("gender").setValue(psex);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mProgressdialog.dismiss();
                    Toast.makeText(editmember.this, "Account Setup Finished!", Toast.LENGTH_LONG).show();
                    freeMemory();
                    Intent backtosignin =new Intent(editmember.this,groupstats.class);
                    backtosignin.putExtra("key", grp);
                    backtosignin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(backtosignin);
                    finish();

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
            mProgressdialog.setMessage("Changing Member Image..");
            mProgressdialog.setCanceledOnTouchOutside(false);
            mProgressdialog.show();

            StorageReference filepath = mProfileImageStore.child(user+"update");
            filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUrl=taskSnapshot.getDownloadUrl().toString();
                    mDatabase2.child("details").child("profileImage").setValue(downloadUrl);

                    DatabaseReference newmember=mD2.child("allmembers").child(grp).child(unow);
                    newmember.child("profileImage").setValue(downloadUrl);


                    mProgressdialog.dismiss();
                    Toast.makeText(editmember.this, "Account Setup Finished!", Toast.LENGTH_LONG).show();
                    freeMemory();
                }
            });

        }
    }

    private void recycleradapt(RecyclerView r,String nam) {

        Query firebaseSearchQuery = mD.child("Groups").orderByChild("name").startAt(nam.toUpperCase()).endAt(nam.toUpperCase()+ "\uf8ff");

        FirebaseRecyclerAdapter<groups,GroupViewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<groups,GroupViewholder>(
                groups.class,
                R.layout.grouplist_row,
                GroupViewholder.class,
                firebaseSearchQuery
        )
        {
            @Override
            protected void populateViewHolder(final GroupViewholder viewHolder, final groups model, int position) {
                final String u_key=getRef(position).getKey();
                final String ggane=model.getName();

                viewHolder.setGroupName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String Username=mName.getText().toString().trim();
                        final String ID=mID.getText().toString().trim();
                        final String Gender=psex;


                        mProgressdialog.setMessage("Migrating Account..");
                        mProgressdialog.setMessage("Modifying: "+Username);
                        mProgressdialog.setCanceledOnTouchOutside(false);
                        mProgressdialog.show();


                        mD2.child("allmembers").child(grp).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    if (snapshot.child("uid").getValue().toString().equals(unow)){
                                        DatabaseReference rrm=mD2.child("allmembers").child(grp).child(snapshot.getKey());
                                        rrm.removeValue();
                                        freeMemory();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        //DatabaseReference res=mDatabase2.child(user);
                        mDatabase2.child("details").child("name").setValue(Username);
                        mDatabase2.child("details").child("id").setValue(ID);
                        mDatabase2.child("details").child("clearance").setValue("member");
                        mDatabase2.child("details").child("gender").setValue(Gender);
                        mDatabase2.child("details").child("group").setValue(ggane);
                        mDatabase2.child("details").child("groupid").setValue(u_key);

                        //DatabaseReference newmember=mDatabase2.child("allmembers").child(grp).child(user);
                        DatabaseReference res=mD2.child("allmembers").child(u_key).push();
                        res.child("name").setValue(Username);
                        res.child("gender").setValue(Gender);
                        res.child("group").setValue(ggane);
                        res.child("id").setValue(ID);
                        res.child("uid").setValue(unow);
                        res.child("profileImage").setValue(url);

                        mDatabasemembers1.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String totalnumbers= (String) dataSnapshot.child("numbers").getValue();
                                if (totalnumbers==null){
                                    totalnumbers="0";
                                }
                                int oldnums=Integer.parseInt(totalnumbers);
                                int newnums=oldnums-1;
                                newnum=String.valueOf(newnums);
                                DatabaseReference updatenums=mDatabasemembers1.child("groupdetails").child("numbers");
                                updatenums.setValue(newnum);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mNumbers.child(u_key).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String totalnumbers= (String) dataSnapshot.child("numbers").getValue();
                                if (totalnumbers==null){
                                    totalnumbers="0";
                                }
                                int oldnums=Integer.parseInt(totalnumbers);
                                int newnums=oldnums+1;
                                newnum=String.valueOf(newnums);
                                DatabaseReference updatenums=mNumbers.child(u_key).child("groupdetails").child("numbers");
                                updatenums.setValue(newnum);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        mProgressdialog.dismiss();
                        Toast.makeText(editmember.this, "Account Transfer Finished!", Toast.LENGTH_LONG).show();
                        freeMemory();
                        Intent backtosignin =new Intent(editmember.this,groupstats.class);
                        backtosignin.putExtra("key", grp);
                        backtosignin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(backtosignin);
                        finish();

                    }
                });


            }
        };
        r.setAdapter(firebaseRecyclerAdapter);

    }

    public static class GroupViewholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public GroupViewholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.groupslistcard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setGroupName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textView9);
            mGroupname.setText(groupname);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_modifymember,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.action_remove){

            mD2.child("allmembers").child(grp).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("uid").getValue().toString().equals(unow)){
                            DatabaseReference rrm=mD2.child("allmembers").child(grp).child(snapshot.getKey());
                            rrm.removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            DatabaseReference rm=mDatabase2;
            rm.removeValue();
            mDatabasemembers1.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String totalnumbers= (String) dataSnapshot.child("numbers").getValue();
                    if (totalnumbers==null){
                        totalnumbers="0";
                    }
                    int oldnums=Integer.parseInt(totalnumbers);
                    int newnums=oldnums-1;
                    newnum=String.valueOf(newnums);
                    DatabaseReference updatenums=mDatabasemembers1.child("groupdetails").child("numbers");
                    updatenums.setValue(newnum);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Intent backtosignin =new Intent(editmember.this,groupstats.class);
            backtosignin.putExtra("key", grp);
            backtosignin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(backtosignin);
            finish();
        }
        if(item.getItemId()==R.id.action_modifyacc){


            final AlertDialog.Builder builders = new AlertDialog.Builder(editmember.this);
            final LinearLayout linearLayout=new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            gnaame=new EditText(editmember.this);
            gnaame.addTextChangedListener(filterTextWatcher);
            recyclerView=new RecyclerView(editmember.this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(editmember.this));
            linearLayout.addView(gnaame);
            linearLayout.addView(recyclerView);
            builders.setTitle("Select Group to transfer")
                    .setCancelable(true)
                    .setView(linearLayout)
                    .setNeutralButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert121 = builders.create();
            alert121.show();

        }

        return true;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //DOTHECALCULATIONSHEREANDSHOWTHERESULTASPERYOURCALCULATIONS

                String uname=gnaame.getText().toString().trim();

                try {
                    recycleradapt(recyclerView,uname.toUpperCase());
                }catch (Exception e){
                    Log.e("exception","Error!");
                }


        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            try {
                recycleradapt(recyclerView,"A");
            }catch (Exception e){
                Log.e("exception","Error!");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String uname=gnaame.getText().toString().trim();
            try {
                recycleradapt(recyclerView,uname);
            }catch (Exception e){
                Log.e("exception","Error!");
            }
        }
    };
}
