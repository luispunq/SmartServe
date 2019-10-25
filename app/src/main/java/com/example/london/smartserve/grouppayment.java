package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class grouppayment extends AppCompatActivity {
    private ImageView mMemberphoto;
    private TextView mMembername, mGroupName, mTotalSavings, mCurrloan,mInst;
    private EditText mTotalrepaid, mLSF, mloanRep;
    private DatabaseReference mDatabasemember, mDatabasetrans, mDatatabasegroup, mDatabase, master, mD;
    private FirebaseAuth mAuth;
    private LinearLayout mLayout, linearLayout, withlayout,choice,rchoice;
    private String user = null, op = null, ops = null;
    private Button mPaySubmit;
    private ProgressDialog mProgress;
    private String nname = null;
    private String grpid = null, paymode = null;
    private String meeetid = null, intr1 = null, intr2 = null;
    private int  opt = 5, opts = 2, newlsfs = 0;
    private String currentadvace, currentpenalty, line = null, ngroup = null;
    private double tototrep=0,newinstallment = 0,newlsf = 0, newadvancepayment = 0, advpy = 0, advncecf = 0, mpesapay = 0, tempadv = 0, tempsav,d1=0,d2=0,d3=0,d4=0;
    private String advancepaymen = null, details = null, facname = null,currentploan=null,totalpsavings=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouppayment);
        mAuth = FirebaseAuth.getInstance();
        ops = mAuth.getCurrentUser().getUid();
        user = getIntent().getExtras().getString("key");
        mMemberphoto = findViewById(R.id.paymemberprofpic);
        mMembername = findViewById(R.id.paymembername);
        mGroupName = findViewById(R.id.paymembergroup);
        mTotalSavings = findViewById(R.id.savingstat);
        mCurrloan = findViewById(R.id.loanstat);
        mLayout = findViewById(R.id.insurance);
        linearLayout = findViewById(R.id.alertt);
        mInst=findViewById(R.id.installmentamount);
        mTotalrepaid = findViewById(R.id.totalrepaidamount);
        mLSF = findViewById(R.id.lsfamount);
        mloanRep = findViewById(R.id.loanpaymentamount);
        mPaySubmit = findViewById(R.id.button);
        mProgress = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("details");
        mDatabasemember = FirebaseDatabase.getInstance().getReference().child("members").child(user);
        //mDatabasemember.keepSynced(true);
        master = FirebaseDatabase.getInstance().getReference().child("masterfinance");

        mMembername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moresavings= new Intent(grouppayment.this,editmemberfinance2.class);
                Bundle extras = new Bundle();
                extras.putString("gkey", "g");
                extras.putString("user", user);
                moresavings.putExtras(extras);
                startActivity(moresavings);
            }
        });








        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.setVisibility(View.VISIBLE);
            }
        });

        mDatabasemember.child("details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nname = (String) dataSnapshot.child("name").getValue();
                String nimage = (String) dataSnapshot.child("profileImage").getValue();
                ngroup = (String) dataSnapshot.child("group").getValue();
                grpid = (String) dataSnapshot.child("groupid").getValue();
                mMembername.setText(nname);
                mGroupName.setText(ngroup);
                Picasso.with(grouppayment.this).load(nimage).into(mMemberphoto);

                mDatabase.child(grpid).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        meeetid = (String) dataSnapshot.child("meetid").getValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasemember.child("project").child("savings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("totalsavings").exists()) {
                    totalpsavings = (String) dataSnapshot.child("totalsavings").getValue();
                    mTotalSavings.setText("Kshs." + totalpsavings);
                    tempsav = Double.parseDouble(totalpsavings);

                } else {
                    mTotalSavings.setText("Kshs. 0");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasemember.child("project").child("loans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("installment").exists()) {
                    currentploan = (String) dataSnapshot.child("totalloan").getValue();
                    mCurrloan.setText("Kshs." + currentploan);
                    op = dataSnapshot.child("installment").getValue().toString();
                    mInst.setText("Kshs." + op);
                    mloanRep.setHint("Kshs." + op);
                } else {
                    currentploan = "0";
                    mCurrloan.setText("Kshs." + currentploan);
                    op = "0";
                    mInst.setText("Kshs." + op);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        mPaySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builders = new AlertDialog.Builder(grouppayment.this);
                builders.setTitle("Select Options")
                        .setCancelable(true)
                        .setPositiveButton("Part MPesa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                paymode = "Part MPesa";
                                dialog.dismiss();
                                processgpayment();
                            }
                        })
                        .setNegativeButton("All Cash", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                paymode = "All Cash";
                                dialog.dismiss();
                                processgpayment();
                            }
                        });
                AlertDialog alert121 = builders.create();
                alert121.show();

            }
        });
    }


    private void processgpayment() {
        final String totalrepaidamount = mTotalrepaid.getText().toString();
        final String lsfamount = mLSF.getText().toString();
        final String installmentamount = mloanRep.getText().toString();


        final DatabaseReference newmemberpayment = mDatabase.child(grpid).child("grouptransactions").child(meeetid).push();
        String temptr = newmemberpayment.getKey();
        newmemberpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        newmemberpayment.child("totalamount").setValue(totalrepaidamount);
        newmemberpayment.child("wcomm").setValue("0");
        newmemberpayment.child("lsf").setValue(lsfamount);
        newmemberpayment.child("loaninstallments").setValue(installmentamount);
        newmemberpayment.child("advancepayment").setValue("0");
        newmemberpayment.child("advanceinterest").setValue("0");
        newmemberpayment.child("memberid").setValue(nname);
        newmemberpayment.child("id").setValue(user);
        newmemberpayment.child("loangvn").setValue("0");
        newmemberpayment.child("loancf").setValue("0");

        DatabaseReference f = mDatabasemember.child("gptemptrs");
        f.child("trid").setValue(temptr);

        mDatabasemember.child("project").child("loans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("totalloan").exists()){
                    String currloan=(String)dataSnapshot.child("totalloan").getValue();
                    double currloanint=Double.parseDouble(currloan);
                    double loanapp=Double.parseDouble(installmentamount);
                    double newloans=currloanint-loanapp;
                    String newcurrloan=String.valueOf(newloans);

                    DatabaseReference newpersonalloan=mDatabasemember.child("project").child("loans");

                    newpersonalloan.child("totalloan").setValue(newcurrloan);
                    newpersonalloan.child("nextloanpaydate").setValue(setnextdate());
                    newmemberpayment.child("loancf").setValue(newcurrloan);
                }else {
                    String currloan="0";
                    double currloanint=Double.parseDouble(currloan);
                    double loanapp=Double.parseDouble(installmentamount);
                    double newloans=currloanint-loanapp;
                    String newcurrloan=String.valueOf(newloans);

                    DatabaseReference newpersonalloan=mDatabasemember.child("project").child("loans");

                    newpersonalloan.child("totalloan").setValue(newcurrloan);
                    newpersonalloan.child("nextloanpaydate").setValue(setnextdate());
                    newmemberpayment.child("loancf").setValue(newcurrloan);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasemember.child("project").child("savings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("totalsavings").exists()){
                    String currloan=(String)dataSnapshot.child("totalsavings").getValue();
                    double currloanint=Double.parseDouble(currloan);
                    double loanapp=Double.parseDouble(lsfamount);
                    double newloans=currloanint+loanapp;
                    String newcurrloan=String.valueOf(newloans);

                    DatabaseReference newpersonalloan=mDatabasemember.child("project").child("savings");

                    newpersonalloan.child("totalsavings").setValue(newcurrloan);

                    newmemberpayment.child("lsfcf").setValue(newcurrloan);
                }else {
                    String currloan="0";
                    double currloanint=Double.parseDouble(currloan);
                    double loanapp=Double.parseDouble(lsfamount);
                    double newloans=currloanint+loanapp;
                    String newcurrloan=String.valueOf(newloans);

                    DatabaseReference newpersonalloan=mDatabasemember.child("project").child("savings");

                    newpersonalloan.child("totalsavings").setValue(newcurrloan);
                    newmemberpayment.child("lsfcf").setValue(newcurrloan);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Toast.makeText(grouppayment.this, "Payment Processed!", Toast.LENGTH_LONG).show();

        Intent home = new Intent(grouppayment.this, memberlist.class);
        home.putExtra("key", grpid);
        startActivity(home);
        finish();
    }



    private String setnextdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(Calendar.DATE, 30); //Adding30days
        String output = sdf.format(c.getTime());

        return output;
    }
}