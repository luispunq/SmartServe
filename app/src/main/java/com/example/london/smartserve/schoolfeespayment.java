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

public class schoolfeespayment extends AppCompatActivity {
    private ImageView mMemberphoto;
    private TextView mMembername, mGroupName, mTotalSavings, mCurrloan, mAdvnce, mInstdiplay, mWithdisp;
    private EditText mTotalrepaid, mLSF, mloanRep, mAdvnpay, mFines, mInsurance, mMpesa, mWithdraw,mOther;
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
    private String currentadvace, currentpenalty, line = null, ngroup = null,temptr;
    private double tototrep=0,tempsavings=0,newinstallment = 0,oldsaving=0,newlsf = 0, newadvancepayment = 0, advpy = 0, advncecf = 0, mpesapay = 0, tempadv = 0, tempsav,d1=0,d2=0,d3=0,d4=0;
    private String advancepaymen = null,oldadvance=null,oldgrpavnce=null, details = null, facname = null,currentploan=null,totalpsavings=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoolfeespayment);
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
        choice=findViewById(R.id.alertt2f);
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
        mDatabasetrans = FirebaseDatabase.getInstance().getReference().child("schoolfeestransactions");
        mDatatabasegroup = FirebaseDatabase.getInstance().getReference().child("finances");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("details");
        mDatabasemember = FirebaseDatabase.getInstance().getReference().child("members").child(user);
        master = FirebaseDatabase.getInstance().getReference().child("masterfinance").child("schoolfees");
        mD = FirebaseDatabase.getInstance().getReference();


        mD.child("members").child(user).child("Mpesa").addListenerForSingleValueEvent(new ValueEventListener() {
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(schoolfeespayment.this);
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
        mDatabasemember.child("details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nname = (String) dataSnapshot.child("name").getValue();
                String nimage = (String) dataSnapshot.child("profileImage").getValue();
                ngroup = (String) dataSnapshot.child("group").getValue();
                grpid = (String) dataSnapshot.child("groupid").getValue();
                mMembername.setText(nname);
                mGroupName.setText(ngroup);
                Picasso.with(schoolfeespayment.this).load(nimage).into(mMemberphoto);

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

        mDatabasemember.child("savings").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("totalsavings").exists()) {
                    totalpsavings = (String) dataSnapshot.child("totalsavings").getValue();
                    mTotalSavings.setText("Kshs." + totalpsavings);
                    tempsav = Double.parseDouble(totalpsavings);
                } else {
                    mTotalSavings.setText("-");
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

        mDatabasemember.child("advances").child("schoolfees").addValueEventListener(new ValueEventListener() {
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


        mPaySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final AlertDialog.Builder builders = new AlertDialog.Builder(schoolfeespayment.this);
                builders.setTitle("Select Options")
                        .setCancelable(true)
                        .setPositiveButton("Part MPesa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                paymode = "Part MPesa";
                                dialog.dismiss();
                                sec();
                            }
                        })
                        .setNegativeButton("All Cash", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                paymode = "All Cash";
                                dialog.dismiss();
                                sec();
                            }
                        });
                AlertDialog alert121 = builders.create();
                alert121.show();*/

                if (mMpesa.getText().toString().equals("0")){
                    final AlertDialog.Builder builders = new AlertDialog.Builder(schoolfeespayment.this);
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
                    final AlertDialog.Builder builders = new AlertDialog.Builder(schoolfeespayment.this);
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
        double secitem4= Double.parseDouble(mAdvnpay.getText().toString());

        if (Double.parseDouble(mWithdraw.getText().toString())>1||Double.parseDouble(mWithdraw.getText().toString())==0){
            if (secitem1==(secitem2+secitem4)){
                if (mTotalSavings.getText().toString().equals("-")) {
                    processpaymentf();
                }else {
                    processpayment();
                }
            }else{
                Toast.makeText(schoolfeespayment.this,"Please make sure your values balance with the total repaid",Toast.LENGTH_LONG).show();
            }
        }

    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //DOTHECALCULATIONSHEREANDSHOWTHERESULTASPERYOURCALCULATIONS
            try {
                double totamount = Double.parseDouble(mTotalrepaid.getText().toString());
                double advce = Double.parseDouble(currentadvace);
                double advp=Double.parseDouble(currentpenalty);


                double foradv=totamount;
                if (foradv>advce){
                    mAdvnpay.setHint("Kshs." + currentadvace);
                    double lsfamt = totamount - (advce);
                    mLSF.setHint(String.valueOf(lsfamt));
                }else {
                    mAdvnpay.setHint("Kshs." + currentpenalty);
                    double lsfamt = totamount - ( advp);
                    mLSF.setHint(String.valueOf(lsfamt));
                }


            } catch (Exception e) {
            }

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void processpayment() {
        final String totalrepaidamount = mTotalrepaid.getText().toString();
        final String lsfamount = mLSF.getText().toString();
        final String advancepayment = mAdvnpay.getText().toString();
        final String mpesa = mMpesa.getText().toString();
        final String with = mWithdraw.getText().toString();



        try {
            if (!TextUtils.isEmpty(advancepayment)) {
                if (opts == 2) {
                    intr2 = "0";
                    advancepaymen = advancepayment;
                    tempadv = Double.parseDouble(advancepaymen);
                } else if (opts == 0) {
                    intr2 = advancepayment;
                    advancepaymen = "0";
                    tempadv = Double.parseDouble("0");
                } else {
                    intr2 = String.valueOf(11 + (0.1 * (Double.parseDouble(currentadvace) - Double.parseDouble(advancepayment))));
                    advncecf = ((Double.parseDouble(currentadvace) - Double.parseDouble(advancepayment)) + Double.parseDouble(intr2)) + 11;
                    advpy = (Double.parseDouble(currentadvace) - advncecf) + 11;
                    advancepaymen = String.valueOf(advpy);
                    tempadv = advpy;
                }


                DatabaseReference newmemberpayment = mDatabasemember.child("schoolfees").child("transactions").push();
                newmemberpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                newmemberpayment.child("totalamount").setValue(totalrepaidamount);

                if (Double.parseDouble(with) > 1&&Double.parseDouble(with)<tempsav) {
                    details = "Withdraw";
                    newmemberpayment.child("lsf").setValue("0");
                    newmemberpayment.child("wcomm").setValue("100");
                    //newmemberpayment.child("totalamount").setValue(with);
                } else if (Double.parseDouble(with) == tempsav) {
                    details = "Withdraw";
                    newmemberpayment.child("lsf").setValue("0");
                    newmemberpayment.child("wcomm").setValue("200");
                    //newmemberpayment.child("totalamount").setValue(with);
                } else {
                    details = "Contribution";
                    newmemberpayment.child("lsf").setValue(lsfamount);
                    newmemberpayment.child("wcomm").setValue("0");
                }

                newmemberpayment.child("advancepayment").setValue(advancepaymen);
                newmemberpayment.child("advanceinterest").setValue(intr2);
                newmemberpayment.child("paymentmode").setValue(paymode);
                newmemberpayment.child("memberid").setValue(user);
                newmemberpayment.child("Mpesa").setValue(mpesa);

                DatabaseReference newgrouppayment = mDatabasetrans.child(grpid).push();
                newgrouppayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                newgrouppayment.child("totalamount").setValue(totalrepaidamount);
                if (Double.parseDouble(with) > 1&&Double.parseDouble(with)<tempsav) {

                    newgrouppayment.child("lsf").setValue("0");
                    newgrouppayment.child("wcomm").setValue("100");
                    //newgrouppayment.child("totalamount").setValue(with);
                } else if (Double.parseDouble(with) == tempsav) {

                    newgrouppayment.child("lsf").setValue("0");
                    newgrouppayment.child("wcomm").setValue("200");
                    //newgrouppayment.child("totalamount").setValue(with);
                } else {

                    newgrouppayment.child("lsf").setValue(lsfamount);
                    newgrouppayment.child("wcomm").setValue("0");
                }
                newgrouppayment.child("advancepayment").setValue(advancepaymen);
                newgrouppayment.child("advanceinterest").setValue(intr2);
                newgrouppayment.child("paymentmode").setValue(paymode);
                newgrouppayment.child("memberid").setValue(user);
                newgrouppayment.child("Mpesa").setValue(mpesa);

                mDatabasemember.child("savings").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String updatedsavings;
                        if (dataSnapshot.child("totalsavings").exists()){
                            String oldsavings = (String) dataSnapshot.child("totalsavings").getValue();
                            oldsaving = Double.parseDouble(oldsavings);
                            tempsavings = Double.parseDouble(lsfamount);
                        }else {
                            String oldsavings = "0";
                            oldsaving = Double.parseDouble(oldsavings);
                            tempsavings = Double.parseDouble(lsfamount);
                        }


                        if (Double.parseDouble(with) > 1&&Double.parseDouble(with)<tempsav) {
                            newlsf = Double.parseDouble(with);
                            double newsaving = oldsaving - newlsf;
                            updatedsavings = String.valueOf(newsaving);
                        } else if (Double.parseDouble(with) == oldsaving) {
                            newlsf = Double.parseDouble(with);
                            double newsaving = oldsaving - newlsf;
                            updatedsavings = String.valueOf(newsaving);
                        } else {
                            newlsf = Double.parseDouble(lsfamount);
                            double newsaving = oldsaving + newlsf;
                            updatedsavings = String.valueOf(newsaving);
                        }


                        DatabaseReference savingsupdate = mDatabasemember.child("savings").child("schoolfees");
                        savingsupdate.child("totalsavings").setValue(updatedsavings);
                        savingsupdate.child("temp").child("tempsavings").setValue(String.valueOf(tempsavings));

                        final DatabaseReference savingssupdate = mDatabasemember.child("savings").child("schoolfees").child("savings").push();
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
                        mD.child("Employees").child(ops).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                facname = dataSnapshot.child("name").getValue().toString();
                                savingssupdate.child("fac").setValue(facname);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        final DatabaseReference updategrpfin = mDatatabasegroup.child(grpid).child("savings").child("schoolfees");
                        mDatatabasegroup.child(grpid).child("savings").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                double newsavings=0;
                                String oldgrpsaving = (String) dataSnapshot.child("totalsavings").getValue();
                                double oldgrpsavins = Double.parseDouble(oldgrpsaving);
                                if (details.equals("Contribution")){
                                    newsavings = oldgrpsavins + Double.parseDouble(lsfamount);
                                }else{
                                    newsavings = oldgrpsavins - Double.parseDouble(with);
                                }

                                String updatedgrpsaving = String.valueOf(newsavings);
                                updategrpfin.child("totalsavings").setValue(updatedgrpsaving);


                                DatabaseReference meettrans = mDatabase.child(grpid).child("schoolfees").child("meetings").child(meeetid).child("savings").child("c2b").push();
                                meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                meettrans.child("membername").setValue(nname);
                                meettrans.child("memberid").setValue(user);
                                meettrans.child("lsf").setValue(newlsf);

                                mDatabasemember.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("sftemptrs").child("trid").exists()){
                                            final String id=dataSnapshot.child("sftemptrs").child("trid").getValue().toString();

                                            mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    double oldtrepaid=Double.parseDouble(dataSnapshot.child("totalamount").getValue().toString());
                                                    double oldlsf=Double.parseDouble(dataSnapshot.child("lsf").getValue().toString());
                                                    double oldlsfcf=Double.parseDouble(dataSnapshot.child("lsfcf").getValue().toString());
                                                    double oldadvance=Double.parseDouble(dataSnapshot.child("advancepayment").getValue().toString());

                                                    double newtrepaid=oldtrepaid+Double.parseDouble(totalrepaidamount);
                                                    double newlsf=oldlsf+Double.parseDouble(lsfamount);
                                                    double newlsfcf=oldlsfcf+Double.parseDouble(lsfamount);
                                                    double newadvance=oldadvance+Double.parseDouble(advancepaymen);

                                                    DatabaseReference newgroupmeetpayment = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).child(id);
                                                    newgroupmeetpayment.child("totalamount").setValue(String.valueOf(newtrepaid));
                                                    newgroupmeetpayment.child("lsf").setValue(String.valueOf(newlsf));
                                                    newgroupmeetpayment.child("lsfcf").setValue(String.valueOf(newlsfcf));
                                                    newgroupmeetpayment.child("advancepayment").setValue(String.valueOf(newadvance));

                                                    DatabaseReference f = mD.child("members").child(user).child("sftemptrs");
                                                    f.child("trid").setValue(id);

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }else {

                                            DatabaseReference newgroupmeetpayment = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).push();
                                            temptr = newgroupmeetpayment.getKey();
                                            newgroupmeetpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                            newgroupmeetpayment.child("totalamount").setValue(totalrepaidamount);
                                            newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                            if (Double.parseDouble(with) > 1&&Double.parseDouble(with)<tempsav) {
                                                newgroupmeetpayment.child("lsf").setValue("0");
                                                newgroupmeetpayment.child("wcomm").setValue("100");
                                                //newgroupmeetpayment.child("totalamount").setValue(with);
                                            } else if (Double.parseDouble(with) == tempsav) {
                                                newgroupmeetpayment.child("lsf").setValue("0");
                                                newgroupmeetpayment.child("wcomm").setValue("200");
                                                //newgroupmeetpayment.child("totalamount").setValue(with);
                                            } else {
                                                newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                                newgroupmeetpayment.child("wcomm").setValue("0");
                                            }

                                            newgroupmeetpayment.child("advancepayment").setValue(advancepaymen);
                                            newgroupmeetpayment.child("lsfcf").setValue(updatedsavings);
                                            newgroupmeetpayment.child("advanceinterest").setValue(intr2);
                                            newgroupmeetpayment.child("paymentmode").setValue(paymode);
                                            newgroupmeetpayment.child("memberid").setValue(nname);
                                            newgroupmeetpayment.child("Mpesa").setValue(mpesa);
                                            newgroupmeetpayment.child("id").setValue(user);
                                            newgroupmeetpayment.child("advgvn").setValue("0");
                                            newgroupmeetpayment.child("advcf").setValue("0");


                                            DatabaseReference f = mD.child("members").child(user).child("sftemptrs");
                                            f.child("trid").setValue(temptr);

                                            mDatabasemember.child("advances").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    if (opts == 2) {

                                                        if (dataSnapshot.child("currentadvance").exists()){
                                                            oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                        }else {
                                                            oldadvance = "0";
                                                        }

                                                        double oldadvances = Double.parseDouble(oldadvance);
                                                        newadvancepayment = Double.parseDouble(advancepayment);
                                                        double newadvance = oldadvances - newadvancepayment;
                                                        double tempadvance = tempadv;
                                                        final String updatedadvance = String.valueOf(newadvance);
                                                        DatabaseReference advupdate = mDatabasemember.child("advances").child("schoolfees");
                                                        advupdate.child("temp").child("tempadv").setValue(String.valueOf(tempadvance));
                                                        advupdate.child("currentadvance").setValue(updatedadvance);


                                                        mDatabasemember.child("sftemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("trid").exists()){

                                                                    String id=dataSnapshot.child("trid").getValue().toString();

                                                                    DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).child(id);
                                                                    newgroupmeetpayment3.child("advcf").setValue(updatedadvance);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });



                                                        if (newadvance == 0) {
                                                            DatabaseReference rm = mDatabasemember.child("advances");
                                                            rm.child("currpenalty").removeValue();
                                                        }


                                                        final DatabaseReference updategrpfinc = mDatatabasegroup.child(grpid).child("advances").child("schoolfees");

                                                        mDatatabasegroup.child(grpid).child("schoolfees").child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("currentadvance").exists()){
                                                                    oldgrpavnce = (String) dataSnapshot.child("currentadvance").getValue();
                                                                }else {
                                                                    oldgrpavnce = "0";
                                                                }

                                                                double oldgrpadv = Double.parseDouble(oldgrpavnce);
                                                                double newadvance = oldgrpadv - newadvancepayment;
                                                                String updatedgrpadv = String.valueOf(newadvance);
                                                                updategrpfinc.child("currentadvance").setValue(updatedgrpadv);

                                                                DatabaseReference meettrans = mDatabase.child(grpid).child("schoolfees").child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                                meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                meettrans.child("membername").setValue(nname);
                                                                meettrans.child("memberid").setValue(user);
                                                                meettrans.child("advancepaid").setValue(advancepayment);

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    } else if (opts == 0) {
                                                        DatabaseReference meettrans = mDatabase.child(grpid).child("schoolfees").child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                        meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                        meettrans.child("membername").setValue(nname);
                                                        meettrans.child("memberid").setValue(user);
                                                        meettrans.child("advancepaid").setValue(advancepayment);

                                                        mDatabasemember.child("sftemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("trid").exists()){
                                                                    final String id=dataSnapshot.child("trid").getValue().toString();
                                                                    mDatabasemember.child("advances").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.child("currentadvance").exists()){
                                                                                String oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                                                DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).child(id);
                                                                                newgroupmeetpayment3.child("advcf").setValue(oldadvance);
                                                                            }else {
                                                                                String oldadvance = "0";
                                                                                DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).child(id);
                                                                                newgroupmeetpayment3.child("advcf").setValue(oldadvance);
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



                                                    } else {
                                                        String oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                        double oldadvances = Double.parseDouble(oldadvance);
                                                        newadvancepayment = advpy;
                                                        double newadvance = oldadvances - newadvancepayment;
                                                        final String updatedadvance = String.valueOf(newadvance);
                                                        DatabaseReference advupdate = mDatabasemember.child("advances").child("schoolfees");
                                                        advupdate.child("currentadvance").setValue(String.valueOf(advncecf));

                                                        mDatabasemember.child("sftemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("trid").exists()){
                                                                    String id=dataSnapshot.child("trid").getValue().toString();
                                                                    DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).child(id);
                                                                    newgroupmeetpayment3.child("advcf").setValue(String.valueOf(advncecf));
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });




                                                        final DatabaseReference updategrpfinc = mDatatabasegroup.child(grpid).child("advances").child("schoolfees");

                                                        mDatatabasegroup.child(grpid).child("advances").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("currentadvance").exists()) {
                                                                    String oldgrpavnce = (String) dataSnapshot.child("currentadvance").getValue();
                                                                    double oldgrpadv = Double.parseDouble(oldgrpavnce);
                                                                    double newadvance = oldgrpadv - newadvancepayment;
                                                                    String updatedgrpadv = String.valueOf(newadvance);
                                                                    updategrpfinc.child("currentadvance").setValue(updatedgrpadv);

                                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("schoolfees").child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                    meettrans.child("membername").setValue(nname);
                                                                    meettrans.child("memberid").setValue(user);
                                                                    meettrans.child("advancepaid").setValue(advancepaymen);
                                                                }else {
                                                                    String oldgrpavnce = "0";
                                                                    double oldgrpadv = Double.parseDouble(oldgrpavnce);
                                                                    double newadvance = oldgrpadv - newadvancepayment;
                                                                    String updatedgrpadv = String.valueOf(newadvance);
                                                                    updategrpfinc.child("currentadvance").setValue(updatedgrpadv);

                                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("schoolfees").child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                    meettrans.child("membername").setValue(nname);
                                                                    meettrans.child("memberid").setValue(user);
                                                                    meettrans.child("advancepaid").setValue(advancepaymen);
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

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                                if (paymode.equals("Part MPesa")) {
                                    confirm();
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


                Toast.makeText(schoolfeespayment.this, "Payment Processed!", Toast.LENGTH_LONG).show();

                Intent home = new Intent(schoolfeespayment.this, memberlist.class);
                home.putExtra("key", grpid);
                startActivity(home);
                finish();
            } else {
                Toast.makeText(schoolfeespayment.this, "Enter All Values..", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.e("Exception",String.valueOf(e));
        }

    }

    private void processpaymentf() {
        final String totalrepaidamount = mTotalrepaid.getText().toString();
        final String lsfamount = mLSF.getText().toString();
        final String advancepayment = mAdvnpay.getText().toString();
        final String mpesa = mMpesa.getText().toString();
        final String with = mWithdraw.getText().toString();



        try {
            if (!TextUtils.isEmpty(advancepayment)) {
                if (opts == 2) {
                    intr2 = "0";
                    advancepaymen = advancepayment;
                    tempadv = Double.parseDouble(advancepaymen);
                } else if (opts == 0) {
                    intr2 = advancepayment;
                    advancepaymen = "0";
                    tempadv = Double.parseDouble("0");
                } else {
                    intr2 = String.valueOf(11 + (0.1 * (Double.parseDouble(currentadvace) - Double.parseDouble(advancepayment))));
                    advncecf = ((Double.parseDouble(currentadvace) - Double.parseDouble(advancepayment)) + Double.parseDouble(intr2)) + 11;
                    advpy = (Double.parseDouble(currentadvace) - advncecf) + 11;
                    advancepaymen = String.valueOf(advpy);
                    tempadv = advpy;
                }


                DatabaseReference newmemberpayment = mDatabasemember.child("schoolfees").child("transactions").push();
                newmemberpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                newmemberpayment.child("totalamount").setValue(totalrepaidamount);
                newmemberpayment.child("lsf").setValue(lsfamount);
                newmemberpayment.child("wcomm").setValue("0");


                newmemberpayment.child("advancepayment").setValue(advancepaymen);
                newmemberpayment.child("advanceinterest").setValue(intr2);
                newmemberpayment.child("paymentmode").setValue(paymode);
                newmemberpayment.child("memberid").setValue(user);
                newmemberpayment.child("Mpesa").setValue(mpesa);

                DatabaseReference newgrouppayment = mDatabasetrans.child(grpid).push();
                newgrouppayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                newgrouppayment.child("totalamount").setValue(totalrepaidamount);
                newgrouppayment.child("lsf").setValue(lsfamount);
                newgrouppayment.child("wcomm").setValue("0");
                newgrouppayment.child("advancepayment").setValue(advancepaymen);
                newgrouppayment.child("advanceinterest").setValue(intr2);
                newgrouppayment.child("paymentmode").setValue(paymode);
                newgrouppayment.child("memberid").setValue(user);
                newgrouppayment.child("Mpesa").setValue(mpesa);

                mDatabasemember.child("savings").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String updatedsavings;
                        if (dataSnapshot.child("totalsavings").exists()){
                            String oldsavings = (String) dataSnapshot.child("totalsavings").getValue();
                            oldsaving = Double.parseDouble(oldsavings);
                            tempsavings = Double.parseDouble(lsfamount);
                        }else {
                            String oldsavings = "0";
                            oldsaving = Double.parseDouble(oldsavings);
                            tempsavings = Double.parseDouble(lsfamount);
                        }

                        newlsf = Double.parseDouble(lsfamount);
                        double newsaving = oldsaving + newlsf;
                        updatedsavings = String.valueOf(newsaving);


                        DatabaseReference savingsupdate = mDatabasemember.child("savings").child("schoolfees");
                        savingsupdate.child("totalsavings").setValue(updatedsavings);
                        savingsupdate.child("temp").child("tempsavings").setValue(String.valueOf(tempsavings));

                        final DatabaseReference savingssupdate = mDatabasemember.child("savings").child("schoolfees").child("savings").push();
                        savingsupdate.child("temp").child("tempssid").setValue(savingssupdate.getKey());
                        savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        savingssupdate.child("details").setValue(details);
                        savingssupdate.child("save").setValue(String.valueOf(newlsf));
                        savingssupdate.child("with").setValue("0");
                        savingssupdate.child("bal").setValue(updatedsavings);
                        mD.child("Employees").child(ops).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                facname = dataSnapshot.child("name").getValue().toString();
                                savingssupdate.child("fac").setValue(facname);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        final DatabaseReference updategrpfin = mDatatabasegroup.child(grpid).child("savings").child("schoolfees");
                        mDatatabasegroup.child(grpid).child("savings").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                double newsavings=0;
                                String oldgrpsaving = (String) dataSnapshot.child("totalsavings").getValue();
                                double oldgrpsavins = Double.parseDouble(oldgrpsaving);
                                newsavings = oldgrpsavins + Double.parseDouble(lsfamount);

                                String updatedgrpsaving = String.valueOf(newsavings);
                                updategrpfin.child("totalsavings").setValue(updatedgrpsaving);


                                DatabaseReference meettrans = mDatabase.child(grpid).child("schoolfees").child("meetings").child(meeetid).child("savings").child("c2b").push();
                                meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                meettrans.child("membername").setValue(nname);
                                meettrans.child("memberid").setValue(user);
                                meettrans.child("lsf").setValue(newlsf);

                                DatabaseReference newgroupmeetpayment = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).push();
                                final String temptr = newgroupmeetpayment.getKey();
                                newgroupmeetpayment.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                newgroupmeetpayment.child("totalamount").setValue(totalrepaidamount);
                                newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                newgroupmeetpayment.child("lsf").setValue(lsfamount);
                                newgroupmeetpayment.child("wcomm").setValue("0");
                                newgroupmeetpayment.child("advancepayment").setValue(advancepaymen);
                                newgroupmeetpayment.child("lsfcf").setValue(updatedsavings);
                                newgroupmeetpayment.child("advanceinterest").setValue(intr2);
                                newgroupmeetpayment.child("paymentmode").setValue(paymode);
                                newgroupmeetpayment.child("memberid").setValue(nname);
                                newgroupmeetpayment.child("Mpesa").setValue(mpesa);
                                newgroupmeetpayment.child("id").setValue(user);
                                newgroupmeetpayment.child("advgvn").setValue("0");
                                newgroupmeetpayment.child("advcf").setValue("0");

                                if (paymode.equals("Part MPesa")) {
                                    confirm();
                                }

                                DatabaseReference f = mD.child("members").child(user).child("temptrs");
                                f.child("trid").setValue(temptr);

                                mDatabasemember.child("advances").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (opts == 2) {

                                            if (dataSnapshot.child("currentadvance").exists()){
                                                oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                            }else {
                                                oldadvance = "0";
                                            }

                                            double oldadvances = Double.parseDouble(oldadvance);
                                            newadvancepayment = Double.parseDouble(advancepayment);
                                            double newadvance = oldadvances - newadvancepayment;
                                            double tempadvance = tempadv;
                                            final String updatedadvance = String.valueOf(newadvance);
                                            DatabaseReference advupdate = mDatabasemember.child("advances").child("schoolfees");
                                            advupdate.child("temp").child("tempadv").setValue(String.valueOf(tempadvance));
                                            advupdate.child("currentadvance").setValue(updatedadvance);


                                            DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).child(temptr);
                                            newgroupmeetpayment3.child("advcf").setValue(updatedadvance);

                                            if (newadvance == 0) {
                                                DatabaseReference rm = mDatabasemember.child("advances");
                                                rm.child("currpenalty").removeValue();
                                            }


                                            final DatabaseReference updategrpfinc = mDatatabasegroup.child(grpid).child("advances").child("schoolfees");

                                            mDatatabasegroup.child(grpid).child("schoolfees").child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.child("currentadvance").exists()){
                                                        oldgrpavnce = (String) dataSnapshot.child("currentadvance").getValue();
                                                    }else {
                                                        oldgrpavnce = "0";
                                                    }

                                                    double oldgrpadv = Double.parseDouble(oldgrpavnce);
                                                    double newadvance = oldgrpadv - newadvancepayment;
                                                    String updatedgrpadv = String.valueOf(newadvance);
                                                    updategrpfinc.child("currentadvance").setValue(updatedgrpadv);

                                                    DatabaseReference meettrans = mDatabase.child(grpid).child("schoolfees").child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                    meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    meettrans.child("membername").setValue(nname);
                                                    meettrans.child("memberid").setValue(user);
                                                    meettrans.child("advancepaid").setValue(advancepayment);

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        } else if (opts == 0) {
                                            DatabaseReference meettrans = mDatabase.child(grpid).child("schoolfees").child("meetings").child(meeetid).child("advances").child("c2b").push();
                                            meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                            meettrans.child("membername").setValue(nname);
                                            meettrans.child("memberid").setValue(user);
                                            meettrans.child("advancepaid").setValue(advancepayment);

                                            mDatabasemember.child("advances").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.child("currentadvance").exists()){
                                                        String oldadvance = (String) dataSnapshot.child("currentadvance").getValue();
                                                        DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).child(temptr);
                                                        newgroupmeetpayment3.child("advcf").setValue(oldadvance);
                                                    }else {
                                                        String oldadvance = "0";
                                                        DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).child(temptr);
                                                        newgroupmeetpayment3.child("advcf").setValue(oldadvance);
                                                    }
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
                                            final String updatedadvance = String.valueOf(newadvance);
                                            DatabaseReference advupdate = mDatabasemember.child("advances").child("schoolfees");
                                            advupdate.child("currentadvance").setValue(String.valueOf(advncecf));

                                            DatabaseReference newgroupmeetpayment3 = mDatabase.child(grpid).child("schoolfees").child("transactions").child(meeetid).child(temptr);
                                            newgroupmeetpayment3.child("advcf").setValue(String.valueOf(advncecf));


                                            final DatabaseReference updategrpfinc = mDatatabasegroup.child(grpid).child("advances").child("schoolfees");

                                            mDatatabasegroup.child(grpid).child("advances").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.child("currentadvance").exists()) {
                                                        String oldgrpavnce = (String) dataSnapshot.child("currentadvance").getValue();
                                                        double oldgrpadv = Double.parseDouble(oldgrpavnce);
                                                        double newadvance = oldgrpadv - newadvancepayment;
                                                        String updatedgrpadv = String.valueOf(newadvance);
                                                        updategrpfinc.child("currentadvance").setValue(updatedgrpadv);

                                                        DatabaseReference meettrans = mDatabase.child(grpid).child("schoolfees").child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                        meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                        meettrans.child("membername").setValue(nname);
                                                        meettrans.child("memberid").setValue(user);
                                                        meettrans.child("advancepaid").setValue(advancepaymen);
                                                    }else {
                                                        String oldgrpavnce = "0";
                                                        double oldgrpadv = Double.parseDouble(oldgrpavnce);
                                                        double newadvance = oldgrpadv - newadvancepayment;
                                                        String updatedgrpadv = String.valueOf(newadvance);
                                                        updategrpfinc.child("currentadvance").setValue(updatedgrpadv);

                                                        DatabaseReference meettrans = mDatabase.child(grpid).child("schoolfees").child("meetings").child(meeetid).child("advances").child("c2b").push();
                                                        meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                        meettrans.child("membername").setValue(nname);
                                                        meettrans.child("memberid").setValue(user);
                                                        meettrans.child("advancepaid").setValue(advancepaymen);
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


                Toast.makeText(schoolfeespayment.this, "Payment Processed!", Toast.LENGTH_LONG).show();

                Intent home = new Intent(schoolfeespayment.this, memberlist.class);
                home.putExtra("key", grpid);
                startActivity(home);
                finish();
            } else {
                Toast.makeText(schoolfeespayment.this, "Enter All Values..", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.e("Exception",String.valueOf(e));
        }

    }

    private void confirm() {
        mD.child("Mpesa").child("Received").child(ngroup).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            DatabaseReference newMpesa = mD.child("Mpesa").child("Received").child(grpid).child(var1);
                            newMpesa.child("Status").setValue("Processed");

                            Toast.makeText(schoolfeespayment.this, "Processed!", Toast.LENGTH_LONG).show();
                            genRCPT(am, user, date);

                        } else {
                            Toast.makeText(schoolfeespayment.this, "Payment Not Found!", Toast.LENGTH_LONG).show();
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
        final DatabaseReference nr = mD.child("Receipts").child(grpid).child(meeetid).child("Mpesa").child(memid);
        DatabaseReference nnr = nr.child("details").push();
        nnr.child("amount").setValue(amount);
        nr.child("date").setValue(date);
        nr.child("name").setValue(nname);
        mD.child("Employees").child(ops).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                facname = dataSnapshot.child("name").getValue().toString();
                nr.child("officer").setValue(facname);
                Toast.makeText(schoolfeespayment.this, "Receipt Generated!", Toast.LENGTH_LONG).show();
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(Calendar.DATE, 42); //Adding30days
        String output = sdf.format(c.getTime());

        return output;
    }
}