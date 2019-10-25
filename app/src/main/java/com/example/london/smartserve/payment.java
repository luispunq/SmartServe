package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class payment extends AppCompatActivity {
    private ImageView mMemberphoto;
    private TextView mMembername, mGroupName, mTotalSavings, mCurrloan, mAdvnce, mInstdiplay, mWithdisp;
    private EditText mTotalrepaid, mLSF, mloanRep, mAdvnpay, mFines, mInsurance, mMpesa, mWithdraw,mOther;
    private DatabaseReference mDatabasemember,mD4,mD3,mD2,mD1, mDatabasetrans, mDatatabasegroup, mDatabase, memacco, mD,accounting;
    private FirebaseAuth mAuth;
    private LinearLayout mLayout, linearLayout, withlayout,choice,rchoice;
    private String user = null, op = null, ops = null,fl=null;
    private Button mPaySubmit;
    private ProgressDialog mProgress;
    private String nname = null;
    private String grpid = null, paymode = null;
    private String meeetid = null, intr2 = null;
    private int  opt = 5, opts = 2, newlsfs = 0;
    private String currentadvace, currentpenalty, line = null, ngroup = null;
    private double tototrep=0,newinstallment = 0,newlsf = 0, newadvancepayment = 0, advpy = 0, advncecf = 0, mpesapay = 0, tempadv = 0, tempsav,d1=0,d2=0,d3=0,d4=0;
    private String advancepaymen = null, details = null, facname = null,currentploan=null,totalpsavings=null,with=null;
    private String temptr,trstype,updatedsavings,updatedloans;
    private double newlsfcf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mAuth = FirebaseAuth.getInstance();
        ops = mAuth.getCurrentUser().getUid();
        user = getIntent().getExtras().getString("key");
        mMemberphoto = findViewById(R.id.paymemberprofpic);
        mMembername = findViewById(R.id.paymembername);
        mGroupName = findViewById(R.id.paymembergroup);
        mTotalSavings = findViewById(R.id.savingstat);
        mInstdiplay = findViewById(R.id.installmentamount);
        mWithdisp = findViewById(R.id.title15532);
        mCurrloan = findViewById(R.id.loanstat);
        mAdvnce = findViewById(R.id.advancestat);
        mLayout = findViewById(R.id.insurance);
        linearLayout = findViewById(R.id.alertt);
        rchoice=findViewById(R.id.alertt2g);
        withlayout = findViewById(R.id.withdr);
        mTotalrepaid = findViewById(R.id.totalrepaidamount);
        mTotalrepaid.addTextChangedListener(filterTextWatcher);
        mLSF = findViewById(R.id.lsfamount);
        mMpesa = findViewById(R.id.kujazia);
        mInsurance = findViewById(R.id.insuranceamt);
        mloanRep = findViewById(R.id.loanpaymentamount);
        mAdvnpay = findViewById(R.id.advanpaymnet);
        mFines = findViewById(R.id.finepayment);
        mOther=findViewById(R.id.otherpayment);
        mPaySubmit = findViewById(R.id.button);
        mWithdraw = findViewById(R.id.withamnt);
        mWithdraw.setText("0");
        mProgress = new ProgressDialog(this);
        accounting=FirebaseDatabase.getInstance().getReference().child("Accounting");
        mDatabasetrans = FirebaseDatabase.getInstance().getReference().child("transactions");
        mDatatabasegroup = FirebaseDatabase.getInstance().getReference().child("finances");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("details");
        mDatabasemember = FirebaseDatabase.getInstance().getReference().child("members").child(user);
        mD4 = FirebaseDatabase.getInstance().getReference().child("Receipts");
        mD3 = FirebaseDatabase.getInstance().getReference().child("Mpesa").child("Received");
        mD2 = FirebaseDatabase.getInstance().getReference().child("Employees").child(ops);
        mD1=FirebaseDatabase.getInstance().getReference().child("insurance").child("all");
        memacco=FirebaseDatabase.getInstance().getReference().child("members").child(user).child("account");


        mDatabasemember.child("Mpesa").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("amount").exists()){
                    mMpesa.setText(dataSnapshot.child("amount").getValue().toString());
                }else {
                    mMpesa.setText("0");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAdvnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(payment.this);
                builder.setTitle("Select Options")
                        .setCancelable(true)
                        .setItems(R.array.Options6, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        opts = 0;
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        opts = 1;
                                        dialog.dismiss();
                                        break;
                                    case 2:
                                        opts = 2;
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.setVisibility(View.VISIBLE);
            }
        });

        mWithdisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withlayout.setVisibility(View.VISIBLE);
            }
        });
        Thread load=new Thread(new Runnable() {
            @Override
            public void run() {
                mDatabasemember.child("details").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        nname = (String) dataSnapshot.child("name").getValue();
                        String nimage = (String) dataSnapshot.child("profileImage").getValue();
                        ngroup = (String) dataSnapshot.child("group").getValue();
                        grpid = (String) dataSnapshot.child("groupid").getValue();
                        mMembername.setText(nname);
                        mGroupName.setText(ngroup);
                        Picasso.with(payment.this).load(nimage).into(mMemberphoto);

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
            }
        });
        load.start();


        mDatabasemember.child("savings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("totalsavings").exists()) {
                    totalpsavings = (String) dataSnapshot.child("totalsavings").getValue();
                    mTotalSavings.setText("Kshs." + totalpsavings);
                    tempsav = Double.parseDouble(totalpsavings);

                } else {
                    mTotalSavings.setText("");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasemember.child("loans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("installment").exists()) {
                    currentploan = (String) dataSnapshot.child("totalloan").getValue();
                    mCurrloan.setText("Kshs." + currentploan);
                    op = dataSnapshot.child("installment").getValue().toString();
                    mInstdiplay.setText("Kshs." + op);
                    mloanRep.setHint("Kshs." + op);
                    tototrep=tototrep+Double.parseDouble(op);
                } else {
                    currentploan = (String) dataSnapshot.child("totalloan").getValue();
                    mCurrloan.setText("Kshs." + currentploan);
                    op = "0";
                    mInstdiplay.setText("Kshs." + op);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasemember.child("advances").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("currpenalty").exists()) {
                    currentadvace = (String) dataSnapshot.child("currentadvance").getValue();
                    currentpenalty = dataSnapshot.child("currpenalty").getValue().toString();
                    mAdvnce.setText("Kshs." + currentadvace);
                    tototrep=tototrep+Double.parseDouble(currentadvace);
                    mTotalrepaid.setHint(String.valueOf(tototrep));
                } else {
                    currentadvace = "0";
                    mAdvnce.setText("Kshs.0");
                    currentpenalty = "0";
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabasemember.child("Mpesa").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("amount").exists()){
                    mMpesa.setText(dataSnapshot.child("amount").getValue().toString());
                }else {
                    mMpesa.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        rchoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regpayment();
            }
        });

        mPaySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMpesa.getText().toString().equals("0")){
                    final AlertDialog.Builder builders = new AlertDialog.Builder(payment.this);
                    builders.setTitle("Confirm Cash Payment")
                            .setCancelable(true)
                            .setNeutralButton("All Cash", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    paymode = "All Cash";
                                    dialog.dismiss();
                                    sec();
                                }
                            });
                    AlertDialog alert121 = builders.create();
                    alert121.show();
                }else {
                    final AlertDialog.Builder builders = new AlertDialog.Builder(payment.this);
                    builders.setTitle("Confirm Mpesa Payment")
                            .setCancelable(true)
                            .setNeutralButton("Part MPesa", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    paymode = "Part MPesa";
                                    dialog.dismiss();
                                    sec();
                                }
                            });
                    AlertDialog alert121 = builders.create();
                    alert121.show();
                }



            }
        });
    }

    private void sec() {
        double secitem1= Double.parseDouble(mTotalrepaid.getText().toString());
        double secitem2= Double.parseDouble(mLSF.getText().toString());
        double secitem3= Double.parseDouble(mloanRep.getText().toString());
        double secitem4= Double.parseDouble(mAdvnpay.getText().toString());
        double secitem5= Double.parseDouble(mFines.getText().toString());
        double secitem6= Double.parseDouble(mOther.getText().toString());
        double secitem7= Double.parseDouble(mInsurance.getText().toString());
        double secitem8=Double.parseDouble(mWithdraw.getText().toString());

        if (Double.parseDouble(mWithdraw.getText().toString())==0){
            if (secitem1==(secitem2+secitem3+secitem4+secitem5+secitem6+secitem7)){
                mDatabasemember.child("details").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("flag").exists()) {
                            processpaymentf();
                        }else {
                            processpayment();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else{
                Toast.makeText(payment.this,"Please make sure your values balance with the total repaid",Toast.LENGTH_LONG).show();
            }
        }else if (Double.parseDouble(mWithdraw.getText().toString())>1){
            if (secitem1==(secitem3+secitem4+secitem5+secitem6+secitem7)){
                mDatabasemember.child("details").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("flag").exists()) {
                            DatabaseReference red=mDatabasemember.child("details");
                            red.child("flag").removeValue();
                            processpaymentf();
                        }else {
                            processpayment();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else{
                Toast.makeText(payment.this,"Please make sure your values balance with the total repaid",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void regpayment() {
        mDatabase.child(grpid).child("grouptransactions").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                d1=0;
                d2=0;
                d3=0;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String va1=snapshot.child("totalamount").getValue().toString();
                    String va2=snapshot.child("lsf").getValue().toString();
                    String va3=snapshot.child("loaninstallments").getValue().toString();


                    double vad1=Double.parseDouble(va1);
                    double vad2=Double.parseDouble(va2);
                    double vad3=Double.parseDouble(va3);


                    d1=d1+vad1;
                    d2=d2+vad2;
                    d3=d3+vad3;


                    mTotalrepaid.setText(String.valueOf(d1));
                    mLSF.setText(String.valueOf(d2));
                    mloanRep.setText(String.valueOf(d3));
                    mAdvnpay.setText("0");
                    mFines.setText("0");
                    mOther.setText("0");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //DOTHECALCULATIONSHEREANDSHOWTHERESULTASPERYOURCALCULATIONS
            try {
                double totamount = Double.parseDouble(mTotalrepaid.getText().toString());
                double loaninst = Double.parseDouble(op);
                double advce = Double.parseDouble(currentadvace);
                double advp=Double.parseDouble(currentpenalty);
                final String lsfamount = mLSF.getText().toString();


                double foradv=totamount-loaninst;
                if (foradv>advce){
                    mAdvnpay.setHint("Kshs." + currentadvace);
                    double lsfamt = totamount - (loaninst + advce);
                    mLSF.setHint("Kshs. "+String.valueOf(lsfamt));
                    mloanRep.setHint("Kshs. "+String.valueOf(totamount-Double.parseDouble(lsfamount)-advce));
                    if (lsfamt<0){
                        double f=(loaninst+advce)-Double.parseDouble(totalpsavings);
                        mLSF.setHint("Please Add "+String.valueOf(f+200));
                    }
                }else {
                    mAdvnpay.setHint("Kshs." + currentpenalty);
                    double lsfamt = totamount - (loaninst + advp);
                    mLSF.setHint("Kshs. "+String.valueOf(lsfamt));
                    mloanRep.setHint("Kshs. "+String.valueOf(totamount-Double.parseDouble(lsfamount)-advp));
                    if (lsfamt<0){
                        double f=(loaninst+advce)-Double.parseDouble(totalpsavings);
                        mLSF.setHint("Please Add "+String.valueOf(f+200));
                    }
                }


            } catch (Exception e) {
                double totamount = 0.0;
                double loaninst = Double.parseDouble(op);
                double advce = Double.parseDouble(currentadvace);
                double lsfamt = totamount - (loaninst + advce);
                //mLSF.setHint(String.valueOf(lsfamt));
            }

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            try {
                double totamount = Double.parseDouble(mTotalrepaid.getText().toString());
                double loaninst = Double.parseDouble(op);
                double advce = Double.parseDouble(currentadvace);
                double advp=Double.parseDouble(currentpenalty);
                final String lsfamount = mLSF.getText().toString();


                double foradv=totamount-loaninst;
                if (foradv>advce){
                    mAdvnpay.setHint("Kshs." + currentadvace);
                    double lsfamt = totamount - (loaninst + advce);
                    mLSF.setHint("Kshs. "+String.valueOf(lsfamt));
                    mloanRep.setHint("Kshs. "+String.valueOf(totamount-Double.parseDouble(lsfamount)-advce));
                    if (lsfamt<0){
                        double f=(loaninst+advce)-Double.parseDouble(totalpsavings);
                        mLSF.setHint("Please Add "+String.valueOf(f+200));
                    }
                }else {
                    mAdvnpay.setHint("Kshs." + currentpenalty);
                    double lsfamt = totamount - (loaninst + advp);
                    mLSF.setHint("Kshs. "+String.valueOf(lsfamt));
                    mloanRep.setHint("Kshs. "+String.valueOf(totamount-Double.parseDouble(lsfamount)-advp));
                    if (lsfamt<0){
                        double f=(loaninst+advce)-Double.parseDouble(totalpsavings);
                        mLSF.setHint("Please Add "+String.valueOf(f+200));
                    }
                }


            } catch (Exception e) {
                double totamount = 0.0;
                double loaninst = Double.parseDouble(op);
                double advce = Double.parseDouble(currentadvace);
                double lsfamt = totamount - (loaninst + advce);
                mLSF.setHint(String.valueOf(lsfamt));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void processpayment() {
        final String totalrepaidamount = mTotalrepaid.getText().toString();
        final String lsfamount = mLSF.getText().toString();
        final String installmentamount = mloanRep.getText().toString();
        final String advancepayment = mAdvnpay.getText().toString();
        final String fines = mFines.getText().toString();
        final String insu = mInsurance.getText().toString();
        final String mpesa = mMpesa.getText().toString();
        with = mWithdraw.getText().toString();
        final String others=mOther.getText().toString();


        try {
            if (!TextUtils.isEmpty(advancepayment) && !TextUtils.isEmpty(fines) && !TextUtils.isEmpty(installmentamount)&& !TextUtils.isEmpty(others)) {
                if (opts == 2) {
                    intr2 = "0";
                    advancepaymen = advancepayment;
                    tempadv = Double.parseDouble(advancepaymen);
                } else if (opts == 0) {
                    intr2 = advancepayment;
                    advancepaymen = "0";
                    tempadv = Double.parseDouble("0");
                } else {

                    advncecf = Math.round((((Double.parseDouble(currentadvace) - Double.parseDouble(advancepayment))+10) *1.1)*100)/100;
                    advpy = Math.round((Double.parseDouble(currentadvace) - advncecf)*100)/100;
                    intr2 = String.valueOf((advncecf/11)+10);
                    advancepaymen = String.valueOf(advpy);
                    tempadv = advpy;
                }

                final DatabaseReference newmemberpayment = mDatabasemember.child("transactions").push();
                newmemberpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                newmemberpayment.child("totalamount").setValue(totalrepaidamount);


                if (Double.parseDouble(with) > 1&&Double.parseDouble(with)<tempsav) {
                    details = "Withdraw";
                    newmemberpayment.child("lsf").setValue("0");
                    newmemberpayment.child("wcomm").setValue("100");
                } else if (Double.parseDouble(with) == tempsav) {
                    details = "Withdraw";
                    newmemberpayment.child("lsf").setValue("0");
                    newmemberpayment.child("wcomm").setValue("200");
                } else {
                    details = "Contribution";
                    newmemberpayment.child("lsf").setValue(lsfamount);
                    newmemberpayment.child("wcomm").setValue("0");
                }

                newmemberpayment.child("loaninstallments").setValue(installmentamount);
                newmemberpayment.child("advancepayment").setValue(advancepaymen);
                newmemberpayment.child("advanceinterest").setValue(intr2);
                newmemberpayment.child("paymentmode").setValue(paymode);
                newmemberpayment.child("memberid").setValue(user);
                newmemberpayment.child("fine").setValue(fines);
                newmemberpayment.child("insurance").setValue(insu);
                newmemberpayment.child("Mpesa").setValue(mpesa);

                DatabaseReference newgrouppayment = mDatabasetrans.child(grpid).push();
                newgrouppayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                newgrouppayment.child("totalamount").setValue(totalrepaidamount);
                newgrouppayment.child("wamount").setValue(with);
                if (Double.parseDouble(with) > 1&&Double.parseDouble(with)<((tempsav+Double.parseDouble(lsfamount))-201)) {
                    newgrouppayment.child("lsf").setValue("0");
                    newgrouppayment.child("wcomm").setValue("100");
                } else if (Double.parseDouble(with) == ((tempsav+Double.parseDouble(lsfamount))-200)) {
                    newgrouppayment.child("lsf").setValue("0");
                    newgrouppayment.child("wcomm").setValue("200");
                } else {
                    newgrouppayment.child("lsf").setValue(lsfamount);
                    newgrouppayment.child("wcomm").setValue("0");
                }
                newgrouppayment.child("loaninstallments").setValue(installmentamount);
                newgrouppayment.child("advancepayment").setValue(advancepaymen);
                newgrouppayment.child("advanceinterest").setValue(intr2);
                newgrouppayment.child("fine").setValue(fines);
                newgrouppayment.child("paymentmode").setValue(paymode);
                newgrouppayment.child("memberid").setValue(user);
                newgrouppayment.child("insurance").setValue(insu);
                newgrouppayment.child("Mpesa").setValue(mpesa);

                mDatabasemember.child("insurance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("insurancetotal").exists()) {
                            String oldsavings = (String) dataSnapshot.child("insurancetotal").getValue();
                            int oldsaving = Integer.parseInt(oldsavings);
                            newlsfs = Integer.parseInt(insu);
                            int newsaving = oldsaving + newlsfs;
                            String updatedsavings = String.valueOf(newsaving);
                            DatabaseReference savingsupdate = mDatabasemember.child("insurance");
                            savingsupdate.child("insurancetotal").setValue(updatedsavings);


                            final DatabaseReference meminsu=mDatabasemember.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date()));
                            meminsu.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()&&!insu.equals("0")){
                                        meminsu.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                        meminsu.child("groupid").setValue(grpid);
                                        meminsu.child("memberid").setValue(user);
                                        meminsu.child("amount").setValue(insu);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            String oldsavings = "0";
                            int oldsaving = Integer.parseInt(oldsavings);
                            newlsfs = Integer.parseInt(insu);
                            int newsaving = oldsaving + newlsfs;
                            String updatedsavings = String.valueOf(newsaving);
                            DatabaseReference savingsupdate = mDatabasemember.child("insurance");
                            savingsupdate.child("insurancetotal").setValue(updatedsavings);

                            DatabaseReference meminsu=mDatabasemember.child("insurance").child("all").push();
                            meminsu.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                            meminsu.child("groupid").setValue(grpid);
                            meminsu.child("memberid").setValue(user);
                            meminsu.child("amount").setValue(insu);
                        }

                        DatabaseReference groupinsu=mD1.push();
                        groupinsu.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        groupinsu.child("groupid").setValue(grpid);
                        groupinsu.child("memberid").setValue(user);
                        groupinsu.child("amount").setValue(insu);

                        DatabaseReference risk=accounting.child("Riskfund").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                        String riskkey=risk.getKey();
                        risk.child("name").setValue("Risk Fund");
                        risk.child("amount").setValue(insu);
                        risk.child("type").setValue("blue");
                        risk.child("meet").setValue(meeetid);
                        risk.child("group").setValue(grpid);
                        risk.child("debitac").setValue("Riskfund");
                        risk.child("creditac").setValue("-");
                        risk.child("description").setValue("Risk fund paid by "+nname);
                        risk.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference risk2=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                        String risk2key=risk2.getKey();
                        risk2.child("name").setValue("Risk Fund");
                        risk2.child("amount").setValue(insu);
                        risk2.child("type").setValue("blue");
                        risk2.child("meet").setValue(meeetid);
                        risk2.child("group").setValue(grpid);
                        risk2.child("debitac").setValue("Riskfund");
                        risk2.child("creditac").setValue("-");
                        risk2.child("description").setValue("Risk fund paid by "+nname);
                        risk2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        final DatabaseReference acctemptrsdata=mDatabasemember.child("accounttemptrs");
                        acctemptrsdata.child("riskkey").setValue(riskkey);
                        acctemptrsdata.child("risk2key").setValue(risk2key);

                        mDatabasemember.child("savings").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String oldsavings = (String) dataSnapshot.child("totalsavings").getValue();
                                final double oldsaving = Double.parseDouble(oldsavings);
                                double tempsavings = Double.parseDouble(lsfamount);

                                if (Double.parseDouble(with) > 1&&Double.parseDouble(with)<((tempsav+Double.parseDouble(lsfamount))-201)) {
                                    newlsf = Double.parseDouble(with);
                                    double newsaving = oldsaving - newlsf-100;
                                    updatedsavings = String.valueOf(newsaving);
                                    trstype="partial";
                                } else if (Double.parseDouble(with) == ((tempsav+Double.parseDouble(lsfamount))-200)) {
                                    newlsf = Double.parseDouble(with);
                                    double newsaving = oldsaving - newlsf-200;
                                    updatedsavings = String.valueOf(newsaving);
                                    trstype="full";
                                } else {
                                    newlsf = Double.parseDouble(lsfamount);
                                    double newsaving = oldsaving + newlsf;
                                    updatedsavings = String.valueOf(newsaving);
                                }


                                DatabaseReference savingsupdate = mDatabasemember.child("savings");
                                savingsupdate.child("totalsavings").setValue(updatedsavings);
                                if (details.equals("Contribution")){
                                    savingsupdate.child("temp").child("tempsavings").setValue(String.valueOf(tempsavings));
                                }else {
                                    savingsupdate.child("temp").child("tempsavings").setValue(with);
                                }



                                final DatabaseReference savingssupdate = mDatabasemember.child("savings").child("savings").push();
                                savingsupdate.child("temp").child("tempssid").setValue(savingssupdate.getKey());
                                savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                savingssupdate.child("details").setValue(details);
                                if (details.equals("Contribution")) {
                                    savingssupdate.child("save").setValue(String.valueOf(newlsf));
                                    savingssupdate.child("with").setValue("0");
                                } else {
                                    savingssupdate.child("save").setValue("0");
                                    savingssupdate.child("with").setValue(with);
                                }
                                savingssupdate.child("bal").setValue(updatedsavings);
                                mD2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        facname = dataSnapshot.child("name").getValue().toString();
                                        savingssupdate.child("fac").setValue(facname);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child(meeetid).child("savings").child("c2b").push();
                                meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                meettrans.child("membername").setValue(nname);
                                meettrans.child("memberid").setValue(user);
                                meettrans.child("lsf").setValue(newlsf);

                                if (paymode.equals("Part MPesa")) {
                                    Thread thread=new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            confirm();
                                        }
                                    });
                                    thread.start();
                                }

                                if (details.equals("Contribution")) {
                                    DatabaseReference membersfund=accounting.child("Membersfund").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                    String memberfundcont=membersfund.getKey();
                                    membersfund.child("name").setValue("LSF");
                                    membersfund.child("amount").setValue(lsfamount);
                                    membersfund.child("type").setValue("red");
                                    membersfund.child("meet").setValue(meeetid);
                                    membersfund.child("group").setValue(grpid);
                                    membersfund.child("debitac").setValue("BankCash");
                                    membersfund.child("creditac").setValue("Membersfund");
                                    membersfund.child("description").setValue("LSF for "+nname);
                                    membersfund.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference bank=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                    String bankkey=bank.getKey();
                                    bank.child("name").setValue("LSF");
                                    bank.child("amount").setValue(lsfamount);
                                    bank.child("type").setValue("blue");
                                    bank.child("meet").setValue(meeetid);
                                    bank.child("group").setValue(grpid);
                                    bank.child("debitac").setValue("BankCash");
                                    bank.child("creditac").setValue("Membersfund");
                                    bank.child("description").setValue("LSF for "+nname);
                                    bank.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference peracc=memacco.push();
                                    peracc.child("name").setValue("LSF");
                                    peracc.child("amount").setValue(lsfamount);
                                    peracc.child("type").setValue("blue");
                                    peracc.child("meet").setValue(meeetid);
                                    peracc.child("group").setValue(grpid);
                                    peracc.child("debitac").setValue("member");
                                    peracc.child("creditac").setValue("Membersfund");
                                    peracc.child("description").setValue("Deposit");
                                    peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);


                                    acctemptrsdata.child("memberfundcont").setValue(memberfundcont);
                                    acctemptrsdata.child("bankkey").setValue(bankkey);
                                    acctemptrsdata.child("memaccdepo").setValue(peracc.getKey());

                                } else {
                                    DatabaseReference bankw=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                    String bankwkey=bankw.getKey();
                                    bankw.child("name").setValue("LSF");
                                    bankw.child("amount").setValue(with);
                                    bankw.child("type").setValue("red");
                                    bankw.child("meet").setValue(meeetid);
                                    bankw.child("group").setValue(grpid);
                                    bankw.child("debitac").setValue("Membersfund");
                                    bankw.child("creditac").setValue("BankCash");
                                    bankw.child("description").setValue("Withdraw for "+nname);
                                    bankw.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference bankw2=accounting.child("Membersfund").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                    String bankw2key=bankw2.getKey();
                                    bankw2.child("name").setValue("LSF");
                                    bankw2.child("amount").setValue(with);
                                    bankw2.child("type").setValue("blue");
                                    bankw2.child("meet").setValue(meeetid);
                                    bankw2.child("group").setValue(grpid);
                                    bankw2.child("debitac").setValue("Membersfund");
                                    bankw2.child("creditac").setValue("BankCash");
                                    bankw2.child("description").setValue("Withdraw for "+nname);
                                    bankw2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference peracc=memacco.push();
                                    peracc.child("name").setValue("LSF");
                                    peracc.child("amount").setValue(with);
                                    peracc.child("type").setValue("red");
                                    peracc.child("meet").setValue(meeetid);
                                    peracc.child("group").setValue(grpid);
                                    peracc.child("debitac").setValue("Membersfund");
                                    peracc.child("creditac").setValue("member");
                                    peracc.child("description").setValue("Withdraw");
                                    peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);


                                    acctemptrsdata.child("bankwkey").setValue(bankwkey);
                                    acctemptrsdata.child("memberfundwith").setValue(bankw2key);
                                    acctemptrsdata.child("memaccwit").setValue(peracc.getKey());

                                    final DatabaseReference acctemptrsdata=mDatabasemember.child("accounttemptrs");
                                    mDatabase.child(grpid).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child("withdrawal").exists()){
                                                DatabaseReference unhanded=accounting.child("Unhanded").child("Trans").child("all").push();
                                                unhanded.child("name").setValue("LSF");
                                                unhanded.child("amount").setValue(with);
                                                unhanded.child("type").setValue("red");
                                                unhanded.child("meet").setValue(meeetid);
                                                unhanded.child("group").setValue(grpid);
                                                unhanded.child("debitac").setValue(nname);
                                                unhanded.child("creditac").setValue("Unhanded");
                                                unhanded.child("description").setValue(facname+"'s Imprest for Withdwals for "+nname);
                                                unhanded.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                acctemptrsdata.child("unhanded").setValue(unhanded.getKey());


                                                DatabaseReference unhanded2=accounting.child("Imprests").child(facname).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("Trans").child("all").push();
                                                unhanded2.child("name").setValue("LSF");
                                                unhanded2.child("amount").setValue(with);
                                                unhanded2.child("type").setValue("red");
                                                unhanded2.child("meet").setValue(meeetid);
                                                unhanded2.child("group").setValue(grpid);
                                                unhanded2.child("debitac").setValue("Membersfund");
                                                unhanded2.child("creditac").setValue("Unhanded");
                                                unhanded2.child("description").setValue(facname+"'s Imprest for Withdwals for "+nname);
                                                unhanded2.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                mDatabasemember.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("ptemptrs").child("status").exists()){
                                            if (dataSnapshot.child("ptemptrs").child("status").getValue().toString().equals("false")){
                                                String idd=dataSnapshot.child("ptemptrs").child("pid").getValue().toString();
                                                DatabaseReference newgroupmeetpayment = mDatabase.child(grpid).child("transactions").child(meeetid).child(idd);
                                                temptr = newgroupmeetpayment.getKey();
                                                newgroupmeetpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                newgroupmeetpayment.child("totalamount").setValue(totalrepaidamount);
                                                newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                                if (Double.parseDouble(with) > 1&&Double.parseDouble(with)<tempsav-201) {
                                                    newgroupmeetpayment.child("lsf").setValue("0");
                                                    newgroupmeetpayment.child("wcomm").setValue("100");
                                                } else if (Double.parseDouble(with) == (tempsav-200)) {
                                                    newgroupmeetpayment.child("lsf").setValue("0");
                                                    newgroupmeetpayment.child("wcomm").setValue("200");
                                                } else {
                                                    newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                                    newgroupmeetpayment.child("wcomm").setValue("0");
                                                }
                                                newgroupmeetpayment.child("loaninstallments").setValue(installmentamount);
                                                newgroupmeetpayment.child("advancepayment").setValue(advancepaymen);
                                                newgroupmeetpayment.child("fines").setValue(fines);
                                                newgroupmeetpayment.child("lsfcf").setValue(updatedsavings);
                                                newgroupmeetpayment.child("advanceinterest").setValue(intr2);
                                                newgroupmeetpayment.child("wamount").setValue(with);

                                                newgroupmeetpayment.child("paymentmode").setValue(paymode+" Mpesa: "+mpesa);
                                                newgroupmeetpayment.child("insurance").setValue(insu);
                                                newgroupmeetpayment.child("memberid").setValue(nname);
                                                newgroupmeetpayment.child("Mpesa").setValue(mpesa);
                                                newgroupmeetpayment.child("id").setValue(user);
                                                newgroupmeetpayment.child("loangvn").setValue("0");
                                                newgroupmeetpayment.child("loancf").setValue("0");
                                                newgroupmeetpayment.child("advgvn").setValue("0");
                                                newgroupmeetpayment.child("advcf").setValue("0");


                                                DatabaseReference f2 = mDatabasemember.child("ptemptrs");
                                                f2.child("status").setValue("true");

                                                DatabaseReference savingsupdatde = mDatabasemember.child("attendance");
                                                savingsupdatde.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).setValue("present");
                                            }else {
                                                if (dataSnapshot.child("attendance").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).exists()){

                                                    final String id=dataSnapshot.child("ptemptrs").child("pid").getValue().toString();

                                                    mDatabase.child(grpid).child("transactions").child(meeetid).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            double oldtrepaid=Double.parseDouble(dataSnapshot.child("totalamount").getValue().toString());
                                                            double oldlsf=Double.parseDouble(dataSnapshot.child("lsf").getValue().toString());
                                                            double oldlsfcf=Double.parseDouble(dataSnapshot.child("lsfcf").getValue().toString());
                                                            double oldloaninst=Double.parseDouble(dataSnapshot.child("loaninstallments").getValue().toString());
                                                            double oldadvance=Double.parseDouble(dataSnapshot.child("advancepayment").getValue().toString());
                                                            double oldinsurance=Double.parseDouble(dataSnapshot.child("insurance").getValue().toString());
                                                            double oldinterest=Double.parseDouble(dataSnapshot.child("advanceinterest").getValue().toString());
                                                            double oldmpesa=Double.parseDouble(dataSnapshot.child("Mpesa").getValue().toString());
                                                            double oldwith=Double.parseDouble(dataSnapshot.child("wamount").getValue().toString());

                                                            if (Double.parseDouble(with)>1){
                                                                newlsfcf=oldlsfcf-Double.parseDouble(with);
                                                            }else {
                                                                newlsfcf=oldlsfcf+Double.parseDouble(lsfamount);
                                                            }

                                                            double newtrepaid=oldtrepaid+Double.parseDouble(totalrepaidamount);
                                                            double newlsf=oldlsf+Double.parseDouble(lsfamount);
                                                            double newloaninst=oldloaninst+Double.parseDouble(installmentamount);
                                                            double newadvance=oldadvance+Double.parseDouble(advancepaymen);
                                                            double newinsurance=oldinsurance+Double.parseDouble(insu);
                                                            double newinterest=oldinterest+Double.parseDouble(intr2);
                                                            double newmpesa=oldmpesa+Double.parseDouble(mpesa);
                                                            double neweith=oldwith+Double.parseDouble(with);

                                                            DatabaseReference newgroupmeetpayment = mDatabase.child(grpid).child("transactions").child(meeetid).child(id);
                                                            newgroupmeetpayment.child("totalamount").setValue(String.valueOf(newtrepaid));
                                                            newgroupmeetpayment.child("lsf").setValue(String.valueOf(newlsf));
                                                            newgroupmeetpayment.child("lsfcf").setValue(String.valueOf(newlsfcf));
                                                            newgroupmeetpayment.child("loaninstallments").setValue(String.valueOf(newloaninst));
                                                            newgroupmeetpayment.child("advancepayment").setValue(String.valueOf(newadvance));
                                                            newgroupmeetpayment.child("insurance").setValue(String.valueOf(newinsurance));
                                                            newgroupmeetpayment.child("advanceinterest").setValue(String.valueOf(newinterest));
                                                            newgroupmeetpayment.child("Mpesa").setValue(String.valueOf(newmpesa));
                                                            newgroupmeetpayment.child("wamount").setValue(String.valueOf(neweith));

                                                            temptr=id;


                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }else {
                                                    DatabaseReference newgroupmeetpayment = mDatabase.child(grpid).child("transactions").child(meeetid).push();
                                                    temptr = newgroupmeetpayment.getKey();
                                                    newgroupmeetpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    newgroupmeetpayment.child("totalamount").setValue(totalrepaidamount);
                                                    newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                                    if (Double.parseDouble(with) > 1&&Double.parseDouble(with)<tempsav-201) {
                                                        newgroupmeetpayment.child("lsf").setValue("0");
                                                        newgroupmeetpayment.child("wcomm").setValue("100");
                                                    } else if (Double.parseDouble(with) == (tempsav-200)) {
                                                        newgroupmeetpayment.child("lsf").setValue("0");
                                                        newgroupmeetpayment.child("wcomm").setValue("200");
                                                    } else {
                                                        newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                                        newgroupmeetpayment.child("wcomm").setValue("0");
                                                    }
                                                    newgroupmeetpayment.child("loaninstallments").setValue(installmentamount);
                                                    newgroupmeetpayment.child("advancepayment").setValue(advancepaymen);
                                                    newgroupmeetpayment.child("fines").setValue(fines);
                                                    newgroupmeetpayment.child("lsfcf").setValue(updatedsavings);
                                                    newgroupmeetpayment.child("advanceinterest").setValue(intr2);
                                                    newgroupmeetpayment.child("wamount").setValue(with);
                                                    newgroupmeetpayment.child("paymentmode").setValue(paymode+" Mpesa: "+mpesa);
                                                    newgroupmeetpayment.child("insurance").setValue(insu);
                                                    newgroupmeetpayment.child("memberid").setValue(nname);
                                                    newgroupmeetpayment.child("Mpesa").setValue(mpesa);
                                                    newgroupmeetpayment.child("id").setValue(user);
                                                    newgroupmeetpayment.child("loangvn").setValue("0");
                                                    newgroupmeetpayment.child("loancf").setValue("0");
                                                    newgroupmeetpayment.child("advgvn").setValue("0");
                                                    newgroupmeetpayment.child("advcf").setValue("0");

                                                    DatabaseReference f2 = mDatabasemember.child("ptemptrs");
                                                    f2.child("status").setValue("true");

                                                    DatabaseReference savingsupdatde = mDatabasemember.child("attendance");
                                                    savingsupdatde.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).setValue("present");
                                                }
                                            }
                                        }else {
                                            DatabaseReference newgroupmeetpayment = mDatabase.child(grpid).child("transactions").child(meeetid).push();
                                            temptr = newgroupmeetpayment.getKey();
                                            newgroupmeetpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                            newgroupmeetpayment.child("totalamount").setValue(totalrepaidamount);
                                            newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                            if (Double.parseDouble(with) > 1&&Double.parseDouble(with)<tempsav-201) {
                                                newgroupmeetpayment.child("lsf").setValue("0");
                                                newgroupmeetpayment.child("wcomm").setValue("100");
                                            } else if (Double.parseDouble(with) == (tempsav-200)) {
                                                newgroupmeetpayment.child("lsf").setValue("0");
                                                newgroupmeetpayment.child("wcomm").setValue("200");
                                            } else {
                                                newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                                newgroupmeetpayment.child("wcomm").setValue("0");
                                            }
                                            newgroupmeetpayment.child("loaninstallments").setValue(installmentamount);
                                            newgroupmeetpayment.child("advancepayment").setValue(advancepaymen);
                                            newgroupmeetpayment.child("fines").setValue(fines);
                                            newgroupmeetpayment.child("lsfcf").setValue(updatedsavings);
                                            newgroupmeetpayment.child("advanceinterest").setValue(intr2);
                                            newgroupmeetpayment.child("wamount").setValue(with);
                                            newgroupmeetpayment.child("paymentmode").setValue(paymode+" Mpesa: "+mpesa);
                                            newgroupmeetpayment.child("insurance").setValue(insu);
                                            newgroupmeetpayment.child("memberid").setValue(nname);
                                            newgroupmeetpayment.child("Mpesa").setValue(mpesa);
                                            newgroupmeetpayment.child("id").setValue(user);
                                            newgroupmeetpayment.child("loangvn").setValue("0");
                                            newgroupmeetpayment.child("loancf").setValue("0");
                                            newgroupmeetpayment.child("advgvn").setValue("0");
                                            newgroupmeetpayment.child("advcf").setValue("0");


                                            DatabaseReference f2 = mDatabasemember.child("ptemptrs");
                                            f2.child("status").setValue("true");
                                            f2.child("pid").setValue(temptr);

                                            DatabaseReference savingsupdatde = mDatabasemember.child("attendance");
                                            savingsupdatde.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).setValue("present");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                mDatabasemember.child("loans").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String flc;
                                        String oldloans = (String) dataSnapshot.child("totalloan").getValue();
                                        double oldloan = Double.parseDouble(oldloans);
                                        double temploan = Double.parseDouble(installmentamount);
                                        newinstallment = Double.parseDouble(installmentamount);
                                        double newloan = oldloan - newinstallment;
                                        updatedloans = String.valueOf(newloan);
                                        final DatabaseReference loansupdate = mDatabasemember.child("loans");
                                        loansupdate.child("totalloan").setValue(updatedloans);
                                        loansupdate.child("temp").child("temploan").setValue(String.valueOf(temploan));
                                        if (temploan>1){
                                            loansupdate.child("nextloanpaydate").setValue(setnextdate());
                                            DatabaseReference savingsupdatde1 = mDatabasemember.child("loans").child("attendance");
                                            savingsupdatde1.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).setValue("present");

                                        }else {
                                            loansupdate.child("nextloanpaydate").setValue(setnextdate());
                                        }
                                        loansupdate.child("temp").child("templl").setValue(savingssupdate.getKey());


                                        if (newinstallment>0){
                                            String comp = (String) dataSnapshot.child("installment").getValue();


                                            if (dataSnapshot.child("flag").exists()){
                                                fl = (String) dataSnapshot.child("flag").getValue();
                                            }else {
                                                fl = "1";
                                            }

                                            if (dataSnapshot.child("compflag").exists()){
                                                flc = (String) dataSnapshot.child("compflag").getValue();
                                            }else {
                                                flc = "1";
                                            }


                                            double oldflag = Double.parseDouble(fl);
                                            double oldflagc = Double.parseDouble(flc);
                                            double compp = Double.parseDouble(comp);

                                            if (((compp*(oldflagc+1))-((compp*(oldflag))))>compp+1000){
                                                DatabaseReference rr=FirebaseDatabase.getInstance().getReference().child("loanarreas").child(user);
                                                rr.child("membername").setValue(nname);
                                                rr.child("arreasamount").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));
                                                rr.child("groupname").setValue(mGroupName.getText().toString());
                                                rr.child("groupid").setValue(grpid);
                                            }

                                            loansupdate.child("flag").setValue(String.valueOf(oldflag+1));
                                            loansupdate.child("compflag").setValue(String.valueOf(oldflagc+1));

                                        }


                                        mDatabasemember.child("ptemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String id=dataSnapshot.child("pid").getValue().toString();
                                                DatabaseReference newgroupmeetpayment1 = mDatabase.child(grpid).child("transactions").child(meeetid).child(id);
                                                newgroupmeetpayment1.child("loancf").setValue(updatedloans);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                        final DatabaseReference savingssupdate = mDatabasemember.child("loans").child("loanss").push();
                                        savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                        savingssupdate.child("loanbf").setValue(oldloans);
                                        savingssupdate.child("inst").setValue(installmentamount);
                                        savingssupdate.child("bal").setValue(updatedloans);
                                        mD2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                facname = dataSnapshot.child("name").getValue().toString();
                                                savingssupdate.child("fac").setValue(facname);
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        if (newloan == 0) {
                                            DatabaseReference rm = mDatabasemember.child("loans");
                                            rm.child("installment").setValue("0");
                                            rm.child("info").removeValue();
                                            rm.child("totalloan").setValue("0");
                                            rm.child("nextloanpaydate").removeValue();

                                            DatabaseReference rmm = mDatabasemember;
                                            rmm.child("loanflag").removeValue();

                                            mDatatabasegroup.child(grpid).child("loans").child("loanon").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                        if (snapshot.child("user").getValue().toString().equals(user)){
                                                            DatabaseReference rrm=mDatatabasegroup.child(grpid).child("loans").child("loanon").child(snapshot.getKey());
                                                            rrm.removeValue();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                        if (Double.parseDouble(installmentamount)>1){
                                            DatabaseReference ltl=accounting.child("Longtermloan").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                            String ltlk=ltl.getKey();
                                            ltl.child("name").setValue("Loan");
                                            ltl.child("amount").setValue(installmentamount);
                                            ltl.child("type").setValue("red");
                                            ltl.child("meet").setValue(meeetid);
                                            ltl.child("group").setValue(grpid);
                                            ltl.child("debitac").setValue("BankCash");
                                            ltl.child("creditac").setValue("Longtermloan");
                                            ltl.child("description").setValue("Loan Installment by "+nname);
                                            ltl.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                            DatabaseReference ltl2=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                            String bankltlk=ltl2.getKey();
                                            ltl2.child("name").setValue("Loan");
                                            ltl2.child("amount").setValue(installmentamount);
                                            ltl2.child("type").setValue("green");
                                            ltl2.child("meet").setValue(meeetid);
                                            ltl2.child("group").setValue(grpid);
                                            ltl2.child("debitac").setValue("Longtermloan");
                                            ltl2.child("creditac").setValue("BankCash");
                                            ltl2.child("description").setValue("Loan Installment by "+nname);
                                            ltl2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                            DatabaseReference peracc=memacco.push();
                                            peracc.child("name").setValue("Loan");
                                            peracc.child("amount").setValue(installmentamount);
                                            peracc.child("type").setValue("red");
                                            peracc.child("meet").setValue(meeetid);
                                            peracc.child("group").setValue(grpid);
                                            peracc.child("debitac").setValue("BankCash");
                                            peracc.child("creditac").setValue("member");
                                            peracc.child("description").setValue("Loan Installment");
                                            peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);


                                            acctemptrsdata.child("loanikey").setValue(ltlk);
                                            acctemptrsdata.child("bankloanikey").setValue(bankltlk);
                                            acctemptrsdata.child("memaccloan").setValue(peracc.getKey());
                                        }

                                        DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child(meeetid).child("loans").child("c2b").push();
                                        meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                        meettrans.child("membername").setValue(nname);
                                        meettrans.child("memberid").setValue(user);
                                        meettrans.child("installment").setValue(newinstallment);

                                        mDatabasemember.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (!dataSnapshot.child("loans").child("totalloan").getValue().toString().equals("0")&&Double.parseDouble(installmentamount)==0){
                                                    mDatabasemember.child("loans").child("attendance").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).exists()){
                                                                DatabaseReference savingsupdatde1 = mDatabasemember.child("loans").child("attendance");
                                                                savingsupdatde1.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).removeValue();
                                                                loansupdate.child("nextloanpaydate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        mDatabasemember.child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String adv=null;
                                                if (opts == 2) {
                                                    String oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                    double oldadvances = Double.parseDouble(oldadvance);
                                                    adv=oldadvance;
                                                    newadvancepayment = Double.parseDouble(advancepayment);
                                                    double newadvance = oldadvances - newadvancepayment;
                                                    double tempadvance = tempadv;
                                                    final String updatedadvance = String.valueOf(newadvance);
                                                    final DatabaseReference advupdate = mDatabasemember.child("advances");
                                                    advupdate.child("temp").child("tempadv").setValue(String.valueOf(tempadvance));
                                                    advupdate.child("currentadvance").setValue(updatedadvance);
                                                    if (newadvancepayment>0){
                                                        advupdate.child("nextadvpaydate").setValue(setnextdate());
                                                        DatabaseReference savingsupdatde1 = mDatabasemember.child("advances").child("attendance");
                                                        savingsupdatde1.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).setValue("present");

                                                    }else {
                                                        advupdate.child("nextadvpaydate").setValue(setnextdate());
                                                    }


                                                    mDatabasemember.child("ptemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String id=dataSnapshot.child("pid").getValue().toString();
                                                            DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("transactions").child(meeetid).child(id);
                                                            newgroupmeetpayment3.child("advcf").setValue(updatedadvance);
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });


                                                    if (newadvance == 0) {
                                                        DatabaseReference rm = mDatabasemember.child("advances");
                                                        rm.child("currpenalty").removeValue();
                                                    }

                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepayment);

                                                    mDatabasemember.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (Double.parseDouble(dataSnapshot.child("advances").child("currentadvance").getValue().toString())>1&&Double.parseDouble(advancepayment)<1){
                                                                mDatabasemember.child("advances").child("attendance").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).exists()){
                                                                            DatabaseReference savingsupdatde1 = mDatabasemember.child("advances").child("attendance");
                                                                            savingsupdatde1.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).removeValue();
                                                                            advupdate.child("nextadvpaydate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });



                                                    DatabaseReference ltla=accounting.child("Shorttermloans").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    ltla.child("name").setValue("Short Term Loan");
                                                    ltla.child("amount").setValue(advancepayment);
                                                    ltla.child("type").setValue("red");
                                                    ltla.child("meet").setValue(meeetid);
                                                    ltla.child("group").setValue(grpid);
                                                    ltla.child("debitac").setValue("BankCash");
                                                    ltla.child("creditac").setValue("Shorttermloans");
                                                    ltla.child("description").setValue("Advance paid by "+nname);
                                                    ltla.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference ltl2a=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    ltl2a.child("name").setValue("Short Term Loan");
                                                    ltl2a.child("amount").setValue(advancepayment);
                                                    ltl2a.child("type").setValue("green");
                                                    ltl2a.child("meet").setValue(meeetid);
                                                    ltl2a.child("group").setValue(grpid);
                                                    ltl2a.child("debitac").setValue("Shorttermloans");
                                                    ltl2a.child("creditac").setValue("BankCash");
                                                    ltl2a.child("description").setValue("Advance paid by "+nname);
                                                    ltl2a.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference peracc=memacco.push();
                                                    peracc.child("name").setValue("Short Term Loan");
                                                    peracc.child("amount").setValue(advancepayment);
                                                    peracc.child("type").setValue("red");
                                                    peracc.child("meet").setValue(meeetid);
                                                    peracc.child("group").setValue(grpid);
                                                    peracc.child("debitac").setValue("BankCash");
                                                    peracc.child("creditac").setValue("member");
                                                    peracc.child("description").setValue("Advance Installment");
                                                    peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    acctemptrsdata.child("advpkey").setValue(ltla.getKey());
                                                    acctemptrsdata.child("bankadvpkey").setValue(ltl2a.getKey());
                                                    acctemptrsdata.child("memaccadv").setValue(peracc.getKey());

                                                    final DatabaseReference savingssupdatez = mDatabasemember.child("advances").child("advancesss").push();
                                                    savingssupdatez.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    savingssupdatez.child("advbf").setValue(oldadvance);
                                                    savingssupdatez.child("paid").setValue(advancepayment);
                                                    savingssupdatez.child("bal").setValue(updatedadvance);
                                                    mD2.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            facname = dataSnapshot.child("name").getValue().toString();
                                                            savingssupdatez.child("fac").setValue(facname);
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });


                                                } else if (opts == 0) {
                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepayment);

                                                    DatabaseReference advupdate = mDatabasemember.child("advances");
                                                    advupdate.child("nextadvpaydate").setValue(setnextdate());

                                                    mDatabasemember.child("ptemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            final String id=dataSnapshot.child("pid").getValue().toString();
                                                            mDatabasemember.child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    String oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                                    DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("transactions").child(meeetid).child(id);
                                                                    newgroupmeetpayment3.child("advcf").setValue(oldadvance);
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

                                                    DatabaseReference advudate = mDatabasemember.child("advances");
                                                    advudate.child("temp").child("tempadv").setValue("0");

                                                    DatabaseReference inc1ad=accounting.child("Income").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    String inc1adkey=inc1ad.getKey();
                                                    inc1ad.child("name").setValue("Income");
                                                    inc1ad.child("amount").setValue(advancepayment);
                                                    inc1ad.child("type").setValue("blue");
                                                    inc1ad.child("meet").setValue(meeetid);
                                                    inc1ad.child("group").setValue(grpid);
                                                    inc1ad.child("debitac").setValue("Income");
                                                    inc1ad.child("creditac").setValue("-");
                                                    inc1ad.child("description").setValue("Advance Interest paid by "+nname);
                                                    inc1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference inc2ad=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    String inc2adkey=inc2ad.getKey();
                                                    inc2ad.child("name").setValue("Income");
                                                    inc2ad.child("amount").setValue(advancepayment);
                                                    inc2ad.child("type").setValue("blue");
                                                    inc2ad.child("meet").setValue(meeetid);
                                                    inc1ad.child("group").setValue(grpid);
                                                    inc2ad.child("debitac").setValue("Income");
                                                    inc2ad.child("creditac").setValue("-");
                                                    inc2ad.child("description").setValue("Advance Interest paid by "+nname);
                                                    inc2ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    acctemptrsdata.child("inc1adkey").setValue(inc1adkey);
                                                    acctemptrsdata.child("bankinc2adkey").setValue(inc2adkey);

                                                    final DatabaseReference savingssupdatez = mDatabasemember.child("advances").child("advancesss").push();
                                                    savingssupdatez.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    savingssupdatez.child("advbf").setValue(adv);
                                                    savingssupdatez.child("paid").setValue("Interest paid "+advancepayment);
                                                    savingssupdatez.child("bal").setValue(adv);
                                                    mD2.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            facname = dataSnapshot.child("name").getValue().toString();
                                                            savingssupdatez.child("fac").setValue(facname);
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });


                                                } else {

                                                    String oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                    double oldadvances = Double.parseDouble(oldadvance);
                                                    newadvancepayment = advpy;
                                                    double newadvance = oldadvances - newadvancepayment;
                                                    final DatabaseReference advupdate = mDatabasemember.child("advances");
                                                    advupdate.child("currentadvance").setValue(String.valueOf(advncecf));
                                                    advupdate.child("currpenalty").setValue(String.valueOf(intr2));
                                                    advupdate.child("nextadvpaydate").setValue(setnextdate());
                                                    DatabaseReference advudate = mDatabasemember.child("advances");

                                                    advudate.child("temp").child("tempadv").setValue(mAdvnpay.getText().toString());


                                                    mDatabasemember.child("ptemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String id=dataSnapshot.child("pid").getValue().toString();
                                                            DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("transactions").child(meeetid).child(id);
                                                            newgroupmeetpayment3.child("advcf").setValue(String.valueOf(advncecf));
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    DatabaseReference ltlap=accounting.child("Shorttermloans").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    ltlap.child("name").setValue("Short Term Loan");
                                                    ltlap.child("amount").setValue(String.valueOf(advpy));
                                                    ltlap.child("type").setValue("red");
                                                    ltlap.child("meet").setValue(meeetid);
                                                    ltlap.child("group").setValue(grpid);
                                                    ltlap.child("debitac").setValue("BankCash");
                                                    ltlap.child("creditac").setValue("Shorttermloans");
                                                    ltlap.child("description").setValue("Partial advance paid by "+nname);
                                                    ltlap.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference ltl2ap=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    ltl2ap.child("name").setValue("Short Term Loan");
                                                    ltl2ap.child("amount").setValue(String.valueOf(advpy));
                                                    ltl2ap.child("type").setValue("green");
                                                    ltl2ap.child("meet").setValue(meeetid);
                                                    ltl2ap.child("group").setValue(grpid);
                                                    ltl2ap.child("debitac").setValue("Shorttermloans");
                                                    ltl2ap.child("creditac").setValue("BankCash");
                                                    ltl2ap.child("description").setValue("Partial advance paid by "+nname);
                                                    ltl2ap.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    acctemptrsdata.child("advpikey").setValue(ltlap.getKey());
                                                    acctemptrsdata.child("bankadvpikey").setValue(ltl2ap.getKey());

                                                    DatabaseReference advpint=accounting.child("Income").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    advpint.child("name").setValue("Income");
                                                    advpint.child("amount").setValue(intr2);
                                                    advpint.child("type").setValue("blue");
                                                    advpint.child("meet").setValue(meeetid);
                                                    advpint.child("group").setValue(grpid);
                                                    advpint.child("debitac").setValue("Income");
                                                    advpint.child("creditac").setValue("-");
                                                    advpint.child("description").setValue("Advance Interest paid by "+nname);
                                                    advpint.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference advpintbank=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    advpintbank.child("name").setValue("Income");
                                                    advpintbank.child("amount").setValue(intr2);
                                                    advpintbank.child("type").setValue("blue");
                                                    advpintbank.child("meet").setValue(meeetid);
                                                    advpintbank.child("group").setValue(grpid);
                                                    advpintbank.child("debitac").setValue("Income");
                                                    advpintbank.child("creditac").setValue("-");
                                                    advpintbank.child("description").setValue("Advance Interest paid by "+nname);
                                                    advpintbank.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference peracc=memacco.push();
                                                    peracc.child("name").setValue("Short Term Loan");
                                                    peracc.child("amount").setValue(String.valueOf(advpy));
                                                    peracc.child("type").setValue("red");
                                                    peracc.child("meet").setValue(meeetid);
                                                    peracc.child("group").setValue(grpid);
                                                    peracc.child("debitac").setValue("BankCash");
                                                    peracc.child("creditac").setValue("member");
                                                    peracc.child("description").setValue("Advance Installment");
                                                    peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    acctemptrsdata.child("advpintkey").setValue(advpint.getKey());
                                                    acctemptrsdata.child("bankadvpintkey").setValue(advpintbank.getKey());
                                                    acctemptrsdata.child("memaccadvint").setValue(peracc.getKey());


                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepaymen);

                                                    final DatabaseReference savingssupdatez = mDatabasemember.child("advances").child("advancesss").push();
                                                    savingssupdatez.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    savingssupdatez.child("advbf").setValue(oldadvance);
                                                    savingssupdatez.child("paid").setValue("Partial paid "+advancepaymen);
                                                    savingssupdatez.child("bal").setValue(String.valueOf(advncecf));
                                                    mD2.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            facname = dataSnapshot.child("name").getValue().toString();
                                                            savingssupdatez.child("fac").setValue(facname);
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });


                                                    if (!fines.equals("0")){
                                                        DatabaseReference fees1f=accounting.child("Feesfines").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                        String fees1fkey=fees1f.getKey();
                                                        fees1f.child("name").setValue("Fees and Fines");
                                                        fees1f.child("amount").setValue(fines);
                                                        fees1f.child("type").setValue("blue");
                                                        fees1f.child("meet").setValue(meeetid);
                                                        fees1f.child("group").setValue(grpid);
                                                        fees1f.child("debitac").setValue("BankCash");
                                                        fees1f.child("creditac").setValue("-");
                                                        fees1f.child("description").setValue("Fines paid for "+nname);
                                                        fees1f.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                        DatabaseReference fees2f=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                        String fees2fkey=fees2f.getKey();
                                                        fees2f.child("name").setValue("Fees and Fines");
                                                        fees2f.child("amount").setValue(fines);
                                                        fees2f.child("type").setValue("blue");
                                                        fees2f.child("meet").setValue(meeetid);
                                                        fees2f.child("group").setValue(grpid);
                                                        fees2f.child("debitac").setValue("Feesfines");
                                                        fees2f.child("creditac").setValue("-");
                                                        fees2f.child("description").setValue("Fines paid for"+nname);
                                                        fees2f.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                        acctemptrsdata.child("fees1fkey").setValue(fees1fkey);
                                                        acctemptrsdata.child("bankfees2fkey").setValue(fees2fkey);
                                                    }

                                                }

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


                DatabaseReference  ref=mDatabase.child(grpid).child("meetings").child(meeetid).child("attendancelist").push();
                ref.child("id").setValue(user);
                ref.child("name").setValue(nname);


                Toast.makeText(payment.this, "Payment Processed!", Toast.LENGTH_SHORT).show();

                Intent home = new Intent(payment.this, memberlist.class);
                home.putExtra("key", grpid);
                startActivity(home);
                finish();
            } else {
                Toast.makeText(payment.this, "Enter All Values..", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.e("Exception",String.valueOf(e));
        }

    }

    private void processpaymentf() {
        final String totalrepaidamount = mTotalrepaid.getText().toString();
        final String lsfamount = mLSF.getText().toString();
        final String installmentamount = mloanRep.getText().toString();
        final String advancepayment = mAdvnpay.getText().toString();
        final String fines = mFines.getText().toString();
        final String insu = mInsurance.getText().toString();
        final String mpesa = mMpesa.getText().toString();
        final String with = mWithdraw.getText().toString();
        final String others=mOther.getText().toString();


        try {
            if (!TextUtils.isEmpty(advancepayment) && !TextUtils.isEmpty(fines) && !TextUtils.isEmpty(installmentamount)&& !TextUtils.isEmpty(others)) {
                if (opts == 2) {
                    intr2 = "0";
                    advancepaymen = advancepayment;
                    tempadv = Double.parseDouble(advancepaymen);
                } else if (opts == 0) {
                    intr2 = advancepayment;
                    advancepaymen = "0";
                    tempadv = Double.parseDouble("0");
                } else {

                    advncecf = Math.round(((Double.parseDouble(currentadvace) - Double.parseDouble(advancepayment)+10) *1.1)*100)/100;
                    advpy = Math.round((Double.parseDouble(currentadvace) - advncecf)*100)/100;
                    intr2 = String.valueOf((advncecf/11)+10);
                    advancepaymen = String.valueOf(advpy);
                    tempadv = advpy;
                }


                DatabaseReference newmemberpayment = mDatabasemember.child("transactions").push();
                newmemberpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                newmemberpayment.child("totalamount").setValue(totalrepaidamount);
                newmemberpayment.child("lsf").setValue(lsfamount);
                newmemberpayment.child("wcomm").setValue("0");
                newmemberpayment.child("loaninstallments").setValue(installmentamount);
                newmemberpayment.child("advancepayment").setValue(advancepaymen);
                newmemberpayment.child("advanceinterest").setValue(intr2);
                newmemberpayment.child("paymentmode").setValue(paymode);
                newmemberpayment.child("memberid").setValue(user);
                newmemberpayment.child("fine").setValue(fines);
                newmemberpayment.child("insurance").setValue(insu);
                newmemberpayment.child("Mpesa").setValue(mpesa);

                DatabaseReference newgrouppayment = mDatabasetrans.child(grpid).push();
                newgrouppayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                newgrouppayment.child("totalamount").setValue(totalrepaidamount);
                newgrouppayment.child("wamount").setValue(with);
                newgrouppayment.child("lsf").setValue(lsfamount);
                newgrouppayment.child("wcomm").setValue("0");
                newgrouppayment.child("loaninstallments").setValue(installmentamount);
                newgrouppayment.child("advancepayment").setValue(advancepaymen);
                newgrouppayment.child("advanceinterest").setValue(intr2);
                newgrouppayment.child("fine").setValue(fines);
                newgrouppayment.child("paymentmode").setValue(paymode);
                newgrouppayment.child("memberid").setValue(user);
                newgrouppayment.child("insurance").setValue(insu);
                newgrouppayment.child("Mpesa").setValue(mpesa);

                mDatabasemember.child("insurance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("insurancetotal").exists()) {
                            String oldsavings = (String) dataSnapshot.child("insurancetotal").getValue();
                            int oldsaving = Integer.parseInt(oldsavings);
                            newlsfs = Integer.parseInt(insu);
                            int newsaving = oldsaving + newlsfs;
                            String updatedsavings = String.valueOf(newsaving);
                            DatabaseReference savingsupdate = mDatabasemember.child("insurance");
                            savingsupdate.child("insurancetotal").setValue(updatedsavings);


                            final DatabaseReference meminsu=mDatabasemember.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date()));
                            meminsu.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()&&!insu.equals("0")){
                                        meminsu.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                        meminsu.child("groupid").setValue(grpid);
                                        meminsu.child("memberid").setValue(user);
                                        meminsu.child("amount").setValue(insu);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            String oldsavings = "0";
                            int oldsaving = Integer.parseInt(oldsavings);
                            newlsfs = Integer.parseInt(insu);
                            int newsaving = oldsaving + newlsfs;
                            String updatedsavings = String.valueOf(newsaving);
                            DatabaseReference savingsupdate = mDatabasemember.child("insurance");
                            savingsupdate.child("insurancetotal").setValue(updatedsavings);

                            DatabaseReference meminsu=mDatabasemember.child("insurance").child("all").push();
                            meminsu.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                            meminsu.child("groupid").setValue(grpid);
                            meminsu.child("memberid").setValue(user);
                            meminsu.child("amount").setValue(insu);
                        }

                        DatabaseReference groupinsu=mD1.push();
                        groupinsu.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        groupinsu.child("groupid").setValue(grpid);
                        groupinsu.child("memberid").setValue(user);
                        groupinsu.child("amount").setValue(insu);

                        DatabaseReference risk=accounting.child("Riskfund").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                        risk.child("name").setValue("Risk Fund");
                        risk.child("amount").setValue(insu);
                        risk.child("type").setValue("blue");
                        risk.child("meet").setValue(meeetid);
                        risk.child("group").setValue(grpid);
                        risk.child("debitac").setValue("Riskfund");
                        risk.child("creditac").setValue("-");
                        risk.child("description").setValue("Risk fund paid by "+nname);
                        risk.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference risk2=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                        risk2.child("name").setValue("Risk Fund");
                        risk2.child("amount").setValue(insu);
                        risk2.child("type").setValue("blue");
                        risk2.child("meet").setValue(meeetid);
                        risk2.child("group").setValue(grpid);
                        risk2.child("debitac").setValue("Riskfund");
                        risk2.child("creditac").setValue("-");
                        risk2.child("description").setValue("Risk fund paid by "+nname);
                        risk2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        mDatabasemember.child("savings").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String oldsavings = (String) dataSnapshot.child("totalsavings").getValue();
                                final double oldsaving = Double.parseDouble(oldsavings);
                                double tempsavings = Double.parseDouble(lsfamount);
                                newlsf = Double.parseDouble(lsfamount);
                                double newsaving = oldsaving + newlsf;
                                updatedsavings = String.valueOf(newsaving);

                                DatabaseReference savingsupdate = mDatabasemember.child("savings");
                                savingsupdate.child("totalsavings").setValue(updatedsavings);
                                savingsupdate.child("temp").child("tempsavings").setValue(String.valueOf(tempsavings));



                                final DatabaseReference savingssupdate = mDatabasemember.child("savings").child("savings").push();
                                savingsupdate.child("temp").child("tempssid").setValue(savingssupdate.getKey());
                                savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                savingssupdate.child("details").setValue("Contribution");
                                savingssupdate.child("save").setValue(String.valueOf(newlsf));
                                savingssupdate.child("with").setValue("0");
                                savingssupdate.child("bal").setValue(updatedsavings);

                                mD2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        facname = dataSnapshot.child("name").getValue().toString();
                                        savingssupdate.child("fac").setValue(facname);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child(meeetid).child("savings").child("c2b").push();
                                meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                meettrans.child("membername").setValue(nname);
                                meettrans.child("memberid").setValue(user);
                                meettrans.child("lsf").setValue(newlsf);

                                if (paymode.equals("Part MPesa")) {
                                    Thread thread=new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            confirm();
                                        }
                                    });
                                    thread.start();
                                }

                                DatabaseReference membersfund=accounting.child("Membersfund").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                membersfund.child("name").setValue("LSF");
                                membersfund.child("amount").setValue(lsfamount);
                                membersfund.child("type").setValue("red");
                                membersfund.child("meet").setValue(meeetid);
                                membersfund.child("group").setValue(grpid);
                                membersfund.child("debitac").setValue("BankCash");
                                membersfund.child("creditac").setValue("Membersfund");
                                membersfund.child("description").setValue("LSF for "+nname);
                                membersfund.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                DatabaseReference bank=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                bank.child("name").setValue("LSF");
                                bank.child("amount").setValue(lsfamount);
                                bank.child("type").setValue("blue");
                                bank.child("meet").setValue(meeetid);
                                bank.child("group").setValue(grpid);
                                bank.child("debitac").setValue("BankCash");
                                bank.child("creditac").setValue("Membersfund");
                                bank.child("description").setValue("LSF for "+nname);
                                bank.child("timestamp").setValue(ServerValue.TIMESTAMP);



                                DatabaseReference newgroupmeetpayment = mDatabase.child(grpid).child("transactions").child(meeetid).push();
                                temptr = newgroupmeetpayment.getKey();
                                newgroupmeetpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                newgroupmeetpayment.child("totalamount").setValue(totalrepaidamount);
                                newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                newgroupmeetpayment.child("wcomm").setValue("0");
                                newgroupmeetpayment.child("loaninstallments").setValue(installmentamount);
                                newgroupmeetpayment.child("advancepayment").setValue(advancepaymen);
                                newgroupmeetpayment.child("fines").setValue(fines);
                                newgroupmeetpayment.child("lsfcf").setValue(updatedsavings);
                                newgroupmeetpayment.child("advanceinterest").setValue(intr2);
                                newgroupmeetpayment.child("wamount").setValue(with);
                                newgroupmeetpayment.child("paymentmode").setValue(paymode);
                                newgroupmeetpayment.child("insurance").setValue(insu);
                                newgroupmeetpayment.child("memberid").setValue(nname);
                                newgroupmeetpayment.child("Mpesa").setValue(mpesa);
                                newgroupmeetpayment.child("id").setValue(user);
                                newgroupmeetpayment.child("loangvn").setValue("0");
                                newgroupmeetpayment.child("loancf").setValue("0");
                                newgroupmeetpayment.child("advgvn").setValue("0");
                                newgroupmeetpayment.child("advcf").setValue("0");

                                DatabaseReference f = mDatabasemember.child("ptemptrs");
                                f.child("pid").setValue(temptr);
                                f.child("status").setValue("false");

                                DatabaseReference savingsupdatde = mDatabasemember.child("attendance");
                                savingsupdatde.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).setValue("present");

                                mDatabasemember.child("loans").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String flc;
                                        String oldloans = (String) dataSnapshot.child("totalloan").getValue();
                                        double oldloan = Double.parseDouble(oldloans);
                                        double temploan = Double.parseDouble(installmentamount);
                                        newinstallment = Double.parseDouble(installmentamount);
                                        double newloan = oldloan - newinstallment;
                                        updatedloans = String.valueOf(newloan);
                                        DatabaseReference loansupdate = mDatabasemember.child("loans");
                                        loansupdate.child("totalloan").setValue(updatedloans);
                                        loansupdate.child("temp").child("temploan").setValue(String.valueOf(temploan));
                                        loansupdate.child("loanflag").setValue(setnextdate());

                                        if (dataSnapshot.child("flag").exists()){
                                            String comp = (String) dataSnapshot.child("installment").getValue();
                                            String fl = (String) dataSnapshot.child("flag").getValue();
                                            if (dataSnapshot.child("compflag").exists()) {
                                                flc = (String) dataSnapshot.child("compflag").getValue();
                                            }else {
                                                flc = "0";
                                            }
                                            double oldflag = Double.parseDouble(fl);
                                            double oldflagc = Double.parseDouble(flc);
                                            double compp = Double.parseDouble(comp);

                                            if (((compp*(oldflagc+1))-((compp*(oldflag))))>compp){
                                                DatabaseReference rr=FirebaseDatabase.getInstance().getReference().child("loanarreas").child(user);
                                                rr.child("membername").setValue(nname);
                                                rr.child("arreasamount").setValue(String.valueOf(((compp*(oldflagc+1))-((compp*(oldflag))))));
                                                rr.child("groupname").setValue(mGroupName.getText().toString());
                                                rr.child("groupid").setValue(grpid);
                                            }

                                            loansupdate.child("flag").setValue(String.valueOf(oldflag+1));
                                            loansupdate.child("compflag").setValue(String.valueOf(oldflagc+1));

                                        }else {
                                            loansupdate.child("flag").setValue("1");
                                        }



                                        DatabaseReference newgroupmeetpayment1 = mDatabase.child(grpid).child("transactions").child(meeetid).child(temptr);
                                        newgroupmeetpayment1.child("loancf").setValue(updatedloans);

                                        final DatabaseReference savingssupdate = mDatabasemember.child("loans").child("loanss").push();
                                        loansupdate.child("temp").child("templl").setValue(savingssupdate.getKey());
                                        savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                        savingssupdate.child("loanbf").setValue(oldloans);
                                        savingssupdate.child("inst").setValue(installmentamount);
                                        savingssupdate.child("bal").setValue(updatedloans);
                                        mD2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                facname = dataSnapshot.child("name").getValue().toString();
                                                savingssupdate.child("fac").setValue(facname);
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        if (newloan == 0) {
                                            DatabaseReference rm = mDatabasemember.child("loans");
                                            rm.child("installment").setValue("0");
                                            rm.child("info").removeValue();
                                            rm.child("totalloan").setValue("0");
                                            rm.child("installment").setValue("0");
                                            rm.child("nextloanpaydate").removeValue();

                                            DatabaseReference rmm = mDatabasemember;
                                            rmm.child("loanflag").removeValue();

                                            mDatatabasegroup.child(grpid).child("loans").child("loanon").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                        if (snapshot.child("user").getValue().toString().equals(user)){
                                                            DatabaseReference rrm=mDatatabasegroup.child(grpid).child("loans").child("loanon").child(snapshot.getKey());
                                                            rrm.removeValue();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }


                                        DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child(meeetid).child("loans").child("c2b").push();
                                        meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                        meettrans.child("membername").setValue(nname);
                                        meettrans.child("memberid").setValue(user);
                                        meettrans.child("installment").setValue(newinstallment);

                                        mDatabasemember.child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (opts == 2) {
                                                    String oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                    double oldadvances = Double.parseDouble(oldadvance);
                                                    newadvancepayment = Double.parseDouble(advancepayment);
                                                    double newadvance = oldadvances - newadvancepayment;
                                                    double tempadvance = tempadv;
                                                    final String updatedadvance = String.valueOf(newadvance);
                                                    DatabaseReference advupdate = mDatabasemember.child("advances");
                                                    advupdate.child("temp").child("tempadv").setValue(String.valueOf(tempadvance));

                                                    advupdate.child("currentadvance").setValue(updatedadvance);

                                                    DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("transactions").child(meeetid).child(temptr);
                                                    newgroupmeetpayment3.child("advcf").setValue(updatedadvance);

                                                    if (newadvance == 0) {
                                                        DatabaseReference rm = mDatabasemember.child("advances");
                                                        rm.child("currpenalty").removeValue();
                                                    }

                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepayment);


                                                } else if (opts == 0) {
                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepayment);

                                                    mDatabasemember.child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                            DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("transactions").child(meeetid).child(temptr);
                                                            newgroupmeetpayment3.child("advcf").setValue(oldadvance);
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    DatabaseReference inc1ad=accounting.child("Income").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    inc1ad.child("name").setValue("Income");
                                                    inc1ad.child("amount").setValue(advancepayment);
                                                    inc1ad.child("type").setValue("blue");
                                                    inc1ad.child("meet").setValue(meeetid);
                                                    inc1ad.child("group").setValue(grpid);
                                                    inc1ad.child("debitac").setValue("Income");
                                                    inc1ad.child("creditac").setValue("-");
                                                    inc1ad.child("description").setValue("Advance Interest paid by "+nname);
                                                    inc1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference inc2ad=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    inc2ad.child("name").setValue("Income");
                                                    inc2ad.child("amount").setValue(advancepayment);
                                                    inc2ad.child("type").setValue("blue");
                                                    inc2ad.child("meet").setValue(meeetid);
                                                    inc1ad.child("group").setValue(grpid);
                                                    inc2ad.child("debitac").setValue("Income");
                                                    inc2ad.child("creditac").setValue("-");
                                                    inc2ad.child("description").setValue("Advance Interest paid by "+nname);
                                                    inc2ad.child("timestamp").setValue(ServerValue.TIMESTAMP);


                                                } else {
                                                    String oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                    double oldadvances = Double.parseDouble(oldadvance);
                                                    newadvancepayment = advpy;
                                                    double newadvance = oldadvances - newadvancepayment;
                                                    final String updatedadvance = String.valueOf(newadvance);
                                                    DatabaseReference advupdate = mDatabasemember.child("advances");
                                                    advupdate.child("currentadvance").setValue(String.valueOf(advncecf));
                                                    advupdate.child("currpenalty").setValue(String.valueOf(intr2));
                                                    DatabaseReference advudate = mDatabasemember.child("advances");
                                                    advudate.child("temp").child("tempadv").setValue(String.valueOf(advpy));

                                                    DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("transactions").child(meeetid).child(temptr);
                                                    newgroupmeetpayment3.child("advcf").setValue(String.valueOf(advncecf));

                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepaymen);


                                                    if (!fines.equals("0")){
                                                        DatabaseReference fees1f=accounting.child("Feesfines").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                        fees1f.child("name").setValue("Fees and Fines");
                                                        fees1f.child("amount").setValue(fines);
                                                        fees1f.child("type").setValue("blue");
                                                        fees1f.child("meet").setValue(meeetid);
                                                        fees1f.child("group").setValue(grpid);
                                                        fees1f.child("debitac").setValue("BankCash");
                                                        fees1f.child("creditac").setValue("-");
                                                        fees1f.child("description").setValue("Fines paid for "+nname);
                                                        fees1f.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                        DatabaseReference fees2f=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                        fees2f.child("name").setValue("Fees and Fines");
                                                        fees2f.child("amount").setValue(fines);
                                                        fees2f.child("type").setValue("blue");
                                                        fees2f.child("meet").setValue(meeetid);
                                                        fees2f.child("group").setValue(grpid);
                                                        fees2f.child("debitac").setValue("Feesfines");
                                                        fees2f.child("creditac").setValue("-");
                                                        fees2f.child("description").setValue("Fines paid for"+nname);
                                                        fees2f.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                    }

                                                }

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





                Toast.makeText(payment.this, "Payment Processed!", Toast.LENGTH_LONG).show();

                Intent home = new Intent(payment.this, memberlist.class);
                home.putExtra("key", grpid);
                startActivity(home);
                finish();
            } else {
                Toast.makeText(payment.this, "Enter All Values..", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.e("Exception",String.valueOf(e));
        }

    }

    private void confirm() {
        mD3.child(ngroup).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String tes1 = snapshot.child("Member").getValue().toString();
                    String tes2 = snapshot.child("Status").getValue().toString();
                    final String var1 = snapshot.child("id").getValue().toString();
                    if (tes1.equals(user)) {
                        if (tes2.equals("received")) {
                            final String am = snapshot.child("Amount").getValue().toString();
                            final String date = snapshot.child("Date").getValue().toString();
                            DatabaseReference newMpesa = mD3.child(grpid).child(var1);
                            newMpesa.child("Status").setValue("Processed");
                            genRCPT(am, user, date);
                            final Thread thread=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mD3.child(ngroup).keepSynced(true);
                                }
                            });
                            thread.start();
                        } else {

                            Toast.makeText(payment.this, "Payment Not Found!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void genRCPT(String amount, String memid, String date) {

        final DatabaseReference nr = mD4.child(grpid).child(meeetid).child("Mpesa").child(memid);
        DatabaseReference nnr = nr.child("details").push();
        //nnr.keepSynced(true);
        //nr.keepSynced(true);
        nnr.child("amount").setValue(amount);
        nr.child("date").setValue(date);
        nr.child("name").setValue(nname);
        mD2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                facname = dataSnapshot.child("name").getValue().toString();
                nr.child("officer").setValue(facname);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabasemember.child("Mpesa").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference rpm=mDatabasemember.child("Mpesa");
                rpm.child("amount").removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String setnextdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(Calendar.DATE, 30); //Adding30days

        return sdf.format(c.getTime());
    }
}