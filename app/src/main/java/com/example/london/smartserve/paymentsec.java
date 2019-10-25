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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class paymentsec extends AppCompatActivity {
    private ImageView mMemberphoto;
    private TextView mMembername, mGroupName, mTotalSavings, mCurrloan, mAdvnce, mInstdiplay, mWithdisp;
    private EditText mTotalrepaid, mLSF, mloanRep, mAdvnpay, mFines, mInsurance, mMpesa, mWithdraw,mOther;
    private DatabaseReference mDatabasemember,mD4,mD3,mD2,mD1,mD5, mDatabasetrans, mDatatabasegroup, mDatabase, master, mD,accounting;
    private FirebaseAuth mAuth;
    private LinearLayout mLayout, linearLayout, withlayout,choice,rchoice;
    private String user = null, op = null, ops = null;
    private Button mPaySubmit;
    private ProgressDialog mProgress;
    private String nname = null;
    private String grpid = null, paymode = null;
    private String intr2 = null;
    private int  opt = 5, opts = 2, newlsfs = 0;
    private String currentadvace, currentpenalty, line = null, ngroup = null;
    private double tototrep=0,newinstallment = 0,newlsf = 0, newadvancepayment = 0, advpy = 0, advncecf = 0, mpesapay = 0, tempadv = 0, tempsav,d1=0,d2=0,d3=0,d4=0;
    private String advancepaymen = null, details = null, facname = null,currentploan=null,totalpsavings=null;
    private String temptr,trstype,updatedsavings,updatedloans;

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
        //mDatabasetrans.keepSynced(true);
        mDatatabasegroup = FirebaseDatabase.getInstance().getReference().child("finances");
        //mDatatabasegroup.keepSynced(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("details");
        //mDatabase.keepSynced(true);
        mDatabasemember = FirebaseDatabase.getInstance().getReference().child("members").child(user);
        //mDatabasemember.keepSynced(true);
        mD4 = FirebaseDatabase.getInstance().getReference().child("Receipts");
        //mD4.keepSynced(true);
        mD3 = FirebaseDatabase.getInstance().getReference().child("Mpesa").child("Received");
        //mD3.keepSynced(true);
        mD2 = FirebaseDatabase.getInstance().getReference().child("Employees").child(ops);
        //mD2.keepSynced(true);
        mD1=FirebaseDatabase.getInstance().getReference().child("insurance").child("all");
        //mD1.keepSynced(true);
        mD5= FirebaseDatabase.getInstance().getReference().child("Accounts").child(ops);



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
                final AlertDialog.Builder builder = new AlertDialog.Builder(paymentsec.this);
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
                        Picasso.with(paymentsec.this).load(nimage).into(mMemberphoto);


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

        mDatabasemember.child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    final AlertDialog.Builder builders = new AlertDialog.Builder(paymentsec.this);
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
                    final AlertDialog.Builder builders = new AlertDialog.Builder(paymentsec.this);
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
                Toast.makeText(paymentsec.this,"Please make sure your values balance with the total repaid",Toast.LENGTH_LONG).show();
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
                Toast.makeText(paymentsec.this,"Please make sure your values balance with the total repaid",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void regpayment() {
        mDatabase.child(grpid).child("grouptransactions").child("sec").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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


                double foradv=totamount-loaninst;
                if (foradv>advce){
                    mAdvnpay.setHint("Kshs." + currentadvace);
                    double lsfamt = totamount - (loaninst + advce);
                    mLSF.setHint(String.valueOf(lsfamt));
                    if (lsfamt<0){
                        double f=(loaninst+advce)-Double.parseDouble(totalpsavings);
                        mLSF.setHint("Please Add "+String.valueOf(f));
                    }
                }else {
                    mAdvnpay.setHint("Kshs." + currentpenalty);
                    double lsfamt = totamount - (loaninst + advp);
                    mLSF.setHint(String.valueOf(lsfamt));
                    if (lsfamt<0){
                        double f=(loaninst+advce)-Double.parseDouble(totalpsavings);
                        mLSF.setHint("Please Add "+String.valueOf(f));
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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            try {
                double totamount = Double.parseDouble(mTotalrepaid.getText().toString());
                double loaninst = Double.parseDouble(op);
                double advce = Double.parseDouble(currentadvace);
                double advp=Double.parseDouble(currentpenalty);


                double foradv=totamount-loaninst;
                if (foradv>advce){
                    mAdvnpay.setHint("Kshs." + currentadvace);
                    double lsfamt = totamount - (loaninst + advce);
                    mLSF.setHint(String.valueOf(lsfamt));
                    if (lsfamt<0){
                        double f=(loaninst+advce)-Double.parseDouble(totalpsavings);
                        mLSF.setHint("Please Add "+String.valueOf(f));
                    }
                }else {
                    mAdvnpay.setHint("Kshs." + currentpenalty);
                    double lsfamt = totamount - (loaninst + advp);
                    mLSF.setHint(String.valueOf(lsfamt));
                    if (lsfamt<0){
                        double f=(loaninst+advce)-Double.parseDouble(totalpsavings);
                        mLSF.setHint("Please Add "+String.valueOf(f));
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
        final String with = mWithdraw.getText().toString();
        final String others=mOther.getText().toString();

        final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("memberstats").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpid);


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

                DatabaseReference newmemberpayment = mDatabasemember.child("transactions").push();
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
                    //newgrouppayment.child("totalamount").setValue(with);
                } else if (Double.parseDouble(with) == ((tempsav+Double.parseDouble(lsfamount))-200)) {

                    newgrouppayment.child("lsf").setValue("0");
                    newgrouppayment.child("wcomm").setValue("200");
                    //newgrouppayment.child("totalamount").setValue(with);
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

                                memstat.child(user).child("name").setValue(nname);
                                memstat.child(user).child("savings").setValue(updatedsavings);


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

                                DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child("sec").child("savings").child("c2b").push();
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

                                        }else {
                                            loansupdate.child("flag").setValue("1");
                                        }



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


                                        DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child("sec").child("loans").child("c2b").push();
                                        meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                        meettrans.child("membername").setValue(nname);
                                        meettrans.child("memberid").setValue(user);
                                        meettrans.child("installment").setValue(newinstallment);

                                        memstat.child(user).child("loans").setValue(updatedloans);
                                        memstat.child(user).child("riskfund").setValue("-");
                                        memstat.child(user).child("arreas").setValue("-");

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

                                                    memstat.child(user).child("advance").setValue(String.valueOf(advncecf));


                                                    if (newadvance == 0) {
                                                        DatabaseReference rm = mDatabasemember.child("advances");
                                                        rm.child("currpenalty").removeValue();
                                                    }

                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child("sec").child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepayment);




                                                } else if (opts == 0) {
                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child("sec").child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepayment);



                                                    DatabaseReference inc1ad=accounting.child("Income").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                                    inc1ad.child("name").setValue("Income");
                                                    inc1ad.child("amount").setValue(advancepayment);
                                                    inc1ad.child("type").setValue("blue");
                                                    inc1ad.child("meet").setValue("sec");
                                                    inc1ad.child("group").setValue(grpid);
                                                    inc1ad.child("debitac").setValue("Income");
                                                    inc1ad.child("creditac").setValue("-");
                                                    inc1ad.child("description").setValue("Advance Interest paid by "+nname);
                                                    inc1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference inc2ad=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                                    inc2ad.child("name").setValue("Income");
                                                    inc2ad.child("amount").setValue(advancepayment);
                                                    inc2ad.child("type").setValue("blue");
                                                    inc2ad.child("meet").setValue("sec");
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
                                                    advudate.child("temp").child("tempadv").setValue(mAdvnpay.getText().toString());

                                                    DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("transactions").child("sec").child(temptr);
                                                    newgroupmeetpayment3.child("advcf").setValue(String.valueOf(advncecf));

                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child("sec").child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepaymen);

                                                    memstat.child(user).child("advance").setValue(String.valueOf(advncecf));


                                                    if (!fines.equals("0")){
                                                        DatabaseReference fees1f=accounting.child("Feesfines").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                                        fees1f.child("name").setValue("Fees and Fines");
                                                        fees1f.child("amount").setValue(fines);
                                                        fees1f.child("type").setValue("blue");
                                                        fees1f.child("meet").setValue("sec");
                                                        fees1f.child("group").setValue(grpid);
                                                        fees1f.child("debitac").setValue("BankCash");
                                                        fees1f.child("creditac").setValue("-");
                                                        fees1f.child("description").setValue("Fines paid for "+nname);
                                                        fees1f.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                        DatabaseReference fees2f=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                                        fees2f.child("name").setValue("Fees and Fines");
                                                        fees2f.child("amount").setValue(fines);
                                                        fees2f.child("type").setValue("blue");
                                                        fees2f.child("meet").setValue("sec");
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

                mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("Balance").exists()){
                            String balb4=dataSnapshot.child("Balance").getValue().toString();
                            double balb44=Double.parseDouble(balb4);
                            double e=balb44+Double.parseDouble(totalrepaidamount);
                            DatabaseReference newdepo=mD5;
                            newdepo.child("Balance").setValue(String.valueOf(e));
                        }else {
                            String balb4="0";
                            double balb44=Double.parseDouble(balb4);
                            double e=balb44+Double.parseDouble(totalrepaidamount);
                            DatabaseReference newdepo=mD5;
                            newdepo.child("Balance").setValue(String.valueOf(e));
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("secretaryhandovers").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(ops);
                ref.child("groupname").setValue(nname+" office payment");
                ref.child("amountout").setValue("0");
                ref.child("status").setValue("received");
                ref.child("amountbanked").setValue(totalrepaidamount);
                ref.child("user").setValue(ops);


                Toast.makeText(paymentsec.this, "Payment Processed!", Toast.LENGTH_LONG).show();

                Intent home = new Intent(paymentsec.this, memberlist.class);
                home.putExtra("key", grpid);
                startActivity(home);
                finish();
            } else {
                Toast.makeText(paymentsec.this, "Enter All Values..", Toast.LENGTH_LONG).show();
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
                    tempadv = Double.parseDouble("0");;
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

                        DatabaseReference risk=accounting.child("Riskfund").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                        risk.child("name").setValue("Risk Fund");
                        risk.child("amount").setValue(insu);
                        risk.child("type").setValue("blue");
                        risk.child("meet").setValue("sec");
                        risk.child("group").setValue(grpid);
                        risk.child("debitac").setValue("Riskfund");
                        risk.child("creditac").setValue("-");
                        risk.child("description").setValue("Risk fund paid by "+nname);
                        risk.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference risk2=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                        risk2.child("name").setValue("Risk Fund");
                        risk2.child("amount").setValue(insu);
                        risk2.child("type").setValue("blue");
                        risk2.child("meet").setValue("sec");
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
                                savingssupdate.child("details").setValue("Contrinution");
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

                                DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child("sec").child("savings").child("c2b").push();
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

                                DatabaseReference membersfund=accounting.child("Membersfund").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                membersfund.child("name").setValue("LSF");
                                membersfund.child("amount").setValue(lsfamount);
                                membersfund.child("type").setValue("red");
                                membersfund.child("meet").setValue("sec");
                                membersfund.child("group").setValue(grpid);
                                membersfund.child("debitac").setValue("BankCash");
                                membersfund.child("creditac").setValue("Membersfund");
                                membersfund.child("description").setValue("LSF for "+nname);
                                membersfund.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                DatabaseReference bank=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                bank.child("name").setValue("LSF");
                                bank.child("amount").setValue(lsfamount);
                                bank.child("type").setValue("blue");
                                bank.child("meet").setValue("sec");
                                bank.child("group").setValue(grpid);
                                bank.child("debitac").setValue("BankCash");
                                bank.child("creditac").setValue("Membersfund");
                                bank.child("description").setValue("LSF for "+nname);
                                bank.child("timestamp").setValue(ServerValue.TIMESTAMP);



                                DatabaseReference newgroupmeetpayment = mDatabase.child(grpid).child("transactions").child("sec").push();
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



                                        DatabaseReference newgroupmeetpayment1 = mDatabase.child(grpid).child("transactions").child("sec").child(temptr);
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


                                        DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child("sec").child("loans").child("c2b").push();
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

                                                    DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("transactions").child("sec").child(temptr);
                                                    newgroupmeetpayment3.child("advcf").setValue(updatedadvance);

                                                    if (newadvance == 0) {
                                                        DatabaseReference rm = mDatabasemember.child("advances");
                                                        rm.child("currpenalty").removeValue();
                                                    }

                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child("sec").child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepayment);


                                                } else if (opts == 0) {
                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child("sec").child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepayment);

                                                    mDatabasemember.child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                            DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("transactions").child("sec").child(temptr);
                                                            newgroupmeetpayment3.child("advcf").setValue(oldadvance);
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    DatabaseReference inc1ad=accounting.child("Income").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                                    inc1ad.child("name").setValue("Income");
                                                    inc1ad.child("amount").setValue(advancepayment);
                                                    inc1ad.child("type").setValue("blue");
                                                    inc1ad.child("meet").setValue("sec");
                                                    inc1ad.child("group").setValue(grpid);
                                                    inc1ad.child("debitac").setValue("Income");
                                                    inc1ad.child("creditac").setValue("-");
                                                    inc1ad.child("description").setValue("Advance Interest paid by "+nname);
                                                    inc1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference inc2ad=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                                    inc2ad.child("name").setValue("Income");
                                                    inc2ad.child("amount").setValue(advancepayment);
                                                    inc2ad.child("type").setValue("blue");
                                                    inc2ad.child("meet").setValue("sec");
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



                                                    DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("transactions").child("sec").child(temptr);
                                                    newgroupmeetpayment3.child("advcf").setValue(String.valueOf(advncecf));

                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("meetings").child("sec").child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepaymen);


                                                    if (!fines.equals("0")){
                                                        DatabaseReference fees1f=accounting.child("Feesfines").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                                        fees1f.child("name").setValue("Fees and Fines");
                                                        fees1f.child("amount").setValue(fines);
                                                        fees1f.child("type").setValue("blue");
                                                        fees1f.child("meet").setValue("sec");
                                                        fees1f.child("group").setValue(grpid);
                                                        fees1f.child("debitac").setValue("BankCash");
                                                        fees1f.child("creditac").setValue("-");
                                                        fees1f.child("description").setValue("Fines paid for "+nname);
                                                        fees1f.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                        DatabaseReference fees2f=accounting.child("BankCash").child(grpid).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                                        fees2f.child("name").setValue("Fees and Fines");
                                                        fees2f.child("amount").setValue(fines);
                                                        fees2f.child("type").setValue("blue");
                                                        fees2f.child("meet").setValue("sec");
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

                final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("secretaryhandovers").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(ops);
                ref.child("groupname").setValue(nname+" office payment");
                ref.child("amountout").setValue("0");
                ref.child("status").setValue("received");
                ref.child("amountbanked").setValue(totalrepaidamount);
                ref.child("user").setValue(ops);

                DatabaseReference  ref2=mDatabase.child(grpid).child("meetings").child("sec").child("attendancelist").push();
                ref2.child("id").setValue(user);
                ref2.child("name").setValue(nname);


                Toast.makeText(paymentsec.this, "Payment Processed!", Toast.LENGTH_LONG).show();

                Intent home = new Intent(paymentsec.this, memberlist.class);
                home.putExtra("key", grpid);
                startActivity(home);
                finish();
            } else {
                Toast.makeText(paymentsec.this, "Enter All Values..", Toast.LENGTH_LONG).show();
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

                            Toast.makeText(paymentsec.this, "Payment Not Found!", Toast.LENGTH_LONG).show();
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

        final DatabaseReference nr = mD4.child(grpid).child("sec").child("Mpesa").child(memid);
        DatabaseReference nnr = nr.child("details").push();
        nnr.keepSynced(true);
        nr.keepSynced(true);
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