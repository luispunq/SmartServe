package com.example.london.smartserve;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class loanapplication extends AppCompatActivity {

    private ImageView mMemberphoto;
    private TextView mMembername,mGroupName,mTotalSavings,mLoanlimit,mIntrest,mInstallments,mAttach;
    private EditText mAmountreq,mInstallmentperiod,gurname1,gurname2,gurname3,gurid1
            ,gurid2,gurid3,gurphone1,gurphone2,gurphone3,itemname1,itemname2,itemname3,mkt1,mkt2
            ,mkt3,cmp1,cmp2,cmp3,sno1,sno2,sno3,reco;
    private Button mCommit,sav1,sav2,sav3,sav4,sav5,sav6;
    private DatabaseReference mDatabasemembers,mDatabasegroup,mDatabaseguarantors,loanrequest,extras,reference,data;
    private StorageReference store;
    public static final int GALLERY_PICK = 1889;
    private FirebaseAuth mAuth;
    private String user,grid=null,mUser,user_name=null;
    private LinearLayout lay1,lay2,lay3,lay4,lay5,lay6;
    private ProgressDialog mProgressdialog;
    private Uri mImageuri=null;
    private int monnthlypay=0;
    private RelativeLayout rl1,rl2,rl3,rl4,rl5,rl6;
    private String totalsavings=null,name=null,image=null,group=null,loankey=null,gkey=null,type=null,gname=null;
    private double finalamount;
    private Toolbar mBar;
    private android.support.v7.app.AlertDialog alert11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loanapplication);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser().getUid();
        mBar=findViewById(R.id.bard);
        setSupportActionBar(mBar);
        Bundle extra = getIntent().getExtras();
        user = extra.getString("key");
        gkey = extra.getString("gkey");
        type = extra.getString("type");
        mMemberphoto=findViewById(R.id.loanappmemberprofpic);
        mMembername=findViewById(R.id.loanappmembername);
        mGroupName=findViewById(R.id.loanappmembergroup);
        mTotalSavings=findViewById(R.id.savingstat);
        mLoanlimit=findViewById(R.id.loanstat);
        mIntrest=findViewById(R.id.loanintrest);
        gurname1=findViewById(R.id.gurname1);
        gurname2=findViewById(R.id.gurname2);
        gurname3=findViewById(R.id.gurname3);
        gurid1=findViewById(R.id.gurid1);
        gurid2=findViewById(R.id.gurid2);
        gurid3=findViewById(R.id.gurid3);
        gurphone1=findViewById(R.id.gurcont1);
        gurphone2=findViewById(R.id.gurcont2);
        gurphone3=findViewById(R.id.gurcont3);
        itemname1=findViewById(R.id.itemname1);
        itemname2=findViewById(R.id.itemname2);
        itemname3=findViewById(R.id.itemname3);
        mkt1=findViewById(R.id.itemmktval1);
        mkt2=findViewById(R.id.itemmktval2);
        mkt3=findViewById(R.id.itemmktval3);
        cmp1=findViewById(R.id.itemcmpval1);
        cmp2=findViewById(R.id.itemcmpval2);
        cmp3=findViewById(R.id.itemcmpval3);
        sno1=findViewById(R.id.itemSNO1);
        sno2=findViewById(R.id.itemSNO2);
        sno3=findViewById(R.id.itemSNO3);
        reco=findViewById(R.id.recomendation);
        lay1=findViewById(R.id.gur1);
        lay2=findViewById(R.id.gur2);
        lay3=findViewById(R.id.gur3);
        lay4=findViewById(R.id.sec1);
        lay5=findViewById(R.id.sec2);
        lay6=findViewById(R.id.sec3);
        rl1=findViewById(R.id.rel1);
        rl2=findViewById(R.id.rel2);
        rl3=findViewById(R.id.rel3);
        rl4=findViewById(R.id.rel4);
        rl5=findViewById(R.id.rel5);
        rl6=findViewById(R.id.rel6);
        mInstallments=findViewById(R.id.title14);
        mAmountreq=findViewById(R.id.reqamount);
        mInstallmentperiod=findViewById(R.id.title10);
        mAttach=findViewById(R.id.title15);
        mInstallmentperiod.addTextChangedListener(filterTextWatcher);
        mCommit=findViewById(R.id.loanappbutton);
        sav1=findViewById(R.id.save1);
        sav2=findViewById(R.id.save2);
        sav3=findViewById(R.id.save3);
        sav4=findViewById(R.id.save4);
        sav5=findViewById(R.id.save5);
        sav6=findViewById(R.id.save6);
        reference=FirebaseDatabase.getInstance().getReference();
        mDatabasemembers= FirebaseDatabase.getInstance().getReference().child("members").child(user);
        mDatabasegroup=FirebaseDatabase.getInstance().getReference().child("loanrequests");
        mDatabaseguarantors=FirebaseDatabase.getInstance().getReference().child("loanextras");
        store= FirebaseStorage.getInstance().getReference().child("loanapplications");
        data=FirebaseDatabase.getInstance().getReference().child("forms");


        loanrequest=mDatabasegroup.push();
        loankey=loanrequest.getKey();


        mProgressdialog=new ProgressDialog(this);


        reference.child("Employees").child(mUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_name= (String) dataSnapshot.child("name").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Groups").child(gkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gname=dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (type.equals("form")){
            CardView v1=findViewById(R.id.gurantordetails);
            CardView v2=findViewById(R.id.securitydetails);
            CardView v3=findViewById(R.id.recommendations);

            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
        }

        sav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressdialog.setTitle("Wait..");
                mProgressdialog.show();

                final RecyclerView recyclerView=new RecyclerView(loanapplication.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(loanapplication.this));
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(loanapplication.this);
                builder.setTitle("Select Guarantor")
                        .setView(recyclerView);
                recycleradapt(recyclerView,gkey,"1");

                alert11 = builder.create();
                alert11.show();

                extras=mDatabaseguarantors.child(loankey).child("guarantors").push();

                extras.child("name").setValue(gurname1.getText().toString());
                extras.child("id").setValue(gurid1.getText().toString());
                extras.child("contact").setValue(gurphone1.getText().toString());
                mProgressdialog.dismiss();

                View view=findViewById(R.id.gur1);
                view.setVisibility(View.GONE);
            }
        });

        sav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressdialog.setTitle("Wait..");
                mProgressdialog.show();
                extras=mDatabaseguarantors.child(loankey).child("guarantors").push();

                final RecyclerView recyclerView=new RecyclerView(loanapplication.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(loanapplication.this));
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(loanapplication.this);
                builder.setTitle("Select Guarantor")
                        .setView(recyclerView);
                recycleradapt(recyclerView,gkey,"2");

                alert11 = builder.create();
                alert11.show();

                extras.child("name").setValue(gurname2.getText().toString());
                extras.child("id").setValue(gurid2.getText().toString());
                extras.child("contact").setValue(gurphone2.getText().toString());
                mProgressdialog.dismiss();
                View view=findViewById(R.id.gur2);
                view.setVisibility(View.GONE);
            }
        });

        sav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressdialog.setTitle("Wait..");
                mProgressdialog.show();
                extras=mDatabaseguarantors.child(loankey).child("guarantors").push();

                final RecyclerView recyclerView=new RecyclerView(loanapplication.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(loanapplication.this));
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(loanapplication.this);
                builder.setTitle("Select Guarantor")
                        .setView(recyclerView);
                recycleradapt(recyclerView,gkey,"3");

                alert11 = builder.create();
                alert11.show();

                extras.child("name").setValue(gurname3.getText().toString());
                extras.child("id").setValue(gurid3.getText().toString());
                extras.child("contact").setValue(gurphone3.getText().toString());
                mProgressdialog.dismiss();
                View view=findViewById(R.id.gur3);
                view.setVisibility(View.GONE);
            }
        });

        sav4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressdialog.setTitle("Wait..");
                mProgressdialog.show();
                extras=mDatabaseguarantors.child(loankey).child("security").push();

                extras.child("itemname").setValue(itemname1.getText().toString());
                extras.child("marketvalue").setValue(mkt1.getText().toString());
                extras.child("compvalue").setValue(cmp1.getText().toString());
                extras.child("sno").setValue(sno1.getText().toString());
                mProgressdialog.dismiss();
                View view=findViewById(R.id.sec1);
                view.setVisibility(View.GONE);
            }
        });

        sav5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressdialog.setTitle("Wait..");
                mProgressdialog.show();
                extras=mDatabaseguarantors.child(loankey).child("security").push();

                extras.child("itemname").setValue(itemname2.getText().toString());
                extras.child("marketvalue").setValue(mkt2.getText().toString());
                extras.child("compvalue").setValue(cmp2.getText().toString());
                extras.child("sno").setValue(sno2.getText().toString());
                mProgressdialog.dismiss();
                View view=findViewById(R.id.sec2);
                view.setVisibility(View.GONE);
            }
        });

        sav6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressdialog.setTitle("Wait..");
                mProgressdialog.show();
                extras=mDatabaseguarantors.child(loankey).child("security").push();

                extras.child("itemname").setValue(itemname3.getText().toString());
                extras.child("marketvalue").setValue(mkt3.getText().toString());
                extras.child("compvalue").setValue(cmp3.getText().toString());
                extras.child("sno").setValue(sno3.getText().toString());
                mProgressdialog.dismiss();
                View view=findViewById(R.id.sec3);
                view.setVisibility(View.GONE);
            }
        });

        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rl1.getVisibility()==View.VISIBLE){
                    rl1.setVisibility(View.GONE);
                }else{
                    rl1.setVisibility(View.VISIBLE);
                }
            }
        });
        lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rl2.getVisibility()==View.VISIBLE){
                    rl2.setVisibility(View.GONE);
                }else{
                    rl2.setVisibility(View.VISIBLE);
                }
            }
        });
        lay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rl2.getVisibility()==View.VISIBLE){
                    rl3.setVisibility(View.GONE);
                }else{
                    rl3.setVisibility(View.VISIBLE);
                }
            }
        });
        lay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rl4.getVisibility()==View.VISIBLE){
                    rl4.setVisibility(View.GONE);
                }else{
                    rl4.setVisibility(View.VISIBLE);
                }
            }
        });
        lay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rl5.getVisibility()==View.VISIBLE){
                    rl5.setVisibility(View.GONE);
                }else{
                    rl5.setVisibility(View.VISIBLE);
                }
            }
        });
        lay6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rl6.getVisibility()==View.VISIBLE){
                    rl6.setVisibility(View.GONE);
                }else{
                    rl6.setVisibility(View.VISIBLE);
                }
            }
        });

        mAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(loanapplication.this);
                alertDialog.setTitle("Select Image Source")
                        .setItems(R.array.Options2,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, 1888);
                                } else if (which == 1) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_PICK);
                                }
                            }
                        });
            }
        });

        mDatabasemembers.child("details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name= (String) dataSnapshot.child("name").getValue();
                image= (String) dataSnapshot.child("profileImage").getValue();
                group=(String)dataSnapshot.child("group").getValue();
                grid=dataSnapshot.child("groupid").getValue().toString();
                mMembername.setText(name);
                mGroupName.setText(group);
                Picasso.with(loanapplication.this).load(image).into(mMemberphoto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasemembers.child("savings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalsavings= (String) dataSnapshot.child("totalsavings").getValue();

                if (totalsavings==null){
                    totalsavings="1";
                }

                mTotalSavings.setText("Kshs."+totalsavings);
                double savings=Double.parseDouble(totalsavings);
                double limit=savings*3;
                String loanlimit=String.valueOf(limit);
                mLoanlimit.setText("Kshs. "+loanlimit);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("fill")){
                    applyloan();
                }else {
                    applyform();
                }

            }
        });

    }

    private void applyform() {

        DatabaseReference newform=data.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        newform.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        newform.child("groupname").setValue(gname);
        newform.child("name").setValue(name);
        newform.child("amount").setValue(mAmountreq.getText().toString());
        newform.child("months").setValue(mInstallmentperiod.getText().toString());
        newform.child("installments").setValue(String.valueOf(monnthlypay));
        newform.child("total").setValue(String.valueOf((int)(finalamount)));
        newform.child("status").setValue("booked");
        newform.child("officer").setValue(user_name);
        newform.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference memberforms=FirebaseDatabase.getInstance().getReference().child("members").child(user);
        memberforms.child("forms").child("id").setValue(newform.getKey());

        DatabaseReference newquerry=FirebaseDatabase.getInstance().getReference().child("helpqueries").push();
        newquerry.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        newquerry.child("user").setValue(mUser);
        newquerry.child("message").setValue("Form booked for "+name);
        newquerry.child("status").setValue("pending");

        Toast.makeText(loanapplication.this, "Form Booked!", Toast.LENGTH_LONG).show();

        Intent back=new Intent(loanapplication.this,memberlistc.class);
        back.putExtra("key",grid);
        startActivity(back);
        finish();
    }


    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS

            double principal=Integer.parseInt(mAmountreq.getText().toString());
            double rate=Integer.parseInt(mIntrest.getText().toString());
            String timet=mInstallmentperiod.getText().toString();

            if (timet.equals("")){
                timet="1";

            }

            double time=Double.parseDouble(timet);

            finalamount=(principal*0.01*time)+principal;

            monnthlypay=(int)(finalamount/time);

            mInstallments.setText("Kshs. "+String.valueOf(monnthlypay));

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void applyloan() {
        mProgressdialog.setMessage("Please wait..");
        mProgressdialog.setMessage("Applying for Loan..");
        mProgressdialog.setCanceledOnTouchOutside(false);
        mProgressdialog.show();

        final String amountrequested=String.valueOf((int)(finalamount));
        final String amountcash=mAmountreq.getText().toString();
        final String installmentperiod=mInstallmentperiod.getText().toString();
        final String rate=mIntrest.getText().toString();
        final String monthlyins=String.valueOf(monnthlypay);
        final String recoms=reco.getText().toString();

        loanrequest.child("requestid").setValue(loankey);
        loanrequest.child("requestdate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        loanrequest.child("amountcash").setValue(amountcash);
        loanrequest.child("userid").setValue(user);
        loanrequest.child("amountrequested").setValue(amountrequested);
        loanrequest.child("rate").setValue(rate);
        loanrequest.child("period").setValue(installmentperiod);
        loanrequest.child("monthlypay").setValue(monthlyins);
        loanrequest.child("status").setValue("pending");
        loanrequest.child("recommendation").setValue(recoms);
        loanrequest.child("image").setValue("default");
        loanrequest.child("officer").setValue(user_name);


        //loanadvisory();

        DatabaseReference endmeetnot=reference.child("Notifications").child("LoanApps").push();
        endmeetnot.child("amount").setValue(amountcash);
        endmeetnot.child("user").setValue(name);

        DatabaseReference newquerry=FirebaseDatabase.getInstance().getReference().child("helpqueries").push();
        newquerry.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        newquerry.child("user").setValue(mUser);
        newquerry.child("message").setValue("Form filled for "+name);
        newquerry.child("status").setValue("pending");

        FirebaseDatabase.getInstance().getReference().child("members").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("forms").child("id").exists()){
                    String id=dataSnapshot.child("forms").child("id").getValue().toString();

                    DatabaseReference newform=data.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").child(id);
                    newform.child("status").setValue("filled");
                    newform.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    newform.child("groupname").setValue(gname);
                    newform.child("name").setValue(name);
                    newform.child("amount").setValue(mAmountreq.getText().toString());
                    newform.child("months").setValue(mInstallmentperiod.getText().toString());
                    newform.child("installments").setValue(String.valueOf(monnthlypay));
                    newform.child("total").setValue(String.valueOf((int)(finalamount)));
                    newform.child("officer").setValue(user_name);
                    newform.child("timestamp").setValue(ServerValue.TIMESTAMP);
                }else {
                    DatabaseReference newforms=data.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                    newforms.child("status").setValue("filled");
                    newforms.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    newforms.child("groupname").setValue(gname);
                    newforms.child("name").setValue(name);
                    newforms.child("amount").setValue(mAmountreq.getText().toString());
                    newforms.child("months").setValue(mInstallmentperiod.getText().toString());
                    newforms.child("installments").setValue(String.valueOf(monnthlypay));
                    newforms.child("total").setValue(String.valueOf((int)(finalamount)));
                    newforms.child("officer").setValue(user_name);
                    newforms.child("timestamp").setValue(ServerValue.TIMESTAMP);

                    DatabaseReference memberforms=FirebaseDatabase.getInstance().getReference().child("members").child(user);
                    memberforms.child("forms").child("id").setValue(newforms.getKey());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mProgressdialog.dismiss();
        Toast.makeText(loanapplication.this, "Loan Applied!", Toast.LENGTH_LONG).show();

        Intent back=new Intent(loanapplication.this,memberlistc.class);
        back.putExtra("key",grid);
        startActivity(back);
        finish();
    }

    private void loanadvisory() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_PICK)
                //onSelectFromGalleryResult(data);
                mImageuri=data.getData();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_loans,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.action_switch){
            FirebaseAuth.getInstance().signOut();
            Intent loggetout=new Intent(loanapplication.this,loanapplication2.class);
            Bundle extras = new Bundle();
            extras.putString("key", user);
            extras.putString("gkey", gkey);
            loggetout.putExtras(extras);
            startActivity(loggetout);
            finish();
        }
        return true;
    }

    private void recycleradapt(RecyclerView r, final String b_key, final String choice) {
        FirebaseRecyclerAdapter<member,membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member,membersViewHolder>(
                member.class,
                R.layout.member_row,
                membersViewHolder.class,
                reference.child("members").child("allmembers").child(b_key)
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder viewHolder, final member model, int position) {


                viewHolder.setMemberPhoto(getApplicationContext(),model.getProfileImage());
                viewHolder.setMemberName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (choice) {
                            case "1":
                                gurname1.setText(model.getName());
                                gurid1.setText(model.getId());
                                alert11.dismiss();
                                break;
                            case "2":
                                gurname2.setText(model.getName());
                                gurid2.setText(model.getId());
                                alert11.dismiss();
                                break;
                            case "3":
                                gurname3.setText(model.getName());
                                gurid3.setText(model.getId());
                                alert11.dismiss();
                                break;
                        }
                    }
                });


            }
        };
        r.setAdapter(firebaseRecyclerAdapter);

    }

    public static class membersViewHolder extends RecyclerView.ViewHolder{
        View mView;


        public membersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setMemberName(String name){
            TextView membername = (TextView)mView.findViewById(R.id.membercardname);
            membername.setText(name);
        }
        public void setMemberPhoto(Context ctx, String image){
            ImageView memberphoto = (ImageView) mView.findViewById(R.id.membercardphoto);
            Picasso.with(ctx).load(image).into(memberphoto);
        }
    }
}
