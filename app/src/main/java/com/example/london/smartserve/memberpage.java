package com.example.london.smartserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class memberpage extends AppCompatActivity {
    private ImageView mMemberphoto,mContct;
    private TextView mMembername,mGroupName,mThatitle,mInsutot,mInsudate,mTotalSavings,mSavingsAmount,mNextpayDate,mSavingsMore,mSfMore,mGpmore
            ,mLoanDue,mLoanInstallment,mLoanMore,mLoanGpMore,
            mAdvaamount,mpena,mGpaccsav,mGpaccloan,mGpaccinst,mSfsavings,mSfadvance,mSfIntrest,lsfadj,loanadj,advadj,mAdvmore;
    private ScrollView normal,sf,gp;
    private DatabaseReference mDatabaseMembers,mData,md,accounting,mDatax,mDatabasegroupfinance,memacco;
    private FirebaseAuth mAuth;
    private String mUid,con="07..",name,editoption="gen";
    private CardView mLayout,mLayout2;
    private Toolbar bar;
    private String clsf,cloan,cadv,unow,uname,grpid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberpage);

        mAuth=FirebaseAuth.getInstance();
        unow=mAuth.getCurrentUser().getUid();
        mUid=getIntent().getExtras().getString("key");
        bar=findViewById(R.id.bars);
        setSupportActionBar(bar);
        gp=findViewById(R.id.groupaccountinfo);
        normal=findViewById(R.id.generalinfo);
        sf=findViewById(R.id.schoolfeesinfo);
        mLayout=findViewById(R.id.cashsummary);
        mLayout2=findViewById(R.id.advancesumm);
        mThatitle=findViewById(R.id.thatitle);
        mMemberphoto=findViewById(R.id.memberprofpic);
        mMembername=findViewById(R.id.membername);
        mGroupName=findViewById(R.id.membergroup);
        mTotalSavings=findViewById(R.id.membertotalsavings);
        mGpaccsav=findViewById(R.id.membertotalsavingsgp);
        mSfsavings=findViewById(R.id.membertotalsavingssf);
        mSavingsMore=findViewById(R.id.title4);
        mAdvmore=findViewById(R.id.title88adv);
        mSfMore=findViewById(R.id.title4sf);
        mGpmore=findViewById(R.id.title4gp);
        mLoanDue=findViewById(R.id.membertotalloan);
        mGpaccloan=findViewById(R.id.membertotalloangp);
        mLoanInstallment=findViewById(R.id.memberloanamount);
        mGpaccinst=findViewById(R.id.memberloanamountgp);
        mLoanMore=findViewById(R.id.title88);
        mAdvaamount=findViewById(R.id.membertotalloanadv);
        mSfadvance=findViewById(R.id.membertotalloanadvsf);
        mpena=findViewById(R.id.memberloanamountadv);
        mSfIntrest=findViewById(R.id.memberloanamountadvsf);
        mContct=findViewById(R.id.contact);
        mInsutot=findViewById(R.id.membertotalinsu);
        mInsudate=findViewById(R.id.memberinsudate);

        lsfadj=findViewById(R.id.lsfbal);
        loanadj=findViewById(R.id.loanbal);
        advadj=findViewById(R.id.advbal);

        mData=FirebaseDatabase.getInstance().getReference().child("details");
        mDatax=FirebaseDatabase.getInstance().getReference().child("Employees").child(unow);
        mDatabaseMembers= FirebaseDatabase.getInstance().getReference().child("members").child(mUid);
        md= FirebaseDatabase.getInstance().getReference().child("members").child(mUid);
        accounting=FirebaseDatabase.getInstance().getReference().child("Accounting");
        mDatabasegroupfinance=FirebaseDatabase.getInstance().getReference().child("finances");
        memacco=FirebaseDatabase.getInstance().getReference().child("members").child(mUid).child("account");

        mDatax.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uname=dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mMembername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (editoption) {
                    case "gen": {
                        /*Intent moresavings = new Intent(memberpage.this, editmemberfinance.class);
                        Bundle extras = new Bundle();
                        extras.putString("gkey", "g");
                        extras.putString("user", mUid);
                        moresavings.putExtras(extras);
                        startActivity(moresavings);
                        break;*/
                        break;
                    }
                    case "ga": {
                        Intent moresavings = new Intent(memberpage.this, editmemberfinance2.class);
                        Bundle extras = new Bundle();
                        extras.putString("gkey", "ga");
                        extras.putString("user", mUid);
                        moresavings.putExtras(extras);
                        startActivity(moresavings);
                        break;
                    }
                    default: {
                        Intent moresavings = new Intent(memberpage.this, editmemberfinance2.class);
                        Bundle extras = new Bundle();
                        extras.putString("gkey", "sf");
                        extras.putString("user", mUid);
                        moresavings.putExtras(extras);
                        startActivity(moresavings);
                        break;
                    }
                }
            }
        });




        mDatabaseMembers.child("details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name= (String) dataSnapshot.child("name").getValue();
                String image= (String) dataSnapshot.child("profileImage").getValue();
                String group=(String)dataSnapshot.child("group").getValue();
                mMembername.setText(name);
                mGroupName.setText(group);
                Picasso.with(memberpage.this).load(image).into(mMemberphoto);
                grpid=dataSnapshot.child("groupid").getValue().toString();
                if (dataSnapshot.child("contact").exists()){
                    con= (String) dataSnapshot.child("contact").getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mContct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builders = new AlertDialog.Builder(memberpage.this);
                builders.setTitle("Call "+name)
                        .setMessage("Phone number "+con)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_call_black_24dp)
                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String uri = "tel:" + con ;
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(uri));
                                startActivity(intent);
                            }
                        });
                AlertDialog alert121 = builders.create();
                alert121.show();
            }
        });


        //General info---------------------------------------------

        mDatabaseMembers.child("savings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalsavings= (String) dataSnapshot.child("totalsavings").getValue();
                clsf=(String) dataSnapshot.child("totalsavings").getValue();
                String amountdue= (String) dataSnapshot.child("amountdue").getValue();
                String nextpaydate=(String)dataSnapshot.child("nextpaydate").getValue();

                mTotalSavings.setText("Kshs. "+totalsavings);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSavingsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moresavings= new Intent(memberpage.this,savingsdetails.class);
                moresavings.putExtra("key",mUid);
                startActivity(moresavings);
            }
        });

        lsfadj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout=new LinearLayout(memberpage.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText basi=new EditText(memberpage.this);
                basi.setInputType(InputType.TYPE_CLASS_NUMBER);
                final TextView basic=new EditText(memberpage.this);
                layout.addView(basic);
                layout.addView(basi);
                basic.setText("Current Savings: "+mTotalSavings.getText().toString());
                basi.setHint("Enter New Saving");
                final AlertDialog.Builder builders = new AlertDialog.Builder(memberpage.this);
                builders.setTitle("Savings Balance Adjustment")
                        .setMessage("Change Savings for "+name)
                        .setView(layout)
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (!TextUtils.isEmpty(basi.getText().toString())){
                                    DatabaseReference databaseReference = md.child("savings");
                                    databaseReference.child("totalsavings").setValue(basi.getText().toString());

                                    md.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String gke=dataSnapshot.child("details").child("groupid").getValue().toString();

                                            final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("memberstats").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(gke).child(mUid);

                                            memstat.child("savings").setValue(basi.getText().toString());

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    if (Double.parseDouble(clsf)>Double.parseDouble(basi.getText().toString())){
                                        final double diff=Double.parseDouble(clsf)-Double.parseDouble(basi.getText().toString());

                                        DatabaseReference bankw=accounting.child("BankCash").child("Trans").child("all").push();
                                        bankw.child("name").setValue("LSF");
                                        bankw.child("amount").setValue(String.valueOf(Double.parseDouble(clsf)-Double.parseDouble(basi.getText().toString())));
                                        bankw.child("type").setValue("red");
                                        bankw.child("meet").setValue("-");
                                        bankw.child("group").setValue("-");
                                        bankw.child("debitac").setValue("Membersfund");
                                        bankw.child("creditac").setValue("Suspense");
                                        bankw.child("description").setValue("Savings Adjustment for "+name+" by "+uname);
                                        bankw.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference bankw2=accounting.child("Membersfund").child("Trans").child("all").push();
                                        bankw2.child("name").setValue("LSF");
                                        bankw2.child("amount").setValue(String.valueOf(Double.parseDouble(clsf)-Double.parseDouble(basi.getText().toString())));
                                        bankw2.child("type").setValue("red");
                                        bankw2.child("meet").setValue("-");
                                        bankw2.child("group").setValue("-");
                                        bankw2.child("debitac").setValue("Membersfund");
                                        bankw2.child("creditac").setValue("Suspense");
                                        bankw2.child("description").setValue("Savings Adjustment for "+name+" by "+uname);
                                        bankw2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference suspe=accounting.child("Suspense").child("Trans").child("all").push();
                                        suspe.child("name").setValue("LSF");
                                        suspe.child("amount").setValue(String.valueOf(diff));
                                        suspe.child("type").setValue("red");
                                        suspe.child("meet").setValue("-");
                                        suspe.child("group").setValue("-");
                                        suspe.child("debitac").setValue("Membersfund");
                                        suspe.child("creditac").setValue("Suspense");
                                        suspe.child("description").setValue("Savings Adjustment for "+name+" by "+uname);
                                        suspe.child("timestamp").setValue(ServerValue.TIMESTAMP);


                                        DatabaseReference peracc=memacco.push();
                                        peracc.child("name").setValue("LSF");
                                        peracc.child("amount").setValue(String.valueOf(diff));
                                        peracc.child("type").setValue("red");
                                        peracc.child("meet").setValue("-");
                                        peracc.child("group").setValue(grpid);
                                        peracc.child("debitac").setValue("Membersfund");
                                        peracc.child("creditac").setValue("member");
                                        peracc.child("description").setValue("Savings Adjustment for "+uname);
                                        peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        mDatabasegroupfinance.child(grpid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String oldsavings=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                                                double newsavings=Double.parseDouble(oldsavings)-diff;
                                                DatabaseReference newsa=mDatabasegroupfinance.child(grpid).child("savings");
                                                newsa.child("totalsavings").setValue(String.valueOf(newsavings));

                                                final DatabaseReference savingssupdate = mDatabaseMembers.child("savings").child("savings").push();
                                                savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                savingssupdate.child("details").setValue("Savings Change");
                                                savingssupdate.child("save").setValue("0");
                                                savingssupdate.child("with").setValue(String.valueOf(diff));
                                                savingssupdate.child("bal").setValue(basi.getText().toString());
                                                savingssupdate.child("fac").setValue(uname);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



                                    }else {
                                        final double diff=Double.parseDouble(basi.getText().toString())-Double.parseDouble(clsf);

                                        DatabaseReference bankw=accounting.child("BankCash").child("Trans").child("all").push();
                                        bankw.child("name").setValue("LSF");
                                        bankw.child("amount").setValue(String.valueOf(Double.parseDouble(basi.getText().toString())-Double.parseDouble(clsf)));
                                        bankw.child("type").setValue("blue");
                                        bankw.child("meet").setValue("-");
                                        bankw.child("group").setValue("-");
                                        bankw.child("debitac").setValue("BankCash");
                                        bankw.child("creditac").setValue("Membersfund");
                                        bankw.child("description").setValue("Savings Adjustment for "+name+" by "+uname);
                                        bankw.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference bankw2=accounting.child("Membersfund").child("Trans").child("all").push();
                                        bankw2.child("name").setValue("LSF");
                                        bankw2.child("amount").setValue(String.valueOf(Double.parseDouble(clsf)-Double.parseDouble(basi.getText().toString())));
                                        bankw2.child("type").setValue("red");
                                        bankw2.child("meet").setValue("-");
                                        bankw2.child("group").setValue("-");
                                        bankw2.child("debitac").setValue("Suspense");
                                        bankw2.child("creditac").setValue("Membersfund");
                                        bankw2.child("description").setValue("Savings Adjustment for "+name+" by "+uname);
                                        bankw2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference suspe=accounting.child("Suspense").child("Trans").child("all").push();
                                        suspe.child("name").setValue("LSF");
                                        suspe.child("amount").setValue(String.valueOf(diff));
                                        suspe.child("type").setValue("blue");
                                        suspe.child("meet").setValue("-");
                                        suspe.child("group").setValue("-");
                                        suspe.child("debitac").setValue("Suspense");
                                        suspe.child("creditac").setValue("Membersfund");
                                        suspe.child("description").setValue("Savings Adjustment for "+name+" by "+uname);
                                        suspe.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference peracc=memacco.push();
                                        peracc.child("name").setValue("LSF");
                                        peracc.child("amount").setValue(String.valueOf(diff));
                                        peracc.child("type").setValue("blue");
                                        peracc.child("meet").setValue("-");
                                        peracc.child("group").setValue(grpid);
                                        peracc.child("debitac").setValue("member");
                                        peracc.child("creditac").setValue("Membersfund");
                                        peracc.child("description").setValue("Savings Adjustment for "+uname);
                                        peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        mDatabasegroupfinance.child(grpid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String oldsavings=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                                                double newsavings=Double.parseDouble(oldsavings)+diff;
                                                DatabaseReference newsa=mDatabasegroupfinance.child(grpid).child("savings");
                                                newsa.child("totalsavings").setValue(String.valueOf(newsavings));

                                                final DatabaseReference savingssupdate = mDatabaseMembers.child("savings").child("savings").push();
                                                savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                savingssupdate.child("details").setValue("Savings Change");
                                                savingssupdate.child("save").setValue(String.valueOf(diff));
                                                savingssupdate.child("with").setValue("0");
                                                savingssupdate.child("bal").setValue(basi.getText().toString());
                                                savingssupdate.child("fac").setValue(uname);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    DatabaseReference newquerry=FirebaseDatabase.getInstance().getReference().child("edits").child(mUid);
                                    newquerry.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    newquerry.child("user").setValue(unow);
                                    newquerry.child("message").setValue("Savings Adjusted for "+name+" by "+uname);
                                    newquerry.child("status").setValue("pending");

                                    DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("MemBalEdit").push();
                                    endmeetnot.child("name").setValue(name);
                                    endmeetnot.child("group").setValue(mGroupName.getText().toString());
                                    endmeetnot.child("officer").setValue(uname);
                                    endmeetnot.child("detail").setValue("Savings Adjusted for "+name+" by "+uname);



                                    Toast.makeText(memberpage.this,"Savings Adjusted!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(memberpage.this,"Enter New Values!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert121 = builders.create();
                alert121.show();
            }
        });

        mDatabaseMembers.child("loans").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("installment").exists()){
                String totalloans= (String) dataSnapshot.child("totalloan").getValue();
                cloan=totalloans;
                String installment= (String) dataSnapshot.child("installment").getValue();
                String nextloanpaydate=(String)dataSnapshot.child("nextloanpaydate").getValue();
                double comp=Double.parseDouble(totalloans);

                if (comp<1&&comp>0){
                    mLayout.setVisibility(View.GONE);
                }else {
                    mLayout.setVisibility(View.VISIBLE);
                    mLoanDue.setText("Kshs. "+totalloans);
                    mLoanInstallment.setText("Kshs. "+installment);
                }
            }else{
                    String totalloans= "0";
                    cloan=totalloans;
                    mLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        loanadj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout=new LinearLayout(memberpage.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText basi=new EditText(memberpage.this);
                basi.setInputType(InputType.TYPE_CLASS_NUMBER);
                final EditText basi2=new EditText(memberpage.this);
                basi2.setInputType(InputType.TYPE_CLASS_NUMBER);
                final TextView basic=new EditText(memberpage.this);
                layout.addView(basic);
                layout.addView(basi);
                layout.addView(basi2);
                basic.setText("Current Loan: "+mLoanDue.getText().toString());
                basi.setHint("Enter New Loan Balance");
                basi2.setHint("Enter Installment");
                final AlertDialog.Builder builders = new AlertDialog.Builder(memberpage.this);
                builders.setTitle("Loan Balance Adjustment")
                        .setMessage("Change Loan for "+name)
                        .setView(layout)
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inst=basi2.getText().toString();

                                if (!TextUtils.isEmpty(basi.getText().toString())){

                                    DatabaseReference databaseReference2 = md;
                                    databaseReference2.child("loans").child("totalloan").setValue(basi.getText().toString());
                                    databaseReference2.child("loans").child("installment").setValue(inst);

                                    md.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String gke=dataSnapshot.child("details").child("groupid").getValue().toString();

                                            final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("memberstats").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(gke).child(mUid);

                                            memstat.child("loans").setValue(basi.getText().toString());

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    if (Double.parseDouble(cloan)>Double.parseDouble(basi.getText().toString())){
                                        final double diff=Double.parseDouble(cloan)-Double.parseDouble(basi.getText().toString());

                                        DatabaseReference ltlp=accounting.child("Longtermloan").child("Trans").child("all").push();
                                        ltlp.child("name").setValue("Loan");
                                        ltlp.child("amount").setValue(String.valueOf(diff));
                                        ltlp.child("type").setValue("red");
                                        ltlp.child("meet").setValue("-");
                                        ltlp.child("group").setValue("-");
                                        ltlp.child("debitac").setValue("Suspense");
                                        ltlp.child("creditac").setValue("Longtermloan");
                                        ltlp.child("description").setValue("Loan Adjustment for "+name+" by "+uname);
                                        ltlp.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference ltl2p=accounting.child("BankCash").child("Trans").child("all").push();
                                        ltl2p.child("name").setValue("Loan");
                                        ltl2p.child("amount").setValue(String.valueOf(diff));
                                        ltl2p.child("type").setValue("blue");
                                        ltl2p.child("meet").setValue("-");
                                        ltl2p.child("group").setValue("-");
                                        ltl2p.child("debitac").setValue("Suspense");
                                        ltl2p.child("creditac").setValue("BankCash");
                                        ltl2p.child("description").setValue("Loan Adjustment for "+name+" by "+uname);
                                        ltl2p.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference suspe=accounting.child("Suspense").child("Trans").child("all").push();
                                        suspe.child("name").setValue("Loan");
                                        suspe.child("amount").setValue(String.valueOf(diff));
                                        suspe.child("type").setValue("blue");
                                        suspe.child("meet").setValue("-");
                                        suspe.child("group").setValue("-");
                                        suspe.child("debitac").setValue("Suspense");
                                        suspe.child("creditac").setValue("Longtermloan");
                                        suspe.child("description").setValue("Loan Adjustment for "+name+" by "+uname);
                                        suspe.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference peracc=memacco.push();
                                        peracc.child("name").setValue("Loan");
                                        peracc.child("amount").setValue(String.valueOf(diff));
                                        peracc.child("type").setValue("red");
                                        peracc.child("meet").setValue("-");
                                        peracc.child("group").setValue(grpid);
                                        peracc.child("debitac").setValue("Suspense");
                                        peracc.child("creditac").setValue("member");
                                        peracc.child("description").setValue("Loan Adjustment by "+uname);
                                        peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        mDatabasegroupfinance.child(grpid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String oldsavings=dataSnapshot.child("loans").child("currentloan").getValue().toString();
                                                double newsavings=Double.parseDouble(oldsavings)-diff;
                                                DatabaseReference newsa=mDatabasegroupfinance.child(grpid).child("loans");
                                                newsa.child("currentloan").setValue(String.valueOf(newsavings));

                                                final DatabaseReference savingssupdate = mDatabaseMembers.child("loans").child("loanss").push();
                                                savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                savingssupdate.child("loanbf").setValue(cloan);
                                                savingssupdate.child("inst").setValue(String.valueOf(diff));
                                                savingssupdate.child("bal").setValue(basi.getText().toString());
                                                savingssupdate.child("fac").setValue(uname);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



                                    }else {
                                        final double diff=Double.parseDouble(basi.getText().toString())-Double.parseDouble(cloan);

                                        DatabaseReference bankw=accounting.child("BankCash").child("Trans").child("all").push();
                                        bankw.child("name").setValue("Loan");
                                        bankw.child("amount").setValue(String.valueOf(diff));
                                        bankw.child("type").setValue("blue");
                                        bankw.child("meet").setValue("-");
                                        bankw.child("group").setValue("-");
                                        bankw.child("debitac").setValue("BankCash");
                                        bankw.child("creditac").setValue("Suspense");
                                        bankw.child("description").setValue("Loan Adjustment for "+name+" by "+uname);
                                        bankw.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference bankw2=accounting.child("Longtermloan").child("Trans").child("all").push();
                                        bankw2.child("name").setValue("Loan");
                                        bankw2.child("amount").setValue(String.valueOf(diff));
                                        bankw2.child("type").setValue("blue");
                                        bankw2.child("meet").setValue("-");
                                        bankw2.child("group").setValue("-");
                                        bankw2.child("debitac").setValue("Longtermloan");
                                        bankw2.child("creditac").setValue("Suspense");
                                        bankw2.child("description").setValue("Loan Adjustment for "+name+" by "+uname);
                                        bankw2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference suspe=accounting.child("Suspense").child("Trans").child("all").push();
                                        suspe.child("name").setValue("Loan");
                                        suspe.child("amount").setValue(String.valueOf(diff));
                                        suspe.child("type").setValue("blue");
                                        suspe.child("meet").setValue("-");
                                        suspe.child("group").setValue("-");
                                        suspe.child("debitac").setValue("Longtermloan");
                                        suspe.child("creditac").setValue("Suspense");
                                        suspe.child("description").setValue("Loan Adjustment for "+name+" by "+uname);
                                        suspe.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference peracc=memacco.push();
                                        peracc.child("name").setValue("Loan");
                                        peracc.child("amount").setValue(String.valueOf(diff));
                                        peracc.child("type").setValue("blue");
                                        peracc.child("meet").setValue("-");
                                        peracc.child("group").setValue(grpid);
                                        peracc.child("debitac").setValue("member");
                                        peracc.child("creditac").setValue("Suspense");
                                        peracc.child("description").setValue("Loan Adjustment by "+uname);
                                        peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        mDatabasegroupfinance.child(grpid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String oldsavings=dataSnapshot.child("loans").child("currentloan").getValue().toString();
                                                double newsavings=Double.parseDouble(oldsavings)+diff;
                                                DatabaseReference newsa=mDatabasegroupfinance.child(grpid).child("loans");
                                                newsa.child("currentloan").setValue(String.valueOf(newsavings));

                                                final DatabaseReference savingssupdate = mDatabaseMembers.child("loans").child("loanss").push();
                                                savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                savingssupdate.child("loanbf").setValue(cloan);
                                                savingssupdate.child("inst").setValue(String.valueOf(diff));
                                                savingssupdate.child("bal").setValue(basi.getText().toString());
                                                savingssupdate.child("fac").setValue(uname);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    DatabaseReference newquerry=FirebaseDatabase.getInstance().getReference().child("edits").child(mUid);
                                    newquerry.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    newquerry.child("user").setValue(unow);
                                    newquerry.child("message").setValue("Loan Adjusted for "+name+" by "+uname);
                                    newquerry.child("status").setValue("pending");

                                    DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("MemBalEdit").push();
                                    endmeetnot.child("name").setValue(name);
                                    endmeetnot.child("group").setValue(mGroupName.getText().toString());
                                    endmeetnot.child("officer").setValue(uname);
                                    endmeetnot.child("detail").setValue("Loan Balance Adjusted for "+name+" by "+uname);

                                    Toast.makeText(memberpage.this,"Loan Adjusted!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();

                                }else {
                                    Toast.makeText(memberpage.this,"Enter New Values!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert121 = builders.create();
                alert121.show();
            }
        });

        mDatabaseMembers.child("advances").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("currentadvance").exists()){
                    String ads= (String) dataSnapshot.child("currentadvance").getValue();
                    cadv=ads;
                    String installment= (String) dataSnapshot.child("currpenalty").getValue();
                    double comp=Double.parseDouble(ads);

                    if (comp<1){
                        //mLayout2.setVisibility(View.GONE);
                        mAdvaamount.setText("Kshs. "+ads);
                        mpena.setText("Kshs. "+installment);
                    }else {
                        mLayout2.setVisibility(View.VISIBLE);
                        mAdvaamount.setText("Kshs. "+ads);
                        mpena.setText("Kshs. 0");
                    }
                }else{
                    String ads= "0";
                    cadv=ads;
                    String installment= "0";
                    double comp=Double.parseDouble(ads);

                    if (comp<1&&comp>0){
                        //mLayout2.setVisibility(View.GONE);
                    }else {
                        mLayout2.setVisibility(View.VISIBLE);
                        mAdvaamount.setText("Kshs. "+ads);
                        mpena.setText("Kshs. "+installment);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        advadj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout=new LinearLayout(memberpage.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText basi=new EditText(memberpage.this);
                basi.setInputType(InputType.TYPE_CLASS_NUMBER);
                final TextView basic=new EditText(memberpage.this);
                layout.addView(basic);
                layout.addView(basi);
                basic.setText("Current Advance: "+mAdvaamount.getText().toString());
                basi.setHint("Enter New Advance Balance");
                final AlertDialog.Builder builders = new AlertDialog.Builder(memberpage.this);
                builders.setTitle("Advance Balance Adjustment")
                        .setMessage("Change Advance for "+name)
                        .setView(layout)
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (!TextUtils.isEmpty(basi.getText().toString())){

                                    DatabaseReference databaseReference4 = md;
                                    if (basi.getText().toString().equals("0")){
                                        databaseReference4.child("advances").child("currentadvance").setValue(basi.getText().toString());
                                        databaseReference4.child("advances").child("currpenalty").setValue("0");
                                    }else {
                                        databaseReference4.child("advances").child("currentadvance").setValue(basi.getText().toString());
                                        databaseReference4.child("advances").child("currpenalty").setValue(String.valueOf(Math.round((10+(Double.parseDouble(basi.getText().toString())*0.1)/1.1))));
                                    }


                                    md.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String gke=dataSnapshot.child("details").child("groupid").getValue().toString();

                                            final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("memberstats").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(gke).child(mUid);

                                            memstat.child("advance").setValue(basi.getText().toString());

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    if (Double.parseDouble(cadv)>Double.parseDouble(basi.getText().toString())){
                                        final double diff=Double.parseDouble(cadv)-Double.parseDouble(basi.getText().toString());

                                        DatabaseReference ltlp=accounting.child("Shorttermloans").child("Trans").child("all").push();
                                        ltlp.child("name").setValue("Short Term Loan");
                                        ltlp.child("amount").setValue(String.valueOf(diff));
                                        ltlp.child("type").setValue("red");
                                        ltlp.child("meet").setValue("-");
                                        ltlp.child("group").setValue("-");
                                        ltlp.child("debitac").setValue("Suspense");
                                        ltlp.child("creditac").setValue("Shorttermloans");
                                        ltlp.child("description").setValue("Advance Adjustment for "+name+" by "+uname);
                                        ltlp.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference ltl2p=accounting.child("BankCash").child("Trans").child("all").push();
                                        ltl2p.child("name").setValue("Short Term Loan");
                                        ltl2p.child("amount").setValue(String.valueOf(diff));
                                        ltl2p.child("type").setValue("red");
                                        ltl2p.child("meet").setValue("-");
                                        ltl2p.child("group").setValue("-");
                                        ltl2p.child("debitac").setValue("Suspense");
                                        ltl2p.child("creditac").setValue("BankCash");
                                        ltl2p.child("description").setValue("Advance Adjustment for "+name+" by "+uname);
                                        ltl2p.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference suspe=accounting.child("Suspense").child("Trans").child("all").push();
                                        suspe.child("name").setValue("Short Term Loan");
                                        suspe.child("amount").setValue(String.valueOf(diff));
                                        suspe.child("type").setValue("blue");
                                        suspe.child("meet").setValue("-");
                                        suspe.child("group").setValue("-");
                                        suspe.child("debitac").setValue("Suspense");
                                        suspe.child("creditac").setValue("Shorttermloans");
                                        suspe.child("description").setValue("Advance Adjustment for "+name+" by "+uname);
                                        suspe.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference peracc=memacco.push();
                                        peracc.child("name").setValue("Short Term Loan");
                                        peracc.child("amount").setValue(String.valueOf(diff));
                                        peracc.child("type").setValue("red");
                                        peracc.child("meet").setValue("-");
                                        peracc.child("group").setValue(grpid);
                                        peracc.child("debitac").setValue("Suspense");
                                        peracc.child("creditac").setValue("member");
                                        peracc.child("description").setValue("Advance Adjustment for "+uname);
                                        peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        mDatabasegroupfinance.child(grpid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String oldsavings=dataSnapshot.child("advances").child("currentadvance").getValue().toString();
                                                double newsavings=Double.parseDouble(oldsavings)-diff;
                                                DatabaseReference newsa=mDatabasegroupfinance.child(grpid).child("advances");
                                                newsa.child("currentadvance").setValue(String.valueOf(newsavings));

                                                final DatabaseReference savingssupdate = mDatabaseMembers.child("advances").child("advancesss").push();
                                                savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                savingssupdate.child("loanbf").setValue(cadv);
                                                savingssupdate.child("inst").setValue(String.valueOf(diff));
                                                savingssupdate.child("bal").setValue(basi.getText().toString());
                                                savingssupdate.child("fac").setValue(uname);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



                                    }else {
                                        final double diff=Double.parseDouble(basi.getText().toString())-Double.parseDouble(cadv);

                                        DatabaseReference bankw=accounting.child("BankCash").child("Trans").child("all").push();
                                        bankw.child("name").setValue("Short Term Loan");
                                        bankw.child("amount").setValue(String.valueOf(diff));
                                        bankw.child("type").setValue("blue");
                                        bankw.child("meet").setValue("-");
                                        bankw.child("group").setValue("-");
                                        bankw.child("debitac").setValue("BankCash");
                                        bankw.child("creditac").setValue("Suspense");
                                        bankw.child("description").setValue("Advance Adjustment for "+name+" by "+uname);
                                        bankw.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference bankw2=accounting.child("Shorttermloans").child("Trans").child("all").push();
                                        bankw2.child("name").setValue("Short Term Loan");
                                        bankw2.child("amount").setValue(String.valueOf(diff));
                                        bankw2.child("type").setValue("blue");
                                        bankw2.child("meet").setValue("-");
                                        bankw2.child("group").setValue("-");
                                        bankw2.child("debitac").setValue("Shorttermloans");
                                        bankw2.child("creditac").setValue("Suspense");
                                        bankw2.child("description").setValue("Advance Adjustment for "+name+" by "+uname);
                                        bankw2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference suspe=accounting.child("Suspense").child("Trans").child("all").push();
                                        suspe.child("name").setValue("Short Term Loan");
                                        suspe.child("amount").setValue(String.valueOf(diff));
                                        suspe.child("type").setValue("red");
                                        suspe.child("meet").setValue("-");
                                        suspe.child("group").setValue("-");
                                        suspe.child("debitac").setValue("Shorttermloans");
                                        suspe.child("creditac").setValue("Suspense");
                                        suspe.child("description").setValue("Advance Adjustment for "+name+" by "+uname);
                                        suspe.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference peracc=memacco.push();
                                        peracc.child("name").setValue("Short Term Loan");
                                        peracc.child("amount").setValue(String.valueOf(diff));
                                        peracc.child("type").setValue("blue");
                                        peracc.child("meet").setValue("-");
                                        peracc.child("group").setValue(grpid);
                                        peracc.child("debitac").setValue("member");
                                        peracc.child("creditac").setValue("Suspense");
                                        peracc.child("description").setValue("Advance Adjustment by "+uname);
                                        peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        mDatabasegroupfinance.child(grpid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String oldsavings=dataSnapshot.child("advances").child("currentadvance").getValue().toString();
                                                double newsavings=Double.parseDouble(oldsavings)+diff;
                                                DatabaseReference newsa=mDatabasegroupfinance.child(grpid).child("advances");
                                                newsa.child("currentadvance").setValue(String.valueOf(newsavings));

                                                final DatabaseReference savingssupdate = mDatabaseMembers.child("advances").child("advancesss").push();
                                                savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                savingssupdate.child("loanbf").setValue(cloan);
                                                savingssupdate.child("inst").setValue(String.valueOf(diff));
                                                savingssupdate.child("bal").setValue(basi.getText().toString());
                                                savingssupdate.child("fac").setValue(uname);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    DatabaseReference newquerry=FirebaseDatabase.getInstance().getReference().child("edits").child(mUid);
                                    newquerry.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    newquerry.child("user").setValue(unow);
                                    newquerry.child("message").setValue("Advance Adjusted for "+name+" by "+uname);
                                    newquerry.child("status").setValue("pending");

                                    DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("MemBalEdit").push();
                                    endmeetnot.child("name").setValue(name);
                                    endmeetnot.child("group").setValue(mGroupName.getText().toString());
                                    endmeetnot.child("officer").setValue(uname);
                                    endmeetnot.child("detail").setValue("Advance Balance Adjusted for "+name+" by "+uname);


                                    Toast.makeText(memberpage.this,"Advance Adjusted!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();

                                }else {
                                    Toast.makeText(memberpage.this,"Enter New Values!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert121 = builders.create();
                alert121.show();
            }
        });

        mDatabaseMembers.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String insutot=dataSnapshot.child("amount").getValue().toString();
                    String insudte=dataSnapshot.child("date").getValue().toString();
                    mInsutot.setText("Kshs. "+insutot);
                    mInsudate.setText(insudte);
                }else {
                    mInsutot.setText("Kshs. 0");
                    mInsudate.setText("Member Hasn't paid insurance");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mLoanMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moreloans= new Intent(memberpage.this,loandetails.class);
                moreloans.putExtra("key",mUid);
                startActivity(moreloans);
            }
        });

        mAdvmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moreloans= new Intent(memberpage.this,advancedetails.class);
                moreloans.putExtra("key",mUid);
                startActivity(moreloans);
            }
        });

        //School Fees info--------------------------------------------------

        mDatabaseMembers.child("savings").child("schoolfees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("totalsavings").exists()){

                String totalsavings= (String) dataSnapshot.child("totalsavings").getValue();

                mSfsavings.setText("Kshs. "+totalsavings);
                }else {
                    String totalsavings= "0";

                    mSfsavings.setText("Kshs. "+totalsavings);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseMembers.child("advances").child("schoolfees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("currpenalty").exists()){
                    String ads= (String) dataSnapshot.child("currentadvance").getValue();
                    String installment= (String) dataSnapshot.child("currpenalty").getValue();
                    double comp=Double.parseDouble(ads);

                    mSfadvance.setText("Kshs. "+ads);
                    mSfIntrest.setText("Kshs. "+installment);
                }else{
                    String ads= "0";
                    String installment= "0";
                    double comp=Double.parseDouble(ads);

                    mSfadvance.setText("Kshs. "+ads);
                    mSfIntrest.setText("Kshs. "+installment);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Group Info------------------------------------------------

        mDatabaseMembers.child("project").child("savings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("totalsavings").exists()){
                    String totalsavings= (String) dataSnapshot.child("totalsavings").getValue();

                    mGpaccsav.setText("Kshs. "+totalsavings);
                }else {
                    mGpaccsav.setText("Kshs. 0");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseMembers.child("project").child("loans").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("installment").exists()){
                    String totalloans= (String) dataSnapshot.child("totalloan").getValue();
                    String installment= (String) dataSnapshot.child("installment").getValue();
                    String nextloanpaydate=(String)dataSnapshot.child("nextloanpaydate").getValue();
                    double comp=Double.parseDouble(totalloans);

                    mGpaccloan.setText("Kshs. "+totalloans);
                    mGpaccinst.setText("Kshs. "+installment);

                }else{
                    String totalloans= "0";
                    String installment= "0";
                    String nextloanpaydate=" ";
                    //double comp=Double.parseDouble(totalloans);
                    //mLayout.setVisibility(View.GONE);

                    mGpaccloan.setText("Kshs. "+totalloans);
                    mGpaccinst.setText("Kshs. "+installment);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.memaccount_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.action_all){
            normal.setVisibility(View.VISIBLE);
            gp.setVisibility(View.GONE);
            sf.setVisibility(View.GONE);
            mThatitle.setText("General Info");
            editoption="gen";
        }
        if(item.getItemId()==R.id.action_sf){
            normal.setVisibility(View.GONE);
            gp.setVisibility(View.GONE);
            sf.setVisibility(View.VISIBLE);
            mThatitle.setText("School Fees Account Info");
            editoption="sf";
        }
        if(item.getItemId()==R.id.action_gp){
            normal.setVisibility(View.GONE);
            gp.setVisibility(View.VISIBLE);
            sf.setVisibility(View.GONE);
            mThatitle.setText("Group Account Info");
            editoption="ga";
        }

        return true;
    }
}
